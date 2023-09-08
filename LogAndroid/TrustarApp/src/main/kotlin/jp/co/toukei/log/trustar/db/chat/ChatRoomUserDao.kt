package jp.co.toukei.log.trustar.db.chat

import androidx.room.Dao
import androidx.room.Query
import io.reactivex.rxjava3.core.Flowable
import jp.co.toukei.log.lib.room.CommonDao
import jp.co.toukei.log.trustar.db.chat.entity.ChatRoomUser
import jp.co.toukei.log.trustar.db.chat.entity.ChatUser
import jp.co.toukei.log.trustar.db.chat.entity.ChatUserWithReadRow

@Dao
interface ChatRoomUserDao : CommonDao<ChatRoomUser> {

    @Query("select u.* from chat_user u where exists (select 1 from chat_room_user where room_id = :roomId and user_id = u.id)")
    fun userListByRoom(roomId: String): Flowable<List<ChatUser>>

    @Query("select u.*, r.read_message_row as last_row from chat_user u inner join chat_room_user r on r.user_id == u.id where r.room_id == :roomId")
    fun userListByRoomWithReadRow(roomId: String): Flowable<List<ChatUserWithReadRow>>

    @Query("select * from chat_room_user where room_id = :roomId")
    fun queryByRoom(roomId: String): List<ChatRoomUser>

    @Query("delete from chat_room_user")
    fun deleteAll()

//    @Query("select r.*, (select count(user_id) from chat_room_user where room_id = r.id) as db_user_count from chat_room r order by last_updated desc")
//    fun selectRoomList(): Flowable<List<ChatRoomExt>>
}
