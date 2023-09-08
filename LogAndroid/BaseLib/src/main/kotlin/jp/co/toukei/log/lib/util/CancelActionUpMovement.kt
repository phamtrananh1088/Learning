package jp.co.toukei.log.lib.util

import android.text.Spannable
import android.text.method.MovementMethod
import android.view.MotionEvent
import android.widget.TextView

class CancelActionUpMovement(
        private val delegate: MovementMethod,
        periodMillis: Long
) : MovementMethod by delegate {

    private val cancelNano = periodMillis * 1000_000
    private var t: Long = 0

    override fun onTouchEvent(widget: TextView?, text: Spannable?, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> t = System.nanoTime()
            MotionEvent.ACTION_UP -> {
                val n = System.nanoTime()
                if (n > t && n > t + cancelNano) {
                    return false
                }
            }
        }

        return delegate.onTouchEvent(widget, text, event)
    }
}
