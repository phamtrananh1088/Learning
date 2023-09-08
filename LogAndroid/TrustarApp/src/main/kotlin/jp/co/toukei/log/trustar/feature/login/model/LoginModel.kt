package jp.co.toukei.log.trustar.feature.login.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.distinctUntilChanged
import io.reactivex.rxjava3.core.Single
import jp.co.toukei.log.lib.maybe
import jp.co.toukei.log.lib.md5
import jp.co.toukei.log.lib.observeOnUI
import jp.co.toukei.log.lib.subscribe
import jp.co.toukei.log.lib.subscribeOnIO
import jp.co.toukei.log.lib.toResultWithLoading
import jp.co.toukei.log.trustar.Config
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.common.CommonViewModel
import jp.co.toukei.log.trustar.repo.NoticeRepo
import jp.co.toukei.log.trustar.rest.post.LoginRequest
import jp.co.toukei.log.trustar.rest.response.LoginResponse
import jp.co.toukei.log.trustar.user.ClientInfo
import jp.co.toukei.log.trustar.user.UserWithToken
import org.json.JSONObject
import third.Result


class LoginModel : CommonViewModel() {

    private val _noticeUnread = MutableLiveData<Int>()

    @JvmField
    val noticeUnreadImportant: LiveData<Int> = _noticeUnread
    var loggedInState: Boolean = false
        private set

    fun setLoggedIn() {
        loggedInState = true
        Current.syncNotice {
            val c = NoticeRepo(Current.userDatabase).countUnreadImportant()
            _noticeUnread.postValue(c)
        }
    }

    private val login: MutableLiveData<Result<Pair<JSONObject, UserWithToken>>?> = MutableLiveData()
    val loginState: LiveData<Result<Pair<JSONObject, UserWithToken>>?> = login

    private val preLoginLiveData = MutableLiveData<Result<PreLogin>?>()

    @JvmField
    val preLoginState: LiveData<Result<PreLogin>?> = preLoginLiveData.distinctUntilChanged()

    class PreLogin(
        @JvmField val key: String,
        @JvmField val request: LoginRequest,
        @JvmField val response: LoginResponse,
    )

    /**
     * "19890111"
     */
    private fun loginKey(): String {
        return byteArrayOf(57, 56, 57, 48).copyInto(ByteArray(8) { 49 }, 1).md5()
    }

    fun preLogin(companyId: String, userId: String, password: String) {
        clearDisposable()
        Single
            .defer {
                val key = loginKey()
                val request = LoginRequest(userId, companyId, password, ClientInfo())
                Config.userApi
                    .preLogin(key, request)
                    .map { PreLogin(key, request, it) }
            }
            .toResultWithLoading(200)
            .subscribeOnIO()
            .observeOnUI()
            .subscribe(preLoginLiveData)
            .disposeOnClear()
    }

    fun preLoginClear() {
        preLoginLiveData.value = null
    }

    fun loginClear() {
        login.value = null
    }

    fun login(preLogin: PreLogin) {
        clearDisposable()
        preLoginClear()
        Config.userApi.login(preLogin.key, preLogin.request)
            .flatMap { json ->
                LoginResponse.fromJSONObject(json.toString())
                    ?.toUserWithToken()
                    .maybe()
                    .switchIfEmpty(Single.error { IllegalStateException() })
                    .map { json to it }
            }
            .toResultWithLoading(0)
            .subscribeOnIO()
            .observeOnUI()
            .subscribe(login)
            .disposeOnClear()
    }
}
