package jp.co.toukei.log.trustar.rest

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import jp.co.toukei.log.lib.moshi.RetrofitConverter
import jp.co.toukei.log.trustar.db.user.entity.Notice
import jp.co.toukei.log.trustar.rest.post.*
import jp.co.toukei.log.trustar.rest.response.Bin
import jp.co.toukei.log.trustar.rest.response.Incidental
import jp.co.toukei.log.trustar.rest.response.Master
import jp.co.toukei.log.trustar.rest.response.RawChart
import jp.co.toukei.log.trustar.rest.response.StateResult
import jp.co.toukei.log.trustar.user.UserInfo
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface OfflineSyncApi {

    @POST("master/getdata")
    fun masterData(
        @Body userInfo: UserInfo,
    ): Single<Master>

    @POST("operation/getdata")
    fun binData(
        @Body userInfo: UserInfo,
    ): Single<Bin>

    @POST("operation/incidentalGetdata")
    fun incidentalData(
        @Body userInfo: UserInfo,
    ): Single<Incidental>

    @POST("operation/getChartData")
    @RetrofitConverter.UnWrapper("binCharts")
    fun getChartData(
        @Body userInfo: UserInfo,
    ): Single<RawChart>

    @POST("notice/getdata")
    @RetrofitConverter.UnWrapper("notices")
    fun noticeData(
        @Body userInfo: UserInfo,
    ): Single<List<Notice>>

    @POST("coordinate/post")
    fun postGeo(
        @Body data: Geo
    ): Single<StateResult>

    @POST("operation/post")
    fun postBin(
        @Body data: BinPost
    ): Single<StateResult>

    @POST("operation/postIncidental")
    fun postIncidental(
        @Body data: IncidentalPost
    ): Single<StateResult>

    @POST("notice/post")
    fun postNotice(
        @Body data: NoticePost
    ): Single<StateResult>

    @POST("fuel/post")
    fun postFuel(
        @Body data: FuelInfo
    ): Single<StateResult>

    @POST("localdata/post")
    fun postFailedData(
        @Body data: LocalData
    ): Single<StateResult>

    @POST("operation/postImg")
    fun uploadImgData(
        @Body data: PicPost
    ): Single<StateResult>

    @POST("appLog/postLogFile")
    fun postLogFile(
        @Header("file_name") fileName: String,
        @Body data: RequestBody
    ): Single<StateResult>

    @POST("operation/postCollection")
    fun postCollection(
        @Body data: CollectionPost
    ): Single<StateResult>

    @POST("rest/post")
    fun postRest(
        @Body data: RestPost
    ): Single<StateResult>

    @POST("operation/postChart")
    @Multipart
    fun postChart(
        @Part chartJson: MultipartBody.Part,
        @Part images: List<MultipartBody.Part>,
    ): Completable

    @POST("Sensor/Upload")
    @Multipart
    fun uploadSensorCSV(
        @Part sensor: MultipartBody.Part,
    ): Completable

}
