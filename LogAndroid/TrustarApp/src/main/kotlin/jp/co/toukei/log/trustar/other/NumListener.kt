package jp.co.toukei.log.trustar.other

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import jp.co.toukei.log.trustar.roundToStringDecimal1

class NumListener(
    private val textView: TextView,
    private val decrement: View,
    private val increment: View,
    private val max: Double,
) : View.OnLongClickListener, Runnable, View.OnClickListener, View.OnTouchListener {
    private var step = 0
    private var delay = 300L

    override fun onClick(v: View?) {
        reset(v)
        setValue()
    }

    override fun onLongClick(v: View?): Boolean {
        reset(v)
        delay = 300L
        textView.post(this)
        return true
    }

    private fun reset(v: View?) {
        textView.clearFocus()
        when (v) {
            decrement -> step = -1
            increment -> step = 1
        }
        textView.removeCallbacks(this)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> textView.removeCallbacks(this)
        }
        return false
    }

    private fun setValue() {
        val i = textView.text?.toString()?.toDoubleOrNull() ?: 0.0
        val value = i + step
        val v1 = if (value > max) max else value
        textView.text = v1.roundToStringDecimal1()
    }

    override fun run() {
        setValue()
        textView.removeCallbacks(this)
        delay -= 50
        textView.postDelayed(this, maxOf(50, delay))
    }
}
