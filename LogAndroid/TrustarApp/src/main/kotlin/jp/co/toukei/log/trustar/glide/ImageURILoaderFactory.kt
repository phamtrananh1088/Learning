package jp.co.toukei.log.trustar.glide

import android.net.Uri
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.ModelCache
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import jp.co.toukei.log.lib.glide.OkHttpStreamFetcher
import okhttp3.Call
import java.io.InputStream

class ImageURILoaderFactory(private val client: Call.Factory) :
    ModelLoaderFactory<ImageURI, InputStream> {

    private val modelCache = ModelCache<ImageURI, GlideUrl>(500)

    override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<ImageURI, InputStream> {
        val uriFactory = multiFactory.build(Uri::class.java, InputStream::class.java)
        return object : ModelLoader<ImageURI, InputStream> {
            override fun buildLoadData(
                model: ImageURI,
                width: Int,
                height: Int,
                options: Options,
            ): ModelLoader.LoadData<InputStream>? {
                if (!model.handle) {
                    return uriFactory.buildLoadData(model.uri, width, height, options)
                }
                val g: GlideUrl = modelCache.get(model, width, height)
                    ?: GlideUrl("${model.uri}?width=$width&height=$height").apply {
                        modelCache.put(model, width, height, this)
                    }
                return ModelLoader.LoadData(g, OkHttpStreamFetcher(client, g))
            }

            override fun handles(model: ImageURI): Boolean = true
        }
    }

    override fun teardown() {}
}
