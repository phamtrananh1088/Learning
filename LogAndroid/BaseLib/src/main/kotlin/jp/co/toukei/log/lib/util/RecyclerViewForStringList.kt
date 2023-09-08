package jp.co.toukei.log.lib.util

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import jp.co.toukei.log.lib.UI
import jp.co.toukei.log.lib.applyView
import jp.co.toukei.log.lib.common.setLayoutParams
import jp.co.toukei.log.lib.rippleDrawable
import jp.co.toukei.log.lib.toBindHolder
import splitties.dimensions.dip
import splitties.views.dsl.core.matchParent
import splitties.views.dsl.core.textView
import splitties.views.dsl.core.wrapContent
import splitties.views.dsl.recyclerview.recyclerView
import splitties.views.padding

abstract class RecyclerViewForStringList<T : Any>(
    context: Context,
    ls: List<T>
) : UI {
    constructor(context: Context, array: Array<out T>) : this(context, array.asList())

    inner class Item(context: Context) : ValueBind<T>(), UI {

        override val view = context.textView()
        override fun onBind(bound: T) {
            view.text = getItemText(bound)
        }
    }

    abstract fun getItemText(item: T): CharSequence

    override val view: RecyclerView = context.recyclerView {
        layoutManager = LinearLayoutManager(context)
        adapter = object : FixedAdapter<T>(ls) {
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): BindHolder<T> {
                return Item(parent.context).applyView {
                    setLayoutParams(matchParent, wrapContent)
                    applyView(it)
                }.toBindHolder()
            }
        }
    }

    open fun applyView(item: Item) {
        item.view.apply {
            padding = dip(16)
            textSize = 18F
            background = rippleDrawable(0)
        }
    }
}
