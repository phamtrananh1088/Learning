package jp.co.toukei.log.trustar.db.user.embedded

import androidx.room.Embedded
import jp.co.toukei.log.trustar.compose.ComposeData
import jp.co.toukei.log.trustar.compose.ComposeData.BinHeaderRow
import jp.co.toukei.log.trustar.db.user.entity.BinHeader
import jp.co.toukei.log.trustar.db.user.entity.BinStatus
import jp.co.toukei.log.trustar.db.user.entity.Truck

class BinHeaderAndStatus(
    @Embedded @JvmField val header: BinHeader,
    @Embedded @JvmField val truck: Truck,
    @Embedded @JvmField val status: BinStatus,
) {

    fun toRow(): BinHeaderRow {
        return BinHeaderRow(
            allocationNo = header.allocationNo,
            allocationNm = header.allocationNm,
            binStatus = header.binStatus,
            statusNm = status.binStatusNm,
            statusBgColor = status.bgColor,
            statusTextColor = status.textColor,
            truckCd = truck.truckCd,
            truckNm = truck.truckNm,
        )
    }

    fun todo() = ComposeData.Bin(
        header.allocationNo,
        header.allocationNm,
        truck.truckNm,
        header.binStatus,
        header.incomingMeter,
    )
}
