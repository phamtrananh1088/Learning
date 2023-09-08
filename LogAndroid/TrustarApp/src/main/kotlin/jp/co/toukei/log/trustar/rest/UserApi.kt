package jp.co.toukei.log.trustar.rest

import io.reactivex.rxjava3.core.Single
import jp.co.toukei.log.trustar.rest.post.LoginRequest
import jp.co.toukei.log.trustar.rest.response.LoginResponse
import org.json.JSONObject
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query


interface UserApi {

    @POST("account/login")
    fun login(
            @Query("api_key") apiKey: String,
            @Body body: LoginRequest
    ): Single<JSONObject>

    @POST("account/prelogin")
    fun preLogin(
            @Query("api_key") apiKey: String,
            @Body body: LoginRequest
    ): Single<LoginResponse>

}
