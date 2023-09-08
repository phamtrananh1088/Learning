package jp.co.toukei.log.trustar.db.chat

import androidx.room.Dao
import androidx.room.Query
import io.reactivex.rxjava3.core.Flowable
import jp.co.toukei.log.lib.room.CommonDao
import jp.co.toukei.log.trustar.db.chat.entity.ChatRoom
import jp.co.toukei.log.trustar.db.chat.entity.ChatRoomExt
import java.util.Optional

@Dao
interface ChatRoomDao : CommonDao<ChatRoom> {

    @Query("select * from chat_room order by last_updated desc")
    fun selectRoomList(): Flowable<List<ChatRoomExt>>

    @Query("select * from chat_room where id = :roomId")
    fun selectRoom(roomId: String): Flowable<Optional<ChatRoom>>

    @Query("delete from chat_room")
    fun deleteAll()

    @Query("delete from chat_room where version != :version")
    fun deleteNot(version: String)

    @Query("delete from chat_room where id = :roomId")
    fun deleteByRoomId(roomId: String)
}
