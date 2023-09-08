package jp.co.toukei.log.trustar.db.user.dao

import androidx.room.Dao
import androidx.room.Query
import io.reactivex.rxjava3.core.Flowable
import jp.co.toukei.log.lib.room.CommonSyncDao
import jp.co.toukei.log.lib.room.SyncChanged
import jp.co.toukei.log.lib.room.SyncDone
import jp.co.toukei.log.lib.room.SyncPending
import jp.co.toukei.log.trustar.db.AllocationRow
import jp.co.toukei.log.trustar.db.user.entity.DeliveryChart
import java.util.Optional

@Dao
interface DeliveryChartDao : CommonSyncDao<DeliveryChart> {

    @Query("select * from delivery_chart where chart_cd = :chartCd limit 1")
    fun selectByChartCd(chartCd: String): Flowable<Optional<DeliveryChart>>

    @Query("select * from delivery_chart where place_cd = :placeCd limit 1")
    fun selectByPlaceCd(placeCd: String): Flowable<Optional<DeliveryChart>>

    @Query("select * from delivery_chart where last_allocation_row = :r limit 1")
    fun selectByAllocationRow(r: AllocationRow): Flowable<Optional<DeliveryChart>>

    @Query("select count(*) from delivery_chart")
    fun count(): Int

    @Query("update delivery_chart set sync = $SyncPending where sync = $SyncChanged")
    override fun setPending()

    @Query("select * from delivery_chart where sync = $SyncPending")
    override fun getPending(): List<DeliveryChart>

    @Query("delete from delivery_chart where sync = $SyncDone")
    override fun deleteSynced()

}
