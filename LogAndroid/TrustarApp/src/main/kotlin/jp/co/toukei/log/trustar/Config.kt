package jp.co.toukei.log.trustar

import android.graphics.Color
import android.os.Environment
import android.preference.PreferenceManager
import androidx.core.content.edit
import androidx.room.Room
import com.squareup.moshi.Moshi
import io.reactivex.rxjava3.schedulers.Schedulers
import jp.co.toukei.log.lib.child
import jp.co.toukei.log.lib.createTempFileInDir
import jp.co.toukei.log.lib.ctx.Ctx
import jp.co.toukei.log.lib.getAndroidId
import jp.co.toukei.log.lib.makeDirs
import jp.co.toukei.log.lib.moshi.CustomMoshiAdapterFactory
import jp.co.toukei.log.lib.moshi.RetrofitConverter
import jp.co.toukei.log.lib.moshi.RetrofitOptionalConverter
import jp.co.toukei.log.lib.retrofit.DefaultAPIException
import jp.co.toukei.log.lib.retrofit.RxCallAdapterWrapperFactory
import jp.co.toukei.log.lib.util.JsonBodyConverter
import jp.co.toukei.log.lib.util.NetworkState
import jp.co.toukei.log.lib.util.RetrofitResponseConverter
import jp.co.toukei.log.lib.util.ReuseDateFormatter
import jp.co.toukei.log.lib.util.Singleton
import jp.co.toukei.log.trustar.common.FilesInDir
import jp.co.toukei.log.trustar.db.image.ImageSendingDB
import jp.co.toukei.log.trustar.db.result.db.ResultDB
import jp.co.toukei.log.trustar.other.DateDetector
import jp.co.toukei.log.trustar.user.ClientInfo
import jp.co.toukei.log.trustar.user.UserInfo
import jp.co.toukei.log.trustar.user.UserWithToken
import okhttp3.Call
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import third.jsonObj
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintWriter
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong

object Config {

    @JvmField
    val locale: Locale = Locale.JAPAN.apply {
        Locale.setDefault(this)
    }

    @JvmField
    val timeZone: TimeZone = TimeZone.getTimeZone("Asia/Tokyo")

    @JvmField
    val androidId = Ctx.context.getAndroidId()
        ?.takeUnless(String::isEmpty)
        ?: throw IllegalArgumentException()

    @JvmField
    val cacheDir: File = Ctx.context.cacheDir

    private val filesDir: File = Ctx.context.filesDir

    val userDir: File
        get() = filesDir.child("user").makeDirs()

    val userDirInCache: File
        get() = cacheDir.child("user").makeDirs()

    @JvmField
    val tmpDir: File = cacheDir.child("tmp")

    private val errLogDir = cacheDir.child("error_log").makeDirs()

    val errLogList: Array<File>?
        get() = errLogDir.listFiles()

    @JvmField
    val localGalleryDir = run {
        Ctx.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) ?: filesDir
    }.child("gallery")

    @JvmField
    val localVideoDir = run {
        Ctx.context.getExternalFilesDir(Environment.DIRECTORY_MOVIES) ?: filesDir
    }.child("video")

    fun logException(e: Throwable, userInfo: UserInfo?) {
        val stack = ByteArrayOutputStream().apply {
            PrintWriter(writer(Charsets.UTF_8)).use {
                e.printStackTrace(it)
                val additional = jsonObj {
                    "user" v userInfo?.jsonBody()
                    "device_date" v currentFormattedDateString()
                    "client" v ClientInfo().jsonBody()
                }.toString('\t'.code)
                it.println()
                it.println(additional)
            }
        }.toByteArray()
        val prefix = userInfo?.run { "${companyCd}_${userId}_" }
        errLogDir.makeDirs().createTempFileInDir(prefix ?: "err_", suffix = ".txt")
            .outputStream()
            .buffered()
            .use {
                it.write(stack)
            }
    }

    val networkState = NetworkState().also { it.register() }

    private val resultDb_ = object : Singleton<ResultDB>(ResultDB::class.java) {

        override fun create(): ResultDB {
            return Room.databaseBuilder(
                Ctx.context,
                ResultDB::class.java,
                "result_db2"
            ).fallbackToDestructiveMigration().build()
        }

        override fun onCleared(t: ResultDB) {
            t.close()
        }
    }
    private val imageDB_ = object : Singleton<ImageSendingDB>(ImageSendingDB::class.java) {

        override fun create(): ImageSendingDB {
            return Room.databaseBuilder(
                Ctx.context,
                ImageSendingDB::class.java,
                "image_sending_db"
            ).fallbackToDestructiveMigration().build()
        }

        override fun onCleared(t: ImageSendingDB) {
            t.close()
        }
    }

    val resultDb: ResultDB
        get() = resultDb_.getInstance()

    val imageDB: ImageSendingDB
        get() = imageDB_.getInstance()


    const val ACTION_DOWNLOADED = "ACTION_DOWNLOADED"
    const val ACTION_DOWNLOADED_EXT_FILE_KEY = "ACTION_DOWNLOADED_EXT_FILE_KEY"


    @JvmField
    val rgbStringRegex = Regex("^\\s*(\\d+)\\s*,\\s*(\\d+)\\s*,\\s*(\\d+)\\s*\$")

    @JvmField
    val commonMoshi: Moshi = Moshi.Builder()
        .add(CustomMoshiAdapterFactory())
        .build()

    private val noCacheHttpClient: Call.Factory = OkHttpClient.Builder()
        .cache(null)
        .retryOnConnectionFailure(false)
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .addNetworkInterceptor { chain ->
            chain.proceed(chain.request()).also {
                if (it.code == 401) {
                    App.broadcastLogout()
                }
            }
        }
        .apply {
            if (BuildConfig.isDebug) {
//                addNetworkInterceptor(okhttp3.logging.HttpLoggingInterceptor().also {
//                    it.level = okhttp3.logging.HttpLoggingInterceptor.Level.BODY
//                })
                insecureSSL()
            }
        }
        .build()

    val baseRetrofit: Retrofit = Retrofit.Builder()
        .callFactory(noCacheHttpClient)
        .addCallAdapterFactory(
            RxCallAdapterWrapperFactory(
                commonMoshi,
                RxJava3CallAdapterFactory.createWithScheduler(Schedulers.io()),
                DefaultAPIException::class
            )
        )
        .addConverterFactory(RetrofitResponseConverter())
        .addConverterFactory(JsonBodyConverter())
        .addConverterFactory(RetrofitOptionalConverter())
        .addConverterFactory(RetrofitConverter(commonMoshi))
        .baseUrl(BuildConfig.baseUrl)
        .build()

    const val isProductRelease = BuildConfig.isProduct && !BuildConfig.isDebug

    @JvmField
    val delayColorRank = listOf(
        Color.RED,
        Color.rgb(255, 176, 97),
        Color.rgb(255, 176, 97)
    )


    @JvmField
    val dateFormatter = ReuseDateFormatter("yyyy-MM-dd'T'HH:mm:ss", locale, timeZone)

    @JvmField
    val dateFormatter2 = ReuseDateFormatter("yyyy/MM/dd HH:mm:ss", locale, timeZone)

    @JvmField
    val dateFormatter3 = ReuseDateFormatter("yyyy/MM/dd HH:mm", locale, timeZone)

    @JvmField
    val dateFormatterMMddHHmm = ReuseDateFormatter("MM月dd日 HH:mm", locale, timeZone)

    @JvmField
    val mmddFormatter = ReuseDateFormatter("MM/dd", locale, timeZone)

    @JvmField
    val dateFormatterMMdde = ReuseDateFormatter("MM月dd日（E）", locale, timeZone)

    @JvmField
    val dateFormatter4 = ReuseDateFormatter("M月d日 E曜日", locale, timeZone)

    @JvmField
    val timeFormatter = ReuseDateFormatter("yyyyMMddHHmmss", locale, timeZone)

    @JvmField
    val dateFormatterForChat = ReuseDateFormatter("yyyy年M月d日 E曜日 HH:mm", locale, timeZone)
    val dateFormatterSensorCsv = ReuseDateFormatter("yyyy-MM-dd HH:mm:ss.SSS", locale, timeZone)

    @JvmField
    val dateFormatter5 = ReuseDateFormatter("yyyy年MM月dd日 E", locale, timeZone)

    @JvmField
    val dateRegex = DateDetector(timeZone)

    class Pref {

        private val preferences = PreferenceManager.getDefaultSharedPreferences(Ctx.context)

        val lastUser: String?
            get() = preferences.getString(KEY_LAST_USER, null)

        val lastApiKey: String?
            get() = preferences.getString(KEY_LAST_API_KEY, null)

        val lastUserCompany: String?
            get() = preferences.getString(KEY_LAST_USER_COMPANY, null)
        var lastWeather: Int
            get() = preferences.getInt(KEY_LAST_WEATHER, -1)
            set(value) {
                preferences.edit(true) {
                    putInt(KEY_LAST_WEATHER, value)
                }
            }

        val uid: String?
            get() = preferences.getString(KEY_USER_UID, null)

        fun setUserToken(u: UserWithToken) {
            val userInfo = u.userInfo
            preferences.edit(true) {
                putString(KEY_LAST_USER, userInfo.userId)
                putString(KEY_LAST_API_KEY, u.token)
                putString(KEY_LAST_USER_COMPANY, userInfo.companyCd)
                putString(KEY_USER_UID, u.uid)
            }
        }

        private companion object {
            private const val KEY_LAST_USER = "KEY_LAST_USER"
            private const val KEY_LAST_API_KEY = "KEY_LAST_API_KEY"
            private const val KEY_LAST_USER_COMPANY = "KEY_LAST_USER_COMPANY"
            private const val KEY_LAST_WEATHER = "KEY_LAST_WEATHER"
            private const val KEY_USER_UID = "KEY_USER_UID"
        }
    }

    @JvmField
    val pref = Pref()

    @JvmField
    val notificationIdCounter = AtomicInteger(0x20000000)

    @JvmField
    val nidLocationUnavailable = notificationIdCounter.incrementAndGet()

    @JvmField
    val nidLocationOnOff = notificationIdCounter.incrementAndGet()

    @JvmField
    val nidFgs = notificationIdCounter.incrementAndGet()

    @JvmField
    val nidPowerSaverOnOff = notificationIdCounter.incrementAndGet()

    @JvmField
    val nidDownload = notificationIdCounter.incrementAndGet()

    const val NotificationChannelIdLocation = "id1"
    const val NotificationChannelIdErr = "id2"
    const val NotificationChannelIdDownload = "id3"
    const val NotificationChannelIdMessage = "id4"
    const val NotificationChannelIdWarning = "id5"

    @JvmField
    val japanTelRegex = japanTelRegex()

    private val atomicCounter = AtomicLong()

    fun incrementedLong(): Long = atomicCounter.incrementAndGet()


    @JvmField
    val commonChartSyncDir = FilesInDir(filesDir.child("chart"))

}
