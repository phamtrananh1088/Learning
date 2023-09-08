package jp.co.toukei.log.trustar.db.user.dao

import androidx.room.Dao
import androidx.room.Query
import io.reactivex.rxjava3.core.Flowable
import jp.co.toukei.log.lib.room.CommonDao
import jp.co.toukei.log.trustar.db.user.entity.DelayReason

@Dao
interface DelayReasonDao : CommonDao<DelayReason> {

    @Query("select * from delay_reason order by display_order")
    fun selectAll(): Flowable<List<DelayReason>>

    @Query("delete from delay_reason")
    fun deleteAll()
}
