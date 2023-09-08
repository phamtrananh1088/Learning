package jp.co.toukei.log.trustar.db.chat.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_room")
class ChatRoom(
    @ColumnInfo(name = "id") @PrimaryKey @JvmField val id: String,
    @ColumnInfo(name = "name") @JvmField val name: String?,
    @ColumnInfo(name = "room_image") @JvmField val image: String?,
    @ColumnInfo(name = "unread") @JvmField val unread: Int,
    @ColumnInfo(name = "user_count") @JvmField val userCount: Int,
    @ColumnInfo(name = "last_updated") @JvmField val lastUpdated: Long,
    @ColumnInfo(name = "notification") @JvmField val notification: Boolean,
    @ColumnInfo(name = "version", index = true) @JvmField val version: String,
)
