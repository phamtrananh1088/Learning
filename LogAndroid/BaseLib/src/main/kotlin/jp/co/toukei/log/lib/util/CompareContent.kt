package jp.co.toukei.log.lib.util

import  androidx.recyclerview.widget.DiffUtil

/**
 * @see [DiffUtil.Callback.areContentsTheSame]
 */
interface CompareContent<T : Any> {
    fun sameContent(other: T): Boolean
}
