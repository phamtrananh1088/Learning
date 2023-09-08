package jp.co.toukei.log.trustar.db.result.dao

import androidx.room.Dao
import androidx.room.Query
import jp.co.toukei.log.lib.room.CommonSyncDao
import jp.co.toukei.log.trustar.db.result.entity.CommonIncidentalTimeResult

@Dao
interface CommonIncidentalTimeResultDao : CommonSyncDao<CommonIncidentalTimeResult> {

    @Query("update common_incidental_time_result set sync = 1 where sync = 0")
    override fun setPending()

    @Query("select * from common_incidental_time_result where sync = 1")
    override fun getPending(): List<CommonIncidentalTimeResult>

}
