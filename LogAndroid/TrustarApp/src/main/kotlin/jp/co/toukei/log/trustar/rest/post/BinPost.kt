package jp.co.toukei.log.trustar.rest.post

import jp.co.toukei.log.lib.jsonArr
import jp.co.toukei.log.lib.util.RequestBodyJson
import jp.co.toukei.log.trustar.db.result.entity.CommonBinResult
import jp.co.toukei.log.trustar.db.result.entity.CommonWorkResult
import jp.co.toukei.log.trustar.user.ClientInfo
import org.json.JSONObject
import third.jsonObj

class BinPost(
        @JvmField val binResults: List<CommonBinResult>,
        @JvmField val workResults: List<CommonWorkResult>,
        private val clientInfo: ClientInfo
) : RequestBodyJson {

    override fun jsonBody(): JSONObject {
        return jsonObj {
            "binResults" v binResults.jsonArr { it.jsonBody() }
            "workResults" v workResults.jsonArr { it.jsonBody() }
            "clientInfo" v clientInfo.jsonBody()
        }
    }
}
