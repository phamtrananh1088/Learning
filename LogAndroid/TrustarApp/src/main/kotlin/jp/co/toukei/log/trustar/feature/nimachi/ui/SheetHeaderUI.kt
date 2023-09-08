package jp.co.toukei.log.trustar.feature.nimachi.ui

import android.content.Context
import android.view.View
import android.widget.TextView
import jp.co.toukei.log.lib.UI
import jp.co.toukei.log.lib.common.*
import jp.co.toukei.log.lib.util.ValueBind
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.feature.nimachi.data.IncidentalItemData
import jp.co.toukei.log.trustar.middleButtonPadding
import jp.co.toukei.log.trustar.primaryButtonStyle
import jp.co.toukei.log.trustar.primaryColorText
import splitties.dimensions.dip
import splitties.views.dsl.core.add
import splitties.views.dsl.core.margin
import splitties.views.dsl.core.textView
import splitties.views.dsl.core.wrapContent
import splitties.views.padding
import splitties.views.textResource
import splitties.views.verticalPadding

class SheetHeaderUI(context: Context) : ValueBind<IncidentalItemData>(), UI {

    private val name: TextView
    val edit: TextView
    private val content: TextView
    val bottomEnd: TextView

    override val view: View = context.constraintLayout {
        padding = dip(16)
        setLayoutParams()
        name = add(textView {
            id = R.id.id1
            textSize = 18F
            primaryColorText()
        }, defaultLParams(matchConstraint, wrapContent) {
            marginStart = dip(8)
            startToStartParent()
            endToStart = R.id.id2
            verticalTo(R.id.id2, 0.5F)
        })
        edit = add(textView {
            id = R.id.id2
            primaryButtonStyle()
            middleButtonPadding()
        }, defaultLParams(wrapContent, wrapContent) {
            endToEndParent()
            topToTopParent()
        })
        add(textView {
            setText(R.string.incidental_sheet_addition_work_)
            id = R.id.id3
            textSize = 16F
            verticalPadding = dip(4)
        }, defaultLParams(wrapContent, wrapContent) {
            startToStartParent()
            below(R.id.id1)
            topMargin = dip(8)
        })
        content = add(textView {
            id = R.id.id4
            textSize = 16F
            verticalPadding = dip(4)
        }, defaultLParams(wrapContent, wrapContent) {
            horizontalCenterInParent()
            horizontalBias = 0F
            below(R.id.id3)
            topMargin = dip(8)
            marginStart = dip(8)
            constrainedWidth = true
        })
        bottomEnd = add(textView {
            gone()
            primaryButtonStyle()
            middleButtonPadding()
        }, defaultLParams(wrapContent, wrapContent) {
            endToEndParent()
            bottomToBottomParent()
            below(R.id.id4)
            margin = dip(8)
        })
    }

    override fun onBind(bound: IncidentalItemData) {
        name.text = bound.shipperNm
        val n = bound.joinedWorkName
        if (n != null) content.text = n
        else content.textResource = R.string.unselected
    }
}
