package jp.co.toukei.log.trustar.feature.home.ui

import android.content.Context
import android.view.View
import android.widget.TextView
import jp.co.toukei.log.lib.UI
import jp.co.toukei.log.lib.common.gone
import jp.co.toukei.log.lib.common.gravityCenterVertical
import jp.co.toukei.log.lib.common.show
import jp.co.toukei.log.lib.common.textColor
import jp.co.toukei.log.lib.rippleDrawable
import jp.co.toukei.log.lib.util.Differ
import jp.co.toukei.log.lib.util.ValueBind
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.db.user.embedded.BinHeaderAndStatus
import splitties.dimensions.dip
import splitties.views.backgroundColor
import splitties.views.centerText
import splitties.views.dsl.core.*
import splitties.views.padding
import splitties.views.verticalPadding

class BinHeadUI(context: Context) : ValueBind<BinHeaderAndStatus>(), UI {

    private val details: TextView
    private val status: TextView
    val icon: View

    override val view: View = context.horizontalLayout {
        weightSum = 1F
        gravityCenterVertical()
        padding = dip(8)
        background = rippleDrawable(0)

        details = add(textView {
            textSize = 20F
            padding = dip(8)
        }, lParams(0, wrapContent, weight = 1F))

        status = add(textView {
            minEms = 4
            centerText()
            textSize = 12F
            includeFontPadding = false
            verticalPadding = dip(2)
        }, lParams(wrapContent, wrapContent) {
            margin = dip(8)
        })

        icon = add(imageView {
            setImageResource(R.drawable.baseline_info_24)
            setColorFilter(context.getColor(R.color.colorPrimary))
            background = rippleDrawable(Int.MAX_VALUE)
            padding = dip(8)
        }, lParams(dip(40), dip(40)))
    }

    fun iconVisible(show: Boolean) {
        if (show) icon.show() else icon.gone()
    }

    override fun onBind(bound: BinHeaderAndStatus) {
        details.text = bound.header.allocationNm
        val bs = bound.status
        status.apply {
            textColor = bs.textColor
            backgroundColor = bs.bgColor
            text = bs.binStatusNm
        }
    }

    class Diff : Differ.SimpleDiffCallback<BinHeaderAndStatus>() {

        override fun areItemsTheSame(
            oldItem: BinHeaderAndStatus,
            newItem: BinHeaderAndStatus,
            oldItemPosition: Int,
            newItemPosition: Int
        ): Boolean {
            return oldItem.sameItem(newItem)
        }

        override fun areContentsTheSame(
            oldItem: BinHeaderAndStatus,
            newItem: BinHeaderAndStatus,
            oldItemPosition: Int,
            newItemPosition: Int
        ): Boolean {
            return oldItem.header.allocationNm == newItem.header.allocationNm &&
                    oldItem.status.sameContent(newItem.status)
        }
    }
}
