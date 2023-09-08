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
import jp.co.toukei.log.trustar.db.user.entity.Shipper
import jp.co.toukei.log.trustar.feature.dialog.SelectFromList
import jp.co.toukei.log.trustar.feature.dialog.ShipperItem
import splitties.views.textResource
import third.Event

class ShipperChooseDialogFragment : FullScreenDialogFragment<PassDataVM<Shipper, Shipper>>() {

    private class ChooseListAdapter(
        private val liveData: MutableLiveData<Event<Shipper>>
    ) : BindHolderListAdapter<ShipperItem.Item>(computationDifferConfig(ShipperItem.Diff())) {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): BindHolder<ShipperItem.Item> {
            return ShipperItem(parent.context)
                .applyView { h ->
                    setLayoutParams()
                    setOnClickListener {
                        h.withBoundValue {
                            liveData.value = shipper.asEvent()
                        }
                    }
                }
                .toBindHolder()
        }
    }

    private val selected = MutableLiveData<Event<Shipper>>()

    override fun createView(owner: FragmentActivity): View {
        val d = SelectFromList(owner).apply {
            currentLabel.textResource = R.string.current_shipper
            search.hintResource = R.string.shipper_search_hint
            listTitle.textResource = R.string.choose_target_shipper
        }

        argLiveData.switchMapNullable { it?.source }.observeNullable(viewLifecycleOwner) {
            d.currentSelected.text = it?.shipperNm
        }

        selected.observeNonNull(viewLifecycleOwner) {
            /*行を押下時に起動されるイベント*/
            arg?.result?.value = it
            dismiss()
        }

        val a = ChooseListAdapter(selected)
        runOnComputation {
            /*抽出条件値を入力した後に起動されるイベント*/
            d.search.multipleQueryByTextChanged(
                100,
                Current.userDatabase.shipperDao().selectAll()
            ) { t, w -> t.shipperCd.contains(w, true) || t.shipperNm.contains(w, true) }
                .map { a -> a.map { ShipperItem.Item(it) } }
                .observeOnUI()
                .subscribeOrIgnore { a.submitList(it) }
                .addTo(viewDisposableContainer)
        }
        d.recyclerView.adapter = a
        return d.view
    }
}
