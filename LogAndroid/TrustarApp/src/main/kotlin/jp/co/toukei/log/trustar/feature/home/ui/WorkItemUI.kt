package jp.co.toukei.log.trustar.feature.home.ui

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.helper.widget.Flow
import androidx.constraintlayout.widget.Barrier
import jp.co.toukei.log.lib.UI
import jp.co.toukei.log.lib.common.barrier
import jp.co.toukei.log.lib.common.below
import jp.co.toukei.log.lib.common.boldTypeface
import jp.co.toukei.log.lib.common.bottomToBottomParent
import jp.co.toukei.log.lib.common.constraintLayout
import jp.co.toukei.log.lib.common.defaultLParams
import jp.co.toukei.log.lib.common.endToEndParent
import jp.co.toukei.log.lib.common.flow
import jp.co.toukei.log.lib.common.gone
import jp.co.toukei.log.lib.common.matchConstraint
import jp.co.toukei.log.lib.common.show
import jp.co.toukei.log.lib.common.showOrGone
import jp.co.toukei.log.lib.common.startToStartParent
import jp.co.toukei.log.lib.common.textColor
import jp.co.toukei.log.lib.common.topToTopParent
import jp.co.toukei.log.lib.common.verticalCenterInParent
import jp.co.toukei.log.lib.common.verticalTo
import jp.co.toukei.log.lib.common.whiteText
import jp.co.toukei.log.lib.rippleDrawable
import jp.co.toukei.log.lib.util.Differ
import jp.co.toukei.log.lib.util.ValueBind
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.feature.home.data.BinDetailForListItem
import splitties.dimensions.dip
import splitties.views.backgroundColor
import splitties.views.centerText
import splitties.views.dsl.core.add
import splitties.views.dsl.core.imageView
import splitties.views.dsl.core.textView
import splitties.views.dsl.core.wrapContent
import splitties.views.padding
import splitties.views.textResource
import splitties.views.verticalPadding

class WorkItemUI(context: Context) : UI, ValueBind<BinDetailForListItem>() {

    private val nameAndTime: TextView
    private val appointedTime: TextView
    private val target: TextView
    private val address: TextView
    private val workStatus: TextView
    private val workL: TextView
    private val wrong: TextView
    val clickInfo: ImageView

    override val view: View = context.constraintLayout {

        padding = dip(8)

        clickInfo = add(imageView {
            setImageResource(R.drawable.baseline_info_24)
            id = R.id.id1
            padding = dip(8)
            background = rippleDrawable(-1)
        }, defaultLParams(dip(24 + 16), dip(24 + 16)) {
            endToEndParent()
            verticalCenterInParent()
        })

        workStatus = add(textView {
            id = R.id.id2
            minEms = 4
            centerText()
            textSize = 12F
            includeFontPadding = false
            verticalPadding = dip(2)
        }, defaultLParams(wrapContent, wrapContent) {
            topToTopParent()
            endToStart = R.id.id1
            marginEnd = dip(8)
        })

        wrong = add(textView {
            id = R.id.id7
            gone()
            padding = dip(2)
            textColor = Color.RED
            textSize = 8F
            centerText()
        }, defaultLParams(wrapContent, matchConstraint) {
            verticalTo(R.id.id2, 0.5F)
            startToEnd = R.id.id2
        })

        workL = add(textView {
            id = R.id.id6
            whiteText()
            textSize = 12F
            includeFontPadding = false
            padding = dip(2)
        }, defaultLParams(wrapContent, wrapContent) {
            topToTopParent()
            endToStart = R.id.id2
            marginEnd = dip(8)
        })

        nameAndTime = add(textView {
            id = R.id.id3
            boldTypeface()
        }, defaultLParams(wrapContent, wrapContent))
        appointedTime = add(textView {
            id = R.id.id16
            boldTypeface()
            textSize = 12F
        }, defaultLParams(wrapContent, wrapContent))

        add(flow {
            id = R.id.id19
            referencedIds = intArrayOf(R.id.id3, R.id.id16)
            setHorizontalBias(0F)
            setVerticalBias(0F)
            setHorizontalStyle(Flow.CHAIN_PACKED)
            setWrapMode(Flow.WRAP_CHAIN)
            setHorizontalGap(dip(8))
        }, defaultLParams(matchConstraint, wrapContent) {
            startToStartParent()
            topToTopParent()
            endToStart = R.id.id6
        })

        add(barrier {
            id = R.id.id4
            referencedIds = intArrayOf(R.id.id2, R.id.id19, R.id.id6)
            type = Barrier.BOTTOM
        }, defaultLParams())
        target = add(textView {
            id = R.id.id5
            boldTypeface()
        }, defaultLParams(matchConstraint, wrapContent) {
            startToStartParent()
            below(R.id.id4)
            endToStart = R.id.id1
            horizontalBias = 0F
        })
        address = add(textView(), defaultLParams(matchConstraint, wrapContent) {
            below(R.id.id5)
            startToStartParent()
            endToStart = R.id.id1
            bottomToBottomParent()
            horizontalBias = 0F
        })
    }

    override fun onBind(bound: BinDetailForListItem) {
        val detail = bound.detail

        val place = detail.place

        nameAndTime.text = bound.workNameAndTimeTitle
        bound.appointedString.let {
            appointedTime.text = it
            appointedTime.showOrGone(!it.isNullOrEmpty())
        }
        target.text = place.placeNameFull
        address.text = place.addr

        workStatus.apply {
            val s = bound.status
            textColor = s.textColor
            backgroundColor = s.bgColor
            text = s.workStatusNm
            view.background = rippleDrawable(0, s.itemColor)
        }
        workL.apply {
            show()
            if (detail.placeLocation != null) {
                val l = bound.deviation
                showOrGone(l != null)
                text = l
                backgroundColor = if (bound.inRange) Color.RED else Color.GRAY
            } else {
                backgroundColor = Color.RED
                textResource = R.string.location_0
            }
        }
        bound.additionalRedText.also {
            wrong.text = it
            if (it == null) wrong.gone()
            else wrong.show()
        }

        clickInfo.apply {
            val color = if (detail.hasNotice) Color.RED else context.getColor(R.color.colorPrimary)
            setColorFilter(color)
        }
    }

    class Diff : Differ.DiffCallback<BinDetailForListItem> {

        override fun areItemsTheSame(
            oldItem: BinDetailForListItem,
            newItem: BinDetailForListItem,
            oldItemPosition: Int,
            newItemPosition: Int,
        ): Boolean {
            return oldItem.sameItem(newItem)
        }

        override fun areContentsTheSame(
            oldItem: BinDetailForListItem,
            newItem: BinDetailForListItem,
            oldItemPosition: Int,
            newItemPosition: Int,
        ): Boolean {
            val d = oldItem.detail.updatedDate
            return d != null && d == newItem.detail.updatedDate && oldItem.deviation == newItem.deviation
        }

        override fun getChangePayload(
            oldItem: BinDetailForListItem,
            newItem: BinDetailForListItem,
            oldItemPosition: Int,
            newItemPosition: Int,
        ): Any = Unit
    }
}
