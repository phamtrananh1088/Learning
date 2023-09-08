package jp.co.toukei.log.trustar.feature.dialog

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.TextView
import jp.co.toukei.log.lib.common.*
import jp.co.toukei.log.lib.rippleDrawableBorder
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.compoundDrawable
import jp.co.toukei.log.trustar.other.WorkMode
import jp.co.toukei.log.trustar.primaryButtonStyle
import jp.co.toukei.log.trustar.primaryGroundColor
import splitties.dimensions.dip
import splitties.views.*
import splitties.views.dsl.core.*

class WorkModeDialog(context: Context, private val workMode: Array<WorkMode>) {

    val start: View

    private var selected: TextView? = null

    private val fastIndex = arrayOfNulls<View>(workMode.size)

    val warning: TextView

    @JvmField
    val view = context.nestedScrollView {
        add(constraintLayout {
            verticalPadding = dip(32)

            add(textView {
                setText(R.string.work_mode_select_title)
                id = R.id.id1
                padding = dip(16)
                centerText()
            }, defaultLParams(matchConstraint, wrapContent) {
                topToTopParent()
                horizontalCenterInParent()
            })

            warning = add(textView {
                setText(R.string.work_mode_select_msg)
                id = R.id.id2
                horizontalPadding = dip(32)
                verticalPadding = dip(4)
                textColor = Color.RED
                centerText()
                gone()
            }, defaultLParams(matchConstraint, wrapContent) {
                below(R.id.id1)
                horizontalCenterInParent()
            })

            start = add(textView {
                setText(R.string.operation_start)
                primaryButtonStyle()
                textSize = 14F
                compoundDrawablePadding = dip(4)
                setCompoundDrawables(null, null, context.run {
                    compoundDrawable(
                        R.drawable.baseline_arrow_downward_24,
                        Color.WHITE,
                        dip(20)
                    )
                }, null)

                disable()
            }, defaultLParams(wrapContent, wrapContent) {
                below(R.id.id3)
                horizontalCenterInParent()
                endToEndParent()
                constrainedWidth = true
                topMargin = dip(32)
            })

            add(horizontalLayout {
                id = R.id.id3
                weightSum = workMode.size.toFloat() // fixme but works.

                val select = View.OnClickListener {
                    if (selected != it) {
                        start.enable()
                        selected?.setOff()
                        selected = it as? TextView
                        selected?.setOn()
                    }
                }

                workMode.forEachIndexed { index, workMode ->
                    add(textView {
                        setText(workMode.string)
                        fastIndex[index] = this
                        textSize = 18F
                        boldTypeface()
                        verticalPadding = dip(16)
                        horizontalPadding = dip(32)
                        centerText()
                        setOff()
                        setOnClickListener(select)
                    }, lParams(0, wrapContent) {
                        weight = 1F
                        margin = dip(8)
                    })
                }
            }, defaultLParams(wrapContent, wrapContent) {
                below(R.id.id2)
                horizontalCenterInParent()
                topMargin = dip(16)
            })
        }, lParams(wrapContent, wrapContent, gravity = gravityCenterHorizontal))
    }

    private fun TextView.setOff() {
        val colorPrimary = context.getColor(R.color.colorPrimary)
        textColor = colorPrimary
        background = rippleDrawableBorder(dip(8), Color.WHITE, colorPrimary, dip(1))
    }

    private fun TextView.setOn() {
        primaryGroundColor(dip(8))
    }

    fun getSelected(): WorkMode? {
        val s = selected ?: return null
        fastIndex.forEachIndexed { index, view -> if (view == s) return workMode[index] }
        return null
    }
}
