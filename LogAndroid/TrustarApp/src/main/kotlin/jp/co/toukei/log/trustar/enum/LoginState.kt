package jp.co.toukei.log.trustar.enum

import androidx.compose.runtime.Immutable
import jp.co.toukei.log.trustar.rest.post.LoginRequest
import jp.co.toukei.log.trustar.rest.response.LoginResponse
import jp.co.toukei.log.trustar.user.UserWithToken
import org.json.JSONObject

@Immutable
sealed class LoginState {

    object None : LoginState()

    object Loading : LoginState()

    class PreLogin(
        @JvmField val key: String,
        @JvmField val request: LoginRequest,
        @JvmField val response: LoginResponse,
    ) : LoginState()

    class Err(
        @JvmField val error: Throwable,
    ) : LoginState()

    object Disabled : LoginState()

    class Ok(
        @JvmField val json: JSONObject,
        @JvmField val user: UserWithToken,
    ) : LoginState()
}
