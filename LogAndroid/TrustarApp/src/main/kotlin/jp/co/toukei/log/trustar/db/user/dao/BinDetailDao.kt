package jp.co.toukei.log.trustar.db.user.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import io.reactivex.rxjava3.core.Flowable
import jp.co.toukei.log.lib.room.CommonDao
import jp.co.toukei.log.trustar.db.user.embedded.BinDetailAndStatus
import jp.co.toukei.log.trustar.db.user.entity.BinDetail

@Dao
interface BinDetailDao : CommonDao<BinDetail> {

    @Query("select * from bin_detail where allocation_no = :allocationNo and allocation_row_no = :allocationRowNo")
    fun find(allocationNo: String, allocationRowNo: Int): BinDetail?

    @Query(
        """
        select ws.*, d.*
        from bin_detail d
        join work_status ws on ws.work_status_cd = d.status
        where d.allocation_no = :allocationNo
        and exists (
         select 1 from bin_header h where h.allocation_no = d.allocation_no
        )
        order by
            case d.status when 1 then 1 when 2 then 2 when 0 then 3 when 3 then 4 end,
            case when d.status = 3 then 1 else d.service_order end,
            case when d.status = 3 then d.work_start_date else d.appointed_date_from end
    """
    )
    fun binDetailAndStatusListByAllocationNo(allocationNo: String): Flowable<List<BinDetailAndStatus>>

    @Query("update bin_detail set status = :workStatus where allocation_no = :allocationNo and allocation_row_no = :allocationRowNo")
    fun setWorkStatus(workStatus: Int, allocationNo: String, allocationRowNo: Int)

    @Query("update bin_detail set status = 0 where status = 1 and allocation_no = :allocationNo")
    fun unsetMoving(allocationNo: String)

    @Query("select count(*) from bin_detail where allocation_no = :allocationNo")
    fun countByAllocationNo(allocationNo: String): Int

    @Query("select count(*) from bin_detail where allocation_no = :allocationNo and status = 3")
    fun countFinished(allocationNo: String): Int

    @Query("select * from bin_detail where allocation_no = :allocationNo and allocation_row_no = :allocationRowNo")
    fun selectBinDetail(allocationNo: String, allocationRowNo: Int): Flowable<BinDetail>

    @Query(
        """
        select count(*) from bin_detail
        where status = :status and allocation_no = :allocationNo
    """
    )
    fun countByStatus(status: Int, allocationNo: String): Int

    @Query("update bin_detail set sync = 1 where sync = 0")
    fun setPending()

    @Query("select * from bin_detail where sync = 1")
    fun getPending(): List<BinDetail>

    @Query("select * from bin_detail where sync = -1 or sync = 2")
    fun safeToDeleteList(): List<BinDetail>

    @RewriteQueriesToDropUnusedColumns
    @Query(
        """
        select *, max(work_end_date) from bin_detail d
        where exists (select 1 from bin_header r where r.allocation_no = d.allocation_no)
         and allocation_no = :allocationNo
         and work_end_date is not null
         and service_order is not null
         and status = 3
        group by allocation_no
    """
    )
    fun findByMaxEndTime(allocationNo: String): BinDetail?

    @RewriteQueriesToDropUnusedColumns
    @Query(
        """
        select *, min(service_order) from bin_detail d
        where exists (select 1 from bin_header r where r.allocation_no = d.allocation_no)
         and allocation_no = :allocationNo
         and (status = 0 or status == 1)
         and (:fromServiceOrder is null or service_order >= :fromServiceOrder)
        group by allocation_no
    """
    )
    fun findNextBinDetailForWork(allocationNo: String, fromServiceOrder: Int?): BinDetail?

}
