package jp.co.toukei.log.trustar.db.chat

import androidx.room.Dao
import androidx.room.Query
import io.reactivex.rxjava3.core.Flowable
import jp.co.toukei.log.lib.room.CommonDao
import jp.co.toukei.log.trustar.db.chat.entity.ChatMessagePending
import jp.co.toukei.log.trustar.db.chat.entity.StringId
import java.util.Optional

@Dao
interface ChatMessagePendingDao : CommonDao<ChatMessagePending> {

    @Query("select * from chat_message_pending where room_id = :roomId order by created_date")
    fun messageByRoom(roomId: String): List<ChatMessagePending>

    @Query("select * from chat_message_pending where room_id= :roomId and status < 2 order by created_date limit 1")
    fun firstUnsentOrSending(roomId: String): Optional<ChatMessagePending>

    @Query("select id from (select min(created_date), room_id id from chat_message_pending where status < 2 group by room_id)")
    fun unsentOrSendingRoomIds(): Flowable<List<StringId>>

    @Query("select id from (select min(created_date), room_id id from chat_message_pending where status < 2 group by room_id having status = 0)")
    fun unsentRoomIdList(): List<StringId>

    @Query("update chat_message_pending set status = :status where id = :id")
    fun setStatus(status: Int, id: Long)

    @Query("update chat_message_pending set status = 0 where status = 2 and room_id = :roomId")
    fun resetErr(roomId: String)

    @Query("update chat_message_pending set status = :status, target_id = :targetId where id = :id")
    fun setStatusAndTargetId(status: Int, targetId: String?, id: Long)

    @Query("delete from chat_message_pending where status = 3 and room_id = :roomId")
    fun deleteSent(roomId: String)

    @Query("select * from chat_message_pending where room_id = :roomId and created_date < :createdDate")
    fun messagePrev(roomId: String, createdDate: Long): List<ChatMessagePending>

    @Query("select * from chat_message_pending where room_id = :roomId and created_date <= :createdDate")
    fun messagePrevIncluded(roomId: String, createdDate: Long): List<ChatMessagePending>

    @Query("select * from chat_message_pending where room_id = :roomId and created_date > :createdDate")
    fun messageNext(roomId: String, createdDate: Long): List<ChatMessagePending>

    @Query("select * from chat_message_pending where status = 3 and room_id = :roomId")
    fun selectSent(roomId: String): List<ChatMessagePending>

    @Query("select * from chat_message_pending where id = :id")
    fun selectById(id: Long): ChatMessagePending?
}
