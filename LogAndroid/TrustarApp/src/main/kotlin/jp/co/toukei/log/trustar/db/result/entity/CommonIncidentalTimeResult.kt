package jp.co.toukei.log.trustar.db.result.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import jp.co.toukei.log.trustar.db.user.entity.IncidentalTime
import jp.co.toukei.log.trustar.user.UserInfo

@Entity(
        tableName = "common_incidental_time_result",
        primaryKeys = ["company_cd", "user_id", "uuid"]
)
class CommonIncidentalTimeResult(
        companyCd: String,
        userId: String,
        @ColumnInfo(name = "uuid") @JvmField val uuid: String,
        @ColumnInfo(name = "header") @JvmField val headerUUID: String,
        @ColumnInfo(name = "sheet") @JvmField val sheetNo: String?,
        @ColumnInfo(name = "row_no") @JvmField val sheetRowNo: Int?,
        @ColumnInfo(name = "type") @JvmField val type: Int,
        @ColumnInfo(name = "begin") @JvmField val beginDatetime: Long?,
        @ColumnInfo(name = "end") @JvmField val endDatetime: Long?,
        @ColumnInfo(name = "deleted") val deleted: Boolean
) : CommonUserSync(companyCd, userId) {

    constructor(userInfo: UserInfo, time: IncidentalTime) : this(
            userInfo.companyCd,
            userInfo.userId,
            time.uuid,
            time.headerUUID,
            time.sheetNo,
            time.sheetRowNo,
            time.type,
            time.beginDatetime,
            time.endDatetime,
            time.deleted
    )
}
