package jp.co.toukei.log.trustar.rest.api

import io.reactivex.rxjava3.core.Single
import jp.co.toukei.log.trustar.rest.annotation.InjectHttpApi
import jp.co.toukei.log.trustar.rest.annotation.Type
import jp.co.toukei.log.trustar.rest.model.FileKey
import jp.co.toukei.log.trustar.rest.model.FileKey2
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query
import retrofit2.http.Streaming

interface FileApi {

    @InjectHttpApi(Type.ApiKeyAndTokenHeader, Type.CompanyCdHeader)
    @GET("File/Download")
    @Streaming
    fun download(@Query("fileKey") fileKey: String): Single<ResponseBody>

    @InjectHttpApi(Type.ApiKeyAndTokenHeader, Type.CompanyCdHeader)
    @POST("File/Upload")
    @Multipart
    fun uploadFile(
        @Part buffer: MultipartBody.Part,
    ): Single<FileKey2>

    @InjectHttpApi(Type.ApiKeyAndTokenHeader, Type.CompanyCdHeader)
    @POST("Image/Upload")
    @Multipart
    fun uploadImage(
        @Part buffer: MultipartBody.Part,
    ): Single<FileKey>

}
