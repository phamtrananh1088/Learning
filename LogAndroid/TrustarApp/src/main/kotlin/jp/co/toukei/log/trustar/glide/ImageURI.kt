package jp.co.toukei.log.trustar.glide

import androidx.core.net.toUri

class ImageURI(
    @JvmField val uriStr: String,
    @JvmField val handle: Boolean,
) {
    val uri get() = uriStr.toUri()
}
