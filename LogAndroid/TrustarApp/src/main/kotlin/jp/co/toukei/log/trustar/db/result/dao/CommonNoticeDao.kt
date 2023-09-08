package jp.co.toukei.log.trustar.db.result.dao

import androidx.room.Dao
import androidx.room.Query
import jp.co.toukei.log.lib.room.CommonSyncDao
import jp.co.toukei.log.lib.room.SyncChanged
import jp.co.toukei.log.lib.room.SyncDone
import jp.co.toukei.log.lib.room.SyncPending
import jp.co.toukei.log.trustar.db.result.entity.CommonNotice

@Dao
interface CommonNoticeDao : CommonSyncDao<CommonNotice> {

    @Query("update notice set sync = $SyncPending where sync = $SyncChanged")
    override fun setPending()

    @Query("select * from notice where sync = $SyncPending")
    override fun getPending(): List<CommonNotice>

    @Query("delete from notice where sync = $SyncDone")
    override fun deleteSynced()

}
