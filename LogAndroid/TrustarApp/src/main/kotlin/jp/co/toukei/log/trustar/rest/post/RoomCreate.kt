package jp.co.toukei.log.trustar.rest.post

import jp.co.toukei.log.lib.chains
import jp.co.toukei.log.lib.jsonArr
import jp.co.toukei.log.lib.util.RequestBodyJson
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.compose.ComposeData
import jp.co.toukei.log.trustar.currentUserInfoClient
import jp.co.toukei.log.trustar.rest.model.UserLite
import org.json.JSONObject
import third.jsonObj

class RoomCreate(
    private val users: List<UserLite>,
) : RequestBodyJson {
    override fun jsonBody(): JSONObject {
        val userIds = users
            .chains(listOf(Current.userInfo))
            .distinct()

        return jsonObj {
            "talkRoom" {
                "users" v userIds.jsonArr {
                    it.jsonObject()
                }
            }
            "userInfo" v currentUserInfoClient()
        }
    }
}

fun RoomCreate(users: List<ComposeData.SelectChatUser>) =
    RoomCreate(users.map { UserLite(it.id, it.companyCd) })