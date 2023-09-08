package jp.co.toukei.log.trustar

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.os.Build
import android.os.PowerManager
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import io.reactivex.rxjava3.exceptions.UndeliverableException
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import jp.co.toukei.log.lib.Const
import jp.co.toukei.log.lib.child
import jp.co.toukei.log.lib.deleteQuickly
import jp.co.toukei.log.lib.makeDirs
import jp.co.toukei.log.trustar.deprecated.defaultSharedPreferences
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import splitties.systemservices.notificationManager

class App : Application() {

    init {
        RxJavaPlugins.setErrorHandler {
            if (it !is UndeliverableException) throw it
        }
    }

    override fun onCreate() {
        super.onCreate()
        stageUpdate()
        //init on main thread!!!
        Config.tmpDir.apply {
            deleteQuickly()
            makeDirs()
        }
        if (!Const.API_PRE_26) initNotificationChannel()
        if (!BuildConfig.isDebug) {
            setUncaughtExceptionHandler()
            SplitBuildVariant.initAppCenter(this)
        }
        ContextCompat.registerReceiver(
            this,
            broadcastReceiver,
            intentFilter,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
    }

    private fun setUncaughtExceptionHandler() {
        val h = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { t, e ->
            runCatching { Config.logException(e, Current.user?.userInfo) }
            h?.uncaughtException(t, e)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initNotificationChannel() {
        val b = NotificationChannel(
            Config.NotificationChannelIdLocation,
            getString(R.string.bin_working),
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            enableVibration(false)
            enableLights(false)
            setShowBadge(false)
        }
        val e = NotificationChannel(
            Config.NotificationChannelIdErr,
            getString(R.string.settings),
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            enableVibration(true)
            enableLights(true)
            setShowBadge(true)
        }
        val d = NotificationChannel(
            Config.NotificationChannelIdDownload,
            getString(R.string.download),
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            enableVibration(false)
            enableLights(false)
            setShowBadge(false)
        }
        val m = NotificationChannel(
            Config.NotificationChannelIdMessage,
            getString(R.string.navigation_message),
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            enableVibration(true)
            enableLights(true)
            setShowBadge(true)
        }
        val w = NotificationChannel(
            Config.NotificationChannelIdWarning,
            "Warning",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            enableVibration(false)
            enableLights(false)
            setShowBadge(false)
        }
        notificationManager.createNotificationChannels(listOf(b, e, d, m, w))
    }

    companion object {

        private val intentFilter = IntentFilter().apply {
            addAction(LocationManager.PROVIDERS_CHANGED_ACTION)
            addAction(Intent.ACTION_TIME_CHANGED)
            addAction(Intent.ACTION_TIME_TICK)
            addAction(Intent.ACTION_TIMEZONE_CHANGED)
            addAction(PowerManager.ACTION_POWER_SAVE_MODE_CHANGED)
        }

        private val broadcastReceiver = object : BroadcastReceiver() {

            override fun onReceive(context: Context, intent: Intent?) {
                when (intent?.action) {
                    Intent.ACTION_TIME_CHANGED, Intent.ACTION_TIMEZONE_CHANGED, Intent.ACTION_TIME_TICK -> {
                        timeFlow.tryEmit(System.currentTimeMillis())
                    }

                    LocationManager.PROVIDERS_CHANGED_ACTION -> {
                        broadcastFlow.tryEmit(BroadcastEvent.LocationSettingsChanged)
                    }

                    PowerManager.ACTION_POWER_SAVE_MODE_CHANGED -> {
                        broadcastFlow.tryEmit(BroadcastEvent.PowerSaverChanged)
                    }
                }
            }
        }
        private val broadcastFlow = MutableSharedFlow<BroadcastEvent>(
            replay = 0,
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST,
        )

        fun broadcastFlow(): Flow<BroadcastEvent> {
            return broadcastFlow
        }

        fun broadcastLogout() {
            broadcastFlow.tryEmit(BroadcastEvent.Logout)
        }

        val gmsLocationErrFlow = MutableSharedFlow<Throwable>(
            replay = 0,
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )
        private val timeFlow = MutableSharedFlow<Long>(
            replay = 0,
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST,
        )
        val timeChangeFlow = timeFlow.asSharedFlow()
    }

    private fun stageUpdate() {
        val stage = 1
        val lastStage = defaultSharedPreferences.getInt("app_stage", 0)
        if (lastStage != stage) {
            defaultSharedPreferences.edit { putInt("app_stage", stage) }
        }
        if (lastStage < 1) {
            // clear install or too old.

            // try to remove unwanted files.
            getDatabasePath("result_db").run {
                delete()
                parentFile?.child("result_db-shm")?.delete()
                parentFile?.child("result_db-wal")?.delete()
            }
        }
    }
}

sealed class BroadcastEvent {
    object LocationSettingsChanged : BroadcastEvent()
    object PowerSaverChanged : BroadcastEvent()
    object Logout : BroadcastEvent()

    //todo consume?
}
