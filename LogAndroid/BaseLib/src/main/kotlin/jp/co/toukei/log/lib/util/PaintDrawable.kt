package jp.co.toukei.log.lib.util

import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.drawable.Drawable

abstract class PaintDrawable(protected val paint: Paint) : Drawable() {

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun getOpacity(): Int = paint.alpha

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }
}
