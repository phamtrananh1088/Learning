package jp.co.toukei.log.trustar.db.result.dao

import androidx.room.Dao
import androidx.room.Query
import jp.co.toukei.log.lib.room.CommonDao
import jp.co.toukei.log.trustar.db.result.embedded.BinResultWithWorkResult
import jp.co.toukei.log.trustar.db.result.entity.CommonBinResult

@Dao
interface CommonBinResultDao : CommonDao<CommonBinResult> {

    @Query("update common_bin_result set sync = 1 where sync = 0")
    fun setPending()

    @Query(
        """
        select
            b.company_cd as b_company_cd,
            b.user_id as b_user_id,
            b.allocation_no as b_allocation_no,
            b.truck_cd as b_truck_cd,
            b.weather_cd as b_weather_cd,
            b.start_latitude as b_start_latitude,
            b.start_longitude as b_start_longitude,
            b.start_date as b_start_date,
            b.end_latitude as b_end_latitude,
            b.end_longitude as b_end_longitude,
            b.end_date as b_end_date,
            b.destination_row_no as b_destination_row_no,
            b.start_accuracy as b_start_accuracy,
            b.end_accuracy as b_end_accuracy,
            b.sync as b_sync,
            b.outgoing_meter as b_outgoing_meter,
            b.incoming_meter as b_incoming_meter,
            b.updated_date as b_updated_date,
            w.*
      from common_bin_result b
      left outer join common_work_result w
       on b.allocation_no = w.allocation_no
        and b.company_cd = w.company_cd
        and b.user_id = w.user_id
        and w.sync = 1
      where b.sync = 1 or w.sync = 1
    """
    )
    fun getPendingWithWorkResult(): List<BinResultWithWorkResult>

    /*
        nullable not supported by room.
        @Query(
            """
            select * from common_bin_result b
            left outer join common_work_result w
            on b.allocation_no = w.allocation_no
            and b.company_cd = w.company_cd
            and b.user_id = w.user_id
            and w.sync = 1
            where b.sync = 1 or w.sync = 1
        """
        )
        fun getPendingWithWork(): Map<CommonBinResult, List<CommonWorkResult?>>
    */
}
