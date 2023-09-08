package jp.co.toukei.log.trustar.rest.post

import jp.co.toukei.log.lib.jsonArr
import jp.co.toukei.log.lib.util.RequestBodyJson
import jp.co.toukei.log.trustar.db.result.entity.FailedData
import jp.co.toukei.log.trustar.user.ClientInfo
import org.json.JSONObject
import third.jsonObj

class LocalData(
        @JvmField val failedData: List<FailedData>,
        private val clientInfo: ClientInfo
) : RequestBodyJson {

    override fun jsonBody(): JSONObject {
        return jsonObj {
            "clientInfo" v clientInfo.jsonBody()

            "failedData" v failedData.jsonArr {
                jsonObj {
                    "dataClass" v it.type
                    "contents" v it.contents
                    "errMessage" v it.errMessage
                    "datetime" v it.date
                }
            }
        }
    }
}
