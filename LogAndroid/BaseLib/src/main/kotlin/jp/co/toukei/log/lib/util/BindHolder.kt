package jp.co.toukei.log.lib.util

import android.view.View
import androidx.recyclerview.widget.RecyclerView

open class BindHolder<T : Any>(
        view: View,
        @JvmField val valueBind: ValueBind<T>
) : RecyclerView.ViewHolder(view) {

    fun onBind(value: T, position: Int, payloads: List<Any>) {
        valueBind.bind(value, position, payloads)
    }
}
