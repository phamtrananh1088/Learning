package jp.co.toukei.log.trustar.rest.model

import annotation.Keep
import annotation.Para

class MessageItemBase @Keep constructor(
    @Para("messageRowId") @JvmField val messageRowId: String,
    @Para("messageId") @JvmField val messageId: String,
)
