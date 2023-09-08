package jp.co.toukei.log.trustar.db.result.dao

import androidx.room.Dao
import androidx.room.Query
import jp.co.toukei.log.lib.room.CommonSyncDao
import jp.co.toukei.log.lib.room.SyncChanged
import jp.co.toukei.log.lib.room.SyncDone
import jp.co.toukei.log.lib.room.SyncPending
import jp.co.toukei.log.trustar.db.result.entity.CommonIncidentalTimeResult

@Dao
interface CommonIncidentalTimeResultDao : CommonSyncDao<CommonIncidentalTimeResult> {

    @Query("update incidental_time_result set sync = $SyncPending where sync = $SyncChanged")
    override fun setPending()

    @Query("select * from incidental_time_result where sync = $SyncPending")
    override fun getPending(): List<CommonIncidentalTimeResult>

    @Query("delete from incidental_time_result where sync = $SyncDone")
    override fun deleteSynced()

}
