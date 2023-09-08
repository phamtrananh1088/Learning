package jp.co.toukei.log.trustar.rest.model

import annotation.Keep
import annotation.Para
import jp.co.toukei.log.lib.sameClass
import org.json.JSONObject
import third.jsonObj

open class UserLite @Keep constructor(
    @Para("userId") @JvmField val userId: String,
    @Para("companyCd") @JvmField val companyCd: String,
) {

    fun jsonObject(): JSONObject {
        return jsonObj {
            "userId" v userId
            "companyCd" v companyCd
        }
    }

    override fun equals(other: Any?): Boolean {
        return other is UserLite && sameClass(other) && other.userId == userId && other.companyCd == companyCd
    }

    override fun hashCode(): Int {
        var result = userId.hashCode()
        result = 31 * result + companyCd.hashCode()
        return result
    }
}
