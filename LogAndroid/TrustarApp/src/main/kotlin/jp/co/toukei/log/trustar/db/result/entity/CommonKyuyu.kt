package jp.co.toukei.log.trustar.db.result.entity

import android.location.Location
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import jp.co.toukei.log.lib.util.RequestBodyJson
import jp.co.toukei.log.trustar.other.Refueled
import jp.co.toukei.log.trustar.user.UserInfo
import org.json.JSONObject
import third.jsonObj

@Entity(
        tableName = "common_kyuyu"
)
class CommonKyuyu(
        @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") @JvmField val id: Int,
        companyCd: String,
        userId: String,

        @ColumnInfo(name = "allocation_no") @JvmField val allocationNo: String,
        @ColumnInfo(name = "truck_cd") @JvmField val truckCd: String,
        @ColumnInfo(name = "input_datetime") @JvmField val inputDatetime: Long,
        @ColumnInfo(name = "input_user_id") @JvmField val inputUserId: String,
        @ColumnInfo(name = "fuelClass_cd") @JvmField val fuelClassCd: String,
        @ColumnInfo(name = "refueling_vol") @JvmField val refuelingVol: Float,
        @ColumnInfo(name = "refueling_payment") @JvmField val refuelingPayment: Float,

        @ColumnInfo(name = "latitude") @JvmField val latitude: Double,
        @ColumnInfo(name = "longitude") @JvmField val longitude: Double,
        @ColumnInfo(name = "accuracy") @JvmField val accuracyInMeters: Float
) : RequestBodyJson, CommonUserSync(companyCd, userId) {

    constructor(
        userInfo: UserInfo,
        refueled: Refueled,
        location: Location?
    ) : this(
            0,
            userInfo.companyCd,
            userInfo.userId,

            refueled.binHeader.allocationNo,
            refueled.binHeader.truckCd,
            System.currentTimeMillis(),
            userInfo.userId,
            refueled.fuel.fuelCd,
            refueled.quantity,
            refueled.paid,

            location?.latitude ?: 0.0,
            location?.longitude ?: 0.0,
            location?.accuracy ?: 0F
    )

    override fun jsonBody(): JSONObject {
        return jsonObj {
            "companyCd" v companyCd
            "userId" v userId

            "allocationNo" v allocationNo
            "truckCd" v truckCd
            "inputDatetime" v inputDatetime
            "inputUserId" v inputUserId
            "fuelClassCd" v fuelClassCd
            "refuelingVol" v refuelingVol
            "refuelingPayment" v refuelingPayment

            "latitude" v latitude
            "longitude" v longitude
            "accuracy" v accuracyInMeters
        }
    }
}
