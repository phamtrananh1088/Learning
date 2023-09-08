package jp.co.toukei.log.trustar.feature.collect.fragment

import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import jp.co.toukei.log.lib.common.setLayoutParams
import jp.co.toukei.log.lib.getViewModel
import jp.co.toukei.log.lib.lengthFilter
import jp.co.toukei.log.lib.observeNonNull
import jp.co.toukei.log.lib.toBindHolder
import jp.co.toukei.log.lib.util.BindHolder
import jp.co.toukei.log.lib.util.MultipleTypeAdapter
import jp.co.toukei.log.lib.withBoundValue
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.common.FullScreenDialogFragment
import jp.co.toukei.log.trustar.db.user.entity.BinDetail
import jp.co.toukei.log.trustar.db.user.entity.CollectionGroup
import jp.co.toukei.log.trustar.dialogForEdit
import jp.co.toukei.log.trustar.feature.collect.ui.CollectFragmentUI
import jp.co.toukei.log.trustar.feature.collect.ui.GroupUI
import jp.co.toukei.log.trustar.feature.collect.ui.Item
import jp.co.toukei.log.trustar.feature.collect.ui.ItemUI
import jp.co.toukei.log.trustar.feature.collect.vm.CollectFragmentVM
import kotlinx.coroutines.launch

class CollectFragment : FullScreenDialogFragment<BinDetail>() {

    override fun createView(owner: FragmentActivity): View {
        val ui = CollectFragmentUI(owner)
        val vm = getViewModel<CollectFragmentVM>()

        val adapter = object : MultipleTypeAdapter<Item>() {
            private val differ = AsyncListDiffer(
                AdapterListUpdateCallback(this),
                AsyncDifferConfig.Builder(object : DiffUtil.ItemCallback<Item>() {
                    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
                        return oldItem.sameItem(newItem)
                    }

                    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
                        return oldItem.sameContent(newItem)
                    }
                }).build()
            )

            override fun getItem(position: Int): Item {
                return differ.currentList[position]
            }

            override fun getItemCount(): Int = differ.currentList.size

            fun submitList(list: List<Pair<CollectionGroup, List<CollectFragmentVM.Row>>>) {
                val d = mutableListOf<Item>()
                list.forEach { (k, v) ->
                    d += Item.Group(k)
                    v.forEach { d += Item.Row(it) }
                }
                differ.submitList(d)
            }

            private val groupHolder: Context.() -> BindHolder<Item.Group> = {
                val u = GroupUI(owner)
                val h = u.toBindHolder()
                u.apply {
                    view.setLayoutParams()
                    addButton.setOnClickListener {
                        withBoundValue {
                            it.context.dialogForEdit(R.string.collections, {
                                lengthFilter(20)
                            }) {
                                it?.let { vm.addRow(it, group) }
                            }
                        }
                    }
                }
                h
            }

            private val rowHolder: Context.() -> BindHolder<Item.Row> = {
                val u = ItemUI(owner)
                val h = u.toBindHolder()
                u.apply {
                    view.setLayoutParams()
                    content.setOnClickListener {
                        withBoundValue {
                            if (row.result.emptyCd()) {
                                it.context.dialogForEdit(R.string.collections, {
                                    setText(row.name)
                                    lengthFilter(20)
                                }) {
                                    it?.let { row.name = it }
                                    notifyItemChanged(h.bindingAdapterPosition)
                                }
                            }
                        }
                    }
                }
                h
            }

            override fun selectHolder(item: Item): Context.() -> BindHolder<out Item> {
                return when (item) {
                    is Item.Group -> groupHolder
                    is Item.Row -> rowHolder
                }
            }
        }

        argLiveData.observeNonNull(viewLifecycleOwner) {
            ui.title.text = it.place.nm1
            vm.setBinDetail(it)
        }
        vm.collectionsLiveData.observeNonNull(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        ui.list.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
        }
        ui.updateButton.setOnClickListener {
            lifecycleScope.launch {
                vm.save()
                dismiss()
            }
        }
        ui.cancelButton.setOnClickListener {
            dismiss()
        }
        Current.syncCollection(true)
        return ui.view
    }
}
