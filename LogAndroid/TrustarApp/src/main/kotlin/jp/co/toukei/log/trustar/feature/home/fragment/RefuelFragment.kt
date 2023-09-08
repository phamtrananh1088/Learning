package jp.co.toukei.log.trustar.feature.home.fragment

import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import jp.co.toukei.log.lib.*
import jp.co.toukei.log.lib.common.*
import jp.co.toukei.log.lib.util.BindHolder
import jp.co.toukei.log.lib.util.Combine4LiveData
import jp.co.toukei.log.lib.util.FixedAdapter
import jp.co.toukei.log.lib.util.RecyclerViewForStringList
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.bottomSheetListenerForExpand
import jp.co.toukei.log.trustar.db.user.embedded.BinHeaderAndStatus
import jp.co.toukei.log.trustar.db.user.entity.Fuel
import jp.co.toukei.log.trustar.feature.home.activity.HomeActivity
import jp.co.toukei.log.trustar.feature.home.model.HomeModel
import jp.co.toukei.log.trustar.feature.home.model.RefuelFragmentModel
import jp.co.toukei.log.trustar.feature.home.ui.BinHeadUI
import jp.co.toukei.log.trustar.feature.home.ui.RefuelFragmentUI
import jp.co.toukei.log.trustar.other.Refueled
import jp.co.toukei.log.trustar.setTextObserver
import splitties.views.dsl.recyclerview.recyclerView

/**
 * 給油
 */
class RefuelFragment : CommonFragment<HomeActivity>() {
    override fun createView(owner: HomeActivity): View {
        val u = RefuelFragmentUI(owner)

        getActivityModel<HomeModel>().lastFormattedDataLiveData.observe(u.dateTextView.setTextObserver())

        val model = getViewModel<RefuelFragmentModel>()

        model.selectedBinHeaderLiveData.observeNullable(viewLifecycleOwner) {
            val bin = it?.orElseNull()
            u.vehicle.text = bin?.truck?.truckNm
            if (bin == null) {
                u.allocationItem.setText(R.string.unselected)
            } else {
                u.allocationItem.text = bin.header.allocationNm
            }
        }
        model.selectedFuelCdLiveData.observeNullable {
            val f = it?.orElseNull()
            if (f == null) {
                u.fuel.setText(R.string.unselected)
            } else {
                u.fuel.text = f.fuelNm
            }
        }
        model.totalAmountOfSelected.observeNullable {
            val t = it?.orElseNull()
            u.currentAmount.apply {
                text = context.getString(R.string.f_liter, t ?: 0F)
            }
        }
        model.binHeaderListLiveData.observeNullable {
            if (it.isNullOrEmpty()) {
                u.allocationItem.setOnClickListener(null)
            } else {
                /*給油入力・便選択の押下時に起動されるイベント*/
                u.allocationItem.setOnClickListener { v ->
                    BottomSheetDialog(v.context).apply {
                        setContentView(context.recyclerView {
                            layoutManager = LinearLayoutManager(context)
                            adapter = object : FixedAdapter<BinHeaderAndStatus>(it) {
                                override fun onCreateViewHolder(
                                    parent: ViewGroup,
                                    viewType: Int
                                ): BindHolder<BinHeaderAndStatus> {
                                    return BinHeadUI(parent.context).applyView { h ->
                                        setLayoutParams()
                                        h.iconVisible(false)
                                        setOnClickListener {
                                            h.withBoundValue {
                                                model.selectBinHeader(header)
                                            }
                                            dismiss()
                                        }
                                    }.toBindHolder()
                                }
                            }
                        })
                        setOnShowListener(bottomSheetListenerForExpand())
                    }.show()
                }
            }
        }

        model.fuelListLiveData.observeNonNull {
            if (it.isNullOrEmpty()) {
                u.fuel.setOnClickListener(null)
            } else {
                /*給油入力・燃料種別選択の押下時に起動されるイベント*/
                u.fuel.setOnClickListener { v ->
                    BottomSheetDialog(v.context).apply {
                        val r = object : RecyclerViewForStringList<Fuel>(context, it) {
                            override fun applyView(item: Item) {
                                super.applyView(item)
                                item.view.setOnClickListener {
                                    item.withBoundValue {
                                        model.selectFuel(this)
                                    }
                                    dismiss()
                                }
                            }

                            override fun getItemText(item: Fuel): CharSequence {
                                return item.fuelNm
                            }
                        }
                        setContentView(r.view)
                        setOnShowListener(bottomSheetListenerForExpand())
                    }.show()
                }
            }
        }

        u.refuelAmount.setText(model.currentQuantityMutableLivaData.value?.toString())
        u.paymentAmount.setText(model.currentPaidMutableLivaData.value?.toString())

        u.refuelAmount.doAfterTextChanged {
            model.currentQuantityMutableLivaData.value = it.toString().toFloatOrNull()
        }
        u.paymentAmount.doAfterTextChanged {
            model.currentPaidMutableLivaData.value = it.toString().toFloatOrNull()
        }

        val combine = Combine4LiveData(
            model.selectedBinHeaderLiveData,
            model.selectedFuelCdLiveData,
            model.currentQuantityMutableLivaData,
            model.currentPaidMutableLivaData,
        ) { ob, of, q, p ->
            val b = ob?.orElseNull()
            val f = of?.orElseNull()
            if (b == null || f == null || q == null || p == null) null
            else Refueled(b.header, f, q, p)
        }
        combine.observeNullable { r ->
            if (r == null) {
                u.button.disable().setOnClickListener(null)
            } else {
                u.button.enable().setOnClickListener {
                    it.hideIME()
                    /*給油入力・入力を確定する押下時に起動されるイベント*/
                    model.refuel(r)
                    u.paymentAmount.empty()
                    u.refuelAmount.empty()
                    u.coordinatorLayout.snackbar(R.string.refuel_success_msg)
                }
            }
        }
        return u.coordinatorLayout
    }

    override fun onHiddenChanged(hidden: Boolean) {
        if (hidden) {
            hideCurrentIME()
        }
    }
}
