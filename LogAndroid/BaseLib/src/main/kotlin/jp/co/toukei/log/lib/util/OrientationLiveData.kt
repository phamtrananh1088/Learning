package jp.co.toukei.log.lib.util

import android.content.Context
import android.view.OrientationEventListener
import android.view.Surface
import androidx.lifecycle.LiveData

class OrientationLiveData(context: Context) : LiveData<Int>() {

    private val orientationEventListener = object : OrientationEventListener(context) {
        override fun onOrientationChanged(orientation: Int) {
            val v = when (orientation) {
                in 45..134 -> Surface.ROTATION_270
                in 135..224 -> Surface.ROTATION_180
                in 225..314 -> Surface.ROTATION_90
                else -> Surface.ROTATION_0
            }
            if (value != v) value = v
        }
    }

    override fun onActive() {
        super.onActive()
        orientationEventListener.enable()
    }

    override fun onInactive() {
        super.onInactive()
        orientationEventListener.disable()
    }
}
