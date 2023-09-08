@file:Suppress("NOTHING_TO_INLINE")

package jp.co.toukei.log.trustar

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Notification
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.location.Location
import android.net.Uri
import android.provider.BaseColumns
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.provider.Settings
import android.util.Base64
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.stfalcon.imageviewer.StfalconImageViewer
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import jp.co.toukei.log.lib.buildNotification
import jp.co.toukei.log.lib.child
import jp.co.toukei.log.lib.common.backgroundColorResId
import jp.co.toukei.log.lib.common.boldTypeface
import jp.co.toukei.log.lib.common.gravityCenterVertical
import jp.co.toukei.log.lib.common.materialAlertDialogBuilder
import jp.co.toukei.log.lib.common.nestedScrollView
import jp.co.toukei.log.lib.common.setLayoutParams
import jp.co.toukei.log.lib.common.snackbar
import jp.co.toukei.log.lib.common.sp
import jp.co.toukei.log.lib.common.textColor
import jp.co.toukei.log.lib.common.whiteText
import jp.co.toukei.log.lib.common.withAlpha
import jp.co.toukei.log.lib.disabledStateDrawable
import jp.co.toukei.log.lib.expand
import jp.co.toukei.log.lib.gradientDrawable
import jp.co.toukei.log.lib.gradientDrawableBorder
import jp.co.toukei.log.lib.millisAfterOffset
import jp.co.toukei.log.lib.permissionResultCheck
import jp.co.toukei.log.lib.rippleDrawable
import jp.co.toukei.log.lib.rippleDrawableBorder
import jp.co.toukei.log.lib.room.AbstractSync
import jp.co.toukei.log.lib.room.CommonSyncDao
import jp.co.toukei.log.lib.singleViewBlock
import jp.co.toukei.log.lib.startAppSettings
import jp.co.toukei.log.lib.util.SugoiAdapter
import jp.co.toukei.log.lib.util.TextDrawable
import jp.co.toukei.log.lib.value
import jp.co.toukei.log.trustar.common.UserFileProvider
import jp.co.toukei.log.trustar.db.user.entity.BinStatus
import jp.co.toukei.log.trustar.db.user.entity.WorkStatus
import jp.co.toukei.log.trustar.glide.GlideApp
import jp.co.toukei.log.trustar.glide.ImageURI
import jp.co.toukei.log.trustar.other.Weather
import jp.co.toukei.log.trustar.rest.response.LoginResponse
import jp.co.toukei.log.trustar.rest.response.StateResult
import jp.co.toukei.log.trustar.user.UserInfo
import jp.co.toukei.log.trustar.user.UserWithToken
import okhttp3.OkHttpClient
import org.json.JSONArray
import org.json.JSONObject
import splitties.dimensions.dip
import splitties.resources.color
import splitties.resources.drawable
import splitties.systemservices.powerManager
import splitties.views.centerText
import splitties.views.dsl.core.add
import splitties.views.dsl.core.editText
import splitties.views.dsl.core.horizontalLayout
import splitties.views.dsl.core.imageView
import splitties.views.dsl.core.lParams
import splitties.views.dsl.core.matchParent
import splitties.views.dsl.core.textView
import splitties.views.dsl.core.wrapContent
import splitties.views.horizontalPadding
import splitties.views.padding
import splitties.views.textColorResource
import splitties.views.verticalPadding
import third.Clock
import third.Result
import third.jsonObj
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.Date
import java.util.Locale
import java.util.Optional
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.X509TrustManager
import kotlin.math.roundToInt


fun Context.primaryBackground(): GradientDrawable {
    return GradientDrawable(
        GradientDrawable.Orientation.BOTTOM_TOP,
        intArrayOf(
            getColor(R.color.colorPrimaryDark),
            getColor(R.color.colorPrimary)
        )
    )
}

inline fun TextView.primaryColorText() {
    textColorResource = R.color.colorPrimary
}

fun TextView.primaryButtonStyle() {
    roundedPrimaryColorBackground()
    bigButtonPadding()
    boldTypeface()
    whiteText()
    centerText()
    textSize = 16F
}

fun TextView.whiteButtonStyle() {
    textSize = 16F
    primaryColorText()
    roundedWhiteBackground()
    bigButtonPadding()
    boldTypeface()
    centerText()
}

fun TextView.whiteButtonSmallStyle() {
    textSize = 12F
    primaryColorText()
    roundedWhiteBackground()
    smallButtonPadding()
    boldTypeface()
    centerText()
}

fun TextView.primaryButtonSmallStyle() {
    roundedPrimaryColorBackground()
    smallButtonPadding()
    boldTypeface()
    whiteText()
    centerText()
    textSize = 12F
}

fun TextView.primaryGroundColor(radius: Int) {
    val colorPrimary = context.getColor(R.color.colorPrimary)
    background = gradientDrawable(radius, colorPrimary)
    whiteText()
}

fun TextView.primaryGroundColorRipple(radius: Int) {
    val colorPrimary = context.getColor(R.color.colorPrimary)
    background = disabledStateDrawable(
        gradientDrawable(radius, colorPrimary.withAlpha(0x66)),
        rippleDrawable(radius, colorPrimary, Color.WHITE)
    )
    whiteText()
}

fun TextView.bigButtonPadding() {
    verticalPadding = dip(12)
    horizontalPadding = dip(32)
}

fun TextView.middleButtonPadding() {
    verticalPadding = dip(6)
    horizontalPadding = dip(20)
}

fun TextView.smallButtonPadding() {
    verticalPadding = dip(4)
    horizontalPadding = dip(16)
}

fun TextView.primaryLabel() {
    verticalPadding = dip(4)
    horizontalPadding = dip(8)
    boldTypeface()
    whiteText()
    backgroundColorResId = R.color.colorPrimary
    textSize = 14F
}

fun View.roundedWhiteBackground() {
    background = disabledStateDrawable(
        gradientDrawable(Int.MAX_VALUE, Color.WHITE.withAlpha(0x66)),
        rippleDrawable(Int.MAX_VALUE, Color.WHITE)
    )
}

fun View.roundedPrimaryColorBackground() {
    val color = context.getColor(R.color.colorPrimary)
    background = disabledStateDrawable(
        gradientDrawable(Int.MAX_VALUE, color.withAlpha(0x66)),
        rippleDrawable(Int.MAX_VALUE, color, Color.WHITE)
    )
}

fun Context.withGlide(): RequestManager {
    // you need compile first.
    return GlideApp.with(this)
}

fun View.withGlide(): RequestManager {
    return context.withGlide()
}

@SuppressLint("BatteryLife")
fun Activity.requireIgnoringBatteryOptimizations(requestCode: Int): Boolean {
    val pkg = packageName
    if (powerManager.isIgnoringBatteryOptimizations(pkg)) return true

    startActivityForResult(Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
        data = "package:$pkg".toUri()
    }, requestCode)
    return false
}

fun bottomSheetListenerForExpand(): DialogInterface.OnShowListener {
    return DialogInterface.OnShowListener {
        val v = (it as? BottomSheetDialog)?.findViewById<View>(R.id.design_bottom_sheet)
        if (v != null) {
            val behavior = try {
                BottomSheetBehavior.from(v)
            } catch (e: Exception) {
                null
            }
            behavior?.expand()
        }
    }
}

fun BottomSheetDialog.findBottomSheetLayoutParams(): ViewGroup.LayoutParams? {
    return findViewById<View>(R.id.design_bottom_sheet)?.layoutParams
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

inline fun BinStatus?.ready() = this?.binStatusCd == 0
inline fun BinStatus?.isWorking() = this?.binStatusCd == 1
inline fun BinStatus?.started() = this != null && binStatusCd >= 1
inline fun BinStatus?.hasFinished() = this?.binStatusCd == 2

inline fun WorkStatus.ready() = workStatusCd == 0
inline fun WorkStatus.isMoving() = workStatusCd == 1
inline fun WorkStatus.isWorking() = workStatusCd == 2
inline fun WorkStatus.hasFinished() = workStatusCd == 3

inline fun WorkStatus.Type?.ready() = this === WorkStatus.Type.Ready
inline fun WorkStatus.Type?.isMoving() = this === WorkStatus.Type.Moving
inline fun WorkStatus.Type?.isWorking() = this === WorkStatus.Type.Working
inline fun WorkStatus.Type?.isWorkingOrMoving() = isMoving() || isWorking()
inline fun WorkStatus.Type?.hasFinished() = this === WorkStatus.Type.Finished


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
    return Config.GMT9.run {
        displayHHmmRange(
            Clock(millisAfterOffset(from)),
            to?.let { Clock(millisAfterOffset(it)) }
        )
    }
}

fun Context.notificationOfBatterySaverIsOn(requestCode: Int): Notification {
    return buildNotification(Config.SettingsNotificationChannelId) {
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
                this@notificationOfBatterySaverIsOn,
                requestCode,
                Intent(Settings.ACTION_BATTERY_SAVER_SETTINGS),
                PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}

fun DialogFragment.showAt(fragmentActivity: FragmentActivity) {
    val fm = fragmentActivity.supportFragmentManager
    val tag = javaClass.name
    show(fm, tag)
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

fun Context.compoundDrawable(
    @DrawableRes res: Int,
    @ColorInt srcAtopColor: Int,
    bound: Int,
): Drawable? {
    return drawable(res)?.mutate()?.apply {
        colorFilter = PorterDuffColorFilter(srcAtopColor, PorterDuff.Mode.SRC_ATOP)
        setBounds(0, 0, bound, bound)
    }
}

fun <T : Any> Single<StateResult>.mapByState(value: T): Single<Result<T>> {
    return map { it.toResult(value) }
}

inline fun JSONArray.forEachOptObject(block: (Int, JSONObject?) -> Unit) {
    (0 until length()).forEach {
        block(it, optJSONObject(it))
    }
}

fun Context.dialogForEdit(
    title: Int,
    editTextInit: EditText.() -> Unit,
    callback: (String?) -> Unit,
) {
    val edit: EditText
    val view = nestedScrollView {
        padding = dip(16)
        edit = add(editText {
            background =
                gradientDrawableBorder(dip(1), Color.WHITE, getColor(R.color.gray_out), dip(1))
            gravity = Gravity.TOP
            textSize = 14F
            imeOptions = EditorInfo.IME_ACTION_DONE
            setSelectAllOnFocus(true)
            editTextInit(this)
        }, lParams(matchParent, wrapContent))
    }
    val d = materialAlertDialogBuilder {
        setTitle(title)
        setView(view)
        setPositiveButton(R.string.confirm) { _, _ -> callback(edit.text?.toString()) }
        setNegativeButton(R.string.cancel, null)
    }.create()
    d.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
    d.show()
    edit.requestFocus()
}

fun Double.roundToStringDecimal1(): String {
    val round1 = (this * 10).roundToInt() / 10.0
    val intValue = round1.toInt()
    return if (round1 - intValue == 0.0) "$intValue" else "$round1"
}

// good performance
fun UserInfo.preventAutoModeDialog(): Boolean {
    return homeDir.child(".prevent_am_dialog").exists()
}

fun UserInfo.setPreventAutoModeDialog() {
    runCatching { homeDir.child(".prevent_am_dialog").createNewFile() }
}

fun textViewOpenMapsOnClick(): View.OnClickListener {
    return View.OnClickListener {
        val t = it as? TextView
        val content = t?.text?.toString()?.trim()
        if (!content.isNullOrEmpty()) {
            val ctx = t.context
            val query = Uri.parse("geo:0,0?q=" + Uri.encode(content))
            val intent = Intent(Intent.ACTION_VIEW, query)
                .apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
//                .run { Intent.createChooser(this, null) }
            runCatching { ctx.startActivity(intent) }
        }
    }
}

fun currentUserInfoClient(): JSONObject {
    return jsonObj {
        "user" v Current.userInfo.jsonObject()
    }
}

fun String.imageURI(): ImageURI {
    return ImageURI(toUri(), true)
}

fun Single<StateResult>.toCompletable(): Completable {
    return flatMapCompletable(StateResult::toCompletable)
}

fun Single<StateResult>.toState(): Single<Optional<String>> {
    return flatMap(StateResult::toSingleOptionalMessage)
}

fun Single<StateResult>.toSingleMessage(): Single<String> {
    return flatMap(StateResult::toSingleMessage)
}

fun Context.alertMsg(@StringRes msg: Int) {
    alertMsg(msg) {}
}

inline fun Context.alertMsg(@StringRes msg: Int, init: MaterialAlertDialogBuilder.() -> Unit) {
    MaterialAlertDialogBuilder(this)
        .setMessage(msg)
        .setPositiveButton(android.R.string.ok, null)
        .apply(init)
        .show()
}

inline fun Context.alertMsg(msg: CharSequence, init: MaterialAlertDialogBuilder.() -> Unit) {
    MaterialAlertDialogBuilder(this)
        .setMessage(msg)
        .setPositiveButton(android.R.string.ok, null)
        .apply(init)
        .show()
}

fun Context.listItemDrawable(): Drawable {
    return rippleDrawable(0, getColor(R.color.listItem))
}

fun groupBlock(
    adapter: SugoiAdapter?,
    block: SugoiAdapter.Block<*>,
    textViewInit: TextView.() -> Unit,
): SugoiAdapter.Block<*> {
    return singleViewBlock {
        horizontalLayout {
            weightSum = 1F
            setLayoutParams()
            gravityCenterVertical()
            background = context.listItemDrawable()
            val tColor = context.getColor(R.color.textColor)

            padding = dip(16)
            add(textView {
                textColor = tColor
                textViewInit()
            }, lParams(0, wrapContent, weight = 1F))
            val img = add(
                imageView {
                    setImageResource(R.drawable.round_expand_more_24)
                    setColorFilter(tColor)
                }, lParams(dip(24), dip(24))
            )
            val l = object : View.OnClickListener, (Boolean, SugoiAdapter.Block<*>) -> Unit {
                override fun onClick(v: View?) {
                    block.offline = !block.offline
                    a()
                }

                private fun a() {
                    img.animate().rotation(if (block.offline) 0F else 180F)
                }

                override fun invoke(p1: Boolean, p2: SugoiAdapter.Block<*>) {
                    if (p2 === block) a()
                }

                init {
                    a()
                }
            }
            setOnClickListener(l)
            adapter?.addOfflineListener(l)
        }
    }
}

fun groupBlock1(@StringRes text: Int, block: SugoiAdapter.Block<*>): SugoiAdapter.Block<*> {
    return groupBlock(null, block) {
        setText(text)
        horizontalPadding = dip(4)
    }
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


fun Context.selectableItemBackground(): Drawable? {
    val t = obtainStyledAttributes(intArrayOf(android.R.attr.selectableItemBackground))
    val d = t.getResourceId(0, 0)
    t.recycle()
    return AppCompatResources.getDrawable(this, d)
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

fun TextView.setTextObserver(): Observer<in CharSequence> {
    return Observer<CharSequence> {
        text = it
    }
}

fun Toolbar.addRightDateText(): TextView {
    return add(
        textView {
            textColorResource = R.color.textDarkBold
            textSize = 14F
            centerText()
        }, Toolbar.LayoutParams(wrapContent, wrapContent, Gravity.END)
    )
}

fun Toolbar.setOldStyle() {
    setTitle(R.string.trustar)
    setTitleTextAppearance(context, R.style.MainToolbar)
    layoutParams?.height = context.dip(40)
}

fun Fragment.openFragmentFadeAnimate(
    activity: FragmentActivity,
    id: Int,
    backName: String? = null,
) {
    activity.supportFragmentManager.beginTransaction()
        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
        .add(id, this)
        .addToBackStack(backName)
        .commit()
}

fun Fragment.openFragmentNoAnimate(
    activity: FragmentActivity,
    id: Int,
    backName: String? = null,
) {
    activity.supportFragmentManager.beginTransaction()
        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        .add(id, this)
        .addToBackStack(backName)
        .commit()
}

fun TextView.roundedSelectorStyle() {
    boldTypeface()
    textSize = 16F
    val colorPrimary = context.color(R.color.colorPrimary)
    textColor = colorPrimary
    verticalPadding = dip(12)
    horizontalPadding = dip(16)
    background = rippleDrawableBorder(Int.MAX_VALUE, Color.WHITE, colorPrimary, dip(2))
    compoundDrawablePadding = dip(4)
    val d = context.drawable(R.drawable.baseline_expand_more_24)?.mutate()?.apply {
        colorFilter = PorterDuffColorFilter(colorPrimary, PorterDuff.Mode.SRC_ATOP)
        setBounds(0, 0, context.dip(20), context.dip(20))
    }
    setCompoundDrawables(null, null, d, null)
}

fun AlertDialog.positiveButton(): Button? {
    return getButton(DialogInterface.BUTTON_POSITIVE)
}

fun Fragment.showFullScreen(activity: FragmentActivity, id: Int) {
    activity.supportFragmentManager.beginTransaction()
        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        .add(id, this)
        .addToBackStack(null)
        .commit()
}

@SuppressLint("RestrictedApi")
fun View.requestFocusAndShowKeyboard() {
    com.google.android.material.internal.ViewUtils.requestFocusAndShowKeyboard(this)
}

@SuppressLint("RestrictedApi")
fun View.postRequestFocusAndShowKeyboard() {
    post(::requestFocusAndShowKeyboard)
}

fun View.parentView(): View? {
    return parent as? View
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
