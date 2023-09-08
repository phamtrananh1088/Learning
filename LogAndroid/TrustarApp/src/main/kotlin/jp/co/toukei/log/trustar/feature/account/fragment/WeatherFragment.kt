package jp.co.toukei.log.trustar.feature.account.fragment

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import jp.co.toukei.log.lib.applyView
import jp.co.toukei.log.lib.common.CommonFragment
import jp.co.toukei.log.lib.dayOfMonth
import jp.co.toukei.log.lib.fitCenterInto
import jp.co.toukei.log.lib.month
import jp.co.toukei.log.lib.observeNullable
import jp.co.toukei.log.lib.setContent
import jp.co.toukei.log.lib.spanCountBy
import jp.co.toukei.log.lib.toBindHolder
import jp.co.toukei.log.lib.util.BindHolder
import jp.co.toukei.log.lib.util.FixedAdapter
import jp.co.toukei.log.lib.withBoundValue
import jp.co.toukei.log.trustar.Config
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.bottomSheetListenerForExpand
import jp.co.toukei.log.trustar.feature.account.ui.WeatherFragmentUI
import jp.co.toukei.log.trustar.feature.account.ui.WeatherItemUI
import jp.co.toukei.log.trustar.feature.login.activity.LoginActivity
import jp.co.toukei.log.trustar.getLastWeather
import jp.co.toukei.log.trustar.other.Weather
import jp.co.toukei.log.trustar.setWeather
import jp.co.toukei.log.trustar.withGlide
import splitties.dimensions.dip
import splitties.views.dsl.core.withTheme
import splitties.views.dsl.recyclerview.recyclerView
import splitties.views.textResource
import java.util.Calendar
import java.util.GregorianCalendar

/**
 * 天気設定、天候
 */
class WeatherFragment : CommonFragment<FragmentActivity>() {
    override fun createView(owner: FragmentActivity): View {
        val u = WeatherFragmentUI(owner)

        val jpLocale = Config.locale
        val currentCalendar = GregorianCalendar(jpLocale)

        u.date.text = owner.getString(
            R.string.weather_m_d_ww,
            currentCalendar.month() + 1,
            currentCalendar.dayOfMonth(),
            currentCalendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, jpLocale)
        )

        val liveData = MutableLiveData(owner.getLastWeather() ?: Weather.Hare)

        liveData.observeNullable(viewLifecycleOwner) {
            u.weatherIcon.apply {
                context.withGlide().load(it?.icon).fitCenterInto(this)
            }
        }

        u.button.apply {
            textResource =
                if (owner is LoginActivity) R.string.goto_home_dashboard else R.string.weather_setup_close
            setOnClickListener { v ->
                /*天候・ダッシュボードへ押下時のイベント*/

                liveData.value?.let { v.context.setWeather(it) }

                if (owner is LoginActivity) owner.finishLogin()
                else owner.onBackPressed()
            }
        }

        val onClick = View.OnClickListener {
            /*天候・今日のお天気は？押下時に起動されるイベント*/
            val array = arrayOf(
                Weather.Hare,
                Weather.Kumori,
                Weather.Ame,
                Weather.Yuki,
                Weather.HareKumori,
                Weather.KumoriHare
            )
            BottomSheetDialog(it.context.withTheme(R.style.AppTheme2)).apply {
                setContent {
                    recyclerView {
                        var span = spanCountBy(dip(128))
                        if (span == 4 || span == 5) span = 3
                        layoutManager = GridLayoutManager(context, span)

                        adapter = object : FixedAdapter<Weather>(array) {
                            override fun onCreateViewHolder(
                                parent: ViewGroup,
                                viewType: Int,
                            ): BindHolder<Weather> {
                                return WeatherItemUI(parent.context).applyView { h ->
                                    setOnClickListener { v ->
                                        h.withBoundValue {
                                            liveData.value = this
                                            v.context.setWeather(this)
                                            dismiss()
                                        }
                                    }
                                }.toBindHolder()
                            }
                        }
                    }
                }
                setOnShowListener(bottomSheetListenerForExpand())
                setOnDismissListener { u.expand.animate().rotation(0F) }
            }.show()
            u.expand.animate().rotation(180F)
        }
        u.weatherIcon.setOnClickListener(onClick)
        u.expand.setOnClickListener(onClick)
        return u.coordinatorLayout
    }
}
