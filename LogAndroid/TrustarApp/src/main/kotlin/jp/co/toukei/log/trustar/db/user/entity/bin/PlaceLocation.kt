package jp.co.toukei.log.trustar.db.user.entity.bin

import android.location.Location
import androidx.room.ColumnInfo
import androidx.room.Ignore

class PlaceLocation(
        @ColumnInfo(name = "latitude") @JvmField val lat: Double,
        @ColumnInfo(name = "longitude") @JvmField val lon: Double
) {

    @Ignore
    @JvmField
    val location = Location("").apply {
        latitude = lat
        longitude = lon
    }
}
