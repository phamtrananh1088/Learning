package jp.co.toukei.log.trustar.db.result.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import jp.co.toukei.log.trustar.db.user.entity.IncidentalHeader
import jp.co.toukei.log.trustar.user.UserInfo

@Entity(
        tableName = "incidental_header_result",
        primaryKeys = ["company_cd", "user_id", "uuid", "allocation_no", "allocation_row_no"]
)
class CommonIncidentalHeaderResult(
        companyCd: String,
        userId: String,
        @ColumnInfo(name = "uuid") @JvmField val uuid: String,
        @ColumnInfo(name = "sheet_no") @JvmField val sheetNo: String?,
        @ColumnInfo(name = "allocation_no") @JvmField val allocationNo: String,
        @ColumnInfo(name = "allocation_row_no") @JvmField val allocationRowNo: Int,
        @ColumnInfo(name = "shipper") @JvmField val shipperCd: String,
        @ColumnInfo(name = "works") @JvmField val workList: List<String>,
        @ColumnInfo(name = "picId") @JvmField val picId: String?,
        @ColumnInfo(name = "deleted") val deleted: Boolean
) : CommonUserSync(companyCd, userId) {

    constructor(userInfo: UserInfo, header: IncidentalHeader) : this(
            userInfo.companyCd,
            userInfo.userId,
            header.uuid,
            header.sheetNo,
            header.allocationNo,
            header.allocationRowNo,
            header.shipperCd,
            header.workList,
            header.picId,
            header.deleted
    )
}
