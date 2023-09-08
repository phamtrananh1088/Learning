package jp.co.toukei.log.trustar.feature.nimachi.ui

import android.content.Context
import android.graphics.Color
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import jp.co.toukei.log.lib.UI
import jp.co.toukei.log.lib.common.textColor
import jp.co.toukei.log.lib.common.whiteText
import jp.co.toukei.log.lib.gradientDrawableBorder
import jp.co.toukei.log.lib.util.ValueBind
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.feature.nimachi.data.WorkItem
import jp.co.toukei.log.trustar.primaryGroundColor
import splitties.dimensions.dip
import splitties.views.centerText
import splitties.views.dsl.core.*
import splitties.views.horizontalPadding
import splitties.views.padding
import splitties.views.verticalPadding

class WorkItemUI(context: Context) : ValueBind<WorkItem>(), UI {

    val tv: TextView

    override val view = context.horizontalLayout {
        padding = dip(8)
        tv = add(textView {
            verticalPadding = dip(16)
            horizontalPadding = dip(32)
            centerText()
            textSize = 16F
        }, lParams(matchParent, matchParent))
    }

    private val colorPrimary = ContextCompat.getColor(context, R.color.colorPrimary)

    override fun onBind(bound: WorkItem) {
        tv.apply {
            text = bound.workNm
            if (bound.selected) {
                whiteText()
                primaryGroundColor(dip(8))
            } else {
                textColor = colorPrimary
                background = gradientDrawableBorder(dip(8), Color.WHITE, colorPrimary, dip(1))
            }
        }
    }

    class Diff : DiffUtil.ItemCallback<WorkItem>() {
        override fun areItemsTheSame(oldItem: WorkItem, newItem: WorkItem): Boolean {
            return oldItem.sameItem(newItem)
        }

        override fun areContentsTheSame(oldItem: WorkItem, newItem: WorkItem): Boolean {
            return oldItem.workNm == newItem.workNm && oldItem.selected == newItem.selected
        }
    }
}
