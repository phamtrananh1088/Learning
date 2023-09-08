package jp.co.toukei.log.trustar.rest.post

import jp.co.toukei.log.lib.util.RequestBodyJson
import jp.co.toukei.log.trustar.currentUserInfoClient
import org.json.JSONObject
import third.jsonObj

class ChatUserSettings(
    private val roomId: String,
    private val userId: String,
    private val notificationFlag: Boolean? = null,
    private val leavingFlag: Boolean? = null,
) : RequestBodyJson {

    override fun jsonBody(): JSONObject {
        return jsonObj {
            "talkRoomId" v roomId
            "userId" v userId
            "notificationFlag" v notificationFlag
            "leavingFlag" v leavingFlag
            "userInfo" v currentUserInfoClient()
        }
    }
}
