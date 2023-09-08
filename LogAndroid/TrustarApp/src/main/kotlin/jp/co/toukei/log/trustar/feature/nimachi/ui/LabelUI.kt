package jp.co.toukei.log.trustar.feature.nimachi.ui

import android.content.Context
import android.view.ViewGroup
import jp.co.toukei.log.lib.UI
import jp.co.toukei.log.lib.common.setLayoutParams
import jp.co.toukei.log.lib.toBindHolder
import jp.co.toukei.log.lib.util.BindHolder
import jp.co.toukei.log.lib.util.SugoiAdapter
import jp.co.toukei.log.lib.util.ValueBind
import jp.co.toukei.log.trustar.primaryLabel
import splitties.views.dsl.core.textView
import splitties.views.textResource

class LabelUI(context: Context) : ValueBind<Int>(), UI {

    override val view = context.textView {
        setLayoutParams()
        primaryLabel()
    }

    override fun onBind(bound: Int) {
        view.textResource = bound
    }

    class Creator : SugoiAdapter.Creator<Int> {
        override fun onCreateViewHolder(context: Context, parent: ViewGroup): BindHolder<Int> {
            return LabelUI(context).toBindHolder()
        }
    }
}
