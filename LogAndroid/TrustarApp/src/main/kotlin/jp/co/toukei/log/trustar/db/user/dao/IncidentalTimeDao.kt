package jp.co.toukei.log.trustar.db.user.dao

import androidx.room.Dao
import androidx.room.Query
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import jp.co.toukei.log.lib.room.CommonSyncDao
import jp.co.toukei.log.trustar.db.user.entity.IncidentalTime

@Dao
interface IncidentalTimeDao : CommonSyncDao<IncidentalTime> {

    @Query("select * from incidental_time")
    fun selectAll(): Observable<List<IncidentalTime>>

    @Query("delete from incidental_time")
    fun deleteAll()

    @Query("select * from incidental_time where deleted = 0 and header = :headerUUID")
    fun findByHeaderUUID(headerUUID: String): Flowable<List<IncidentalTime>>

    @Query("update incidental_time set sync = 1 where sync = 0")
    override fun setPending()

    @Query("select * from incidental_time where sync = 1")
    override fun getPending(): List<IncidentalTime>

    @Query("delete from incidental_time where sync = -1 or sync = 2")
    fun deleteSynced()
}
