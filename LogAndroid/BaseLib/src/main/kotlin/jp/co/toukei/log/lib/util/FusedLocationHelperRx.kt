package jp.co.toukei.log.lib.util

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Emitter
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.internal.functions.Functions
import io.reactivex.rxjava3.processors.BehaviorProcessor
import jp.co.toukei.log.lib.ctx.Ctx
import jp.co.toukei.log.lib.get
import jp.co.toukei.log.lib.observeOnIO
import jp.co.toukei.log.lib.optional
import jp.co.toukei.log.lib.replace
import jp.co.toukei.log.lib.rxConsumer
import jp.co.toukei.log.lib.subscribeOnIO
import jp.co.toukei.log.lib.toMaybe
import jp.co.toukei.log.lib.weakRef
import java.lang.ref.WeakReference
import java.util.Optional
import java.util.concurrent.atomic.AtomicReference


class FusedLocationHelperRx {

    private val myClient = ClientWrapper(
        LocationServices.getFusedLocationProviderClient(Ctx.context)
    )
    private val locationSettingsClient = LocationServices.getSettingsClient(Ctx.context)

    private val savedRequest = mutableListOf<LocationRequest>()

    fun addRequest(
        intervalMillis: Long,
        minUpdateIntervalMillis: Long = 3000L,
        priority: Int = Priority.PRIORITY_HIGH_ACCURACY,
    ): Flowable<Location> {
        val r = LocationRequest.Builder(intervalMillis)
            .setPriority(priority)
            .setMinUpdateIntervalMillis(minUpdateIntervalMillis)
            .build()
        return addRequest(r)
    }

    fun addRequest(
        request: LocationRequest,
    ): Flowable<Location> {
        return myClient.addRequestLocationUpdates(request, locationSettingsClient)
            .doOnSubscribe {
                savedRequest += request
            }
            .doFinally {
                savedRequest -= request
            }
    }

    fun checkSettings(): Completable {
        return locationSettingsClient.checkSettings(savedRequest)
    }

    fun forceStopLocationUpdates() {
        myClient.removeAllRequests()
    }
}


class LocationRequestHelper(
    private val rx: FusedLocationHelperRx,
    private val onLocation: (Location) -> Unit,
    private val onErr: (Throwable) -> Unit,
) {

    @JvmField
    val latestErr = BehaviorProcessor.create<Optional<Throwable>>()

    private val errConsumer = rxConsumer<Throwable> {
        latestErr.onNext(it.optional())
        onErr(it)
    }

    private val disposable = AtomicReference<Disposable>()
    private var locationRequest: LocationRequest? = null

    fun setRequest(
        request: LocationRequest,
    ) {
        latestErr.onNext(Optional.empty())
        locationRequest = request
        val d = rx.addRequest(request).subscribe(onLocation, errConsumer)
        disposable.replace(d)
    }

    fun reconnected(checkIfConnected: Boolean = false): Boolean {
        Log.d("hmm", "checkIfConnected=$checkIfConnected")
        latestErr.onNext(Optional.empty())
        val disconnected = disposable.get()?.isDisposed != false
        if (disconnected) {
            locationRequest?.let { setRequest(it) }
        } else if (checkIfConnected) {
            //todo
            rx.checkSettings().subscribe(Functions.EMPTY_ACTION, errConsumer)
        }
        return disconnected
    }

    fun disconnect(clearRequest: Boolean) {
        latestErr.onNext(Optional.empty())
        if (clearRequest) {
            locationRequest = null
        }
        disposable.replace(null)
    }
}

private class LocationListenerFixLeak(
    private val emitter: WeakReference<out Emitter<Location>>,
) : LocationListener {
    override fun onLocationChanged(p0: Location) {
        emitter.get { onNext(p0) }
    }

    fun clear() {
        emitter.clear()
    }
}

@SuppressLint("MissingPermission")
private class ClientWrapper(
    private val client: FusedLocationProviderClient,
) {
    private val s = mutableListOf<LocationListener>()

    fun requestLocationUpdates(
        p0: LocationRequest,
        p1: LocationListener,
        p2: Looper?,
    ): Task<Void> {
        return client.requestLocationUpdates(p0, p1, p2).addOnSuccessListener { s += p1 }
    }

    fun removeLocationUpdates(p0: LocationListener): Task<Void> {
        return client.removeLocationUpdates(p0).addOnSuccessListener { s -= p0 }
    }

    fun removeAllRequests() {
        s.forEach(::removeLocationUpdates)
    }
}

fun SettingsClient.checkSettings(
    vararg requests: LocationRequest,
): Completable {
    return checkSettings(requests.asList())
}

fun SettingsClient.checkSettings(
    requests: Collection<LocationRequest>,
): Completable {
    return Completable.defer {
        checkLocationSettings(
            LocationSettingsRequest.Builder()
                .addAllLocationRequests(requests)
                .setAlwaysShow(true)
                .build()
        ).toMaybe().ignoreElement().observeOnIO().subscribeOnIO()
    }
}

private fun ClientWrapper.addRequestLocationUpdates(
    request: LocationRequest,
    settingsClient: SettingsClient?,
): Flowable<Location> {
    val f = Flowable.create({ e ->
        val l = LocationListenerFixLeak(e.weakRef())
        val d = requestLocationUpdates(request, l, Looper.getMainLooper())
            .toMaybe()
            .subscribe(Functions.emptyConsumer()) {
                l.clear()
                e.tryOnError(it)
                removeLocationUpdates(l)
            }
        e.setCancellable {
            l.clear()
            d.dispose()
            removeLocationUpdates(l)
        }
    }, BackpressureStrategy.LATEST).observeOnIO().subscribeOnIO()
    return if (settingsClient == null) f else {
        f.startWith(settingsClient.checkSettings(request))
    }
}
