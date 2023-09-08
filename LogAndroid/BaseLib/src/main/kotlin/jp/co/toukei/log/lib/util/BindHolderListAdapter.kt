package jp.co.toukei.log.lib.util

import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import jp.co.toukei.log.lib.assertFailed


abstract class BindHolderListAdapter<T : Any>(
        config: AsyncDifferConfig<T>
) : ListAdapter<T, BindHolder<T>>(config) {

    final override fun onBindViewHolder(holder: BindHolder<T>, position: Int) {
        assertFailed()
    }

    final override fun onBindViewHolder(holder: BindHolder<T>, position: Int, payloads: List<Any>) {
        holder.onBind(getItem(position), position, payloads)
    }
}
