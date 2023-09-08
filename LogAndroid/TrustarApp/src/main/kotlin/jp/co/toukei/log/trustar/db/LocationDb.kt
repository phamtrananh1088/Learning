package jp.co.toukei.log.trustar.db

import android.location.Location

class LocationDb(
    val lon: Float,
    val lat: Float,
    val accuracy: Float,
)

fun LocationDb(location: Location) = location.run {
    LocationDb(longitude.toFloat(), latitude.toFloat(), accuracy)
}

fun LocationDb.toLocation(): Location = Location("").also {
    it.latitude = lat.toDouble()
    it.longitude = lon.toDouble()
    it.accuracy = accuracy
}
