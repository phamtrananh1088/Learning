package jp.co.toukei.log.trustar.other

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import jp.co.toukei.log.trustar.R

sealed class Weather(
        @StringRes @JvmField val name: Int,
        @DrawableRes @JvmField val icon: Int,
        @JvmField val value: Int
) {
    object Hare : Weather(R.string.weather_hare, R.drawable.hare_48dp, 1)
    object Kumori : Weather(R.string.weather_kumori, R.drawable.kumori_48dp, 2)
    object Ame : Weather(R.string.weather_ame, R.drawable.ame_48dp, 3)
    object Yuki : Weather(R.string.weather_yuki, R.drawable.yuki_48dp, 4)
    object HareKumori : Weather(R.string.weather_hare_kumori, R.drawable.hare_kumori_48dp, 5)
    object KumoriHare : Weather(R.string.weather_kumori_hare, R.drawable.kumori_hare_48dp, 6)
}
