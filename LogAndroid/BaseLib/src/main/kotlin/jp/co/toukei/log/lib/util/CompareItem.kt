package jp.co.toukei.log.lib.util

import  androidx.recyclerview.widget.DiffUtil

/**
 * @see [DiffUtil.Callback.areItemsTheSame]
 */
interface CompareItem<T : Any> {
    fun sameItem(other: T): Boolean
}
