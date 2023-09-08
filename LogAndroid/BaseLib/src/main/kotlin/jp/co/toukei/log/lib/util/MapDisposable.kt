package jp.co.toukei.log.lib.util

import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.exceptions.CompositeException

class MapDisposable<K> : Disposable {

    private var resources = mutableMapOf<K, Disposable>()

    @Volatile
    var disposed: Boolean = false

    override fun dispose() {
        if (disposed) return
        val set: List<Disposable>
        synchronized(this) {
            if (disposed) return
            disposed = true
            set = resources.values.toList()
            resources.clear()
        }
        dispose(set)
    }

    fun clear() {
        if (disposed) return
        val set: List<Disposable>
        synchronized(this) {
            if (disposed) return
            set = resources.values.toList()
            resources.clear()
        }
        dispose(set)
    }

    fun put(key: K, d: Disposable): Boolean {
        if (!disposed) {
            synchronized(this) {
                if (!disposed) {
                    val old = resources.put(key, d)
                    if (old != null && old !== d)
                        old.dispose()
                    return true
                }
            }
        }
        d.dispose()
        return false
    }

    fun take(key: K): Disposable? {
        return resources.remove(key)
    }

    fun keyDisposed(key: K): Boolean? {
        return resources[key]?.isDisposed
    }

    override fun isDisposed(): Boolean = disposed

    private fun dispose(set: Iterable<Disposable>) {
        val err = set.mapNotNull { runCatching { it.dispose() }.exceptionOrNull() }
        if (err.isNotEmpty()) throw CompositeException(err)
    }
}
