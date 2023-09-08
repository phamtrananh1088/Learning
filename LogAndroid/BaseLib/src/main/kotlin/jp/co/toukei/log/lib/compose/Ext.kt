@file:OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class
)

package jp.co.toukei.log.lib.compose

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import io.reactivex.rxjava3.core.Flowable
import jp.co.toukei.log.lib.ctx.Ctx
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


@Composable
fun LoadState.delayByState(timeMillis: (LoadState) -> Long?): State<LoadState?> {
    val loadState = this
    return produceState<LoadState?>(
        initialValue = null,
        key1 = loadState
    ) {
        timeMillis(loadState)?.let {
            delay(it)
        }
        value = loadState
    }
}

inline fun <T> LazyListScope.itemsIndexed1(
    items: List<T>,
    noinline key: ((item: T) -> Any)? = null,
    crossinline itemContent: @Composable LazyItemScope.(index: Int, item: T) -> Unit,
) = items(
    count = items.size,
    key = if (key != null) { index: Int -> key(items[index]) } else null,
) {
    itemContent(it, items[it])
}

@Composable
fun <T> Iterable<T>.rememberDistinctBy(selector: (T) -> Any): List<T> {
    return remember(this) { distinctBy(selector) }
}

val LazyListState.lastItemVisible: Boolean?
    @Composable
    get() {
        val r by remember {
            derivedStateOf {
                val info = layoutInfo
                val l = info.totalItemsCount - 1
                val v = info.visibleItemsInfo.lastOrNull()
                if (v == null) null else v.index == l
            }
        }
        return r
    }

val LazyListState.lastVisibleItemKey: Any?
    @Composable
    get() {
        val r by remember {
            derivedStateOf {
                val info = layoutInfo
                info.visibleItemsInfo.lastOrNull()?.key
            }
        }
        return r
    }

val LazyListState.lastItemKeyWhenVisible: Any?
    @Composable
    get() {
        val r by remember {
            derivedStateOf {
                val info = layoutInfo
                val l = info.totalItemsCount - 1
                val v = info.visibleItemsInfo.lastOrNull()
                if (v == null || v.index != l) null else v.key
            }
        }
        return r
    }

suspend fun LazyListState.scrollToLast() {
    val c = layoutInfo.totalItemsCount
    if (c > 0) scrollToItem(c - 1)
}

fun LazyListState.centerItem(): LazyListItemInfo? {
    val h = layoutInfo.viewportSize.height / 2
    return layoutInfo.visibleItemsInfo.firstOrNull { it.offset + it.size > h }
}

fun LazyListState.itemByIndex(index: Int): LazyListItemInfo? {
    return layoutInfo.visibleItemsInfo.firstOrNull { it.index == index }
}

suspend fun SnackbarHostState.replaceMessage(
    message: String,
    duration: SnackbarDuration = SnackbarDuration.Short,
) {
    currentSnackbarData?.dismiss()
    showSnackbar(message, duration = duration)
}

fun SnackbarHostState.replaceMessage(
    scope: CoroutineScope,
    message: String,
    duration: SnackbarDuration = SnackbarDuration.Short,
) {
    scope.launch { replaceMessage(message, duration) }
}

fun SnackbarHostState.replaceMessage(
    scope: CoroutineScope,
    @StringRes messageId: Int,
    duration: SnackbarDuration = SnackbarDuration.Short,
) {
    scope.launch { replaceMessage(Ctx.context.getString(messageId), duration) }
}

suspend fun SnackbarHostState.replaceMessage(
    @StringRes messageId: Int,
    duration: SnackbarDuration = SnackbarDuration.Short,
) {
    replaceMessage(Ctx.context.getString(messageId), duration)
}

fun DestinationsNavigator.navigateUpLambda(): () -> Unit {
    return { navigateUp() }
}

@Composable
fun <T : R, R> Flow<T>.recollectState(
    recollectInitial: R,
    recollectKey: Any?,
): State<R> {
    val result = remember(recollectKey) {
        mutableStateOf(recollectInitial)
    }
    LaunchedEffect(recollectKey, this) {
        collect {
            result.value = it
        }
    }
    return result
}

@SuppressLint("ComposableModifierFactory")
@Composable
fun Modifier.rememberMaxWidth(min: Dp, key: Any? = null): Modifier {
    val density = LocalDensity.current
    var firstColMaxWidth by remember(key) { mutableIntStateOf(0) }
    return remember(key1 = firstColMaxWidth) {
        widthIn(min = minOf(min, density.run { firstColMaxWidth.toDp() }))
            .onSizeChanged {
                val d = it.width
                if (d > firstColMaxWidth) {
                    firstColMaxWidth = d
                }
            }
    }
}


@Composable
fun RowScope.SecCol(
    text: String,
    onClick: (() -> Unit)? = null,
    textColor: Color = Color.Unspecified,
    showArrowColor: Color? = null,
    selectable: Boolean = true,
) {
    val s = selectable && text.isNotEmpty()
    val t: @Composable RowScope.() -> Unit = {
        val content = @Composable {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = text,
                color = textColor,
                fontSize = 14.sp,
            )
        }
        if (s) {
            SelectionContainer(
                modifier = Modifier.fillMaxWidth(),
                content = content
            )
        } else {
            content()
        }
    }

    Row(
        modifier = Modifier
            .weight(1F)
            .run { if (onClick != null) clickable(onClick = onClick) else this }
            .padding(horizontal = 8.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        content = if (showArrowColor == null) t else { ->
            Row(
                modifier = Modifier.weight(1F),
                content = t
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = showArrowColor,
            )
        }
    )
}

fun PagerState.animateScrollTo(coroutineScope: CoroutineScope, page: Int) {
    coroutineScope.launch {
        animateScrollToPage(page)
    }
}

@Composable
fun <R : Any, T : R> Flowable<T>.subscribeAsState2(initial: R): State<R> {
    val state = remember(this) {
        mutableStateOf(initial)
    }
    DisposableEffect(this) {
        val d = subscribe(
            { state.value = it },
            {},
        )
        onDispose {
            d.dispose()
        }
    }
    return state
}

@Composable
fun scrollFirstToCenterState(
    initialFirstVisibleItemIndex: Int,
): LazyListState {
    val state: LazyListState = rememberLazyListState(
        initialFirstVisibleItemIndex = initialFirstVisibleItemIndex
    )
    var ftc by rememberSaveable {
        mutableStateOf(true)
    }
    if (ftc) {
        //workaround...
        val s = state.isScrollInProgress
        LaunchedEffect(s) {
            launch(Dispatchers.Main.immediate) {
                if (!s) {
                    state.layoutInfo.visibleItemsInfo.firstOrNull()?.let {
                        state.scrollToItem(
                            index = it.index,
                            scrollOffset = (it.size - state.layoutInfo.viewportSize.height) / 2
                        )
                        ftc = false
                    }
                }
            }
        }
        remember {
            derivedStateOf { state.layoutInfo.visibleItemsInfo.firstOrNull() }
        }.value?.let {
            LaunchedEffect(Unit) {
                launch(Dispatchers.Main.immediate) {
                    state.scrollToItem(
                        index = it.index,
                        scrollOffset = (it.size - state.layoutInfo.viewportSize.height) / 2
                    )
                    ftc = false
                }
            }
        }
    }
    return state
}

fun FocusManager.clearFocusOnActionDone(): KeyboardActions {
    return KeyboardActions(onDone = { clearFocus() })
}

@Composable
fun DefaultTopBar(
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    title: @Composable () -> Unit,
) {
    val color = MaterialTheme.colorScheme.onPrimary
    TopAppBar(
        modifier = modifier,
        title = title,
        navigationIcon = navigationIcon,
        actions = actions,
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            navigationIconContentColor = color,
            titleContentColor = color,
            actionIconContentColor = color,
        )
    )
}

@Composable
fun NormalTopBar(
    modifier: Modifier = Modifier,
    title: String,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
) {
    DefaultTopBar(
        modifier = modifier,
        navigationIcon = navigationIcon,
        actions = actions,
    ) {
        Text(
            text = title,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
fun SearchBoxTopBar(
    modifier: Modifier = Modifier,
    title: String,
    query: String?,
    placeholder: String? = null,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    onQueryChanged: (String?) -> Unit,
) {
    DefaultTopBar(
        modifier = modifier,
        navigationIcon = navigationIcon,
        actions = actions,
    ) {
        if (query != null) {
            BasicSearchBox(
                placeholder = placeholder,
                value = query,
                onValueChange = onQueryChanged,
            )
        } else {
            Text(
                text = title,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}


@Composable
fun BoxScope.DropdownMenuAt(
    alignment: Alignment = Alignment.CenterEnd,
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .width(1.dp)
            .align(alignment)
    ) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest,
            content = content,
        )
    }
}

@Composable
fun IconButton(
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(),
    tint: Color = LocalContentColor.current,
    contentDescription: String? = null,
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick, modifier = modifier, enabled = enabled, colors = colors
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = tint,
        )
    }
}
