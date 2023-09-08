package jp.co.toukei.log.lib.util

import jp.co.toukei.log.lib.pollAll
import java.io.Closeable
import java.io.IOException
import java.util.*

class ClosableContainer : Closeable {

    private var queue: Queue<Closeable>? = LinkedList()

    @Synchronized
    fun <T : Closeable> put(closeable: T): T {
        queue?.offer(closeable)
        return closeable
    }

    @Synchronized
    @Throws(IOException::class)
    override fun close() {
        val cause = mutableListOf<Throwable>()
        queue?.pollAll {
            try {
                it.close()
            } catch (e: Throwable) {
                cause += e
            }
        }
        queue = null
        if (cause.isNotEmpty()) {
            throw IOException().apply { cause.forEach(::addSuppressed) }
        }
    }
}
