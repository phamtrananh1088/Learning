@file:OptIn(
    ExperimentalLayoutApi::class
)

package jp.co.toukei.log.trustar.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pullrefresh.pullRefresh
import androidx.compose.material3.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sebaslogen.resaca.rememberScoped
import jp.co.toukei.log.common.compose.AppPropTodo
import jp.co.toukei.log.common.compose.HeadButton
import jp.co.toukei.log.lib.compose.DefaultCheckboxDialog
import jp.co.toukei.log.lib.compose.DefaultConfirmDialog
import jp.co.toukei.log.lib.compose.DefaultProgressDialog
import jp.co.toukei.log.lib.compose.bottomShadow
import jp.co.toukei.log.lib.compose.delegatePress
import jp.co.toukei.log.lib.compose.positionInParent
import jp.co.toukei.log.lib.compose.rememberDistinctBy
import jp.co.toukei.log.lib.compose.replaceMessage
import jp.co.toukei.log.lib.compose.verticalScrollbar
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.compose.sheet.EndBinSheet
import jp.co.toukei.log.trustar.enum.EndBinState
import jp.co.toukei.log.trustar.getLastWeather
import jp.co.toukei.log.trustar.other.Weather
import jp.co.toukei.log.trustar.preventAutoModeDialog
import jp.co.toukei.log.trustar.setPreventAutoModeDialog
import jp.co.toukei.log.trustar.viewmodel.HomeVM


@Composable
private fun BinDetailListRow(
    row: ComposeData.BinDetailRow,
    detailClick: (ComposeData.BinRow) -> Unit,
    click: (ComposeData.BinDetailRow) -> Unit,
) {
    BinDetailListRow(
        statusNm = row.statusNm,
        nameAndTime = row.workTitle,
        appointedTime = row.appointedTime,
        target = row.target,
        address = row.address,
        statusBgColor = row.statusBgColor,
        statusTextColor = row.statusTextColor,
        rowBgColor = row.rowBgColor,
        detailClick = { detailClick(row.row) },
        click = { click(row) },
        locationLabel = row.locationLabel,
        warning = row.warning,
        hasNotice = row.hasNotice,
    )
}

@Composable
private fun BinDetailListRow(
    statusNm: String,
    nameAndTime: String,
    appointedTime: String?,
    target: String,
    address: String?,
    statusBgColor: Int,
    statusTextColor: Int,
    rowBgColor: Int,
    detailClick: () -> Unit,
    click: () -> Unit,
    locationLabel: ComposeData.Label?,
    warning: String?,
    hasNotice: Boolean,
) {
    var workaround1 by remember {
        //fixme    FlowRow bug. use IntrinsicSize.Min
        mutableIntStateOf(0)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 1.dp)
            .background(Color(rowBgColor))
            .clickable(onClick = click)
            .padding(vertical = 10.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier
                .onSizeChanged {
                    workaround1 = it.height
                }
                .weight(1F, fill = true),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                FlowRow(
                    modifier = Modifier.weight(1F, fill = true),
                    verticalArrangement = Arrangement.Center,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = nameAndTime,
                        style = AppPropTodo.Text.defaultBold,
                    )
                    if (appointedTime != null) {
                        Text(
                            text = appointedTime,
                            style = AppPropTodo.Text.smallBold,
                        )
                    }
                }
                if (locationLabel != null) {
                    BinStatusLabel(
                        modifier = Modifier.padding(end = 8.dp),
                        text = locationLabel.string,
                        bgColor = locationLabel.color,
                        textColor = Color.White,
                        minWide = 2
                    )
                }
                BinStatusLabel(
                    modifier = Modifier
                        .padding(end = 8.dp),
                    text = statusNm,
                    bgColor = Color(statusBgColor),
                    textColor = Color(statusTextColor),
                    minWide = 4
                )
            }
            Text(
                text = target,
                style = AppPropTodo.Text.defaultBold,
            )
            Text(
                text = address.orEmpty(),
                style = AppPropTodo.Text.default,
            )
        }

        Box(
            modifier = Modifier
                .heightIn(
                    min = LocalDensity.current.run { workaround1.toDp() - 2.dp }
                ),
            contentAlignment = Alignment.Center,
        ) {
            if (warning != null && workaround1 > 1) {
                Text(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(vertical = 4.dp),
                    text = warning,
                    style = AppPropTodo.Text.mini,
                    color = Color.Red,
                )
            }
            BinInfoIconButton(
                onClick = detailClick,
                tint = when {
                    hasNotice -> AppPropTodo.Color.binOpNotice
                    else -> MaterialTheme.colorScheme.primary
                }
            )
        }
    }
}


@Composable
private fun BinList(
    state: State<List<ComposeData.BinDetailRow>>,
    detailClick: (ComposeData.BinRow) -> Unit,
    click: (ComposeData.BinDetailRow) -> Unit,
) {
    val list by state

    val listKey: (ComposeData.BinDetailRow) -> String = {
        it.row.run { "$allocationNo\n$allocationRowNo" }
    }
    val safeList = list.rememberDistinctBy(listKey)

    val listState: LazyListState = rememberLazyListState()

    val firstWorkingRow by remember(safeList) {
        derivedStateOf {
            list.firstOrNull()
                ?.takeIf { it.workStatus.isWorkingOrMoving() }
                ?.row?.allocationRowNo
        }
    }

    LaunchedEffect(firstWorkingRow) {
        if (firstWorkingRow != null)
            listState.scrollToItem(0)
    }

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
            BinDetailListRow(
                row = it,
                detailClick = detailClick,
                click = click,
            )
        }
    }
}

@Composable
private fun Head(
    inAutoMode: Boolean,
    allocationNm: String,
    truckNm: String,
    displayModeSwitch: Boolean,
    modeSwitchChange: (Boolean) -> Unit,
) {
    var modeChangedByUser by rememberScoped {
        mutableStateOf(false)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier
                .weight(1F)
                .padding(8.dp),
        ) {
            Text(
                text = allocationNm,
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = truckNm,
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.titleLarge,
            )
        }
        if (displayModeSwitch) {
            val interactionSource = remember {
                MutableInteractionSource()
            }
            val m = remember { mutableStateOf(Offset.Zero) }

            Text(
                modifier = Modifier
                    .delegatePress(interactionSource, {
                        modeChangedByUser = true
                        modeSwitchChange(!inAutoMode)
                    }, m)
                    .padding(horizontal = 8.dp),
                text = stringResource(
                    if (inAutoMode) R.string.work_mode_automatic else R.string.work_mode_manual
                ),
                style = AppPropTodo.Text.defaultBold,
            )
            val c = MaterialTheme.colorScheme
            Switch(
                modifier = Modifier
                    .positionInParent(m),
                checked = inAutoMode,
                onCheckedChange = {
                    modeChangedByUser = true
                    modeSwitchChange(it)
                },
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
    if (modeChangedByUser) {
        if (Current.userInfo.preventAutoModeDialog()) {
            modeChangedByUser = false
        } else {
            val onDismissRequest = {
                modeChangedByUser = false
            }
            val checkState = rememberScoped {
                mutableStateOf(false)
            }
            DefaultCheckboxDialog(
                checkState = checkState,
                checkBoxText = stringResource(R.string.auto_mode_do_not_show),
                onDismissRequest = onDismissRequest,
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (checkState.value) {
                                Current.userInfo.setPreventAutoModeDialog()
                            }
                            onDismissRequest()
                        }
                    ) {
                        Text(stringResource(id = android.R.string.ok))
                    }
                },
                title = stringResource(
                    if (inAutoMode) R.string.auto_mode_a_dialog_title else R.string.auto_mode_m_dialog_title
                ),
                content = stringResource(
                    if (inAutoMode) R.string.auto_mode_a_dialog_msg else R.string.auto_mode_m_dialog_msg
                ),
            )
        }
    }
}

@Composable
private fun Head2(
    vm: HomeVM,
    bin: ComposeData.Bin,
    snbHost: SnackbarHostState,
    hasWorkingWorks: Boolean,
    hasNotStartedWorks: Boolean,

    addWork: () -> Unit,
    startBinClick: (ComposeData.Bin) -> Unit,
    endBinClick: (ComposeData.EndBin) -> Unit,
) {
    val workAddEnabled by vm.detailNav.enableAddWorkBin
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val status = bin.binStatus
    val finished = status.isFinished()
    val statusColor = if (finished)
        AppPropTodo.Color.binFinishedColor
    else
        MaterialTheme.colorScheme.primary

    val h by vm.binHeaderList.subscribeAsState(emptyList())

    val hasWorkingHeader by remember(h) {
        derivedStateOf {
            h.firstOrNull { it.header.binStatus.isWorking() } != null
        }
    }

    var displayEndBinDialog by rememberScoped {
        mutableStateOf<Weather?>(null)
    }
    displayEndBinDialog?.let { weather ->
        val onDismissRequest = {
            displayEndBinDialog = null
        }
        if (hasWorkingWorks) {
            LaunchedEffect(Unit) {
                snbHost.replaceMessage(scope, R.string.end_when_working_exists_msg)
                onDismissRequest()
            }
        } else {
            vm.WhenBinStatusChanged(
                allocationNo = bin.allocationNo,
                action = onDismissRequest
            )
            DefaultConfirmDialog(
                onDismissRequest = onDismissRequest,
                confirmButtonText = R.string.operation_end,
                dismissButtonText = R.string.cancel,
                title = if (hasNotStartedWorks) R.string.end_when_ready_exists_alert_title else R.string.confirm,
                content = if (hasNotStartedWorks) R.string.end_when_ready_exists_alert_msg else R.string.operation_end_alert_msg,
            ) {
                endBinClick(ComposeData.EndBin(bin, weather))
                onDismissRequest()
            }
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(statusColor)
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        HeadButton(
            enabled = !finished,
            horizontalPadding = 32,
            verticalPadding = 8,
            text = stringResource(
                if (status.started()) R.string.operation_end
                else R.string.operation_start
            ),
            style = AppPropTodo.Text.defaultBold,
            color = MaterialTheme.colorScheme.onPrimary,
            contentColor = statusColor,
            contentEnd = {
                Icon(
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .size(20.dp),
                    painter = painterResource(
                        id = if (status.isNew()) R.drawable.ic_bin_start
                        else R.drawable.ic_bin_stop
                    ),
                    contentDescription = null,
                )
            },
        ) {
            when {
                status.isNew() -> {
                    if (hasWorkingHeader) {
                        snbHost.replaceMessage(
                            scope,
                            R.string.start_when_started_operation_exists_msg
                        )
                    } else {
                        startBinClick(bin)
                    }
                }

                status.isWorking() -> {
                    val weather = context.getLastWeather()
                    if (weather == null) {
                        snbHost.replaceMessage(scope, R.string.weather_need_to_be_set_msg)
                    } else {
                        displayEndBinDialog = weather
                    }
                }
            }
        }
        HeadButton(
            enabled = workAddEnabled,
            horizontalPadding = 16,
            verticalPadding = 4,
            text = stringResource(id = R.string.work_add),
            style = AppPropTodo.Text.smallBold,
            color = MaterialTheme.colorScheme.onPrimary,
            contentColor = statusColor,
            click = addWork,
        )
    }
}

@Composable
fun HomeDetail(
    vm: HomeVM,
    snbHost: SnackbarHostState,
    modifier: Modifier,
    bin: ComposeData.Bin,
    list: State<List<ComposeData.BinDetailRow>>,
    detailClick: (ComposeData.BinRow) -> Unit,
    modeSwitchChange: (Boolean) -> Unit,

    addWork: (ComposeData.Bin) -> Unit,
    startBinClick: (ComposeData.Bin) -> Unit,
    selectBinDetail: (ComposeData.BinRow) -> Unit,
    startInAutoMode: (ComposeData.BinDetailRow, ComposeData.Location) -> Unit,
) {

    val scope = rememberCoroutineScope()

    val h1 by list

    val hasNotStartedWorks by remember(h1) {
        derivedStateOf {
            h1.firstOrNull { it.workStatus.isNew() } != null
        }
    }
    val hasWorkingWorks by remember(h1) {
        derivedStateOf {
            h1.firstOrNull { it.workStatus.isWorking() } != null
        }
    }

    var moveBinDetailDialog by rememberScoped {
        mutableStateOf<ComposeData.BinRow?>(null)
    }
    var startAutoModeBinDetailDialog by rememberScoped {
        mutableStateOf<Pair<ComposeData.BinDetailRow, ComposeData.Location>?>(null)
    }

    val refreshing by vm.fetchBinDataState
    val refreshState = rememberPullRefreshState(
        refreshing = refreshing,
        onRefresh = { vm.fetchBinData() }
    )

    val binStatus = bin.binStatus

    val click = { it: ComposeData.BinDetailRow ->
        val detail = it.row
        val notAutoMode = !vm.detailNav.inAutoMode.value
        when {
            binStatus.isFinished() -> {
                if (notAutoMode) {
                    snbHost.replaceMessage(
                        scope,
                        R.string.work_after_operation_finished_tip
                    )
                }
            }

            binStatus.isNew() -> {
                if (notAutoMode) {
                    snbHost.replaceMessage(
                        scope,
                        R.string.work_before_operation_start_tip
                    )
                }
            }

            binStatus.isWorking() -> {
                val workStatus = it.workStatus
                when {
                    workStatus.isWorkingOrMoving() -> {
                        if (notAutoMode) {
                            selectBinDetail(detail)
                        }
                    }

                    hasWorkingWorks -> {
                        if (notAutoMode) {
                            snbHost.replaceMessage(
                                scope,
                                R.string.move_work_when_working_exists_msg
                            )
                        }
                    }

                    workStatus.isFinished() -> {
                        if (notAutoMode) {
                            selectBinDetail(detail)
                        }
                    }

                    workStatus.isNew() -> {
                        if (notAutoMode) {
                            moveBinDetailDialog = detail
                        } else if (!it.hasPlaceLocation) {
                            val l = it.lastDetectLocation
                            if (l != null) {
                                startAutoModeBinDetailDialog = it to l
                            }
                        }
                    }
                }
            }
        }
    }

    var displayEndBinDialog by rememberScoped {
        mutableStateOf<ComposeData.EndBin?>(null)
    }

    val endBinClick: (ComposeData.EndBin) -> Unit = { e ->
        if (Current.userInfo.meterInputEnabled) {
            displayEndBinDialog = e
        } else {
            vm.endBin(e, null)
        }
    }


    Column(
        modifier = modifier
    ) {
        Head(
            vm.detailNav.inAutoMode.value,
            allocationNm = bin.allocationNm,
            truckNm = bin.truckNm,
            displayModeSwitch = binStatus.isWorking() && vm.userInfo.geofenceUseFlag,
            modeSwitchChange = modeSwitchChange,
        )
        Head2(
            vm = vm,
            bin = bin,
            snbHost = snbHost,
            hasWorkingWorks = hasWorkingWorks,
            hasNotStartedWorks = hasNotStartedWorks,
            addWork = {
                addWork(bin)
            },
            startBinClick = startBinClick,
            endBinClick = endBinClick,
        )
        Box(
            modifier = Modifier
                .clipToBounds()
                .pullRefresh(refreshState)
        ) {
            BinList(
                state = list,
                detailClick = detailClick,
                click = click,
            )
            BinPullRefresh(refreshing, refreshState)
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .bottomShadow(6.dp),
            )
        }
    }
    moveBinDetailDialog?.let { b ->
        val onDismissRequest = {
            moveBinDetailDialog = null
        }
        vm.WhenWorkStatusOrBinStatusChanged(
            allocationNo = b.allocationNo,
            allocationRow = b.allocationRowNo,
            action = onDismissRequest,
        )
        DefaultConfirmDialog(
            onDismissRequest = onDismissRequest,
            confirmButtonText = R.string.work_move,
            dismissButtonText = R.string.cancel,
            title = R.string.work_move_to_destination_title,
            content = R.string.work_move_to_destination_msg,
        ) {
            selectBinDetail(b)
            onDismissRequest()
        }
    }

    startAutoModeBinDetailDialog?.let { (b, location) ->
        val onDismissRequest = {
            startAutoModeBinDetailDialog = null
        }
        vm.WhenWorkStatusOrBinStatusChanged(
            allocationNo = b.row.allocationNo,
            allocationRow = b.row.allocationRowNo,
            action = onDismissRequest,
        )
        DefaultConfirmDialog(
            onDismissRequest = onDismissRequest,
            confirmButtonText = R.string.yes,
            dismissButtonText = R.string.no,
            content = R.string.work_start_without_dest_location_msg,
        ) {
            startInAutoMode(b, location)
            onDismissRequest()
        }
    }
    displayEndBinDialog?.let { e ->
        val onDismissRequest = {
            displayEndBinDialog = null
        }
        vm.WhenBinStatusChanged(
            allocationNo = e.bin.allocationNo,
            action = onDismissRequest
        )
        EndBinSheet(
            onDismissRequest = onDismissRequest,
            defaultMeter = e.bin.incomingMeter
        ) { input ->
            vm.endBin(e, input)
        }
    }

    val binEndState by vm.endStateSyncState

    when (binEndState) {
        EndBinState.Loading -> {
            DefaultProgressDialog(
                title = stringResource(id = R.string.data_end_data_sending),
                content = stringResource(id = R.string.data_end_data_sending_msg)
            )
        }

        EndBinState.Error -> {
            DefaultConfirmDialog(
                confirmButtonText = R.string.data_resend,
                dismissButtonText = R.string.cancel,
                dismissButtonClick = vm::dismissBinSync,
                title = R.string.server_connection_err,
                content = R.string.sync_connection_err_alert_msg,
                confirmButtonClick = vm::endBinSync
            )
        }
    }
}
