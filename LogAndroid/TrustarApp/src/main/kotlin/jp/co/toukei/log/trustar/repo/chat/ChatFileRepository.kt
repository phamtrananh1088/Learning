package jp.co.toukei.log.trustar.repo.chat

import io.reactivex.rxjava3.core.Single
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.rest.model.FileKey
import jp.co.toukei.log.trustar.rest.model.FileKey2
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ChatFileRepository {

    fun uploadFile(filename: String, body: RequestBody): Single<FileKey> {
        return uploadFile2(filename, body).map { it.key }
    }

    fun uploadFile2(filename: String, body: RequestBody): Single<FileKey2> {
        val p = MultipartBody.Part.createFormData("buffer", filename, body)
        return Current.chatFileApi.uploadFile(p)
    }

    fun uploadImage(filename: String, body: RequestBody): Single<FileKey> {
        val p = MultipartBody.Part.createFormData("buffer", filename, body)
        return Current.chatFileApi.uploadImage(p)
    }

    fun uploadVideo(filename: String, body: RequestBody): Single<FileKey2> {
        return uploadFile2(filename, body)
    }

}
