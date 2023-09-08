package jp.co.toukei.log.trustar.other

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.StateListDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewTreeObserver
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker
import jp.co.toukei.log.lib.common.*
import jp.co.toukei.log.lib.rippleDrawable
import splitties.dimensions.dip
import splitties.views.backgroundColor
import splitties.views.centerText
import splitties.views.dsl.core.*
import splitties.views.padding
import java.text.SimpleDateFormat
import java.util.*

/**
 * well, it works.
 * @see[com.github.florent37.singledateandtimepicker.dialog.DoubleDateAndTimePickerDialog]
 */
class VeryBadDateTimePicker(
    context: Context,
    @ColorRes mainColorResource: Int,
    contentSize: Float,

    @StringRes tab0Text: Int,
    @StringRes tab1Text: Int,
    @StringRes buttonOkText: Int,

    defaultDate0: Date,
    defaultDate1: Date,
    dayFormatter: SimpleDateFormat?,
    customLocale: Locale,
    pick: Context.(Date, Date) -> Unit
) {

    val view = context.verticalLayout {
        lateinit var buttonTab0: TextView
        lateinit var buttonTab1: TextView
        lateinit var tab0: SingleDateAndTimePicker
        lateinit var tab1: SingleDateAndTimePicker

        val mainColor = ContextCompat.getColor(context, mainColorResource)

        backgroundColor = Color.WHITE
        gravityCenterHorizontal()

        add(horizontalLayout {
            weightSum = 2F
            buttonTab0 = add(button {
                setText(tab0Text)
                isSelected = true
            }, lParams(0, matchParent, weight = 1F))
            buttonTab1 = add(button {
                setText(tab1Text)
            }, lParams(0, matchParent, weight = 1F))
            buttonTab0.setOnClickListener {
                if (tab0.translationX != 0f) {
                    buttonTab0.isSelected = true
                    buttonTab1.isSelected = false
                    tab0.animate().translationX(0f)
                    tab1.animate().translationX(tab1.width.toFloat())
                }
            }
            val selected = intArrayOf(android.R.attr.state_selected)
            val unselected = intArrayOf(-android.R.attr.state_selected)
            arrayOf(buttonTab0, buttonTab1).forEach {
                it.apply {
                    textSize = contentSize
                    background = StateListDrawable().apply {
                        addState(unselected, rippleDrawable(0, mainColor))
                        addState(selected, rippleDrawable(0, Color.WHITE))
                    }
                    setTextColor(
                        ColorStateList(
                            arrayOf(unselected, selected),
                            intArrayOf(Color.WHITE, mainColor)
                        )
                    )
                }
            }
        }, lParams(matchParent, dip(50)))
        add(frameLayout {
            tab0 = view(::SingleDateAndTimePicker) {
                setDefaultDate(defaultDate0)
            }
            tab1 = view(::SingleDateAndTimePicker) {
                setDefaultDate(defaultDate1)
                viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                    override fun onPreDraw(): Boolean {
                        viewTreeObserver.removeOnPreDrawListener(this)
                        translationX = width.toFloat()
                        return false
                    }
                })
            }
            arrayOf(tab0, tab1).forEach {
                add(it.apply {
                    setCurved(false)
                    setCyclic(true)
                    setVisibleItemCount(5)
                    setDisplayDays(true)
                    setDisplayHours(true)
                    setDisplayMinutes(true)
                    setStepSizeMinutes(1)
                    setSelectedTextColor(mainColor)
                    setDayFormatter(dayFormatter)
                    setCustomLocale(customLocale)
                    setIsAmPm(false)
                    setTextSize(sp(contentSize.toInt()))
                }, lParams(matchParent, dip(200)))
            }
        }, lParams(matchParent, wrapContent) {
            topMargin = dip(20)
        })
        add(textView {
            setText(buttonOkText)
            centerText()
            setTextColor(mainColor)
            textSize = contentSize
            isAllCaps = true
            boldTypeface()
            padding = dip(20)
            background = rippleDrawable(0, Color.TRANSPARENT)
            setOnClickListener(Click(buttonTab0, buttonTab1, tab0, tab1, pick))
        }, lParams(wrapContent, wrapContent) {
            gravity = Gravity.END
        })
        buttonTab1.setOnClickListener(Click(buttonTab0, buttonTab1, tab0, tab1, null))
    }

    private class Click(
        private val buttonTab0: TextView,
        private val buttonTab1: TextView,
        private val tab0: SingleDateAndTimePicker,
        private val tab1: SingleDateAndTimePicker,
        private val callback: (Context.(Date, Date) -> Unit)?
    ) : View.OnClickListener {
        override fun onClick(view: View) {
            if (!displayTab1()) callback?.invoke(view.context, tab0.date, tab1.date)
        }

        private fun displayTab1(): Boolean {
            val v = tab0.translationX == 0f
            if (v) {
                buttonTab0.isSelected = false
                buttonTab1.isSelected = true
                tab0.animate().translationX(-tab0.width.toFloat())
                tab1.animate().translationX(0f)
            }
            return v
        }
    }
}
