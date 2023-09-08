package jp.co.toukei.log.trustar.feature.home.ui

import android.content.Context
import android.text.InputType
import android.text.method.DigitsKeyListener
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import jp.co.toukei.log.lib.common.*
import jp.co.toukei.log.lib.gradientDrawable
import jp.co.toukei.log.trustar.Config
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.db.user.embedded.BinHeaderAndStatus
import jp.co.toukei.log.trustar.hasFinished
import jp.co.toukei.log.trustar.started
import splitties.dimensions.dip
import splitties.views.dsl.core.*
import splitties.views.gravityCenterHorizontal
import splitties.views.horizontalPadding
import splitties.views.verticalPadding

class BinHeaderInfoUI(context: Context) {

    private val vehicle: TextView
    private val startScheduled: TextView
    private val endScheduled: TextView
    private val start: TextView
    private val end: TextView
    private val crew: TextView
    private val crew2: TextView
    private val note: TextView

    val outgoing: EditText
    val incoming: EditText

    private val outgoingMeterGroup: View
    private val incomingMeterGroup: View

    @JvmField
    val view = context.nestedScrollView {
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

        fun TableRow.addItemValue(): TextView {
            return add(textView {
                verticalPadding = dip(16)
                horizontalPadding = dip(8)
                textSize = 14F
                setTextIsSelectable(true)
            }, defaultLParams(0, wrapContent, weight = 1F))
        }

        fun TableRow.addItemValueEdit(): EditText {
            return add(editText {
                verticalPadding = dip(16)
                horizontalPadding = dip(8)
                textSize = 14F
                background = null
                singleLine()
                imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI or EditorInfo.IME_ACTION_DONE
                inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_CLASS_NUMBER
                keyListener = DigitsKeyListener.getInstance("0123456789")
                setSelectAllOnFocus(true)
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
                    addItemName(R.string.bin_vehicle)
                    vehicle = addItemValue()
                }, defaultLParams(matchParent, wrapContent))
                add(tableRow {
                    addItemName(R.string.bin_start_scheduled_time)
                    startScheduled = addItemValue()
                }, defaultLParams(matchParent, wrapContent))
                add(tableRow {
                    addItemName(R.string.bin_end_scheduled_time)
                    endScheduled = addItemValue()
                }, defaultLParams(matchParent, wrapContent))
                add(tableRow {
                    addItemName(R.string.bin_start_time)
                    start = addItemValue()
                }, defaultLParams(matchParent, wrapContent))
                add(tableRow {
                    addItemName(R.string.bin_end_time)
                    end = addItemValue()
                }, defaultLParams(matchParent, wrapContent))
                add(tableRow {
                    addItemName(R.string.bin_crew)
                    crew = addItemValue()
                }, defaultLParams(matchParent, wrapContent))
                add(tableRow {
                    addItemName(R.string.bin_crew2)
                    crew2 = addItemValue()
                }, defaultLParams(matchParent, wrapContent))
                add(tableRow {
                    addItemName(R.string.bin_note_1)
                    note = addItemValue()
                }, defaultLParams(matchParent, wrapContent))
                outgoingMeterGroup = add(tableRow {
                    gone()
                    addItemName(R.string.bin_outgoing_meter)
                    outgoing = addItemValueEdit()
                }, defaultLParams(matchParent, wrapContent))
                incomingMeterGroup = add(tableRow {
                    gone()
                    addItemName(R.string.bin_incoming_meter)
                    incoming = addItemValueEdit()
                }, defaultLParams(matchParent, wrapContent))
            }, defaultLParams(matchConstraint, wrapContent) {
                horizontalCenterInParent()
            })

        }, lParams(matchParent, wrapContent, gravity = gravityCenterHorizontal))
    }

    fun applyBinHeaderData(binHeaderAndStatus: BinHeaderAndStatus?) {
        val header = binHeaderAndStatus?.header
        val status = binHeaderAndStatus?.status
        val started = status.started()
        val finished = status.hasFinished()

        vehicle.text = binHeaderAndStatus?.truck?.truckNm

        startScheduled.text = header?.planStartDatetime?.let(Config.dateFormatter3::format)
        endScheduled.text = header?.planEndDatetime?.let(Config.dateFormatter3::format)
        start.text = header?.drvStartDatetime
            ?.takeIf { status.started() }
            ?.let(Config.dateFormatter3::format)
        end.text = header?.drvEndDatetime?.let(Config.dateFormatter3::format)

        crew.text = header?.driverNm
        crew2.text = header?.subDriverNm
        note.text = header?.allocationNote1

        outgoingMeterGroup.showOrGone(started)
        incomingMeterGroup.showOrGone(finished)
    }
}
