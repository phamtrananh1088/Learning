package jp.co.toukei.log.trustar.db.chat.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded

class ChatUserWithReadRow(
    @Embedded @JvmField val chatUser: ChatUser,
    @ColumnInfo(name = "last_row") @JvmField val lastRow: String?,
)
