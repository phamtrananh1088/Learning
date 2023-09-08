package jp.co.toukei.log.trustar.rest.api

import io.reactivex.rxjava3.core.Single
import jp.co.toukei.log.trustar.rest.annotation.InjectHttpApi
import jp.co.toukei.log.trustar.rest.annotation.Type
import jp.co.toukei.log.trustar.rest.post.PicQuery
import jp.co.toukei.log.trustar.rest.post.UnscheduledBin
import jp.co.toukei.log.trustar.rest.response.UnscheduledBinAllocation
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface OnlineApi {

    @POST("unplannedAllocation/post")
    @InjectHttpApi(Type.ApiKeyQuery)
    fun startUnscheduledBin(
        @Body data: UnscheduledBin,
    ): Single<UnscheduledBinAllocation>

    @POST("unplannedAllocation/getToken")
    @InjectHttpApi(Type.ApiKeyQuery, Type.PostUserInfo)
    fun getToken(): Single<String>

    @POST("operation/getImg")
    @InjectHttpApi(Type.ApiKeyQuery)
    fun getImgData(
         @Body query: PicQuery,
    ): Single<ResponseBody>

    @POST("Operation/GetChartImg")
    @InjectHttpApi(Type.ApiKeyQuery, Type.CompanyCdHeader)
    fun getChartImage(
        @Header("userId") userId: String,
        @Header("fileId") fileId: String,
    ): Single<ResponseBody>

}
