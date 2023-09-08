package jp.co.toukei.log.trustar.db.result.entity

import android.location.Location
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import jp.co.toukei.log.lib.util.RequestBodyJson
import jp.co.toukei.log.trustar.db.user.entity.BinHeader
import jp.co.toukei.log.trustar.user.UserInfo
import org.json.JSONObject
import third.jsonObj

@Entity(
    tableName = "common_coordinate"
)
class CommonCoordinate(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") @JvmField val id: Int,
    companyCd: String,
    userId: String,

    @ColumnInfo(name = "truck_cd") @JvmField val truckCd: String,
    @ColumnInfo(name = "allocation_no") @JvmField val allocationNo: String,

    @ColumnInfo(name = "latitude") @JvmField val latitude: Double,
    @ColumnInfo(name = "longitude") @JvmField val longitude: Double,
    @ColumnInfo(name = "accuracy") @JvmField val accuracyInMeters: Float,
    @ColumnInfo(name = "date") @JvmField val date: Long,
    @ColumnInfo(name = "operating_mode") @JvmField val operatingMode: Boolean,
) : RequestBodyJson, CommonUserSync(companyCd, userId) {

    constructor(
        location: Location,
        binHeader: BinHeader,
        userInfo: UserInfo,
        isAutoMode: Boolean
    ) : this(
        0,
        userInfo.companyCd,
        userInfo.userId,

        binHeader.truckCd,
        binHeader.allocationNo,

        location.latitude,
        location.longitude,
        location.accuracy,
        System.currentTimeMillis(),
        isAutoMode
    )

    override fun jsonBody(): JSONObject {
        return jsonObj {
            "companyCd" v companyCd
            "userId" v userId

            "truckCd" v truckCd
            "allocationNo" v allocationNo

            "latitude" v latitude
            "longitude" v longitude
            "accuracy" v accuracyInMeters
            "getDatetime" v date

            if (operatingMode)
                "operatingMode" v "autoMode"
        }
    }
}
