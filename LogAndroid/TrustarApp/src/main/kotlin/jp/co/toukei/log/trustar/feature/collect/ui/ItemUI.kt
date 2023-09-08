package jp.co.toukei.log.trustar.feature.collect.ui

import android.content.Context
import android.text.InputType
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import jp.co.toukei.log.lib.UI
import jp.co.toukei.log.lib.common.gravityCenter
import jp.co.toukei.log.lib.common.gravityCenterVertical
import jp.co.toukei.log.lib.gradientDrawable
import jp.co.toukei.log.lib.rippleDrawable
import jp.co.toukei.log.lib.util.DecimalInputFilter
import jp.co.toukei.log.lib.util.ValueBind
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.other.NumListener
import jp.co.toukei.log.trustar.roundToStringDecimal1
import splitties.dimensions.dip
import splitties.views.centerText
import splitties.views.dsl.core.add
import splitties.views.dsl.core.editText
import splitties.views.dsl.core.horizontalLayout
import splitties.views.dsl.core.imageView
import splitties.views.dsl.core.lParams
import splitties.views.dsl.core.matchParent
import splitties.views.dsl.core.textView
import splitties.views.dsl.core.wrapContent
import splitties.views.padding
import splitties.views.verticalPadding

class ItemUI(context: Context) : ValueBind<Item.Row>(), UI {

    val content: TextView
    private val exp: TextView
    private val act: TextView

    override val view: View = context.horizontalLayout {
        val gray = ContextCompat.getColor(context, R.color.defaultBackground)
        val textColor = ContextCompat.getColor(context, R.color.textDark)
        weightSum = 5F
        padding = dip(8)
        gravityCenterVertical()
        content = add(textView {
            textSize = 16F
            verticalPadding = dip(8)
        }, lParams(0, wrapContent, weight = 2F))
        val icSize = dip(40)
        add(horizontalLayout {
            gravityCenter()
            exp = add(textView {
                textSize = 18F
                minEms = 2
                centerText()
                background = gradientDrawable(dip(2), gray)
            }, lParams(wrapContent, matchParent))
        }, lParams(0, icSize, weight = 1F))
        add(horizontalLayout {
            gravityCenter()
            add(horizontalLayout {
                gravityCenterVertical()
                background = gradientDrawable(dip(4), gray)
                isFocusableInTouchMode = true
                val decrement = add(imageView {
                    setImageResource(R.drawable.round_remove_24)
                    setColorFilter(textColor)
                    padding = dip(8)
                    background = rippleDrawable(Int.MAX_VALUE)
                }, lParams(icSize, icSize))
                act = add(editText {
                    minEms = 3
                    textSize = 16F
                    centerText()
                    background = null
                    filters = arrayOf(DecimalInputFilter(4, 1))
                    imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI or EditorInfo.IME_ACTION_DONE
                    inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
                    setSelectAllOnFocus(true)
                    doAfterTextChanged {
                        val i = it?.toString()?.toDoubleOrNull()
                        if (i == null) setText("0")
                        boundValue?.row?.actualQuantity = i ?: 0.0
                    }
                }, lParams(wrapContent, wrapContent))
                val increment = add(imageView {
                    setImageResource(R.drawable.round_add_24)
                    setColorFilter(textColor)
                    padding = dip(8)
                    background = rippleDrawable(Int.MAX_VALUE)
                }, lParams(icSize, icSize))

                val l = NumListener(act, decrement, increment, 9999.9)
                decrement.apply {
                    setOnClickListener(l)
                    setOnLongClickListener(l)
                    setOnTouchListener(l)
                }
                increment.apply {
                    setOnClickListener(l)
                    setOnLongClickListener(l)
                    setOnTouchListener(l)
                }
            }, lParams(wrapContent, wrapContent))
        }, lParams(0, wrapContent, weight = 2F))
    }

    override fun onBind(bound: Item.Row) {
        val b = bound.row
        content.text = b.name
        exp.text = b.expectedQuantity.roundToStringDecimal1()
        act.text = b.actualQuantity.roundToStringDecimal1()
    }

}
