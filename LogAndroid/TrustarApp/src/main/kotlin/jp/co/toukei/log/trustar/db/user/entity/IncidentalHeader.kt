package jp.co.toukei.log.trustar.db.user.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import jp.co.toukei.log.lib.room.DeletableSync
import jp.co.toukei.log.lib.util.CompareItem
import jp.co.toukei.log.trustar.db.user.db.UserDB

@Entity(tableName = "incidental_header")
class IncidentalHeader(
        @PrimaryKey @ColumnInfo(name = "uuid") @JvmField val uuid: String,
        /** @see [UserDB.CREATE_INDEX_INCIDENTAL_HEADER_SHEET] */
        @ColumnInfo(name = "sheet_no") @JvmField val sheetNo: String?,
        @ColumnInfo(name = "allocation_no") @JvmField val allocationNo: String,
        @ColumnInfo(name = "allocation_row_no") @JvmField val allocationRowNo: Int,
        shipperCd: String,
        workList: List<String>,

        status: Int,
        picId: String?
) : DeletableSync(), CompareItem<IncidentalHeader> {

    @ColumnInfo(name = "pic_id")
    var picId = picId
        set(value) {
            field = value
            recordChanged()
        }

    @ColumnInfo(name = "status")
    var status = status
        set(value) {
            field = value
            recordChanged()
        }

    @ColumnInfo(name = "shipper")
    var shipperCd: String = shipperCd
        set(value) {
            field = value
            recordChanged()
        }
    @ColumnInfo(name = "works")
    var workList = workList
        set(value) {
            field = value
            recordChanged()
        }

    override fun sameItem(other: IncidentalHeader): Boolean {
        return uuid == other.uuid
    }

    fun localSignatureId(): String {
        return "incidental:$uuid:$picId"
    }

    fun setSignStatus(isSigned: Boolean) {
        status = if (isSigned) 1 else 0
    }
}
