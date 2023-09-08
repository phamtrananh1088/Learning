package jp.co.toukei.log.trustar.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.internal.disposables.ListCompositeDisposable
import jp.co.toukei.log.lib.addTo
import jp.co.toukei.log.lib.maybe
import jp.co.toukei.log.lib.md5
import jp.co.toukei.log.lib.observeOnUI
import jp.co.toukei.log.lib.subscribeOnIO
import jp.co.toukei.log.trustar.Config
import jp.co.toukei.log.trustar.enum.LoginState
import jp.co.toukei.log.trustar.rest.api.UserApi
import jp.co.toukei.log.trustar.rest.post.LoginRequest
import jp.co.toukei.log.trustar.rest.response.LoginResponse
import jp.co.toukei.log.trustar.user.ClientInfo
import retrofit2.create
import java.util.concurrent.TimeUnit

class LoginVM(
    defaultCompanyCd: String?,
) : ViewModel() {

    private val userApi: UserApi = Config.baseRetrofit.create()

    private val disposable = ListCompositeDisposable()

    /**
     * "19890111"
     */
    private fun loginKey(): String {
        return byteArrayOf(57, 56, 57, 48).copyInto(ByteArray(8) { 49 }, 1).md5()
    }

    val inputCompanyCd = mutableStateOf(defaultCompanyCd.orEmpty())
    val inputUser = mutableStateOf("")

    val loginState: MutableState<LoginState> = mutableStateOf(LoginState.None)

    fun preLogin(companyId: String, userId: String, password: String) {
        reset()
        Single
            .defer<LoginState> {
                val key = loginKey()
                val request = LoginRequest(userId, companyId, password, ClientInfo())
                userApi
                    .preLogin(key, request)
                    .map { LoginState.PreLogin(key, request, it) }
            }
            .subscribe(0)
    }

    fun login(preLogin: LoginState.PreLogin) {
        reset()
        userApi.login(preLogin.key, preLogin.request)
            .flatMap { json ->
                LoginResponse.fromJSONObject(json.toString())
                    ?.toUserWithToken()
                    .maybe()
                    .switchIfEmpty(Single.error { IllegalStateException() })
                    .map {
                        if (it.userInfo.appAuthority == 0) LoginState.Disabled
                        else LoginState.Ok(json, it)
                    }
            }
            .subscribe(0)
    }

    private fun Single<LoginState>.subscribe(delay: Long) {
        toFlowable()
            .startWithItem(LoginState.Loading)
            .switchMap {
                val f = Flowable.just(it)
                if (it !is LoginState.Loading) f
                else f.delay(delay, TimeUnit.MILLISECONDS)
            }
            .subscribeOnIO()
            .observeOnUI()
            .subscribe(
                {
                    loginState.value = it
                },
                {
                    loginState.value = LoginState.Err(it)
                }
            )
            .addTo(disposable)
    }

    fun reset() {
        disposable.clear()
        loginState.value = LoginState.None
    }
}
