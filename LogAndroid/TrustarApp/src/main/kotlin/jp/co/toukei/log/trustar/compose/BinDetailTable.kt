@file:OptIn(ExperimentalMaterial3Api::class)

package jp.co.toukei.log.trustar.compose

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.co.toukei.log.lib.compose.SecCol
import jp.co.toukei.log.common.compose.VerticalDivider
import jp.co.toukei.log.lib.compose.rememberMaxWidth
import jp.co.toukei.log.common.enum.LaunchIntent
import jp.co.toukei.log.common.enum.open
import jp.co.toukei.log.trustar.Config
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.enum.BinStatusEnum
import jp.co.toukei.log.trustar.other.BinDetailExtraData
import java.text.DecimalFormat

@Composable
fun BinDetailTable(
    clickDelayReasonChange: (BinDetailExtraData) -> Unit,
    clickTemperature: (BinDetailExtraData) -> Unit,
    clickMemo: (BinDetailExtraData) -> Unit,
    clickIncidental: ((BinDetailExtraData) -> Unit)? = null,
    clickCollect: (BinDetailExtraData) -> Unit,
    clickDeliveryChart: (BinDetailExtraData) -> Unit,
    data: BinDetailExtraData,
) {
    val b = data.binDetail
    val p = b.place
    val p2 = b.placeExt
    BinDetailTable(
        delayReason = data.delayReason?.reasonText,
        serviceOrder = b.serviceOrder,
        operationOrder = b.operationOrder,
        placeNameFull = b.place.placeNameFull,
        displayPlanTime = b.displayPlanTime,
        displayWorkTime = b.displayWorkTime(),
        workNm = data.work?.workNm,
        zipCode = p2?.zip,
        placeAddress = p.addr,
        placeTel1 = p2?.tel1,
        placeMail1 = p2?.mail1,
        placeTel2 = p2?.tel2,
        placeMail2 = p2?.mail2,
        placeNote1 = p2?.note1,
        placeNote2 = p2?.note2,
        placeNote3 = p2?.note3,
        enableIncidentalClick = if (clickIncidental != null) { -> clickIncidental(data) } else null,
        incidentalHeaderCount = data.incidentalHeaderCount,
        signedIncidentalHeaderCount = data.signedIncidentalHeaderCount,
        hasCollect = data.hasCollect,
        temperature = b.temperature,
        experiencePlaceNote1 = b.experiencePlaceNote1,
        clickDelayReasonChange = if (b.isDelayed()) { -> clickDelayReasonChange(data) } else null,
        clickTemperature = { clickTemperature(data) },
        clickMemo = { clickMemo(data) },
        clickCollect = { clickCollect(data) },
        clickDeliveryChart = { clickDeliveryChart(data) },
    )
}

@Composable
fun BinDetailTable(
    delayReason: String?,
    serviceOrder: Int?,
    operationOrder: Int?,
    placeNameFull: String,
    displayPlanTime: String?,
    displayWorkTime: String?,
    workNm: String?,

    zipCode: String?,
    placeAddress: String?,
    placeTel1: String?,
    placeMail1: String?,
    placeTel2: String?,
    placeMail2: String?,
    placeNote1: String?,
    placeNote2: String?,
    placeNote3: String?,

    enableIncidentalClick: (() -> Unit)?,
    incidentalHeaderCount: Int,
    signedIncidentalHeaderCount: Int,
    hasCollect: Boolean,

    temperature: Double?,
    experiencePlaceNote1: String?,

    clickDelayReasonChange: (() -> Unit)?,
    clickTemperature: () -> Unit,
    clickMemo: () -> Unit,
    clickCollect: () -> Unit,
    clickDeliveryChart: () -> Unit,
    placeholder: String = "",
) {
    val firstColModifier = Modifier
        .padding(8.dp)
        .rememberMaxWidth(128.dp)
    val context = LocalContext.current
    val rowModifier = Modifier
    val primaryColor = MaterialTheme.colorScheme.primary

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
    ) {
        BinRow(
            firstColText = R.string.delivery_chart,
            firstColModifier = firstColModifier,
            modifier = rowModifier,
        ) {
            SecCol(
                text = "",
                onClick = clickDeliveryChart,
                showArrowColor = primaryColor,
            )
        }
        BinRow(
            firstColText = R.string.bin_untimely_delivered_reason,
            firstColModifier = firstColModifier,
            modifier = rowModifier,
        ) {
            SecCol(delayReason ?: placeholder)
            Text(
                text = stringResource(id = R.string.bin_untimely_delivered_reason_change),
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clip(CircleShape)
                    .run {
                        if (clickDelayReasonChange != null) {
                            clickable(onClick = clickDelayReasonChange)
                        } else {
                            alpha(0.4f)
                        }
                    }
                    .background(color = primaryColor)
                    .padding(vertical = 4.dp, horizontal = 16.dp),
            )
        }
        BinRow(
            firstColText = R.string.bin_service_order,
            firstColModifier = firstColModifier,
            modifier = rowModifier,
        ) {
            SecCol(text = serviceOrder?.takeIf { it > 0 }?.toString() ?: placeholder)
        }
        BinRow(
            firstColText = R.string.bin_operation_order,
            firstColModifier = firstColModifier,
            modifier = rowModifier,
        ) {
            SecCol(text = operationOrder?.takeIf { it > 0 }?.toString() ?: placeholder)
        }
        BinRow(
            firstColText = R.string.bin_place_name,
            firstColModifier = firstColModifier,
            modifier = rowModifier,
        ) {
            SecCol(text = placeNameFull)
        }
        BinRow(
            firstColText = R.string.bin_plan_time,
            firstColModifier = firstColModifier,
            modifier = rowModifier,
        ) {
            SecCol(text = displayPlanTime ?: placeholder)
        }
        BinRow(
            firstColText = R.string.bin_actual_time,
            firstColModifier = firstColModifier,
            modifier = rowModifier,
        ) {
            SecCol(text = displayWorkTime ?: placeholder)
        }
        BinRow(
            firstColText = R.string.bin_work_name,
            firstColModifier = firstColModifier,
            modifier = rowModifier,
        ) {
            SecCol(text = workNm ?: placeholder)
        }
        BinRow(
            firstColText = R.string.bin_zip_code,
            firstColModifier = firstColModifier,
            modifier = rowModifier,
        ) {
            SecCol(text = zipCode ?: placeholder)
        }
        BinRow(
            firstColText = R.string.bin_place_address,
            firstColModifier = firstColModifier,
            modifier = rowModifier,
        ) {
            SecCol(
                text = placeAddress ?: placeholder,
                onClick = { LaunchIntent.Map.open(context, placeAddress) },
                textColor = primaryColor,
            )
        }
        BinRow(
            firstColText = R.string.bin_place_tel1,
            firstColModifier = firstColModifier,
            modifier = rowModifier,
        ) {
            SecCol(
                text = placeTel1 ?: placeholder,
                onClick = { LaunchIntent.Dial.open(context, placeTel1) },
                textColor = primaryColor,
            )
        }
        BinRow(
            firstColText = R.string.bin_place_mail1,
            firstColModifier = firstColModifier,
            modifier = rowModifier,
        ) {
            SecCol(
                text = placeMail1 ?: placeholder,
                onClick = { LaunchIntent.Email.open(context, placeMail1) },
                textColor = primaryColor,
            )
        }
        BinRow(
            firstColText = R.string.bin_place_tel2,
            firstColModifier = firstColModifier,
            modifier = rowModifier,
        ) {
            SecCol(
                text = placeTel2 ?: placeholder,
                onClick = { LaunchIntent.Dial.open(context, placeTel2) },
                textColor = primaryColor,
            )
        }
        BinRow(
            firstColText = R.string.bin_place_mail2,
            firstColModifier = firstColModifier,
            modifier = rowModifier,
        ) {
            SecCol(
                text = placeMail2 ?: placeholder,
                onClick = { LaunchIntent.Email.open(context, placeMail2) },
                textColor = primaryColor,
            )
        }
        BinRow(
            firstColText = R.string.bin_place_note1,
            firstColModifier = firstColModifier,
            modifier = rowModifier,
        ) {
            SecCol(text = placeNote1 ?: placeholder)
        }
        BinRow(
            firstColText = R.string.bin_place_note2,
            firstColModifier = firstColModifier,
            modifier = rowModifier,
        ) {
            SecCol(text = placeNote2 ?: placeholder)
        }
        BinRow(
            firstColText = R.string.bin_place_note3,
            firstColModifier = firstColModifier,
            modifier = rowModifier,
        ) {
            SecCol(text = placeNote3 ?: placeholder)
        }

        if (enableIncidentalClick != null) {
            BinRow(
                firstColText = R.string.bin_incidental,
                firstColModifier = firstColModifier,
                modifier = rowModifier,
            ) {
                SecCol(
                    text = if (incidentalHeaderCount > 0) stringResource(id = R.string.bin_registered) else placeholder,
                    onClick = enableIncidentalClick,
                    showArrowColor = primaryColor,
                )
            }
            BinRow(
                firstColText = R.string.bin_signature,
                firstColModifier = firstColModifier,
                modifier = rowModifier,
            ) {
                SecCol(
                    text = if (signedIncidentalHeaderCount > 0) stringResource(id = R.string.bin_registered) else placeholder,
                )
            }
        }
        BinRow(
            firstColText = R.string.bin_temperature,
            firstColModifier = firstColModifier,
            modifier = rowModifier,
        ) {
            SecCol(
                text = temperature?.let { "$it ℃" } ?: placeholder,
                onClick = clickTemperature,
                showArrowColor = primaryColor,
            )
        }
        BinRow(
            firstColText = R.string.bin_memo,
            firstColModifier = firstColModifier,
            modifier = rowModifier,
        ) {
            SecCol(
                text = experiencePlaceNote1 ?: placeholder,
                onClick = clickMemo,
                showArrowColor = primaryColor,
            )
        }
        BinRow(
            firstColText = R.string.collections_edit,
            firstColModifier = firstColModifier,
            modifier = rowModifier,
        ) {
            SecCol(
                text = if (hasCollect) stringResource(id = R.string.bin_registered) else placeholder,
                onClick = clickCollect,
                showArrowColor = primaryColor,
            )
        }
    }
}

@Composable
private inline fun ColumnScope.BinRow(
    modifier: Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    firstColModifier: Modifier,
    @StringRes firstColText: Int,
    content: @Composable RowScope.() -> Unit,
) {
    Row(modifier, horizontalArrangement, Alignment.CenterVertically) {
        FirstCol(firstColText, firstColModifier)
        content()
    }
    VerticalDivider()
}

@Composable
private fun FirstCol(
    @StringRes stringId: Int,
    modifier: Modifier,
) {
    Text(
        text = stringResource(id = stringId),
        modifier = modifier,
        fontSize = 12.sp,
    )
}


@Composable
fun BinHeaderTable(
    truckNm: String?,
    startScheduled: Long?,
    endScheduled: Long?,
    start: Long?,
    end: Long?,
    crew: String?,
    crew2: String?,
    note: String?,
    outgoing: Int?,
    incoming: Int?,
    binStatusEnum: BinStatusEnum,
    clickOutMeter: () -> Unit,
    clickInMeter: () -> Unit,
    placeholder: String = "",
) {
    val firstColModifier = Modifier
        .padding(8.dp)
        .rememberMaxWidth(128.dp)

    val rowModifier = Modifier

    val formatter = remember { DecimalFormat("#,###,###") }
    val df = Config.dateFormatter3

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
    ) {
        BinRow(
            firstColText = R.string.bin_vehicle,
            firstColModifier = firstColModifier,
            modifier = rowModifier,
        ) {
            SecCol(text = truckNm ?: placeholder)
        }
        BinRow(
            firstColText = R.string.bin_start_scheduled_time,
            firstColModifier = firstColModifier,
            modifier = rowModifier,
        ) {
            SecCol(text = startScheduled?.let(df::format) ?: placeholder)
        }
        BinRow(
            firstColText = R.string.bin_end_scheduled_time,
            firstColModifier = firstColModifier,
            modifier = rowModifier,
        ) {
            SecCol(text = endScheduled?.let(df::format) ?: placeholder)
        }
        BinRow(
            firstColText = R.string.bin_start_time,
            firstColModifier = firstColModifier,
            modifier = rowModifier,
        ) {
            SecCol(text = start?.let(df::format) ?: placeholder)
        }
        BinRow(
            firstColText = R.string.bin_end_time,
            firstColModifier = firstColModifier,
            modifier = rowModifier,
        ) {
            SecCol(text = end?.let(df::format) ?: placeholder)
        }
        BinRow(
            firstColText = R.string.bin_crew,
            firstColModifier = firstColModifier,
            modifier = rowModifier,
        ) {
            SecCol(text = crew ?: placeholder)
        }
        BinRow(
            firstColText = R.string.bin_crew2,
            firstColModifier = firstColModifier,
            modifier = rowModifier,
        ) {
            SecCol(text = crew2 ?: placeholder)
        }
        BinRow(
            firstColText = R.string.bin_note_1,
            firstColModifier = firstColModifier,
            modifier = rowModifier,
        ) {
            SecCol(text = note ?: placeholder)
        }
        if (binStatusEnum.started()) {
            BinRow(
                firstColText = R.string.bin_outgoing_meter,
                firstColModifier = firstColModifier,
                modifier = rowModifier,
            ) {
                SecCol(
                    text = outgoing?.let { "${formatter.format(it)} km" } ?: placeholder,
                    onClick = clickOutMeter,
                )
            }
        }
        if (binStatusEnum.isFinished()) {
            BinRow(
                firstColText = R.string.bin_incoming_meter,
                firstColModifier = firstColModifier,
                modifier = rowModifier,
            ) {
                SecCol(
                    text = incoming?.let { "${formatter.format(it)} km" } ?: placeholder,
                    onClick = clickInMeter,
                )
            }
        }
    }
}
