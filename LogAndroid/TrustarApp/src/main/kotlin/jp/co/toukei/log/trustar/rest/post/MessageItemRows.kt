package jp.co.toukei.log.trustar.rest.post

import jp.co.toukei.log.lib.jsonArr
import jp.co.toukei.log.lib.util.RequestBodyJson
import jp.co.toukei.log.trustar.currentUserInfoClient
import org.json.JSONObject
import third.jsonObj

class MessageItemRows(
    private val messageRowIds: List<String>,
) : RequestBodyJson {

    override fun jsonBody(): JSONObject {
        return jsonObj {
            "messageItems" v messageRowIds.jsonArr {
                jsonObj {
                    "messageRowId" v it
                }
            }
            "userInfo" v currentUserInfoClient()
        }
    }
}
