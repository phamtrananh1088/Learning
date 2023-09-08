package jp.co.toukei.log.trustar.service

import android.Manifest
import android.app.Notification
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import androidx.core.app.NotificationCompat
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import jp.co.toukei.log.lib.buildNotification
import jp.co.toukei.log.lib.millisAfterOffset
import jp.co.toukei.log.lib.observeOnIO
import jp.co.toukei.log.lib.optional
import jp.co.toukei.log.lib.orElseNull
import jp.co.toukei.log.lib.replace
import jp.co.toukei.log.lib.setTo
import jp.co.toukei.log.lib.subscribeOnComputation
import jp.co.toukei.log.trustar.App
import jp.co.toukei.log.trustar.BroadcastEvent
import jp.co.toukei.log.trustar.Config
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.checkLocationOnOffNotification
import jp.co.toukei.log.trustar.checkPowerSaverNotification
import jp.co.toukei.log.trustar.ddString
import kotlinx.coroutines.rx3.asFlowable
import splitties.systemservices.locationManager
import splitties.systemservices.notificationManager
import splitties.systemservices.powerManager
import third.Clock
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference

class BinLocationService : Service() {

    private val runningDisposable = AtomicReference<Disposable>()
    private val broadcastFlowDisposable = AtomicReference<Disposable>()

    private fun ensureSubscribe() {
        val o = runningDisposable.get()
        if (o?.isDisposed == false) return

        Current.userFlowable
            .switchMap {
                val c = it.orElseNull()
                if (c == null) Flowable.error(Exception()) else Flowable.just(c.binLocationTask)
            }
            .distinctUntilChanged()
            .switchMap {
                it.throttleLatestRecord(2)
            }
            .switchMapSingle {
                if (it.running) Single.just(it.location.optional()) else Single.error(Exception())
            }
            .throttleLatest(1, TimeUnit.SECONDS, true)
            .onBackpressureLatest()
            .observeOnIO(1)
            .subscribeOnComputation()
            .onTerminateDetach()
            .subscribe(
                {
                    if (!Config.isProductRelease) {
                        val c = it.orElseNull()
                        if (c != null) {
                            val time = Clock(Config.timeZone.millisAfterOffset(c.time)).hhmmss
                            updateNotification("$time (${c.ddString()})")
                        }
                    }
                },
                {
                    serviceStop()
                },
                {
                    serviceStop()
                }
            )
            .setTo(runningDisposable)
    }

    private fun cancelNotification() {
        notificationManager.cancel(Config.nidFgs)
    }

    @Suppress("DEPRECATION")
    private fun serviceStop() {
        stopForeground(true)
        stopSelf()
    }

    override fun onCreate() {

        // https://developer.android.com/about/versions/14/changes/fgs-types-required#location
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return stopSelf()
        }

        startForeground(Config.nidFgs, buildNotification(null))
        ensureSubscribe()

        App.broadcastFlow()
            .asFlowable()
            .retry()
            .subscribe {
                when (it) {
                    BroadcastEvent.LocationSettingsChanged -> {
                        checkLocation()
                    }

                    BroadcastEvent.PowerSaverChanged -> {
                        checkPowerSaver()
                    }

                    else -> {}
                }
            }
            .setTo(broadcastFlowDisposable)

        checkLocation()
        checkPowerSaver()
    }

    private fun checkLocation() {
        notificationManager.checkLocationOnOffNotification(this, 0, locationManager)
    }

    private fun checkPowerSaver() {
        notificationManager.checkPowerSaverNotification(this, 1, powerManager)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        ensureSubscribe()
        return START_STICKY
    }

    override fun onDestroy() {
        cancelNotification()
        broadcastFlowDisposable.replace(null)
        runningDisposable.replace(null)
    }

    private fun updateNotification(text: String) {
        notificationManager.notify(
            Config.nidFgs,
            buildNotification(text)
        )
    }

    private fun buildNotification(text: String?): Notification {
        return buildNotification(Config.NotificationChannelIdLocation) {
            setContentTitle(getString(R.string.bin_working))
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
