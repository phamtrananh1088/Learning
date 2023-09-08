package jp.co.toukei.log.trustar.feature.home.fragment

import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import jp.co.toukei.log.lib.asEvent
import jp.co.toukei.log.lib.common.ArgBottomDialogFragment
import jp.co.toukei.log.lib.common.PassDataVM
import jp.co.toukei.log.lib.common.enableIf
import jp.co.toukei.log.lib.observeNullable
import jp.co.toukei.log.lib.setOnLostFocusHideIME
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.feature.home.ui.BinMeterInputUI
import splitties.views.textResource
import java.util.*

class BinStartOutgoingDialog : ArgBottomDialogFragment<PassDataVM<Int, Optional<Int>>>() {

    private val currentValue = MutableLiveData<Int?>()

    override fun createView(owner: FragmentActivity): View {
        val ui = BinMeterInputUI(owner).apply {
            title1.textResource = R.string.outgoing_meter_input_title
            title2.textResource = R.string.outgoing_meter_in_km
            cancel.textResource = R.string.input_later

            currentValue.observeNullable(viewLifecycleOwner) {
                confirm.enableIf(it != null)
            }
            input.doAfterTextChanged {
                currentValue.value = it?.toString()?.toIntOrNull()
            }
            cancel.setOnClickListener {
                arg?.result?.value = Optional.ofNullable<Int>(null).asEvent()
                dismiss()
            }
            confirm.setOnClickListener {
                arg?.result?.value = Optional.ofNullable(currentValue.value).asEvent()
                dismiss()
            }
            input.setText(arg?.source?.value?.toString())
            input.setOnLostFocusHideIME()
        }
        return ui.view
    }
}
