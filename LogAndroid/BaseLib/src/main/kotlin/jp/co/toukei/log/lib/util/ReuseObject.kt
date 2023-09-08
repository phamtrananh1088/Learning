package jp.co.toukei.log.lib.util

import java.util.concurrent.atomic.AtomicReference

abstract class ReuseObject<T> {

    protected abstract fun make(): T

    private val v = AtomicReference<T>()

    fun take(): T {
        return v.getAndSet(null) ?: make()
    }

    fun reuse(value: T) {
        v.set(value)
    }

    inline fun <R> use(block: (T) -> R): R {
        val t = take()
        val r = block(t)
        reuse(t)
        return r
    }
}
