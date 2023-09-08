/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 modified
 */
package com.zeevox.pie.paint

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import android.view.WindowInsets
import androidx.annotation.FloatRange
import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

class Painting(context: Context) : View(context), SpotFilter.Plotter {

    private var devicePressureMin = 0f // ideal value
    private var devicePressureMax = 0.5f // ideal value

    private var bitmap: Bitmap? = null
    private var paperColor: Int = 0xFFFFFFFF.toInt()

    private var _paintCanvas: Canvas? = null
    private val _bitmapLock = Object()

    private var _drawPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var _lastX = 0f
    private var _lastY = 0f
    private var _lastR = 0f
    private var _insets: WindowInsets? = null

    private var _brushWidth = 10f

    private var _filter = SpotFilter(10, 0.5f, 0.9f, this)

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        setupBitmaps()

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setupBitmaps()
    }

    override fun onApplyWindowInsets(insets: WindowInsets?): WindowInsets {
        _insets = insets
        if (insets != null && _paintCanvas == null) {
            setupBitmaps()
        }
        return super.onApplyWindowInsets(insets)
    }

    /**
     * いろいろスマホ、おかしい。
     * https://www.google.com/search?q=1-(x-1)%5E2%2C(x-1)%5E3%2B1%2C1-(x-1)%5E4
     * @see [MotionEvent.PointerCoords.pressure]
     */
    private fun width(@FloatRange(from = 0.0, to = 1.0) pressure: Float): Float {
        //        val r = (1 - (pressure - 1).pow(4))  //y=1-(x-1)^4
        val r = (pressure - 1).pow(3) + 1  //y=(x-1)^3+1

        return r * _brushWidth
    }

    override fun plot(s: MotionEvent.PointerCoords) {
        val c = _paintCanvas ?: return
        synchronized(_bitmapLock) {
            var x = _lastX
            var y = _lastY
            var r = _lastR
            val p = adjustPressure(s.pressure)
            val newR = max(1f, width(p))

            if (r >= 0) {
                val d = hypot(s.x - x, s.y - y)
                if (d > 1f && (r + newR) > 1f) {
                    val N = (2 * d / min(4f, r + newR)).toInt()

                    val stepX = (s.x - x) / N
                    val stepY = (s.y - y) / N
                    val stepR = (newR - r) / N
                    for (i in 0 until N - 1) { // we will draw the last circle below
                        x += stepX
                        y += stepY
                        r += stepR
                        c.drawCircle(x, y, r, _drawPaint)
                    }
                }
            }

            c.drawCircle(s.x, s.y, newR, _drawPaint)
            _lastX = s.x
            _lastY = s.y
            _lastR = newR
        }
    }

    @FloatRange(from = 0.0, to = 1.0)
    private fun adjustPressure(pressure: Float): Float {
        if (pressure > devicePressureMax) devicePressureMax = pressure
        if (pressure < devicePressureMin) devicePressureMin = pressure

        val min = devicePressureMin
        val max = devicePressureMax
        return if (max > min) (pressure - min) / (max - min) else 1.0f
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val c = _paintCanvas
        if (event == null || c == null) return super.onTouchEvent(event)

        /*
        val pt = Paint(Paint.ANTI_ALIAS_FLAG)
        pt.style = Paint.Style.STROKE
        pt.color = 0x800000FF.toInt()
        _paintCanvas?.drawCircle(event.x, event.y, 20f, pt)
        */

        when (event.actionMasked) {
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                _filter.add(event)
                _filter.finish()
                invalidate()
            }

            MotionEvent.ACTION_DOWN -> {
                _lastR = -1f
                _filter.add(event)
                invalidate()
            }

            MotionEvent.ACTION_MOVE -> {
                _filter.add(event)
                invalidate()
            }
        }

        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        bitmap?.let {
            canvas.drawBitmap(it, 0f, 0f, _drawPaint)
        }
    }

    // public api
    fun clear() {
        bitmap = null
        setupBitmaps()
        invalidate()
    }

    fun sampleAt(x: Float, y: Float): Int {
        val localX = (x - left).toInt()
        val localY = (y - top).toInt()
        return bitmap?.getPixel(localX, localY) ?: Color.BLACK
    }

    fun setPaintColor(color: Int) {
        _drawPaint.color = color
    }

    fun setBrushWidth(w: Float) {
        _brushWidth = w
    }

    private fun setupBitmaps() {
        val w = width
        val h = height
        if (w <= 0 || h <= 0) return
        val oldBits = bitmap
        var bits = oldBits
        if (bits == null || bits.width != w || bits.height != h) {
            bits = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        }
        if (bits == null) return

        val c = Canvas(bits)

        if (oldBits != null) {
            if (oldBits.width < oldBits.height != bits.width < bits.height) {
                // orientation change. let's rotate things so they fit better
                val matrix = Matrix()
                if (bits.width > bits.height) {
                    // now landscape
                    matrix.postRotate(-90f)
                    matrix.postTranslate(0f, bits.height.toFloat())
                } else {
                    // now portrait
                    matrix.postRotate(90f)
                    matrix.postTranslate(bits.width.toFloat(), 0f)
                }
                if (bits.width != oldBits.height || bits.height != oldBits.width) {
                    matrix.postScale(
                            bits.width.toFloat() / oldBits.height,
                            bits.height.toFloat() / oldBits.width)
                }
                c.setMatrix(matrix)
            }
            // paint the old artwork atop the new
            c.drawBitmap(oldBits, 0f, 0f, _drawPaint)
            c.setMatrix(Matrix())
        } else {
            c.drawColor(paperColor)
        }

        bitmap = bits
        _paintCanvas = c
    }
}
