package jp.co.toukei.log.trustar.feature.nimachi.ui

import android.content.Context
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import jp.co.toukei.log.lib.common.*
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.primaryButtonStyle
import splitties.dimensions.dip
import splitties.views.dsl.core.*
import splitties.views.dsl.recyclerview.recyclerView

class RecyclerViewButton(context: Context) {

    val list: RecyclerView
    val button: TextView
    val title: TextView

    @JvmField
    val view = context.constraintLayout {
        title = add(textView {
            id = R.id.id3
            gone()
        }, defaultLParams(matchParent, wrapContent) {
            topToTopParent()
            horizontalCenterInParent()
        })
        list = add(recyclerView {
            id = R.id.id1
            layoutManager = LinearLayoutManager(context)
        }, defaultLParams(matchParent, 0) {
            verticalWeight = 1F
            horizontalCenterInParent()
            below(R.id.id3)
            above(R.id.id2)
            bottomMargin = dip(8)
        })
        button = add(textView {
            id = R.id.id2
            primaryButtonStyle()
        }, defaultLParams(wrapContent, wrapContent) {
            horizontalCenterInParent()
            bottomToBottomParent()
            margin = dip(8)
        })
    }
}
