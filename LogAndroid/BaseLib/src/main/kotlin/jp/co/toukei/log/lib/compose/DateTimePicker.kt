@file:OptIn(ExperimentalFoundationApi::class)

package jp.co.toukei.log.lib.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp


@Composable
private fun Picker(
    modifier: Modifier,
    height: Dp,
    shadowColor: Color,
    currentDayBackground: Color,
    currentTextColor: Color,
    content: @Composable RowScope.(fontSize: TextUnit, rowHeight: Dp) -> Unit,
) {
    val hh = maxOf(12.dp, height)
    val hp: Float
    val fontSize: TextUnit = LocalDensity.current.run {
        hp = hh.toPx()
        (hh * 0.5F).toSp()
    }
    val brush = Brush.verticalGradient(
        colors = listOf(shadowColor, Color.Transparent, shadowColor)
    )
    Row(
        modifier = modifier
            .fillMaxSize()
            .drawWithContent {
                with(drawContext.canvas.nativeCanvas) {
                    val (w, h) = size
                    val f = h / 2
                    val tl = Offset(0F, f - hp / 2)
                    val a = Size(w, hp)
                    drawRect(
                        currentDayBackground,
                        topLeft = tl,
                        size = a,
                    )
                    val cp = saveLayer(null, null)
                    drawContent()
                    drawRect(
                        color = currentTextColor,
                        topLeft = tl,
                        size = a,
                        blendMode = BlendMode.SrcAtop,
                    )
                    restoreToCount(cp)
                    drawRect(
                        brush = brush
                    )
                }
            }
            .padding(vertical = 8.dp, horizontal = 8.dp),
        content = { content(fontSize, hh) },
    )
}


@Composable
private fun RowD(
    modifier: Modifier,
    dayState: LazyListState,
    rowHeight: Dp,
    textColor: Color,
    fontSize: TextUnit,
    dateStr: (day: Int) -> String,
) {
    LazyColumn(
        modifier = modifier,
        state = dayState,
        flingBehavior = rememberSnapFlingBehavior(lazyListState = dayState),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(
            count = Int.MAX_VALUE,
            key = { it }
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
                    .height(rowHeight)
                    .wrapContentHeight(align = Alignment.CenterVertically),
                text = dateStr(it),
                color = textColor,
                fontSize = fontSize,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun RowH(
    modifier: Modifier,
    hhState: LazyListState,
    rowHeight: Dp,
    textColor: Color,
    fontSize: TextUnit,
    textAlign: TextAlign = TextAlign.Center,
) {
    LazyColumn(
        modifier = modifier,
        state = hhState,
        flingBehavior = rememberSnapFlingBehavior(lazyListState = hhState),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(
            count = Int.MAX_VALUE,
            key = { it }
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
                    .height(rowHeight)
                    .wrapContentHeight(align = Alignment.CenterVertically),
                text = "${it % 24}".padStart(2, '0'),
                color = textColor,
                fontSize = fontSize,
                textAlign = textAlign,
            )
        }
    }
}

@Composable
private fun RowM(
    modifier: Modifier,
    mmState: LazyListState,
    rowHeight: Dp,
    textColor: Color,
    fontSize: TextUnit,
    textAlign: TextAlign = TextAlign.Center,
) {
    LazyColumn(
        modifier = modifier,
        state = mmState,
        flingBehavior = rememberSnapFlingBehavior(lazyListState = mmState),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(
            count = Int.MAX_VALUE,
            key = { it }
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
                    .height(rowHeight)
                    .wrapContentHeight(align = Alignment.CenterVertically),
                text = "${it % 60}".padStart(2, '0'),
                color = textColor,
                fontSize = fontSize,
                textAlign = textAlign,
            )
        }
    }
}

@Composable
fun TimePicker(
    modifier: Modifier,
    hhState: LazyListState,
    mmState: LazyListState,
    height: Dp,
    textColor: Color,
    currentTextColor: Color,
    shadowColor: Color,
    currentDayBackground: Color,
) {
    Picker(
        modifier = modifier,
        height = height,
        shadowColor = shadowColor,
        currentDayBackground = currentDayBackground,
        currentTextColor = currentTextColor,
    ) { fontSize, rowHeight ->
        RowH(
            modifier = Modifier
                .weight(1F),
            hhState = hhState,
            rowHeight = rowHeight,
            textColor = textColor,
            fontSize = fontSize,
            textAlign = TextAlign.End,
        )
        Text(
            modifier = Modifier
                .fillMaxHeight()
                .wrapContentHeight(),
            textAlign = TextAlign.Center,
            text = ":",
            color = textColor,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold,
            style = TextStyle(
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                )
            ),
        )
        RowM(
            modifier = Modifier
                .weight(1F),
            mmState = mmState,
            rowHeight = rowHeight,
            textColor = textColor,
            fontSize = fontSize,
            textAlign = TextAlign.Start,
        )
    }
}

@Composable
fun DatePicker(
    modifier: Modifier,
    dayState: LazyListState,
    height: Dp,
    textColor: Color,
    currentTextColor: Color,
    shadowColor: Color,
    currentDayBackground: Color,
    dateStr: (day: Int) -> String,
) {
    val horizontal = 4.dp
    Picker(
        modifier = modifier,
        height = height,
        shadowColor = shadowColor,
        currentDayBackground = currentDayBackground,
        currentTextColor = currentTextColor,
    ) { fontSize, rowHeight ->
        RowD(
            modifier = Modifier
                .weight(1F)
                .padding(horizontal = horizontal),
            dayState = dayState,
            rowHeight = rowHeight,
            textColor = textColor,
            fontSize = fontSize,
            dateStr = dateStr
        )
    }
}

@Composable
fun DateTimePicker(
    modifier: Modifier,
    dayState: LazyListState,
    hhState: LazyListState,
    mmState: LazyListState,
    height: Dp,
    textColor: Color,
    currentTextColor: Color,
    shadowColor: Color,
    currentDayBackground: Color,
    dateStr: (day: Int) -> String,
) {
    Picker(
        modifier = modifier,
        height = height,
        shadowColor = shadowColor,
        currentDayBackground = currentDayBackground,
        currentTextColor = currentTextColor,
    ) { fontSize, rowHeight ->
        RowD(
            modifier = Modifier
                .weight(3F),
            dayState = dayState,
            rowHeight = rowHeight,
            textColor = textColor,
            fontSize = fontSize,
            dateStr = dateStr
        )
        RowH(
            modifier = Modifier
                .weight(1F),
            hhState = hhState,
            rowHeight = rowHeight,
            textColor = textColor,
            fontSize = fontSize,
            textAlign = TextAlign.End,
        )
        Text(
            modifier = Modifier
                .fillMaxHeight()
                .wrapContentHeight(),
            textAlign = TextAlign.Center,
            text = ":",
            color = textColor,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold,
            style = TextStyle(
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                )
            ),
        )
        RowM(
            modifier = Modifier
                .weight(1F),
            mmState = mmState,
            rowHeight = rowHeight,
            textColor = textColor,
            fontSize = fontSize,
            textAlign = TextAlign.Start,
        )
    }
}
