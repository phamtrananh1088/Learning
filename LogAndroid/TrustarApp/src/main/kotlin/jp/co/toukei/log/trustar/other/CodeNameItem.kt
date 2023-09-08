package jp.co.toukei.log.trustar.other

import android.content.Context
import android.view.View
import android.widget.TextView
import jp.co.toukei.log.lib.UI
import jp.co.toukei.log.lib.common.boldTypeface
import jp.co.toukei.log.lib.common.gravityCenterVertical
import jp.co.toukei.log.lib.rippleDrawable
import jp.co.toukei.log.lib.util.ValueBind
import splitties.dimensions.dip
import splitties.views.dsl.core.*
import splitties.views.padding

abstract class CodeNameItem<T : Any>(context: Context) : ValueBind<T>(), UI {
    protected val code: TextView
    protected val name: TextView

    override val view: View = context.horizontalLayout {
        gravityCenterVertical()
        background = rippleDrawable(0)
        code = add(textView {
            boldTypeface()
            textSize = 16F
            padding = dip(8)
        }, lParams(wrapContent, wrapContent))
        name = add(textView {
            textSize = 16F
            padding = dip(8)
        }, lParams(wrapContent, wrapContent))
    }

}
