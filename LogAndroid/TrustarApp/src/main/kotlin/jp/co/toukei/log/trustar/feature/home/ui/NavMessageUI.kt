package jp.co.toukei.log.trustar.feature.home.ui

import android.content.Context
import android.widget.TextView
import jp.co.toukei.log.trustar.addRightDateText
import jp.co.toukei.log.trustar.common.ui.DefaultSwipeRecyclerViewUI
import jp.co.toukei.log.trustar.setOldStyle

class NavMessageUI(context: Context) : DefaultSwipeRecyclerViewUI(context) {

    val dateTextView: TextView = toolbar.addRightDateText()

    init {
        toolbar.setOldStyle()
    }
}
