package jp.co.toukei.log.trustar.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.sebaslogen.resaca.rememberScoped
import jp.co.toukei.log.common.compose.AppPropTodo
import jp.co.toukei.log.common.compose.HeadButton
import jp.co.toukei.log.common.compose.MyClickableText
import jp.co.toukei.log.common.compose.TextClk
import jp.co.toukei.log.common.enum.LaunchIntent
import jp.co.toukei.log.common.enum.open
import jp.co.toukei.log.lib.compose.DefaultConfirmDialog
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.enum.NoticeUseCase
import jp.co.toukei.log.trustar.viewmodel.HomeVM
import jp.co.toukei.log.trustar.viewmodel.LoginOkVM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
@Destination
fun NoticeList(
    vm: HomeVM,
    navigator: DestinationsNavigator,
    rank: Int,
) {
    val v = viewModel {
        LoginOkVM(vm.user)
    }
    val list by remember(v, rank) { v.noticeList(rank, false) }.subscribeAsState(emptyList())

    NoticeListWithConfirm2(
        vm = v,
        list = list,
        buttonClick = {
            vm.noticeMarkRead(rank)
            navigator.navigateUp()
        },
    )
}


@Composable
fun NoticeListWithConfirm2(
    vm: LoginOkVM,
    list: List<ComposeData.Notice>,
    buttonClick: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val ctx = LocalContext.current
    NoticeListWithConfirm(
        list = list,
        buttonClick = buttonClick,
    ) {
        scope.launch(Dispatchers.IO) {
            vm.user.commitCommonDb()
            when (it.useCase) {
                NoticeUseCase.Logout -> {
                    Current.logout(ctx, true)
                }

                NoticeUseCase.Restart -> {
                    Current.restartLaunch(ctx)
                }
            }
        }
    }
}

@Composable
fun NoticeList(
    modifier: Modifier = Modifier,
    list: List<ComposeData.Notice>,
    buttonClick: () -> Unit,
    noticeClick: (ComposeData.Notice) -> Unit,
) {
    Box(
        modifier = modifier
            .primaryBackground(),
        contentAlignment = Alignment.TopCenter,
    ) {
        if (list.isEmpty()) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    modifier = Modifier
                        .weight(1F)
                        .wrapContentHeight(),
                    text = stringResource(id = R.string.no_data_placeholder),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                )
                Footer(buttonClick)
            }
        } else {
            val containerColor = MaterialTheme.colorScheme.surface
            val contentColor = MaterialTheme.colorScheme.onSurface
            val colors = CardDefaults.cardColors(
                containerColor = containerColor,
                contentColor = contentColor,
                disabledContainerColor = containerColor,
                disabledContentColor = contentColor
            )
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = remember {
                    val space = 8.dp
                    object : Arrangement.Vertical {
                        override val spacing = space

                        override fun Density.arrange(
                            totalSize: Int,
                            sizes: IntArray,
                            outPositions: IntArray,
                        ) {
                            if (sizes.isEmpty()) return
                            val spacePx = space.roundToPx()
                            var occupied = 0
                            var lastSpace = 0
                            sizes.forEachIndexed { index, size ->
                                if (index == sizes.lastIndex) {
                                    outPositions[index] = totalSize - size
                                } else {
                                    outPositions[index] = minOf(occupied, totalSize - size)
                                }
                                lastSpace = minOf(spacePx, totalSize - outPositions[index] - size)
                                occupied = outPositions[index] + size + lastSpace
                            }
                            occupied -= lastSpace
                        }
                    }
                },
            ) {
                items(
                    items = list,
                    key = { it.id },
                ) {
                    Card(
                        modifier = Modifier.run {
                            if (it.useCase.clickable()) {
                                clickable {
                                    noticeClick(it)
                                }
                            } else this
                        },
                        colors = colors,
                    ) {
                        NoticeItem(it)
                    }
                }
                item {
                    Footer(buttonClick)
                }
            }
        }
    }
}

@Composable
fun NoticeListWithConfirm(
    modifier: Modifier = Modifier,
    list: List<ComposeData.Notice>,
    buttonClick: () -> Unit,
    noticeClick: (ComposeData.Notice) -> Unit,
) {
    var rowClick by rememberScoped {
        mutableStateOf<ComposeData.Notice?>(null)
    }
    rowClick?.let {
        val onDismissRequest = {
            rowClick = null
        }
        DefaultConfirmDialog(
            onDismissRequest = onDismissRequest,
            confirmButtonText = R.string.yes,
            dismissButtonText = R.string.cancel,
            title = R.string.notice_send_data_alert_title,
            content = R.string.notice_send_data_alert_msg,
        ) {
            noticeClick(it)
            onDismissRequest()
        }
    }
    NoticeList(
        modifier = modifier,
        list = list,
        buttonClick = buttonClick,
        noticeClick = {
            rowClick = it
        },
    )
}

@Composable
private fun Footer(
    buttonClick: () -> Unit,
) {
    HeadButton(
        modifier = Modifier.padding(vertical = 54.dp),
        horizontalPadding = 32,
        verticalPadding = 12,
        text = stringResource(id = R.string.notice_mark_read),
        style = AppPropTodo.Text.defaultBold,
        color = MaterialTheme.colorScheme.onPrimary,
        contentColor = MaterialTheme.colorScheme.primary,
        click = buttonClick,
    )
}

@Composable
private fun NoticeItem(
    notice: ComposeData.Notice,
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
    ) {
        Text(
            text = remember(notice) {
                AnnotatedString.Builder(notice.title).apply {
                    if (notice.isNew) {
                        withStyle(
                            SpanStyle(
                                color = Color.Red,
                                fontSize = 16.sp,
                            )
                        ) {
                            append("　NEW")
                        }
                    }
                }.toAnnotatedString()
            },
            color = AppPropTodo.Color.noticeTitle,
            style = AppPropTodo.Text.bold16,
        )
        val context = LocalContext.current
        MyClickableText(
            mask = TextClk.TEL or TextClk.URL,
            modifier = Modifier
                .padding(top = 8.dp),
            text = notice.content,
            style = AppPropTodo.Text.size16,
        ) {
            when (it) {
                is TextClk.Tel -> {
                    LaunchIntent.Dial.open(context, it.text)
                }

                is TextClk.Url -> {
                    LaunchIntent.Url.open(context, it.text)
                }

                else -> {}
            }
        }
    }
}
