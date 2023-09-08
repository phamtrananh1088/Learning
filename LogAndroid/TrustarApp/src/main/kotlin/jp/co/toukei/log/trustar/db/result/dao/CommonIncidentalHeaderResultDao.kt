package jp.co.toukei.log.trustar.db.result.dao

import androidx.room.Dao
import androidx.room.Query
import jp.co.toukei.log.lib.room.CommonSyncDao
import jp.co.toukei.log.trustar.db.result.entity.CommonIncidentalHeaderResult

@Dao
interface CommonIncidentalHeaderResultDao : CommonSyncDao<CommonIncidentalHeaderResult> {

    @Query("update common_incidental_header_result set sync = 1 where sync = 0")
    override fun setPending()

    @Query("select * from common_incidental_header_result where sync = 1")
    override fun getPending(): List<CommonIncidentalHeaderResult>

}
