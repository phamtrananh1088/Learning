@file:OptIn(ExperimentalMaterial3Api::class)

package jp.co.toukei.log.trustar.compose

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pullrefresh.PullRefreshIndicator
import androidx.compose.material3.pullrefresh.PullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sebaslogen.resaca.rememberScoped
import jp.co.toukei.log.common.compose.AppPropTodo
import jp.co.toukei.log.common.compose.CircularProgress
import jp.co.toukei.log.common.compose.PrimarySurface
import jp.co.toukei.log.common.compose.ZoomImage
import jp.co.toukei.log.lib.compose.DefaultSheet
import jp.co.toukei.log.lib.createTempDirInDir
import jp.co.toukei.log.lib.deleteQuickly
import jp.co.toukei.log.lib.orElseNull
import jp.co.toukei.log.lib.readToTmpFile
import jp.co.toukei.log.lib.streamDecodeBitmap
import jp.co.toukei.log.trustar.App
import jp.co.toukei.log.trustar.Config
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.viewmodel.HomeVM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import third.Result
import java.io.File


@Composable
fun PrimaryListHeadTitle(
    title: String,
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(horizontal = 8.dp, vertical = 6.dp),
        text = title,
        color = MaterialTheme.colorScheme.onPrimary,
    )
}


@Composable
fun TopBarRightText(
    modifier: Modifier = Modifier,
    text: String = stringResource(id = R.string.trustar),
    rightText: String? = null,
    endLayout: (@Composable RowScope.() -> Unit)? = null,
) {
    PrimarySurface(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .height(40.dp)
                .padding(start = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 4.dp),
                text = text,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.3.sp,
                    shadow = Shadow(
                        color = Color(0x66000000),
                        offset = Offset(1F, 1F),
                        blurRadius = 4F,
                    )
                )
            )
            Spacer(modifier = Modifier.weight(1F))
            if (rightText != null) {
                Text(
                    text = rightText,
                    color = Color.Black,
                    modifier = Modifier,
                    fontSize = 14.sp,
                    letterSpacing = 0.sp,
                    fontWeight = FontWeight.Normal,
                )
            }
            if (endLayout != null) {
                endLayout()
            }
        }
    }
}

@Composable
fun BinStatusLabel(
    modifier: Modifier = Modifier,
    text: String,
    bgColor: Color,
    textColor: Color,
    minWide: Int,
    textStyle: TextStyle = AppPropTodo.Text.small,
) {
    val size = LocalDensity.current.run { textStyle.fontSize.toDp() }
    val wide = size * minWide

    Text(
        modifier = modifier
            .background(bgColor, RoundedCornerShape(2.dp))
            .padding(vertical = 2.dp)
            .padding(horizontal = size / 2)
            .widthIn(min = wide),
        text = text,
        style = textStyle,
        color = textColor,
        textAlign = TextAlign.Center,
    )
}


@Composable
fun BinInfoIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colorScheme.primary,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = null,
            tint = tint
        )
    }
}


@Composable
fun BoxScope.BinPullRefresh(
    refreshing: Boolean,
    state: PullRefreshState,
) {
    PullRefreshIndicator(
        refreshing = refreshing,
        state = state,
        modifier = Modifier.align(Alignment.TopCenter),
        backgroundColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
    )
}

fun Modifier.primaryBackground(): Modifier = composed {
    fillMaxSize()
        .background(
            brush = Brush.verticalGradient(
                listOf(
                    MaterialTheme.colorScheme.primary,
                    AppPropTodo.Color.deepPrimary
                )
            ),
        )
}

@Composable
fun HomeVM.WhenBinStatusChanged(
    allocationNo: String,
    action: () -> Unit,
) {
    val a by rememberUpdatedState(newValue = action)
    val s by remember(this, allocationNo) {
        binHeader(allocationNo)
    }.subscribeAsState(null)
    s?.let { v ->
        DisposableEffect(v.orElseNull()?.binStatus?.value) {
            onDispose(a)
        }
    }
}

@Composable
fun HomeVM.WhenWorkStatusChanged(
    allocationNo: String,
    allocationRow: Int,
    action: () -> Unit,
) {
    val a by rememberUpdatedState(newValue = action)
    val s by remember(this, allocationNo, allocationRow) {
        binDetail(
            allocationNo,
            allocationRow
        )
    }.subscribeAsState(null)
    s?.let { v ->
        DisposableEffect(v.orElseNull()?.workStatus?.value) {
            onDispose(a)
        }
    }
}

@Composable
fun HomeVM.WhenWorkStatusOrBinStatusChanged(
    allocationNo: String,
    allocationRow: Int,
    action: () -> Unit,
) {
    WhenBinStatusChanged(allocationNo, action)
    WhenWorkStatusChanged(allocationNo, allocationRow, action)
}

@Composable
fun <T : Any> rememberScopedMutableStateOf(): MutableState<T?> {
    return rememberScoped {
        mutableStateOf(null)
    }
}

@Composable
fun rememberTmpDir(): File {
    val dir = remember {
        Config.tmpDir.createTempDirInDir()
    }
    DisposableEffect(Unit) {
        onDispose(dir::deleteQuickly)
    }
    return dir
}

@Composable
fun ViewImage(
    file: File,
    dismiss: () -> Unit
) {
    ViewImage(
        produceState<Result<ImageBitmap>?>(Result.Loading) {
            value = withContext(Dispatchers.IO) {
                file.streamDecodeBitmap(2048)?.run {
                    Result.Value(asImageBitmap())
                }
            }
        }.value,
        dismiss
    )
}

@Composable
fun ViewImage(
    file: Uri,
    dismiss: () -> Unit
) {
    val context = LocalContext.current
    val dir = rememberTmpDir()
    ViewImage(
        produceState<Result<ImageBitmap>?>(Result.Loading) {
            value = withContext(Dispatchers.IO) {
                context.contentResolver.readToTmpFile(file, dir)
                    ?.streamDecodeBitmap(2048)?.run {
                        Result.Value(asImageBitmap())
                    }
            }
        }.value,
        dismiss
    )
}

@Composable
private fun ViewImage(
    bitmap: Result<ImageBitmap>?,
    dismiss: () -> Unit
) {
    DefaultSheet(onDismiss = dismiss) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            var d by rememberScoped {
                mutableStateOf(true)
            }
            when (bitmap) {
                is Result.Loading -> {
                    CircularProgress()
                }

                is Result.Value -> {
                    ZoomImage(
                        modifier = Modifier.fillMaxSize(),
                        bitmap = bitmap.value,
                        zoomMin0 = 1F,
                        zoomMax = 4F
                    ) {
                        d = !d
                    }

                }

                else -> {
                    Button(onClick = dismiss) {
                        Text(text = "err")
                    }
                }
            }

            AnimatedVisibility(
                visible = d,
                enter = slideInVertically { -it },
                exit = slideOutVertically { -it }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .align(Alignment.TopStart)
                        .background(Color(0x33000000))
                        .padding(8.dp),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    IconButton(onClick = dismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun rememberSystemTimeChange(): State<Long> {
    return produceState(System.currentTimeMillis()) {
        App.timeChangeFlow.collect { value = it }
    }
}
