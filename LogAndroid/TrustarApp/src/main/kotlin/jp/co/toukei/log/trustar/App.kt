package jp.co.toukei.log.trustar

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.distribute.Distribute
import io.reactivex.rxjava3.exceptions.UndeliverableException
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import jp.co.toukei.log.lib.Const
import jp.co.toukei.log.lib.deleteQuickly
import jp.co.toukei.log.lib.makeDirs
import splitties.systemservices.notificationManager

class App : Application() {

    init {
        RxJavaPlugins.setErrorHandler {
            if (it !is UndeliverableException) throw it
        }
    }

    override fun onCreate() {
        super.onCreate()
        //init on main thread!!!
        Config.tmpDir.apply {
            deleteQuickly()
            makeDirs()
        }
        setUncaughtExceptionHandler()
        if (!Const.API_PRE_26) initNotificationChannel()
        if (!BuildConfig.isDebug) initAppCenter()
    }

    private fun setUncaughtExceptionHandler() {
        val h = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { t, e ->
            runCatching { Config.logException(e, Current.user?.userInfo) }
            h?.uncaughtException(t, e)
        }
    }

    private fun initAppCenter() {
        AppCenter.start(
            this,
            BuildConfig.appCenterKey,
            Analytics::class.java,
            Distribute::class.java
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initNotificationChannel() {
        val l = NotificationChannel(
            Config.LocationNotificationChannelId,
            "Location",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            enableVibration(false)
            enableLights(false)
            setShowBadge(false)
        }
        val p = NotificationChannel(
            Config.SettingsNotificationChannelId,
            "Settings",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            enableVibration(true)
            enableLights(true)
            setShowBadge(true)
        }
        val d = NotificationChannel(
            Config.downloadNotificationChannelId,
            getString(R.string.download),
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            enableVibration(false)
            enableLights(false)
            setShowBadge(false)
        }
        val m = NotificationChannel(
            Config.messageNotificationChannelId,
            getString(R.string.navigation_message),
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            enableVibration(true)
            enableLights(true)
            setShowBadge(true)
        }

        notificationManager.createNotificationChannels(listOf(l, p, d, m))
    }
}
