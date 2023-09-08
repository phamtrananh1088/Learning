package jp.co.toukei.log.trustar.feature.account.ui

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import jp.co.toukei.log.lib.UI
import jp.co.toukei.log.lib.common.gravityCenterHorizontal
import jp.co.toukei.log.lib.rippleDrawable
import jp.co.toukei.log.lib.util.ValueBind
import jp.co.toukei.log.trustar.other.Weather
import splitties.dimensions.dip
import splitties.views.dsl.core.*
import splitties.views.padding
import splitties.views.textResource

class WeatherItemUI(context: Context) : ValueBind<Weather>(), UI {

    private val icon: ImageView
    private val name: TextView

    override val view = context.verticalLayout {
        padding = dip(8)

        background = rippleDrawable(0)

        gravityCenterHorizontal()

        icon = add(imageView {
            padding = dip(12)
        }, lParams(dip(72), dip(72)))

        name = add(textView {
            padding = dip(8)
        }, lParams(wrapContent, wrapContent))
    }

    override fun onBind(bound: Weather) {
        icon.setImageResource(bound.icon)
        name.textResource = bound.name
    }
}
