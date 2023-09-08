package jp.co.toukei.log.trustar.db.result.dao

import androidx.room.Dao
import androidx.room.Query
import jp.co.toukei.log.lib.room.CommonSyncDao
import jp.co.toukei.log.lib.room.SyncChanged
import jp.co.toukei.log.lib.room.SyncDone
import jp.co.toukei.log.lib.room.SyncPending
import jp.co.toukei.log.trustar.db.result.entity.CommonCoordinate

@Dao
interface CommonCoordinateDao : CommonSyncDao<CommonCoordinate> {

    @Query("update coordinate set sync = $SyncPending where sync = $SyncChanged")
    override fun setPending()

    @Query("select * from coordinate where sync = $SyncPending")
    override fun getPending(): List<CommonCoordinate>

    @Query("delete from coordinate where sync = $SyncDone")
    override fun deleteSynced()

}
