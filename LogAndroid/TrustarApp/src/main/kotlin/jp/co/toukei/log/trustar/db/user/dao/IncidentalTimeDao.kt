package jp.co.toukei.log.trustar.db.user.dao

import androidx.room.Dao
import androidx.room.Query
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import jp.co.toukei.log.lib.room.CommonSyncDao
import jp.co.toukei.log.lib.room.SyncChanged
import jp.co.toukei.log.lib.room.SyncDone
import jp.co.toukei.log.lib.room.SyncPending
import jp.co.toukei.log.trustar.db.user.entity.IncidentalTime

@Dao
interface IncidentalTimeDao : CommonSyncDao<IncidentalTime> {

    @Query("select * from incidental_time")
    fun selectAll(): Observable<List<IncidentalTime>>

    @Query("delete from incidental_time")
    fun deleteAll()

    @Query("select * from incidental_time where deleted = 0 and header = :headerUUID")
    fun findByHeaderUUID(headerUUID: String): Flowable<List<IncidentalTime>>

    @Query("update incidental_time set sync = $SyncPending where sync = $SyncChanged")
    override fun setPending()

    @Query("select * from incidental_time where sync = $SyncPending")
    override fun getPending(): List<IncidentalTime>

    @Query("delete from incidental_time where sync = $SyncDone")
    override fun deleteSynced()
}
