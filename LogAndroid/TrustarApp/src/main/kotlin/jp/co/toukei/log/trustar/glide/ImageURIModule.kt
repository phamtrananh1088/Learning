package jp.co.toukei.log.trustar.glide

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import jp.co.toukei.log.trustar.Current
import java.io.InputStream

@GlideModule
class ImageURIModule : AppGlideModule() {

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.append(
            ImageURI::class.java,
            InputStream::class.java,
            ImageURILoaderFactory(Current.msgCachedHttpClientV2)
        )
    }
}
