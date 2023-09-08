package jp.co.toukei.log.lib.ext

import android.content.Context
import android.graphics.*
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.toRectF
import androidx.core.graphics.withTranslation

class ImageViewWithBadgeText(context: Context) : AppCompatImageView(context) {

    private var text: String? = null
    private var textBounds: RectF? = null
    private var xPositionPercent: Float = 0F

    private var sizeW = 0
    private var sizeH = 0

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        sizeW = w
        sizeH = h
        super.onSizeChanged(w, h, oldw, oldh)
    }

    fun setBadge(text: String?, xPositionPercent: Float, textSize: Float) {
        this.text = text
        this.xPositionPercent = xPositionPercent
        paintText.textSize = textSize

        textBounds = if (text == null) null else {
            Rect().also { paintText.getTextBounds(text, 0, text.length, it) }.toRectF()
        }
        paintBorder.strokeWidth = textBounds?.run { (bottom - top) / 4 } ?: 0F
        invalidate()
    }

    private val paint = Paint().apply {
        isAntiAlias = true
        color = Color.RED
        style = Paint.Style.FILL
    }
    private val paintBorder = Paint().apply {
        isAntiAlias = true
        color = Color.WHITE
        style = Paint.Style.STROKE
    }
    private val paintText = Paint().apply {
        isAntiAlias = true
        color = Color.WHITE
        style = Paint.Style.FILL
        textAlign = Paint.Align.LEFT
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val str = text ?: return
        val rect = textBounds ?: return

        val bw = paintBorder.strokeWidth
        val vp = bw * 2
        val hp = vp * 1.5F

        val l = rect.left - hp
        val t = rect.top - vp
        val r = rect.right + hp
        val b = rect.bottom + vp
        val w = r - l

        var x = sizeW * xPositionPercent
        val m = sizeW - w
        if (x > m) x = m

        canvas.withTranslation(x, bw - t + paddingTop) {
            drawRoundRect(l, t, r, b, w, w, paint)
            drawRoundRect(l, t, r, b, w, w, paintBorder)
            drawText(str, 0F, 0F, paintText)
        }
    }
}
