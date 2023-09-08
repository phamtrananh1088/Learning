package jp.co.toukei.log.trustar.rest.post

import jp.co.toukei.log.lib.jsonArr
import jp.co.toukei.log.lib.util.RequestBodyJson
import jp.co.toukei.log.trustar.db.result.entity.CommonNotice
import jp.co.toukei.log.trustar.user.ClientInfo
import org.json.JSONObject
import third.jsonObj

class NoticePost(
        @JvmField val notices: List<CommonNotice>,
        private val clientInfo: ClientInfo
) : RequestBodyJson {

    override fun jsonBody(): JSONObject {
        return jsonObj {
            "notices" v notices.jsonArr {
                it.jsonBody()
            }
            "clientInfo" v clientInfo.jsonBody()
        }
    }
}
