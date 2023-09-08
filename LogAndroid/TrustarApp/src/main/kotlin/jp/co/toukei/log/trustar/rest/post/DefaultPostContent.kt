package jp.co.toukei.log.trustar.rest.post

import jp.co.toukei.log.lib.util.RequestBodyJson
import jp.co.toukei.log.trustar.currentUserInfoClient
import org.json.JSONObject
import third.jsonObj

class DefaultPostContent(
    private val map: Map<String, String?>,
) : RequestBodyJson {

    constructor(key: String, value: String) : this(mapOf(key to value))

    override fun jsonBody(): JSONObject {
        return jsonObj {
            map.forEach { (key, value) -> key v value }
            "userInfo" v currentUserInfoClient()
        }
    }

    companion object {

        fun messageId(id: String): DefaultPostContent = DefaultPostContent("messageId", id)
        fun token(token: String): DefaultPostContent = DefaultPostContent("token", token)

    }
}
