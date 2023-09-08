package jp.co.toukei.log.trustar.feature.nimachi.ui

import android.content.Context
import android.graphics.Color
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import jp.co.toukei.log.lib.common.addDividerItemDecoration
import jp.co.toukei.log.lib.common.gravityCenterHorizontal
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.primaryButtonStyle
import jp.co.toukei.log.trustar.primaryLabel
import splitties.dimensions.dip
import splitties.views.dsl.core.*
import splitties.views.dsl.recyclerview.recyclerView

class SheetListUI(context: Context) {

    val create: TextView
    val list: RecyclerView

    @JvmField
    val view = context.verticalLayout {
        gravityCenterHorizontal()
        create = add(textView {
            setText(R.string.incidental_sheet_new_create)
            primaryButtonStyle()
        }, lParams(wrapContent, wrapContent) {
            margin = dip(8)
        })
        add(textView {
            setText(R.string.incidental_created)
            primaryLabel()
        }, lParams(matchParent, wrapContent))
        list = add(recyclerView {
            val lm = LinearLayoutManager(context)
            layoutManager = lm
            addDividerItemDecoration(lm.orientation, Color.TRANSPARENT, dip(1))
        }, lParams(matchParent, wrapContent))
    }
}
