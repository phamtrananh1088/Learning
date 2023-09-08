package jp.co.toukei.log.trustar.chat

import jp.co.toukei.log.trustar.db.chat.entity.ChatRoom
import jp.co.toukei.log.trustar.db.chat.entity.ChatUserWithReadRow

class ChatRoomWithUsers2(
    @JvmField val room: ChatRoom,
    @JvmField val users: List<ChatUserWithReadRow>,
)
