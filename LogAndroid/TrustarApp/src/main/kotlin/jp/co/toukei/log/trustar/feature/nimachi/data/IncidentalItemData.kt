package jp.co.toukei.log.trustar.feature.nimachi.data

import jp.co.toukei.log.trustar.db.user.embedded.IncidentalListItem
import jp.co.toukei.log.trustar.db.user.entity.IncidentalTime
import jp.co.toukei.log.trustar.db.user.entity.IncidentalWork
import jp.co.toukei.log.trustar.db.user.entity.Shipper

open class IncidentalItemData(
    @JvmField val shipper: Shipper?,
    @JvmField val works: List<IncidentalWork?>?,
    open val times: Iterable<TimeItem>?,
    @JvmField val editTarget: EditTarget?
) {
    @JvmField
    val shipperNm: CharSequence? = shipper?.shipperNm

    @JvmField
    val joinedWorkName: CharSequence? = works?.joinToString("／") { it?.workNm ?: "???" }
}

class IncidentalItemDataDB(
    @JvmField val item: IncidentalListItem,
    works: List<IncidentalWork?>?,
    times: Iterable<IncidentalTime>
) : IncidentalItemData(
    item.shipper,
    works,
    times.map(::TimeItemDB)
        .sortedWith(compareBy(
            { it.begin?.date },
            { it.end?.date }
        )),
    EditTarget.Sheet(item.sheet.uuid)
)
