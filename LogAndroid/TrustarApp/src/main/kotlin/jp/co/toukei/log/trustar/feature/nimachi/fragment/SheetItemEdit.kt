package jp.co.toukei.log.trustar.feature.nimachi.fragment

import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.ItemTouchHelper
import jp.co.toukei.log.lib.*
import jp.co.toukei.log.lib.common.PassDataVM
import jp.co.toukei.log.lib.common.enableIf
import jp.co.toukei.log.lib.common.materialAlertDialogBuilder
import jp.co.toukei.log.lib.common.show
import jp.co.toukei.log.lib.util.Differ
import jp.co.toukei.log.lib.util.SugoiAdapter
import jp.co.toukei.log.lib.util.SugoiAdapter.ValueBlock
import jp.co.toukei.log.lib.util.SwipeRemoveHelperCallback
import jp.co.toukei.log.trustar.Config
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.common.FullScreenDialogFragment
import jp.co.toukei.log.trustar.db.user.entity.IncidentalWork
import jp.co.toukei.log.trustar.db.user.entity.Shipper
import jp.co.toukei.log.trustar.feature.home.fragment.ShipperChooseDialogFragment
import jp.co.toukei.log.trustar.feature.nimachi.data.*
import jp.co.toukei.log.trustar.feature.nimachi.ui.*
import jp.co.toukei.log.trustar.feature.nimachi.vm.SheetItemEditVM
import jp.co.toukei.log.trustar.other.VeryBadDateTimePicker
import jp.co.toukei.log.trustar.showAt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import splitties.views.textResource
import java.text.SimpleDateFormat
import java.util.*

/**
 * 待機・附帯作業詳細・編集
 *
 * - 作業を選択[SheetWorkSelection]
 * - 荷主変更[ShipperChooseDialogFragment]
 * - 詳細[SheetItem]
 */
class SheetItemEdit : FullScreenDialogFragment<SheetItemEdit.Arg>() {

    class Arg(
        @JvmField val target: EditTarget
    )

    private val scope = MainScope()

    override fun onDestroyView() {
        super.onDestroyView()
        scope.cancel()
    }

    override fun createView(owner: FragmentActivity): View {
        val argData = requireNotNull(arg)
        val editModel = getViewModel<SheetItemEditVM>()
        val editor = editModel.editTarget(argData.target)

        val passDataShipper = getViewModel<PassDataVM<Shipper, Shipper>>("s")
        val passDataWork =
            getViewModel<PassDataVM<List<IncidentalWork?>, List<IncidentalWork>>>("w")

        val u = RecyclerViewButton(owner)
        u.button.textResource = R.string.update_content
        u.button.setOnClickListener {
            scope.launch(Dispatchers.Main) {
                val h = editModel.save()
                dismiss()
                val t = argData.target
                if (t is EditTarget.Add && h != null) {
                    SheetItem().also { it.arg = SheetItem.Arg(t.binDetail, h) }.showAt(owner)
                }
            }
        }
        editor.displayShipper.observeNullable(viewLifecycleOwner) {
            u.button.enableIf(it != null)
        }

        val adapter = SugoiAdapter(10)
        val headCreator = sugoiCreator {
            SheetHeaderUI(this).apply {
                edit.textResource = R.string.change_shipper
                bottomEnd.apply {
                    show()
                    textResource = R.string.select_work
                    setOnClickListener {
                        SheetWorkSelection().also { it.arg = passDataWork }.showAt(owner)
                    }
                }
                edit.setOnClickListener {
                    /*荷主変更*/
                    ShipperChooseDialogFragment().also { it.arg = passDataShipper }.showAt(owner)
                }
            }
        }
        val labelCreator = LabelUI.Creator()
        val timeCreator = sugoiCreator {
            TimeItemUI<EditedTime>(this).apply {
                view.setOnClickListener {
                    /*待機・附帯作業時間設定・データ更新処理*/
                    withBoundValue {
                        it.context.pickDate(
                            targetBeginDate()?.date,
                            targetEndDate()?.date
                        ) { b, e ->
                            editor.editTime(this, b, e)
                            onBind(this)
                        }
                    }
                }
            }
        }
        val iconCreator = sugoiCreator { ClickToAddUI(this) }
        val dateTimeDiffCallback = TimeItemUI.Diff<EditedTime>()

        val headBlock = ValueBlock(headCreator)
        val nimachiPlaceholder = textPlaceholderBlock(R.string.unregistered_placeholder, true)
        val additionalPlaceholder = textPlaceholderBlock(R.string.unregistered_placeholder, true)
        val nimachiBlock = Block(editor, dateTimeDiffCallback) {
            nimachiPlaceholder.offline = it.isNotEmpty()
        }
        val additionalBlock = Block(editor, dateTimeDiffCallback) {
            additionalPlaceholder.offline = it.isNotEmpty()
        }

        headBlock.attachToAdapter(adapter)
        ValueBlock(labelCreator).applyValue(R.string.incidental_sheet_await)
            .attachToAdapter(adapter)
        nimachiBlock.attachToAdapter(adapter)
        nimachiPlaceholder.attachToAdapter(adapter)

        ValueBlock(iconCreator).applyValue(Click(0, editor)).attachToAdapter(adapter)
        ValueBlock(labelCreator).applyValue(R.string.incidental_sheet_addition_work)
            .attachToAdapter(adapter)
        additionalBlock.attachToAdapter(adapter)
        additionalPlaceholder.attachToAdapter(adapter)

        ValueBlock(iconCreator).applyValue(Click(1, editor)).attachToAdapter(adapter)

        u.list.adapter = adapter

        editor.displayData.observeNullable(viewLifecycleOwner) {
            headBlock.applyValue(it)
            nimachiBlock.submitList(it?.times?.nimachiTime(), timeCreator)
            additionalBlock.submitList(it?.times?.additionalTime(), timeCreator)
            passDataShipper.source.value = it?.shipper
            passDataWork.source.value = it?.works
        }

        passDataShipper.result.observeNonNull(viewLifecycleOwner) {
            it.getContentIfNotHandled().let { s -> s?.let(editor::setShipper) }
        }
        passDataWork.result.observeNonNull(viewLifecycleOwner) {
            it.getContentIfNotHandled().let { w -> editor.setWorks(w) }
        }

        ItemTouchHelper(
            SwipeRemoveHelperCallback(
                adapter,
                EditedTime::class.java
            )
        ).attachToRecyclerView(u.list)

        return u.view
    }

    private class Block(
        private val editor: SheetItemEditVM.Editor,
        diffCallback: Differ.DiffCallback<EditedTime>,
        listUpdatedListener: ((List<EditedTime>) -> Unit)? = null
    ) : SugoiAdapter.DiffListBlock<EditedTime>(
        diffCallback, listUpdatedListener
    ) {
        override fun onRemovedItem(item: EditedTime) {
            /*待機・附帯作業時間設定・データ削除処理*/
            editor.deleteTime(item)
        }
    }

    private inner class Click(
        private val type: Int,
        private val editor: SheetItemEditVM.Editor
    ) : View.OnClickListener {
        override fun onClick(it: View) {
            /*待機・附帯作業時間設定・追加を押下時に起動されるイベント*/
            it.context.pickDate(null, null) { b, e ->
                /*待機・附帯作業時間設定・データ追加処理*/
                editor.addTime(TimeItem(b, e, type))
            }
        }
    }

    private fun Context.pickDate(
        begin: Long?,
        end: Long?,
        callback: (begin: Long?, end: Long?) -> Unit
    ) {
        val d = materialAlertDialogBuilder {}.create()
        val bad = VeryBadDateTimePicker(
            this,
            R.color.colorPrimary,
            16F,
            R.string.pick_start_time,
            R.string.pick_end_time,
            R.string.determine,
            (begin ?: System.currentTimeMillis()).let(::Date),
            (end ?: System.currentTimeMillis()).let(::Date),
            SimpleDateFormat("yyyy年MM月dd日 E", Config.locale),
            Config.locale
        ) { b, e ->
            /*時間入力・確定押下時に起動されるイベント*/
            val bt = b.time
            val et = e.time
            if (bt < et) {
                callback(bt, et)
                if (d.isShowing) d.dismiss()
            } else {
                materialAlertDialogBuilder {
                    setMessage(R.string.invalid_time_range)
                    setPositiveButton(android.R.string.ok, null)
                }.show()
            }
        }
        d.setView(bad.view)
        d.show()
    }
}
