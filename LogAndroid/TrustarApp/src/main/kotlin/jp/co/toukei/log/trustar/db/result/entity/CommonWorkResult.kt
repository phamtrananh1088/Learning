package jp.co.toukei.log.trustar.db.result.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import jp.co.toukei.log.lib.util.RequestBodyJson
import jp.co.toukei.log.trustar.db.user.embedded.LocationRecord
import jp.co.toukei.log.trustar.db.user.entity.BinDetail
import jp.co.toukei.log.trustar.user.UserInfo
import org.json.JSONObject
import third.jsonObj

@Entity(
    tableName = "common_work_result",
    primaryKeys = ["company_cd", "user_id", "allocation_no", "allocation_row_no"]
)
class CommonWorkResult(
    companyCd: String,
    userId: String,
    @ColumnInfo(name = "allocation_no") @JvmField val allocationNo: String,
    @ColumnInfo(name = "allocation_row_no") @JvmField val allocationRowNo: Int,

    @ColumnInfo(name = "work_cd") @JvmField val workCd: String?,
    @ColumnInfo(name = "status") @JvmField val status: Int,
    @ColumnInfo(name = "delay_status") @JvmField val delayStatus: Int,
    @ColumnInfo(name = "delay_reason_cd") @JvmField val delayReasonCd: String?,
    @ColumnInfo(name = "misdelivery_status") @JvmField val misdeliveryStatus: Int,
    @ColumnInfo(name = "org_allocation_row_no") @JvmField val orgAllocationRowNo: Int?,

    @Embedded(prefix = "start_") @JvmField val workStart: LocationRecord?,
    @Embedded(prefix = "end_") @JvmField val workEnd: LocationRecord?,
    @ColumnInfo(name = "temperature") @JvmField val temperature: Double?,
    @ColumnInfo(name = "experience_place_note1") @JvmField val experiencePlaceNote1: String?,
    @ColumnInfo(name = "operating_mode") @JvmField val operatingMode: Boolean,
) : RequestBodyJson, CommonUserSync(companyCd, userId) {

    constructor(userInfo: UserInfo, binDetail: BinDetail) : this(
        userInfo.companyCd,
        userInfo.userId,
        binDetail.allocationNo,
        binDetail.allocationRowNo,

        binDetail.work?.workCd,
        binDetail.status,
        binDetail.delayStatus,
        binDetail.delayReasonCd,
        binDetail.misdeliveryStatus,
        binDetail.origAllocationRowNo,

        binDetail.workStart,
        binDetail.workEnd,
        binDetail.temperature,
        binDetail.experiencePlaceNote1,
        binDetail.autoModeFlag
    )

    override fun jsonBody(): JSONObject {
        return jsonObj {
            "companyCd" v companyCd
            "userId" v userId
            "allocationNo" v allocationNo
            "allocationRowNo" v allocationRowNo

            "workCd" v workCd
            "status" v status
            "delayStatus" v (if (delayStatus == 0) 0 else 1)
            "delayReasonCd" v delayReasonCd
            "misdeliveryStatus" v misdeliveryStatus
            "orgAllocationRowNo" v orgAllocationRowNo

            workStart?.apply {
                "latitudeFrom" v (latitude ?: 0.0)
                "longitudeFrom" v (longitude ?: 0.0)
                "accuracyFrom" v (accuracy ?: 0F)
                "workDatetimeFrom" v date
            }
            workEnd?.apply {
                "latitudeTo" v (latitude ?: 0.0)
                "longitudeTo" v (longitude ?: 0.0)
                "accuracyTo" v (accuracy ?: 0F)
                "workDatetimeTo" v date
            }
            "temperature" v (temperature ?: JSONObject.NULL)
            "experiencePlaceNote1" v (experiencePlaceNote1 ?: JSONObject.NULL)

            if (operatingMode)
                "operatingMode" v "autoMode"
        }
    }
}
