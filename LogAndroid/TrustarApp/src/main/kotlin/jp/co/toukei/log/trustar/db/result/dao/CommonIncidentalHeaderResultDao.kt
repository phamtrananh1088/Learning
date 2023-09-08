package jp.co.toukei.log.trustar.db.result.dao

import androidx.room.Dao
import androidx.room.Query
import jp.co.toukei.log.lib.room.CommonSyncDao
import jp.co.toukei.log.lib.room.SyncChanged
import jp.co.toukei.log.lib.room.SyncDone
import jp.co.toukei.log.lib.room.SyncPending
import jp.co.toukei.log.trustar.db.result.entity.CommonIncidentalHeaderResult

@Dao
interface CommonIncidentalHeaderResultDao : CommonSyncDao<CommonIncidentalHeaderResult> {

    @Query("update incidental_header_result set sync = $SyncPending where sync = $SyncChanged")
    override fun setPending()

    @Query("select * from incidental_header_result where sync = $SyncPending")
    override fun getPending(): List<CommonIncidentalHeaderResult>

    @Query("delete from incidental_header_result where sync = $SyncDone")
    override fun deleteSynced()

}
