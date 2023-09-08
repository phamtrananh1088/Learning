package jp.co.toukei.log.trustar.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.sebaslogen.resaca.rememberScoped
import jp.co.toukei.log.trustar.BuildConfig
import jp.co.toukei.log.trustar.Config
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.SplitBuildVariant
import jp.co.toukei.log.trustar.compose.destinations.AddChatRoomDestination
import jp.co.toukei.log.trustar.compose.destinations.SettingsDestination
import jp.co.toukei.log.trustar.enum.HomeNavi
import jp.co.toukei.log.trustar.viewmodel.HomeVM


@Composable
fun HomeTopBar(
    vm: HomeVM,
    navigator: DestinationsNavigator,
) {
    val s by vm.selectedNavigateItem

    val currentDate by rememberSystemTimeChange()
    val dateStr by remember {
        derivedStateOf {
            Config.dateFormatterMMdde.format(currentDate)
        }
    }

    val modifier = Modifier

    when (s) {
        HomeNavi.Dashboard -> {
            TopBarRightText(
                modifier = modifier,
                rightText = dateStr
            ) {
                DashboardTopAction(navigator)
            }
        }

        HomeNavi.Detail, HomeNavi.Operate, HomeNavi.Fuel -> {
            TopBarRightText(
                modifier = modifier,
                rightText = dateStr
            )
        }

        HomeNavi.Message -> {
            TopBarRightText(
                modifier = modifier,
                text = stringResource(id = R.string.talk),
                rightText = dateStr
            ) {
                MessageTopAction(vm, navigator)
            }
        }
    }
}


@Composable
private fun MessageTopAction(
    vm: HomeVM,
    navigator: DestinationsNavigator,
) {
    IconButton(
        onClick = {
            navigator.navigate(AddChatRoomDestination)
        }
    ) {
        Icon(
            Icons.Default.Add,
            contentDescription = stringResource(id = R.string.create_talk_room)
        )
    }
}

@Composable
private fun DashboardTopAction(
    navigator: DestinationsNavigator,
) {
    Box {
        var showMenu by rememberScoped { mutableStateOf(false) }
        IconButton(
            onClick = {
                showMenu = true
            }
        ) {
            Icon(Icons.Default.MoreVert, null)
        }

        val dismiss = { showMenu = false }
        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = dismiss,
            offset = DpOffset(20.dp, (-20).dp)
        ) {
            DropdownMenuItem(
                text = { Text(text = stringResource(id = R.string.settings)) },
                onClick = {
                    navigator.navigate(SettingsDestination)
                    dismiss()
                },
                leadingIcon = {
                    Icon(Icons.Filled.Settings, null)
                }
            )
            if (BuildConfig.isDebug) {
                //todo remove
                DropdownMenuItem(
                    text = { Text(text = "export") },
                    onClick = SplitBuildVariant::debugDbExport,
                )
            }
        }
    }
}
