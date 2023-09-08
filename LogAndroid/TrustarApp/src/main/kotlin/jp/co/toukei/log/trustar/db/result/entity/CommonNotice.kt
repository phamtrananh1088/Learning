package jp.co.toukei.log.trustar.db.result.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import jp.co.toukei.log.lib.util.RequestBodyJson
import jp.co.toukei.log.trustar.db.user.entity.Notice
import jp.co.toukei.log.trustar.user.UserInfo
import org.json.JSONObject
import third.jsonObj

@Entity(
    tableName = "notice",
    primaryKeys = ["company_cd", "user_id", "record_id"]
)
class CommonNotice(
    companyCd: String,
    userId: String,
    @ColumnInfo(name = "record_id") @JvmField val recordId: String,

    @ColumnInfo(name = "grp_id") @JvmField val grpRecordId: String,
    @ColumnInfo(name = "notice_cd") @JvmField val noticeCd: String?,
    @ColumnInfo(name = "read_datetime") @JvmField val readDatetime: Long?,
) : RequestBodyJson, CommonUserSync(companyCd, userId) {

    constructor(userInfo: UserInfo, notice: Notice) : this(
        userInfo.companyCd,
        userInfo.userId,
        notice.recordId,
        notice.grpRecordId,
        notice.noticeCd,
        notice.readDatetime
    )

    override fun jsonBody(): JSONObject {
        return jsonObj {
            "companyCd" v companyCd
            "userId" v userId
            "recordId" v recordId

            "grpRecordId" v grpRecordId
            "noticeCd" v noticeCd
            "readDatetime" v readDatetime
        }
    }
}
