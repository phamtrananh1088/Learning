package jp.co.toukei.log.trustar.db.user.dao

import androidx.room.Dao
import androidx.room.Query
import jp.co.toukei.log.lib.room.CommonDao
import jp.co.toukei.log.trustar.db.user.entity.WorkPlace

@Dao
interface WorkPlaceDao : CommonDao<WorkPlace> {

    @Query("select * from work_place")
    fun selectAll(): Array<WorkPlace>

    @Query("delete from work_place")
    fun deleteAll()

    @Query("select * from work_place where place_cd = :cd")
    fun findByCd(cd: String): WorkPlace?
}
