package jp.co.toukei.log.trustar.compose.sheet

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.sebaslogen.resaca.rememberScoped
import com.sebaslogen.resaca.viewModelScoped
import jp.co.toukei.log.lib.compose.DefaultSheet
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.compose.IncidentalEdit
import jp.co.toukei.log.trustar.compose.IncidentalList
import jp.co.toukei.log.trustar.compose.IncidentalView
import jp.co.toukei.log.trustar.enum.IncidentalEnum
import jp.co.toukei.log.trustar.viewmodel.HomeVM
import jp.co.toukei.log.trustar.viewmodel.IncidentalListVM


@Composable
fun IncidentalListSheet(
    vm: HomeVM,
    allocationNo: String,
    allocationRowNo: Int,
    onDismiss: () -> Unit,
) {
    var display by rememberScoped {
        mutableStateOf<IncidentalEnum>(IncidentalEnum.List(allocationNo, allocationRowNo))
    }
    val v = viewModelScoped {
        IncidentalListVM(vm.user, allocationNo, allocationRowNo)
    }

    when (val a = display) {
        is IncidentalEnum.List -> {
            val list by v.list.subscribeAsState(null)
            list?.let { l ->
                DefaultSheet(onDismiss = onDismiss) {
                    IncidentalList(
                        modifier = Modifier,
                        list = l,
                        deleteItem = {
                            v.delete(it.sheet)
                            Current.syncIncidental()
                        },
                        clickButton = {
                            display = IncidentalEnum.Add(a.allocationNo, a.allocationRowNo)
                        }
                    ) { item ->
                        display = IncidentalEnum.View(item.sheet.uuid)
                    }
                }
            }
            LaunchedEffect(Unit) {
                Current.syncIncidental()
            }
        }

        is IncidentalEnum.Add -> {
            DefaultSheet(onDismiss = onDismiss) {
                IncidentalEdit(
                    vm = v,
                    defaultShipper = null,
                    defaultWorks = null,
                    defaultTimeRangeList = emptyList(),
                    buttonClick = { shipper, works, addedTimeRangeList, _, _ ->
                        val header = v.addIncidental(
                            allocationNo = allocationNo,
                            allocationRowNo = allocationRowNo,
                            shipper = shipper,
                            works = works,
                            items = addedTimeRangeList
                        )
                        display = IncidentalEnum.View(header.uuid)
                    }
                )
            }
        }

        is IncidentalEnum.Edit -> {
            remember {
                v.sheetDetailsByUUID(a.headerUUID)
            }.subscribeAsState(null).value?.let { detail ->
                val header = detail.header
                DefaultSheet(onDismiss = onDismiss) {
                    IncidentalEdit(
                        vm = v,
                        defaultShipper = detail.shipper2,
                        defaultWorks = detail.workList,
                        defaultTimeRangeList = detail.timeList,
                        buttonClick = { shipper, works, addedTimeRangeList, deletedTimeRangeList, editedTimeRangeList ->
                            v.editIncidental(
                                uuid = header.uuid,
                                shipper = shipper,
                                works = works,
                                addedTimeRangeList = addedTimeRangeList,
                                deletedTimeRangeList = deletedTimeRangeList,
                                editedTimeRangeList = editedTimeRangeList
                            )
                            display = IncidentalEnum.View(header.uuid)
                        }
                    )
                }
            }
        }

        is IncidentalEnum.View -> {
            remember {
                v.sheetDetailsByUUID(a.headerUUID)
            }.subscribeAsState(null).value?.let { detail ->
                val header = detail.header
                DefaultSheet(onDismiss = onDismiss) {
                    IncidentalView(
                        vm = v,
                        header = header,
                        defaultShipper = detail.shipper2,
                        defaultWorks = detail.workList,
                        defaultTimeRangeList = detail.timeList,
                        edit = {
                            display = IncidentalEnum.Edit(header.uuid)
                        },
                        onClick = {
                            display = IncidentalEnum.List(
                                allocationNo = header.allocationNo,
                                allocationRowNo = header.allocationRowNo
                            )
                        },
                    )
                }
            }
            LaunchedEffect(Unit) {
                Current.syncIncidental()
            }
        }
    }
}
