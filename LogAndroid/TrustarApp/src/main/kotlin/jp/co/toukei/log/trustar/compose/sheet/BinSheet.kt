package jp.co.toukei.log.trustar.compose.sheet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sebaslogen.resaca.rememberScoped
import jp.co.toukei.log.common.compose.CircularProgress
import jp.co.toukei.log.common.compose.InputBoxDialog
import jp.co.toukei.log.common.compose.NumberInputBoxDialog
import jp.co.toukei.log.lib.compose.DefaultConfirmDialog
import jp.co.toukei.log.lib.compose.DefaultSheet
import jp.co.toukei.log.lib.compose.NumberInputBox
import jp.co.toukei.log.lib.compose.rememberDistinctBy
import jp.co.toukei.log.lib.optional
import jp.co.toukei.log.lib.orElseNull
import jp.co.toukei.log.lib.util.DecimalInputFilter
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.binKilometerInputValidation
import jp.co.toukei.log.trustar.compose.BinDetailTable
import jp.co.toukei.log.trustar.compose.BinHeaderListRow
import jp.co.toukei.log.trustar.compose.BinHeaderTable
import jp.co.toukei.log.trustar.compose.ComposeData
import jp.co.toukei.log.trustar.compose.TwoButton1
import jp.co.toukei.log.trustar.compose.WhenBinStatusChanged
import jp.co.toukei.log.trustar.db.user.embedded.BinHeaderAndStatus
import jp.co.toukei.log.trustar.db.user.entity.DelayReason
import jp.co.toukei.log.trustar.db.user.entity.Fuel
import jp.co.toukei.log.trustar.enum.StartUnscheduledBinState
import jp.co.toukei.log.trustar.other.BinDetailExtraData
import jp.co.toukei.log.trustar.other.WorkMode
import jp.co.toukei.log.trustar.viewmodel.BinDetailExtraVM
import jp.co.toukei.log.trustar.viewmodel.BinHeaderVM
import jp.co.toukei.log.trustar.viewmodel.HomeVM
import jp.co.toukei.log.trustar.viewmodel.StartBinVM
import jp.co.toukei.log.trustar.viewmodel.StartUnscheduledVM
import java.util.Optional


@Composable
private fun MeterInput(
    title: String,
    content: String,
    defaultInput: Int?,
    button1Text: String,
    button2Text: String,
    button1Click: () -> Unit,
    button2Click: (Int?) -> Unit,
) {
    val modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()
    val c1 = MaterialTheme.colorScheme.primary
    val c2 = MaterialTheme.colorScheme.onPrimary
    Column(
        modifier = modifier,
    ) {
        Text(
            modifier = modifier,
            text = title
        )
        Text(
            modifier = modifier,
            text = content
        )
        var input by rememberScoped {
            mutableStateOf(defaultInput)
        }
        NumberInputBox(
            modifier = modifier,
            value = input?.toString().orEmpty(),
            inputValidation = binKilometerInputValidation(),
            onValueChange = { s, _ -> input = s.toIntOrNull() },
            keyboardType = KeyboardType.Number,
        )
        TwoButton1(
            modifier = modifier,
            color = c1,
            contentColor = c2,
            button1Text = button1Text,
            button2Text = button2Text,
            button1Click = button1Click,
            button2Click = { button2Click(input) },
            button2Enabled = input != null
        )
    }
}

@Composable
fun EndBinSheet(
    onDismissRequest: () -> Unit,
    defaultMeter: Int?,
    meter: (Int?) -> Unit,
) {
    DefaultSheet(onDismiss = onDismissRequest) { dismissRequest ->
        MeterInput(
            title = stringResource(R.string.incoming_meter_input_title),
            content = stringResource(R.string.incoming_meter_in_km),
            defaultInput = defaultMeter,
            button1Text = stringResource(id = R.string.cancel),
            button2Text = stringResource(id = R.string.confirm_input),
            button1Click = dismissRequest,
            button2Click = {
                meter(it)
                dismissRequest()
            },
        )
    }
}

@Composable
fun StartBinSheet(
    hvm: HomeVM, //todo
    vm: StartBinVM,
    onDismissRequest: () -> Unit,
) {
    val data by vm.data.subscribeAsState(null)
    data?.let { o ->
        val d = o.orElseNull()
        if (d == null) {
            LaunchedEffect(Unit) { onDismissRequest() }
        } else {
            DefaultSheet(onDismiss = onDismissRequest) { dismissRequest ->
                val allocationNo = d.header.allocationNo
                hvm.WhenBinStatusChanged(
                    allocationNo,
                    action = dismissRequest
                )
                var selectedTruck by rememberScoped {
                    mutableStateOf(d.truck.run { ComposeData.TruckKun(truckCd, truckNm) })
                }
                var inputtedMeter by rememberScoped {
                    mutableStateOf<Optional<Int>?>(null)
                }

                var step by rememberScoped {
                    mutableIntStateOf(1)
                }
                when (step) {
                    1 -> {
                        val list by vm.truckList.subscribeAsState(emptyList())
                        StartBinTruckChoose(
                            list = list,
                            defaultTruck = selectedTruck,
                            button1Text = stringResource(id = R.string.change_vehicle),
                            button2Text = stringResource(id = R.string.yes),
                            button2EndIcon = null,
                        ) {
                            selectedTruck = it
                            step = if (vm.meterInputEnabled) 2 else 3
                        }
                    }

                    2 -> {
                        val setMeter = { meter: Int? ->
                            inputtedMeter = meter.optional()
                            step = 3
                        }
                        MeterInput(
                            title = stringResource(R.string.outgoing_meter_input_title),
                            content = stringResource(R.string.outgoing_meter_in_km),
                            defaultInput = d.header.outgoingMeter,
                            button1Text = stringResource(id = R.string.input_later),
                            button2Text = stringResource(id = R.string.confirm_input),
                            button1Click = { setMeter(null) },
                            button2Click = setMeter,
                        )
                    }

                    3 -> {
                        val startWithWork = { workMode: WorkMode ->
                            val location = Current.lastLocation
                            vm.startBin(
                                allocationNo = allocationNo,
                                truck = selectedTruck,
                                setKilometer = inputtedMeter,
                                location = location
                            )
                            //todo
                            val task = vm.user.binLocationTask
                            if (workMode == WorkMode.Automatic) {
                                task.setInAutoMode(allocationNo)
                            } else {
                                task.cancelAutoMode()
                            }
                        }

                        if (vm.geofenceUseFlag) {
                            val binList by vm.binDetailList.subscribeAsState(emptyList())
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.TopCenter,
                            ) {
                                WorkModeChoose(
                                    showWarning = binList.any { it.placeLocation == null },
                                    onClick = startWithWork
                                )
                            }
                        } else {
                            LaunchedEffect(Unit) {
                                startWithWork(WorkMode.Normal)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StartUnscheduledBinSheet(
    hvm: HomeVM,
    vm: StartUnscheduledVM,
    onDismissRequest: () -> Unit,
    onSuccess: (String?) -> Unit,
) {
    val b by hvm.binHeaderList.subscribeAsState(null)
    b?.let { l ->
        if (
            vm.sus.value?.detectHasWorking != false &&
            l.any { it.header.binStatus.isWorking() }
        ) {
            DefaultConfirmDialog(
                content = R.string.start_when_started_operation_exists_msg,
                confirmButtonText = R.string.confirm,
                confirmButtonClick = onDismissRequest
            )
        } else {
            DefaultSheet(onDismiss = onDismissRequest) { dismissRequest ->
                StartUnscheduledBin(
                    vm = vm,
                    onSuccess = {
                        onSuccess(it)
                        dismissRequest()
                    },
                    cancel = dismissRequest
                )
            }
        }
    }
}

@Composable
private fun StartUnscheduledBin(
    vm: StartUnscheduledVM,
    onSuccess: (String?) -> Unit,
    cancel: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 256.dp),
        contentAlignment = Alignment.Center,
    ) {
        var selectedTruck by rememberScoped {
            mutableStateOf<ComposeData.TruckKun?>(null)
        }
        var inputtedMeter by rememberScoped {
            mutableStateOf<Int?>(null)
        }

        val sus = vm.sus
        when (val s = sus.value) {
            is StartUnscheduledBinState.TokenLoading -> {
                CircularProgress()
            }

            is StartUnscheduledBinState.GetTokenError -> {
                CircularProgress()
                DefaultConfirmDialog(
                    content = R.string.start_bin_connection_err_alert_msg,
                    confirmButtonText = R.string.confirm,
                    confirmButtonClick = cancel
                )
            }

            is StartUnscheduledBinState.WorkingBinExists -> {
                LaunchedEffect(Unit) {
                    cancel()
                }
            }

            is StartUnscheduledBinState.Ready -> {
                val token = s.token
                var step by rememberScoped {
                    mutableIntStateOf(1)
                }
                val setMeter = { meter: Int? ->
                    inputtedMeter = meter
                    step = 3
                }
                val setTruck = { it: ComposeData.TruckKun ->
                    selectedTruck = it
                    step = if (vm.meterInputEnabled) 2 else 3
                }
                when (step) {
                    1 -> {
                        val list by vm.truckList.subscribeAsState(emptyList())
                        StartBinTruckChoose(
                            list = list,
                            defaultTruck = selectedTruck,
                            button1Text = stringResource(id = R.string.change_vehicle),
                            button2Text = stringResource(id = R.string.operation_start),
                            button2EndIcon = Icons.Default.ArrowDownward,
                            buttonClick = setTruck,
                        )
                    }

                    2 -> {
                        MeterInput(
                            title = stringResource(R.string.outgoing_meter_input_title),
                            content = stringResource(R.string.outgoing_meter_in_km),
                            defaultInput = inputtedMeter,
                            button1Text = stringResource(id = R.string.input_later),
                            button2Text = stringResource(id = R.string.confirm_input),
                            button1Click = { setMeter(null) },
                            button2Click = setMeter,
                        )
                    }

                    3 -> {
                        LaunchedEffect(Unit) {
                            val location = Current.lastLocation
                            val t = selectedTruck
                            if (t == null) {
                                step = 1
                            } else {
                                vm.startUnscheduledBin(
                                    token = token,
                                    truck = t,
                                    kilometer = inputtedMeter,
                                    location = location
                                )
                            }
                        }
                    }
                }
            }

            is StartUnscheduledBinState.Sending -> {
                CircularProgress()
            }

            is StartUnscheduledBinState.AddFailed -> {
                Text(
                    text = stringResource(
                        R.string.server_connection_err
                    )
                )
                DefaultConfirmDialog(
                    content = R.string.start_bin_connection_err_alert_msg,
                    confirmButtonText = R.string.confirm,
                    confirmButtonClick = cancel
                )
            }

            is StartUnscheduledBinState.Added -> {
                LaunchedEffect(Unit) {
                    onSuccess(s.allocationNo)
                }
            }

            is StartUnscheduledBinState.AddedButReloadFailed -> {
                LaunchedEffect(Unit) {
                    onSuccess(null)
                }
            }

            null -> {
                LaunchedEffect(Unit) {
                    vm.fetchStartUnscheduledToken()
                }
            }
        }
    }
}

@Composable
private fun EmptyPlaceHolder() {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 64.dp),
        textAlign = TextAlign.Center,
        text = stringResource(id = R.string.no_data_placeholder)
    )
}

@Composable
fun FuelSheet(
    vm: HomeVM,
    selectFuel: (Fuel) -> Unit,
    hide: () -> Unit,
) {
    val list by vm.fuelList.subscribeAsState(emptyList())

    DefaultSheet(onDismiss = hide) { dismissRequest ->
        if (list.isEmpty()) {
            EmptyPlaceHolder()
        } else {
            LazyColumn {
                items(
                    items = list,
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectFuel(it)
                                dismissRequest()
                            }
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            modifier = Modifier
                                .weight(1F)
                                .padding(8.dp),
                            text = it.fuelNm,
                            style = MaterialTheme.typography.titleLarge,
                        )
                    }
                }
            }
        }
    }

}

@Composable
fun DelayReasonSheet(
    list: List<DelayReason>,
    select: (DelayReason) -> Unit,
    hide: () -> Unit,
) {
    DefaultSheet(onDismiss = hide) { dismissRequest ->
        if (list.isEmpty()) {
            EmptyPlaceHolder()
        } else {
            LazyColumn {
                items(
                    items = list,
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                select(it)
                                dismissRequest()
                            }
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            modifier = Modifier
                                .weight(1F)
                                .padding(8.dp),
                            text = it.reasonText.orEmpty(),
                            style = MaterialTheme.typography.titleLarge,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FuelBinListSheet(
    vm: HomeVM,
    selectBinHeader: (ComposeData.BinHeaderRow) -> Unit,
    hide: () -> Unit,
) {
    val k = ComposeData.BinHeaderRow.key
    val list by vm.binHeaderList.subscribeAsState(emptyList())
    val safeList = list
        .map(BinHeaderAndStatus::toRow)
        .rememberDistinctBy(k)

    DefaultSheet(onDismiss = hide) { dismissRequest ->
        if (list.isEmpty()) {
            EmptyPlaceHolder()
        } else {
            LazyColumn {
                items(
                    items = safeList,
                    key = k,
                ) {
                    BinHeaderListRow(row = it) { r ->
                        selectBinHeader(r)
                        dismissRequest()
                    }
                }
            }
        }
    }
}


@Composable
fun BinDetailInfo(
    vm: BinDetailExtraVM,
    clickIncidental: ((BinDetailExtraData) -> Unit)? = null,
    clickCollect: (BinDetailExtraData) -> Unit,
    clickDeliveryChart: (BinDetailExtraData) -> Unit,
    dismiss: () -> Unit,
) {
    DefaultSheet(onDismiss = dismiss) {
        val dd = vm.details.subscribeAsState(null)
        val data = dd.value

        if (data == null) {
            CircularProgress()
        } else {
            var showDelayReason by rememberScoped {
                mutableStateOf(false)
            }
            var showMemoEdit by rememberScoped {
                mutableStateOf(false)
            }
            var showTemperatureEdit by rememberScoped {
                mutableStateOf(false)
            }
            BinDetailTable(
                clickDelayReasonChange = {
                    showDelayReason = true
                },
                clickTemperature = {
                    showTemperatureEdit = true
                },
                clickMemo = {
                    showMemoEdit = true
                },
                clickIncidental = clickIncidental,
                clickCollect = clickCollect,
                clickDeliveryChart = clickDeliveryChart,
                data = data,
            )



            if (showDelayReason) {
                DelayReasonSheet(
                    list = data.delayReasons,
                    select = { vm.setDelayReason(it) }
                ) {
                    showDelayReason = false
                }
            }
            if (showMemoEdit) {
                val onDismissRequest = { showMemoEdit = false }
                InputBoxDialog(
                    onDismissRequest = onDismissRequest,
                    defaultText = data.binDetail.experiencePlaceNote1.orEmpty(),
                    inputValidation = {
                        it.length < 100
                    },
                    title = stringResource(id = R.string.bin_memo),
                    imeAction = ImeAction.Default,
                ) {
                    vm.setMemo(it)
                    onDismissRequest()
                }
            }
            if (showTemperatureEdit) {
                val onDismissRequest = { showTemperatureEdit = false }
                val regex = remember {
                    DecimalInputFilter(4, 1, true).regex
                }
                NumberInputBoxDialog(
                    onDismissRequest = onDismissRequest,
                    defaultText = data.binDetail.temperature?.toString().orEmpty(),
                    inputValidation = { s, _ ->
                        regex.matches(s)
                    },
                    title = stringResource(id = R.string.bin_temperature),
                ) {
                    vm.setTemperature(it.toDoubleOrNull())
                    onDismissRequest()
                }
            }
        }
    }
}

@Composable
fun BinHeaderInfo(
    vm: BinHeaderVM,
    dismiss: () -> Unit,
) {
    DefaultSheet(onDismiss = dismiss) {
        val d = vm.detail.subscribeAsState(null)
        val data = d.value?.orElseNull()
        if (data == null) {
            CircularProgress()
        } else {
            val h = data.header
            val t = data.truck
            var showOutEdit by rememberScoped {
                mutableStateOf(false)
            }
            var showInEdit by rememberScoped {
                mutableStateOf(false)
            }
            BinHeaderTable(
                truckNm = t.truckNm,
                startScheduled = h.planStartDatetime,
                endScheduled = h.planEndDatetime,
                start = h.startLocation?.date,
                end = h.endLocation?.date,
                crew = h.driverNm,
                crew2 = h.subDriverNm,
                note = h.allocationNote1,
                outgoing = h.outgoingMeter,
                incoming = h.incomingMeter,
                binStatusEnum = h.binStatus,
                clickOutMeter = { showOutEdit = true },
                clickInMeter = { showInEdit = true },
            )
            if (showOutEdit) {
                MeterInputBox(
                    onDismissRequest = { showOutEdit = false },
                    default = h.outgoingMeter,
                    title = stringResource(id = R.string.bin_outgoing_meter),
                ) {
                    vm.setOutgoing(it)
                }
            }
            if (showInEdit) {
                MeterInputBox(
                    onDismissRequest = { showInEdit = false },
                    default = h.incomingMeter,
                    title = stringResource(id = R.string.bin_incoming_meter),
                ) {
                    vm.setIncoming(it)
                }
            }
        }
    }
}

@Composable
private fun MeterInputBox(
    onDismissRequest: () -> Unit,
    default: Int?,
    title: String?,
    ok: (Int?) -> Unit,
) {
    NumberInputBoxDialog(
        onDismissRequest = onDismissRequest,
        defaultText = default?.toString().orEmpty(),
        keyboardType = KeyboardType.Number,
        inputValidation = binKilometerInputValidation(),
        title = title,
    ) {
        ok(it.toIntOrNull())
        onDismissRequest()
    }
}
