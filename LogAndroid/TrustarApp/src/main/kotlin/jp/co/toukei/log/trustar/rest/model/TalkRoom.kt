package jp.co.toukei.log.trustar.rest.model

import annotation.Keep
import annotation.Para

class TalkRoom @Keep constructor(
    @Para("talkRoomId") @JvmField val talkRoomId: String,
    @Para("talkRoomName") @JvmField val talkRoomName: String?,
    @Para("talkRoomImageUrl") @JvmField val talkRoomImageUrl: String?,
    @Para("notificationFlag") @JvmField val notificationFlag: Boolean?,
    @Para("users") @JvmField val users: Array<TalkUser>?,
    @Para("readMessageItems") readMessageItems: Array<MessageItemRead>?,
    @Para("deletedMessageItems") @JvmField val deletedMessageItems: Array<MessageItemBase>?,
    @Para("userCount") @JvmField val userCount: Int?,
    @Para("unreadCount") @JvmField val unreadCount: Int?,
    @Para("lastUpdateDatetime") @JvmField val lastUpdateDatetime: Long?,
) {
    @JvmField
    val readRowsByUser = mutableMapOf<String, String>().apply {
        readMessageItems?.forEach { m ->
            m.readers?.forEach { u ->
                merge(u.userId, m.messageRowId) { t1, t2 -> if (t1 > t2) t1 else t2 }
            }
        }
    }
}
