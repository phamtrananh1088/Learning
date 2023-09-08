package jp.co.toukei.log.trustar.feature.home.fragment.binheader

import android.view.View
import androidx.fragment.app.FragmentActivity
import jp.co.toukei.log.lib.*
import jp.co.toukei.log.lib.common.PassDataVM
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.db.user.entity.BinStatus
import jp.co.toukei.log.trustar.db.user.entity.Truck
import jp.co.toukei.log.trustar.feature.home.fragment.BinStartOutgoingDialog
import jp.co.toukei.log.trustar.feature.home.fragment.DetailFragment
import jp.co.toukei.log.trustar.feature.home.fragment.VehicleChooseDialogFragment
import jp.co.toukei.log.trustar.feature.home.ui.BinStartDialogUI
import jp.co.toukei.log.trustar.other.WorkMode
import jp.co.toukei.log.trustar.showAt
import java.util.*

/**
 * 運行開始
 *
 * - 配送計画 [DetailFragment]
 * - 車両変更 [VehicleChooseDialogFragment]
 * - 開始 [WorkModeChooseDialog]
 */
class BinStartDialog : BinHeaderDialogFragment() {

    override fun createView(owner: FragmentActivity): View {
        val passDataVM = getViewModel<PassDataVM<Truck, Truck>>("1")
        val passDataInMeterVM = getViewModel<PassDataVM<Int, Optional<Int>>>("2")
        val selected = passDataVM.source

        val d = BinStartDialogUI(owner)
        d.change.setOnClickListener {

            /*運行開始・車両選択押下時のイベント*/
            /*車両変更*/
            val f = VehicleChooseDialogFragment()
            f.arg = passDataVM
            f.showAt(owner)
        }

        passDataInMeterVM.result.observeNonNull(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { o ->
                o.withValue { km -> binHeaderModel.setOutgoing(km) }
                startOrSelectWorkMode(owner)
                dismiss()
            }
        }

        d.done.setOnClickListener {
            if (Current.userInfo.meterInputEnabled) {
                BinStartOutgoingDialog()
                    .apply { arg = passDataInMeterVM }
                    .showAt(owner)
            } else {
                startOrSelectWorkMode(owner)
                dismiss()
            }
        }

        binHeaderModel.binHeaderLiveData.observeNullable(viewLifecycleOwner) {
            val bin = it?.orElseNull()
            passDataInMeterVM.source.value = bin?.header?.outgoingMeter
            if (bin == null || bin.status.type != BinStatus.Type.Ready)
                dismiss()
            else {
                val t = bin.truck
                d.currentVehicle.text = t.truckNm
                selected.value = t
            }
        }

        passDataVM.result.observeNonNull(viewLifecycleOwner) {
            /*車両変更・データの更新処理*/
            it.getContentIfNotHandled()?.let { t ->
                binHeaderModel.setTruck(t)
            }
        }
        return d.view
    }

    private fun startOrSelectWorkMode(owner: FragmentActivity) {
        if (Current.userInfo.geofenceUseFlag) {
            binHeaderModel.binHeaderLiveData.value?.orElseNull()?.header?.allocationNo?.let {
                WorkModeChooseDialog().apply {
                    arg = it
                }.showAt(owner)
            }
        } else {
            binHeaderModel.start(WorkMode.Normal, Current.lastLocation)
        }
    }
}
