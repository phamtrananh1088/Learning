package jp.co.toukei.log.trustar.user

import jp.co.toukei.log.trustar.currentFormattedDateString
import java.io.*
import java.util.concurrent.atomic.AtomicReference

class SimpleLog(private val file: File) : Closeable, Flushable {

    private val out = AtomicReference(FileOutputStream(file, true).bufferedWriter())

    @Synchronized
    fun logLine(text: CharSequence) {
        runCatching {
            out.get().appendLine(buildString(20 + text.length) {
                append(currentFormattedDateString()).append('\t').append(text)
            })
        }
    }

    @Synchronized
    override fun flush() {
        runCatching { out.get()?.flush() }
    }

    @Synchronized
    override fun close() {
        runCatching { out.getAndSet(null)?.close() }
    }

    @Synchronized
    fun export(target: OutputStream): Boolean {
        val o = out.get() ?: return false
        try {
            o.close()
        } catch (e: Throwable) {
            return false
        }
        return try {
            file.inputStream().use {
                it.copyTo(target)
            }
            true
        } catch (e: Throwable) {
            false
        } finally {
            out.set(FileOutputStream(file, false).bufferedWriter())
        }
    }
}
