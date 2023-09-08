package jp.co.toukei.log.trustar.db.user.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import jp.co.toukei.log.lib.room.DeletableSync
import jp.co.toukei.log.lib.util.CompareItem
import jp.co.toukei.log.trustar.db.user.db.UserDB

/**
 * @property type 0: 荷待待機　1: 付帯業務
 */
@Entity(tableName = "incidental_time")
class IncidentalTime(
        @PrimaryKey @ColumnInfo(name = "uuid") @JvmField val uuid: String,
        @ColumnInfo(name = "header", index = true) @JvmField val headerUUID: String,
        /** @see [UserDB.CREATE_INDEX_INCIDENTAL_TIME_SHEET] */
        @ColumnInfo(name = "sheet") @JvmField val sheetNo: String?,
        @ColumnInfo(name = "row_no") @JvmField val sheetRowNo: Int?,
        @ColumnInfo(name = "type") @JvmField val type: Int,
        beginDatetime: Long?,
        endDatetime: Long?
) : DeletableSync(), CompareItem<IncidentalTime> {

    @ColumnInfo(name = "begin")
    var beginDatetime = beginDatetime
        set(value) {
            field = value
            recordChanged()
        }

    @ColumnInfo(name = "end")
    var endDatetime = endDatetime
        set(value) {
            field = value
            recordChanged()
        }

    override fun sameItem(other: IncidentalTime): Boolean {
        return uuid == other.uuid
    }
}
