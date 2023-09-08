package jp.co.toukei.log.common.compose

import androidx.compose.animation.core.FloatExponentialDecaySpec
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculateCentroid
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.input.pointer.AwaitPointerEventScope
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChanged
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.input.pointer.util.addPointerInputChange
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.util.fastAny
import androidx.compose.ui.util.fastForEach
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.cancellation.CancellationException
import kotlin.math.abs
import kotlin.math.pow

@Composable
fun ZoomImage(
    modifier: Modifier,
    bitmap: ImageBitmap,
    zoomMin0: Float,
    zoomMax: Float,
    onClick: () -> Unit,
) {
    val click by rememberUpdatedState(newValue = onClick)
    val bitmapPainter = remember(bitmap) {
        BitmapPainter(bitmap)
    }
    val transformOriginZero = TransformOrigin(0F, 0F)

    val bitmapSize = bitmapPainter.intrinsicSize
    val bw = bitmapSize.width
    val bh = bitmapSize.height


    BoxWithConstraints(
        modifier
    ) {
        val mw = constraints.maxWidth
        val mh = constraints.maxHeight
        val initScale = minOf(mw / bw, mh / bh)
        val zoomMin = minOf(zoomMax, zoomMin0 * initScale)
        var mScale by remember(bitmapPainter) {
            mutableFloatStateOf(initScale)
        }
        var mOffset by remember(bitmapPainter) {
            mutableStateOf(
                Offset(
                    x = (mw - mScale * bw) / 2,
                    y = (mh - mScale * bh) / 2,
                )
            )
        }
        val setOffset = fun(scale: Float, x: Float, y: Float) {
            val ox = mw - bw * scale
            val oy = mh - bh * scale
            mOffset = Offset(
                x = if (ox < 0) x.coerceIn(ox, 0F) else x.coerceIn(0F, ox),
                y = if (oy < 0) y.coerceIn(oy, 0F) else y.coerceIn(0F, oy),
            )
        }
        val move = fun(panX: Float, panY: Float) {
            val (x, y) = mOffset
            setOffset(mScale, x + panX, y + panY)
        }
        val transform = fun(centroid: Offset, panX: Float, panY: Float, scale: Float) {
            val os = mScale
            mScale = scale
            val (tx, ty) = mOffset.scaleTo(centroid, scale / os)
            setOffset(scale, tx + panX, ty + panY)
        }
        val onGesture = fun(centroid: Offset, pan: Offset, zoom: Float) {
            transform(centroid, pan.x, pan.y, (mScale * zoom).coerceIn(zoomMin, zoomMax))
        }
        val zoomByY = fun(centroid: Offset, y: Float) {
            onGesture(centroid, Offset.Zero, 1F + y / 500)
        }
        val moveDecay = FloatExponentialDecaySpec(1F)


        val s1 = mScale
        val o1 = mOffset
        Layout(
            {},
            Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTransform1(
                        onGesture = onGesture,
                        onGestureFling = { centroid, velocity, zoomVelocityY ->
                            launch {
                                moveDecay.myFling(velocity.x, velocity.y) { x, y ->
                                    move(x, y)
                                    abs(minOf(x, y)) > 0.01
                                }
                            }
                            launch {
                                moveDecay.myFling(zoomVelocityY / 2) { y ->
                                    zoomByY(centroid, y)
                                    abs(y) > 1
                                }
                            }
                        },
                        onDoubleTap = { down ->
                            val t = mScale
                            val t2 = t * 3
                            val target = when {
                                t > zoomMax * 0.99 -> initScale
                                t2 > zoomMax -> zoomMax
                                t2 < zoomMax -> t2
                                else -> initScale
                            }
                            launch {
                                pow2(mScale, target, 150) { z ->
                                    transform(down, 0F, 0F, z)
                                    abs(z) > 0.001
                                }
                            }
                        },
                        onDoubleTapMove = { down, position, previous ->
                            val y = (position - previous).y
                            zoomByY(down, y)
                        },
                        onDoubleTapMoveFling = { down, velocity ->
                            launch {
                                moveDecay.myFling(velocity.y) { y ->
                                    zoomByY(down, y)
                                    abs(y) > 1
                                }
                            }
                        },
                        onClick = click
                    )
                }
                .graphicsLayer(
                    scaleX = s1,
                    scaleY = s1,
                    translationX = o1.x,
                    translationY = o1.y,
                    transformOrigin = transformOriginZero
                )
                .paint(
                    bitmapPainter,
                    alignment = Alignment.TopStart,
                    contentScale = ContentScale.None,
                    alpha = 1F,
                    colorFilter = null
                )
        ) { _, constraints ->
            layout(constraints.minWidth, constraints.minHeight) {}
        }
    }
}

private fun Offset.scaleTo(at: Offset, scale: Float): Offset {
    val (cx, cy) = at
    return Offset(
        x = scale * (x - cx) + cx,
        y = scale * (y - cy) + cy,
    )
}

private suspend fun PointerInputScope.detectTransform1(
    onGesture: (centroid: Offset, pan: Offset, zoom: Float) -> Unit,
    onGestureFling: CoroutineScope.(centroid: Offset, velocity: Velocity, zoomVelocityY: Float) -> Unit,
    onDoubleTap: CoroutineScope.(down: Offset) -> Unit,
    onDoubleTapMove: (down: Offset, position: Offset, previous: Offset) -> Unit,
    onDoubleTapMoveFling: CoroutineScope.(down: Offset, velocity: Velocity) -> Unit,
    onClick: () -> Unit,
) = coroutineScope {
    awaitPointerEventScope {
        val vc = viewConfiguration
        val slop = vc.touchSlop
        val slop2 = slop * slop
        val dtm = vc.doubleTapMinTimeMillis
        val dtt = vc.doubleTapTimeoutMillis

        val vt = VelocityTracker()
        val vt2 = VelocityTracker()
        var lastUp: PointerInputChange? = null

        val scope = CoroutineScope(EmptyCoroutineContext)


        while (isActive) {
            try {
                val l = lastUp
                lastUp = null

                val firstDown = if (l != null) {
                    val down = withTimeoutOrNull(dtt) {
                        awaitFirstDown(requireUnconsumed = false)
                    }
                    if (down == null) {
                        onClick()
                        continue
                    }
                    down
                } else {
                    awaitFirstDown(requireUnconsumed = false)
                }

                scope.coroutineContext.cancelChildren()
                vt.resetTracking()
                if (l != null && (firstDown.uptimeMillis - l.uptimeMillis) in dtm..dtt) {
                    var p = firstDown
                    val fp = firstDown.position
                    val fid = firstDown.id
                    var pastTouchSlop = false
                    while (true) {
                        val event = awaitPointerEvent()
                        val ec = event.changes
                        if (ec.size != 1) break
                        val c = ec[0]
                        if (c.id != fid) break
                        val cp = c.position
                        val pp = p.position
                        vt.addPointerInputChange(c)
                        if (c.pressed) {
                            if (!pastTouchSlop && fp.minus(cp).getDistanceSquared() > slop2) {
                                pastTouchSlop = true
                            }
                            if (pastTouchSlop) {
                                onDoubleTapMove(fp, cp, pp)
                                c.consume()
                            }
                        } else {
                            if (p === firstDown) {
                                scope.onDoubleTap(fp)
                            } else {
                                if (pastTouchSlop) {
                                    scope.onDoubleTapMoveFling(fp, vt.calculateVelocity())
                                }
                            }
                            break
                        }
                        p = c
                    }
                } else {
                    var zoom = 1F
                    var move = Offset.Zero
                    var pastTouchSlop = false
                    var centroid = Offset.Zero
                    vt2.resetTracking()
                    while (true) {
                        val event = awaitPointerEvent()
                        val ec = event.changes
                        if (ec.fastAny { it.isConsumed }) break

                        val first = ec[0]

                        if (!ec.anyPressed()) {
                            if (pastTouchSlop) {
                                scope.onGestureFling(
                                    centroid,
                                    vt.calculateVelocity(),
                                    vt2.calculateVelocity().y
                                )
                            } else {
                                if (ec.size == 1) {
                                    val r = ec[0]
                                    if (r.id == firstDown.id) {
                                        lastUp = r
                                    }
                                }
                            }
                            break
                        }
                        val firstT = first.uptimeMillis

                        val xcc = event.calculateCentroid(useCurrent = true)
                        val xcp = event.calculateCentroid(useCurrent = false)
                        val panChange = if (xcc == Offset.Unspecified) Offset.Zero else xcc - xcp

                        move += panChange
                        vt.addPosition(firstT, move)

                        val xsc = event.calculateCentroidSize1(true, xcc, xcp)
                        val xsp = event.calculateCentroidSize1(false, xcc, xcp)
                        val zoomChange = if (xsc == 0f || xsp == 0f) 1F else xsc / xsp
                        if (zoomChange != 1F) {
                            vt2.addPosition(firstT, Offset(0F, xsc))
                        }

                        centroid = xcp

                        if (!pastTouchSlop) {
                            zoom *= zoomChange

                            if (abs(1 - zoom) * xsp > slop || move.getDistanceSquared() > slop2) {
                                pastTouchSlop = true
                            }
                        }

                        if (pastTouchSlop) {
                            if (zoomChange != 1F || panChange != Offset.Zero) {
                                onGesture(xcp, panChange, zoomChange)
                            }
                            ec.fastForEach {
                                if (it.positionChanged()) {
                                    it.consume()
                                }
                            }
                        }
                    }
                }
                awaitAllPointersUp()
            } catch (e: CancellationException) {
                if (isActive) {
                    awaitAllPointersUp()
                } else {
                    throw e
                }
            }
        }
    }
}


private fun List<PointerInputChange>.anyPressed(): Boolean {
    return fastAny { it.pressed }
}

private suspend fun AwaitPointerEventScope.awaitAllPointersUp() {
    if (currentEvent.changes.anyPressed()) {
        do {
            val events = awaitPointerEvent(PointerEventPass.Final)
        } while (events.changes.anyPressed())
    }
}

private fun PointerEvent.calculateCentroidSize1(
    useCurrent: Boolean,
    centroid: Offset,
    previousCentroid: Offset,
): Float {
    val i = if (useCurrent) centroid else previousCentroid
    if (i == Offset.Unspecified) {
        return 0f
    }
    var distanceToCentroid = 0f
    var distanceWeight = 0
    changes.fastForEach { change ->
        if (change.pressed && change.previousPressed) {
            val position = if (useCurrent) change.position else change.previousPosition
            distanceToCentroid += (position - i).getDistance()
            distanceWeight++
        }
    }
    return distanceToCentroid / distanceWeight.toFloat()
}

private suspend fun FloatExponentialDecaySpec.myFling(
    velocity1: Float,
    velocity2: Float,
    delay: Long = 2,
    block: (Float, Float) -> Boolean
) {
    val start = System.nanoTime()
    val md = maxOf(
        getDurationNanos(0F, velocity1),
        getDurationNanos(0F, velocity2),
    )
    var ox = 0F
    var oy = 0F
    val d = velocity2 / velocity1
    do {
        delay(delay)
        val t = System.nanoTime() - start
        if (t > md) break
        val cx = getValueFromNanos(t, 0F, velocity1)
        val cy = cx * d
        val rx = cx - ox
        val ry = cy - oy
        ox = cx
        oy = cy
    } while (block(rx, ry))
}

private suspend fun FloatExponentialDecaySpec.myFling(
    velocity1: Float,
    delay: Long = 2,
    block: (Float) -> Boolean
) {
    val start = System.nanoTime()
    val md = getDurationNanos(0F, velocity1)
    var ox = 0F
    do {
        delay(delay)
        val t = System.nanoTime() - start
        if (t > md) break
        val cx = getValueFromNanos(t, 0F, velocity1)
        val rx = cx - ox
        ox = cx
    } while (block(rx))
}

private suspend fun pow2(
    from: Float,
    to: Float,
    duration: Long,
    delay: Long = 2,
    block: (Float) -> Boolean
) {
    val d = to - from
    val start = System.nanoTime()
    do {
        delay(delay)
        val t = (System.nanoTime() - start) / 1000_000
        if (t > duration) break
        val r = (1 - ((t.toFloat() / duration) - 1).pow(2))
        val v = from + r * d
    } while (block(v))
    block(to)
}
