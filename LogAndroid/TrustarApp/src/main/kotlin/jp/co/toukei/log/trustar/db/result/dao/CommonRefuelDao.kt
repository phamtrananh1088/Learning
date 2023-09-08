package jp.co.toukei.log.trustar.db.result.dao

import androidx.room.Dao
import androidx.room.Query
import io.reactivex.rxjava3.core.Flowable
import jp.co.toukei.log.lib.room.CommonSyncDao
import jp.co.toukei.log.lib.room.SyncChanged
import jp.co.toukei.log.lib.room.SyncDone
import jp.co.toukei.log.lib.room.SyncPending
import jp.co.toukei.log.trustar.db.result.entity.CommonRefuel
import java.util.Optional

@Dao
interface CommonRefuelDao : CommonSyncDao<CommonRefuel> {

    @Query("update refuel set sync = $SyncPending where sync = $SyncChanged")
    override fun setPending()

    @Query("select * from refuel where sync = $SyncPending")
    override fun getPending(): List<CommonRefuel>

    @Query("delete from refuel where sync = $SyncDone")
    override fun deleteSynced()

    @Query("delete from refuel where sync = $SyncDone and input_datetime < :date")
    fun deleteSyncedBeforeDate(date: Long)


    @Query(
        """
        select sum(k.refueling_vol) from refuel k
        where k.company_cd = :companyCd and k.allocation_no = :allocationNo and k.fuelClass_cd = :fuelClassCd
    """
    )
    fun getTotalAmount(
        companyCd: String,
        allocationNo: String,
        fuelClassCd: String
    ): Flowable<Optional<Float>>

}
