package jp.co.toukei.log.lib.util

import okio.Buffer
import okio.ForwardingSource
import okio.Source

class ProgressForwardSource(private val total: Long, source: Source) : ForwardingSource(source) {

    private var read = 0L

    val percentage: Int
        get() = if (total <= 0) -1 else (read * 100 / total).toInt()

    override fun read(sink: Buffer, byteCount: Long): Long {
        return super.read(sink, byteCount).also {
            if (it > 0) read += it
        }
    }
}
