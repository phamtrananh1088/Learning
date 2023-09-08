package jp.co.toukei.log.trustar.db.result.dao

import androidx.room.Dao
import androidx.room.Query
import jp.co.toukei.log.lib.room.CommonSyncDao
import jp.co.toukei.log.trustar.db.result.entity.CommonNotice

@Dao
interface CommonNoticeDao : CommonSyncDao<CommonNotice> {

    @Query("update common_notice set sync = 1 where sync = 0")
    override fun setPending()

    @Query("select * from common_notice where sync = 1")
    override fun getPending(): List<CommonNotice>
}
