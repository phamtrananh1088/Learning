package jp.co.toukei.log.trustar.db.chat.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "chat_room_user",
    primaryKeys = ["room_id", "user_id"],
    foreignKeys = [
        ForeignKey(
            entity = ChatRoom::class,
            parentColumns = ["id"],
            childColumns = ["room_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
class ChatRoomUser(
    @ColumnInfo(name = "room_id") @JvmField val roomId: String,
    @ColumnInfo(name = "user_id") @JvmField val userId: String,
    @ColumnInfo(name = "read_message_row") @JvmField val readMessageRowId: String?,
)
