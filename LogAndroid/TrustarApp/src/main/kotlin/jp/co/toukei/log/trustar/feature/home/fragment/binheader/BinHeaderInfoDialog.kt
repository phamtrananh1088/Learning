package jp.co.toukei.log.trustar.feature.home.fragment.binheader

import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import jp.co.toukei.log.lib.hideIME
import jp.co.toukei.log.lib.observeNonNull
import jp.co.toukei.log.lib.orElseNull
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.feature.home.ui.BinHeaderInfoUI
import java.text.DecimalFormat

class BinHeaderInfoDialog : BinHeaderDialogFragment() {

    private abstract class Listener : View.OnFocusChangeListener, TextView.OnEditorActionListener {

        override fun onFocusChange(v: View?, hasFocus: Boolean) {
            val tv: TextView = v as? TextView ?: return
            val number = tv.text.toString().findDecInt()
            if (hasFocus) {
                tv.text = number?.toString()
            } else {
                tv.hideIME()
                valueChanged(number)
            }
        }

        abstract fun valueChanged(value: Int?)

        override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                v.clearFocus()
            }
            return false
        }

        private fun String.findDecInt(): Int? {
            var f = -1
            var l = -1
            for (index in indices) {
                val c = this[index]
                if (c == ',') continue
                if (c in '0'..'9') {
                    if (f < 0) f = index
                    l = index
                } else if (l >= 0) break
            }
            val e = l + 1
            return if (f in 0 until e) {
                subSequence(f, e)
                    .filterNot { it == ',' }
                    .toString()
                    .toIntOrNull()
            } else null
        }
    }

    private val formatter = DecimalFormat("#,###,###")

    private fun TextView.setFormattedValue(value: Int?) {
        text = value?.let { "${formatter.format(it)} km" }
    }

    override fun createView(owner: FragmentActivity): View {
        val ui = BinHeaderInfoUI(owner)

        binHeaderModel.binHeaderLiveData.observeNonNull(viewLifecycleOwner) {
            val v = it.orElseNull()
            ui.applyBinHeaderData(v)
            ui.outgoing.setFormattedValue(v?.header?.outgoingMeter)
            ui.incoming.setFormattedValue(v?.header?.incomingMeter)
        }

        val inputEnable = Current.userInfo.meterInputEnabled
        ui.outgoing.apply {
            if (inputEnable) {
                val l = object : Listener() {
                    override fun valueChanged(value: Int?) {
                        binHeaderModel.setOutgoing(value)
                    }
                }
                onFocusChangeListener = l
                setOnEditorActionListener(l)
            } else {
                isEnabled = false
            }
        }
        ui.incoming.apply {
            if (inputEnable) {
                val l = object : Listener() {
                    override fun valueChanged(value: Int?) {
                        binHeaderModel.setIncoming(value)
                    }
                }
                onFocusChangeListener = l
                setOnEditorActionListener(l)
            } else {
                isEnabled = false
            }
        }
        return ui.view
    }
}
