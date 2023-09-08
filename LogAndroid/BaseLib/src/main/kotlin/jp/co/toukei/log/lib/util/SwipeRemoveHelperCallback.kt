package jp.co.toukei.log.lib.util

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class SwipeRemoveHelperCallback(
        private val adapter: SugoiAdapter,
        private val boundValueType: Class<*>
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val p = viewHolder.adapterPosition
        if (!adapter.deleteAt(p)) adapter.notifyItemChanged(p)
    }

    override fun getSwipeDirs(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val holder = viewHolder as? BindHolder<*>
        return if (holder?.valueBind?.boundValue?.javaClass !== boundValueType) 0
        else super.getSwipeDirs(recyclerView, viewHolder)
    }
}
