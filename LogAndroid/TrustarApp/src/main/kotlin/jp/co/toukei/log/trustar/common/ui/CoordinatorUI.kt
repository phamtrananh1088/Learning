package jp.co.toukei.log.trustar.common.ui

import android.content.Context
import androidx.coordinatorlayout.widget.CoordinatorLayout
import splitties.views.dsl.coordinatorlayout.coordinatorLayout

open class CoordinatorUI(context: Context) {
    @JvmField
    val coordinatorLayout: CoordinatorLayout = context.coordinatorLayout {
        fitsSystemWindows = true
    }
}
