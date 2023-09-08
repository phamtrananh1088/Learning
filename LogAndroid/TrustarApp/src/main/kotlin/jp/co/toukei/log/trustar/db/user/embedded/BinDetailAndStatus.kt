package jp.co.toukei.log.trustar.db.user.embedded

import androidx.room.Embedded
import jp.co.toukei.log.lib.util.CompareItem
import jp.co.toukei.log.trustar.db.user.entity.BinDetail
import jp.co.toukei.log.trustar.db.user.entity.WorkStatus

class BinDetailAndStatus(
        @Embedded @JvmField val detail: BinDetail,
        @Embedded @JvmField val status: WorkStatus
) : CompareItem<BinDetailAndStatus> {

    override fun sameItem(other: BinDetailAndStatus): Boolean {
        return other.detail.sameItem(detail) && other.status.sameItem(status)
    }
}
