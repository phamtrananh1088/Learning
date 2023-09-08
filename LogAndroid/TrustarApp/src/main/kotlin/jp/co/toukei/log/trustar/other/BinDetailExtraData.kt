package jp.co.toukei.log.trustar.other

import jp.co.toukei.log.trustar.db.user.entity.BinDetail
import jp.co.toukei.log.trustar.db.user.entity.CollectionResult
import jp.co.toukei.log.trustar.db.user.entity.DelayReason
import jp.co.toukei.log.trustar.db.user.entity.Work

class BinDetailExtraData(
    @JvmField val binDetail: BinDetail,
    @JvmField val delayReasons: List<DelayReason>,
    @JvmField val incidentalHeaderCount: Int,
    @JvmField val signedIncidentalHeaderCount: Int,
    @JvmField val work: Work?,
    @JvmField val binCollect: CollectionResult?,
) {
    val delayReason: DelayReason? = binDetail.delayReasonCd?.let { cd ->
        delayReasons.firstOrNull { it.reasonCd == cd }
    }

    @JvmField
    val hasCollect = binCollect?.rows()?.any { it.actualQuantity > 0 } ?: false
}
