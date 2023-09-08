package jp.co.toukei.log.trustar.rest.post

import jp.co.toukei.log.lib.util.RequestBodyJson
import jp.co.toukei.log.trustar.compose.ComposeData
import jp.co.toukei.log.trustar.db.user.embedded.LocationRecord
import jp.co.toukei.log.trustar.user.ClientInfo
import jp.co.toukei.log.trustar.user.UserInfo
import org.json.JSONObject
import third.jsonObj

class UnscheduledBin(
    private val token: String,
    private val truck: ComposeData.TruckKun,
    private val locationRecord: LocationRecord,
    private val clientInfo: ClientInfo,
    private val userInfo: UserInfo,
    private val outgoingMeter: Int?,
) : RequestBodyJson {

    override fun jsonBody(): JSONObject {
        return jsonObj {
            "companyCd" v userInfo.companyCd
            "userId" v userInfo.userId

            "truckCd" v truck.truckCd

            locationRecord.run {
                "latitude" v (location?.lat ?: 0F)
                "longitude" v (location?.lon ?: 0F)
                "accuracy" v (location?.accuracy ?: 0F)
                "executeDate" v date
            }

            "outgoingMeter" v outgoingMeter

            "allocationToken" v jsonObj {
                "token" v token
            }

            "clientInfo" v clientInfo.jsonBody()
        }
    }
}
