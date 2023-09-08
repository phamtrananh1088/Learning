package jp.co.toukei.log.lib.ext

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.SystemClock
import android.view.View
import androidx.core.graphics.withTranslation
import jp.co.toukei.log.lib.common.withAlpha
import jp.co.toukei.log.lib.fastHHMMString
import splitties.dimensions.dip
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

class CircleTimer(context: Context) : View(context) {

    private class Text(
        @JvmField val paint: Paint,
        @JvmField val text: String
    ) {

        @JvmField
        val centerY: Float

        @JvmField
        val halfHeight: Float

        init {
            val d = paint.descent()
            val a = paint.ascent()
            halfHeight = (d - a) / 2
            centerY = (d + a) / 2
        }

        fun buildWithText(t: String): Text {
            return if (text == t) this else Text(paint, t)
        }
    }

    private var centerText: Text? = null
    private var belowText: Text? = null

    fun setCenterTextSize(size: Float) {
        paintCenterText.textSize = size
        paintBelowText.textSize = size / 3
        postInvalidate()
    }

    fun setBelowText(text: String?) {
        belowText = text?.let { Text(paintBelowText, it) }
        postInvalidate()
    }

    private var colorAlpha = 0x60
    fun setAlphaAndSecondColor(alpha: Int, gray: Int) {
        colorAlpha = alpha
        paintBelowText.color = gray
        postInvalidate()
    }

    private var strokeWidth: Float = context.dip(2).toFloat()
    private var dotRadius: Float = strokeWidth

    fun setRingWidth(width: Float, radius: Float) {
        strokeWidth = width
        dotRadius = radius
        paintR.strokeWidth = width
        paintA.strokeWidth = width
        postInvalidate()
    }

    private var angle: Float = 0F
    private var yOffset: Float = 0F
    private var xOffset: Float = 0F


    fun setCurrentTimeMillis(totalMillis: Int, basedSystemTimeMillis: Long?) {
        run.totalMillis = totalMillis
        run.basedRealTime = basedSystemTimeMillis?.let {
            it - System.currentTimeMillis() + SystemClock.elapsedRealtime()
        }
        postInvalidate()
    }

    private var update: Long = 20
    fun setUpdateRate(millis: Long) {
        if (millis > 0) {
            update = millis
            postInvalidate()
        }
    }

    private fun calculateValue(totalMillis: Int, basedRealTime: Long?) {
        val offset =
            if (basedRealTime != null) SystemClock.elapsedRealtime() - basedRealTime else 0L
        angle = offset.toFloat() * 360 / totalMillis
        val r = Math.toRadians(angle.toDouble())
        yOffset = 1 + sin(r).toFloat()
        xOffset = 1 - cos(r).toFloat()

        val m = (offset / 60_000).toInt()
        fastHHMMString(m).toString().let {
            centerText = centerText?.buildWithText(it) ?: Text(paintCenterText, it)
        }
    }

    fun setPaintColor(color: Int) {
        paintCenterText.color = color
        paintR.color = color.withAlpha(colorAlpha)
        paintA.color = color
        paintDot.color = color
        postInvalidate()
    }

    private val run = object : Runnable {

        var totalMillis: Int = 3600_000
        var basedRealTime: Long? = null

        override fun run() {
            calculateValue(totalMillis, basedRealTime)
            invalidate()
            postDelayed(this, update)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        post(run)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeCallbacks(run)
    }

    private val paintR = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
    }

    private val paintA = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
    }
    private val paintDot = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
    }

    private val paintCenterText = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
    }
    private val paintBelowText = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val min = min(
            getDefaultSize(suggestedMinimumWidth, widthMeasureSpec),
            getDefaultSize(suggestedMinimumHeight, heightMeasureSpec)
        )
        setMeasuredDimension(min, min)
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        val sw = strokeWidth
        val r = dotRadius

        val w = (width - paddingLeft - paddingRight).toFloat()
        val h = (height - paddingTop - paddingBottom).toFloat()
        val min = min(w, h)

        canvas.withTranslation(
            paddingLeft.toFloat() + (w - min) / 2,
            paddingTop.toFloat() + (h - min) / 2
        ) {
            val start = max(sw / 2, r)
            val end = min - start
            drawArc(start, start, end, end, 0F, 360F, false, paintR)
            drawArc(start, start, end, end, 270F, angle, false, paintA)

            val center = min / 2 - start
            drawCircle(
                start + center * yOffset,
                start + center * xOffset,
                r,
                paintDot
            )
            val c = start + center
            centerText?.run {
                drawText(text, c, c - centerY, paint)
                val bOff = halfHeight
                belowText?.run {
                    val o = bOff + halfHeight
                    drawText(text, c, c - centerY + o, paint)
                }
            }
        }
    }
}
