package jp.co.toukei.log.trustar.db.user.dao

import androidx.room.Dao
import androidx.room.Query
import jp.co.toukei.log.lib.room.CommonSyncDao
import jp.co.toukei.log.trustar.db.user.entity.CollectionResult

@Dao
interface CollectionResultDao : CommonSyncDao<CollectionResult> {

    @Query("update collection_result set sync = 1 where sync = 0")
    override fun setPending()

    @Query("select * from collection_result where sync = 1")
    override fun getPending(): List<CollectionResult>

    @Query("select * from collection_result where allocation_no = :allocationNo and allocation_row_no = :allocationRowNo")
    fun getResult(allocationNo: String, allocationRowNo: Int): CollectionResult?

    @Query("select * from collection_result where sync = -1 or sync = 2")
    fun safeToDeleteList(): List<CollectionResult>
}
