package jp.co.toukei.log.trustar.db.user.dao

import androidx.room.Dao
import androidx.room.Query
import io.reactivex.rxjava3.core.Flowable
import jp.co.toukei.log.lib.room.CommonDao
import jp.co.toukei.log.trustar.db.user.entity.Shipper

@Dao
interface ShipperDao : CommonDao<Shipper> {

    @Query("select * from shipper")
    fun selectAll(): Flowable<List<Shipper>>

    @Query("delete from shipper")
    fun deleteAll()

    @Query("select * from shipper where shipper_cd = :shipperCd")
    fun findByCd(shipperCd: String): Shipper?
}
