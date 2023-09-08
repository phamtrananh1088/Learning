package jp.co.toukei.log.trustar.chat.ui

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.TextView
import jp.co.toukei.log.lib.UI
import jp.co.toukei.log.lib.common.backgroundColorResId
import jp.co.toukei.log.lib.common.gone
import jp.co.toukei.log.lib.common.gravityCenterVertical
import jp.co.toukei.log.lib.rippleDrawable
import jp.co.toukei.log.lib.util.ValueBind
import jp.co.toukei.log.trustar.R
import splitties.dimensions.dip
import splitties.views.dsl.core.add
import splitties.views.dsl.core.frameLayout
import splitties.views.dsl.core.horizontalLayout
import splitties.views.dsl.core.imageView
import splitties.views.dsl.core.lParams
import splitties.views.dsl.core.matchParent
import splitties.views.dsl.core.textView
import splitties.views.dsl.core.wrapContent
import splitties.views.padding
import splitties.views.topPadding

class ListItemHeaderUI<T>(context: Context) : ValueBind<ListItemHeaderUI.Header<T>>(), UI {

    val content: TextView
    val comment: TextView
    val icon: View

    override val view: View = context.frameLayout {
        topPadding = dip(16)
        add(horizontalLayout {
            weightSum = 1F
            gravityCenterVertical()
            padding = dip(8)
            backgroundColorResId = R.color.listItem

            val textColor = context.getColor(R.color.textColor)
            content = add(textView {
                textSize = 16F
                padding = dip(8)
                setTextColor(textColor)
            }, lParams(wrapContent, wrapContent))
            comment = add(textView {
                textSize = 16F
                padding = dip(8)
                setTextColor(textColor)
            }, lParams(wrapContent, wrapContent) {
                weight = 1F
                marginStart = dip(64)
            })
            val icSize = dip(40)
            icon = add(imageView {
                setImageResource(R.drawable.round_add_24)
                setColorFilter(textColor)
                padding = dip(8)
                background = rippleDrawable(Int.MAX_VALUE, Color.TRANSPARENT)
                gone()
            }, lParams(icSize, icSize))
        }, lParams(matchParent, wrapContent))
    }

    override fun onBind(bound: Header<T>) {
        content.text = bound.name
        comment.text = bound.comment
    }

    class Header<T>(
        @JvmField val name: String,
        @JvmField val comment: String?,
        @JvmField val value: T,
    )
}
