package jp.co.toukei.log.lib.util

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import kotlin.math.ceil

class TextDrawable(
        text: String,
        size: Float,
        private val intrinsicWidthHeightRatio: Float,
        color: Int
) : PaintDrawable(
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textAlign = Paint.Align.CENTER
            style = Paint.Style.FILL
            textSize = size
            this.color = color
        }
) {

    var text: String = text
        set(value) {
            field = value
            compute()
        }
    private val rect = Rect()

    private fun compute() {
        paint.getTextBounds(text, 0, text.length, rect)
        invalidateSelf()
    }

    override fun draw(canvas: Canvas) {
        val b = bounds
        canvas.drawText(
                text,
                0,
                text.length,
                b.exactCenterX(),
                b.exactCenterY() - rect.exactCenterY(),
                paint
        )
    }

    override fun getIntrinsicWidth(): Int {
        return if (intrinsicWidthHeightRatio < 0) -1 else ceil((intrinsicWidthHeightRatio * rect.width()).toDouble()).toInt()
    }

    override fun getIntrinsicHeight(): Int {
        return if (intrinsicWidthHeightRatio < 0) -1 else ceil((intrinsicWidthHeightRatio * rect.height()).toDouble()).toInt()
    }

    init {
        compute()
    }
}
