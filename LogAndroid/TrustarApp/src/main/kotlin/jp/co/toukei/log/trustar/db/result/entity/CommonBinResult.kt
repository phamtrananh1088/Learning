package jp.co.toukei.log.trustar.db.result.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import jp.co.toukei.log.lib.util.RequestBodyJson
import jp.co.toukei.log.trustar.db.user.embedded.LocationRecord
import jp.co.toukei.log.trustar.db.user.entity.BinHeader
import jp.co.toukei.log.trustar.user.UserInfo
import org.json.JSONObject
import third.jsonObj


@Entity(
    tableName = "bin_result",
    primaryKeys = ["company_cd", "user_id", "allocation_no"]
)
class CommonBinResult(
    companyCd: String,
    userId: String,
    @ColumnInfo(name = "allocation_no") @JvmField val allocationNo: String,

    @ColumnInfo(name = "truck_cd") @JvmField val truckCd: String,
    @ColumnInfo(name = "weather_cd") @JvmField val weatherCd: Int?,
    @ColumnInfo(name = "destination_row_no") @JvmField val destinationRowNo: Int,

    @Embedded(prefix = "start_") @JvmField val startLocation: LocationRecord?,
    @Embedded(prefix = "end_") @JvmField val endLocation: LocationRecord?,

    @ColumnInfo(name = "outgoing_meter") @JvmField val outgoingMeter: Int?,
    @ColumnInfo(name = "incoming_meter") @JvmField val incomingMeter: Int?,

    @ColumnInfo(name = "updated_date") @JvmField val updatedDate: Long,
) : RequestBodyJson, CommonUserSync(companyCd, userId) {

    constructor(userInfo: UserInfo, binHeader: BinHeader) : this(
        userInfo.companyCd,
        userInfo.userId,
        binHeader.allocationNo,

        binHeader.truckCd,
        binHeader.weatherCd,
        binHeader.destinationRowNo,

        binHeader.startLocation,
        binHeader.endLocation,
        binHeader.outgoingMeter,
        binHeader.incomingMeter,

        binHeader.updatedDate
    )

    override fun jsonBody(): JSONObject {
        return jsonObj {
            "companyCd" v companyCd
            "userId" v userId
            "allocationNo" v allocationNo

            "truckCd" v truckCd
            "weatherCd" v weatherCd
            "destinationRowNo" v destinationRowNo

            startLocation?.apply {
                "startLatitude" v (location?.lat ?: 0F)
                "startLongitude" v (location?.lon ?: 0F)
                "startAccuracy" v (location?.accuracy ?: 0F)
                "startDatetime" v (date)
            }
            endLocation?.apply {
                "endLatitude" v (location?.lat ?: 0F)
                "endLongitude" v (location?.lon ?: 0F)
                "endAccuracy" v (location?.accuracy ?: 0F)
                "endDatetime" v (date)
            }

            "outgoingMeter" v (outgoingMeter ?: JSONObject.NULL)
            "incomingMeter" v (incomingMeter ?: JSONObject.NULL)

            "updatedDate" v updatedDate
        }
    }

    /*
        // to prevent room warning.
        override fun equals(other: Any?): Boolean {
            return super.equals(other)
        }

        override fun hashCode(): Int {
            return super.hashCode()
        }
    */
}
