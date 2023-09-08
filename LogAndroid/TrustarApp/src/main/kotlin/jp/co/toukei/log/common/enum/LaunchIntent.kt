package jp.co.toukei.log.common.enum

import android.content.Context
import androidx.core.net.toUri
import jp.co.toukei.log.lib.email
import jp.co.toukei.log.lib.makeDial
import jp.co.toukei.log.lib.openMap
import jp.co.toukei.log.lib.openUri

enum class LaunchIntent {
    Email, Dial, Map, Url
}

fun LaunchIntent.open(context: Context, value: String?) {
    if (value.isNullOrEmpty()) return
    when (this) {
        LaunchIntent.Email -> context.email(value)
        LaunchIntent.Dial -> context.makeDial(value)
        LaunchIntent.Map -> context.openMap(value)
        LaunchIntent.Url -> context.openUri(value.toUri())
    }
}
