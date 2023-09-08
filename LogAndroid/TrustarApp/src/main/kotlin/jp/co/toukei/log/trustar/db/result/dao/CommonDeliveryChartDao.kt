package jp.co.toukei.log.trustar.db.result.dao

import androidx.room.Dao
import androidx.room.Query
import jp.co.toukei.log.lib.room.CommonSyncDao
import jp.co.toukei.log.lib.room.SyncChanged
import jp.co.toukei.log.lib.room.SyncDone
import jp.co.toukei.log.lib.room.SyncPending
import jp.co.toukei.log.trustar.db.result.entity.CommonDeliveryChart

@Dao
interface CommonDeliveryChartDao : CommonSyncDao<CommonDeliveryChart> {

    @Query("update delivery_chart set sync = $SyncPending where sync = $SyncChanged")
    override fun setPending()

    @Query("select * from delivery_chart where sync = $SyncPending")
    override fun getPending(): List<CommonDeliveryChart>

    @Query("select * from delivery_chart where sync = $SyncPending limit 1")
    fun getAnyPending(): CommonDeliveryChart?

    @Query("delete from delivery_chart where sync = $SyncDone")
    override fun deleteSynced()

}
