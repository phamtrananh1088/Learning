package jp.co.toukei.log.trustar.other

import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.db.user.entity.BinDetail
import jp.co.toukei.log.trustar.db.user.entity.DelayReason
import jp.co.toukei.log.trustar.db.user.entity.Work

class BinDetailData(
    @JvmField val binDetail: BinDetail,
    @JvmField val delayReasons: Array<DelayReason>,
    @JvmField val incidentalHeaderCount: Int,
    @JvmField val signedIncidentalHeaderCount: Int,
    @JvmField val work: Work?,
) {
    val delayReason: DelayReason? = binDetail.delayReasonCd?.let { cd ->
        delayReasons.firstOrNull { it.reasonCd == cd }
    }

    @JvmField
    val incidentalEnabled = Current.user?.userInfo?.incidentalEnabled == true
}
