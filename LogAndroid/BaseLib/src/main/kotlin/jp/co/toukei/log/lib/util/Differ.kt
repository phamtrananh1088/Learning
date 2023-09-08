package jp.co.toukei.log.lib.util

import androidx.recyclerview.widget.DiffUtil
import jp.co.toukei.log.lib.assertFailed

class Differ<out T>(
        private val oldList: List<T>,
        private val newList: List<T>,
        private val callback: DiffCallback<T>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val o = oldList[oldItemPosition]
        val n = newList[newItemPosition]
        if (o != null && n != null)
            return callback.areItemsTheSame(o, n, oldItemPosition, newItemPosition)
        return o == null && n == null
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val o = oldList[oldItemPosition]
        val n = newList[newItemPosition]
        if (o != null && n != null)
            return callback.areContentsTheSame(o, n, oldItemPosition, newItemPosition)
        if (o == null && n == null)
            return true
        assertFailed()
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val o = oldList[oldItemPosition]
        val n = newList[newItemPosition]
        if (o != null && n != null)
            return callback.getChangePayload(o, n, oldItemPosition, newItemPosition)
        assertFailed()
    }

    interface DiffCallback<T> {
        fun areItemsTheSame(oldItem: T, newItem: T, oldItemPosition: Int, newItemPosition: Int): Boolean
        fun areContentsTheSame(oldItem: T, newItem: T, oldItemPosition: Int, newItemPosition: Int): Boolean
        fun getChangePayload(oldItem: T, newItem: T, oldItemPosition: Int, newItemPosition: Int): Any?
    }

    abstract class SimpleDiffCallback<T : Any> : DiffCallback<T> {

        override fun getChangePayload(oldItem: T, newItem: T, oldItemPosition: Int, newItemPosition: Int): Any? = null
    }

    open class DefaultDiffCallback<T> : DiffCallback<T> {

        override fun areItemsTheSame(oldItem: T, newItem: T, oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: T, newItem: T, oldItemPosition: Int, newItemPosition: Int): Boolean {
            return true
        }

        override fun getChangePayload(oldItem: T, newItem: T, oldItemPosition: Int, newItemPosition: Int): Any? {
            return null
        }
    }
}
