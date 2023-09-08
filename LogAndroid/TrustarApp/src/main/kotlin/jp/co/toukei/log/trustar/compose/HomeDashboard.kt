@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package jp.co.toukei.log.trustar.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.pullrefresh.pullRefresh
import androidx.compose.material3.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import jp.co.toukei.log.common.compose.AppPropTodo
import jp.co.toukei.log.common.compose.PrimarySurface
import jp.co.toukei.log.lib.compose.bottomShadow
import jp.co.toukei.log.lib.compose.delegatePress
import jp.co.toukei.log.lib.compose.minWidthMatchHeight
import jp.co.toukei.log.lib.compose.positionInParent
import jp.co.toukei.log.lib.compose.rememberDistinctBy
import jp.co.toukei.log.lib.compose.replaceMessage
import jp.co.toukei.log.lib.compose.verticalScrollbar
import jp.co.toukei.log.lib.toggle
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.compose.ComposeData.BinHeaderRow
import jp.co.toukei.log.trustar.db.user.embedded.BinHeaderAndStatus
import jp.co.toukei.log.trustar.viewmodel.HomeVM


@Composable
private fun Head(
    vm: HomeVM,
    bellImportantClick: () -> Unit,
    bellNormalClick: () -> Unit,
) {
    val importantNotice by vm.unreadImportant.subscribeAsState(0)
    val normalNotice by vm.unreadNormal.subscribeAsState(0)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Text(
            text = vm.userInfo.userNm,
            modifier = Modifier
                .weight(1F)
                .padding(8.dp),
            style = MaterialTheme.typography.titleLarge,
        )
        BadgeImage(
            importantNotice,
            AppPropTodo.Color.bellImportant,
            stringResource(id = R.string.dashboard_important),
            bellImportantClick
        )
        BadgeImage(
            normalNotice,
            AppPropTodo.Color.bellNormal,
            stringResource(id = R.string.dashboard_normal),
            bellNormalClick
        )
    }
}


@Composable
private fun BadgeImage(
    badge: Int,
    tint: Color,
    label: String,
    click: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val interactionSource = remember {
            MutableInteractionSource()
        }

        val m = remember { mutableStateOf(Offset.Zero) }
        Box(
            modifier = Modifier
                .positionInParent(m)
                .size(48.dp)
                .clip(CircleShape)
                .clickable(
                    indication = rememberRipple(radius = 24.dp),
                    interactionSource = interactionSource,
                    onClick = click
                )
                .padding(4.dp),
            contentAlignment = Alignment.TopStart
        ) {
            Image(
                modifier = Modifier
                    .fillMaxSize(),
                imageVector = Icons.Default.Notifications,
                contentDescription = null,
                colorFilter = ColorFilter.tint(tint),
            )
            if (badge > 0) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .background(Color.White, RoundedCornerShape(50))
                        .padding(1.2.dp)
                        .background(Color.Red, RoundedCornerShape(50))
                        .minWidthMatchHeight(),
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 2.dp),
                        text = badge.toString(),
                        style = MaterialTheme.typography.labelMedium,
                        maxLines = 1,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier
                .delegatePress(interactionSource, click, m)
        )
    }
}

@Composable
private fun Head2(
    vm: HomeVM,
    amount: Int,
    remain: Int,
    showAllToggle: () -> Unit,
) {
    PrimarySurface {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier
                    .weight(1F)
                    .padding(horizontal = 8.dp),
                horizontalAlignment = Alignment.Start,
            ) {
                val st = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                Text(
                    text = stringResource(id = R.string.today_operation_amount_d, amount),
                    style = st,
                )
                Spacer(
                    modifier = Modifier.padding(2.dp)
                )
                Text(
                    text = stringResource(id = R.string.today_operation_remain_d, remain),
                    style = st,
                )
            }
            val interactionSource = remember {
                MutableInteractionSource()
            }
            val m = remember { mutableStateOf(Offset.Zero) }

            Text(
                modifier = Modifier
                    .delegatePress(interactionSource, showAllToggle, m)
                    .padding(horizontal = 8.dp),
                text = stringResource(id = R.string.show_all),
                style = MaterialTheme.typography.labelLarge,
            )
            val c = MaterialTheme.colorScheme
            Switch(
                modifier = Modifier
                    .positionInParent(m),
                checked = vm.dashboardShowAll.value,
                onCheckedChange = { showAllToggle() },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = c.onPrimary,
                    checkedTrackColor = c.primaryContainer,
                    checkedBorderColor = Color.Transparent,
                    uncheckedThumbColor = c.onPrimary.copy(alpha = 0.5F),
                    uncheckedTrackColor = c.scrim.copy(alpha = 0.5F),
                    uncheckedBorderColor = c.primaryContainer.copy(alpha = 0.8F),
                ),
                interactionSource = interactionSource,
            )
        }
    }
}

@Composable
private fun AddUnscheduledBin(
    click: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = click)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier
                .padding(4.dp),
            imageVector = Icons.Default.AddCircle,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            modifier = Modifier
                .padding(8.dp),
            text = stringResource(id = R.string.operation_add_unscheduled),
            style = MaterialTheme.typography.titleMedium,
        )
    }
}

@Composable
fun BinHeaderListRow(
    row: BinHeaderRow,
    detailClick: ((BinHeaderRow) -> Unit)? = null,
    click: (BinHeaderRow) -> Unit,
) {
    BinHeaderListRow(
        allocationNm = row.allocationNm,
        binStatusNm = row.statusNm,
        statusBgColor = row.statusBgColor,
        statusTextColor = row.statusTextColor,
        detailClick = detailClick?.let { { it(row) } },
        click = { click(row) }
    )
}

@Composable
private fun BinHeaderListRow(
    allocationNm: String,
    binStatusNm: String,
    statusBgColor: Int,
    statusTextColor: Int,
    detailClick: (() -> Unit)? = null,
    click: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = click)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier
                .weight(1F)
                .padding(8.dp),
            text = allocationNm,
            style = MaterialTheme.typography.titleLarge,
        )
        BinStatusLabel(
            text = binStatusNm,
            bgColor = Color(statusBgColor),
            textColor = Color(statusTextColor),
            minWide = 3,
        )
        if (detailClick != null) {
            BinInfoIconButton(
                onClick = detailClick
            )
        }
    }
}

@Composable
private fun BinHeaderList(
    vm: HomeVM,
    addClick: () -> Unit,
    rowClick: (BinHeaderRow) -> Unit,
    detailClick: (BinHeaderRow) -> Unit,
    binList: List<BinHeaderRow>,
    binListUnfinished: List<BinHeaderRow>,
) {
    val listKey = BinHeaderRow.key
    val showAll by vm.dashboardShowAll
    val safeList = (if (showAll) binList else binListUnfinished).run {
        rememberDistinctBy(listKey)
    }
    val listState: LazyListState = rememberLazyListState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .verticalScrollbar(listState),
        state = listState,
    ) {
        items(
            items = safeList,
            key = listKey,
        ) {
            BinHeaderListRow(
                it,
                detailClick = detailClick,
                click = rowClick
            )
        }
        item {
            AddUnscheduledBin(addClick)
        }
    }
}

@Composable
private fun BinHeaderList(
    vm: HomeVM,
    list: List<BinHeaderRow>,
    unfinished: List<BinHeaderRow>,
    addClick: () -> Unit,
    rowClick: (BinHeaderRow) -> Unit,
    detailClick: (BinHeaderRow) -> Unit,
) {
    val refreshing by vm.fetchBinDataState

    val refreshState = rememberPullRefreshState(
        refreshing = refreshing,
        onRefresh = { vm.fetchBinData() }
    )
    Box(
        modifier = Modifier
            .clipToBounds()
            .pullRefresh(refreshState)
    ) {
        BinHeaderList(
            vm = vm,
            addClick = addClick,
            rowClick = rowClick,
            detailClick = detailClick,
            binList = list,
            binListUnfinished = unfinished,
        )
        BinPullRefresh(refreshing, refreshState)
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .bottomShadow(6.dp),
        )
    }
}

@Composable
fun HomeDashboard(
    vm: HomeVM,
    snbHost: SnackbarHostState,
    modifier: Modifier,
    bellImportantClick: () -> Unit,
    bellNormalClick: () -> Unit,
    addClick: () -> Unit,
    rowClick: (BinHeaderRow) -> Unit,
    detailClick: (BinHeaderRow) -> Unit,
) {
    val scope = rememberCoroutineScope()

    val l by vm.binHeaderList.subscribeAsState(emptyList())
    val list = l.map(BinHeaderAndStatus::toRow)

    val unfinished = remember(list) {
        list.filterNot { it.binStatus.isFinished() }
    }
    val working = remember(list) {
        list.filter { it.binStatus.isWorking() }
    }

    Column(
        modifier = modifier
    ) {
        Head(
            vm = vm,
            bellImportantClick = bellImportantClick,
            bellNormalClick = bellNormalClick,
        )
        Head2(
            vm = vm,
            amount = list.size,
            remain = unfinished.size,
            showAllToggle = {
                vm.dashboardShowAll.toggle()
            }
        )
        BinHeaderList(
            vm = vm,
            list = list,
            unfinished = unfinished,
            addClick = {
                if (working.isNotEmpty()) {
                    snbHost.replaceMessage(scope, R.string.start_when_started_operation_exists_msg)
                } else {
                    addClick()
                }
            },
            rowClick = rowClick,
            detailClick = detailClick
        )
    }
}
