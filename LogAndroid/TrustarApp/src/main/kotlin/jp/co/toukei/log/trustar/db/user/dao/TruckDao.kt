package jp.co.toukei.log.trustar.db.user.dao

import androidx.room.Dao
import androidx.room.Query
import io.reactivex.rxjava3.core.Observable
import jp.co.toukei.log.lib.room.CommonDao
import jp.co.toukei.log.trustar.db.user.entity.Truck

@Dao
interface TruckDao : CommonDao<Truck> {

    @Query("select * from truck")
    fun selectAll(): Observable<List<Truck>>

    @Query("delete from truck")
    fun deleteAll()

}
