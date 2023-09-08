package jp.co.toukei.log.trustar.chat.ui

import jp.co.toukei.log.lib.util.Progress
import jp.co.toukei.log.trustar.db.chat.AttachmentExt

interface Dl {

    fun boundAttachmentExt(): AttachmentExt?

    fun setDownloadProgress(progress: Progress?)
}
