package jp.co.toukei.log.trustar.db.user.dao

import androidx.room.Dao
import androidx.room.Query
import io.reactivex.rxjava3.core.Flowable
import jp.co.toukei.log.lib.room.CommonSyncDao
import jp.co.toukei.log.lib.room.SyncChanged
import jp.co.toukei.log.lib.room.SyncDone
import jp.co.toukei.log.lib.room.SyncPending
import jp.co.toukei.log.trustar.db.user.embedded.IncidentalListItem
import jp.co.toukei.log.trustar.db.user.entity.IncidentalHeader
import java.util.Optional

@Dao
interface IncidentalHeaderDao : CommonSyncDao<IncidentalHeader> {

    @Query("select * from incidental_header where uuid = :uuid")
    fun selectByUUID(uuid: String): IncidentalHeader?

    @Query("select * from incidental_header where uuid = :uuid")
    fun selectByUUIDRx(uuid: String): Flowable<Optional<IncidentalHeader>>

    @Query(
        """
        select * from incidental_header h
        left join shipper p on p.shipper_cd = h.shipper
        where deleted = 0 and h.uuid = :uuid
    """
    )
    fun findByUUID(uuid: String): Flowable<IncidentalListItem>

    @Query(
        """
        select * from incidental_header h
        left join shipper p on p.shipper_cd = h.shipper
        where deleted = 0
          and h.allocation_no = :allocationNo
          and h.allocation_row_no = :allocationRowNo
    """
    )
    fun selectSheetList(
        allocationNo: String,
        allocationRowNo: Int,
    ): Flowable<List<IncidentalListItem>>

    @Query(
        """
        select count(*) from incidental_header h
        where deleted = 0
          and h.allocation_no = :allocationNo
          and h.allocation_row_no = :allocationRowNo
    """
    )
    fun countSheetList(allocationNo: String, allocationRowNo: Int): Flowable<Int>

    @Query(
        """
        select count(*) from incidental_header h
        where deleted = 0
          and status = 1
          and h.allocation_no = :allocationNo
          and h.allocation_row_no = :allocationRowNo
    """
    )
    fun countSignedSheetList(allocationNo: String, allocationRowNo: Int): Flowable<Int>

    @Query("update incidental_header set sync = $SyncPending where sync = $SyncChanged")
    override fun setPending()

    @Query("select * from incidental_header where sync = $SyncPending")
    override fun getPending(): List<IncidentalHeader>

    @Query("delete from incidental_header where sync = $SyncDone")
    override fun deleteSynced()
}
