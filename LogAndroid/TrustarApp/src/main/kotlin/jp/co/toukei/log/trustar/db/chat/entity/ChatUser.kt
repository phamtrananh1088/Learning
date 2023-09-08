package jp.co.toukei.log.trustar.db.chat.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import jp.co.toukei.log.lib.util.CompareItem
import jp.co.toukei.log.trustar.imageURI

@Entity(tableName = "chat_user")
class ChatUser(
    @ColumnInfo(name = "id") @PrimaryKey @JvmField val id: String,
    @ColumnInfo(name = "name") @JvmField val name: String?,
    @ColumnInfo(name = "mail") @JvmField val mail: String?,
    @ColumnInfo(name = "avatar") @JvmField val avatar: String?,
) : CompareItem<ChatUser> {

    @Ignore
    @JvmField
    val avatarUri = avatar?.imageURI()

    override fun sameItem(other: ChatUser): Boolean {
        return id == other.id
    }
}
