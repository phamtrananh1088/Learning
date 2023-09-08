package jp.co.toukei.log.trustar.db.user.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import jp.co.toukei.log.lib.room.DeletableSync
import jp.co.toukei.log.trustar.db.user.db.UserDB
import jp.co.toukei.log.trustar.enum.EditEnum

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
    beginDatetime: Long,
    endDatetime: Long,
) : DeletableSync() {

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
}

data class TimeRange(
    @JvmField val delegate: IncidentalTime?,
    @JvmField val id: String,
    @JvmField val type: Int,
    @JvmField val start: Long,
    @JvmField val end: Long,
    @JvmField val origState: EditEnum?,
    @JvmField val newState: EditEnum? = origState,
) {

    fun deleted(): TimeRange {
        return TimeRange(delegate, id, type, start, end, origState, EditEnum.Deleted)
    }

    fun editDate(newStart: Long, newEnd: Long): TimeRange {
        return TimeRange(delegate, id, type, newStart, newEnd, origState, EditEnum.Edited)
    }

    fun isDeletedAdded(): Boolean {
        return origState == EditEnum.Added && newState == EditEnum.Deleted
    }

    companion object {
        @JvmField
        val key: (TimeRange) -> String = { it.id }
    }
}
