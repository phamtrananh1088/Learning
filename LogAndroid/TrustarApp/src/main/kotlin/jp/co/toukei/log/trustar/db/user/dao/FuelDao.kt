package jp.co.toukei.log.trustar.db.user.dao

import androidx.room.Dao
import androidx.room.Query
import io.reactivex.rxjava3.core.Flowable
import jp.co.toukei.log.lib.room.CommonDao
import jp.co.toukei.log.trustar.db.user.entity.Fuel

@Dao
interface FuelDao : CommonDao<Fuel> {

    @Query("select * from fuel")
    fun list(): Flowable<List<Fuel>>

    @Query("delete from fuel")
    fun deleteAll()
}
