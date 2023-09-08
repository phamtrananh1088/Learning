package jp.co.toukei.log.trustar.db.chat

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import jp.co.toukei.log.lib.room.Converter
import jp.co.toukei.log.trustar.db.chat.entity.ChatMessage
import jp.co.toukei.log.trustar.db.chat.entity.ChatMessagePending
import jp.co.toukei.log.trustar.db.chat.entity.ChatRoom
import jp.co.toukei.log.trustar.db.chat.entity.ChatRoomUser
import jp.co.toukei.log.trustar.db.chat.entity.ChatUser

@Database(
    entities = [
        ChatRoom::class,
        ChatUser::class,
        ChatRoomUser::class,
        ChatMessage::class,
        ChatMessagePending::class
    ],
    exportSchema = false,
    version = 2
)
@TypeConverters(Converter::class)
abstract class ChatDB : RoomDatabase() {

    abstract fun chatRoomDao(): ChatRoomDao
    abstract fun chatUserDao(): ChatUserDao
    abstract fun chatRoomUserDao(): ChatRoomUserDao
    abstract fun chatMessageDao(): ChatMessageDao
    abstract fun chatMessagePendingDao(): ChatMessagePendingDao
}
