package jp.co.toukei.log.trustar.db.user.dao

import androidx.room.Dao
import androidx.room.Query
import jp.co.toukei.log.lib.room.CommonDao
import jp.co.toukei.log.trustar.db.user.entity.WorkStatus

@Dao
interface WorkStatusDao : CommonDao<WorkStatus> {

    @Query("delete from work_status")
    fun deleteAll()
}
