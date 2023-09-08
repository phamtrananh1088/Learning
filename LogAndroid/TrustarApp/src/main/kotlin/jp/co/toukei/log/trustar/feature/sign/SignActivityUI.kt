package jp.co.toukei.log.trustar.feature.sign

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.TextView
import com.zeevox.pie.paint.Painting
import jp.co.toukei.log.lib.common.*
import jp.co.toukei.log.lib.gradientDrawableBorder
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.primaryGroundColorRipple
import splitties.dimensions.dip
import splitties.views.backgroundColor
import splitties.views.dsl.core.*
import splitties.views.horizontalPadding
import splitties.views.padding
import splitties.views.verticalPadding

class SignActivityUI(context: Context) {

    val painter: Painting

    val done: View

    val view = context.constraintLayout {
        backgroundColor = Color.WHITE

        padding = dip(8)

        add(textView {
            setText(R.string.sign_inside_area)
            id = R.id.id1
            padding = dip(4)
        }, defaultLParams(matchConstraint, wrapContent) {
            topToTopParent()
            horizontalCenterInParent()
            horizontalBias = 0F
        })

        add(horizontalLayout {
            padding = dip(2)
            background = gradientDrawableBorder(dip(2), Color.WHITE, Color.GRAY, dip(1))
            painter = add(view(::Painting) {}, lParams(matchParent, matchParent))
        }, defaultLParams(matchParent, matchConstraint) {
            below(R.id.id1)
            horizontalCenterInParent()
            above(R.id.id3)
            bottomMargin = dip(8)
        })

        fun TextView.style() {
            primaryGroundColorRipple(dip(4))
            textSize = 16F
            verticalPadding = dip(4)
            horizontalPadding = dip(32)
        }

        add(textView {
            setText(R.string.clear)
            id = R.id.id3
            style()
            setOnClickListener {
                /*署名・クリア押下時に起動されるイベント*/
                painter.clear()
            }
        }, defaultLParams(wrapContent, wrapContent) {
            startToStartParent()
            endToStart = R.id.id4
            bottomToBottomParent()
        })

        done = add(textView {
            setText(R.string.determine)
            id = R.id.id4
            style()
        }, defaultLParams(wrapContent, wrapContent) {
            startToEnd = R.id.id3
            topToTop = R.id.id3
            endToEndParent()
            bottomToBottomParent()
        })
    }
}
