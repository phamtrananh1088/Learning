package jp.co.toukei.log.lib.util

abstract class Pending<K : Any, V : Any> {

    private val pending = mutableMapOf<Any, V>()
    private val target = mutableMapOf<Any, V>()

    protected abstract fun createPending(k: K): V

    protected open fun convertKey(k: K): Any = k

    fun getPending(k: K): V {
        return pending.getOrPut(convertKey(k)) { createPending(k) }
    }

    fun getTarget(k: K): V? {
        return target[convertKey(k)]
    }

    fun commit(k: K): V? {
        val c = convertKey(k)
        val v = pending.remove(c) ?: return null
        target[c] = v
        return v
    }
}
