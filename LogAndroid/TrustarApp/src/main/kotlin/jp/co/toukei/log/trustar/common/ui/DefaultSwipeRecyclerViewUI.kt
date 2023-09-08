package jp.co.toukei.log.trustar.common.ui

import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.appbar.AppBarLayout
import jp.co.toukei.log.lib.common.addDividerItemDecoration
import jp.co.toukei.log.lib.common.backgroundColorResId
import jp.co.toukei.log.lib.common.swipeRefreshLayout
import jp.co.toukei.log.trustar.R
import splitties.dimensions.dip
import splitties.views.dsl.coordinatorlayout.defaultLParams
import splitties.views.dsl.core.add
import splitties.views.dsl.core.matchParent
import splitties.views.dsl.recyclerview.recyclerView

open class DefaultSwipeRecyclerViewUI(context: Context) : AppbarUI(context) {

    @JvmField
    val swipeRefreshLayout: SwipeRefreshLayout = coordinatorLayout.run {
        add(swipeRefreshLayout {
            backgroundColorResId = R.color.defaultBackground
        }, defaultLParams(matchParent, matchParent) {
            behavior = AppBarLayout.ScrollingViewBehavior()
        })
    }

    @JvmField
    val list: RecyclerView = swipeRefreshLayout.run {
        add(recyclerView {
            val lm = LinearLayoutManager(context)
            layoutManager = lm
            addDividerItemDecoration(lm.orientation, Color.TRANSPARENT, dip(1))
        }, ViewGroup.LayoutParams(matchParent, matchParent))
    }
}
