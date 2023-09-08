package jp.co.toukei.log.trustar.feature.home.fragment

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import jp.co.toukei.log.lib.*
import jp.co.toukei.log.lib.common.PassDataVM
import jp.co.toukei.log.lib.common.hintResource
import jp.co.toukei.log.lib.common.setLayoutParams
import jp.co.toukei.log.lib.util.BindHolder
import jp.co.toukei.log.lib.util.BindHolderListAdapter
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.common.FullScreenDialogFragment
import jp.co.toukei.log.trustar.db.user.entity.Truck
import jp.co.toukei.log.trustar.feature.dialog.SelectFromList
import jp.co.toukei.log.trustar.feature.dialog.TruckItem
import splitties.views.textResource
import third.Event

/**
 * 車両選択
 *
 * - 運行開始 [BinStartDialogFragment]
 * - 計画なし運行追加 [UnscheduledBinStart]
 */
class VehicleChooseDialogFragment : FullScreenDialogFragment<PassDataVM<Truck, Truck>>() {

    private class TruckChooseListAdapter(
        private val liveData: MutableLiveData<Event<Truck>>
    ) : BindHolderListAdapter<TruckItem.Item>(computationDifferConfig(TruckItem.Diff())) {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): BindHolder<TruckItem.Item> {
            return TruckItem(parent.context)
                .applyView { h ->
                    setLayoutParams()
                    setOnClickListener {
                        h.withBoundValue {
                            liveData.value = truck.asEvent()
                        }
                    }
                }
                .toBindHolder()
        }
    }

    private val selected = MutableLiveData<Event<Truck>>()

    override fun createView(owner: FragmentActivity): View {
        val d = SelectFromList(owner).apply {
            currentLabel.textResource = R.string.current_vehicle
            search.hintResource = R.string.vehicle_search_hint
            listTitle.textResource = R.string.choose_target_vehicle
        }
        argLiveData.switchMapNullable { it?.source }.observeNullable(viewLifecycleOwner) {
            d.currentSelected.text = it?.truckNm
        }
        selected.observeNonNull(viewLifecycleOwner) {
            arg?.result?.value = it
            dismiss()
        }

        val a = TruckChooseListAdapter(selected)
        runOnIo {
            /*抽出条件値を入力した後に起動されるイベント*/
            d.search.multipleQueryByTextChanged(
                100,
                Current.userDatabase.truckDao().selectAll()
            ) { t, w -> t.truckCd.contains(w, true) || t.truckNm.contains(w, true) }
                .map { a -> a.map { TruckItem.Item(it) } }
                .observeOnUI()
                .subscribeOrIgnore { a.submitList(it) }
                .addTo(viewDisposableContainer)
        }
        d.recyclerView.adapter = a
        return d.view
    }
}
