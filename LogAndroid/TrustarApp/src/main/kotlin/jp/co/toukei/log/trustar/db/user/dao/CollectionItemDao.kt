package jp.co.toukei.log.trustar.db.user.dao

import androidx.room.Dao
import androidx.room.Query
import io.reactivex.rxjava3.core.Flowable
import jp.co.toukei.log.lib.room.CommonDao
import jp.co.toukei.log.trustar.db.user.entity.CollectionGroup

@Dao
interface CollectionItemDao : CommonDao<CollectionGroup> {

    @Query("select * from collection_group order by display_order")
    fun selectAll(): Flowable<List<CollectionGroup>>

    @Query("delete from collection_group")
    fun deleteAll()
}
