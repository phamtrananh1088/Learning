package jp.co.toukei.log.trustar.rest.model

import annotation.Keep
import annotation.Para
import jp.co.toukei.log.trustar.imageURI

class TalkUser @Keep constructor(
    @Para("userId") userId: String,
    @Para("companyCd") companyCd: String,
    @Para("userName") userName: String?,
    @Para("mailAddress") @JvmField val mailAddress: String?,
    @Para("avatarImageUrl") @JvmField val avatarImageUrl: String?,
) : Member(userId, companyCd, userName, avatarImageUrl?.imageURI())
