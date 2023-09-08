package jp.co.toukei.log.trustar.feature.home.fragment

import android.content.Context
import android.text.InputType
import android.text.method.DigitsKeyListener
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import jp.co.toukei.log.lib.common.ArgBottomDialogFragment
import jp.co.toukei.log.lib.common.singleLine
import jp.co.toukei.log.lib.lazyViewModel
import jp.co.toukei.log.lib.lengthFilter
import jp.co.toukei.log.lib.observeNonNull
import jp.co.toukei.log.lib.util.DecimalInputFilter
import jp.co.toukei.log.lib.util.RecyclerViewForStringList
import jp.co.toukei.log.lib.withBoundValue
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.bottomSheetListenerForExpand
import jp.co.toukei.log.trustar.db.user.entity.BinDetail
import jp.co.toukei.log.trustar.db.user.entity.DelayReason
import jp.co.toukei.log.trustar.dialogForEdit
import jp.co.toukei.log.trustar.feature.collect.fragment.CollectFragment
import jp.co.toukei.log.trustar.feature.dialog.BinDetailDialog
import jp.co.toukei.log.trustar.feature.home.model.BinDetailModel
import jp.co.toukei.log.trustar.feature.nimachi.fragment.SheetList
import jp.co.toukei.log.trustar.showAt

/**
 * 配送先詳細
 *
 * -待機・附帯 [SheetList]
 */
class BinDetailsDialogFragment : ArgBottomDialogFragment<BinDetail>() {

    private val binDetailModel by lazyViewModel<BinDetailModel>()

    override fun createView(owner: FragmentActivity): View {
        val d = BinDetailDialog(owner)

        argLiveData.observeNonNull(viewLifecycleOwner) {
            binDetailModel.binData(it)
            d.binIncidental.setOnClickListener { _ ->
                /*配送先詳細・待機附帯押下時に起動されるイベント*/
                SheetList().apply { arg = it }.showAt(owner)
            }
        }

        binDetailModel.binLiveData.observeNonNull(viewLifecycleOwner) {
            d.setData(it)
            d.displayCollectGroup(true)
            val delayReason = it.delayReasons
            val binDetail = it.binDetail
            val click = if (binDetail.isDelayed()) View.OnClickListener { v ->
                val sheet = BottomSheetDialog(v.context)
                val u =
                    object : RecyclerViewForStringList<DelayReason>(sheet.context, delayReason) {
                        override fun getItemText(item: DelayReason) = item.reasonText.orEmpty()
                        override fun applyView(item: Item) {
                            super.applyView(item).apply {
                                item.view.setOnClickListener {
                                    item.withBoundValue {
                                        binDetailModel.setDelayReason(this)
                                    }
                                    sheet.dismiss()
                                }
                            }
                        }
                    }
                sheet.setContentView(u.view)
                sheet.setOnShowListener(bottomSheetListenerForExpand())
                sheet.show()
            } else null
            d.reasonChange(click)

            d.binTemperature.apply {
                setOnClickListener { it.context.editTemperature(binDetail.temperature) }
            }
            d.binMemo.apply {
                setOnClickListener { it.context.editExperiencePlaceNote1(binDetail.experiencePlaceNote1) }
            }
            d.binCollect.setOnClickListener {
                CollectFragment()
                    .apply { arg = binDetail }
                    .showAt(owner)
            }
        }

        return d.view
    }

    private fun Context.editExperiencePlaceNote1(value: String?) {
        dialogForEdit(R.string.bin_memo, {
            setText(value)
            lengthFilter(100)
        }) {
            binDetailModel.setMemo(it)
        }
    }

    private fun Context.editTemperature(value: Double?) {
        dialogForEdit(R.string.bin_temperature, {
            setText(value?.toString())
            singleLine()
            inputType =
                InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
            keyListener = DigitsKeyListener.getInstance("-0123456789.")
            filters = arrayOf(DecimalInputFilter(3, 1, true))
        }) {
            val regex = Regex("^(-?\\d{0,3}(\\.\\d*)?)(?=$|\\D)")
            val t = it?.let(regex::find)?.groups?.get(1)?.value?.toDoubleOrNull()
            // start with -999.9 ~ 999.9
            binDetailModel.setTemperature(t)
        }
    }
}
