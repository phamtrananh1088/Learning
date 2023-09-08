package jp.co.toukei.log.trustar.user

class UserWithToken(
    @JvmField val userInfo: UserInfo,
    @JvmField val token: String,
    @JvmField val uid: String?,
)
