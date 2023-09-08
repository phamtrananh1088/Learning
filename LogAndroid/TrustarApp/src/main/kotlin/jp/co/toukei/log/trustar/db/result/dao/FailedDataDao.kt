package jp.co.toukei.log.trustar.db.result.dao

import androidx.room.Dao
import androidx.room.Query
import jp.co.toukei.log.lib.room.CommonDao
import jp.co.toukei.log.trustar.db.result.entity.FailedData

@Dao
interface FailedDataDao : CommonDao<FailedData> {

    @Query("select * from failed_data")
    fun selectAll(): List<FailedData>
}
