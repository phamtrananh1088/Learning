package jp.co.toukei.log.lib.util

import android.content.Context
import android.view.ViewGroup
import androidx.annotation.MainThread
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import jp.co.toukei.log.lib.assertFailed
import jp.co.toukei.log.lib.runOnComputation
import jp.co.toukei.log.lib.runOnUi
import jp.co.toukei.log.lib.util.SugoiAdapter.E
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference

class SugoiAdapter(initBlockSize: Int) : Adapter<BindHolder<*>>() {

    interface Removable {
        fun remove(position: Int): Boolean
    }

    abstract class Block<T : Any> : ListUpdateCallback {

        final override fun onChanged(position: Int, count: Int, payload: Any?) {
            adapter.get()?.let {
                val found = it.headPosition(this)
                if (found >= 0) {
                    it.notifyItemRangeChanged(position + found, count, payload)
                }
            }
        }

        final override fun onMoved(fromPosition: Int, toPosition: Int) {
            adapter.get()?.let {
                val found = it.headPosition(this)
                if (found >= 0) {
                    it.notifyItemMoved(fromPosition + found, toPosition + found)
                }
            }
        }

        final override fun onInserted(position: Int, count: Int) {
            adapter.get()?.let {
                val found = it.headPosition(this)
                if (found >= 0) {
                    it.notifyItemRangeInserted(position + found, count)
                }
            }
        }

        final override fun onRemoved(position: Int, count: Int) {
            adapter.get()?.let {
                val found = it.headPosition(this)
                if (found >= 0) {
                    it.notifyItemRangeRemoved(position + found, count)
                }
            }
        }

        abstract fun getItemCount(): Int
        abstract fun getRawItem(position: Int): E<T>
        open fun getItem(position: Int): T = getRawItem(position).value

        private var adapter = AtomicReference<SugoiAdapter>()

        fun attachToAdapter(adapter: SugoiAdapter): Boolean {
            val b = this.adapter.compareAndSet(null, adapter)
            if (b) adapter.addBlock(this)
            return b
        }

        var offline: Boolean = false
            set(value) {
                if (value != field) {
                    val count = getItemCount()
                    if (value) onRemoved(0, count)
                    field = value
                    if (!value) onInserted(0, count)
                    adapter.get()?.offlineNotifier?.forEach { it(value, this) }
                }
            }

        open fun getId(value: T): Int = -1
        open fun onBind(holder: BindHolder<T>, value: T, position: Int, payloads: List<Any>) {
            holder.onBind(value, position, payloads)
        }
    }

    open class DiffListBlock<T : Any>(
        private val diffCallback: Differ.DiffCallback<in T>,
        private val listUpdatedListener: ((List<T>) -> Unit)? = null,
    ) : Block<T>(), Differ.DiffCallback<E<T>>, Removable {

        override fun areItemsTheSame(
            oldItem: E<T>,
            newItem: E<T>,
            oldItemPosition: Int,
            newItemPosition: Int,
        ): Boolean {
            return oldItem.creator === newItem.creator &&
                    diffCallback.areItemsTheSame(
                        oldItem.value,
                        newItem.value,
                        oldItemPosition,
                        newItemPosition
                    )
        }

        override fun areContentsTheSame(
            oldItem: E<T>,
            newItem: E<T>,
            oldItemPosition: Int,
            newItemPosition: Int,
        ): Boolean {
            return oldItem.creator === newItem.creator && diffCallback.areContentsTheSame(
                oldItem.value,
                newItem.value,
                oldItemPosition,
                newItemPosition
            )
        }

        override fun getChangePayload(
            oldItem: E<T>,
            newItem: E<T>,
            oldItemPosition: Int,
            newItemPosition: Int,
        ): Any? {
            return diffCallback.getChangePayload(
                oldItem.value,
                newItem.value,
                oldItemPosition,
                newItemPosition
            )
        }

        @Volatile
        private var currentList: List<E<T>> = emptyList()

        private val pending = AtomicInteger(0)

        override fun getItemCount() = currentList.size
        override fun getRawItem(position: Int): E<T> = currentList[position]

        private fun computeAndSet(expected: Int, l: List<E<T>>) {
            val result = DiffUtil.calculateDiff(Differ(currentList, l, this), false)
            val listener = listUpdatedListener
            val r = if (listener != null) l.map { it.value } else null
            runOnUi {
                if (pending.compareAndSet(expected, 0)) {
                    currentList = l
                    result.dispatchUpdatesTo(this)
                    if (r != null) listener?.invoke(r)
                }
            }
        }

        fun <E : T> submitList(list: Iterable<E>?, creator: Creator<in E>) {
            submitList(list?.asSequence(), creator)
        }

        fun <E : T> submitList(list: Sequence<E>?, creator: Creator<in E>) {
            submitList(list?.map { E(creator, it) })
        }

        fun submitList(iterable: Sequence<E<T>>?) {
            if (pending.get() < 0) return // removing, not able to change current state.
            val expected = pending.incrementAndGet()
            runOnComputation {
                val l = iterable?.toList() ?: emptyList()
                computeAndSet(expected, l)
            }
        }

        override fun remove(position: Int): Boolean {
            if (pending.compareAndSet(0, -1) && position in currentList.indices) {
                runOnComputation {
                    val s = currentList.toMutableList()
                    val removed = s.removeAt(position)
                    val result = DiffUtil.calculateDiff(Differ(currentList, s, this), false)
                    val listener = listUpdatedListener
                    val r = if (listener != null) s.map { it.value } else null
                    runOnUi {
                        pending.set(0)
                        currentList = s
                        result.dispatchUpdatesTo(this)
                        onRemovedItem(removed.value)
                        if (r != null) listener?.invoke(r)
                    }
                }
                return true
            }
            return false
        }

        protected open fun onRemovedItem(item: T) {}
    }

    class Adder<T : Any>(initSize: Int = 16) : Iterable<E<T>> {
        private val temp = ArrayList<E<T>>(initSize)

        override fun iterator(): Iterator<E<T>> = temp.iterator()

        fun <E : T> addItem(e: E, creator: Creator<E>) {
            temp.add(E(creator, e))
        }

        fun <E : T> addItems(iterable: Iterable<E>?, creator: Creator<E>) {
            iterable?.forEach { addItem(it, creator) }
        }
    }

    open class ArrayListBlock<T : Any>(initSize: Int) : Block<T>(), Removable {

        private val list = ArrayList<E<T>>(initSize)

        override fun getItemCount() = list.size
        override fun getRawItem(position: Int) = list[position]

        fun addList(items: List<E<T>>, position: Int = -1) {
            val size = getItemCount()
            val insert = if (position in 0..size) position else size
            list.addAll(insert, items)
            onInserted(insert, items.size)
        }

        fun clear() {
            val size = getItemCount()
            list.clear()
            onRemoved(0, size)
        }

        override fun remove(position: Int): Boolean {
            val size = getItemCount()
            val del = if (position in 0 until size) position else return false
            list.removeAt(del)
            onRemoved(del, 1)
            return true
        }
    }

    open class ValueBlock<T : Any>(
        private val creator: Creator<T>,
    ) : Block<T>() {
        private var e: E<T>? = null

        final override fun getItemCount(): Int = if (e == null) 0 else 1
        final override fun getRawItem(position: Int): E<T> {
            val value = e
            requireNotNull(value)
            return value
        }

        @MainThread
        fun applyValue(item: T?, payload: Any? = Unit): ValueBlock<T> {
            val old = e
            e = item?.let { E(creator, it) }

            if (old == null) {
                if (e != null) onInserted(0, 1)
            } else {
                if (e != null) onChanged(0, 1, payload)
                else onRemoved(0, 1)
            }
            return this
        }
    }

    abstract class Block2<T : Any> : Block<T>() {

        abstract fun item(position: Int): T

        abstract fun creatorAt(position: Int): Creator<T>

        override fun getItem(position: Int): T = item(position)

        override fun getRawItem(position: Int): E<T> {
            return E(creatorAt(position), item(position))
        }
    }

    class NoBind : ValueBind<Unit>() {
        override fun onBind(bound: Unit) {}
    }

    interface Creator<T : Any> {
        fun onCreateViewHolder(
            context: Context,
            parent: ViewGroup,
        ): BindHolder<T>
    }

    @Suppress("UNCHECKED_CAST")
    private fun addBlock(block: Block<out Any>) {
        if (hasBlock(block)) return
        val count = itemCount
        blockList.add(block as Block<Any>)
        notifyItemRangeInserted(count, block.getItemCount())
    }

    private fun hasBlock(block: Block<out Any>): Boolean {
        return blockList.find { it === block } != null
    }

    class E<out T> internal constructor(
        internal val creator: Creator<*>,
        @JvmField val value: T,
    )

    private val blockList: ArrayList<Block<Any>> = ArrayList(initBlockSize)
    private val blockIterable: Iterable<Block<Any>> =
        blockList.asSequence()
            .filterNot { it.offline }
            .asIterable()

    //todo unused references.
    private val creatorIdGenerator = IdGenerator<Creator<*>>(initBlockSize)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindHolder<*> {
        return creatorIdGenerator.getValue(viewType).onCreateViewHolder(parent.context, parent)
    }

    private fun getCreatorId(item: E<*>): Int {
        return creatorIdGenerator.genId(item.creator)
    }

    override fun getItemViewType(position: Int): Int {
        val b = blockByPosition(position)
        val block = b.value
        val p = b.int
        val item = block.getRawItem(p)
        return getCreatorId(item)
    }

    override fun getItemId(position: Int): Long {
        val b = blockByPosition(position)
        val block = b.value
        val p = b.int
        val item = block.getRawItem(p)
        val cid = getCreatorId(item)
        val id = block.getId(item.value)
        if (id == -1) return RecyclerView.NO_ID
        return (cid.toLong() shl 32) or (id.toLong() shl 32 ushr 32) // zA mAjiKkU.
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(
        holder: BindHolder<*>,
        position: Int,
        payloads: MutableList<Any>,
    ) {
        val b = blockByPosition(position)
        val block = b.value
        val p = b.int
        block.onBind(holder as BindHolder<Any>, block.getItem(p), p, payloads)
    }

    override fun onBindViewHolder(holder: BindHolder<*>, position: Int) {
        assertFailed()
    }

    override fun getItemCount(): Int = blockIterable.sumOf(Block<*>::getItemCount)

    private fun blockByPosition(position: Int): IntPair<Block<Any>> {
        return findBlockByPosition(position) ?: assertFailed()
    }

    private fun headPosition(block: Block<*>): Int {
        blockIterable.fold(0) { p, b ->
            if (b === block) return p else p + b.getItemCount()
        }
        return -1
    }

    // can be public access.
    private fun findBlockByPosition(position: Int): IntPair<Block<Any>>? {
        blockIterable.fold(position) { acc, block ->
            val c = block.getItemCount()
            val p = acc - c
            if (p < 0) return IntPair(acc, block) else p
        }
        return null
    }

    fun creatorAt(position: Int): Creator<*>? {
        return findBlockByPosition(position)?.let {
            it.value.getRawItem(it.int).creator
        }
    }

    fun deleteAt(position: Int): Boolean {
        val f = findBlockByPosition(position) ?: return false
        val r = f.value as? Removable ?: return false
        return r.remove(f.int)
    }

    private val offlineNotifier = LinkedList<(Boolean, Block<*>) -> Unit>()

    fun addOfflineListener(listener: (Boolean, Block<*>) -> Unit) {
        offlineNotifier += listener
    }
}
