package jp.co.toukei.log.trustar.rest.post

import jp.co.toukei.log.lib.util.RequestBodyJson
import jp.co.toukei.log.trustar.db.user.embedded.LocationRecord
import jp.co.toukei.log.trustar.db.user.entity.Truck
import jp.co.toukei.log.trustar.user.ClientInfo
import jp.co.toukei.log.trustar.user.UserInfo
import org.json.JSONObject
import third.jsonObj

class UnscheduledBin(
        private val truck: Truck,
        private val locationRecord: LocationRecord,
        private val clientInfo: ClientInfo,
        private val userInfo: UserInfo,
        private val outgoingMeter: Int? = null
) : RequestBodyJson {

    override fun jsonBody(): JSONObject {
        return jsonObj {
            "companyCd" v userInfo.companyCd
            "userId" v userInfo.userId

            "truckCd" v truck.truckCd

            "latitude" v (locationRecord.latitude ?: 0.0)
            "longitude" v (locationRecord.longitude ?: 0.0)
            "accuracy" v (locationRecord.accuracy ?: 0F)
            "executeDate" v locationRecord.date

            "outgoingMeter" v outgoingMeter

            "clientInfo" v clientInfo.jsonBody()
        }
    }
}
