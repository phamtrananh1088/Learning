package jp.co.toukei.log.trustar.db.user.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import jp.co.toukei.log.trustar.db.Converter
import jp.co.toukei.log.trustar.db.user.dao.BinDetailDao
import jp.co.toukei.log.trustar.db.user.dao.BinHeaderDao
import jp.co.toukei.log.trustar.db.user.dao.BinStatusDao
import jp.co.toukei.log.trustar.db.user.dao.CollectionItemDao
import jp.co.toukei.log.trustar.db.user.dao.CollectionResultDao
import jp.co.toukei.log.trustar.db.user.dao.DelayReasonDao
import jp.co.toukei.log.trustar.db.user.dao.DeliveryChartDao
import jp.co.toukei.log.trustar.db.user.dao.FuelDao
import jp.co.toukei.log.trustar.db.user.dao.IncidentalHeaderDao
import jp.co.toukei.log.trustar.db.user.dao.IncidentalTimeDao
import jp.co.toukei.log.trustar.db.user.dao.IncidentalWorkDao
import jp.co.toukei.log.trustar.db.user.dao.NoticeDao
import jp.co.toukei.log.trustar.db.user.dao.SensorDetectEventDao
import jp.co.toukei.log.trustar.db.user.dao.ShipperDao
import jp.co.toukei.log.trustar.db.user.dao.TruckDao
import jp.co.toukei.log.trustar.db.user.dao.WorkDao
import jp.co.toukei.log.trustar.db.user.dao.WorkPlaceDao
import jp.co.toukei.log.trustar.db.user.dao.WorkStatusDao
import jp.co.toukei.log.trustar.db.user.entity.BinDetail
import jp.co.toukei.log.trustar.db.user.entity.BinHeader
import jp.co.toukei.log.trustar.db.user.entity.BinStatus
import jp.co.toukei.log.trustar.db.user.entity.CollectionGroup
import jp.co.toukei.log.trustar.db.user.entity.CollectionResult
import jp.co.toukei.log.trustar.db.user.entity.DelayReason
import jp.co.toukei.log.trustar.db.user.entity.DeliveryChart
import jp.co.toukei.log.trustar.db.user.entity.Fuel
import jp.co.toukei.log.trustar.db.user.entity.IncidentalHeader
import jp.co.toukei.log.trustar.db.user.entity.IncidentalTime
import jp.co.toukei.log.trustar.db.user.entity.IncidentalWork
import jp.co.toukei.log.trustar.db.user.entity.Notice
import jp.co.toukei.log.trustar.db.user.entity.SensorDetectEvent
import jp.co.toukei.log.trustar.db.user.entity.Shipper
import jp.co.toukei.log.trustar.db.user.entity.Truck
import jp.co.toukei.log.trustar.db.user.entity.Work
import jp.co.toukei.log.trustar.db.user.entity.WorkPlace
import jp.co.toukei.log.trustar.db.user.entity.WorkStatus

@Database(
    entities = [
        BinHeader::class,
        BinDetail::class,
        BinStatus::class,
        WorkStatus::class,
        Work::class,
        Fuel::class,
        Truck::class,
        Shipper::class,
        WorkPlace::class,
        Notice::class,
        DelayReason::class,

        IncidentalHeader::class,
        IncidentalWork::class,
        IncidentalTime::class,
        CollectionGroup::class,
        CollectionResult::class,
        SensorDetectEvent::class,
        DeliveryChart::class,
    ],
    exportSchema = false,
    version = 21
)
@TypeConverters(Converter::class)
abstract class UserDB : RoomDatabase() {

    abstract fun noticeDao(): NoticeDao

    abstract fun binHeaderDao(): BinHeaderDao

    abstract fun binDetailDao(): BinDetailDao

    abstract fun workDao(): WorkDao

    abstract fun workStatusDao(): WorkStatusDao

    abstract fun binStatusDao(): BinStatusDao

    abstract fun fuelDao(): FuelDao

    abstract fun truckDao(): TruckDao

    abstract fun shipperDao(): ShipperDao

    abstract fun workPlaceDao(): WorkPlaceDao

    abstract fun delayReasonDao(): DelayReasonDao

    abstract fun collectionItemDao(): CollectionItemDao

    abstract fun collectionResultDao(): CollectionResultDao

    abstract fun incidentalHeaderDao(): IncidentalHeaderDao

    abstract fun incidentalWorkDao(): IncidentalWorkDao

    abstract fun incidentalTimeDao(): IncidentalTimeDao

    abstract fun sensorDetectEventDao(): SensorDetectEventDao

    abstract fun deliveryChartDao(): DeliveryChartDao

    companion object {

        /**
         * @see [IncidentalHeader]
         */
        const val CREATE_INDEX_INCIDENTAL_HEADER_SHEET =
            "create unique index if not exists `index_incidental_header_sheet_no` on `incidental_header` (`sheet_no`) where `sheet_no` is not null"

        /**
         * @see [IncidentalTime]
         */
        const val CREATE_INDEX_INCIDENTAL_TIME_SHEET =
            "create unique index if not exists `index_incidental_time_sheet_row` on `incidental_time` (`sheet`,`row_no`) where `sheet` is not null and `row_no` is not null"
    }
}
