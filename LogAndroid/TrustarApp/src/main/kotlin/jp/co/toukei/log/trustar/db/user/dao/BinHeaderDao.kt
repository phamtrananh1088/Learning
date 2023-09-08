package jp.co.toukei.log.trustar.db.user.dao

import androidx.room.Dao
import androidx.room.Query
import io.reactivex.rxjava3.core.Flowable
import jp.co.toukei.log.lib.room.CommonSyncDao
import jp.co.toukei.log.lib.room.SyncChanged
import jp.co.toukei.log.lib.room.SyncDone
import jp.co.toukei.log.lib.room.SyncPending
import jp.co.toukei.log.trustar.db.user.embedded.BinHeaderAndStatus
import jp.co.toukei.log.trustar.db.user.entity.BinHeader
import java.util.Optional

@Dao
interface BinHeaderDao : CommonSyncDao<BinHeader> {

    @Query("select * from bin_header where allocation_no = :allocationNo")
    fun find(allocationNo: String): BinHeader?

    /**
     * 移動中設定
     */
    @Query(
        """
        update bin_header
        set destination_row_no = :allocationRowNo, sync = 0
        where allocation_no = :allocationNo
        """
    )
    fun setDestination(allocationRowNo: Int, allocationNo: String)

    @Query(
        """
        select h.*, s.*, t.* from bin_header h
        join truck t on t.truck_cd = h.bin_truck_cd
        join bin_status s on h.bin_status = s.bin_status_cd
        order by
         case h.bin_status when 1 then 1 when 0 then 2 else 3 end,
         h.start_date
        """
    )
    fun selectAllWithStatus(): Flowable<List<BinHeaderAndStatus>>

    @Query(
        """
        select h.*, s.*, t.* from bin_header h
        join truck t on t.truck_cd = h.bin_truck_cd
        join bin_status s on h.bin_status = s.bin_status_cd
        where h.allocation_no = :allocationNo
        """
    )
    fun selectBinHeaderWithStatus(allocationNo: String): Flowable<Optional<BinHeaderAndStatus>>

    @Query("select count(*) from bin_header where bin_status = :status")
    fun countByStatus(status: Int): Int

    @Query("delete from bin_header")
    fun deleteAll()

    @Query("select * from bin_header")
    fun selectAll(): Flowable<List<BinHeader>>

    @Query("select * from bin_header where bin_status = :status limit 1")
    fun selectOneByStatus(status: Int): Optional<BinHeader>


    @Query("update bin_header set sync = $SyncPending where sync = $SyncChanged")
    override fun setPending()

    @Query("select * from bin_header where sync = $SyncPending")
    override fun getPending(): List<BinHeader>

    @Query("delete from bin_header where sync = $SyncDone")
    override fun deleteSynced()

    @Query(
        """
        update bin_header set sync = $SyncPending
        where exists(
         select 1 from bin_detail r
         where r.allocation_no = bin_header.allocation_no and r.sync = $SyncPending
        )
    """
    )
    fun setSyncPendingByWorkResult()

    @Query("select * from bin_header where sync = $SyncDone")
    fun safeToDeleteList(): List<BinHeader>
}
