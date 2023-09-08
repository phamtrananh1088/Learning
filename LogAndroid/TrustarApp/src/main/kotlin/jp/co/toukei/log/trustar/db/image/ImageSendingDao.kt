package jp.co.toukei.log.trustar.db.image

import androidx.room.Dao
import androidx.room.Query
import jp.co.toukei.log.lib.room.CommonDao

@Dao
interface ImageSendingDao : CommonDao<ImageSending> {

    @Query("select * from image_sending limit 1")
    fun selectAny(): ImageSending?

    @Query("delete from image_sending where pic_id = :picId")
    fun deleteByPicId(picId: String)
}
