package jp.co.toukei.log.trustar.db.user.dao

import androidx.room.Dao
import androidx.room.Query
import io.reactivex.rxjava3.core.Flowable
import jp.co.toukei.log.lib.room.CommonDao
import jp.co.toukei.log.trustar.db.user.entity.IncidentalWork

@Dao
interface IncidentalWorkDao : CommonDao<IncidentalWork> {

    @Query("select * from incidental_work")
    fun selectAll(): Flowable<List<IncidentalWork>>

    @Query("delete from incidental_work")
    fun deleteAll()
}
