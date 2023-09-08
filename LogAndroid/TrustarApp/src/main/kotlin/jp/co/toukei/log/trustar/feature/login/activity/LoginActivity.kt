package jp.co.toukei.log.trustar.feature.login.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import jp.co.toukei.log.lib.lazyViewModel
import jp.co.toukei.log.lib.startActivityClearAndNewTask
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.feature.account.fragment.NoticeListFragment
import jp.co.toukei.log.trustar.feature.account.fragment.WeatherFragment
import jp.co.toukei.log.trustar.feature.account.ui.LoginContainerUI
import jp.co.toukei.log.trustar.feature.home.activity.HomeActivity
import jp.co.toukei.log.trustar.feature.login.fragment.LoginFragment
import jp.co.toukei.log.trustar.feature.login.model.LoginModel
import jp.co.toukei.log.trustar.getLastWeather

/**
 * ログインActivity
 */
class LoginActivity : FragmentActivity() {

    private val vm by lazyViewModel<LoginModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val u = LoginContainerUI(this)
        setContentView(u.root)

        //todo fragment state restore
        if (savedInstanceState == null) {
            showFragment(LoginFragment())
        }
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out)
            .replace(R.id.main_root, fragment, null)
            .commit()
    }

    fun afterLoggedIn(noticeCount: Int) {
        if (noticeCount > 0) {
            showFragment(NoticeListFragment().also { it.vmArg.arg = 1 })
        } else {
            weatherSetup()
        }
    }

    /* 重要なお知らせを確認した */
    fun weatherSetup(skipIfSet: Boolean = false) {
        /*天候・天候が未設定の場合の判定処理*/
        if (skipIfSet && getLastWeather() != null) {
            finishLogin()
        } else {
            showFragment(WeatherFragment())
        }
    }

    /* ダッシュボードへ */
    fun finishLogin() {
        startActivityClearAndNewTask<HomeActivity>()
    }

    override fun onBackPressed() {
        if (vm.loggedInState) finishLogin()
        else super.onBackPressed()
    }
}
