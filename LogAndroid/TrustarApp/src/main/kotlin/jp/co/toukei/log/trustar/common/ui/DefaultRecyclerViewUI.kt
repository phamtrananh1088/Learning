package jp.co.toukei.log.trustar.common.ui

import android.content.Context
import android.graphics.Color
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import jp.co.toukei.log.lib.common.addDividerItemDecoration
import jp.co.toukei.log.lib.common.backgroundColorResId
import jp.co.toukei.log.trustar.R
import splitties.dimensions.dip
import splitties.views.dsl.coordinatorlayout.defaultLParams
import splitties.views.dsl.core.add
import splitties.views.dsl.core.matchParent
import splitties.views.dsl.recyclerview.recyclerView

open class DefaultRecyclerViewUI(context: Context) : AppbarUI(context) {

    @JvmField
    val list: RecyclerView = coordinatorLayout.run {
        add(recyclerView {
            backgroundColorResId = R.color.defaultBackground
            val lm = LinearLayoutManager(context)
            layoutManager = lm
            addDividerItemDecoration(lm.orientation, Color.TRANSPARENT, dip(1))
        }, defaultLParams(matchParent, matchParent) {
            behavior = AppBarLayout.ScrollingViewBehavior()
        })
    }
}
