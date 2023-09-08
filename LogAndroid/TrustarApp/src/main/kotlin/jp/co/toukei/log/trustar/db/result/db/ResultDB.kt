package jp.co.toukei.log.trustar.db.result.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import jp.co.toukei.log.lib.room.Converter
import jp.co.toukei.log.trustar.db.result.dao.*
import jp.co.toukei.log.trustar.db.result.entity.*

@Database(
    entities = [
        CommonBinResult::class,
        CommonWorkResult::class,
        CommonNotice::class,
        CommonCoordinate::class,
        CommonKyuyu::class,
        FailedData::class,
        CommonIncidentalHeaderResult::class,
        CommonIncidentalTimeResult::class,
        CommonCollectionResult::class
    ],
    exportSchema = false,
    version = 12
)
@TypeConverters(Converter::class)
abstract class ResultDB : RoomDatabase() {
    abstract fun commonBinResultDao(): CommonBinResultDao
    abstract fun commonWorkResultDao(): CommonWorkResultDao
    abstract fun commonNoticeDao(): CommonNoticeDao
    abstract fun commonCoordinateDao(): CommonCoordinateDao
    abstract fun commonKyuyuDao(): CommonKyuyuDao
    abstract fun commonIncidentalHeaderResultDao(): CommonIncidentalHeaderResultDao
    abstract fun commonIncidentalTimeResultDao(): CommonIncidentalTimeResultDao
    abstract fun commonCollectionResultDao(): CommonCollectionResultDao
    abstract fun failedDataDao(): FailedDataDao
}
