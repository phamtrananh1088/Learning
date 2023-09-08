package jp.co.toukei.log.trustar.rest.post

import jp.co.toukei.log.lib.jsonArr
import jp.co.toukei.log.lib.util.RequestBodyJson
import jp.co.toukei.log.trustar.db.result.entity.CommonCoordinate
import jp.co.toukei.log.trustar.user.ClientInfo
import org.json.JSONObject
import third.jsonObj

class Geo(
        @JvmField val coordinates: List<CommonCoordinate>,
        private val clientInfo: ClientInfo
) : RequestBodyJson {

    override fun jsonBody(): JSONObject {
        return jsonObj {
            "coordinates" v coordinates.jsonArr {
                it.jsonBody()
            }
            "clientInfo" v clientInfo.jsonBody()
        }
    }
}
