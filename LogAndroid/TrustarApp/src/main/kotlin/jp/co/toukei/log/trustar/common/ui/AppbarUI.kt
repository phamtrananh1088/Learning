package jp.co.toukei.log.trustar.common.ui

import android.content.Context
import androidx.appcompat.widget.Toolbar
import com.google.android.material.appbar.AppBarLayout
import jp.co.toukei.log.trustar.R
import splitties.views.dsl.appcompat.toolbar
import splitties.views.dsl.coordinatorlayout.defaultLParams
import splitties.views.dsl.core.add
import splitties.views.dsl.core.matchParent
import splitties.views.dsl.core.wrapContent
import splitties.views.dsl.material.appBarLayout
import splitties.views.dsl.material.defaultLParams

open class AppbarUI(context: Context) : CoordinatorUI(context) {

    val toolbarLayoutParams: AppBarLayout.LayoutParams

    @JvmField
    val appBarLayout: AppBarLayout = coordinatorLayout.run {
        add(appBarLayout(), defaultLParams(matchParent, wrapContent) {
            behavior = AppBarLayout.Behavior()
        })
    }

    @JvmField
    val toolbar: Toolbar = appBarLayout.run {
        add(toolbar(theme = R.style.ToolbarTheme) {
            title = ""
        }, defaultLParams(matchParent, wrapContent) {
            scrollFlags = 0
            toolbarLayoutParams = this
        })
    }
}
