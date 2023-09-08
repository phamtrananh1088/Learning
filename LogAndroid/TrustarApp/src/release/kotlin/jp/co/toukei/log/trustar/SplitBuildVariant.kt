@file:Suppress("NOTHING_TO_INLINE")

package jp.co.toukei.log.trustar

import android.app.Application
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.distribute.Distribute

object SplitBuildVariant {

    inline fun debugSensorEvent(
        a: Any?,
        b: Any?,
    ) = Unit

    inline fun debugDbExport() = Unit

    inline fun debugRestEvent(
        a: Any?,
        b: Any?,
    ) = Unit

    fun initAppCenter(app: Application) {
        AppCenter.start(
            app,
            BuildConfig.appCenterKey,
            Analytics::class.java,
            Distribute::class.java
        )
    }
}
