package jp.co.toukei.log.trustar.db.user.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import annotation.Keep
import annotation.Para
import jp.co.toukei.log.lib.util.CompareItem

/**
 * 荷主
 */
@Entity(tableName = "shipper", primaryKeys = ["shipper_cd"])
class Shipper @Keep constructor(
        @Para("shipperCd") @ColumnInfo(name = "shipper_cd") @JvmField val shipperCd: String,
        @Para("shipperNm1") @ColumnInfo(name = "shipper_nm") @JvmField val shipperNm: String
) : CompareItem<Shipper> {

    override fun sameItem(other: Shipper): Boolean {
        return shipperCd == other.shipperCd
    }
}
