package jp.co.toukei.log.lib.glide

import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.HttpException
import com.bumptech.glide.load.data.DataFetcher
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.util.ContentLengthInputStream
import okhttp3.*
import okhttp3.internal.closeQuietly
import java.io.Closeable
import java.io.IOException
import java.io.InputStream

/**
 * just kotlin version of com.github.bumptech.glide:okhttp3-integration
 */
class OkHttpStreamFetcher(
        private val client: Call.Factory,
        private val url: GlideUrl
) : DataFetcher<InputStream>, Callback {

    private var loadCallback: DataFetcher.DataCallback<in InputStream>? = null
    @Volatile
    private var call: Call? = null

    override fun cancel() {
        call?.cancel()
    }

    override fun loadData(priority: Priority, callback: DataFetcher.DataCallback<in InputStream>) {
        loadCallback = callback
        val c = Request.Builder()
                .url(url.toStringUrl())
                .apply { url.headers.forEach { (t, u) -> addHeader(t, u) } }
                .build()
                .let(client::newCall)
        call = c
        c.enqueue(this)
    }

    override fun onFailure(call: Call, e: IOException) {
        loadCallback?.onLoadFailed(e)
    }

    private var stream: Closeable? = null
    private var body: ResponseBody? = null

    override fun onResponse(call: Call, response: Response) {
        val lc = loadCallback ?: return
        val b = response.body ?: return lc.onLoadFailed(NullPointerException())
        body = b
        if (response.isSuccessful) {
            val content = ContentLengthInputStream.obtain(b.byteStream(), b.contentLength())
            stream = content
            lc.onDataReady(content)
        } else {
            lc.onLoadFailed(HttpException(response.message, response.code))
        }
    }

    override fun cleanup() {
        stream?.closeQuietly()
        body?.closeQuietly()
        loadCallback = null
    }

    override fun getDataClass(): Class<InputStream> = InputStream::class.java
    override fun getDataSource(): DataSource = DataSource.REMOTE
}
