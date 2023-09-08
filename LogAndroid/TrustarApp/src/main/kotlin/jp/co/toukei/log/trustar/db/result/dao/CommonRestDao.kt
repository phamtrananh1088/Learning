package jp.co.toukei.log.trustar.db.result.dao

import androidx.room.Dao
import androidx.room.Query
import jp.co.toukei.log.lib.room.CommonSyncDao
import jp.co.toukei.log.lib.room.SyncChanged
import jp.co.toukei.log.lib.room.SyncDone
import jp.co.toukei.log.lib.room.SyncPending
import jp.co.toukei.log.trustar.db.result.entity.CommonRest

@Dao
interface CommonRestDao : CommonSyncDao<CommonRest> {

    @Query("update rest set sync = $SyncPending where sync = $SyncChanged")
    override fun setPending()

    @Query("select * from rest where sync = $SyncPending")
    override fun getPending(): List<CommonRest>

    @Query("delete from rest where sync = $SyncDone")
    override fun deleteSynced()

}
