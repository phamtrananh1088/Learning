package jp.co.toukei.log.trustar.db.result.dao

import androidx.room.Dao
import androidx.room.Query
import jp.co.toukei.log.lib.room.CommonDao
import jp.co.toukei.log.lib.room.SyncChanged
import jp.co.toukei.log.lib.room.SyncPending
import jp.co.toukei.log.trustar.db.result.entity.CommonBinResult
import jp.co.toukei.log.trustar.db.result.entity.CommonWorkResult

@Dao
interface CommonBinResultDao : CommonDao<CommonBinResult> {

    @Query("update bin_result set sync = $SyncPending where sync = $SyncChanged")
    fun setPending()

    @Query(
        """
        select * from bin_result b
        left outer join work_result w
             on b.allocation_no = w.allocation_no
            and b.company_cd = w.company_cd
            and b.user_id = w.user_id
            and w.sync = $SyncPending
        where b.sync = $SyncPending or w.sync = $SyncPending
        """
    )
    fun getPendingWithWork(): Map<CommonBinResult, List<CommonWorkResult>>

}
