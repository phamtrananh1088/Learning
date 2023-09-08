package jp.co.toukei.log.trustar.chat

import jp.co.toukei.log.trustar.db.chat.entity.ChatRoom
import jp.co.toukei.log.trustar.db.chat.entity.ChatUser

class ChatRoomWithUsers(
    @JvmField val room: ChatRoom,
    @JvmField val users: List<ChatUser>,
)
