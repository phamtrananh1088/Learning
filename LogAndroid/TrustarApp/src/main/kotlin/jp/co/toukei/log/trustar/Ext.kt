@file:Suppress("NOTHING_TO_INLINE")

package jp.co.toukei.log.trustar

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ClipData
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.PowerManager
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.BaseColumns
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.provider.Settings
import android.util.Base64
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.RequiresPermission
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.stfalcon.imageviewer.StfalconImageViewer
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import jp.co.toukei.log.lib.Const
import jp.co.toukei.log.lib.buildNotification
import jp.co.toukei.log.lib.child
import jp.co.toukei.log.lib.common.snackbar
import jp.co.toukei.log.lib.common.sp
import jp.co.toukei.log.lib.common.whiteText
import jp.co.toukei.log.lib.common.withAlpha
import jp.co.toukei.log.lib.disabledStateDrawable
import jp.co.toukei.log.lib.gradientDrawable
import jp.co.toukei.log.lib.millisAfterOffset
import jp.co.toukei.log.lib.permissionResultCheck
import jp.co.toukei.log.lib.rippleDrawable
import jp.co.toukei.log.lib.room.AbstractSync
import jp.co.toukei.log.lib.room.CommonSyncDao
import jp.co.toukei.log.lib.startAppSettings
import jp.co.toukei.log.lib.unixTimeDaysBetween
import jp.co.toukei.log.lib.util.TextDrawable
import jp.co.toukei.log.lib.value
import jp.co.toukei.log.trustar.common.UserFileProvider
import jp.co.toukei.log.trustar.db.user.entity.BinStatus
import jp.co.toukei.log.trustar.db.user.entity.WorkStatus
import jp.co.toukei.log.trustar.glide.ImageURI
import jp.co.toukei.log.trustar.other.Weather
import jp.co.toukei.log.trustar.rest.response.LoginResponse
import jp.co.toukei.log.trustar.rest.response.StateResult
import jp.co.toukei.log.trustar.user.UserInfo
import jp.co.toukei.log.trustar.user.UserWithToken
import okhttp3.OkHttpClient
import org.json.JSONArray
import org.json.JSONObject
import splitties.systemservices.clipboardManager
import third.Clock
import third.jsonObj
import java.math.BigDecimal
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.Date
import java.util.Locale
import java.util.Optional
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.X509TrustManager


fun TextView.primaryGroundColorRipple(radius: Int) {
    val colorPrimary = context.getColor(R.color.colorPrimary)
    background = disabledStateDrawable(
        gradientDrawable(radius, colorPrimary.withAlpha(0x66)),
        rippleDrawable(radius, colorPrimary, Color.WHITE)
    )
    whiteText()
}

fun Context.withGlide(): RequestManager {
    // you need compile first.
    return Glide.with(this)
}

fun View.withGlide(): RequestManager {
    return context.withGlide()
}


fun String?.colorFromString(noColor: Int = Color.TRANSPARENT): Int {
    this ?: return noColor
    val match = Config.rgbStringRegex.matchEntire(this) ?: return noColor
    val v = match.groupValues
    val b = v.getOrNull(3)?.toIntOrNull() ?: return noColor
    val g = v[2].toIntOrNull() ?: return noColor
    val r = v[1].toIntOrNull() ?: return noColor
    return Color.rgb(r and 0xff, g and 0xff, b and 0xff)
}

fun String?.dateFromStringT(): Date? {
    this ?: return null
    return Config.dateFormatter.parse(this)
}

fun String?.dateFromString2(): Date? {
    this ?: return null
    return Config.dateFormatter2.parse(this)
}

fun String.hms60From4dig(default: Int? = null): Int? {
    if (length == 4) {
        val m1 = this[0] - '0'
        val m2 = this[1] - '0'
        if (m1 in 0..9 && m2 in 0..9) {
            val s1 = this[2] - '0'
            val s2 = this[3] - '0'
            if (s1 in 0..9 && s2 in 0..9)
                return (m1 * 10 + m2) * 60 + s1 * 10 + s2
        }
    }
    return default
}

fun String.tryBase64Decode(flags: Int): ByteArray? {
    return try {
        Base64.decode(toByteArray(), flags)
    } catch (e: Throwable) {
        null
    }
}

//todo
fun Throwable.errMessage(): String = message ?: "err"

inline fun BinStatus?.isWorking() = this?.binStatusCd == 1

inline fun WorkStatus.isWorking() = workStatusCd == 2


/**
 * 位置記録を無視の場合
 * @param range 半径
 * @param current 今の位置
 * @param previous 前回の位置
 * @param lastRecorded 最終記録したの位置
 */
fun shouldSkipLocation(
    range: Int,
    current: Location,
    previous: Location?,
    lastRecorded: Location?,
): Boolean {
    if (lastRecorded == null || previous == null) return false
    if (lastRecorded.distanceTo(current) > range) return false
    if (lastRecorded !== previous) return previous.distanceTo(current) <= range
    return true
}

fun shouldSkipCoordinateRecord(
    timeSampleInSec: Int,
    current: Location,
    lastRecorded: Location?,
): Boolean {
    if (lastRecorded == null) return false
    val notLessThan = lastRecorded.time + timeSampleInSec * 1000
    val currentTime = current.time
    return currentTime < notLessThan
}

fun Location.ddString(): String {
    return buildString(30) {
        append(latitude).append(',').append(' ').append(longitude)
    }
}

fun Context.getLastWeather(): Weather? {
    return when (Config.pref.lastWeather) {
        1 -> Weather.Hare
        2 -> Weather.Kumori
        3 -> Weather.Ame
        4 -> Weather.Yuki
        5 -> Weather.HareKumori
        6 -> Weather.KumoriHare
        else -> null
    }
}

/*天候・データ更新処理*/
fun Context.setWeather(weather: Weather) {
    Config.pref.lastWeather = weather.value
}

fun UserInfo.removeLoginData() {
    UserInfo.userJsonFile(this).delete()
}

fun Context.setLastUser(user: UserWithToken, jsonObject: JSONObject? = null) {
    val userInfo = user.userInfo
    Config.pref.setUserToken(user)
    if (jsonObject != null) {
        UserInfo.userJsonFile(userInfo).writeText(jsonObject.toString(), Charsets.UTF_8)
    }
}

fun Context.readUserInfoFromLocal(): UserWithToken? {
    val companyCd = Config.pref.lastUserCompany
    val userId = Config.pref.lastUser
    if (companyCd != null && userId != null) {
        return runCatching {
            LoginResponse.fromJSONObject(
                UserInfo.userJsonFile(companyCd, userId).readText(Charsets.UTF_8)
            )?.toUserWithToken()?.value()
        }.getOrNull()
    }
    return null
}

fun displayHHmmRange(from: Clock, to: Clock?): String? {
    return "${from.hhmm} 〜 ${to?.hhmm.orEmpty()}"
}

fun displayHHmmRange(from: Long, to: Long?): String? {
    return Config.timeZone.run {
        displayHHmmRange(
            Clock(millisAfterOffset(from)),
            to?.let { Clock(millisAfterOffset(it)) }
        )
    }
}

fun NotificationManager.checkPowerSaverNotification(
    ctx: Context,
    requestCode: Int,
    powerManager: PowerManager,
) {
    ctx.apply {
        val id = Config.nidPowerSaverOnOff
        if (powerManager.isPowerSaveMode) {
            notify(id, buildNotification(Config.NotificationChannelIdErr) {
                setContentTitle(getString(R.string.notification_battery_saver_is_on_title))
                setContentText(getString(R.string.notification_battery_saver_is_on_text))
                setSmallIcon(R.drawable.baseline_info_24)
                setOngoing(true)
                setStyle(
                    NotificationCompat.BigTextStyle(this).also {
                        it.setBigContentTitle(getString(R.string.notification_battery_saver_is_on_title))
                        it.bigText(getString(R.string.notification_battery_saver_is_on_big_text))
                    }
                )
                setContentIntent(
                    PendingIntent.getActivity(
                        ctx,
                        requestCode,
                        Intent(Settings.ACTION_BATTERY_SAVER_SETTINGS),
                        PendingIntent.FLAG_IMMUTABLE
                    )
                )
            })
        } else {
            cancel(id)
        }
    }
}

fun NotificationManager.checkLocationOnOffNotification(
    ctx: Context,
    requestCode: Int,
    locationManager: LocationManager,
) {
    ctx.apply {
        val id = Config.nidLocationOnOff
        if (LocationManagerCompat.isLocationEnabled(locationManager)) {
            cancel(id)
        } else {
            notify(id, buildNotification(Config.NotificationChannelIdErr) {
                setContentText(getString(R.string.notification_location_is_off_title))
                setSmallIcon(R.drawable.baseline_info_24)
                setOngoing(true)
                setContentIntent(
                    PendingIntent.getActivity(
                        ctx, requestCode,
                        packageManager.getLaunchIntentForPackage(
                            packageName
                        ),
                        PendingIntent.FLAG_IMMUTABLE
                    )
                )
            })
        }
    }
}

fun currentFormattedDateString(): String {
    return Config.dateFormatter.format(System.currentTimeMillis())
}

fun <T : AbstractSync> CommonSyncDao<T>.setThenGetPending(): List<T> {
    setPending()
    return getPending()
}

fun Context.defaultSizeTextDrawable(@StringRes res: Int): TextDrawable {
    return TextDrawable(
        getString(res),
        sp(32).toFloat(),
        2F,
        0x33000000
    )
}

inline fun JSONArray.forEachOptObject(block: (Int, JSONObject?) -> Unit) {
    (0 until length()).forEach {
        block(it, optJSONObject(it))
    }
}

// good performance
fun UserInfo.preventAutoModeDialog(): Boolean {
    return homeDir.child(".prevent_am_dialog").exists()
}

fun UserInfo.setPreventAutoModeDialog() {
    runCatching { homeDir.child(".prevent_am_dialog").createNewFile() }
}

fun currentUserInfoClient(): JSONObject {
    return jsonObj {
        "user" v Current.userInfo.jsonObject()
    }
}

//todo which api?
fun String.imageURI(): ImageURI {
    return ImageURI(this, true)
}

fun Single<StateResult>.toCompletable(): Completable {
    return flatMapCompletable(StateResult::toCompletable)
}

fun Single<StateResult>.toState(): Single<Optional<String>> {
    return flatMap(StateResult::toSingleOptionalMessage)
}

@RequiresPermission(Manifest.permission.VIBRATE)
fun Vibrator.vibrateCompat(millis: Long) {
    if (Const.API_PRE_26) {
        vibrate(millis)
    } else {
        vibrate(VibrationEffect.createOneShot(millis, VibrationEffect.DEFAULT_AMPLITUDE))
    }
}

inline fun Context.alertMsg(@StringRes msg: Int, init: MaterialAlertDialogBuilder.() -> Unit) {
    MaterialAlertDialogBuilder(this)
        .setMessage(msg)
        .setPositiveButton(android.R.string.ok, null)
        .apply(init)
        .show()
}

fun Throwable.snackbarMessage(view: View) {
    message?.let { view.snackbar(it) }
}

fun Context.getResColor(@ColorRes id: Int): Int = ContextCompat.getColor(this, id)

fun cacheFileShareUri(fileKey: String, name: String): Uri {
    return UserFileProvider.getUriForCacheFile(
        fileKey,
        name,
        "${BuildConfig.APPLICATION_ID}.user_provider"
    )
}

fun localFileShareUri(id: String, name: String): Uri {
    return UserFileProvider.getUriForLocalFile(
        id,
        name,
        "${BuildConfig.APPLICATION_ID}.user_provider"
    )
}

fun Context.startOpenFile(uri: Uri, mime: String?) {
    startActivity(
        Intent.createChooser(
            Intent(Intent.ACTION_VIEW)
                .setDataAndType(uri, mime)
                .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION),
            getString(R.string.open_with)
        )
    )
}

fun Context.startShareFile(uri: Uri) {
    startActivity(
        Intent.createChooser(
            Intent(Intent.ACTION_SEND)
                .putExtra(Intent.EXTRA_STREAM, uri)
                .setType(contentResolver.getType(uri))
                .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION),
            null
        )
    )
}


fun humanReadableSize(value: Long, locale: Locale = Locale.getDefault()): String = when {
    value < 1024 -> "$value B"
    else -> {
        val z = (63 - value.countLeadingZeroBits()) / 10
        String.format(locale, "%.1f %siB", value.toDouble() / (1L shl z * 10), " KMGTPE"[z])
    }
}

fun Uri.displayName(contentResolver: ContentResolver): String? {
    val c = OpenableColumns.DISPLAY_NAME
    return contentResolver.query(this, arrayOf(c), null, null, null)?.use {
        val i = it.getColumnIndex(c)
        it.moveToFirst()
        it.getString(i)
    }
}

fun timeString(): String {
    return Config.timeFormatter.format(System.currentTimeMillis())
}


fun ImageView.showDefaultImageViewer(imageURI: ImageURI) {
    defaultImageViewer(imageURI).show()
}

fun stfalconImageViewerShowImage(context: Context, imageURI: ImageURI) {
    StfalconImageViewer
        .Builder(context, listOf(imageURI)) { view, image ->
            context.withGlide()
                .load(image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .fitCenter()
                .into(view)
        }
        .withBackgroundColor(android.graphics.Color.BLACK)
        .show()
}

fun ImageView.defaultImageViewer(imageURI: ImageURI): StfalconImageViewer.Builder<ImageURI> {
    return StfalconImageViewer
        .Builder(context, listOf(imageURI)) { view, image ->
            withGlide()
                .load(image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .fitCenter()
                .into(view)
        }
        .withBackgroundColor(Color.BLACK)
        .withTransitionFrom(this)
}

/* return true for GRANTED */
fun Activity.defaultPermissionResultCheck(
    permissions: Array<out String>,
    grantResults: IntArray,
    @StringRes msg: Int,
): Boolean {
    val act = this
    val l = object : (Boolean, String) -> Unit, DialogInterface.OnCancelListener,
        DialogInterface.OnClickListener {

        override fun onCancel(dialog: DialogInterface?) {
            finish()
        }

        override fun onClick(dialog: DialogInterface?, which: Int) {
            startAppSettings()
        }

        override fun invoke(p1: Boolean, p2: String) {
            if (p1) {
                finish()
            } else {
                MaterialAlertDialogBuilder(act)
                    .setMessage(msg)
                    .setPositiveButton(android.R.string.ok, this)
                    .setOnCancelListener(this)
                    .show()
            }
        }
    }
    return permissionResultCheck(permissions, grantResults, l)
}

fun ContentResolver.queryMediaImages(): List<Uri> {
    val uris = mutableListOf<Uri>()
    query(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        null, null, null,
        MediaStore.MediaColumns.DATE_MODIFIED + " desc"
    )?.use {
        val idC = it.getColumnIndex(BaseColumns._ID)
        if (idC >= 0)
            while (it.moveToNext()) {
                uris += ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    it.getLong(idC)
                )
            }
    }
    return uris
}

@SuppressLint("RestrictedApi")
fun View.requestFocusAndShowKeyboard() {
    com.google.android.material.internal.ViewUtils.requestFocusAndShowKeyboard(this)
}

@SuppressLint("BadHostnameVerifier", "CustomX509TrustManager")
fun OkHttpClient.Builder.insecureSSL() {
    if (Config.isProductRelease) {
//        throw UnknownError("not for production release.")
        return
    }
    val o = object : X509TrustManager, HostnameVerifier {
        override fun checkClientTrusted(chain: Array<X509Certificate>?, authType: String?) = Unit
        override fun checkServerTrusted(chain: Array<X509Certificate>?, authType: String?) = Unit
        override fun getAcceptedIssuers(): Array<X509Certificate> = emptyArray()
        override fun verify(hostname: String?, session: SSLSession?): Boolean = true
    }
    sslSocketFactory(
        SSLContext.getInstance("SSL")
            .apply { init(null, arrayOf(o), SecureRandom()) }
            .socketFactory,
        o
    )
    hostnameVerifier(o)
}

fun japanTelRegex(): Regex {
    return Regex(
        listOf(
            "0[1-9]" to "\\d{4}",
            "0[1-9]\\d" to "\\d{3}",
            "0[1-9]\\d{2}" to "\\d{2}",
            "0[1-9]\\d{3}" to "\\d",
            "0[2-9]0" to "\\d{4}",
        ).flatMap { (s1, s2) ->
            listOf(
                "$s1$s2\\d{4}",
                "$s1-$s2-\\d{4}",
                "\\($s1\\)$s2-\\d{4}",
                "$s1\\($s2\\)\\d{4}",
            )
        }.joinToString("|") { "((?<!\\d)$it(?!\\d))" },
        RegexOption.MULTILINE
    )
}

fun binKilometerInputValidation(): (String, BigDecimal) -> Boolean {
    return { s, _ -> (s.toIntOrNull() ?: -1) in 0..9_999_999 }
}

fun Boolean.toInt(): Int = if (this) 1 else 0

fun Long.dateTimeFormat(currentDate: Long = System.currentTimeMillis()): String {
    return if (Config.timeZone.unixTimeDaysBetween(this, currentDate) == 0) {
        Clock(Config.timeZone.millisAfterOffset(this)).hhmm
    } else {
        Config.mmddFormatter.format(this)
    }
}

fun copyToClipboard(text: String) {
    clipboardManager.setPrimaryClip(ClipData.newPlainText("", text))
}
