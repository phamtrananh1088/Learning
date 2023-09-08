package jp.co.toukei.log.trustar.rest.post

import jp.co.toukei.log.lib.jsonArr
import jp.co.toukei.log.lib.util.RequestBodyJson
import jp.co.toukei.log.trustar.db.result.entity.CommonRest
import jp.co.toukei.log.trustar.user.ClientInfo
import org.json.JSONObject
import third.jsonObj

class RestPost(
    @JvmField val list: List<CommonRest>,
    private val clientInfo: ClientInfo,
) : RequestBodyJson {

    override fun jsonBody(): JSONObject {
        return jsonObj {
            "rests" v list.jsonArr {
                it.jsonBody()
            }
            "clientInfo" v clientInfo.jsonBody()
        }
    }
}
