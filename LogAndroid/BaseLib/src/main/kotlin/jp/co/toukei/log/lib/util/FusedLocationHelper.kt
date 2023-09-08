package jp.co.toukei.log.lib.util

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import io.reactivex.rxjava3.functions.Action
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.internal.functions.Functions
import jp.co.toukei.log.lib.observeOnUI
import jp.co.toukei.log.lib.startNewHandlerThread
import jp.co.toukei.log.lib.subscribeOnIO
import jp.co.toukei.log.lib.toMaybe
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

class FusedLocationHelper(
    context: Context,
    private val locationCallback: LocationCallback,
) {

    private val looper = startNewHandlerThread(javaClass.simpleName).looper

    private val locationClient = LocationServices.getFusedLocationProviderClient(context)
    private val locationSettingsClient = LocationServices.getSettingsClient(context)

    private var request = AtomicReference<Request>()

    private inner class Request(
        @JvmField val locationRequest: LocationRequest,
        callback: LocationCallback,
        @JvmField val onFail: Consumer<Throwable>,
    ) : Action {

        private val fixLeak = LocationCallbackFix(callback)

        @Volatile
        var started: Boolean = false
            private set

        /*
         * tryStart
         * stop
         * onSuccess <<<< callback problem here.
         *
         * todo
         */
        private val hotfixStarting = AtomicBoolean(false)

        @SuppressLint("CheckResult")
        fun checkSettings(onSuccess: Action?) {
            val c = locationSettingsClient.checkLocationSettings(
                LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest)
                    .setAlwaysShow(true)
                    .build()
            )
            c.toMaybe(3000)
                .ignoreElement()
                .subscribeOnIO()
                .observeOnUI()
                .subscribe(onSuccess ?: Functions.EMPTY_ACTION, onFail)
        }

        fun tryStart() {
            val s = if (started) null else this
            if (s != null) {
                hotfixStarting.set(true)
            }
            checkSettings(s)
        }

        @SuppressLint("MissingPermission", "CheckResult")
        override fun run() {
            if (!hotfixStarting.get()) return
            stop()
            val task = locationClient.requestLocationUpdates(
                locationRequest,
                fixLeak,
                looper
            )
            task.toMaybe(3000)
                .ignoreElement()
                .subscribeOnIO()
                .observeOnUI()
                .subscribe({ started = true }, onFail)
        }

        fun stop() {
            hotfixStarting.set(false)
            started = false
            locationClient.removeLocationUpdates(fixLeak)
        }
    }

    @Volatile
    var connected = false
        private set

    @Volatile
    private var disposed = false

    @Synchronized
    fun setLocationRequest(locationRequest: LocationRequest, onFail: Consumer<Throwable>): Boolean {
        if (disposed) return false
        val t = request.getAndSet(null)
        val s =
            t !== null && t.locationRequest === locationRequest && t.onFail === onFail && request.compareAndSet(
                null,
                t
            )
        if (!s) {
            t?.stop()
            request.getAndSet(Request(locationRequest, locationCallback, onFail))?.stop()
        }
        connect()
        return true
    }

    fun checkSettings() {
        request.get()?.checkSettings(null)
    }

    @Synchronized
    fun connect() {
        if (connected) {
            request.get()?.tryStart()
        } else if (!disposed) {
            connected = true
            request.get()?.tryStart()
        }
    }

    @Synchronized
    fun disconnect() {
        connected = false
        request.get()?.stop()
    }

    @Synchronized
    fun destroy() {
        disconnect()
        request.set(null)
        looper.quit()
        disposed = true
    }
}
