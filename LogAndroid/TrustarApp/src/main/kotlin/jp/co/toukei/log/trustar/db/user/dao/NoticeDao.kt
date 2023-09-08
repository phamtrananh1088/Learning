package jp.co.toukei.log.trustar.db.user.dao

import androidx.room.Dao
import androidx.room.Query
import io.reactivex.rxjava3.core.Flowable
import jp.co.toukei.log.lib.room.CommonSyncDao
import jp.co.toukei.log.lib.room.SyncChanged
import jp.co.toukei.log.lib.room.SyncDone
import jp.co.toukei.log.lib.room.SyncPending
import jp.co.toukei.log.trustar.db.user.entity.Notice

@Dao
interface NoticeDao : CommonSyncDao<Notice> {

    @Query(
        """
        update notice
        set unread_flag = 0, read_datetime = :readDate, sync = $SyncChanged
        where event_rank = :rank and unread_flag = 1
        """
    )
    fun markAllReadByRank(rank: Int, readDate: Long)

    @Query("update notice set sync = $SyncPending where sync = $SyncChanged and unread_flag = 0")
    override fun setPending()

    @Query("select * from notice where sync = $SyncPending")
    override fun getPending(): List<Notice>

    @Query("delete from notice where sync = $SyncDone")
    override fun deleteSynced()

    @Query("select count(record_id) from notice where event_rank = :rank and unread_flag = 1")
    fun countUnreadByRank(rank: Int): Flowable<Int>

    @Query("select * from notice where event_rank = :rank and unread_flag = 1 order by publication_date_from desc")
    fun unreadByRank(rank: Int): Flowable<List<Notice>>

    @Query("select * from notice where event_rank = :rank order by publication_date_from desc")
    fun selectByRank(rank: Int): Flowable<List<Notice>>

    @Query("delete from notice")
    fun deleteAll()
}
