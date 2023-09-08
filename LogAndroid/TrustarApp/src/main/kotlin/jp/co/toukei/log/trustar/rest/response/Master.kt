package jp.co.toukei.log.trustar.rest.response

import annotation.Keep
import annotation.Para
import jp.co.toukei.log.trustar.db.user.entity.BinStatus
import jp.co.toukei.log.trustar.db.user.entity.CollectionGroup
import jp.co.toukei.log.trustar.db.user.entity.DelayReason
import jp.co.toukei.log.trustar.db.user.entity.Fuel
import jp.co.toukei.log.trustar.db.user.entity.Shipper
import jp.co.toukei.log.trustar.db.user.entity.Truck
import jp.co.toukei.log.trustar.db.user.entity.Work
import jp.co.toukei.log.trustar.db.user.entity.WorkPlace
import jp.co.toukei.log.trustar.db.user.entity.WorkStatus

class Master @Keep constructor(
    @Para("workStatuses") @JvmField val workStatuses: Array<WorkStatus>?,
    @Para("binStatuses") @JvmField val binStatuses: Array<BinStatus>?,
    @Para("works") works: Array<RawWork>?,
    @Para("fuels") @JvmField val fuels: Array<Fuel>?,
    @Para("trucks") @JvmField val trucks: Array<Truck>?,
    @Para("shippers") @JvmField val shippers: Array<Shipper>?,
    @Para("workPlaces") @JvmField val rawWorkPlace: Array<RawWorkPlace>?,
    @Para("delayReason") @JvmField val delayReason: Array<DelayReason>?,
    @Para("collections") @JvmField val collections: Array<CollectionGroup>?,
) {

    @JvmField
    val works = works?.map { it.work }

    class RawWork @Keep constructor(
        @Para("workCd") workCd: String,
        @Para("workNm") workNm: String,
        @Para("appDisplayFlag") displayFlag: Int?,
        @Para("displayOrder") displayOrder: Int,
    ) {
        @JvmField
        val work = Work(workCd, workNm, displayFlag != 0, displayOrder)
    }

    class RawWorkPlace @Keep constructor(
        @Para("placeCd") placeCd: String,
        @Para("placeNm") placeNm: String,
        @Para("placeImage1") placeImage1: String?,
        @Para("placeImage2") placeImage2: String?,
        @Para("placeImage3") placeImage3: String?,
    ) {

        @JvmField
        val workPlace = WorkPlace(
            placeCd,
            placeNm,
            listOfNotNull(placeImage1, placeImage2, placeImage3)
        )
    }
}
