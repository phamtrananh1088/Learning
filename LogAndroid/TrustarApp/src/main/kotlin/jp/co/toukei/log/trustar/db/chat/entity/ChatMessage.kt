package jp.co.toukei.log.trustar.db.chat.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import jp.co.toukei.log.lib.tryFromJSON
import jp.co.toukei.log.trustar.Config
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.db.chat.Attachment
import jp.co.toukei.log.trustar.db.chat.AttachmentExt
import jp.co.toukei.log.trustar.db.chat.Img
import jp.co.toukei.log.trustar.user.UserInfo

@Entity(
    tableName = ChatMessage.TABLE_NAME,
    indices = [Index(value = ["message_row", "room_id"], unique = true)],
    foreignKeys = [
        ForeignKey(
            entity = ChatRoom::class,
            parentColumns = ["id"],
            childColumns = ["room_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
class ChatMessage(
    @ColumnInfo(name = "id") @PrimaryKey @JvmField val id: String,
    @ColumnInfo(name = "room_id", index = true) @JvmField val roomId: String,
    @ColumnInfo(name = "user_id") @JvmField val userId: String,
    @ColumnInfo(name = "text") @JvmField val text: String?,
    @ColumnInfo(name = "created_date") @JvmField val createdDate: Long,

    @ColumnInfo(name = "message_row", index = true) @JvmField val messageRow: String,
    @ColumnInfo(name = "type") @JvmField val type: Int, //0:text, 1:img, 2:audio, 3:video, 4:file
) {
    @Ignore
    @JvmField
    val isSelf = userId == Current.userId

    companion object {

        const val TYPE_TEXT = 0
        const val TYPE_IMG = 1
        const val TYPE_AUDIO = 2
        const val TYPE_VIDEO = 3
        const val TYPE_FILE = 4

        const val TABLE_NAME = "chat_message"

        @JvmStatic
        fun textAsAttachment(message: ChatMessage): Attachment? {
            return message.text?.let {
                Config.commonMoshi.tryFromJSON<Attachment>(it)
            }
        }

        @JvmStatic
        fun textAsImg(message: ChatMessage): Img? {
            return message.text?.let {
                Config.commonMoshi.tryFromJSON<Img>(it)
            }
        }

        @JvmStatic
        fun attachment(message: ChatMessage): Attachment? {
            return when (message.type) {
                TYPE_IMG -> textAsImg(message)?.attachment
                TYPE_AUDIO, TYPE_VIDEO, TYPE_FILE -> textAsAttachment(message)
                else -> null
            }
        }

        @JvmStatic
        fun attachmentFile(message: ChatMessage): AttachmentExt? {
            return attachment(message)?.let {
                AttachmentExt(it, UserInfo.cacheFileByKey(Current.userInfo, it.key).file, true)
            }
        }
    }
}
