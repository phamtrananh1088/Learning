@file:OptIn(ExperimentalFoundationApi::class)

package jp.co.toukei.log.common.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.internal.disposables.ListCompositeDisposable
import jp.co.toukei.log.lib.checkerboard
import jp.co.toukei.log.lib.compose.animateScrollTo
import jp.co.toukei.log.lib.compose.subscribeAsState2
import jp.co.toukei.log.lib.replayThenAutoConnect
import jp.co.toukei.log.lib.streamDecodeBitmap
import third.Result
import java.io.File


@Composable
private fun BoxWithConstraintsScope.SlideItem(
    page: Int,
    image: Result<File>,
    click: (Int, File) -> Unit,
) {
    when (image) {
        is Result.Error -> {
            Image(
                modifier = Modifier.size(64.dp),
                imageVector = Icons.Default.BrokenImage,
                contentDescription = null,
            )
        }

        is Result.Loading -> {
            CircularProgress(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
            )
        }

        is Result.Value -> {
            val it = image.value
            val m = constraints.run { maxOf(maxHeight, maxWidth) }
            val bitmap = it.streamDecodeBitmap(m)?.asImageBitmap()
            if (bitmap == null) {
                Image(
                    modifier = Modifier.size(64.dp),
                    imageVector = Icons.Default.BrokenImage,
                    contentDescription = null,
                )
            } else {
                Image(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable {
                            click(page, it)
                        },
                    bitmap = bitmap,
                    contentDescription = null,
                )
            }
        }
    }
}

private class CachedRequest<K : Any, R : Any> {

    private val container = ListCompositeDisposable()
    private val map = mutableMapOf<K, Flowable<R>>()
    private val lock = Any()

    fun flowable(key: K, create: (key: K) -> Flowable<R>): Flowable<R> {
        return synchronized(lock) {
            map.getOrPut(key) {
                create(key)
                    .onTerminateDetach()
                    .replayThenAutoConnect(container, 0)
            }
        }
    }

    fun clear() {
        synchronized(lock) {
            map.clear()
            container.clear()
        }
    }
}

@Composable
fun <T : Any> ImageSlide(
    modifier: Modifier,
    pagerState: PagerState,
    key: (page: Int) -> T,
    request: (key: T) -> Flowable<Result<File>>,
    onDelete: ((index: Int) -> Unit)? = null,
    click: (index: Int, File) -> Unit,
) {
    val cachedRequest = remember {
        CachedRequest<T, Result<File>>()
    }
    DisposableEffect(Unit) {
        onDispose {
            cachedRequest.clear()
        }
    }
    Box(
        modifier = modifier,
    ) {
        HorizontalPagerSlider(modifier, pagerState) { page ->
            val k = key(page)
            val img = remember(k, request) {
                cachedRequest.flowable(k, request)
            }.subscribeAsState2(Result.Loading).value

            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                SlideItem(page, img, click)
            }
        }
        if (onDelete != null && pagerState.currentPage < pagerState.pageCount) {
            Box(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.Red.copy(alpha = 0.6F))
                    .clickable(
                        indication = rememberRipple(radius = 24.dp),
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = {
                            onDelete(pagerState.currentPage)
                        }
                    )
                    .align(Alignment.BottomCenter),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}


@Composable
fun HorizontalPagerSlider(
    modifier: Modifier,
    pagerState: PagerState,
    pageContent: @Composable PagerScope.(page: Int) -> Unit
) {
    val imageCount = pagerState.pageCount
    if (imageCount < 1) {
        Spacer(modifier = modifier)
        return
    }
    val coroutineScope = rememberCoroutineScope()

    val bg = Color.Black.copy(0.2F)
    val tint = ColorFilter.tint(Color.White)
    val tintDisabled = ColorFilter.tint(Color.White.copy(alpha = 0.4F))

    BoxWithConstraints(
        modifier = modifier
    ) {
        val cm = Modifier
            .fillMaxHeight()
            .width(48.dp)
            .widthIn(max = maxWidth / 4)
            .background(bg)

        HorizontalPager(
            modifier = Modifier
                .fillMaxSize()
                .background(ShaderBrush(checkerboard(30))),
            state = pagerState,
            pageContent = pageContent
        )

        val cp = pagerState.currentPage
        val goLeft = cp > 0
        Image(
            modifier = cm
                .align(Alignment.CenterStart)
                .clickable(goLeft) {
                    pagerState.animateScrollTo(coroutineScope, pagerState.currentPage - 1)
                },
            imageVector = Icons.Default.ArrowBackIosNew,
            contentDescription = null,
            colorFilter = if (goLeft) tint else tintDisabled,
        )
        val goRight = cp < imageCount - 1
        Image(
            modifier = cm
                .align(Alignment.CenterEnd)
                .clickable(goRight) {
                    pagerState.animateScrollTo(coroutineScope, pagerState.currentPage + 1)
                },
            imageVector = Icons.Default.ArrowForwardIos,
            contentDescription = null,
            colorFilter = if (goRight) tint else tintDisabled,
        )
    }
}
