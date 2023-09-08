package jp.co.toukei.log.trustar.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.sebaslogen.resaca.rememberScoped
import jp.co.toukei.log.common.compose.AppPropTodo
import jp.co.toukei.log.lib.compose.DefaultConfirmDialog
import jp.co.toukei.log.lib.compose.rememberDistinctBy
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.db.user.entity.BinDetail
import jp.co.toukei.log.trustar.feature.home.data.BinDetailForWork
import jp.co.toukei.log.trustar.other.WorkBin
import jp.co.toukei.log.trustar.viewmodel.HomeVM

data class ClickWork(
    val work: ComposeData.Work,
    val workBin: WorkBin,
)

@Composable
fun HomeOperate(
    vm: HomeVM,
    modifier: Modifier,

    bin: WorkBin,
    onClickInfo: (ComposeData.BinRow) -> Unit,
    onClickIncidental: (ComposeData.BinRow) -> Unit,
) {
    var workDialog by rememberScoped {
        mutableStateOf<ClickWork?>(null)
    }
    workDialog?.let { e ->
        val onDismissRequest = {
            workDialog = null
        }
        vm.WhenBinStatusChanged(
            allocationNo = e.workBin.allocationNo,
            action = onDismissRequest
        )
        when (val b = e.workBin) {
            is WorkBin.Bin -> {
                if (b.binDetail.workStatus.isWorking()) {
                    LaunchedEffect(Unit) {
                        vm.endWork(b.allocationNo, b.binDetail.allocationRowNo)
                        onDismissRequest()
                    }
                } else {
                    vm.WhenWorkStatusChanged(
                        allocationNo = b.allocationNo,
                        allocationRow = b.binDetail.allocationRowNo,
                        action = onDismissRequest,
                    )
                    var s by rememberScoped {
                        mutableStateOf(false)
                    }
                    val start = {
                        onDismissRequest()
                        vm.startWork(b.allocationNo, b.binDetail.allocationRowNo, e.work)
                    }
                    if (s) {
                        StartWorkDialog2(
                            onDismissRequest = onDismissRequest,
                            onClick = start,
                        )
                    } else {
                        StartWorkDialog(
                            onDismissRequest = onDismissRequest,
                            work = e.work.workNm
                        ) {
                            val location = Current.lastLocation
                            if (location != null &&
                                BinDetail.checkIfMisdelivered(b.binDetail, location)
                            ) {
                                s = true
                            } else {
                                start()
                            }
                        }
                    }
                }
            }

            is WorkBin.New -> {
                StartWorkDialog(
                    onDismissRequest = onDismissRequest,
                    work = e.work.workNm
                ) {
                    onDismissRequest()
                    vm.startNewAddWork(b.allocationNo, e.work, b.place)
                }
            }
        }
    }

    val works by vm.workList.subscribeAsState(emptyList())

    val binDetail: BinDetail?
    val place: ComposeData.Place
    val isWorking: Boolean

    when (bin) {
        is WorkBin.New -> {
            binDetail = null
            place = bin.place.todoCompose()
            isWorking = false
        }

        is WorkBin.Bin -> {
            binDetail = bin.binDetail
            place = binDetail.place.todoCompose()
            isWorking = binDetail.workStatus.isWorking()
        }
    }
    val d = binDetail?.let { BinDetailForWork(LocalContext.current, it) }
    val dd = binDetail?.let {
        ComposeData.BinRow(it.allocationNo, it.allocationRowNo)
    }

    HomeOperate(
        modifier = modifier,
        workButtonList = works.map {
            ComposeData.Work(it.workCd, it.workNm, it.displayOrder)
        },
        currentWorkingCd = binDetail?.run {
            if (workStatus.isWorkingOrMoving()) work?.workCd else null
        },
        isWorking = isWorking,
        place = place,
        displayPlanTime = binDetail?.displayPlanTime,
        hasNotice = binDetail?.hasNotice ?: false,
        workStartTime = d?.workStartTime,
        delayRankColor = d?.delayRankColor,
        workStateText = d?.workStateText,
        onClickInfo = dd?.let { { onClickInfo(it) } },
        onClickIncidental = if (vm.userInfo.incidentalEnabled) { -> dd?.let(onClickIncidental) } else null,
        onClickWorkButton = { w ->
            workDialog = ClickWork(w, bin)
        },
    )
}

@Composable
private fun StartWorkDialog(
    onDismissRequest: () -> Unit,
    work: String,
    onClick: () -> Unit,
) {
    DefaultConfirmDialog(
        onDismissRequest = onDismissRequest,
        content = stringResource(id = R.string.work_start_alert_msg),
        title = stringResource(id = R.string.work_start_by_s1_alert_title, work),
        confirmButtonText = stringResource(id = R.string.start),
        dismissButtonText = stringResource(id = R.string.cancel),
        confirmButtonClick = onClick,
    )
}

@Composable
private fun StartWorkDialog2(
    onDismissRequest: () -> Unit,
    onClick: () -> Unit,
) {
    DefaultConfirmDialog(
        onDismissRequest = onDismissRequest,
        content = R.string.work_misdelivered_alert_msg,
        title = R.string.work_misdelivered_alert_title,
        confirmButtonText = R.string.start,
        dismissButtonText = R.string.cancel,
        confirmButtonClick = onClick,
    )
}

@Composable
private fun HomeOperate(
    modifier: Modifier = Modifier,
    workButtonList: List<ComposeData.Work>,
    currentWorkingCd: String?,
    isWorking: Boolean,
    place: ComposeData.Place?,
    displayPlanTime: String?,
    hasNotice: Boolean,

    workStartTime: Long?,
    delayRankColor: Int?,
    workStateText: String?,

    onClickInfo: (() -> Unit)?,
    onClickIncidental: (() -> Unit)?,
    onClickWorkButton: (ComposeData.Work) -> Unit,
) {
    val (sortedWorks, selectedWork) = run { // fixme `remember` not works on currentWorkCd ???
        val m = workButtonList.groupBy { it.workCd == currentWorkingCd }
        Pair(m[false]?.sortedBy { it.displayOrder } ?: emptyList(), m[true]?.firstOrNull())
    }
    val primaryColor = MaterialTheme.colorScheme.primary
    val workingColor = AppPropTodo.Color.working

    val iconColor = when {
        hasNotice -> AppPropTodo.Color.binOpNotice
        else -> primaryColor
    }

    val buttonColor = ButtonDefaults.buttonColors(
        containerColor = if (isWorking) workingColor else primaryColor,
        contentColor = AppPropTodo.Color.workButton,
        disabledContainerColor = AppPropTodo.Color.buttonDisabled,
        disabledContentColor = AppPropTodo.Color.workButton,
    )
    val buttonModifier = Modifier
        .padding(8.dp)
    val buttonTextModifier = Modifier
        .padding(vertical = 12.dp)

    val matchedButtonTextModifier = Modifier
        .padding(vertical = 32.dp)


    if (workButtonList.isNotEmpty()) {
        val normalWorkList = sortedWorks.rememberDistinctBy(ComposeData.Work.key)
        LazyVerticalGrid(
            modifier = modifier.fillMaxWidth(),
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp)
        ) {
            item(
                span = { GridItemSpan(maxLineSpan) }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                ) {
                    if (isWorking) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            val workStatus = selectedWork?.run {
                                stringResource(id = R.string.work_status_s1_ing, workNm)
                            }.orEmpty()

                            Text(
                                modifier = Modifier.weight(1f, fill = false),
                                text = workStatus,
                                fontWeight = FontWeight.Bold,
                                fontSize = 40.sp,
                                lineHeight = 42.sp,
                                color = workingColor,
                            )
                            CircleTimer(
                                workStartTime = workStartTime,
                                delayRankColor = delayRankColor,
                                workStateText = workStateText,
                            )
                        }
                    } else {
                        Text(
                            text = place?.nm1.orEmpty(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            lineHeight = 24.sp,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = place?.nm2.orEmpty(),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                lineHeight = 24.sp,
                                modifier = Modifier
                                    .weight(1F)
                                    .padding(vertical = 4.dp)
                            )
                            if (onClickInfo != null) {
                                BinInfoIconButton(onClickInfo, tint = iconColor)
                            }
                        }
                        Text(
                            text = place?.address.orEmpty(),
                            fontSize = 12.sp,
                            lineHeight = 18.sp,
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f, fill = false)
                                .padding(end = 4.dp, bottom = 4.dp),
                            horizontalAlignment = Alignment.Start,
                        ) {
                            if (displayPlanTime != null) {
                                Text(
                                    text = displayPlanTime,
                                    fontSize = 16.sp,
                                    lineHeight = 24.sp,
                                )
                                Text(
                                    text = stringResource(R.string.work_notice_of_time),
                                    fontSize = 12.sp,
                                    lineHeight = 18.sp,
                                    color = workingColor,
                                )
                            }
                        }
                        Column(
                            modifier = Modifier,
                            horizontalAlignment = Alignment.End,
                        ) {
                            if (isWorking && onClickInfo != null) {
                                BinInfoIconButton(onClickInfo, tint = iconColor)
                            }
                            if (onClickIncidental != null) {
                                Button(
                                    onClick = onClickIncidental
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.incidental_sheets),
                                        modifier = Modifier,
                                    )
                                }
                            }
                        }
                    }
                }
            }
            if (selectedWork != null) {
                item(
                    span = { GridItemSpan(maxLineSpan) }
                ) {
                    Button(
                        modifier = buttonModifier,
                        colors = buttonColor,
                        shape = AppPropTodo.Shape.workButtonShape,
                        onClick = { onClickWorkButton(selectedWork) },
                    ) {
                        Text(
                            modifier = matchedButtonTextModifier,
                            text = selectedWork.workNm.let {
                                if (isWorking) {
                                    stringResource(id = R.string.work_status_s1_end, it)
                                } else it
                            },
                            fontSize = 24.sp,
                            lineHeight = 32.sp,
                        )
                    }
                }
            }
            items(
                items = normalWorkList,
                key = ComposeData.Work.key,
            ) {
                Button(
                    modifier = buttonModifier,
                    enabled = !isWorking,
                    colors = buttonColor,
                    shape = AppPropTodo.Shape.workButtonShape,
                    onClick = { onClickWorkButton(it) },
                ) {
                    Text(
                        modifier = buttonTextModifier,
                        text = it.workNm,
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                    )
                }
            }
        }
    }
}

@Composable
private fun CircleTimer(
    workStartTime: Long?,
    delayRankColor: Int?,
    workStateText: String?,
) {
    //todo compose
    val w = LocalDensity.current.run { 2.dp.toPx() }
    AndroidView(
        modifier = Modifier
            .width(120.dp)
            .height(120.dp)
            .padding(vertical = 4.dp),
        factory = {
            jp.co.toukei.log.lib.ext.CircleTimer(it).apply {
                val tw = w * 16
                setCenterTextSize(tw)
                setUpdateRate(2000)
                setRingWidth(w, w)
                setAlphaAndSecondColor(0x60, 0xff000000.toInt())
            }
        },
    ) {
        it.setCurrentTimeMillis(3600_000, workStartTime)
        it.setPaintColor(delayRankColor ?: 0xff92d050.toInt())
        it.setBelowText(workStateText)
    }
}
