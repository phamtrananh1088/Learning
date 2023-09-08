package jp.co.toukei.log.trustar.db.user.dao

import androidx.room.Dao
import androidx.room.Query
import io.reactivex.rxjava3.core.Flowable
import jp.co.toukei.log.lib.room.CommonDao
import jp.co.toukei.log.trustar.db.user.entity.Work
import java.util.Optional

@Dao
interface WorkDao : CommonDao<Work> {

    @Query("select * from work where display_flag = 1 order by display_order")
    fun displayList(): Flowable<List<Work>>

    @Query("delete from work")
    fun deleteAll()

    @Query("select * from work where work_cd = :workCd")
    fun workByCd(workCd: String): Flowable<Optional<Work>>
}
