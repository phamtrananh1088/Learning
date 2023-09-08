package jp.co.toukei.log.trustar.rest.post

import jp.co.toukei.log.lib.util.RequestBodyJson
import jp.co.toukei.log.trustar.user.ClientInfo
import org.json.JSONObject
import third.jsonObj

class LoginRequest(
        private val userId: String,
        private val companyCd: String,
        private val password: String,
        private val clientInfo: ClientInfo
) : RequestBodyJson {

    override fun jsonBody(): JSONObject {
        return jsonObj {
            "userId" v userId
            "companyCd" v companyCd
            "password" v password
            "clientInfo" v clientInfo.jsonBody()
        }
    }
}
