package jp.co.toukei.log.trustar.feature.nimachi.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import jp.co.toukei.log.lib.common.ArgBottomDialogFragment
import jp.co.toukei.log.lib.getViewModel
import jp.co.toukei.log.lib.observeNonNull
import jp.co.toukei.log.lib.observeNullable
import jp.co.toukei.log.lib.sugoiCreator
import jp.co.toukei.log.lib.textPlaceholderBlock
import jp.co.toukei.log.lib.util.BindHolder
import jp.co.toukei.log.lib.util.SugoiAdapter
import jp.co.toukei.log.lib.withBoundValue
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.db.user.embedded.IncidentalListItem
import jp.co.toukei.log.trustar.db.user.entity.BinDetail
import jp.co.toukei.log.trustar.feature.home.fragment.BinDetailsDialogFragment
import jp.co.toukei.log.trustar.feature.home.fragment.OperationFragment
import jp.co.toukei.log.trustar.feature.nimachi.data.EditTarget
import jp.co.toukei.log.trustar.feature.nimachi.ui.SheetListItemUI
import jp.co.toukei.log.trustar.feature.nimachi.ui.SheetListUI
import jp.co.toukei.log.trustar.feature.nimachi.vm.SheetListVM
import jp.co.toukei.log.trustar.showAt

/**
 * 待機・附帯 荷主一覧
 *
 * - 作業入力 [OperationFragment]
 * - 配送先詳細 [BinDetailsDialogFragment]
 */
class SheetList : ArgBottomDialogFragment<BinDetail>() {

    override fun createView(owner: FragmentActivity): View {
        val ui = SheetListUI(owner)

        ui.create.setOnClickListener {
            /*新規登録押下時に起動されるイベント*/
            arg?.let { bin ->
                SheetItemEdit().also { it.arg = SheetItemEdit.Arg(EditTarget.Add(bin)) }
                    .showAt(owner)
                dismiss()
            }
        }

        val vm = getViewModel<SheetListVM>()
        val diff = SheetListItemUI.Diff()

        val adapter = SugoiAdapter(2)
        val placeholder = textPlaceholderBlock(R.string.unregistered_placeholder, true)
        val block = object : SugoiAdapter.DiffListBlock<IncidentalListItem>(
            diff,
            { placeholder.offline = it.isNotEmpty() }
        ) {
            override fun onRemovedItem(item: IncidentalListItem) {
                /*待機・附帯作業詳細・入力を削除を押下時に起動されるイベント*/
                vm.removeIncidental(item)
            }
        }

        placeholder.attachToAdapter(adapter)
        block.attachToAdapter(adapter)

        val creator = sugoiCreator {
            SheetListItemUI(this).apply {
                view.setOnClickListener {
                    /*荷主押下時に起動されるイベント*/
                    /*荷主一覧・行を押下時に起動されるイベント*/
                    arg?.let { bin ->
                        withBoundValue {
                            SheetItem().also { it.arg = SheetItem.Arg(bin, sheet) }.showAt(owner)
                            dismiss()
                        }
                    }
                }
            }
        }

        ui.list.adapter = adapter

        vm.apply {
            argLiveData.observeNonNull(viewLifecycleOwner) {
                setBinDetail(it)
            }
            listLiveData.observeNullable(viewLifecycleOwner) {
                block.submitList(it, creator)
            }
        }

        ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                return false
            }

            override fun getSwipeDirs(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
            ): Int {
                val holder = viewHolder as? BindHolder<*>
                return if (holder?.valueBind?.boundValue !is IncidentalListItem) 0
                else super.getSwipeDirs(recyclerView, viewHolder)
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val p = viewHolder.bindingAdapterPosition
                if (!adapter.deleteAt(p)) adapter.notifyItemChanged(p)
            }
        }).attachToRecyclerView(ui.list)

        return ui.view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Current.syncIncidental()
    }
}
