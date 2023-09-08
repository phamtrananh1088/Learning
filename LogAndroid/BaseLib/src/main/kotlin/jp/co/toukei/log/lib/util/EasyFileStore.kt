package jp.co.toukei.log.lib.util

import android.net.Uri
import androidx.collection.ArrayMap
import androidx.core.net.toUri
import jp.co.toukei.log.lib.child
import jp.co.toukei.log.lib.createTempFileInDir
import jp.co.toukei.log.lib.deleteQuickly
import jp.co.toukei.log.lib.makeDirs
import okhttp3.internal.closeQuietly
import java.io.Closeable
import java.io.File
import java.io.FileFilter
import java.io.InputStream
import java.util.concurrent.atomic.AtomicReference

abstract class EasyFileStore<in T>(private val dir: File) : Closeable {

    private val tmpDir: File = dir.child(".tmp")

    abstract fun fileName(identity: T): String

    private fun targetFile(identity: T): File {
        return dir.child(fileName(identity))
    }

    fun getFile(identity: T): Uri? {
        val target = targetFile(identity)
        return if (target.canRead()) target.toUri() else null
    }

    fun getFileInputStream(identity: T): InputStream? {
        val target = targetFile(identity)
        return if (target.canRead()) target.inputStream() else null
    }

    fun store(identity: T, stream: InputStream) {
        val h = closeableMap.get() ?: return
        val new = tmpDir.makeDirs().createTempFileInDir()
        val out = new.outputStream()
        h[identity] = out
        closeableMap.get() ?: return // ensure
        try {
            out.use { stream.copyTo(it) }
            val target = targetFile(identity)
            target.delete()
            new.renameTo(target)
        } catch (e: Throwable) {
            new.delete()
        } finally {
            h.remove(identity)
        }
    }

    private val closeableMap = AtomicReference(ArrayMap<T, Closeable>(8))

    override fun close() {
        cancel(closeableMap.getAndSet(null)?.values)
    }

    @Synchronized
    fun interrupt() {
        cancel(closeableMap.get()?.values)
    }

    @Synchronized
    fun removeAllFiles() {
        interrupt()
        dir.deleteQuickly()
    }

    @Synchronized
    fun removeFiles(identity: List<T>) {
        closeableMap.get()?.apply {
            identity.forEach { remove(it)?.closeQuietly() }
        }
        if (dir.listFiles(FileFilter { it.isFile }).isNullOrEmpty()) return

        identity.forEach { targetFile(it).delete() }
    }

    private fun cancel(closeable: Iterable<Closeable>?) {
        closeable?.forEach(Closeable::closeQuietly)
        tmpDir.deleteQuickly()
    }
}
