package jp.co.toukei.log.lib.util

import androidx.recyclerview.widget.RecyclerView
import jp.co.toukei.log.lib.assertFailed
import jp.co.toukei.log.lib.randomAccess

abstract class FixedAdapter<T : Any>(list: List<T>) : RecyclerView.Adapter<BindHolder<T>>() {

    constructor(arr: Array<out T>) : this(arr.asList())

    protected val list: List<T> = list.randomAccess()

    override fun getItemCount(): Int = list.size

    final override fun onBindViewHolder(holder: BindHolder<T>, position: Int) {
        assertFailed()
    }

    override fun onBindViewHolder(holder: BindHolder<T>, position: Int, payloads: List<Any>) {
        holder.onBind(list[position], position, payloads)
    }
}
