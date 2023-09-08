package jp.co.toukei.log.trustar.feature.nimachi.ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.TextView
import jp.co.toukei.log.lib.UI
import jp.co.toukei.log.lib.common.*
import jp.co.toukei.log.lib.rippleDrawable
import jp.co.toukei.log.lib.util.Differ
import jp.co.toukei.log.lib.util.ValueBind
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.feature.nimachi.data.EditedTime
import jp.co.toukei.log.trustar.feature.nimachi.data.TimeItem
import splitties.dimensions.dip
import splitties.views.dsl.core.add
import splitties.views.dsl.core.textView
import splitties.views.dsl.core.wrapContent
import splitties.views.padding

class TimeItemUI<T : TimeItem>(context: Context) : ValueBind<T>(), UI {

    private val index: TextView
    private val begin: TextView
    private val end: TextView

    override val view: View = context.constraintLayout {
        setLayoutParams()

        index = add(textView {
            id = R.id.id1
            textSize = 14F
            padding = dip(8)
            boldTypeface()
        }, defaultLParams(wrapContent, wrapContent) {
            startToStartParent()
            topToTopParent()
            bottomToBottomParent()
        })

        begin = add(textView {
            id = R.id.id2
            textSize = 16F
            padding = dip(8)
        }, defaultLParams(wrapContent, wrapContent) {
            startToStartParent()
            endToStart = R.id.id3
            horizontalBias = 0.85F
            topToTopParent()
            bottomToBottomParent()
        })

        add(textView {
            text = "〜"
            id = R.id.id3
            textSize = 16F
            padding = dip(8)
        }, defaultLParams(wrapContent, wrapContent) {
            startToStartParent()
            endToEndParent()
            topToTopParent()
            bottomToBottomParent()
        })
        end = add(textView {
            id = R.id.id4
            textSize = 16F
            padding = dip(8)
        }, defaultLParams(wrapContent, wrapContent) {
            startToEnd = R.id.id3
            horizontalBias = 0.15F
            endToEndParent()
            topToTopParent()
            bottomToBottomParent()
        })
    }

    override fun onBind(bound: T) {
        begin.text = bound.beginDateString.orEmpty()
        end.text = bound.endDateString.orEmpty()

        view.background = if (bound is EditedTime && bound.hasChanged()) {
            rippleDrawable(0, bound.color)
        } else {
            rippleDrawable(0)
        }
    }

    override fun onBind(value: T, position: Int, payloads: List<Any>) {
        super.onBind(value, position, payloads)
        @SuppressLint("SetTextI18n")
        index.text = (1 + position).toString()
    }

    class Diff<T : TimeItem> : Differ.SimpleDiffCallback<T>() {

        override fun areItemsTheSame(
            oldItem: T,
            newItem: T,
            oldItemPosition: Int,
            newItemPosition: Int
        ): Boolean {
            return oldItem.sameItem(newItem)
        }

        override fun areContentsTheSame(
            oldItem: T,
            newItem: T,
            oldItemPosition: Int,
            newItemPosition: Int
        ): Boolean {
            return oldItemPosition == newItemPosition &&
                    oldItem.beginDateString == newItem.beginDateString &&
                    oldItem.endDateString == newItem.endDateString
        }
    }
}
