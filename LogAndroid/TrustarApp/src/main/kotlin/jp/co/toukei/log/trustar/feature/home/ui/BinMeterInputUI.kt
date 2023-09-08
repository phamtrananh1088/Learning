package jp.co.toukei.log.trustar.feature.home.ui

import android.content.Context
import android.graphics.Color
import android.text.InputType
import android.text.method.DigitsKeyListener
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import jp.co.toukei.log.lib.common.*
import jp.co.toukei.log.lib.gradientDrawableBorder
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.primaryButtonStyle
import splitties.dimensions.dip
import splitties.views.dsl.core.*
import splitties.views.horizontalPadding
import splitties.views.padding

class BinMeterInputUI(context: Context) {

    val title1: TextView
    val title2: TextView
    val input: EditText

    val cancel: TextView
    val confirm: TextView

    @JvmField
    val view = context.nestedScrollView {
        add(constraintLayout {
            padding = dip(8)
            title1 = add(textView {
                id = R.id.id1
                padding = dip(8)
            }, defaultLParams(matchConstraint, wrapContent) {
                horizontalCenterInParent()
                topToTopParent()
            })
            title2 = add(textView {
                id = R.id.id2
                padding = dip(8)
            }, defaultLParams(matchConstraint, wrapContent) {
                horizontalCenterInParent()
                below(R.id.id1)
            })

            input = add(editText {
                id = R.id.id3
                singleLine()
                background =
                    gradientDrawableBorder(dip(2), Color.TRANSPARENT, 0xffcccccc.toInt(), dip(1))
                imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI or EditorInfo.IME_ACTION_DONE
                inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_CLASS_NUMBER
                keyListener = DigitsKeyListener.getInstance("0123456789")
            }, defaultLParams(matchConstraint, wrapContent) {
                horizontalCenterInParent()
                below(R.id.id2)
                margin = dip(4)
            })

            cancel = add(textView {
                setText(R.string.cancel)
                id = R.id.id4
                primaryButtonStyle()
                horizontalPadding = dip(16)
            }, defaultLParams(wrapContent, wrapContent) {
                startToStartParent()
                endToStart = R.id.id5
                below(R.id.id3)
                topMargin = dip(32)
                bottomToBottomParent()
                matchConstraintPercentWidth = 0.4F
                constrainedWidth = true
            })
            confirm = add(textView {
                setText(R.string.confirm_input)
                id = R.id.id5
                primaryButtonStyle()
                horizontalPadding = dip(16)
            }, defaultLParams(wrapContent, wrapContent) {
                startToEnd = R.id.id4
                endToEndParent()
                topToTop = R.id.id4
                bottomToBottom = R.id.id4
                matchConstraintPercentWidth = 0.4F
                constrainedWidth = true
            })
        }, lParams(matchParent, wrapContent))
    }
}
