package jp.co.toukei.log.trustar.db.chat

import android.net.Uri
import jp.co.toukei.log.trustar.cacheFileShareUri
import jp.co.toukei.log.trustar.localFileShareUri
import java.io.File

open class AttachmentExt(
    attachment: Attachment,
    @JvmField val file: File,
    @JvmField val isCacheFile: Boolean,
) : Attachment(attachment.key, attachment.name, attachment.size) {

    fun contentUri(): Uri {
        return if (isCacheFile) {
            cacheFileShareUri(key, name)
        } else {
            localFileShareUri(key, name)
        }
    }
}
