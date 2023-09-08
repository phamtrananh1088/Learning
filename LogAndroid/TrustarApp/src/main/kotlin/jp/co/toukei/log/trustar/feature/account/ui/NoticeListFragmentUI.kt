package jp.co.toukei.log.trustar.feature.account.ui

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.common.ui.CoordinatorUI
import splitties.dimensions.dip
import splitties.views.dsl.coordinatorlayout.defaultLParams
import splitties.views.dsl.core.add
import splitties.views.dsl.core.matchParent
import splitties.views.dsl.recyclerview.recyclerView
import splitties.views.horizontalPadding
import splitties.views.verticalPadding

class NoticeListFragmentUI(context: Context) : CoordinatorUI(context) {

    val recyclerView = coordinatorLayout.run {
        setBackgroundResource(R.drawable.window_placeholder)
        add(recyclerView {
            clipToPadding = false
            verticalPadding = dip(32)
            horizontalPadding = dip(16)
            layoutManager = LinearLayoutManager(context)
        }, defaultLParams(matchParent, matchParent))
    }
}
