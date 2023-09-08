package jp.co.toukei.log.trustar.feature.home.fragment

import android.graphics.Color
import android.view.View
import androidx.fragment.app.FragmentActivity
import jp.co.toukei.log.lib.*
import jp.co.toukei.log.lib.common.*
import jp.co.toukei.log.trustar.*
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.db.user.entity.Truck
import jp.co.toukei.log.trustar.feature.home.model.HomeModel
import jp.co.toukei.log.trustar.feature.home.model.UnscheduledBinStartVM
import jp.co.toukei.log.trustar.feature.home.ui.BinStartDialogUI
import jp.co.toukei.log.trustar.rest.response.ResponseException
import splitties.dimensions.dip
import splitties.views.textResource
import third.Result
import java.util.*

/**
 * 計画なし運行追加
 *
 * 開始成功：配送計画 [DetailFragment]
 * @see [HomeModel.lazyNavigationByAllocationNo]
 */
class UnscheduledBinStart : CommonBottomSheetDialogFragment() {

    override fun createView(owner: FragmentActivity): View {
        val passDataTruckVM = getViewModel<PassDataVM<Truck, Truck>>("1")
        val passDataInMeterVM = getViewModel<PassDataVM<Int, Optional<Int>>>("2")
        val vm = getViewModel<UnscheduledBinStartVM>()
        val selected = passDataTruckVM.source

        val d = BinStartDialogUI(owner)
        d.change.apply {
            textResource = R.string.choose_vehicle
            setOnClickListener {
                val f = VehicleChooseDialogFragment()
                f.arg = passDataTruckVM
                f.showAt(owner)
            }
        }
        d.done.apply {
            textResource = R.string.operation_start
            setCompoundDrawables(null, null, context.run {
                compoundDrawable(
                    R.drawable.baseline_arrow_downward_24,
                    Color.WHITE,
                    dip(20)
                )
            }, null)
            setOnClickListener {
                selected.value?.let { t ->
                    if (Current.userInfo.meterInputEnabled) {
                        BinStartOutgoingDialog()
                            .apply { arg = passDataInMeterVM }
                            .showAt(owner)
                    } else {
                        vm.startUnscheduledBin(t)
                    }
                }
            }
            disable()
        }

        passDataInMeterVM.result.observeNonNull(viewLifecycleOwner) {
            selected.value?.let { t ->
                it.getContentIfNotHandled()?.let { km ->
                    val n = km.orElseNull()
                    vm.startUnscheduledBin(t, n)
                    passDataInMeterVM.source.value = n
                }
            }
        }

        selected.observeNullable(viewLifecycleOwner) {
            d.currentVehicle.text = it?.truckNm
            d.done.enableIf(it != null)
        }

        passDataTruckVM.result.observeNonNull(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { t ->
                selected.value = t
            }
        }

        //todo observe all binHeader which status != 1.
        val hm = owner.getViewModel<HomeModel>()

        vm.startState.observeNullable(viewLifecycleOwner) {
            val isLoading = it is Result.Loading
            d.switchLoading(isLoading)
            isCancelable = it == null || !isLoading
            it?.runOnError { err ->
                if (err is ResponseException) {
                    dismiss()
                    d.view.context.longToast(err.errMessage())
                } else {
                    d.view.context.materialAlertDialogBuilder {
                        setTitle(R.string.server_connection_err)
                        setMessage(R.string.start_bin_connection_err_alert_msg)
                        setPositiveButton(android.R.string.ok, null)
                        setOnDismissListener { vm.clearError() }
                    }.show()
                }
            }
            it?.runOnValue { value ->
                dismiss()
                hm.lazyNavigationByAllocationNo(value)
            }
        }
        return d.view
    }
}
