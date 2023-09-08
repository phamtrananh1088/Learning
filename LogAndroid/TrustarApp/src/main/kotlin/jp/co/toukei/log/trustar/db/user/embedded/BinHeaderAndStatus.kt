package jp.co.toukei.log.trustar.db.user.embedded

import androidx.room.Embedded
import jp.co.toukei.log.lib.util.CompareItem
import jp.co.toukei.log.trustar.db.user.entity.BinHeader
import jp.co.toukei.log.trustar.db.user.entity.BinStatus
import jp.co.toukei.log.trustar.db.user.entity.Truck

class BinHeaderAndStatus(
        @Embedded @JvmField val header: BinHeader,
        @Embedded @JvmField val truck: Truck,
        @Embedded @JvmField val status: BinStatus
) : CompareItem<BinHeaderAndStatus> {

    override fun sameItem(other: BinHeaderAndStatus): Boolean {
        return other.header.sameItem(header) && other.status.sameItem(status) && other.truck.sameItem(truck)
    }
}
