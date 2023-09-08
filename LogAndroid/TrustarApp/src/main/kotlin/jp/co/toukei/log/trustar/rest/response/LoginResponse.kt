package jp.co.toukei.log.trustar.rest.response

import annotation.Find
import annotation.Para
import jp.co.toukei.log.lib.fromJSON
import jp.co.toukei.log.trustar.Config
import jp.co.toukei.log.trustar.user.UserInfo
import jp.co.toukei.log.trustar.user.UserWithToken
import third.Result

class LoginResponse @Find constructor(
    @Para("loginResult") @JvmField val loginResult: LR,
    @Para("userInfo") @JvmField val userInfo: UserInfo?
) {
    class LR @Find constructor(
        @Para("isLoggedIn") @JvmField val isLoggedIn: Boolean,
        @Para("messageCode") @JvmField val messageCode: String,
        @Para("token") @JvmField val token: String?,
        @Para("uid") @JvmField val uid: String?,
    )

    fun toUserWithToken(): Result<UserWithToken>? {
        return loginResult.run {
            if (isLoggedIn) {
                val u = userInfo
                if (u == null || token == null) {
                    null
                } else {
                    Result.Value(UserWithToken(u, token, uid))
                }
            } else {
                Result.Error(LoginException(messageCode))
            }
        }
    }

    companion object {

        @JvmStatic
        fun fromJSONObject(j: String): LoginResponse? {
            return Config.commonMoshi.fromJSON<LoginResponse>(j)
        }
    }
}
