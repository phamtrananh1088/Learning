package jp.co.toukei.log.lib.compose

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.BlendModeCompat
import androidx.core.graphics.setBlendMode
import androidx.core.graphics.withTranslation
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


fun Modifier.longPressRepeat(
    delayMillis: Long = 400,
    stepMillis: Long = 50,
    onPressingRepeat: (Int) -> Unit,
): Modifier = composed {
    val interactionSource = remember { MutableInteractionSource() }
    val l = rememberUpdatedState(newValue = onPressingRepeat)
    pointerInput(interactionSource) {
        coroutineScope {
            awaitEachGesture {
                val down = awaitFirstDown()
                down.consume()
                var d = delayMillis
                val dp = PressInteraction.Press(down.position)
                val j = launch {
                    interactionSource.emit(dp)
                    var a = 0
                    while (true) {
                        l.value(a++)
                        delay(maxOf(d, stepMillis))
                        d -= stepMillis
                    }
                }
                val e = waitForUpOrCancellation()
                e?.consume()
                j.cancel()
                launch {
                    interactionSource.emit(
                        if (e == null) PressInteraction.Cancel(dp) else PressInteraction.Release(dp)
                    )
                }
            }
        }
    }.indication(interactionSource, rememberRipple())
}

fun Modifier.delegatePress(
    interactionSource: MutableInteractionSource,
    onTap: () -> Unit,
    delegatePositionInParent: State<Offset>,
) = composed {
    val tap by rememberUpdatedState(newValue = onTap)
    val m = remember { mutableStateOf(Offset.Zero) }
    positionInParent(m)
        .pointerInput(interactionSource) {
            coroutineScope {
                awaitEachGesture {
                    val down = awaitFirstDown()
                    val d = down.position + m.value - delegatePositionInParent.value
                    val dp = PressInteraction.Press(d)
                    launch {
                        interactionSource.emit(dp)
                    }
                    val e = waitForUpOrCancellation()
                    launch {
                        interactionSource.emit(
                            if (e == null) PressInteraction.Cancel(dp)
                            else PressInteraction.Release(dp)
                        )
                    }
                    launch {
                        if (e != null) {
                            tap()
                        }
                    }
                }
            }
        }
}

fun Modifier.onFocusSelectAll(
    tfv: MutableState<TextFieldValue>,
) = composed {
    var sa by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(sa) {
        if (sa) {
            val v = tfv.value
            tfv.value = v.copy(selection = TextRange(v.text.length, 0))
            sa = false
        }
    }
    onFocusChanged {
        if (it.isFocused) {
            sa = true
        }
    }
}

fun Modifier.onFocusChanged(
    isFocused: MutableState<Boolean>,
) = onFocusChanged {
    isFocused.value = it.isFocused
}

fun Modifier.positionInParent(
    state: MutableState<Offset>,
) = onGloballyPositioned {
    state.value = it.positionInParent()
}

fun Modifier.topShadow(
    height: Dp = 4.dp,
    color: Color = Color(0x22000000),
): Modifier {
    return this
        .wrapContentHeight(unbounded = true)
        .fillMaxWidth()
        .graphicsLayer()
        .drawWithContent {
            drawContent()
            val sh = height.toPx()
            val (w) = size
            drawContext.canvas.nativeCanvas.withTranslation(y = -sh) {
                drawRect(
                    0F,
                    0F,
                    w,
                    sh,
                    android.graphics
                        .Paint()
                        .apply {
                            style = android.graphics.Paint.Style.FILL
                            setBlendMode(BlendModeCompat.SRC_OVER)
                            shader = LinearGradientShader(
                                from = Offset(0F, sh),
                                to = Offset.Zero,
                                colors = listOf(color, Color.Transparent),
                            )
                        },
                )
            }
        }
}

fun Modifier.bottomShadow(
    height: Dp = 4.dp,
    color: Color = Color(0x22000000),
): Modifier {
    return this
        .wrapContentHeight(unbounded = true)
        .fillMaxWidth()
        .graphicsLayer()
        .drawWithContent {
            drawContent()
            val sh = height.toPx()
            val (w, h) = size
            drawContext.canvas.nativeCanvas.withTranslation(y = h) {
                drawRect(
                    0F,
                    0F,
                    w,
                    sh,
                    android.graphics
                        .Paint()
                        .apply {
                            style = android.graphics.Paint.Style.FILL
                            setBlendMode(BlendModeCompat.SRC_OVER)
                            shader = LinearGradientShader(
                                from = Offset.Zero,
                                to = Offset(0F, sh),
                                colors = listOf(color, Color.Transparent),
                            )
                        },
                )
            }
        }
}

fun Modifier.verticalScrollbar(
    state: LazyListState,
    width: Dp = 4.dp,
    color: Color = Color.Gray.copy(alpha = 0.5F),
): Modifier = composed {
    val scrolling = state.isScrollInProgress
    val alpha by animateFloatAsState(
        targetValue = if (scrolling) 1f else 0f,
        animationSpec = tween(durationMillis = if (scrolling) 150 else 500),
        label = ""
    )
    drawWithContent {
        drawContent()
        val (w, h) = size
        val s = width.toPx()

        val i = state.layoutInfo

        val v = i.visibleItemsInfo
        val vf = v.firstOrNull()
        val vl = v.lastOrNull()

        if (vf != null && vl != null && (scrolling || alpha > 0.0f)) {
            val elementHeight = (h) / i.totalItemsCount

            val t = vf.index * elementHeight - elementHeight * vf.offset / vf.size
            val b = minOf(h, vl.index * elementHeight - elementHeight * (vl.offset - h) / vl.size)

            drawRoundRect(
                color = color,
                topLeft = Offset(
                    w - s,
                    t
                ),
                size = Size(
                    width = s,
                    b - t
                ),
                alpha = alpha,
                cornerRadius = CornerRadius(8F),
            )
        }
    }
}

fun Modifier.horizontalScrollBar(
    state: LazyListState,
    height: Dp = 4.dp,
    color: Color = Color.Gray.copy(alpha = 0.5F),
): Modifier = composed {
    val scrolling = state.isScrollInProgress
    val alpha by animateFloatAsState(
        targetValue = if (scrolling) 1f else 0f,
        animationSpec = tween(durationMillis = if (scrolling) 150 else 500),
        label = ""
    )
    drawWithContent {
        drawContent()
        val (w, h) = size
        val s = height.toPx()

        val i = state.layoutInfo

        val v = i.visibleItemsInfo
        val vf = v.firstOrNull()
        val vl = v.lastOrNull()

        if (vf != null && vl != null && (scrolling || alpha > 0.0f)) {
            val elementWidth = (w) / i.totalItemsCount

            val t = vf.index * elementWidth - elementWidth * vf.offset / vf.size
            val b = minOf(w, vl.index * elementWidth - elementWidth * (vl.offset - w) / vl.size)

            drawRoundRect(
                color = color,
                topLeft = Offset(
                    t,
                    h - s
                ),
                size = Size(
                    b - t,
                    s
                ),
                alpha = alpha,
                cornerRadius = CornerRadius(8F),
            )
        }
    }
}

fun Modifier.minWidthMatchHeight(): Modifier {
    return layout { measurable, constraints ->
        val p = measurable.measure(constraints.run {
            copy(minWidth = measurable.minIntrinsicHeight(minWidth))
        })
        val w = p.width
        val h = p.height
        layout(w, h) {
            p.place(0, 0)
        }
    }
}
