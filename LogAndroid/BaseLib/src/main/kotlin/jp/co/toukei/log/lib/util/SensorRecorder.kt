package jp.co.toukei.log.lib.util

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import jp.co.toukei.log.lib.dateBasedOnCurrentDate
import java.util.concurrent.TimeUnit

class SensorRecorder(
    private val sensorManager: SensorManager,
    private val listenerCallback: (sensorType: Int, timestampNano: Long, x: Float, y: Float, z: Float) -> Unit,
) {

    private val listener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            val n = event.timestamp
            val (x, y, z) = event.values
            listenerCallback(event.sensor.type, n, x, y, z)
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        }
    }

    fun startGravity(): Boolean {
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY) ?: return false
        return sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    fun startAccelerometer(): Boolean {
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) ?: return false
        return sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    fun startGravityOrAccelerometer(): Boolean {
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)
            ?: sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            ?: return false
        return sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    fun startMagneticField(): Boolean {
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) ?: return false
        return sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    fun stopAll() {
        sensorManager.unregisterListener(listener)
    }

    class EventXYZT(
        val x: Float,
        val y: Float,
        val z: Float,
        val t: Long, //nano
    )
}


fun SensorManager.rxAccelerometerDetection(
    sampling: Long, // MILLISECONDS
    size: Int,
    limit: Long,   // MILLISECONDS
    threshold: Float,
): Flowable<SensorRecorder.EventXYZT> {
    return Flowable
        .create({ emitter ->
            val r = SensorRecorder(this) { _, t, x, y, z ->
                emitter.onNext(SensorRecorder.EventXYZT(x, y, z, t))
            }
            emitter.setCancellable { r.stopAll() }
            r.startAccelerometer()
        }, BackpressureStrategy.BUFFER)
        .window(sampling, TimeUnit.MILLISECONDS)
        .flatMapMaybe {
            it.scan { t1, t2 ->
                val ax = (t1.x + t2.x) / 2
                val ay = (t1.y + t2.y) / 2
                val az = (t1.z + t2.z) / 2
                val at = (t1.t + t2.t) / 2
                SensorRecorder.EventXYZT(ax, ay, az, at)
            }.lastElement()
        }
        .buffer(size, 1)
        .onBackpressureDrop()
        .filter { l ->
            val xMax = l.maxOf { it.x }
            val xMin = l.minOf { it.x }
            val yMax = l.maxOf { it.y }
            val yMin = l.minOf { it.y }
            val zMax = l.maxOf { it.z }
            val zMin = l.minOf { it.z }
            xMax - xMin > threshold && yMax - yMin > threshold && zMax - zMin > threshold
        }
        .flatMap({
            val data = it.firstOrNull()
            val f = if (data == null) Flowable.empty() else Flowable.just(data)
            f.concatWith(Completable.complete().delaySubscription(limit, TimeUnit.MILLISECONDS))
        }, 1)
}

/**
 * @return event time based on current OS time.
 */
fun SensorManager.rxAccelerometerDetectionMillis(
    sampling: Long, // MILLISECONDS
    size: Int,
    limit: Long,   // MILLISECONDS
    threshold: Float,
): Flowable<Long> {
    return rxAccelerometerDetection(sampling, size, limit, threshold).map { e ->
        dateBasedOnCurrentDate(e.t)
    }
}
