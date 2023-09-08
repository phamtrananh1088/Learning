package jp.co.toukei.log.trustar.other

import jp.co.toukei.log.trustar.compose.ComposeData
import jp.co.toukei.log.trustar.db.user.entity.IncidentalHeader
import jp.co.toukei.log.trustar.db.user.entity.IncidentalTime
import jp.co.toukei.log.trustar.db.user.entity.IncidentalWork
import jp.co.toukei.log.trustar.db.user.entity.Shipper
import jp.co.toukei.log.trustar.db.user.entity.TimeRange

class IncidentalHeaderDetail(
    shipper: Shipper?,
    @JvmField val header: IncidentalHeader,
    workList: List<IncidentalWork>?,
    timeList: List<IncidentalTime>,
) {
    @JvmField
    val timeList = timeList.map { e ->
        TimeRange(
            delegate = e,
            id = e.uuid,
            type = e.type,
            start = e.beginDatetime,
            end = e.endDatetime,
            origState = null,
            newState = null,
        )
    }

    @JvmField
    val shipper2 = shipper?.let {
        ComposeData.Shipper(it.shipperCd, it.shipperNm)
    }

    @JvmField
    val workList = workList?.map {
        ComposeData.IncidentalWork(it.workCd, it.workNm.orEmpty())
    }
}
