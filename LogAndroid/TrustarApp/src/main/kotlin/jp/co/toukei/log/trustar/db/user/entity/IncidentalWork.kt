package jp.co.toukei.log.trustar.db.user.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import jp.co.toukei.log.lib.util.CompareItem

@Entity(tableName = "incidental_work", primaryKeys = ["work_cd"])
class IncidentalWork(
        @ColumnInfo(name = "work_cd") @JvmField val workCd: String,
        @ColumnInfo(name = "work_nm") @JvmField val workNm: String?,
        @ColumnInfo(name = "display_order") @JvmField val displayOrder: Int
) : CompareItem<IncidentalWork> {

    override fun sameItem(other: IncidentalWork): Boolean {
        return workCd == other.workCd
    }
}
