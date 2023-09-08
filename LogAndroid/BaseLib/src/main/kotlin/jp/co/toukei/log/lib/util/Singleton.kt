package jp.co.toukei.log.lib.util

abstract class Singleton<T : Any>(private val lock: Any) {

    @Volatile
    private var t: T? = null

    protected abstract fun create(): T

    protected open fun onCleared(t: T) {}

    fun clear() {
        var o: T?
        synchronized(lock) {
            o = t
            t = null
        }
        o?.let(::onCleared)
    }

    fun getInstance(): T {
        return t ?: synchronized(lock) {
            t ?: create().also { t = it }
        }
    }
}
