package jp.co.toukei.log.lib.util

import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import jp.co.toukei.log.lib.get
import jp.co.toukei.log.lib.weakRef

class LocationCallbackFix(locationCallbackFix: LocationCallback) : LocationCallback() {
    private val weak = locationCallbackFix.weakRef()

    override fun onLocationAvailability(p0: LocationAvailability) {
        weak.get { onLocationAvailability(p0) }
    }

    override fun onLocationResult(p0: LocationResult) {
        weak.get { onLocationResult(p0) }
    }
}
