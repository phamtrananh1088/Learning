package jp.co.toukei.log.trustar.compose

import android.app.Activity.RESULT_OK
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.sebaslogen.resaca.rememberScoped
import jp.co.toukei.log.common.compose.AppPropTodo
import jp.co.toukei.log.common.compose.LocalAppColor
import jp.co.toukei.log.common.compose.ProcessingDialog
import jp.co.toukei.log.lib.compose.DefaultConfirmDialog
import jp.co.toukei.log.lib.compose.NormalTopBar
import jp.co.toukei.log.lib.compose.navigateUpLambda
import jp.co.toukei.log.lib.compose.replaceMessage
import jp.co.toukei.log.lib.orElseNull
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.chat.activity.EditRoom
import jp.co.toukei.log.trustar.chat.vm.ChatOptionVM
import jp.co.toukei.log.trustar.imageURI
import jp.co.toukei.log.trustar.viewmodel.HomeVM
import third.Result

@Composable
@Destination
fun ChatOption(
    vm: HomeVM,
    roomId: String,
    navigator: DestinationsNavigator,
) {
    ChatOption(
        vm = viewModel {
            ChatOptionVM(roomId, vm.user)
        },
        userAdded = {
            vm.reloadMessageRoom()
        },
        naviBack = navigator.navigateUpLambda(),
    )
}

@Composable
fun ChatOption(
    vm: ChatOptionVM,
    userAdded: () -> Unit,
    naviBack: () -> Unit,
) {
    val snbHost = remember { SnackbarHostState() }
    val context = LocalContext.current
    val addUserLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            userAdded()
        }
    }
    val addUser = {
        addUserLauncher.launch(
            EditRoom.intentForStartActivity(context, vm.roomId)
        )
    }

    val optional = vm.chatRoomWithUsers.subscribeAsState(null).value
    val data = optional?.orElseNull()

    if (optional != null && data == null) {
        LaunchedEffect(Unit) {
            naviBack()
        }
    }
    val roomName = data?.room?.name.orEmpty()

    Scaffold(
        snackbarHost = { SnackbarHost(snbHost) },
        topBar = {
            NormalTopBar(
                title = roomName,
                navigationIcon = {
                    IconButton(onClick = naviBack) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null
                        )
                    }
                },
            )
        },
    ) {
        if (data != null) {
            val users = data.users
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(it),
            ) {
                val userCount = users.size

                Head(pluralStringResource(id = R.plurals.members_of_room, userCount, userCount))
                UserAdd(
                    onClick = addUser,
                )
                users.forEach { user ->
                    UserItem(
                        userName = user.name.orEmpty(),
                        avatarUrl = user.avatar
                    )
                }
                Head(stringResource(id = R.string.talk_room_settings))
                NotificationOnOff(snbHost, data.room.notification, vm)
                QuitRoom(snbHost, roomName, vm)
            }
        }
    }
}

private fun Modifier.listBg(): Modifier = composed {
    background(LocalAppColor.current.listItemBg)
}


@Composable
private fun NotificationOnOff(
    snbHost: SnackbarHostState,
    notificationOnOff: Boolean,
    vm: ChatOptionVM,
) {
    val scope = rememberCoroutineScope()
    var process by rememberScoped {
        mutableStateOf<Boolean?>(null)
    }
    process?.let { on ->
        val l = remember(vm, on) { vm.notificationOnOff(on) }.subscribeAsState(null)
        when (l.value) {
            is Result.Error -> {
                snbHost.replaceMessage(scope, R.string.room_update_settings_err_alert_msg)
                process = null
            }

            is Result.Value -> {
                process = null
            }

            else -> {
                ProcessingDialog()
            }
        }
    }
    Row(
        modifier = Modifier
            .fillMaxSize()
            .listBg()
            .clickable {
                process = !notificationOnOff
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier
                .weight(1F, true)
                .padding(16.dp),
            text = stringResource(id = R.string.allow_notification),
            style = AppPropTodo.Text.size18
        )
        val c = MaterialTheme.colorScheme
        Switch(
            modifier = Modifier
                .padding(horizontal = 16.dp),
            checked = notificationOnOff,
            onCheckedChange = { process = it },
            colors = SwitchDefaults.colors(
                checkedThumbColor = c.onPrimary,
                checkedTrackColor = c.primaryContainer,
                checkedBorderColor = Color.Transparent,
                uncheckedThumbColor = c.onPrimary.copy(alpha = 0.5F),
                uncheckedTrackColor = c.scrim.copy(alpha = 0.5F),
                uncheckedBorderColor = c.primaryContainer.copy(alpha = 0.8F),
            ),
        )
    }
}

@Composable
private fun QuitRoom(
    snbHost: SnackbarHostState,
    roomName: String,
    vm: ChatOptionVM,
) {
    val scope = rememberCoroutineScope()
    var confirm by rememberScoped {
        mutableStateOf(false)
    }
    var process by rememberScoped {
        mutableStateOf(false)
    }

    if (process) {
        val l = remember(vm) { vm.leaveRoom() }.subscribeAsState(null)
        when (l.value) {
            is Result.Error -> {
                snbHost.replaceMessage(scope, R.string.room_leave_err_alert_msg)
                process = false
            }

            is Result.Value -> {
                process = false
            }

            else -> {
                ProcessingDialog()
            }
        }
    }
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .listBg()
            .clickable {
                confirm = true
            }
            .padding(16.dp),
        text = stringResource(id = R.string.quit_talk_room),
        style = AppPropTodo.Text.size18
    )
    if (confirm) {
        val toggle = {
            confirm = !confirm
        }
        DefaultConfirmDialog(
            title = stringResource(R.string.confirm),
            dismissButtonText = stringResource(R.string.cancel),
            onDismissRequest = toggle,
            confirmButtonText = stringResource(R.string.quit),
            content = stringResource(
                id = R.string.room_quit_s1_alert_msg,
                roomName
            )
        ) {
            confirm = false
            process = true
        }
    }
}

@Composable
private fun ColumnScope.Head(
    text: String,
) {
    Spacer(modifier = Modifier.padding(8.dp))
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .listBg()
            .padding(16.dp),
        text = text,
        style = AppPropTodo.Text.size16
    )
}

@Composable
private fun UserAdd(
    onClick: (() -> Unit)
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .listBg()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            modifier = Modifier
                .size(60.dp)
                .padding(16.dp),
            imageVector = Icons.Default.PersonAdd,
            contentDescription = null,
            colorFilter = ColorFilter.tint(AppPropTodo.Color.iconTint)
        )
        Text(
            modifier = Modifier
                .padding(8.dp),
            text = stringResource(id = R.string.add_user),
            style = AppPropTodo.Text.size16
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun UserItem(
    userName: String,
    avatarUrl: String?,
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .listBg()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        GlideImage(
            modifier = Modifier
                .size(60.dp)
                .padding(8.dp)
                .clip(CircleShape),
            model = avatarUrl?.imageURI(),
            contentDescription = null
        ) {
            it.diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.ic_avatar)
                .centerCrop()
        }
        Text(
            modifier = Modifier
                .padding(8.dp),
            text = userName,
            style = AppPropTodo.Text.size16
        )
    }
}
