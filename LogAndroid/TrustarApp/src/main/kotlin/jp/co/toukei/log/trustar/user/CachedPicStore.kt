package jp.co.toukei.log.trustar.user

import android.net.Uri
import android.util.Base64
import io.reactivex.rxjava3.core.Maybe
import jp.co.toukei.log.lib.encodeToBase64String
import jp.co.toukei.log.lib.subscribeOnIO
import jp.co.toukei.log.lib.util.EasyFileStore
import jp.co.toukei.log.trustar.Config
import jp.co.toukei.log.trustar.rest.post.PicQuery
import java.io.File

class CachedPicStore(
    directory: File,
    private val user: LoggedUser
) : EasyFileStore<String>(directory) {

    override fun fileName(identity: String): String {
        return identity.toByteArray()
            .encodeToBase64String(Base64.NO_PADDING or Base64.NO_WRAP or Base64.URL_SAFE)
    }

    fun getOrDownload(
        identity: String,
        picId: String?
    ): Maybe<Uri> {
        val file = getFile(identity)
        return when {
            file != null -> Maybe.just(file)
            picId == null -> Maybe.empty()
            else -> Config.fetchApi
                .getImgData(user.token, PicQuery(picId, user.userInfo))
                .flatMapMaybe {
                    it.byteStream().use { s -> store(identity, s) }
                    getFile(identity)?.let { f -> Maybe.just(f) } ?: Maybe.empty()
                }
                .subscribeOnIO()
        }
    }
}
