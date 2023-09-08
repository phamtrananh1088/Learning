package jp.co.toukei.log.lib.retrofit

import annotation.Keep
import annotation.Para

class DefaultAPIException @Keep constructor(
    @Para("Message") @JvmField val msg: String,
) : RuntimeException(msg)
