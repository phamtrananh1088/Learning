package jp.co.toukei.log.trustar.db.result.dao

import androidx.room.Dao
import androidx.room.Query
import jp.co.toukei.log.lib.room.CommonDao
import jp.co.toukei.log.trustar.db.result.entity.CommonSensorCsv

@Dao
interface CommonSensorCsvDao : CommonDao<CommonSensorCsv> {

    @Query("select * from sensor_csv limit 1")
    fun selectAny(): CommonSensorCsv?

}
