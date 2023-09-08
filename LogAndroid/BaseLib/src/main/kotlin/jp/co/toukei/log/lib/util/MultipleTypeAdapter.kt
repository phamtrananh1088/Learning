package jp.co.toukei.log.lib.util

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class MultipleTypeAdapter<E : Any> : RecyclerView.Adapter<BindHolder<E>>() {

    @Suppress("UNCHECKED_CAST")
    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindHolder<E> {
        return holderCache.getValue(viewType)(parent.context) as BindHolder<E>
    }

    final override fun onBindViewHolder(holder: BindHolder<E>, position: Int) {}
    override fun onBindViewHolder(holder: BindHolder<E>, position: Int, payloads: MutableList<Any>) {
        holder.onBind(getItem(position), position, payloads)
    }

    abstract fun getItem(position: Int): E
    abstract fun selectHolder(item: E): Context.() -> BindHolder<out E>

    private val holderCache = IdGenerator<Context.() -> BindHolder<out E>>(8)

    override fun getItemViewType(position: Int): Int {
        val e = getItem(position)
        val h = selectHolder(e)
        return holderCache.genId(h)
    }
}
