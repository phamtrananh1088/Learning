package jp.co.toukei.log.trustar.db.result.dao

import androidx.room.Dao
import androidx.room.Query
import jp.co.toukei.log.lib.room.CommonDao
import jp.co.toukei.log.lib.room.SyncChanged
import jp.co.toukei.log.lib.room.SyncPending
import jp.co.toukei.log.trustar.db.result.entity.CommonWorkResult

@Dao
interface CommonWorkResultDao : CommonDao<CommonWorkResult> {

    @Query("update work_result set sync = $SyncPending where sync = $SyncChanged")
    fun setPending()

}
