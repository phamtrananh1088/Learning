package jp.co.toukei.log.trustar.db.chat

import annotation.Keep
import annotation.Para
import org.json.JSONObject
import third.jsonObj

open class Attachment @Keep constructor(
    @Para("key") @JvmField val key: String,
    @Para("name") @JvmField val name: String,
    @Para("size") @JvmField val size: Long?,
) {
    fun serializeJson(): JSONObject {
        return jsonObj {
            "key" v key
            "name" v name
            "size" v size
        }
    }
}
