package jp.co.toukei.log.trustar.db.user.dao

import androidx.room.*
import jp.co.toukei.log.lib.room.CommonDao
import jp.co.toukei.log.trustar.db.user.entity.SensorDetectEvent

@Dao
interface SensorDetectEventDao : CommonDao<SensorDetectEvent> {

    @Query("select * from sensor_detect where allocation_no = :allocationNo")
    fun findBy(allocationNo: String): List<SensorDetectEvent>

    @Query("delete from sensor_detect where allocation_no = :allocationNo")
    fun deleteBy(allocationNo: String)

    @Query("select distinct allocation_no from sensor_detect")
    fun allocationList(): List<String>

    @RewriteQueriesToDropUnusedColumns
    @Query("select *, max(event_date) from sensor_detect group by allocation_no")
    fun latestEventEachGroup(): List<SensorDetectEvent>

    @Query("delete from sensor_detect where allocation_no = :allocationNo and event_date between :from and :to")
    fun deleteByTimeRange(allocationNo: String, from: Long, to: Long)

    @RewriteQueriesToDropUnusedColumns
    @Query("select * from sensor_detect where allocation_no = :allocationNo order by event_date desc limit 1")
    fun latestEvent(allocationNo: String): SensorDetectEvent?
}
