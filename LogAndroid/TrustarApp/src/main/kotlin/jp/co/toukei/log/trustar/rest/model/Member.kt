package jp.co.toukei.log.trustar.rest.model

import jp.co.toukei.log.trustar.glide.ImageURI

open class Member(
    userId: String,
    companyCd: String,
    val userName: String?,
    val avatarUri: ImageURI?,
) : UserLite(userId, companyCd)
