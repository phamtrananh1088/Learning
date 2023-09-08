package jp.co.toukei.log.trustar.db.user.dao

import androidx.room.Dao
import androidx.room.Query
import io.reactivex.rxjava3.core.Flowable
import jp.co.toukei.log.lib.room.CommonSyncDao
import jp.co.toukei.log.lib.room.SyncChanged
import jp.co.toukei.log.lib.room.SyncDone
import jp.co.toukei.log.lib.room.SyncPending
import jp.co.toukei.log.trustar.db.user.entity.CollectionResult
import java.util.Optional

@Dao
interface CollectionResultDao : CommonSyncDao<CollectionResult> {

    @Query("update collection_result set sync = $SyncPending where sync = $SyncChanged")
    override fun setPending()

    @Query("select * from collection_result where sync = $SyncPending")
    override fun getPending(): List<CollectionResult>

    @Query("delete from collection_result where sync = $SyncDone")
    override fun deleteSynced()

    @Query("select * from collection_result where allocation_no = :allocationNo and allocation_row_no = :allocationRowNo")
    fun getResult(allocationNo: String, allocationRowNo: Int): Flowable<Optional<CollectionResult>>

    @Query("select * from collection_result where sync = $SyncDone")
    fun safeToDeleteList(): List<CollectionResult>
}
