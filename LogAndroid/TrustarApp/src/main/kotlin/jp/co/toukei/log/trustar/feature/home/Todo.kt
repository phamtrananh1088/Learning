package jp.co.toukei.log.trustar.feature.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.sebaslogen.resaca.rememberScoped
import com.sebaslogen.resaca.viewModelScoped
import jp.co.toukei.log.lib.compose.DefaultSheet
import jp.co.toukei.log.trustar.compose.CollectMain
import jp.co.toukei.log.trustar.compose.ComposeData
import jp.co.toukei.log.trustar.compose.DeliveryChart
import jp.co.toukei.log.trustar.compose.HomeDashboard
import jp.co.toukei.log.trustar.compose.HomeDetail
import jp.co.toukei.log.trustar.compose.sheet.BinDetailInfo
import jp.co.toukei.log.trustar.compose.sheet.BinHeaderInfo
import jp.co.toukei.log.trustar.compose.sheet.IncidentalListSheet
import jp.co.toukei.log.trustar.compose.sheet.StartBinSheet
import jp.co.toukei.log.trustar.compose.sheet.StartUnscheduledBinSheet
import jp.co.toukei.log.trustar.other.BinDetailExtraData
import jp.co.toukei.log.trustar.other.WorkBin
import jp.co.toukei.log.trustar.viewmodel.BinCollectVM
import jp.co.toukei.log.trustar.viewmodel.BinDetailExtraVM
import jp.co.toukei.log.trustar.viewmodel.BinHeaderVM
import jp.co.toukei.log.trustar.viewmodel.DeliveryChartVM
import jp.co.toukei.log.trustar.viewmodel.HomeVM
import jp.co.toukei.log.trustar.viewmodel.StartBinVM
import jp.co.toukei.log.trustar.viewmodel.StartUnscheduledVM


@Composable
fun HomeDashboard(
    vm: HomeVM,
    paddingValues: PaddingValues,
    snbHost: SnackbarHostState,
    bellImportantClick: () -> Unit,
    bellNormalClick: () -> Unit,
    navigationBinHeader: (String) -> Unit,
) {
    val d = rememberScoped {
        mutableStateOf<String?>(null)
    }

    var unscheduled by rememberScoped {
        mutableStateOf(false)
    }

    HomeDashboard(
        vm,
        snbHost,
        Modifier
            .padding(paddingValues)
            .fillMaxSize(),
        bellImportantClick = bellImportantClick,
        bellNormalClick = bellNormalClick,
        addClick = {
            unscheduled = true
        },
        rowClick = { bin -> navigationBinHeader(bin.allocationNo) },
        detailClick = { bin ->
            d.value = bin.allocationNo
        },
    )
    d.value?.let {
        BinHeaderInfo(
            vm = viewModelScoped {
                BinHeaderVM(vm.user, it)
            },
            dismiss = {
                d.value = null
            },
        )
    }
    if (unscheduled) {
        val dismiss = {
            unscheduled = false
        }
        StartUnscheduledBinSheet(
            hvm = vm,
            vm = viewModelScoped {
                StartUnscheduledVM(vm.user)
            },
            onDismissRequest = dismiss,
            onSuccess = {
                if (it != null) {
                    navigationBinHeader(it)
                }
            },
        )
    }
}

@Composable
fun HomeDetail(
    vm: HomeVM,
    paddingValues: PaddingValues,
    snbHost: SnackbarHostState,
    bin: ComposeData.Bin,
) {
    val d = rememberScoped {
        mutableStateOf<ComposeData.BinRow?>(null)
    }
    var start by rememberScoped {
        mutableStateOf(false)
    }
    HomeDetail(
        vm = vm,
        snbHost = snbHost,
        modifier = Modifier.padding(paddingValues),
        bin = bin,
        list = remember(vm, vm.detailNav::binDetailListFlowable).subscribeAsState(emptyList()),
        detailClick = { d.value = it },
        modeSwitchChange = {
            val task = vm.user.binLocationTask
            if (it) {
                task.setInAutoMode(bin.allocationNo)
            } else {
                task.cancelAutoMode()
            }
        },
        addWork = { vm.navigationWorkAddNew(it.allocationNo) },
        startBinClick = { start = true },
        selectBinDetail = vm::navigationBinDetail,
        startInAutoMode = vm::startInAutoMode,
    )
    d.value?.let {
        BinDetailInfo(vm, it) {
            d.value = null
        }
    }
    if (start) {
        val dismiss = {
            start = false
        }
        StartBinSheet(
            hvm = vm,
            vm = viewModelScoped {
                StartBinVM(vm.user, bin.allocationNo)
            },
            onDismissRequest = dismiss
        )
    }
}

@Composable
fun HomeOperate(
    vm: HomeVM,
    paddingValues: PaddingValues,
    bin: WorkBin,
) {
    val d = rememberScoped {
        mutableStateOf<ComposeData.BinRow?>(null)
    }
    val incidental = rememberScoped {
        mutableStateOf<ComposeData.BinRow?>(null)
    }
    jp.co.toukei.log.trustar.compose.HomeOperate(
        vm = vm,
        modifier = Modifier.padding(paddingValues),
        bin = bin,
        onClickInfo = {
            d.value = it
        },
        onClickIncidental = {
            incidental.value = it
        },
    )
    d.value?.let {
        BinDetailInfo(vm, it) {
            d.value = null
        }
    }
    incidental.value?.let { b ->
        IncidentalListSheet(vm, b.allocationNo, b.allocationRowNo) {
            incidental.value = null
        }
    }
}

@Composable
fun BinDetailInfo(
    vm: HomeVM,
    bin: ComposeData.BinRow,
    dismiss: () -> Unit,
) {
    var showCollect by rememberScoped {
        mutableStateOf<BinDetailExtraData?>(null)
    }
    val incidental = rememberScoped {
        mutableStateOf<ComposeData.BinRow?>(null)
    }
    var chart by rememberScoped {
        mutableStateOf<ComposeData.BinRow?>(null)
    }

    BinDetailInfo(
        vm = viewModelScoped {
            BinDetailExtraVM(vm.user, bin)
        },
        clickIncidental = if (vm.userInfo.incidentalEnabled) { it ->
            incidental.value = bin
        } else null,
        clickCollect = {
            showCollect = it
        },
        clickDeliveryChart = {
            chart = bin
        },
        dismiss = dismiss,
    )

    showCollect?.let {
        val onDismissRequest = { showCollect = null }
        val d = it.binDetail
        val place = d.place.nm1.orEmpty()
        val vms = viewModelScoped {
            BinCollectVM(
                vm.user,
                d.allocationNo,
                d.allocationRowNo,
            )
        }
        DefaultSheet(onDismiss = onDismissRequest) { dismissRequest ->
            CollectMain(
                modifier = Modifier
                    .fillMaxSize(),
                place = place,
                vm = vms,
                onDismiss = dismissRequest,
            )
        }
    }
    incidental.value?.let { b ->
        IncidentalListSheet(vm, b.allocationNo, b.allocationRowNo) {
            incidental.value = null
        }
    }
    chart?.let { c ->
        val onDismissRequest = { chart = null }
        DefaultSheet(onDismiss = onDismissRequest) { dismissRequest ->
            DeliveryChart(
                vm = viewModelScoped {
                    DeliveryChartVM(c, vm.user)
                },
                naviBack = dismissRequest
            )
        }
    }
}
