@file:OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)

package jp.co.toukei.log.trustar.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.window.DialogProperties
import com.sebaslogen.resaca.rememberScoped
import jp.co.toukei.log.lib.compose.DateTimePicker
import jp.co.toukei.log.lib.compose.DefaultConfirmDialog
import jp.co.toukei.log.lib.compose.animateScrollTo
import jp.co.toukei.log.lib.compose.centerItem
import jp.co.toukei.log.lib.compose.scrollFirstToCenterState
import jp.co.toukei.log.lib.ctx.Ctx
import jp.co.toukei.log.trustar.Config
import jp.co.toukei.log.trustar.R
import third.Clock

@JvmInline
private value class AA(
    val timezoneDate: Long,
) {
    val day: Int
        get() = (timezoneDate / 86400_000).toInt()
    val hourIndex: Int
        get() = 24 * 44739242 + Clock(timezoneDate).hours
    val minIndex: Int
        get() = 60 * 17895697 + Clock(timezoneDate).minutes
}

@Composable
fun DatePickerDialog(
    modifier: Modifier = Modifier,
    defaultDate0: Long? = null,
    defaultDate1: Long? = null,
    onDismissRequest: () -> Unit,
    onSelected: (Long, Long) -> Unit,
) {
    var err by rememberScoped {
        mutableStateOf(false)
    }
    AlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        properties = DialogProperties(),
    ) {
        Surface(
            modifier = modifier,
            shape = MaterialTheme.shapes.medium,
            color = AlertDialogDefaults.containerColor,
            tonalElevation = AlertDialogDefaults.TonalElevation,
        ) {
            DatePickerPager(
                modifier = Modifier.fillMaxSize(),
                tab0 = stringResource(id = R.string.pick_start_time),
                tab1 = stringResource(id = R.string.pick_end_time),
                height = 32.dp,
                defaultDate0 = defaultDate0 ?: System.currentTimeMillis(),
                defaultDate1 = defaultDate1 ?: System.currentTimeMillis(),
                timeZoneOffset = Config.timeZone.rawOffset,
                textColor = MaterialTheme.typography.bodyLarge.color,
                currentTextColor = MaterialTheme.colorScheme.primary,
                shadowColor = MaterialTheme.colorScheme.onPrimary,
                currentDayBackground = Color.Black.copy(alpha = 0.1F),
                dateStr = { o, t ->
                    if (o == 0) Ctx.context.getString(R.string.today)
                    else Config.dateFormatter5.format(t)
                }
            ) { start, end ->
                if (start >= end) {
                    err = true
                } else {
                    onSelected(start, end)
                }
            }
        }
    }
    if (err) {
        DefaultConfirmDialog(
            content = R.string.invalid_time_range,
        ) {
            err = false
        }
    }
}

@Composable
fun DatePickerPager(
    modifier: Modifier,
    tab0: String,
    tab1: String,
    height: Dp,
    defaultDate0: Long,
    defaultDate1: Long,
    timeZoneOffset: Int,
    textColor: Color,
    currentTextColor: Color,
    shadowColor: Color,
    currentDayBackground: Color,
    dateStr: (offset: Int, time: Long) -> String,
    date: (Long, Long) -> Unit,
) {
    val aa0 = AA(defaultDate0 + timeZoneOffset)
    val aa1 = AA(defaultDate1 + timeZoneOffset)

    val today = AA(System.currentTimeMillis() + timeZoneOffset).day

    val dayState = scrollFirstToCenterState(aa0.day)
    val hhState = scrollFirstToCenterState(aa0.hourIndex)
    val mmState = scrollFirstToCenterState(aa0.minIndex)

    val dayState1 = scrollFirstToCenterState(aa1.day)
    val hhState1 = scrollFirstToCenterState(aa1.hourIndex)
    val mmState1 = scrollFirstToCenterState(aa1.minIndex)

    val dateString: (Int) -> String = { d ->
        dateStr(d - today, d * 86400_000L)
    }

    Column(
        modifier = modifier
    ) {
        val coroutineScope = rememberCoroutineScope()
        val pagerState = rememberPagerState {
            2
        }

        val scrollTo: (Int) -> Unit = {
            pagerState.animateScrollTo(coroutineScope, it)
        }

        TabRow(
            selectedTabIndex = pagerState.currentPage,
            indicator = { tabPositions ->
                if (pagerState.currentPage < tabPositions.size) {
                    SecondaryIndicator(
                        Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
                    )
                }
            },
            divider = {},
        ) {
            listOf(
                tab0,
                tab1
            ).forEachIndexed { index, tab ->
                Text(
                    text = tab,
                    modifier = Modifier
                        .wrapContentHeight()
                        .clickable {
                            scrollTo(index)
                        }
                        .padding(16.dp),
                    textAlign = TextAlign.Center,
                )
            }
        }
        HorizontalPager(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F),
            state = pagerState,
            userScrollEnabled = false,
        ) {
            val m = Modifier
            when (it) {
                0 -> {
                    DateTimePicker(
                        modifier = m,
                        dayState = dayState,
                        hhState = hhState,
                        mmState = mmState,
                        height = height,
                        textColor = textColor,
                        currentTextColor = currentTextColor,
                        shadowColor = shadowColor,
                        currentDayBackground = currentDayBackground,
                        dateStr = dateString
                    )
                }

                1 -> {
                    DateTimePicker(
                        modifier = m,
                        dayState = dayState1,
                        hhState = hhState1,
                        mmState = mmState1,
                        height = height,
                        textColor = textColor,
                        currentTextColor = currentTextColor,
                        shadowColor = shadowColor,
                        currentDayBackground = currentDayBackground,
                        dateStr = dateString
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.End)
                .padding(horizontal = 8.dp)
        ) {
            CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.primary) {
                val textStyle =
                    MaterialTheme.typography.labelLarge
                ProvideTextStyle(value = textStyle) {
                    TextButton(onClick = {
                        when (pagerState.currentPage) {
                            0 -> {
                                scrollTo(1)
                            }

                            1 -> {
                                val d0 = millis(dayState, hhState, mmState, timeZoneOffset)
                                val d1 = millis(dayState1, hhState1, mmState1, timeZoneOffset)
                                if (d0 != null) {
                                    if (d1 != null) {
                                        date(d0, d1)
                                    }
                                } else {
                                    scrollTo(0)
                                }
                            }
                        }
                    }) {
                        Text(text = stringResource(id = R.string.determine))
                    }
                }
            }
        }
    }
}

private fun millis(
    dayState: LazyListState,
    hhState: LazyListState,
    mmState: LazyListState,
    timeZoneOffset: Int,
): Long? {
    val d = dayState.centerItem()?.index
    val h = hhState.centerItem()?.index?.mod(24)
    val m = mmState.centerItem()?.index?.mod(60)
    return if (d != null && h != null && m != null) {
        (((d * 24L) + h) * 60 + m) * 60_000 - timeZoneOffset
    } else null
}

private fun Modifier.pagerTabIndicatorOffset(
    pagerState: PagerState,
    tabPositions: List<TabPosition>,
    pageIndexMapping: (Int) -> Int = { it },
): Modifier = layout { measurable, constraints ->
    if (tabPositions.isEmpty()) {
        // If there are no pages, nothing to show
        layout(constraints.maxWidth, 0) {}
    } else {
        val currentPage = minOf(tabPositions.lastIndex, pageIndexMapping(pagerState.currentPage))
        val currentTab = tabPositions[currentPage]
        val previousTab = tabPositions.getOrNull(currentPage - 1)
        val nextTab = tabPositions.getOrNull(currentPage + 1)
        val fraction = pagerState.currentPageOffsetFraction
        val indicatorWidth = if (fraction > 0 && nextTab != null) {
            lerp(currentTab.width, nextTab.width, fraction).roundToPx()
        } else if (fraction < 0 && previousTab != null) {
            lerp(currentTab.width, previousTab.width, -fraction).roundToPx()
        } else {
            currentTab.width.roundToPx()
        }
        val indicatorOffset = if (fraction > 0 && nextTab != null) {
            lerp(currentTab.left, nextTab.left, fraction).roundToPx()
        } else if (fraction < 0 && previousTab != null) {
            lerp(currentTab.left, previousTab.left, -fraction).roundToPx()
        } else {
            currentTab.left.roundToPx()
        }
        val placeable = measurable.measure(
            Constraints(
                minWidth = indicatorWidth,
                maxWidth = indicatorWidth,
                minHeight = 0,
                maxHeight = constraints.maxHeight
            )
        )
        layout(constraints.maxWidth, maxOf(placeable.height, constraints.minHeight)) {
            placeable.placeRelative(
                indicatorOffset,
                maxOf(constraints.minHeight - placeable.height, 0)
            )
        }
    }
}
