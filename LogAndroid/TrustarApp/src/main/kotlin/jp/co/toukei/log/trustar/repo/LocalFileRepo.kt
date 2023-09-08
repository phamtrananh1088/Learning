package jp.co.toukei.log.trustar.repo

import jp.co.toukei.log.lib.child
import jp.co.toukei.log.lib.moveOrCopy
import jp.co.toukei.log.trustar.db.chat.Attachment
import jp.co.toukei.log.trustar.db.chat.AttachmentExt
import jp.co.toukei.log.trustar.user.UserInfo
import java.io.File
import java.util.UUID

class LocalFileRepo(
    private val user: UserInfo,
) {

    fun saveLocalAttachment(tmp: File, name: String): AttachmentExt {
        val fileName = UUID.randomUUID().toString()
        val file = UserInfo.userLocalFileDir(user).child(fileName)
        if (!tmp.moveOrCopy(file)) throw Exception()
        val a = Attachment(fileName, name, file.length())
        return AttachmentExt(a, file, false)
    }

    inline fun <R> saveLocalAttachment(tmp: File, name: String, block: (AttachmentExt) -> R): R {
        val att = saveLocalAttachment(tmp, name)
        try {
            return block(att)
        } catch (e: Throwable) {
            att.file.delete()
            throw e
        }
    }
}
