package jp.co.toukei.log.trustar.feature.home.ui

import android.content.Context
import android.widget.CheckBox
import android.widget.TextView
import androidx.annotation.StringRes
import jp.co.toukei.log.trustar.R
import splitties.dimensions.dip
import splitties.views.dsl.core.*
import splitties.views.padding

class AutoModeDialogUI(context: Context) {

    private val check: CheckBox
    private val msg: TextView

    @JvmField
    val view = context.verticalLayout {
        padding = dip(16)
        msg = add(textView {
            padding = dip(8)
            textSize = 18F
        }, lParams(wrapContent, wrapContent))
        check = add(checkBox {
            setText(R.string.auto_mode_do_not_show)
            padding = dip(8)
        }, lParams(matchParent, wrapContent))
    }

    fun setMsg(@StringRes id: Int) {
        msg.setText(id)
    }

    val checked: Boolean
        get() {
            return check.isChecked
        }
}
