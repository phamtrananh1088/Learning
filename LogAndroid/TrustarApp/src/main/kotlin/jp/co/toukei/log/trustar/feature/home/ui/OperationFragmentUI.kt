package jp.co.toukei.log.trustar.feature.home.ui

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.Barrier
import androidx.constraintlayout.widget.Group
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.appbar.AppBarLayout
import jp.co.toukei.log.lib.common.above
import jp.co.toukei.log.lib.common.alignTop
import jp.co.toukei.log.lib.common.barrier
import jp.co.toukei.log.lib.common.below
import jp.co.toukei.log.lib.common.boldTypeface
import jp.co.toukei.log.lib.common.constraintLayout
import jp.co.toukei.log.lib.common.defaultLParams
import jp.co.toukei.log.lib.common.disable
import jp.co.toukei.log.lib.common.endToEndParent
import jp.co.toukei.log.lib.common.gone
import jp.co.toukei.log.lib.common.gravityCenterVertical
import jp.co.toukei.log.lib.common.group
import jp.co.toukei.log.lib.common.horizontalCenterInParent
import jp.co.toukei.log.lib.common.invisible
import jp.co.toukei.log.lib.common.matchConstraint
import jp.co.toukei.log.lib.common.nestedScrollView
import jp.co.toukei.log.lib.common.show
import jp.co.toukei.log.lib.common.showOrGone
import jp.co.toukei.log.lib.common.startToStartParent
import jp.co.toukei.log.lib.common.textColor
import jp.co.toukei.log.lib.common.topToTopParent
import jp.co.toukei.log.lib.common.verticalTo
import jp.co.toukei.log.lib.common.whiteText
import jp.co.toukei.log.lib.ext.CircleTimer
import jp.co.toukei.log.lib.gradientDrawable
import jp.co.toukei.log.lib.rippleDrawable
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.addRightDateText
import jp.co.toukei.log.trustar.common.ui.AppbarUI
import jp.co.toukei.log.trustar.db.user.entity.Work
import jp.co.toukei.log.trustar.db.user.entity.bin.Place
import jp.co.toukei.log.trustar.feature.home.data.BinDetailForWork
import jp.co.toukei.log.trustar.isMoving
import jp.co.toukei.log.trustar.isWorking
import jp.co.toukei.log.trustar.middleButtonPadding
import jp.co.toukei.log.trustar.primaryButtonStyle
import jp.co.toukei.log.trustar.setOldStyle
import splitties.dimensions.dip
import splitties.views.centerText
import splitties.views.dsl.coordinatorlayout.defaultLParams
import splitties.views.dsl.core.add
import splitties.views.dsl.core.frameLayout
import splitties.views.dsl.core.horizontalLayout
import splitties.views.dsl.core.imageView
import splitties.views.dsl.core.lParams
import splitties.views.dsl.core.matchParent
import splitties.views.dsl.core.textView
import splitties.views.dsl.core.view
import splitties.views.dsl.core.wrapContent
import splitties.views.horizontalPadding
import splitties.views.padding
import splitties.views.verticalPadding


class OperationFragmentUI(context: Context) : AppbarUI(context) {

    private val place1: TextView
    private val place2: TextView
    private val address: TextView
    private val workStatus: TextView

    private val placeGroup: Group
    private val workingGroup: Group
    private val timeGroup: Group

    private val timeRange: TextView
    private val timer: CircleTimer
    val clickInfo: ImageView
    val nimachi: View
    private val flex: FlexboxLayout
    val dateTextView: TextView = toolbar.addRightDateText()

    init {
        toolbar.setOldStyle()
        coordinatorLayout.run {
            add(nestedScrollView {
                add(constraintLayout {
                    padding = dip(16)

                    place1 = add(textView {
                        id = R.id.id1
                        boldTypeface()
                        textSize = 16F
                        verticalPadding = dip(4)
                    }, defaultLParams(wrapContent, wrapContent) {
                        topToTopParent()
                        startToStartParent()
                    })

                    add(horizontalLayout {
                        id = R.id.id2
                        weightSum = 1F

                        gravityCenterVertical()

                        place2 = add(textView {
                            boldTypeface()
                            textSize = 16F
                            verticalPadding = dip(4)
                        }, lParams(0, wrapContent, weight = 1F))

                        clickInfo = add(imageView {
                            setImageResource(R.drawable.baseline_info_24)
                        }, lParams(dip(24), dip(24)))
                    }, defaultLParams(matchConstraint, wrapContent) {
                        below(R.id.id1)
                        startToStartParent()
                        endToEndParent()
                    })
                    address = add(textView {
                        id = R.id.id3
                        textSize = 12F
                    }, defaultLParams(matchConstraint, wrapContent) {
                        below(R.id.id2)
                        horizontalCenterInParent()
                    })

                    placeGroup = add(group {
                        id = R.id.id10
                        referencedIds = intArrayOf(R.id.id2, R.id.id3)
                        invisible()
                    }, defaultLParams())

                    timer = add(view(::CircleTimer) {
                        id = R.id.id4

                        val w = dip(2) * 1.2.toFloat()
                        val tw = w * 16
                        setCenterTextSize(tw)
                        setUpdateRate(200)
                        setRingWidth(w, w)
                        setAlphaAndSecondColor(0x60, Color.BLACK)
                    }, defaultLParams(wrapContent, dip(120)) {
                        topToTopParent()
                        endToEndParent()
                        constrainedWidth = true
                        above(R.id.id7)
                    })

                    val orangeColor = context.getColor(R.color.accentOrange)

                    workStatus = add(textView {
                        id = R.id.id6
                        textSize = 40F
                        textColor = orangeColor
                    }, defaultLParams(wrapContent, wrapContent) {
                        verticalTo(R.id.id4, 0.5F)
                        constrainedWidth = true
                        startToStartParent()
                        endToStart = R.id.id4
                        horizontalBias = 0F
                    })
                    workingGroup = add(group {
                        id = R.id.id11
                        referencedIds = intArrayOf(R.id.id4, R.id.id6)
                        invisible()
                    }, defaultLParams())

                    add(barrier {
                        id = R.id.id7
                        type = Barrier.BOTTOM
                        referencedIds = intArrayOf(R.id.id3, R.id.id4, R.id.id6)
                    }, defaultLParams())

                    nimachi = add(textView {
                        setText(R.string.incidental_sheets)
                        id = R.id.id13
                        primaryButtonStyle()
                        middleButtonPadding()
                    }, defaultLParams(wrapContent, wrapContent) {
                        below(R.id.id7)
                        endToEndParent()
                        topMargin = dip(16)
                    })

                    timeRange = add(textView {
                        id = R.id.id8
                        textSize = 16F
                    }, defaultLParams(matchConstraint, wrapContent) {
                        alignTop(R.id.id13)
                        startToStartParent()
                        endToStart = R.id.id13
                    })

                    add(textView {
                        setText(R.string.work_notice_of_time)
                        id = R.id.id9
                        textColor = orangeColor
                        textSize = 12F
                    }, defaultLParams(matchConstraint, wrapContent) {
                        below(R.id.id8)
                        startToStartParent()
                        endToStart = R.id.id13
                    })

                    timeGroup = add(group {
                        id = R.id.id12
                        referencedIds = intArrayOf(R.id.id8, R.id.id9)
                        invisible()
                    }, defaultLParams())

                    add(barrier {
                        id = R.id.id14
                        type = Barrier.BOTTOM
                        referencedIds = intArrayOf(R.id.id13, R.id.id9)
                    }, defaultLParams())

                    flex = add(view(::FlexboxLayout) {
                        flexWrap = FlexWrap.WRAP
                        alignItems = AlignItems.STRETCH
                    }, defaultLParams(matchConstraint, wrapContent) {
                        horizontalCenterInParent()
                        below(R.id.id14)
                        topMargin = dip(16)
                    })
                }, lParams(matchParent, matchParent))
            }, defaultLParams(matchParent, matchParent) {
                behavior = AppBarLayout.ScrollingViewBehavior()
            })
        }
    }

    fun setData(
        works: List<Work>?,
        item: BinDetailForWork?,
        isWorkingStyle: Boolean,
        callback: (Context.(work: Work) -> Unit)?,
    ) {
        setData(works, item, item?.detail?.place, isWorkingStyle, callback)
    }

    fun setData(
        works: List<Work>?,
        item: BinDetailForWork?,
        place: Place?,
        isWorkingStyle: Boolean,
        callback: (Context.(work: Work) -> Unit)?,
    ) {
        val context = coordinatorLayout.context
        val gray = context.getColor(R.color.gray_out)

        workingGroup.invisible()
        placeGroup.gone()
        timeGroup.gone()
        if (item != null) {
            if (isWorkingStyle) workingGroup.show() else placeGroup.show()
        }
        item?.detail?.displayPlanTime.let {
            timeRange.text = it
            if (it != null) timeGroup.show()
        }
        if (isWorkingStyle) place1.invisible() else place1.show()
        nimachi.showOrGone(item != null && item.incidentalEnabled)

        workStatus.text = null
        place1.text = place?.nm1
        place2.text = place?.nm2
        address.text = place?.addr

        val color = context.getColor(
            if (isWorkingStyle) R.color.accentOrange else R.color.colorPrimary
        )
        clickInfo.setColorFilter(if (item?.detail?.hasNotice == true) Color.RED else color)

        timer.apply {
            setCurrentTimeMillis(3600_000, item?.workStartTime)
            setPaintColor(item?.delayRankColor ?: 0xff92d050.toInt())
            setBelowText(item?.workStateText)
        }
        val needMatchCd = item?.detail?.run {
            statusType?.run {
                if (isMoving() || isWorking()) work?.workCd else null
            }
        }

        flex.apply {
            removeAllViewsInLayout()

            works?.forEach {
                val matched = needMatchCd != null && needMatchCd == it.workCd

                if (matched) {
                    workStatus.text = context.getString(R.string.work_status_s1_ing, it.workNm)
                }

                add(frameLayout {
                    padding = dip(8)
                    add(textView {
                        boldTypeface()
                        centerText()
                        whiteText()
                        textSize = if (matched) 24F else 16F
                        verticalPadding = dip(if (matched) 32 else 16)
                        horizontalPadding = dip(8)

                        text = if (isWorkingStyle && matched) {
                            context.getString(R.string.work_status_s1_end, it.workNm)
                        } else {
                            it.workNm
                        }

                        if (isWorkingStyle && !matched) {
                            disable()
                            background = gradientDrawable(dip(8), gray)
                        } else {
                            background = rippleDrawable(dip(8), color, Color.WHITE)
                            if (callback != null)
                                setOnClickListener { v -> callback(v.context, it) }
                        }
                    }, lParams(matchParent, matchParent))
                }, FlexboxLayout.LayoutParams(wrapContent, wrapContent).apply {
                    if (matched) order = -1
                    alignSelf = AlignItems.STRETCH
                    flexBasisPercent = if (matched) 1F else 0.4999F
                })
            }
        }
    }
}
