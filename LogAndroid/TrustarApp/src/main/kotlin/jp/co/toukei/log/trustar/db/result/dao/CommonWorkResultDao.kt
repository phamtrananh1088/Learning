package jp.co.toukei.log.trustar.db.result.dao

import androidx.room.Dao
import androidx.room.Query
import jp.co.toukei.log.lib.room.CommonDao
import jp.co.toukei.log.trustar.db.result.entity.CommonWorkResult

@Dao
interface CommonWorkResultDao : CommonDao<CommonWorkResult> {

    @Query("update common_work_result set sync = 1 where sync = 0")
    fun setPending()

}
