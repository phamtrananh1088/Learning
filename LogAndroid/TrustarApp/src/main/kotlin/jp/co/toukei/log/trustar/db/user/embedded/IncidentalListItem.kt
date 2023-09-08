package jp.co.toukei.log.trustar.db.user.embedded

import androidx.room.Embedded
import androidx.room.Ignore
import jp.co.toukei.log.trustar.db.user.entity.IncidentalHeader
import jp.co.toukei.log.trustar.db.user.entity.Shipper

class IncidentalListItem(
    @Embedded @JvmField val shipper: Shipper?,
    @Embedded @JvmField val sheet: IncidentalHeader,
) {
    @Ignore
    @JvmField
    val shipperNm: String? = shipper?.shipperNm

    @Ignore
    @JvmField
    val signStatus: Int = sheet.status
}
