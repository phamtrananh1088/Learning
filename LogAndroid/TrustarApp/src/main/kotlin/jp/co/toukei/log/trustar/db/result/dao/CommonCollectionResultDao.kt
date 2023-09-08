package jp.co.toukei.log.trustar.db.result.dao

import androidx.room.Dao
import androidx.room.Query
import jp.co.toukei.log.lib.room.CommonSyncDao
import jp.co.toukei.log.lib.room.SyncChanged
import jp.co.toukei.log.lib.room.SyncDone
import jp.co.toukei.log.lib.room.SyncPending
import jp.co.toukei.log.trustar.db.result.entity.CommonCollectionResult

@Dao
interface CommonCollectionResultDao : CommonSyncDao<CommonCollectionResult> {

    @Query("update collection_result set sync = $SyncPending where sync = $SyncChanged")
    override fun setPending()

    @Query("select * from collection_result where sync = $SyncPending")
    override fun getPending(): List<CommonCollectionResult>

    @Query("delete from collection_result where sync = $SyncDone")
    override fun deleteSynced()

}
