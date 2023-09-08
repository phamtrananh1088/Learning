package jp.co.toukei.log.trustar.rest.model

import annotation.Keep
import annotation.Para
import jp.co.toukei.log.trustar.db.chat.Attachment
import jp.co.toukei.log.trustar.db.chat.Img
import jp.co.toukei.log.trustar.db.chat.entity.ChatMessage
import java.util.Locale

class MessageItem @Keep constructor(
    @Para("messageId") @JvmField val messageId: String,
    @Para("talkRoomId") @JvmField val talkRoomId: String,
    @Para("messageRowId") @JvmField val messageRowId: String,
    @Para("userId") @JvmField val userId: String,
    @Para("messageText") @JvmField val messageText: String?,
    @Para("messageClass") @JvmField val messageClass: String?,
    @Para("createdDatetime") @JvmField val createdDatetime: Long?,
    @Para("sentDatetime") @JvmField val sentDatetime: Long?,
    @Para("files") private val files: Array<F>?,
) {
    class F @Keep constructor(
        @Para("fileKey") @JvmField val fileKey: String,
        @Para("fileName") @JvmField val fileName: String?,
        @Para("fileSize") @JvmField val fileSize: Long?,

        //for image
        @Para("fileUrl") @JvmField val imgUrl: String,
        @Para("width") @JvmField val width: Int,
        @Para("height") @JvmField val height: Int,
    ) {
        fun asAttachment() = Attachment(fileKey, fileName ?: "", fileSize ?: 0L)
        fun asImg() = Img(asAttachment(), imgUrl, width, height)
    }

    fun message(): ChatMessage {
        val file = files?.firstOrNull()
        return when (messageClass?.lowercase(Locale.ROOT)) {
            "i" -> ChatMessage(
                messageId,
                talkRoomId,
                userId,
                file?.asImg()?.serializeJson()?.toString(),
                createdDatetime ?: 0,
                messageRowId,
                ChatMessage.TYPE_IMG
            )

            "v" -> ChatMessage(
                messageId,
                talkRoomId,
                userId,
                file?.asAttachment()?.serializeJson()?.toString(),
                createdDatetime ?: 0,
                messageRowId,
                ChatMessage.TYPE_VIDEO
            )

            "a" -> ChatMessage(
                messageId,
                talkRoomId,
                userId,
                file?.asAttachment()?.serializeJson()?.toString(),
                createdDatetime ?: 0,
                messageRowId,
                ChatMessage.TYPE_AUDIO
            )

            "f" -> ChatMessage(
                messageId,
                talkRoomId,
                userId,
                file?.asAttachment()?.serializeJson()?.toString(),
                createdDatetime ?: 0,
                messageRowId,
                ChatMessage.TYPE_FILE
            )

            else -> ChatMessage(
                messageId,
                talkRoomId,
                userId,
                messageText,
                createdDatetime ?: 0,
                messageRowId,
                ChatMessage.TYPE_TEXT
            )
        }
    }
}
