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
    tableName = "rest"
)
class CommonRest(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") @JvmField val id: Int,
    companyCd: String,
    userId: String,

    @ColumnInfo(name = "allocation_no") @JvmField val allocationNo: String,

    @ColumnInfo(name = "start_location") @JvmField val startLocation: LocationDb,
    @ColumnInfo(name = "end_date") @JvmField val endDate: Long,
    @ColumnInfo(name = "elapsed") @JvmField val elapsed: Long,
) : RequestBodyJson, CommonUserSync(companyCd, userId) {

    constructor(
        userInfo: UserInfo,
        allocationNo: String,
        startLocation: Location,
        endDate: Long,
        elapsed: Long,
    ) : this(
        id = 0,
        companyCd = userInfo.companyCd,
        userId = userInfo.userId,
        allocationNo = allocationNo,
        startLocation = LocationDb(startLocation),
        endDate = endDate,
        elapsed = elapsed,
    )

    override fun jsonBody(): JSONObject {
        return jsonObj {
            "companyCd" v companyCd
            "userId" v userId

            "allocationNo" v allocationNo
            "restStartDate" v (endDate - elapsed)
            "restEndDate" v endDate
            "restLatitude" v startLocation.lat
            "restLongitude" v startLocation.lon
            "restAccuracy" v startLocation.accuracy
        }
    }
}
