package jp.co.toukei.log.trustar.chat

import android.graphics.Color
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import androidx.core.text.buildSpannedString
import androidx.recyclerview.widget.DiffUtil
import jp.co.toukei.log.lib.ctx.Ctx
import jp.co.toukei.log.lib.millisAfterOffset
import jp.co.toukei.log.lib.sameClass
import jp.co.toukei.log.lib.unixTimeDaysBetween
import jp.co.toukei.log.lib.util.CompareContent
import jp.co.toukei.log.trustar.Config
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.db.chat.AttachmentExt
import jp.co.toukei.log.trustar.db.chat.entity.ChatMessage
import jp.co.toukei.log.trustar.db.chat.entity.ChatMessagePending
import jp.co.toukei.log.trustar.db.chat.entity.ChatUser
import jp.co.toukei.log.trustar.glide.ImageURI
import third.Clock

sealed class MessageItem(
    @JvmField val listId: String?,
    @JvmField val dbText: String,
    @JvmField val self: Boolean,
    @JvmField val sentDate: Long,

    @JvmField val status: CharSequence?,
    @JvmField val hasPrev: Boolean,
    @JvmField val hasNext: Boolean,
) : CompareContent<MessageItem> {

    @JvmField
    val dateStr = if (Config.timeZone.unixTimeDaysBetween(System.currentTimeMillis(), sentDate) == 0) {
        Clock(Config.timeZone.millisAfterOffset(sentDate)).hhmm
    } else {
        Config.mmddFormatter.format(sentDate)
    }

    companion object {
        const val PAYLOAD_CONTENT = 0b1
        const val PAYLOAD_STATUS = 0b10
        const val PAYLOAD_DATE = 0b100

        fun hasPayload(type: Int, payload: Int): Boolean {
            return type and payload == type
        }
    }

    /** @see [DiffUtil.ItemCallback.areContentsTheSame] */
    override fun sameContent(other: MessageItem): Boolean {
        return other.dbText == dbText &&
                other.hasPrev == hasPrev &&
                other.hasNext == hasNext &&
                other.status == status &&
                other.self == self &&
                other.dateStr == dateStr
    }

    /** @see [DiffUtil.ItemCallback.getChangePayload] */
    fun getPayload(other: MessageItem): Int? {
        var l = 0
        if (other.dbText != dbText) {
            l = l or PAYLOAD_CONTENT
        }
        if (other.status != status) {
            l = l or PAYLOAD_STATUS
        }
        if (other.dateStr != dateStr) {
            l = l or PAYLOAD_DATE
        }
        return if (l == 0) null else l
    }

    interface Text {
        val text: CharSequence?
    }

    interface Dl {
        fun attachmentExt(): AttachmentExt?
    }

    interface Image {
        val imageUri: ImageURI?
        val ratio: Float
    }

    interface Video : Dl {

    }

    interface Audio : Dl {

    }

    interface F : Dl {

    }

    sealed class Pending(
        @JvmField val pending: ChatMessagePending,
        hasPrev: Boolean,
        hasNext: Boolean,
    ) : MessageItem(pending.targetId,
        pending.text,
        true,
        pending.createdDate,
        when (pending.status) {
            ChatMessagePending.STATE_SENDING -> Ctx.context.getString(R.string.msg_sending)
            ChatMessagePending.STATE_ERR -> buildSpannedString {
                append(Ctx.context.getString(R.string.msg_tap_to_retry))
                setSpan(
                    ForegroundColorSpan(Color.RED),
                    0,
                    length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }

            else -> null
        },
        hasPrev,
        hasNext) {

        val isSending: Boolean
            get() = pending.status == ChatMessagePending.STATE_SENDING

        override fun sameContent(other: MessageItem): Boolean {
            return other is Pending && sameClass(other) && super.sameContent(other)
        }

        @JvmField
        val attachmentExt = ChatMessagePending.attachmentExt(pending)

        class ImgPending(
            pending: ChatMessagePending,
            hasPrev: Boolean,
            hasNext: Boolean,
        ) : Pending(pending, hasPrev, hasNext), Image {
            override val imageUri: ImageURI?
                get() = attachmentExt?.file?.let { ImageURI(it.canonicalPath, false) }
            override val ratio: Float = ChatMessagePending.textAsImg(pending)?.ratio ?: 1F
        }

        class AudioPending(
            pending: ChatMessagePending,
            hasPrev: Boolean,
            hasNext: Boolean,
        ) : Pending(pending, hasPrev, hasNext), Audio {
            override fun attachmentExt(): AttachmentExt? = attachmentExt
        }

        class TextPending(
            pending: ChatMessagePending,
            hasPrev: Boolean,
            hasNext: Boolean,
        ) : Pending(pending, hasPrev, hasNext), Text {
            override val text = pending.text
        }

        class VideoPending(
            pending: ChatMessagePending,
            hasPrev: Boolean,
            hasNext: Boolean,
        ) : Pending(pending, hasPrev, hasNext), Video {
            override fun attachmentExt(): AttachmentExt? = attachmentExt
        }

        class FilePending(
            pending: ChatMessagePending,
            hasPrev: Boolean,
            hasNext: Boolean,
        ) : Pending(pending, hasPrev, hasNext), F {
            override fun attachmentExt(): AttachmentExt? = attachmentExt
        }
    }

    sealed class Sent(
        @JvmField val msg: ChatMessage,
        user: ChatUser?,
        readers: List<ChatUser?>?,
        hasPrev: Boolean,
        hasNext: Boolean,
    ) : MessageItem(
        msg.id,
        msg.text.orEmpty(),
        msg.isSelf,
        msg.createdDate,
        if (readers.isNullOrEmpty()) null else {
            val c = readers.count { it != null }
            if (c == 0) null else buildString {
                append(Ctx.context.getString(R.string.msg_been_read))
                if (c != readers.count()) append(c) //everybody.
            }
        },
        hasPrev, hasNext
    ) {

        @JvmField
        val avatar = user?.avatarUri

        @JvmField
        val username = user?.name

        @JvmField
        val readers = readers?.mapNotNull { it }

        override fun sameContent(other: MessageItem): Boolean {
            return other is Sent && sameClass(other) && super.sameContent(other)
        }

        protected val attachmentExt = ChatMessage.attachmentFile(msg)

        class ImgSent(
            msg: ChatMessage,
            user: ChatUser?,
            readers: List<ChatUser?>?,
            hasPrev: Boolean,
            hasNext: Boolean,
        ) : Sent(msg, user, readers, hasPrev, hasNext), Image {
            private val img = ChatMessage.textAsImg(msg)

            override val imageUri: ImageURI?
                get() = img?.let { ImageURI(it.url , true) }
            override val ratio: Float
                get() = img?.ratio ?: 1F
        }

        class VideoSent(
            msg: ChatMessage,
            user: ChatUser?,
            readers: List<ChatUser?>?,
            hasPrev: Boolean,
            hasNext: Boolean,
        ) : Sent(msg, user, readers, hasPrev, hasNext), Video {
            override fun attachmentExt(): AttachmentExt? {
                return attachmentExt
            }
        }

        class AudioSent(
            msg: ChatMessage,
            user: ChatUser?,
            readers: List<ChatUser?>?,
            hasPrev: Boolean,
            hasNext: Boolean,
        ) : Sent(msg, user, readers, hasPrev, hasNext), Audio {
            override fun attachmentExt(): AttachmentExt? = attachmentExt
        }

        class TextSent(
            msg: ChatMessage,
            user: ChatUser?,
            readers: List<ChatUser?>?,
            hasPrev: Boolean,
            hasNext: Boolean,
        ) : Sent(msg, user, readers, hasPrev, hasNext), Text {
            override val text: CharSequence? = msg.text
//                ?.let {
//                    SpannableStringBuilder(it).apply {
//                        Config.dateRegex.findAll(it).forEach { (range, date) ->
//                            val ss = listOf(
//                                object : ClickableSpan() {
//                                    override fun onClick(widget: View) {
//                                        Main.startActivityForSchedule(widget.context, date)
//                                    }
//                                },
//                                StyleSpan(Typeface.BOLD),
//                                ForegroundColorSpan(0xff0000EE.toInt())
//                            )
//                            ss.forEach {
//                                setSpan(
//                                    it,
//                                    range.first,
//                                    range.last + 1,
//                                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//                                )
//                            }
//                        }
//                    }
//                }
        }

        class FileSent(
            msg: ChatMessage,
            user: ChatUser?,
            readers: List<ChatUser?>?,
            hasPrev: Boolean,
            hasNext: Boolean,
        ) : Sent(msg, user, readers, hasPrev, hasNext), F {

            override fun attachmentExt(): AttachmentExt? {
                return attachmentExt
            }
        }
    }
}
