package jp.co.toukei.log.trustar.feature.account.ui

import android.content.Context
import android.graphics.Color
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import jp.co.toukei.log.lib.common.above
import jp.co.toukei.log.lib.common.below
import jp.co.toukei.log.lib.common.bottomToBottomParent
import jp.co.toukei.log.lib.common.constraintLayout
import jp.co.toukei.log.lib.common.defaultLParams
import jp.co.toukei.log.lib.common.horizontalCenterInParent
import jp.co.toukei.log.lib.common.singleLine
import jp.co.toukei.log.lib.common.textColor
import jp.co.toukei.log.lib.common.topToTopParent
import jp.co.toukei.log.lib.rippleDrawable
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.common.ui.CoordinatorUI
import jp.co.toukei.log.trustar.whiteButtonStyle
import splitties.dimensions.dip
import splitties.views.dsl.coordinatorlayout.defaultLParams
import splitties.views.dsl.core.add
import splitties.views.dsl.core.imageView
import splitties.views.dsl.core.matchParent
import splitties.views.dsl.core.textView
import splitties.views.dsl.core.wrapContent
import splitties.views.padding

class WeatherFragmentUI(context: Context) : CoordinatorUI(context) {

    @JvmField
    val date: TextView

    @JvmField
    val weatherIcon: ImageView

    @JvmField
    val expand: ImageView

    @JvmField
    val button: TextView

    @JvmField
    val constraintLayout = coordinatorLayout.run {
        setBackgroundResource(R.drawable.window_placeholder)
        add(constraintLayout {
            padding = dip(56)
            date = add(textView {
                id = R.id.id1
                textSize = 22F
                textColor = Color.WHITE
                setShadowLayer(4F, 1F, 1F, 0x66000000)
                singleLine()
            }, defaultLParams(wrapContent, wrapContent) {
                topToTopParent()
                horizontalCenterInParent()
                topMargin = dip(8)
            })
            add(textView {
                setText(R.string.tf_weather_today)
                id = R.id.id2
                textSize = 14F
                textColor = Color.WHITE
                padding = dip(4)
                singleLine()
            }, defaultLParams(wrapContent, wrapContent) {
                horizontalCenterInParent()
                below(R.id.id1)
                above(R.id.id3)
                verticalChainStyle = ConstraintLayout.LayoutParams.CHAIN_PACKED
            })
            weatherIcon = add(imageView {
                id = R.id.id3
                padding = dip(6 * 2)
                background = rippleDrawable(-1, Color.WHITE)
            }, defaultLParams(dip(48 + 12), dip(48 + 12)) {
                horizontalCenterInParent()
                below(R.id.id2)
                above(R.id.id5)
                verticalChainStyle = ConstraintLayout.LayoutParams.CHAIN_PACKED
                topMargin = dip(4)
            })
            expand = add(imageView {
                setImageResource(R.drawable.baseline_expand_more_24)
                id = R.id.id4
                setColorFilter(Color.WHITE)
                padding = dip(4)
            }, defaultLParams(dip(24 + 8), dip(24 + 8)) {
                topToTop = R.id.id3
                bottomToBottom = R.id.id3
                verticalBias = 0.5F
                startToEnd = R.id.id3
            })
            button = add(textView {
                id = R.id.id5
                whiteButtonStyle()
            }, defaultLParams(wrapContent, wrapContent) {
                horizontalCenterInParent()
                bottomToBottomParent()
            })
        }, defaultLParams(matchParent, matchParent))
    }
}
