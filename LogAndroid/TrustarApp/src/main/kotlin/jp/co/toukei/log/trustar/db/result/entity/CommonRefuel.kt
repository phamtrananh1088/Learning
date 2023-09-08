package jp.co.toukei.log.trustar.db.result.entity

import android.location.Location
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import jp.co.toukei.log.trustar.db.LocationDb
import jp.co.toukei.log.lib.util.RequestBodyJson
import jp.co.toukei.log.trustar.user.UserInfo
import org.json.JSONObject
import third.jsonObj

@Entity(
    tableName = "refuel"
)
class CommonRefuel(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") @JvmField val id: Int,
    companyCd: String,
    userId: String,

    @ColumnInfo(name = "allocation_no") @JvmField val allocationNo: String,
    @ColumnInfo(name = "truck_cd") @JvmField val truckCd: String,
    @ColumnInfo(name = "input_datetime") @JvmField val inputDatetime: Long,
    @ColumnInfo(name = "input_user_id") @JvmField val inputUserId: String,
    @ColumnInfo(name = "fuelClass_cd") @JvmField val fuelClassCd: String,
    @ColumnInfo(name = "refueling_vol") @JvmField val refuelingVol: Float,
    @ColumnInfo(name = "refueling_fee") @JvmField val refuelingFee: Float,

    @ColumnInfo(name = "location") @JvmField val location: LocationDb,
) : RequestBodyJson, CommonUserSync(companyCd, userId) {

    constructor(
        userInfo: UserInfo,
        allocationNo: String,
        truckCd: String,
        fuelCd: String,
        quantity: Float,
        cost: Float,
        location: Location?,
    ) : this(
        id = 0,
        companyCd = userInfo.companyCd,
        userId = userInfo.userId,

        allocationNo = allocationNo,
        truckCd = truckCd,
        inputDatetime = System.currentTimeMillis(),
        inputUserId = userInfo.userId,
        fuelClassCd = fuelCd,
        refuelingVol = quantity,
        refuelingFee = cost,
        location = location?.let(::LocationDb) ?: LocationDb(0F, 0F, 0F),
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
            "refuelingPayment" v refuelingFee

            "latitude" v location.lat
            "longitude" v location.lon
            "accuracy" v location.accuracy
        }
    }
}
