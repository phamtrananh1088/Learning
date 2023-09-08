package jp.co.toukei.log.trustar.feature.login.fragment

import android.view.View
import androidx.core.widget.doAfterTextChanged
import jp.co.toukei.log.lib.common.CommonFragment
import jp.co.toukei.log.lib.common.materialAlertDialogBuilder
import jp.co.toukei.log.lib.common.snackbar
import jp.co.toukei.log.lib.getViewModel
import jp.co.toukei.log.lib.hideIME
import jp.co.toukei.log.lib.isLoading
import jp.co.toukei.log.lib.runOnError
import jp.co.toukei.log.lib.runOnValue
import jp.co.toukei.log.trustar.Config
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.errMessage
import jp.co.toukei.log.trustar.feature.login.activity.LoginActivity
import jp.co.toukei.log.trustar.feature.login.model.LoginModel
import jp.co.toukei.log.trustar.feature.login.ui.LoginFragmentUI
import jp.co.toukei.log.trustar.requestFocusAndShowKeyboard
import jp.co.toukei.log.trustar.rest.response.LoginException
import jp.co.toukei.log.trustar.snackbarMessage

/**
 * ログイン
 */
class LoginFragment : CommonFragment<LoginActivity>() {
    override fun createView(owner: LoginActivity): View {
        val loginModel = owner.getViewModel<LoginModel>()
        val u = LoginFragmentUI(owner)
        u.login.setOnFocusChangeListener { v, hasFocus -> if (hasFocus) v.hideIME() }
        u.login.setOnClickListener {
            it.hideIME()
            /*ログイン・ログインボタン押下時のイベント*/
            val context = it.context
            val cid = u.companyId.text?.toString()
            val uid = u.userId.text?.toString()
            val pwd = u.password.text?.toString()

            when {
                cid.isNullOrBlank() -> {
                    u.companyId.requestFocusAndShowKeyboard()
                    u.companyIdParent.error =
                        context.getString(R.string.s1_field_required, u.companyIdParent.hint)
                }

                uid.isNullOrBlank() -> {
                    u.userId.requestFocusAndShowKeyboard()
                    u.userIdParent.error =
                        context.getString(R.string.s1_field_required, u.userIdParent.hint)
                }

                pwd.isNullOrBlank() -> {
                    u.password.requestFocusAndShowKeyboard()
                    u.passwordParent.error =
                        context.getString(R.string.s1_field_required, u.passwordParent.hint)
                }

                else -> loginModel.preLogin(cid, uid, pwd)
            }
        }
        u.companyId.apply {
            setText(Config.pref.lastUserCompany)
            doAfterTextChanged {
                u.companyIdParent.error = null
            }
        }
        u.userId.doAfterTextChanged {
            u.userIdParent.error = null
        }
        u.password.doAfterTextChanged {
            u.passwordParent.error = null
        }
        loginModel.noticeUnreadImportant.observeNonNull {
            owner.afterLoggedIn(it)
        }

        loginModel.preLoginState.observeNullable {
            /*ログイン・ログインのデータチェックのAPI起動箇所*/
            if (it == null) {
                u.setIsLoading(false)
            } else {
                u.setIsLoading(it.isLoading())
                it.runOnError { err ->
                    loginModel.preLoginClear()
                    u.setIsLoading(false)
                    err.snackbarMessage(u.coordinatorLayout)
                }
                it.runOnValue { p ->
                    u.setIsLoading(true)
                    if (p.response.loginResult.isLoggedIn) {
                        u.context.materialAlertDialogBuilder {
                            setMessage(R.string.login_sso_alert_msg)
                            setCancelable(false)
                            setNegativeButton(R.string.no) { d, _ ->
                                d.cancel()
                            }
                            setOnCancelListener {
                                loginModel.preLoginClear()
                            }
                            setPositiveButton(R.string.yes) { _, _ ->
                                loginModel.login(p)
                            }
                        }.show()
                    } else {
                        loginModel.login(p)
                    }
                }
            }
        }
        loginModel.loginState.observeNullable {
            if (it == null) {
                u.setIsLoading(false)
            } else {
                u.setIsLoading(it.isLoading())
                it.runOnError { err ->
                    loginModel.loginClear()
                    if (err is LoginException) {
                        loginErr(u.coordinatorLayout, err)
                    } else {
                        u.coordinatorLayout.snackbar(err.errMessage())
                    }
                }
                it.runOnValue { (json, user) ->
                    if (user.userInfo.appAuthority == 0) {
                        u.context.materialAlertDialogBuilder {
                            setMessage(R.string.login_disabled)
                            setPositiveButton(android.R.string.ok) { _, _ -> loginModel.loginClear() }
                        }.show()
                        loginModel.loginClear()
                    } else if (!owner.isFinishing) {
                        u.setIsLoading(true)
                        /*ログイン・ログイン情報の保存処理*/
                        Current.login(u.context, user, json)
                        loginModel.setLoggedIn()
                    }
                }
            }
        }
        return u.coordinatorLayout
    }

    private fun loginErr(view: View, err: LoginException) {
        val alert401: Pair<Int?, Int>? = when (err.messageCode) {
            "401.1", "401.2" -> R.string.login_auth_err to R.string.login_auth_wrong_msg
            "401.3" -> null to R.string.login_auth_password_expired_msg
            "401.5" -> R.string.login_auth_err to R.string.login_auth_invalid_account_msg
            else -> null
        }
        if (alert401 != null) {
            view.context.materialAlertDialogBuilder {
                alert401.first?.let {
                    setTitle(it)
                }
                setMessage(alert401.second)
                setPositiveButton(android.R.string.ok, null)
            }.show()
        } else {
            view.snackbar(err.messageCode)
        }
    }
}
