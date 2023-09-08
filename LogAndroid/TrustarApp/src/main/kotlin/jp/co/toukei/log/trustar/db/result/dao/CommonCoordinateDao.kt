package jp.co.toukei.log.trustar.db.result.dao

import androidx.room.Dao
import androidx.room.Query
import jp.co.toukei.log.lib.room.CommonSyncDao
import jp.co.toukei.log.trustar.db.result.entity.CommonCoordinate

@Dao
interface CommonCoordinateDao : CommonSyncDao<CommonCoordinate> {

    @Query("update common_coordinate set sync = 1 where sync = 0")
    override fun setPending()

    @Query("select * from common_coordinate where sync = 1")
    override fun getPending(): List<CommonCoordinate>
}
