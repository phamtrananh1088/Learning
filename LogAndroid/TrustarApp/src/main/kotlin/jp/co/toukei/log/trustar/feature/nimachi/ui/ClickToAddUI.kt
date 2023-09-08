package jp.co.toukei.log.trustar.feature.nimachi.ui

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import jp.co.toukei.log.lib.UI
import jp.co.toukei.log.lib.common.setLayoutParams
import jp.co.toukei.log.lib.rippleDrawable
import jp.co.toukei.log.lib.util.ValueBind
import jp.co.toukei.log.trustar.R
import splitties.dimensions.dip
import splitties.views.dsl.core.imageView
import splitties.views.padding

class ClickToAddUI(context: Context) : ValueBind<View.OnClickListener>(), UI {

    override val view = context.imageView {
        setLayoutParams(dip(48), dip(48))
        padding = dip(12)
        setImageResource(R.drawable.baseline_add_circle_24)
        setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary))
        background = rippleDrawable(Int.MAX_VALUE)
    }

    override fun onBind(bound: View.OnClickListener) {
        view.setOnClickListener(bound)
    }
}
