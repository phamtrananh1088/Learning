package jp.co.toukei.log.trustar.other

import jp.co.toukei.log.trustar.db.user.entity.BinDetail
import jp.co.toukei.log.trustar.db.user.entity.bin.Place

//todo rename
sealed class WorkBin(
    @JvmField val allocationNo: String,
) {

    class New(allocationNo: String, placeNm1: String) : WorkBin(allocationNo) {
        @JvmField
        val place = Place(placeNm1)
    }

    class Bin(@JvmField val binDetail: BinDetail) : WorkBin(binDetail.allocationNo)
}
