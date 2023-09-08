package jp.co.toukei.log.trustar.detect

import android.location.Location
import android.os.SystemClock
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.Disposable
import jp.co.toukei.log.lib.observeOnIO
import jp.co.toukei.log.lib.util.MapDisposable
import jp.co.toukei.log.lib.util.rxAccelerometerDetectionMillis
import jp.co.toukei.log.lib.util.withLastElementOnCancel
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.db.user.entity.BinHeader
import jp.co.toukei.log.trustar.db.user.entity.SensorDetectEvent
import jp.co.toukei.log.trustar.user.UserInfo
import splitties.systemservices.sensorManager
import java.util.concurrent.TimeUnit
import kotlin.math.max

fun restEvent(
    working: Flowable<List<BinHeader>>,
    location: Flowable<Location>,
    user: UserInfo,
): Flowable<Pair<String, DetectRestEvent>> {
    val restDistanceMeter = user.restDistanceMeter
    val restTimeInMillis = user.restTimeInMin * 60_000L

    return working.eachWorkingBin(
        internalCancelEmit = { _, p ->
            val r = p?.second
            if (r is DetectRestEvent.RestDetected) {
                val now = SystemClock.elapsedRealtimeNanos()
                val d = DetectRestEvent.MoveDetected(r, now)
                val over = d.elapsedRestMillis() > restTimeInMillis
                if (over) p.first to d else null
            } else null
        }
    ) { b ->
        val allocationNo = b.allocationNo
        DetectRestEvent.Reset()
            .detectAfter(
                locationFlow = location,
                restDistance = restDistanceMeter,
                restSpeedMpMs = restDistanceMeter.toDouble() / 15_000,
                restTimeInMillis = restTimeInMillis,
                speedDetectSample = 10_000,
            )
            .map { allocationNo to it }
            .onErrorComplete()
    }.onBackpressureBuffer()
}

fun sensorDetect(
    working: Flowable<List<BinHeader>>,
    binHeaderDelayInMillis: Long,
    workDelayInMillis: Long,
    detectControl: (allocationNo: String) -> Flowable<Boolean>,
): Flowable<SensorRecord> {
    return Flowable.defer {
        val sensorEvent = sensorManager
            .rxAccelerometerDetectionMillis(100, 10, 10_000, 4.3F)
            .observeOnIO()
            .share()
        working.eachWorkingBin(
            internalCancelEmit = { b, _ ->
                val now = System.currentTimeMillis()
                val from = now - binHeaderDelayInMillis
                SensorRecord.DropRecent(b.allocationNo, from, now, true)
            }
        ) { b ->
            detectControl(b.allocationNo)
                .distinctUntilChanged()
                .switchMap {
                    if (it) {
                        sensorEvent
                            .map { eventDate ->
                                SensorRecord.Record(
                                    SensorDetectEvent(b, Current.lastLocation, eventDate)
                                )
                            }
                            .delaySubscription(workDelayInMillis, TimeUnit.MILLISECONDS)
                    } else {
                        val now = System.currentTimeMillis()
                        val from = now - workDelayInMillis
                        Flowable.just(
                            SensorRecord.DropRecent(b.allocationNo, from, now, false)
                        )
                    }
                }
                .delaySubscription(
                    binHeaderDelayInMillis - max(
                        0L,
                        System.currentTimeMillis() - (b.startLocation?.date ?: 0L)
                    ),
                    TimeUnit.MILLISECONDS
                )
                .onErrorComplete()
        }.onBackpressureLatest()
    }
}

fun <T : Any> Flowable<List<BinHeader>>.eachWorkingBin(
    internalCancelEmit: ((BinHeader, lastElement: T?) -> T?)? = null,
    flowable: (BinHeader) -> Flowable<T>,
): Flowable<T> {
    return Flowable.create({ emitter ->
        var onCancelEmit = internalCancelEmit
        val mapDisposable = MapDisposable<String>()
        val d = subscribe { ls ->
            val map = mutableMapOf<String, Disposable>()
            ls.filter { it.binStatus.isWorking() }.forEach { b ->
                val k = b.allocationNo
                val d = mapDisposable.take(k)
                val x = if (d == null || d.isDisposed) {
                    flowable(b)
                        .withLastElementOnCancel {
                            onCancelEmit?.invoke(b, it)?.let(emitter::onNext)
                        }
                        .subscribe(emitter::onNext, emitter::onError)
                } else {
                    d
                }
                map.put(k, x)?.dispose()
            }
            mapDisposable.clear()
            map.forEach {
                mapDisposable.put(it.key, it.value)
            }
        }
        emitter.setCancellable {
            onCancelEmit = null
            d.dispose()
            mapDisposable.dispose()
        }
    }, BackpressureStrategy.MISSING)
}
