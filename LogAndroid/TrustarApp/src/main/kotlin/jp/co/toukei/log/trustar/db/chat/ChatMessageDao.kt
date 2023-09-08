package jp.co.toukei.log.trustar.db.chat

import androidx.room.Dao
import androidx.room.Query
import jp.co.toukei.log.lib.room.CommonDao
import jp.co.toukei.log.trustar.db.chat.entity.ChatMessage

@Dao
interface ChatMessageDao : CommonDao<ChatMessage> {

    @Query("select * from chat_message where room_id = :roomId order by message_row limit :size")
    fun messageOldest(roomId: String, size: Int): List<ChatMessage>

    @Query("select * from chat_message where room_id = :roomId order by message_row desc limit :size")
    fun messageLatest(roomId: String, size: Int): List<ChatMessage>

    @Query("select * from chat_message where room_id = :roomId and message_row < :messageRow order by message_row desc limit :size")
    fun messagePrev(roomId: String, size: Int, messageRow: String): List<ChatMessage>

    @Query("select * from chat_message where room_id = :roomId and message_row > :messageRow order by message_row limit :size")
    fun messageNext(roomId: String, size: Int, messageRow: String): List<ChatMessage>

    @Query("delete from chat_message where id in (:ids)")
    fun deleteById(ids: List<String>)

    @Query("delete from chat_message where room_id = :roomId ")
    fun deleteAll(roomId: String)

    @Query("select * from chat_message where room_id = :roomId and message_row between :startR and :endR")
    fun messageBetween(roomId: String, startR: String, endR: String): List<ChatMessage>
}
