package jp.co.toukei.log.trustar.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.os.IBinder
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import androidx.core.location.LocationManagerCompat
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.Disposable
import jp.co.toukei.log.lib.buildNotification
import jp.co.toukei.log.lib.millisAfterOffset
import jp.co.toukei.log.lib.observeOnIO
import jp.co.toukei.log.lib.orElseNull
import jp.co.toukei.log.lib.subscribeOnComputation
import jp.co.toukei.log.trustar.Config
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.notificationOfBatterySaverIsOn
import splitties.systemservices.locationManager
import splitties.systemservices.notificationManager
import splitties.systemservices.powerManager
import third.Clock
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference

class BinLocationService : Service() {

    private val disposable = AtomicReference<Disposable>()

    private fun ensureSubscribe() {
        val o = disposable.get()
        if (o != null) return

        Current.userFlowable
            .switchMap {
                val c = it.orElseNull()
                if (c == null) Flowable.error(Exception()) else Flowable.just(c.binLocationTask)
            }
            .distinctUntilChanged()
            .switchMap {
                it.throttleLatestRecord(2)
            }
            .switchMap {
                val c = it.orElseNull()
                if (c == null) Flowable.error(Exception()) else Flowable.just(c)
            }
            .throttleLatest(1, TimeUnit.SECONDS, true)
            .onBackpressureLatest()
            .observeOnIO(1)
            .subscribeOnComputation()
            .onTerminateDetach()
            .subscribe(
                {
                    if (!Config.isProductRelease) {
                        val c = it.location
                        val time = Clock(Config.GMT9.millisAfterOffset(it.time)).hhmmss
                        updateNotification("$time (${c.latitude}, ${c.longitude})")
                    }
                },
                {
                    serviceStop()
                },
                {
                    serviceStop()
                }
            )
            .let {
                disposable.getAndSet(it)?.dispose()
            }
    }

    private fun cancelNotification() {
        notificationManager.cancel(Config.LocationNotificationId)
    }

    @Suppress("DEPRECATION")
    private fun serviceStop() {
        stopForeground(true)
        stopSelf()
    }

    override fun onCreate() {
        startForeground(Config.LocationNotificationId, buildNotification(null))
        ensureSubscribe()
        registerReceiver(receiver, receiver.intentFilter)
        receiver.checkLocation()
        receiver.checkPowerSaver()
    }

    private val receiver = object : BroadcastReceiver() {
        @JvmField
        val intentFilter = IntentFilter().apply {
            addAction(LocationManager.PROVIDERS_CHANGED_ACTION)
            addAction(PowerManager.ACTION_POWER_SAVE_MODE_CHANGED)
        }

        override fun onReceive(context: Context, intent: Intent?) {
            when (intent?.action) {
                LocationManager.PROVIDERS_CHANGED_ACTION -> checkLocation()
                PowerManager.ACTION_POWER_SAVE_MODE_CHANGED -> checkPowerSaver()
            }
        }

        fun checkLocation() {
            notificationManager.apply {
                val id = Config.LocationSettingsNotificationId
                if (LocationManagerCompat.isLocationEnabled(locationManager)) {
                    cancel(id)
                } else {
                    notify(
                        id,
                        buildNotification(Config.SettingsNotificationChannelId) {
                            setContentText(getString(R.string.notification_location_is_off_title))
                            setSmallIcon(R.drawable.baseline_info_24)
                            setOngoing(true)
                            setContentIntent(
                                PendingIntent.getActivity(
                                    this@BinLocationService, 0,
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

        fun checkPowerSaver() {
            notificationManager.apply {
                val id = Config.PowerSaverSettingsNotificationId
                if (powerManager.isPowerSaveMode) {
                    notify(id, notificationOfBatterySaverIsOn(1))
                } else {
                    cancel(id)
                }
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        ensureSubscribe()
        return START_STICKY
    }

    override fun onDestroy() {
        cancelNotification()
        disposable.getAndSet(null)?.dispose()
        unregisterReceiver(receiver)
    }

    private fun updateNotification(text: String) {
        notificationManager.notify(
            Config.LocationNotificationId,
            buildNotification(text)
        )
    }

    private fun buildNotification(text: String?): Notification {
        return buildNotification(Config.LocationNotificationChannelId) {
            setContentTitle("Location Service is running.")
            text?.let(::setContentText)
            setSmallIcon(R.drawable.baseline_location_on_24)
            setOngoing(true)
            setCategory(Notification.CATEGORY_SERVICE)
            color = getColor(R.color.colorPrimary)
            setColorized(true)
            foregroundServiceBehavior = NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
