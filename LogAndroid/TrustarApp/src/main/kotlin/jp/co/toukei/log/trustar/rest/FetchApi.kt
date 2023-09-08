package jp.co.toukei.log.trustar.rest

import io.reactivex.rxjava3.core.Single
import jp.co.toukei.log.trustar.rest.post.PicQuery
import jp.co.toukei.log.trustar.rest.post.UnscheduledBin
import jp.co.toukei.log.trustar.rest.response.*
import jp.co.toukei.log.trustar.user.UserInfo
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query


interface FetchApi {

    @POST("master/getdata")
    fun masterData(
            @Query("api_key") apiKey: String,
            @Body userInfo: UserInfo
    ): Single<Master>

    @POST("operation/getdata")
    fun binData(
            @Query("api_key") apiKey: String,
            @Body userInfo: UserInfo
    ): Single<Bin>

    @POST("operation/incidentalGetdata")
    fun incidentalData(
            @Query("api_key") apiKey: String,
            @Body userInfo: UserInfo
    ): Single<Incidental>

    @POST("notice/getdata")
    fun noticeData(
            @Query("api_key") apiKey: String,
            @Body userInfo: UserInfo
    ): Single<NoticeInfo>

    @POST("operation/getImg")
    fun getImgData(
            @Query("api_key") apiKey: String,
            @Body query: PicQuery
    ): Single<ResponseBody>

    @POST("unplannedAllocation/post")
    fun startUnscheduledBin(
            @Query("api_key") apiKey: String,
            @Body data: UnscheduledBin
    ): Single<UnscheduledBinAllocation>
}
