package jp.co.toukei.log.trustar.rest

import io.reactivex.rxjava3.core.Single
import jp.co.toukei.log.trustar.rest.post.*
import jp.co.toukei.log.trustar.rest.response.StateResult
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface SyncApi {

    @POST("coordinate/post")
    fun postGeo(
            @Query("api_key") apiKey: String,
            @Body data: Geo
    ): Single<StateResult>

    @POST("operation/post")
    fun postBin(
            @Query("api_key") apiKey: String,
            @Body data: BinPost
    ): Single<StateResult>

    @POST("operation/postIncidental")
    fun postIncidental(
            @Query("api_key") apiKey: String,
            @Body data: IncidentalPost
    ): Single<StateResult>

    @POST("notice/post")
    fun postNotice(
            @Query("api_key") apiKey: String,
            @Body data: NoticePost
    ): Single<StateResult>

    @POST("fuel/post")
    fun postFuel(
            @Query("api_key") apiKey: String,
            @Body data: FuelInfo
    ): Single<StateResult>

    @POST("localdata/post")
    fun postFailedData(
            @Query("api_key") apiKey: String,
            @Body data: LocalData
    ): Single<StateResult>

    @POST("operation/postImg")
    fun uploadImgData(
            @Query("api_key") apiKey: String,
            @Body data: PicPost
    ): Single<StateResult>

    @POST("appLog/postLogFile")
    fun postLogFile(
            @Query("api_key") apiKey: String,
            @Header("file_name") fileName: String,
            @Body data: RequestBody
    ): Single<StateResult>

    @POST("operation/postCollection")
    fun postCollection(
            @Query("api_key") apiKey: String,
            @Body data: CollectionPost
    ): Single<StateResult>
}
