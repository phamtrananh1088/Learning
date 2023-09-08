package jp.co.toukei.log.lib.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MarginItemDecoration(private val marginInPx: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.apply {
            top = marginInPx
            left = marginInPx
            right = marginInPx
            bottom = marginInPx
        }
    }
}
