package jp.co.toukei.log.trustar.db.image

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import jp.co.toukei.log.trustar.db.Converter

@Database(
        entities = [
            ImageSending::class
        ],
        exportSchema = false,
        version = 1
)
@TypeConverters(Converter::class)
abstract class ImageSendingDB : RoomDatabase() {
    abstract fun imageSendingDao(): ImageSendingDao
}
