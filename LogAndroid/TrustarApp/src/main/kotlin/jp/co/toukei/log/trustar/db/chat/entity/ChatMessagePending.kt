package jp.co.toukei.log.trustar.db.chat.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import jp.co.toukei.log.lib.child
import jp.co.toukei.log.lib.tryFromJSON
import jp.co.toukei.log.lib.util.RequestBodyJson
import jp.co.toukei.log.trustar.Config
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.currentUserInfoClient
import jp.co.toukei.log.trustar.db.chat.Attachment
import jp.co.toukei.log.trustar.db.chat.AttachmentExt
import jp.co.toukei.log.trustar.db.chat.Img
import jp.co.toukei.log.trustar.user.UserInfo
import org.json.JSONObject
import third.jsonObj

@Entity(
    tableName = ChatMessagePending.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = ChatRoom::class,
            parentColumns = ["id"],
            childColumns = ["room_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
class ChatMessagePending(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) @JvmField val id: Long,
    @ColumnInfo(name = "created_date") @JvmField val createdDate: Long,
    @ColumnInfo(name = "status") @JvmField val status: Int,
    @ColumnInfo(name = "room_id", index = true) @JvmField val roomId: String,
    @ColumnInfo(name = "user_id") @JvmField val userId: String,
    @ColumnInfo(name = "text") @JvmField val text: String,
    @ColumnInfo(name = "target_id") @JvmField val targetId: String?,
    @ColumnInfo(name = "type") @JvmField val type: Int,//0:text, 1:send_img, 2:img, 3:send_audio, 4:audio, 5: send_video, 6: video
    @ColumnInfo(name = "ext") @JvmField val ext: String?,
) : RequestBodyJson {

    override fun jsonBody(): JSONObject {
        val u = currentUserInfoClient()
        return when (type) {
            TYPE_IMG_SEND, TYPE_AUDIO_SEND, TYPE_VIDEO_SEND, TYPE_FILE_SEND -> jsonObj {}
            TYPE_IMG -> jsonObj {
                "fileKey" v ext
                "messageItem" {
                    "identity" v id
                    "talkRoomId" v roomId
                    "userId" v userId
                    "messageClass" v "I"
                    "createdDatetime" v createdDate
                }
                "userInfo" v u
            }

            TYPE_AUDIO -> jsonObj {
                "fileKey" v ext
                "messageItem" {
                    "identity" v id
                    "talkRoomId" v roomId
                    "userId" v userId
                    "messageClass" v "A"
                    "createdDatetime" v createdDate
                }
                "userInfo" v u
            }

            TYPE_VIDEO -> jsonObj {
                "fileKey" v ext
                "messageItem" {
                    "identity" v id
                    "talkRoomId" v roomId
                    "userId" v userId
                    "messageClass" v "V"
                    "createdDatetime" v createdDate
                }
                "userInfo" v u
            }

            TYPE_FILE -> jsonObj {
                "fileKey" v ext
                "messageItem" {
                    "identity" v id
                    "talkRoomId" v roomId
                    "userId" v userId
                    "messageClass" v "F"
                    "createdDatetime" v createdDate
                }
                "userInfo" v u
            }

            else -> jsonObj {
                "messageItem" {
                    "identity" v id
                    "talkRoomId" v roomId
                    "userId" v userId
                    "messageText" v text
                    "messageClass" v "T"
                    "createdDatetime" v createdDate
                }
                "userInfo" v u
            }
        }
    }

    companion object {
        const val STATE_NORMAL = 0
        const val STATE_SENDING = 1
        const val STATE_ERR = 2
        const val STATE_SENT = 3

        const val TYPE_TEXT = 0
        const val TYPE_IMG_SEND = 1
        const val TYPE_IMG = 2
        const val TYPE_AUDIO_SEND = 3
        const val TYPE_AUDIO = 4
        const val TYPE_VIDEO_SEND = 5
        const val TYPE_VIDEO = 6
        const val TYPE_FILE_SEND = 7
        const val TYPE_FILE = 8

        const val TABLE_NAME = "chat_message_pending"

        @JvmStatic
        fun textAsAttachment(message: ChatMessagePending): Attachment? {
            return message.text.let {
                Config.commonMoshi.tryFromJSON<Attachment>(it)
            }
        }

        @JvmStatic
        fun textAsImg(message: ChatMessagePending): Img? {
            return message.text.let {
                Config.commonMoshi.tryFromJSON<Img>(it)
            }
        }

        @JvmStatic
        fun attachment(message: ChatMessagePending): Attachment? {
            return when (message.type) {
                TYPE_IMG_SEND, TYPE_IMG -> textAsImg(message)?.attachment
                else -> textAsAttachment(message)
            }
        }

        @JvmStatic
        fun attachmentExt(message: ChatMessagePending): AttachmentExt? {
            return attachment(message)?.let { att ->
                when (message.type) {
                    TYPE_IMG_SEND, TYPE_AUDIO_SEND, TYPE_VIDEO_SEND, TYPE_FILE_SEND -> {
                        val f = UserInfo.userLocalFileDir(Current.userInfo).child(att.key)
                        AttachmentExt(att, f, false)
                    }

                    TYPE_IMG, TYPE_AUDIO, TYPE_VIDEO, TYPE_FILE -> {
                        val f = UserInfo.cacheFileByKey(Current.userInfo, att.key).file
                        AttachmentExt(att, f, true)
                    }

                    else -> null
                }
            }
        }

        @JvmStatic
        private fun createMessage(roomId: String, type: Int, text: String): ChatMessagePending {
            return ChatMessagePending(
                0,
                System.currentTimeMillis(),
                STATE_NORMAL,
                roomId,
                Current.userId,
                text,
                null,
                type,
                null
            )
        }

        @JvmStatic
        fun textMessage(roomId: String, text: String): ChatMessagePending {
            return createMessage(roomId, TYPE_TEXT, text)
        }

        @JvmStatic
        fun imageMessage(roomId: String, localImg: Img): ChatMessagePending {
            return createMessage(roomId, TYPE_IMG_SEND, localImg.serializeJson().toString())
        }

        @JvmStatic
        fun audioMessage(roomId: String, localFile: Attachment): ChatMessagePending {
            return createMessage(roomId, TYPE_AUDIO_SEND, localFile.serializeJson().toString())
        }

        @JvmStatic
        fun videoMessage(roomId: String, localFile: Attachment): ChatMessagePending {
            return createMessage(roomId, TYPE_VIDEO_SEND, localFile.serializeJson().toString())
        }

        @JvmStatic
        fun fileMessage(roomId: String, localFile: Attachment): ChatMessagePending {
            return createMessage(roomId, TYPE_FILE_SEND, localFile.serializeJson().toString())
        }

        @JvmStatic
        private fun keyMessage(
            update: ChatMessagePending,
            type: Int,
            newText: String,
            key: String,
        ): ChatMessagePending {
            return ChatMessagePending(
                update.id,
                update.createdDate,
                STATE_NORMAL,
                update.roomId,
                update.userId,
                newText,
                null,
                type,
                key
            )
        }

        @JvmStatic
        fun imageKeyMessage(m: ChatMessagePending, key: String): ChatMessagePending {
            return keyMessage(m, TYPE_IMG, m.text, key)
        }

        @JvmStatic
        fun audioKeyMessage(m: ChatMessagePending, key: String): ChatMessagePending {
            return keyMessage(m, TYPE_AUDIO, m.text, key)
        }

        @JvmStatic
        fun videoKeyMessage(
            m: ChatMessagePending,
            cacheFile: Attachment,
            key: String,
        ): ChatMessagePending {
            return keyMessage(m, TYPE_VIDEO, cacheFile.serializeJson().toString(), key)
        }

        @JvmStatic
        fun fileKeyMessage(
            m: ChatMessagePending,
            cacheFile: Attachment,
            key: String,
        ): ChatMessagePending {
            return keyMessage(m, TYPE_FILE, cacheFile.serializeJson().toString(), key)
        }
    }
}
