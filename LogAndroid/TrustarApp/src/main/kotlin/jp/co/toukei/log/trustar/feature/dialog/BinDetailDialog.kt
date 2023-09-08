package jp.co.toukei.log.trustar.feature.dialog

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import jp.co.toukei.log.lib.common.*
import jp.co.toukei.log.lib.gradientDrawable
import jp.co.toukei.log.lib.rippleDrawable
import jp.co.toukei.log.lib.textViewMakeDialOnClick
import jp.co.toukei.log.lib.textViewSendMailOnClick
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.compoundDrawable
import jp.co.toukei.log.trustar.other.BinDetailData
import jp.co.toukei.log.trustar.primaryButtonSmallStyle
import jp.co.toukei.log.trustar.textViewOpenMapsOnClick
import splitties.dimensions.dip
import splitties.views.dsl.core.*
import splitties.views.gravityCenterHorizontal
import splitties.views.horizontalPadding
import splitties.views.textResource
import splitties.views.verticalPadding

class BinDetailDialog(context: Context) {

    private val binDelayReason: TextView
    private val binDelayReasonChange: View
    private val binServiceOrder: TextView
    private val binOperationOrder: TextView
    private val binPlaceName: TextView
    private val binPlanTime: TextView
    private val binActualTime: TextView
    private val binWorkName: TextView
    private val binZipCode: TextView
    private val binPlaceAddress: TextView
    private val binPlaceTel1: TextView
    private val binPlaceMail1: TextView
    private val binPlaceTel2: TextView
    private val binPlaceMail2: TextView
    private val binPlaceNote1: TextView
    private val binPlaceNote2: TextView
    private val binPlaceNote3: TextView
    val binIncidental: TextView
    private val binSignature: TextView
    private val binIncidentalGroup: View
    private val binSignGroup: View
    val binTemperature: TextView
    val binMemo: TextView
    val binCollect: TextView
    private val binCollectGroup: View

    @JvmField
    val view = context.nestedScrollView {

        val dialOnClick = textViewMakeDialOnClick()
        val mapOnClick = textViewOpenMapsOnClick()
        val emailOnClick = textViewSendMailOnClick()
        val primaryColor = ContextCompat.getColor(context, R.color.colorPrimary)

        fun TableRow.addItemName(
            @StringRes name: Int,
        ) {
            add(textView {
                setText(name)
                verticalPadding = dip(8)
                horizontalPadding = dip(8)
                textSize = 12F
            }, defaultLParams(wrapContent, wrapContent))
        }

        fun TableRow.addItemValue(
            hasClick: Boolean = false
        ): TextView {
            return add(textView {
                verticalPadding = dip(16)
                horizontalPadding = dip(8)
                textSize = 14F
                if (hasClick) background = rippleDrawable(0)
                else setTextIsSelectable(true)
            }, defaultLParams(0, wrapContent, weight = 1F))
        }

        add(constraintLayout {
            maxWidth = dip(512)
            add(tableLayout {
                weightSum = 1F
                val textC = ContextCompat.getColor(context, R.color.gray_out)
                dividerDrawable = gradientDrawable(0, textC).apply { setSize(dip(1), dip(1)) }
                showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
                add(tableRow {
                    addItemName(R.string.bin_untimely_delivered_reason)
                    binDelayReason = addItemValue()
                    binDelayReasonChange = add(textView {
                        setText(R.string.bin_untimely_delivered_reason_change)
                        primaryButtonSmallStyle()
                        disable()
                    }, defaultLParams(wrapContent, wrapContent) {
                        marginEnd = dip(16)
                    })
                }, defaultLParams(matchParent, wrapContent))
                add(tableRow {
                    addItemName(R.string.bin_service_order)
                    binServiceOrder = addItemValue()
                }, defaultLParams(matchParent, wrapContent))
                add(tableRow {
                    addItemName(R.string.bin_operation_order)
                    binOperationOrder = addItemValue()
                }, defaultLParams(matchParent, wrapContent))
                add(tableRow {
                    addItemName(R.string.bin_place_name)
                    binPlaceName = addItemValue()
                }, defaultLParams(matchParent, wrapContent))
                add(tableRow {
                    addItemName(R.string.bin_plan_time)
                    binPlanTime = addItemValue()
                }, defaultLParams(matchParent, wrapContent))
                add(tableRow {
                    addItemName(R.string.bin_actual_time)
                    binActualTime = addItemValue()
                }, defaultLParams(matchParent, wrapContent))
                add(tableRow {
                    addItemName(R.string.bin_work_name)
                    binWorkName = addItemValue()
                }, defaultLParams(matchParent, wrapContent))
                add(tableRow {
                    addItemName(R.string.bin_zip_code)
                    binZipCode = addItemValue()
                }, defaultLParams(matchParent, wrapContent))
                add(tableRow {
                    addItemName(R.string.bin_place_address)
                    binPlaceAddress = addItemValue(true).apply {
                        setOnClickListener(mapOnClick)
                        textColor = primaryColor
                    }
                }, defaultLParams(matchParent, wrapContent))
                add(tableRow {
                    addItemName(R.string.bin_place_tel1)
                    binPlaceTel1 = addItemValue(true).apply {
                        setOnClickListener(dialOnClick)
                        textColor = primaryColor
                    }
                }, defaultLParams(matchParent, wrapContent))
                add(tableRow {
                    addItemName(R.string.bin_place_mail1)
                    binPlaceMail1 = addItemValue(true).apply {
                        setOnClickListener(emailOnClick)
                        textColor = primaryColor
                    }
                }, defaultLParams(matchParent, wrapContent))
                add(tableRow {
                    addItemName(R.string.bin_place_tel2)
                    binPlaceTel2 = addItemValue(true).apply {
                        setOnClickListener(dialOnClick)
                        textColor = primaryColor
                    }
                }, defaultLParams(matchParent, wrapContent))
                add(tableRow {
                    addItemName(R.string.bin_place_mail2)
                    binPlaceMail2 = addItemValue(true).apply {
                        setOnClickListener(emailOnClick)
                        textColor = primaryColor
                    }
                }, defaultLParams(matchParent, wrapContent))
                add(tableRow {
                    addItemName(R.string.bin_place_note1)
                    binPlaceNote1 = addItemValue()
                }, defaultLParams(matchParent, wrapContent))
                add(tableRow {
                    addItemName(R.string.bin_place_note2)
                    binPlaceNote2 = addItemValue()
                }, defaultLParams(matchParent, wrapContent))
                add(tableRow {
                    addItemName(R.string.bin_place_note3)
                    binPlaceNote3 = addItemValue()
                }, defaultLParams(matchParent, wrapContent))
                binIncidentalGroup = add(tableRow {
                    gone()
                    addItemName(R.string.bin_incidental)
                    binIncidental = addItemValue(true).apply {
                        setCompoundDrawables(
                            null, null,
                            context.compoundDrawable(
                                R.drawable.baseline_keyboard_arrow_right_24,
                                primaryColor,
                                context.dip(24)
                            ), null
                        )
                    }
                }, defaultLParams(matchParent, wrapContent))
                binSignGroup = add(tableRow {
                    gone()
                    addItemName(R.string.bin_signature)
                    binSignature = addItemValue()
                }, defaultLParams(matchParent, wrapContent))
                add(tableRow {
                    addItemName(R.string.bin_temperature)
                    binTemperature = addItemValue(true)
                }, defaultLParams(matchParent, wrapContent))
                add(tableRow {
                    addItemName(R.string.bin_memo)
                    binMemo = addItemValue(true)
                }, defaultLParams(matchParent, wrapContent))
                binCollectGroup = add(tableRow {
                    addItemName(R.string.collections_edit)
                    binCollect = addItemValue(true)
                }, defaultLParams(matchParent, wrapContent))
            }, defaultLParams(matchConstraint, wrapContent) {
                horizontalCenterInParent()
            })

        }, lParams(matchParent, wrapContent, gravity = gravityCenterHorizontal))
    }

    fun reasonChange(onClickListener: View.OnClickListener?) {
        binDelayReasonChange.apply {
            if (onClickListener != null) enable().setOnClickListener(onClickListener)
            else disable().setOnClickListener(null)
        }
    }

    fun displayCollectGroup(visible: Boolean) {
        binCollectGroup.showOrGone(visible)
    }

    fun setData(b: BinDetailData) {
        val karaPlaceholder = ""
        val binDetail = b.binDetail

        binDelayReason.text = b.delayReason?.reasonText ?: karaPlaceholder

        binServiceOrder.text = binDetail.serviceOrder
            ?.takeIf { it > 0 }?.toString() ?: karaPlaceholder
        binOperationOrder.text = binDetail.operationOrder
            ?.takeIf { it > 0 }?.toString() ?: karaPlaceholder

        val place = binDetail.place
        val placeExt = binDetail.placeExt

        binPlaceName.text = place.placeNameFull
        binPlanTime.text = binDetail.displayPlanTime ?: karaPlaceholder
        binActualTime.text = binDetail.displayWorkTime() ?: karaPlaceholder
        binWorkName.text = binDetail.work?.workNm

        binZipCode.text = placeExt?.zip ?: karaPlaceholder
        binPlaceAddress.text = place.addr ?: karaPlaceholder

        binPlaceTel1.text = placeExt?.tel1 ?: karaPlaceholder
        binPlaceMail1.text = placeExt?.mail1 ?: karaPlaceholder
        binPlaceTel2.text = placeExt?.tel2 ?: karaPlaceholder
        binPlaceMail2.text = placeExt?.mail2 ?: karaPlaceholder

        binPlaceNote1.text = placeExt?.note1 ?: karaPlaceholder
        binPlaceNote2.text = placeExt?.note2 ?: karaPlaceholder
        binPlaceNote3.text = placeExt?.note3 ?: karaPlaceholder

        binIncidentalGroup.showOrGone(b.incidentalEnabled)
        binSignGroup.showOrGone(b.incidentalEnabled)

        if (b.incidentalHeaderCount > 0) {
            binIncidental.textResource = R.string.bin_registered
        } else {
            binIncidental.text = karaPlaceholder
        }
        if (b.signedIncidentalHeaderCount > 0) {
            binSignature.textResource = R.string.bin_registered
        } else {
            binSignature.text = karaPlaceholder
        }
        binTemperature.text = binDetail.temperature?.let { "$it ℃" }
        binMemo.text = binDetail.experiencePlaceNote1 ?: karaPlaceholder
    }
}
