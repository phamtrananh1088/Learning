package jp.co.toukei.log.trustar.db.chat

import androidx.room.Dao
import androidx.room.Query
import jp.co.toukei.log.lib.room.CommonDao
import jp.co.toukei.log.trustar.db.chat.entity.ChatUser

@Dao
interface ChatUserDao : CommonDao<ChatUser> {

    @Query("delete from chat_user")
    fun deleteAll()
}
