package jp.co.toukei.log.trustar.db.user.dao

import androidx.room.Dao
import androidx.room.Query
import jp.co.toukei.log.lib.room.CommonDao
import jp.co.toukei.log.trustar.db.user.entity.BinStatus

@Dao
interface BinStatusDao : CommonDao<BinStatus> {

    @Query("delete from bin_status")
    fun deleteAll()
}
