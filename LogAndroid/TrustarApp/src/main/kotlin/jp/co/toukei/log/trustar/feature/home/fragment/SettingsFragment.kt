package jp.co.toukei.log.trustar.feature.home.fragment

import android.view.View
import jp.co.toukei.log.lib.common.CommonFragment
import jp.co.toukei.log.lib.textViewMakeDialOnClick
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.deprecated.startActivity
import jp.co.toukei.log.trustar.feature.account.fragment.WeatherFragment
import jp.co.toukei.log.trustar.feature.home.activity.HomeActivity
import jp.co.toukei.log.trustar.feature.home.ui.SettingsFragmentUI
import jp.co.toukei.log.trustar.feature.login.activity.LoginActivity
import jp.co.toukei.log.trustar.openFragmentNoAnimate

/**
 * 設定
 */
class SettingsFragment : CommonFragment<HomeActivity>() {
    override fun createView(owner: HomeActivity): View {
        val u = SettingsFragmentUI(owner)

        setupToolbarNavigation(u.toolbar)

        u.switchUser.setOnClickListener {
            /*設定・ユーザー切替押下時に起動されるイベント*/
            it.context.startActivity<LoginActivity>()
        }
        u.tel.apply {
            val t = Current.user?.userInfo?.branchTel
            text = t
            if (!t.isNullOrEmpty()) {
                /*設定・電話ボタン押下時に起動されるイベント*/
                setOnClickListener(textViewMakeDialOnClick())
            }
        }
        u.weatherSetup.setOnClickListener {
            /*設定・天候ボタン押下時に起動されるイベント*/
            WeatherFragment().openFragmentNoAnimate(requireActivity(), R.id.main_root)
        }

        return u.coordinatorLayout
    }
}
