@file:OptIn(ExperimentalGlideComposeApi::class)

package jp.co.toukei.log.trustar.compose

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pullrefresh.PullRefreshIndicator
import androidx.compose.material3.pullrefresh.pullRefresh
import androidx.compose.material3.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import jp.co.toukei.log.lib.compose.minWidthMatchHeight
import jp.co.toukei.log.lib.compose.rememberDistinctBy
import jp.co.toukei.log.lib.compose.replaceMessage
import jp.co.toukei.log.lib.compose.verticalScrollbar
import jp.co.toukei.log.lib.ctx.Ctx
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.chat.PushMessage
import jp.co.toukei.log.trustar.chat.activity.Chat
import jp.co.toukei.log.trustar.dateTimeFormat
import jp.co.toukei.log.trustar.db.chat.entity.ChatRoomExt
import jp.co.toukei.log.trustar.deprecated.intentFor
import jp.co.toukei.log.trustar.glide.ImageURI
import jp.co.toukei.log.trustar.viewmodel.HomeVM
import third.Result

@Composable
fun HomeMessage(
    modifier: Modifier,
    list: List<ChatRoomExt>,
    refreshing: Boolean,
    onRefresh: () -> Unit,
    onClick: (String) -> Unit,
) {
    val state = rememberPullRefreshState(
        refreshing = refreshing,
        onRefresh = onRefresh
    )
    val key: (ChatRoomExt) -> String = { it.room.id }
    val safeList = list.rememberDistinctBy(key)

    Box(
        modifier = modifier
            .clipToBounds()
            .pullRefresh(state)
    ) {
        val listState: LazyListState = rememberLazyListState()
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .verticalScrollbar(listState),
            state = listState,
        ) {
            items(
                items = safeList,
                key = key,
            ) {
                RoomRow(
                    id = it.room.id,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 1.dp),
                    roomAvatar = it.imageUri,
                    roomName = it.title,
                    unread = it.room.unread,
                    lastUpdated = it.room.lastUpdated,
                    onClick = onClick,
                )
            }
        }
        PullRefreshIndicator(
            refreshing = refreshing,
            state = state,
            modifier = Modifier.align(Alignment.TopCenter),
            backgroundColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
        )
    }
}

@Composable
private fun Avatar(roomAvatar: ImageURI?) {
    GlideImage(
        modifier = Modifier.size(54.dp),
        model = roomAvatar ?: R.drawable.ic_avatar,
        contentDescription = null
    ) {
        it.placeholder(R.drawable.ic_avatar)
            .centerCrop()
    }
}

@Composable
private fun RoomRow(
    id: String,
    modifier: Modifier,
    roomAvatar: ImageURI?, //todo
    roomName: String,
    unread: Int,
    lastUpdated: Long,
    onClick: (String) -> Unit,
) {

    Row(
        modifier = modifier
            .clickable(onClick = { onClick(id) })
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Avatar(roomAvatar)
        Text(
            modifier = Modifier
                .weight(1F, fill = true)
                .padding(horizontal = 16.dp),
            text = roomName,
            style = MaterialTheme.typography.titleLarge,
        )
        Column(
            modifier = Modifier.fillMaxHeight(),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            val currentDate by rememberSystemTimeChange()
            val dateStr by remember(lastUpdated) {
                derivedStateOf {
                    lastUpdated.dateTimeFormat(currentDate)
                }
            }
            Text(
                text = dateStr,
                style = MaterialTheme.typography.bodyMedium,
            )
            if (unread > 0) {
                Surface(
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.minWidthMatchHeight(),
                    color = MaterialTheme.colorScheme.primary,
                ) {
                    Box(
                        modifier = Modifier,
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (unread > 99) "99+" else unread.toString(),
                            style = MaterialTheme.typography.labelMedium,
                            maxLines = 1,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NavMessage(
    paddingValues: PaddingValues,
    snbHost: SnackbarHostState,
    vm: HomeVM,
) {
    val list by vm.chatRoomList.subscribeAsState(initial = emptyList())

    val loadingState = vm.reloadMessageRoomLiveData.observeAsState()

    val l = loadingState.value

    var refreshing by remember {
        mutableStateOf(l is Result.Loading)
    }
    LaunchedEffect(l) {
        refreshing = l is Result.Loading
        if (l is Result.Error) {
            snbHost.replaceMessage(Ctx.context.getString(R.string.load_failed))
        }
    }

    val reload = {
        refreshing = true
        vm.reloadMessageRoom()
    }
    val push by PushMessage.pushMessageLiveData.observeAsState()
    LaunchedEffect(push) {
        if (push != null) {
            reload()
        }
    }

    val clickLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Chat.RESP_RELOAD) {
            vm.reloadMessageRoom()
        }
    }
    val context = LocalContext.current
    HomeMessage(
        Modifier
            .fillMaxSize()
            .padding(paddingValues),
        list,
        refreshing,
        reload,
    ) {
        clickLauncher.launch(
            context.intentFor<Chat>(Chat.ARG_ROOM_ID to it)
        )
    }
}
