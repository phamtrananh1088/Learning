package jp.co.toukei.log.trustar.feature.home.ui

import android.content.Context
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import jp.co.toukei.log.lib.common.*
import jp.co.toukei.log.lib.ext.ImageViewWithBadgeText
import jp.co.toukei.log.lib.rippleDrawable
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.addRightDateText
import jp.co.toukei.log.trustar.common.ui.DefaultRecyclerViewUI
import jp.co.toukei.log.trustar.setOldStyle
import splitties.dimensions.dip
import splitties.views.dsl.core.*
import splitties.views.dsl.material.defaultLParams
import splitties.views.horizontalPadding
import splitties.views.padding
import splitties.views.textResource
import splitties.views.verticalPadding

class DashboardFragmentUI(context: Context) : DefaultRecyclerViewUI(context) {

    val bellNormal: ImageViewWithBadgeText
    val bellImportant: ImageViewWithBadgeText
    val fullName: TextView
    val operationAmount: TextView
    val operationRemain: TextView
    val showAll: SwitchCompat
    val dateTextView: TextView = toolbar.addRightDateText()

    init {
        toolbar.setOldStyle()
        appBarLayout.apply {
            add(constraintLayout {
                backgroundColorResId = R.color.defaultBackground
                verticalPadding = dip(8)
                bellNormal = add(view(::ImageViewWithBadgeText, 0) {
                    id = R.id.id1
                    padding = dip(4)
                    setColorFilter(context.getColor(R.color.bell_normal))
                    background = rippleDrawable(-1)
                }, defaultLParams(dip(48), dip(48)) {
                    endToEndParent()
                    topToTopParent()
                    marginEnd = dip(8)
                })
                add(textView {
                    setText(R.string.dashboard_normal)
                    id = R.id.id2
                    textSize = 12F
                }, defaultLParams(wrapContent, wrapContent) {
                    below(R.id.id1)
                    constrainedWidth = true
                    startToStart = R.id.id1
                    endToEnd = R.id.id1
                })

                bellImportant = add(view(::ImageViewWithBadgeText, 0) {
                    id = R.id.id3
                    padding = dip(4)
                    setColorFilter(context.getColor(R.color.bell_important))
                    background = rippleDrawable(-1)
                }, defaultLParams(dip(48), dip(48)) {
                    topToTopParent()
                    endToStart = R.id.id1
                })
                add(textView {
                    setText(R.string.dashboard_important)
                    id = R.id.id4
                    textSize = 12F
                }, defaultLParams(wrapContent, wrapContent) {
                    below(R.id.id3)
                    constrainedWidth = true
                    startToStart = R.id.id3
                    endToEnd = R.id.id3
                })

                fullName = add(textView {
                    id = R.id.id5
                    textSize = 20F
                    padding = dip(8)
                }, defaultLParams(wrapContent, wrapContent) {
                    endToStart = R.id.id3
                    startToStartParent()
                    topToTopParent()
                    horizontalBias = 0F
                    marginStart = dip(8)
                    constrainedWidth = true
                })
            }, defaultLParams(matchParent, wrapContent))
            add(constraintLayout {
                id = R.id.id7
                padding = dip(8)
                backgroundColorResId = R.color.colorPrimary

                operationAmount = add(textView {
                    id = R.id.id8
                    boldTypeface()
                    whiteText()
                    horizontalPadding = dip(8)
                }, defaultLParams(wrapContent, wrapContent) {
                    topToTopParent()
                    startToStartParent()
                    above(R.id.id9)
                    verticalChainStyle = ConstraintLayout.LayoutParams.CHAIN_SPREAD_INSIDE
                })
                operationRemain = add(textView {
                    id = R.id.id9
                    boldTypeface()
                    whiteText()
                    horizontalPadding = dip(8)
                }, defaultLParams(wrapContent, wrapContent) {
                    bottomToBottomParent()
                    startToStartParent()
                    below(R.id.id8)
                    verticalChainStyle = ConstraintLayout.LayoutParams.CHAIN_SPREAD_INSIDE
                })
                showAll = add(switchCompact(theme = R.style.WhiteSwitchCompact) {
                    id = R.id.id10
                    textResource = R.string.show_all
                    boldTypeface()
                    whiteText()
                    isChecked = true
                }, defaultLParams(wrapContent, wrapContent) {
                    endToEndParent()
                    topToTopParent()
                    bottomToBottomParent()
                })
            }, defaultLParams(matchParent, wrapContent))
        }
    }
}
