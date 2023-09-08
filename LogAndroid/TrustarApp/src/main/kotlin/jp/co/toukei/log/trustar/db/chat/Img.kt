package jp.co.toukei.log.trustar.db.chat

import annotation.Keep
import annotation.Para
import org.json.JSONObject
import third.jsonObj

class Img @Keep constructor(
    @Para("attachment") @JvmField val attachment: Attachment,
    @Para("url") @JvmField val url: String,
    @Para("width") @JvmField val width: Int,
    @Para("height") @JvmField val height: Int,
) {

    @JvmField
    val ratio = if (width > 0 && height > 0) (width.toFloat() / height.toFloat()) else Float.NaN

    fun serializeJson(): JSONObject {
        return jsonObj {
            "attachment" v attachment.serializeJson()
            "url" v url
            "width" v width
            "height" v height
        }
    }
}
