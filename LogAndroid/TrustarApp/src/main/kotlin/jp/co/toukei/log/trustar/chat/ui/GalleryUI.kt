package jp.co.toukei.log.trustar.chat.ui

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.appbar.AppBarLayout
import jp.co.toukei.log.lib.common.backgroundColorResId
import jp.co.toukei.log.lib.spanCountBy
import jp.co.toukei.log.lib.util.MarginItemDecoration
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.common.ui.AppbarUI
import splitties.dimensions.dip
import splitties.views.dsl.coordinatorlayout.defaultLParams
import splitties.views.dsl.core.add
import splitties.views.dsl.core.matchParent
import splitties.views.dsl.recyclerview.recyclerView

class GalleryUI(context: Context) : AppbarUI(context) {

    @JvmField
    val list = coordinatorLayout.run {
        add(recyclerView {
            backgroundColorResId = R.color.defaultBackground
            val lm = GridLayoutManager(context, context.spanCountBy(dip(144)))
            layoutManager = lm
            addItemDecoration(MarginItemDecoration(dip(1)))
        }, defaultLParams(matchParent, matchParent) {
            behavior = AppBarLayout.ScrollingViewBehavior()
        })
    }
}
