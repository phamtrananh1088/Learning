package jp.co.toukei.log.trustar.db.result.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import jp.co.toukei.log.trustar.db.Converter
import jp.co.toukei.log.trustar.db.result.dao.CommonBinResultDao
import jp.co.toukei.log.trustar.db.result.dao.CommonCollectionResultDao
import jp.co.toukei.log.trustar.db.result.dao.CommonCoordinateDao
import jp.co.toukei.log.trustar.db.result.dao.CommonDeliveryChartDao
import jp.co.toukei.log.trustar.db.result.dao.CommonIncidentalHeaderResultDao
import jp.co.toukei.log.trustar.db.result.dao.CommonIncidentalTimeResultDao
import jp.co.toukei.log.trustar.db.result.dao.CommonNoticeDao
import jp.co.toukei.log.trustar.db.result.dao.CommonRefuelDao
import jp.co.toukei.log.trustar.db.result.dao.CommonRestDao
import jp.co.toukei.log.trustar.db.result.dao.CommonSensorCsvDao
import jp.co.toukei.log.trustar.db.result.dao.CommonWorkResultDao
import jp.co.toukei.log.trustar.db.result.dao.FailedDataDao
import jp.co.toukei.log.trustar.db.result.entity.CommonBinResult
import jp.co.toukei.log.trustar.db.result.entity.CommonCollectionResult
import jp.co.toukei.log.trustar.db.result.entity.CommonCoordinate
import jp.co.toukei.log.trustar.db.result.entity.CommonDeliveryChart
import jp.co.toukei.log.trustar.db.result.entity.CommonIncidentalHeaderResult
import jp.co.toukei.log.trustar.db.result.entity.CommonIncidentalTimeResult
import jp.co.toukei.log.trustar.db.result.entity.CommonNotice
import jp.co.toukei.log.trustar.db.result.entity.CommonRefuel
import jp.co.toukei.log.trustar.db.result.entity.CommonRest
import jp.co.toukei.log.trustar.db.result.entity.CommonSensorCsv
import jp.co.toukei.log.trustar.db.result.entity.CommonWorkResult
import jp.co.toukei.log.trustar.db.result.entity.FailedData

@Database(
    entities = [
        CommonBinResult::class,
        CommonWorkResult::class,
        CommonNotice::class,
        CommonCoordinate::class,
        CommonRefuel::class,
        FailedData::class,
        CommonIncidentalHeaderResult::class,
        CommonIncidentalTimeResult::class,
        CommonCollectionResult::class,
        CommonSensorCsv::class,
        CommonRest::class,
        CommonDeliveryChart::class,
    ],
    exportSchema = false,
    version = 17
)
@TypeConverters(Converter::class)
abstract class ResultDB : RoomDatabase() {
    abstract fun commonBinResultDao(): CommonBinResultDao
    abstract fun commonWorkResultDao(): CommonWorkResultDao
    abstract fun commonNoticeDao(): CommonNoticeDao
    abstract fun commonCoordinateDao(): CommonCoordinateDao
    abstract fun commonRefuelDao(): CommonRefuelDao
    abstract fun commonIncidentalHeaderResultDao(): CommonIncidentalHeaderResultDao
    abstract fun commonIncidentalTimeResultDao(): CommonIncidentalTimeResultDao
    abstract fun commonCollectionResultDao(): CommonCollectionResultDao
    abstract fun failedDataDao(): FailedDataDao
    abstract fun commonSensorCsvDao(): CommonSensorCsvDao
    abstract fun commonRestDao(): CommonRestDao
    abstract fun commonDeliveryChartDao(): CommonDeliveryChartDao
}
