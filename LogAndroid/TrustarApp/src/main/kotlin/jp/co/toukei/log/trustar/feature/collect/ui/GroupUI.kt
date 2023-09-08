package jp.co.toukei.log.trustar.feature.collect.ui

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import jp.co.toukei.log.lib.UI
import jp.co.toukei.log.lib.common.boldTypeface
import jp.co.toukei.log.lib.common.gravityCenterVertical
import jp.co.toukei.log.lib.gradientDrawable
import jp.co.toukei.log.lib.rippleDrawable
import jp.co.toukei.log.lib.util.ValueBind
import jp.co.toukei.log.trustar.R
import splitties.dimensions.dip
import splitties.views.centerText
import splitties.views.dsl.core.*
import splitties.views.padding

class GroupUI(context: Context) : ValueBind<Item.Group>(), UI {

    private val title: TextView
    val addButton: View

    override val view = context.verticalLayout {
        weightSum = 1F

        val textC = ContextCompat.getColor(context, R.color.gray_out)
        val textColor = ContextCompat.getColor(context, R.color.textDark)
        dividerDrawable = gradientDrawable(0, textC).apply { setSize(dip(1), dip(1)) }
        showDividers = LinearLayout.SHOW_DIVIDER_BEGINNING

        add(horizontalLayout {
            weightSum = 1F
            padding = dip(8)
            gravityCenterVertical()
            title = add(textView {
                textSize = 16F
            }, lParams(0, wrapContent, weight = 1F))
            val icSize = dip(40)
            addButton = add(imageView {
                setImageResource(R.drawable.round_add_24)
                setColorFilter(textColor)
                padding = dip(8)
                background = rippleDrawable(Int.MAX_VALUE)
            }, lParams(icSize, icSize))
        }, lParams(matchParent, wrapContent))

        add(horizontalLayout {
            weightSum = 5F
            padding = dip(8)
            gravity = Gravity.CENTER_VERTICAL or Gravity.END
            add(textView {
                setText(R.string.expected_quantity)
                centerText()
                textSize = 18F
                boldTypeface()
            }, lParams(0, wrapContent, weight = 1F))
            add(textView {
                setText(R.string.actual_quantity)
                centerText()
                textSize = 18F
                boldTypeface()
            }, lParams(0, wrapContent, weight = 2F))
        }, lParams(matchParent, wrapContent))
    }

    override fun onBind(bound: Item.Group) {
        title.text = bound.group.collectionClassName
    }
}
