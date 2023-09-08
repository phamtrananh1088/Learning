package jp.co.toukei.log.trustar.detect

import android.location.Location
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.processors.BehaviorProcessor
import jp.co.toukei.log.lib.dateBasedOnCurrentDate
import jp.co.toukei.log.lib.elapsedMillis
import jp.co.toukei.log.lib.observeOnIO
import jp.co.toukei.log.lib.secondToLast
import jp.co.toukei.log.lib.util.keepHistoryUntil
import jp.co.toukei.log.lib.withPrevious
import jp.co.toukei.log.trustar.detect.DetectRestEvent.MoveDetected
import jp.co.toukei.log.trustar.detect.DetectRestEvent.Reset
import jp.co.toukei.log.trustar.detect.DetectRestEvent.RestDetected

sealed class DetectRestEvent {

    class MoveDetected(
        val rest: RestDetected,
        private val endRealtimeNanos: Long,
    ) : DetectRestEvent() {

        fun elapsedRestMillis(): Long {
            return (endRealtimeNanos - rest.startRestLocation.elapsedRealtimeNanos) / 1000_000
        }

        fun restEndTimeBasedOsDate(): Long {
            return dateBasedOnCurrentDate(endRealtimeNanos)
        }
    }

    class RestDetected(
        val startRestLocation: Location,
        val speed: Double,
    ) : DetectRestEvent()

    class Reset(
        val elapsedMillis: Long? = null,
    ) : DetectRestEvent()
}

fun DetectRestEvent.detectAfter(
    locationFlow: Flowable<Location>,
    restDistance: Int,
    restSpeedMpMs: Double,
    restTimeInMillis: Long,
    speedDetectSample: Long,
): Flowable<DetectRestEvent> {
    val behaviorProcessor = BehaviorProcessor.createDefault(this)
    return behaviorProcessor
        .switchMap { e ->
            when (e) {
                is RestDetected -> locationFlow
                    .withPrevious()
                    .switchMap { (secondToLast, last) ->
                        if (secondToLast != null && last.distanceTo(e.startRestLocation) > restDistance) {
                            val t = last.elapsedMillis(e.startRestLocation)
                            Flowable.just(
                                if (t > restTimeInMillis) {
                                    MoveDetected(e, secondToLast.elapsedRealtimeNanos)
                                } else {
                                    Reset(t)
                                }
                            )
                        } else {
                            Flowable.empty()
                        }
                    }

                else -> locationFlow
                    .locationSample(speedDetectSample)
                    .switchMap { (lastDropped, list) ->
                        val last = list.lastOrNull()
                        if (lastDropped == null || last == null) {
                            Flowable.empty()
                        } else {
                            val t = last.elapsedMillis(lastDropped)
                            val distance = last.distanceTo(lastDropped)
                            val speed = distance.toDouble() / t
                            if (speed < restSpeedMpMs) {
                                val secondToLast = list.secondToLast() ?: lastDropped
                                Flowable.just(RestDetected(secondToLast, speed))
                            } else {
                                Flowable.empty()
                            }
                        }
                    }
            }
        }
        .doAfterNext(behaviorProcessor::onNext)
        .observeOnIO()
}

private fun Flowable<Location>.locationSample(speedDetectSample: Long): Flowable<Pair<Location?, List<Location>>> {
    return keepHistoryUntil { current, item ->
        current.elapsedMillis(item) >= speedDetectSample
    }.map { (drops, list) ->
        drops.lastOrNull() to list
    }.scan { (d0, _), (d, l) ->
        val lastDropped = d ?: d0
        lastDropped to l
    }
}
