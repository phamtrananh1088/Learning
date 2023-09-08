package jp.co.toukei.log.trustar.db.user.embedded

import android.location.Location
import androidx.room.ColumnInfo

class LocationRecord(
        @ColumnInfo(name = "latitude") @JvmField val latitude: Double?,
        @ColumnInfo(name = "longitude") @JvmField val longitude: Double?,
        @ColumnInfo(name = "accuracy") @JvmField val accuracy: Float?,
        @ColumnInfo(name = "date") @JvmField val date: Long
) {
    constructor(location: Location?, date: Long) : this(
            location?.latitude,
            location?.longitude,
            location?.accuracy,
            date
    )
}
