package jp.co.toukei.log.trustar.db.result.dao

import androidx.room.Dao
import androidx.room.Query
import io.reactivex.rxjava3.core.Flowable
import jp.co.toukei.log.lib.room.CommonSyncDao
import jp.co.toukei.log.trustar.db.result.entity.CommonKyuyu
import java.util.*

@Dao
interface CommonKyuyuDao : CommonSyncDao<CommonKyuyu> {

    @Query("update common_kyuyu set sync = 1 where sync = 0")
    override fun setPending()

    @Query("select * from common_kyuyu where sync = 1")
    override fun getPending(): List<CommonKyuyu>

    @Query(
        """
        select sum(k.refueling_vol) from common_kyuyu k
        where k.company_cd = :companyCd and k.allocation_no = :allocationNo and k.fuelClass_cd = :fuelClassCd
    """
    )
    fun getTotalAmount(
        companyCd: String,
        allocationNo: String,
        fuelClassCd: String
    ): Flowable<Optional<Float>>

    @Query("delete from common_kyuyu where sync = 2 and input_datetime < :date")
    fun deleteSyncedBeforeDate(date: Long)
}
