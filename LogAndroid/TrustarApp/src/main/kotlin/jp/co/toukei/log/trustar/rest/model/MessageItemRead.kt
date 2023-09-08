package jp.co.toukei.log.trustar.rest.model

import annotation.Keep
import annotation.Para

class MessageItemRead @Keep constructor(
    @Para("messageRowId") @JvmField val messageRowId: String,
    @Para("readers") @JvmField val readers: Array<U>?,
) {
    class U @Keep constructor(
        @Para("userId") @JvmField val userId: String,
    )
}
