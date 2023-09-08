package jp.co.toukei.log.lib.compose

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.UiComposable
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import jp.co.toukei.log.lib.fastFoldWithPrev
import jp.co.toukei.log.lib.fastForEachWithNext
import jp.co.toukei.log.lib.util.NTuple4
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext

@Composable
fun <T : Any, K> DecoratedList(
    modifier: Modifier,
    element: (Int) -> T?,
    boxTopPadding: Dp,
    boxWidth: Dp,
    state: LazyListState = rememberLazyListState(),
    sameKey: (T) -> K,
    box: @Composable @UiComposable BoxWithConstraintsScope.(T) -> Unit,
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical = if (reverseLayout) Arrangement.Bottom else Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    content: LazyListScope.() -> Unit,
) {
    Box(
        modifier = modifier
    ) {
        LazyListDecoration(
            modifier = Modifier
                .fillMaxHeight()
                .width(boxWidth),
            state = state,
            boxTopPadding = boxTopPadding,
            element = element,
            sameKey = sameKey,
            box
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = state,
            contentPadding = PaddingValues(start = boxWidth),
            reverseLayout = reverseLayout,
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment,
            flingBehavior = flingBehavior,
            userScrollEnabled = userScrollEnabled,
            content = content
        )
    }
}

@Composable
fun <T : Any, K> LazyListDecoration(
    modifier: Modifier,
    state: LazyListState,
    boxTopPadding: Dp,
    element: (Int) -> T?,
    sameKey: (T) -> K,
    box: @Composable @UiComposable BoxWithConstraintsScope.(T) -> Unit,
) {
    Box(
        modifier = modifier
    ) {
        val boxTopPaddingPx = LocalDensity.current.run { boxTopPadding.roundToPx() }
        produceState(emptyList(), state) {
            snapshotFlow {
                state.layoutInfo.visibleItemsInfo
            }.collectLatest { info ->
                value = withContext(Dispatchers.IO) {
                    ArrayList<NTuple4<T, K, Int, Int?>>(info.size).also { arr ->
                        info.fastFoldWithPrev(null as NTuple4<T, K, Int, Int?>?) { acc, p, e ->
                            val v = element(e.index)
                            if (v == null) {
                                null
                            } else {
                                val k = sameKey(v)
                                if (acc != null && k == acc.t2) acc else {
                                    NTuple4(v, k, e.offset, p?.run { offset + size }).also {
                                        arr += it
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }.value.fastForEachWithNext { e, n ->
            BoxWithConstraints(
                modifier = Modifier
                    .layout { measurable, constraints ->
                        val p = measurable.measure(constraints)
                        val h = p.height
                        layout(p.width, h) {
                            val t = e.t3.coerceAtLeast(boxTopPaddingPx)
                            p.place(
                                x = 0,
                                y = n?.t4?.let { t.coerceAtMost(it - h) } ?: t
                            )
                        }
                    },
            ) {
                box(e.t1)
            }
        }
    }
}
