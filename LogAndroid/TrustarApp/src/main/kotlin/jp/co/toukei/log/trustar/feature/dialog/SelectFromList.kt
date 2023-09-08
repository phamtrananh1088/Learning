package jp.co.toukei.log.trustar.feature.dialog

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import jp.co.toukei.log.lib.common.*
import jp.co.toukei.log.lib.gradientDrawableBorder
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.primaryLabel
import splitties.dimensions.dip
import splitties.views.dsl.core.*
import splitties.views.dsl.recyclerview.recyclerView
import splitties.views.horizontalPadding
import splitties.views.padding
import splitties.views.verticalPadding

class SelectFromList(context: Context) {

    val currentLabel: TextView
    val currentSelected: TextView
    val search: EditText
    val listTitle: TextView
    val recyclerView: RecyclerView

    @JvmField
    val view = context.verticalLayout {
        add(horizontalLayout {
            verticalPadding = dip(4)
            horizontalPadding = dip(8)
            gravityCenterVertical()
            currentLabel = add(textView {
                textSize = 14F
            }, lParams(wrapContent, wrapContent))
            currentSelected = add(textView {
                boldTypeface()
                padding = dip(8)
                textSize = 16F
            }, lParams(wrapContent, wrapContent))
        }, lParams())
        add(view(::View) {
            backgroundColorResId = R.color.gray_out
        }, lParams(matchParent, dip(1)))
        search = add(editText {
            padding = dip(8)
            imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI or EditorInfo.IME_ACTION_DONE
            singleLine()
            background = gradientDrawableBorder(
                dip(1),
                Color.TRANSPARENT,
                context.getColor(R.color.gray_out),
                dip(1)
            )
        }, lParams(matchParent, wrapContent) {
            topMargin = dip(4)
            horizontalMargin = dip(8)
        })
        add(view(::View) {
            backgroundColorResId = R.color.gray_out
        }, lParams(matchParent, dip(1)) {
            topMargin = dip(2)
        })
        listTitle = add(textView {
            primaryLabel()
        }, lParams(matchParent, wrapContent) {
            topMargin = dip(2)
        })

        recyclerView = add(recyclerView {
            val lm = LinearLayoutManager(context)
            layoutManager = lm
            addDividerItemDecoration(lm.orientation, Color.TRANSPARENT, dip(1))
        }, lParams(matchParent, matchParent))
    }
}
