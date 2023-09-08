package jp.co.toukei.log.trustar.rest.post

import jp.co.toukei.log.lib.jsonArr
import jp.co.toukei.log.lib.util.RequestBodyJson
import jp.co.toukei.log.trustar.compose.ComposeData
import jp.co.toukei.log.trustar.currentUserInfoClient
import jp.co.toukei.log.trustar.rest.model.UserLite
import org.json.JSONObject
import third.jsonObj

class RoomAddMember(
    private val roomId: String,
    private val users: List<UserLite>,
) : RequestBodyJson {

    override fun jsonBody(): JSONObject {
        return jsonObj {
            "talkRoom" {
                "talkRoomId" v roomId
                "users" v users.distinct().jsonArr {
                    it.jsonObject()
                }
            }
            "userInfo" v currentUserInfoClient()
        }
    }
}

fun RoomAddMember(roomId: String, users: List<ComposeData.SelectChatUser>) = RoomAddMember(
    roomId, users.map { UserLite(it.id, it.companyCd) }
)
