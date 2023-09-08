@file:OptIn(ExperimentalFoundationApi::class)

package jp.co.toukei.log.trustar.compose

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.material3.pullrefresh.pullRefresh
import androidx.compose.material3.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.sebaslogen.resaca.rememberScoped
import jp.co.toukei.log.common.compose.ProcessingDialog
import jp.co.toukei.log.common.compose.VerticalDivider
import jp.co.toukei.log.lib.common.toast
import jp.co.toukei.log.lib.compose.NormalTopBar
import jp.co.toukei.log.lib.compose.SearchInputBox
import jp.co.toukei.log.lib.compose.bottomShadow
import jp.co.toukei.log.lib.compose.horizontalScrollBar
import jp.co.toukei.log.lib.compose.navigateUpLambda
import jp.co.toukei.log.lib.compose.rememberDistinctBy
import jp.co.toukei.log.lib.compose.replaceMessage
import jp.co.toukei.log.lib.compose.verticalScrollbar
import jp.co.toukei.log.lib.ctx.Ctx
import jp.co.toukei.log.lib.orElseNull
import jp.co.toukei.log.lib.util.CheckUser
import jp.co.toukei.log.lib.util.UserCheck
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.errMessage
import jp.co.toukei.log.trustar.imageURI
import jp.co.toukei.log.trustar.user.LoggedUser
import jp.co.toukei.log.trustar.viewmodel.ChatSelectUserVM
import jp.co.toukei.log.trustar.viewmodel.HomeVM
import third.Result

@Composable
private fun SearchBox(
    checkUser: CheckUser<ComposeData.SelectChatUser>,
) {
    var text by rememberScoped {
        mutableStateOf("")
    }
    LaunchedEffect(text) {
        checkUser.setQueryText(text)
    }
    SearchInputBox(
        value = text,
        onValueChange = { text = it },
        placeholder = stringResource(id = R.string.search_by_name)
    )

    val key = { it: UserCheck<ComposeData.SelectChatUser> ->
        ComposeData.SelectChatUser.key(it.user)
    }
    val users by checkUser.checkedResult.subscribeAsState(initial = emptyList())
    val list = users.rememberDistinctBy(selector = key)

    val unselect: (ComposeData.SelectChatUser) -> Unit = {
        checkUser.selectId(it.id, false)
    }

    AnimatedVisibility(
        modifier = Modifier.background(MaterialTheme.colorScheme.onPrimary),
        visible = list.isNotEmpty(),
        enter = expandVertically(),
        exit = shrinkVertically(),
    ) {
        val state = rememberLazyListState()
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp)
                .horizontalScrollBar(state)
                .padding(4.dp),
            state = state,
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.Start),
        ) {
            items(
                items = list,
                key = key,
            ) {
                UserSelected(
                    modifier = Modifier.animateItemPlacement(),
                    user = it.user,
                    onRemove = if (it.keepState) null else unselect
                )
            }
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun UserAvatar(
    avatarUrl: String?
) {
    GlideImage(
        modifier = Modifier
            .size(54.dp)
            .clip(CircleShape),
        model = avatarUrl?.imageURI(), //todo model
        contentDescription = null
    ) {
        it.diskCacheStrategy(DiskCacheStrategy.NONE)
            .placeholder(R.drawable.ic_avatar)
            .centerCrop()
    }
}

@Composable
private fun UserSelected(
    modifier: Modifier,
    user: ComposeData.SelectChatUser,
    onRemove: ((ComposeData.SelectChatUser) -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .run {
                onRemove?.let {
                    clickable(
                        onClick = { it(user) },
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    )
                } ?: this
            },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .padding(4.dp),
        ) {
            UserAvatar(user.avatarUrl)
            if (onRemove != null) {
                Icon(
                    modifier = Modifier.align(Alignment.TopEnd),
                    imageVector = Icons.Default.Cancel,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }
        Text(
            modifier = Modifier
                .padding(horizontal = 8.dp),
            text = user.name.orEmpty(),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun UserList(
    vm: ChatSelectUserVM,
) {
    val history by vm.displayList0.subscribeAsState(emptyList())
    val ls by vm.displayList1.subscribeAsState(emptyList())

    val state = rememberLazyListState()

    val onClick = { a: ComposeData.SelectChatUser, b: Boolean ->
        val ok = vm.selectId(a.id, b)
        if (!ok) {
            Ctx.context.toast(if (b) R.string.user_cannot_be_selected else R.string.user_cannot_be_deleted)
        }
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .verticalScrollbar(state),
        state = state,
    ) {
        if (history.isNotEmpty()) {
            stickyHeader(
                key = 1,
            ) {
                Header(R.string.talk_history)
                VerticalDivider()
            }
            items(
                history,
                key = { "1" + it.user.id },
            ) {
                Item(it.user, it.checked, onClick)
                ItemDivider()
            }
        }
        stickyHeader(
            key = 2,
        ) {
            Header(R.string.logged_in_user)
            VerticalDivider()
        }
        if (ls.isEmpty()) {
            item {
                Placeholder()
                ItemDivider()
            }
        } else {
            items(
                ls,
                key = { "2" + it.user.id },
            ) {
                Item(user = it.user, checked = it.checked, onClick)
                ItemDivider()
            }
        }
    }
}

@Composable
private fun ItemDivider() {
    VerticalDivider(color = Color.Transparent)
}

@Composable
private fun Placeholder() {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        text = stringResource(id = R.string.no_data_placeholder),
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun Header(@StringRes title: Int) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.onPrimary)
            .padding(16.dp),
        text = stringResource(id = title),
    )
}

@Composable
private fun LazyItemScope.Item(
    user: ComposeData.SelectChatUser,
    checked: Boolean,
    onClick: ((ComposeData.SelectChatUser, Boolean) -> Unit)? = null,
) {
    val click = if (onClick != null) { -> onClick(user, !checked) } else null
    Row(
        modifier = Modifier
            .animateItemPlacement()
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.onPrimary)
            .run {
                if (click == null) this else clickable(onClick = click)
            }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TriStateCheckbox(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            state = ToggleableState(checked),
            enabled = click != null,
            onClick = click,
        )
        UserAvatar(user.avatarUrl)
        Text(
            text = user.name.orEmpty(),
            modifier = Modifier.padding(8.dp),
        )
    }
}


@Composable
private fun Add0(
    snbHost: SnackbarHostState,
    vm: ChatSelectUserVM,
    content: @Composable ColumnScope.(Int) -> Unit
) {

    val users by vm.selectedUsers.subscribeAsState(initial = emptyList())

    val userSize = users.size

    Scaffold(
        snackbarHost = { SnackbarHost(snbHost) },
        topBar = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                content(userSize)
                SearchBox(vm.checkUser)
                Box(modifier = Modifier.bottomShadow(6.dp))
            }
        },
    ) {
        val state = vm.state.subscribeAsState(initial = null).value
        if (state is Result.Error) {
            LaunchedEffect(Unit) {
                snbHost.replaceMessage(state.error.errMessage())
            }
        }
        val refreshing = state is Result.Loading
        val refreshState = rememberPullRefreshState(
            refreshing = refreshing,
            onRefresh = vm::loadUsers
        )
        LaunchedEffect(Unit) {
            vm.loadUsers()
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .clipToBounds()
                .pullRefresh(refreshState)
        ) {
            UserList(vm)
            BinPullRefresh(refreshing = refreshing, state = refreshState)
        }
    }
}

@Composable
private fun ChatRoomAdd(
    vm: ChatSelectUserVM,
    onDone: () -> Unit,
    naviBack: () -> Unit,
) {
    val snbHost = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val u = rememberScoped {
        mutableStateOf<List<ComposeData.SelectChatUser>?>(null)
    }
    u.value?.let { users ->
        val rx = remember(vm, u) { vm.addRoom(users) }.subscribeAsState(null)
        when (rx.value) {
            is Result.Error -> {
                snbHost.replaceMessage(scope, R.string.room_create_err_alert_msg)
                u.value = null
            }

            is Result.Value -> {
                LaunchedEffect(Unit) { onDone() }
            }

            else -> {
                ProcessingDialog()
            }
        }
    }
    val done = {
        u.value = vm.selectedUser()
    }
    Add0(
        snbHost = snbHost,
        vm = vm
    ) { userSize ->
        val title = if (userSize == 0) {
            stringResource(id = R.string.create_talk_room)
        } else {
            pluralStringResource(id = R.plurals.user_selected, userSize, userSize)
        }
        NormalTopBar(
            title = title,
            navigationIcon = {
                IconButton(onClick = naviBack) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null
                    )
                }
            },
            actions = {
                if (userSize > 0) {
                    IconButton(onClick = done) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = stringResource(id = R.string.create)
                        )
                    }
                }
            },
        )
    }
    LaunchedEffect(vm) {
        vm.excludeIds(setOf(vm.user.userInfo.userId))
    }
}

@Composable
private fun ChatRoomUserAdd(
    roomId: String,
    vm: ChatSelectUserVM,
    onDone: () -> Unit,
    naviBack: () -> Unit,
) {
    val snbHost = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val u = rememberScoped {
        mutableStateOf<List<ComposeData.SelectChatUser>?>(null)
    }
    u.value?.let { users ->
        val rx = remember(vm, u) { vm.editRoom(roomId, users) }.subscribeAsState(null)
        when (rx.value) {
            is Result.Error -> {
                snbHost.replaceMessage(scope, R.string.room_add_user_err_alert_msg)
                u.value = null
            }

            is Result.Value -> {
                LaunchedEffect(Unit) { onDone() }
            }

            else -> {
                ProcessingDialog()
            }
        }
    }
    val done = {
        u.value = vm.selectedUser()
    }
    Add0(
        snbHost = snbHost,
        vm = vm
    ) { userSize ->
        val title = pluralStringResource(id = R.plurals.user_selected, userSize, userSize)
        NormalTopBar(
            title = title,
            navigationIcon = {
                IconButton(onClick = naviBack) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null
                    )
                }
            },
            actions = {
                if (userSize > 0) {
                    IconButton(onClick = done) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = stringResource(id = R.string.add)
                        )
                    }
                }
            },
        )
    }

    DisposableEffect(roomId, vm) {
        val d = vm.roomUserIds(roomId)
            .subscribe {
                it.orElseNull()?.let(vm::excludeIds)
            }
        onDispose {
            d.dispose()
        }
    }
}

@Composable
@Destination
fun AddChatRoom(
    vm: HomeVM,
    navigator: DestinationsNavigator,
) {
    ChatRoomAdd(
        vm = viewModel {
            ChatSelectUserVM(vm.user)
        },
        onDone = {
            vm.reloadMessageRoom()
            navigator.navigateUp()
        },
        naviBack = navigator.navigateUpLambda(),
    )
}

@Composable
fun EditChatRoomUser(
    roomId: String,
    user: LoggedUser,
    onDone: () -> Unit,
    naviBack: () -> Unit,

    /*
        navigator: DestinationsNavigator,
    */
) {
    ChatRoomUserAdd(
        roomId,
        vm = viewModel {
            ChatSelectUserVM(user)
        },
        onDone = onDone,
        naviBack = naviBack,
    )
}
