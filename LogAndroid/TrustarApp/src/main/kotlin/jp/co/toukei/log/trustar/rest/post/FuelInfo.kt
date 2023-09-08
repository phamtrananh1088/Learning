package jp.co.toukei.log.trustar.rest.post

import jp.co.toukei.log.lib.jsonArr
import jp.co.toukei.log.lib.util.RequestBodyJson
import jp.co.toukei.log.trustar.db.result.entity.CommonRefuel
import jp.co.toukei.log.trustar.user.ClientInfo
import org.json.JSONObject
import third.jsonObj

class FuelInfo(
        @JvmField val kyuyu: List<CommonRefuel>,
        private val clientInfo: ClientInfo
) : RequestBodyJson {

    override fun jsonBody(): JSONObject {
        return jsonObj {
            "Kyuyus" v kyuyu.jsonArr {
                it.jsonBody()
            }
            "clientInfo" v clientInfo.jsonBody()
        }
    }
}
