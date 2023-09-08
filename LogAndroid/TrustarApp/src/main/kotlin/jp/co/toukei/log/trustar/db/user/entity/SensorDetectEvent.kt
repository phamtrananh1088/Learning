package jp.co.toukei.log.trustar.db.user.entity

import android.location.Location
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import jp.co.toukei.log.trustar.db.user.embedded.LocationRecord
import java.text.DateFormat
import java.util.Date

@Entity(
    tableName = "sensor_detect",
    indices = [Index("event_date"), Index("allocation_no")]
)
class SensorDetectEvent(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") @JvmField val id: Int,
    @ColumnInfo(name = "allocation_no") @JvmField val allocationNo: String,
    @Embedded(prefix = "event_") @JvmField val eventRecord: LocationRecord,
    @ColumnInfo(name = "location_timestamp") @JvmField val locationTimestamp: Long?,
) {

    constructor(
        binHeader: BinHeader,
        location: Location?,
        eventTime: Long,
    ) : this(0, binHeader.allocationNo, LocationRecord(location, eventTime), location?.time)

    fun csvRow(formatter: DateFormat): String {
        return eventRecord.run {
            val l = location
            "${formatter.format(Date(date))},${l?.lon ?: 0F},${l?.lat ?: 0F},${l?.accuracy ?: 0F}"
        }
    }


    fun copyOf(newLocation: Location): SensorDetectEvent {
        return SensorDetectEvent(
            id, allocationNo, LocationRecord(newLocation, eventRecord.date), newLocation.time
        )
    }
}
