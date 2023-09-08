@file:OptIn(ExperimentalFoundationApi::class)

package jp.co.toukei.log.trustar.compose.sheet

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import jp.co.toukei.log.lib.compose.DefaultSheet
import jp.co.toukei.log.lib.queryStr
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.compose.ChooseFromList
import jp.co.toukei.log.trustar.compose.ComposeData


@Composable
fun VehicleChoose(
    currentText: String,
    list: List<ComposeData.TruckKun>,
    select: (ComposeData.TruckKun) -> Unit,
    onDismiss: () -> Unit,
) {
    DefaultSheet(
        swipeDismiss = false,
        onDismiss = onDismiss
    ) { dismissRequest ->
        ChooseFromList(
            currentTextLabel = stringResource(R.string.current_vehicle),
            currentText = currentText,
            placeholder = stringResource(id = R.string.vehicle_search_hint),
            listTitle = stringResource(R.string.choose_target_vehicle),
            list = list,
            key = ComposeData.TruckKun.key,
            firstStr = ComposeData.TruckKun::truckCd,
            secondStr = ComposeData.TruckKun::truckNm,
            filter = { t, w ->
                val s = w.queryStr()
                t.truckCd.queryStr().contains(s) || t.truckNm.queryStr().contains(s)
            },
            select = {
                select(it)
                dismissRequest()
            },
        )
    }
}


@Composable
fun ShipperChoose(
    currentText: String,
    list: List<ComposeData.Shipper>,
    select: (ComposeData.Shipper) -> Unit,
    onDismiss: () -> Unit,
) {
    DefaultSheet(
        swipeDismiss = false,
        onDismiss = onDismiss
    ) { dismissRequest ->
        ChooseFromList(
            currentTextLabel = stringResource(R.string.current_shipper),
            currentText = currentText,
            placeholder = stringResource(id = R.string.shipper_search_hint),
            listTitle = stringResource(R.string.choose_target_shipper),
            list = list,
            key = ComposeData.Shipper.key,
            firstStr = ComposeData.Shipper::shipperCd,
            secondStr = ComposeData.Shipper::shipperNm,
            filter = { t, w ->
                val s = w.queryStr()
                t.shipperCd.queryStr().contains(s) || t.shipperNm.queryStr().contains(s)
            },
            select = {
                select(it)
                dismissRequest()
            },
        )
    }
}
