package jp.co.toukei.log.trustar.rest.post

import jp.co.toukei.log.lib.util.RequestBodyJson
import jp.co.toukei.log.trustar.db.result.entity.CommonCollectionResult
import jp.co.toukei.log.trustar.forEachOptObject
import jp.co.toukei.log.trustar.user.UserInfo
import org.json.JSONArray
import org.json.JSONObject
import third.jsonObj

class CollectionPost(
        private val userInfo: UserInfo?,
        @JvmField val collections: List<CommonCollectionResult>
) : RequestBodyJson {

    override fun jsonBody(): JSONObject {
        return jsonObj {
            "collectionResults" v JSONArray().put(
                    jsonObj {
                        "companyCd" v userInfo?.companyCd
                        "userId" v userInfo?.userId
                        "addCollection" v JSONArray().also { t ->
                            collections.forEach { a ->
                                a.postJson.forEachOptObject { _, r ->
                                    if (r != null) t.put(r)
                                }
                            }
                        }
                    }
            )
        }
    }
}
