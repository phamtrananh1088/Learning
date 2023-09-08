package jp.co.toukei.log.trustar.db.result.dao

import androidx.room.Dao
import androidx.room.Query
import jp.co.toukei.log.lib.room.CommonSyncDao
import jp.co.toukei.log.trustar.db.result.entity.CommonCollectionResult

@Dao
interface CommonCollectionResultDao : CommonSyncDao<CommonCollectionResult> {

    @Query("update common_collection_result set sync = 1 where sync = 0")
    override fun setPending()

    @Query("select * from common_collection_result where sync = 1")
    override fun getPending(): List<CommonCollectionResult>

}
