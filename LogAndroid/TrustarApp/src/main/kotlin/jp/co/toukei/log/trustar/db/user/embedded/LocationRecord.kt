package jp.co.toukei.log.trustar.db.user.embedded

import android.location.Location
import androidx.room.ColumnInfo
import jp.co.toukei.log.trustar.db.LocationDb
import jp.co.toukei.log.trustar.db.toLocation

class LocationRecord(
    @ColumnInfo(name = "date") @JvmField val date: Long,// event datetime. not location timestamp.
    @ColumnInfo(name = "location") @JvmField val location: LocationDb?,
) {
    constructor(location: Location?, date: Long) : this(
        date,
        location?.let(::LocationDb),
    )

    fun distanceTo(record: LocationRecord): Float? {
        val l = location?.toLocation() ?: return null
        val l2 = record.location?.toLocation() ?: return null
        return l.distanceTo(l2)
    }
}
