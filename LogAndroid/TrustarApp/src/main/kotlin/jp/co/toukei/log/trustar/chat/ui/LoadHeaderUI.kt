package jp.co.toukei.log.trustar.chat.ui

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.paging.LoadState
import jp.co.toukei.log.lib.UI
import jp.co.toukei.log.lib.common.gone
import jp.co.toukei.log.lib.common.progressBar
import jp.co.toukei.log.lib.util.ValueBind
import jp.co.toukei.log.trustar.R
import splitties.dimensions.dip
import splitties.views.centerText
import splitties.views.dsl.core.add
import splitties.views.dsl.core.frameLayout
import splitties.views.dsl.core.lParams
import splitties.views.dsl.core.textView
import splitties.views.dsl.core.wrapContent
import splitties.views.gravityCenter
import splitties.views.padding
import splitties.views.textResource

class LoadHeaderUI(context: Context) : UI, ValueBind<LoadState>() {

    private val progress: View
    val text: TextView

    override val view = context.frameLayout {
        padding = dip(16)
        progress = add(progressBar {
            isIndeterminate = true
            gone()
        }, lParams(wrapContent, wrapContent, gravity = gravityCenter))
        text = add(textView {
            textResource = R.string.chat_retry
            gone()
            padding = dip(16)
            centerText()
        }, lParams(wrapContent, wrapContent, gravity = gravityCenter))
    }

    override fun onBind(bound: LoadState) {
        when (bound) {
            is LoadState.NotLoading -> {
                progress.visibility = View.GONE
                text.visibility = View.GONE
            }

            is LoadState.Loading -> {
                progress.visibility = View.VISIBLE
                text.visibility = View.GONE
            }

            is LoadState.Error -> {
                progress.visibility = View.GONE
                text.visibility = View.VISIBLE
            }
        }
    }
}
