package jp.co.toukei.log.trustar.feature.home.ui

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import jp.co.toukei.log.lib.common.*
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.primaryButtonStyle
import splitties.dimensions.dip
import splitties.views.dsl.core.*
import splitties.views.horizontalPadding
import splitties.views.padding
import splitties.views.verticalPadding

class BinStartDialogUI(context: Context) {

    val currentVehicle: TextView
    val change: TextView
    val done: TextView

    private val content: View
    private val progress: View

    @JvmField
    val view = context.nestedScrollView {
        add(constraintLayout {
            verticalPadding = dip(32)

            add(textView {
                setText(R.string.current_vehicle)
                id = R.id.id1
                horizontalPadding = dip(16)
            }, defaultLParams(matchConstraint, wrapContent) {
                horizontalCenterInParent()
                topToTopParent()
            })
            currentVehicle = add(textView {
                id = R.id.id2
                padding = dip(16)
            }, defaultLParams(matchConstraint, wrapContent) {
                horizontalCenterInParent()
                below(R.id.id1)
                marginStart = dip(32)
            })
            add(textView {
                setText(R.string.operation_start_by_this_vehicle)
                id = R.id.id3
                padding = dip(16)
            }, defaultLParams(matchConstraint, wrapContent) {
                horizontalCenterInParent()
                below(R.id.id2)
            })

            change = add(textView {
                setText(R.string.change_vehicle)
                id = R.id.id4
                primaryButtonStyle()
                horizontalPadding = dip(16)
            }, defaultLParams(matchConstraint, wrapContent) {
                startToStartParent()
                endToStart = R.id.id5
                below(R.id.id3)
                topMargin = dip(32)
                bottomToBottomParent()
                horizontalChainStyle = ConstraintLayout.LayoutParams.CHAIN_SPREAD
                matchConstraintPercentWidth = 0.4F
            })
            done = add(textView {
                setText(R.string.yes)
                id = R.id.id5
                primaryButtonStyle()
                horizontalPadding = dip(16)
            }, defaultLParams(matchConstraint, wrapContent) {
                startToEnd = R.id.id4
                endToEndParent()
                topToTop = R.id.id4
                bottomToBottom = R.id.id4
                horizontalChainStyle = ConstraintLayout.LayoutParams.CHAIN_SPREAD
                matchConstraintPercentWidth = 0.4F
            })

            content = add(group {
                referencedIds = intArrayOf(
                    R.id.id1,
                    R.id.id2,
                    R.id.id3,
                    R.id.id4,
                    R.id.id5
                )
            }, defaultLParams())

            progress = add(progressBar {
                isIndeterminate = true
                padding = dip(32)
                indeterminateTintList =
                    ContextCompat.getColorStateList(context, R.color.colorPrimary)
                gone()
            }, defaultLParams(wrapContent, wrapContent) {
                topToTopParent()
                horizontalCenterInParent()
                bottomToBottomParent()
            })
        }, lParams(matchParent, wrapContent))
    }

    fun switchLoading(isLoading: Boolean) {
        if (isLoading) {
            content.gone()
            progress.show()
        } else {
            content.show()
            progress.gone()
        }
    }
}
