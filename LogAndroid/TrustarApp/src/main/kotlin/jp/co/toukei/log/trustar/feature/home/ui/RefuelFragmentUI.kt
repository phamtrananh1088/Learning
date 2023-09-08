package jp.co.toukei.log.trustar.feature.home.ui

import android.content.Context
import android.graphics.Color
import android.text.InputType
import android.view.Gravity
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.appbar.AppBarLayout
import jp.co.toukei.log.lib.common.below
import jp.co.toukei.log.lib.common.boldTypeface
import jp.co.toukei.log.lib.common.constraintLayout
import jp.co.toukei.log.lib.common.defaultLParams
import jp.co.toukei.log.lib.common.horizontalCenterInParent
import jp.co.toukei.log.lib.common.nestedScrollView
import jp.co.toukei.log.lib.common.startToStartParent
import jp.co.toukei.log.lib.common.topToTopParent
import jp.co.toukei.log.lib.gradientDrawableBorder
import jp.co.toukei.log.lib.util.DecimalInputFilter
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.addRightDateText
import jp.co.toukei.log.trustar.common.ui.AppbarUI
import jp.co.toukei.log.trustar.primaryButtonStyle
import jp.co.toukei.log.trustar.roundedSelectorStyle
import jp.co.toukei.log.trustar.setOldStyle
import splitties.dimensions.dip
import splitties.views.dsl.coordinatorlayout.defaultLParams
import splitties.views.dsl.core.add
import splitties.views.dsl.core.editText
import splitties.views.dsl.core.lParams
import splitties.views.dsl.core.matchParent
import splitties.views.dsl.core.textView
import splitties.views.dsl.core.wrapContent
import splitties.views.padding
import splitties.views.verticalPadding


class RefuelFragmentUI(context: Context) : AppbarUI(context) {

    val allocationItem: TextView
    val vehicle: TextView
    val currentAmount: TextView
    val fuel: TextView
    val refuelAmount: EditText
    val paymentAmount: EditText
    val button: TextView
    val dateTextView: TextView = toolbar.addRightDateText()

    init {
        toolbar.setOldStyle()
        coordinatorLayout.run {
            add(nestedScrollView {
                val colorGray = context.getColor(R.color.gray_out)

                fun EditText.style2() {
                    padding = dip(12)
                    background = gradientDrawableBorder(dip(1), Color.WHITE, colorGray, dip(1))
                    inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_CLASS_NUMBER
                }

                add(constraintLayout {
                    maxWidth = dip(512)
                    padding = dip(16)

                    allocationItem = add(textView {
                        id = R.id.id9
                        roundedSelectorStyle()
                    }, defaultLParams(matchParent, wrapContent) {
                        topToTopParent()
                        horizontalCenterInParent()
                    })
                    vehicle = add(textView {
                        id = R.id.id1
                        textSize = 16F
                        verticalPadding = dip(12)
                    }, defaultLParams(matchParent, wrapContent) {
                        below(R.id.id9)
                        horizontalCenterInParent()
                    })
                    add(textView {
                        setText(R.string.refuel_amount_of_the_operation)
                        id = R.id.id2
                        textSize = 14F
                    }, defaultLParams(wrapContent, wrapContent) {
                        startToStartParent()
                        topToTop = R.id.id3
                        bottomToBottom = R.id.id3
                    })
                    currentAmount = add(textView {
                        id = R.id.id3
                        boldTypeface()
                        textSize = 18F
                        padding = dip(12)
                    }, defaultLParams(wrapContent, wrapContent) {
                        below(R.id.id1)
                        startToEnd = R.id.id2
                    })
                    fuel = add(textView {
                        id = R.id.id4
                        roundedSelectorStyle()
                    }, defaultLParams(matchParent, wrapContent) {
                        below(R.id.id3)
                        horizontalCenterInParent()
                    })
                    add(textView {
                        setText(R.string.refuel_amount_in_liter)
                        id = R.id.id5
                        textSize = 14F
                        verticalPadding = dip(12)
                    }, defaultLParams(wrapContent, wrapContent) {
                        startToStartParent()
                        below(R.id.id4)
                    })
                    refuelAmount = add(editText {
                        id = R.id.id6
                        style2()
                        filters = arrayOf(DecimalInputFilter(4, 1))
                        imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI or EditorInfo.IME_ACTION_NEXT
                    }, defaultLParams(matchParent, wrapContent) {
                        below(R.id.id5)
                        horizontalCenterInParent()
                    })
                    add(textView {
                        setText(R.string.amount_include_tax)
                        id = R.id.id7
                        textSize = 14F
                        verticalPadding = dip(12)
                    }, defaultLParams(wrapContent, wrapContent) {
                        startToStartParent()
                        below(R.id.id6)
                    })
                    paymentAmount = add(editText {
                        id = R.id.id8
                        style2()
                        filters = arrayOf(DecimalInputFilter(7, 0))
                        imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI or EditorInfo.IME_ACTION_DONE
                    }, defaultLParams(matchParent, wrapContent) {
                        below(R.id.id7)
                        horizontalCenterInParent()
                    })
                    button = add(textView {
                        setText(R.string.confirm_input)
                        primaryButtonStyle()
                    }, defaultLParams(wrapContent, wrapContent) {
                        below(R.id.id8)
                        horizontalCenterInParent()
                        topMargin = dip(32)
                    })

                }, lParams(matchParent, matchParent) {
                    topMargin = dip(8)
                    gravity = Gravity.CENTER_HORIZONTAL
                })
            }, defaultLParams(matchParent, matchParent) {
                behavior = AppBarLayout.ScrollingViewBehavior()
            })
        }
    }
}
