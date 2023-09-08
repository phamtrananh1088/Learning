package jp.co.toukei.log.trustar.feature.nimachi.ui

import android.content.Context
import android.view.View
import android.widget.TextView
import jp.co.toukei.log.lib.UI
import jp.co.toukei.log.lib.common.*
import jp.co.toukei.log.lib.gradientDrawable
import jp.co.toukei.log.lib.rippleDrawable
import jp.co.toukei.log.lib.util.Differ
import jp.co.toukei.log.lib.util.ValueBind
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.db.user.embedded.IncidentalListItem
import splitties.dimensions.dip
import splitties.views.centerText
import splitties.views.dsl.core.*
import splitties.views.padding
import splitties.views.textResource

class SheetListItemUI(context: Context) : ValueBind<IncidentalListItem>(), UI {

    private val shipper: TextView
    private val r: TextView

    override fun onBind(bound: IncidentalListItem) {
        shipper.text = bound.shipperNm
        when (bound.signStatus) {
            0 -> r.apply {
                show()
                textResource = R.string.unsigned
                background = gradientDrawable(0, 0xff58d489.toInt())
            }
            1 -> r.apply {
                show()
                textResource = R.string.signed
                background = gradientDrawable(0, 0xffdfcdbc.toInt())
            }
            else -> r.gone()
        }
    }

    override val view: View = context.horizontalLayout {
        padding = dip(8)
        setLayoutParams(matchParent, wrapContent)
        weightSum = 1F
        gravityCenterVertical()
        background = rippleDrawable(0)
        shipper = add(textView {
            textSize = 18F
            padding = dip(8)
        }, lParams(0, wrapContent, weight = 1F))
        r = add(textView {
            textSize = 14F
            whiteText()
            padding = dip(4)
            centerText()
        }, lParams(wrapContent, wrapContent))
    }

    class Diff : Differ.SimpleDiffCallback<IncidentalListItem>() {

        override fun areItemsTheSame(
            oldItem: IncidentalListItem,
            newItem: IncidentalListItem,
            oldItemPosition: Int,
            newItemPosition: Int
        ): Boolean {
            return oldItem === newItem || oldItem.sameItem(newItem)
        }

        override fun areContentsTheSame(
            oldItem: IncidentalListItem,
            newItem: IncidentalListItem,
            oldItemPosition: Int,
            newItemPosition: Int
        ): Boolean {
            return oldItem.shipperNm == newItem.shipperNm &&
                    oldItem.signStatus == newItem.signStatus &&
                    oldItem.javaClass === newItem.javaClass
        }
    }
}
