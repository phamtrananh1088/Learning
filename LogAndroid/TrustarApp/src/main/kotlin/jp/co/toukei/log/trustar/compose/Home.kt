@file:OptIn(
    ExperimentalMaterial3Api::class
)

package jp.co.toukei.log.trustar.compose

import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import jp.co.toukei.log.common.compose.AppPropTodo
import jp.co.toukei.log.lib.compose.replaceMessage
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.compose.destinations.NoticeListDestination
import jp.co.toukei.log.trustar.enum.HomeNavi
import jp.co.toukei.log.trustar.repo.Important_Notice
import jp.co.toukei.log.trustar.repo.Normal_Notice
import jp.co.toukei.log.trustar.viewmodel.HomeVM

@Composable
@Destination
@RootNavGraph(start = true)
fun Home(
    vm: HomeVM,
    navigator: DestinationsNavigator,
) {
    val clickNavi: (HomeNavi) -> Unit = {
        vm.postMenuId(it, true)
    }

    val snbHost = remember { SnackbarHostState() }

    val unreadMessageCount by vm.unreadMessageCount.subscribeAsState(initial = 0)

    val selectedNavi = vm.selectedNavigateItem

    Scaffold(
        snackbarHost = { SnackbarHost(snbHost) },
        topBar = {
            HomeTopBar(vm = vm, navigator = navigator)
        },
        bottomBar = {
            Surface(
                color = AppPropTodo.Color.bottomNaviBg,
                contentColor = AppPropTodo.Color.bottomNaviTint,
                tonalElevation = NavigationBarDefaults.Elevation,
                modifier = Modifier
            ) {
                val itemColor = NavigationBarItemDefaults.colors(
                    disabledIconColor = AppPropTodo.Color.bottomNaviTint,
                    disabledTextColor = AppPropTodo.Color.bottomNaviTint,
                    indicatorColor = AppPropTodo.Color.bottomNaviSelectedIndicator,
                    selectedIconColor = AppPropTodo.Color.bottomNaviSelected,
                    selectedTextColor = AppPropTodo.Color.bottomNaviSelected,
                    unselectedIconColor = AppPropTodo.Color.bottomNaviTint,
                    unselectedTextColor = AppPropTodo.Color.bottomNaviTint,
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(NavigationBarDefaults.windowInsets)
                        .height(80.dp)
                        .selectableGroup(),
                ) {
                    val s = selectedNavi.value
                    NavItem(
                        label = R.string.home_navigation_dashboard,
                        icon = R.drawable.dashboard_24dp,
                        itemColor = itemColor,
                        selected = s == HomeNavi.Dashboard
                    ) { clickNavi(HomeNavi.Dashboard) }
                    NavItem(
                        label = R.string.home_navigation_deliver,
                        icon = R.drawable.document_24dp,
                        itemColor = itemColor,
                        selected = s == HomeNavi.Detail
                    ) { clickNavi(HomeNavi.Detail) }
                    NavItem(
                        label = R.string.home_navigation_operation,
                        icon = R.drawable.toolbox_24dp,
                        itemColor = itemColor,
                        selected = s == HomeNavi.Operate,
                        badge = if (!vm.detailNav.startedBinDetailList.value.isNullOrEmpty()) {
                            {
                                Badge(
                                    containerColor = AppPropTodo.Color.bottomNaviBadge,
                                    contentColor = AppPropTodo.Color.bottomNaviBadgeText,
                                )
                            }
                        } else null
                    ) { clickNavi(HomeNavi.Operate) }
                    NavItem(
                        label = R.string.home_navigation_refuel,
                        icon = R.drawable.fill_tank_24dp,
                        itemColor = itemColor,
                        selected = s == HomeNavi.Fuel
                    ) { clickNavi(HomeNavi.Fuel) }

                    NavItem(
                        label = R.string.navigation_message,
                        icon = R.drawable.round_chat_24,
                        itemColor = itemColor,
                        selected = s == HomeNavi.Message,
                        badge = unreadMessageCount.takeIf { it > 0 }?.let {
                            {
                                Badge(
                                    containerColor = AppPropTodo.Color.bottomNaviBadge,
                                    contentColor = AppPropTodo.Color.bottomNaviBadgeText,
                                ) {
                                    Text(if (it <= 999) it.toString() else "999+")
                                }
                            }
                        }
                    ) { clickNavi(HomeNavi.Message) }
                }
            }
        }
    ) {
        when (selectedNavi.value) {
            HomeNavi.Dashboard -> {
                jp.co.toukei.log.trustar.feature.home.HomeDashboard(
                    vm, it, snbHost,
                    bellImportantClick = {
                        navigator.navigate(NoticeListDestination(Important_Notice))
                    },
                    bellNormalClick = {
                        navigator.navigate(NoticeListDestination(Normal_Notice))
                    },
                    navigationBinHeader = vm::navigationBinHeader
                )
            }

            HomeNavi.Detail -> {
                vm.detailNav.selectedBinHeader.value?.todo()?.let { b ->
                    jp.co.toukei.log.trustar.feature.home.HomeDetail(
                        vm = vm,
                        paddingValues = it,
                        snbHost = snbHost,
                        bin = b,
                    )
                }
                if (vm.detailNav.selectedBinHeader.value == null) {
                    LaunchedEffect(Unit) {
                        vm.postMenuId(HomeNavi.Dashboard, false)
                    }
                }
            }

            HomeNavi.Operate -> {
                val working = vm.operateNav.currentWork.value

                if (working == null) {
                    LaunchedEffect(Unit) {
                        vm.postMenuId(HomeNavi.Detail, false)
                    }
                } else {
                    jp.co.toukei.log.trustar.feature.home.HomeOperate(
                        vm, it,
                        bin = working,
                    )
                }
                val h = vm.detailNav.selectedBinHeader.value
                when {
                    h == null -> {
                        LaunchedEffect(Unit) {
                            vm.postMenuId(HomeNavi.Dashboard, false)
                        }
                    }
                    !h.header.binStatus.isWorking() -> {
                        LaunchedEffect(Unit) {
                            vm.postMenuId(HomeNavi.Detail, false)
                        }
                    }
                }

                DisposableEffect(Unit) {
                    onDispose {
                        if (selectedNavi.value != HomeNavi.Operate)
                            vm.operateNav.unsetAdd()
                    }
                }
            }

            HomeNavi.Fuel -> {
                HomeRefuel(
                    vm = vm,
                    paddingValues = it,
                    snbHost = snbHost,
                )
            }

            HomeNavi.Message -> {
                LaunchedEffect(Unit) {
                    vm.reloadMessageRoom()
                }
                NavMessage(it, snbHost, vm)
            }
        }
    }
    Msg(vm, snbHost)

    BackHandler {
        val id = when (vm.selectedNavigateItem.value) {
            HomeNavi.Detail -> HomeNavi.Dashboard
            HomeNavi.Operate -> HomeNavi.Detail
            HomeNavi.Fuel -> HomeNavi.Dashboard
            HomeNavi.Message -> HomeNavi.Dashboard
            else -> null
        }
        id?.let(clickNavi)
    }
}

@Composable
fun Msg(vm: HomeVM, snbHost: SnackbarHostState) {
    val scope = rememberCoroutineScope()

    val msg by vm.msgFlow.collectAsState(null)
    msg?.let {
        if (it is VmEvent.Msg) {
            snbHost.replaceMessage(scope, it.id)
        }
    }
}

@Composable
private fun RowScope.NavItem(
    @StringRes label: Int,
    @DrawableRes icon: Int,
    itemColor: NavigationBarItemColors,
    selected: Boolean,
    badge: @Composable (BoxScope.() -> Unit)? = null,
    onSelect: () -> Unit,
) {
    NavigationBarItem(
        selected = selected,
        onClick = onSelect,
        icon = {
            if (badge != null) {
                BadgedBox(
                    badge = badge
                ) {
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = null,
                    )
                }
            } else {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                )
            }
        },
        label = {
            Text(
                text = stringResource(id = label),
                fontSize = 9.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        colors = itemColor,
    )
}
