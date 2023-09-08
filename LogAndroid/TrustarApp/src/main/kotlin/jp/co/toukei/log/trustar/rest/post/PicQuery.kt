package jp.co.toukei.log.trustar.rest.post

import jp.co.toukei.log.lib.util.RequestBodyJson
import jp.co.toukei.log.trustar.user.UserInfo
import org.json.JSONObject
import third.jsonObj

class PicQuery(
        private val picId: String,
        private val userInfo: UserInfo
) : RequestBodyJson {

    override fun jsonBody(): JSONObject {
        return jsonObj {
            "companyCd" v userInfo.companyCd
            "picId" v picId
        }
    }
}
