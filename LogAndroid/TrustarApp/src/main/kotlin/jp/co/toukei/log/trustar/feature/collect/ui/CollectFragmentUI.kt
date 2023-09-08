package jp.co.toukei.log.trustar.feature.collect.ui

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import jp.co.toukei.log.lib.common.materialButtonUnelevated
import jp.co.toukei.log.lib.gradientDrawable
import jp.co.toukei.log.trustar.R
import splitties.dimensions.dip
import splitties.views.bottomPadding
import splitties.views.dsl.core.*
import splitties.views.dsl.recyclerview.recyclerView
import splitties.views.padding
import splitties.views.verticalPadding

class CollectFragmentUI(context: Context) {

    val title: TextView
    val list: RecyclerView
    val cancelButton: View
    val updateButton: View

    @JvmField
    val view = context.verticalLayout {
        weightSum = 1F

        val textC = ContextCompat.getColor(context, R.color.gray_out)
        dividerDrawable = gradientDrawable(0, textC).apply { setSize(dip(1), dip(1)) }
        showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE

        title = add(textView {
            textSize = 16F
            padding = dip(8)
            verticalPadding = dip(12)
        }, lParams())
        list = add(recyclerView {
            clipToPadding = false
            bottomPadding = dip(16)
        }, lParams(matchParent, 0, weight = 1F))
        add(horizontalLayout {
            gravity = Gravity.END or Gravity.CENTER_VERTICAL
            clipToPadding = false
            elevation = dip(4).toFloat()
            padding = dip(4)

            cancelButton = add(materialButtonUnelevated {
                setText(R.string.cancel)
            }, lParams(wrapContent, wrapContent) {
                margin = dip(4)
            })
            updateButton = add(materialButtonUnelevated {
                setText(R.string.update)
            }, lParams(wrapContent, wrapContent) {
                margin = dip(4)
            })
        }, lParams(matchParent, wrapContent))
    }
}
