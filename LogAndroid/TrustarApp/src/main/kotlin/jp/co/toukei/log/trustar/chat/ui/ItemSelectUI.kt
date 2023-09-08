package jp.co.toukei.log.trustar.chat.ui

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.widget.ImageView
import androidx.recyclerview.selection.SelectionTracker
import jp.co.toukei.log.lib.UI
import jp.co.toukei.log.lib.common.backgroundColorResId
import jp.co.toukei.log.lib.common.constraintLayout
import jp.co.toukei.log.lib.common.defaultLParams
import jp.co.toukei.log.lib.common.endToEndParent
import jp.co.toukei.log.lib.common.gone
import jp.co.toukei.log.lib.common.matchConstraint
import jp.co.toukei.log.lib.common.setLayoutParams
import jp.co.toukei.log.lib.common.show
import jp.co.toukei.log.lib.common.topToTopParent
import jp.co.toukei.log.lib.gradientDrawable
import jp.co.toukei.log.lib.util.ValueBind
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.withGlide
import splitties.dimensions.dip
import splitties.views.dsl.core.add
import splitties.views.dsl.core.imageView
import splitties.views.dsl.core.margin
import splitties.views.dsl.core.matchParent
import splitties.views.padding

class ItemSelectUI(context: Context) : ValueBind<ItemSelectUI.Item>(), UI {

    val img: ImageView
    private val checkView: ImageView

    override val view = context.constraintLayout {
        backgroundColorResId = R.color.grayBackground
        img = add(imageView {
            scaleType = ImageView.ScaleType.CENTER_CROP
        }, defaultLParams(matchParent, matchConstraint) {
            dimensionRatio = "1"
        })
        checkView = add(imageView {
            padding = dip(2)
            setColorFilter(Color.WHITE)
            id = R.id.id2
        }, defaultLParams(dip(24), dip(24)) {
            endToEndParent()
            topToTopParent()
            margin = dip(4)
        })
        setLayoutParams()
    }

    override fun onBind(bound: Item) {
        img.withGlide()
            .load(bound.uri)
            .placeholder(R.drawable.image_placeholder)
            .centerCrop()
            .into(img)
        mode(bound.check, 0)
    }

    private fun mode(check: Boolean?, duration: Long) {
        with(if (check == true) 0.8F else 1F) {
            img.animate()
                .scaleX(this)
                .scaleY(this)
                .duration = duration
        }
        checkView.apply {
            when (check) {
                null -> {
                    gone()
                    background = null
                    setImageDrawable(null)
                }

                true -> {
                    show()
                    background =
                        gradientDrawable(Int.MAX_VALUE, context.getColor(R.color.colorPrimary))
                    setImageResource(R.drawable.round_done_24)
                }

                false -> {
                    show()
                    background = GradientDrawable().apply {
                        shape = GradientDrawable.OVAL
                        cornerRadius = Int.MAX_VALUE.toFloat()
                        setColor(Color.TRANSPARENT)
                        setStroke(dip(3), 0x99999999.toInt())
                    }
                    setImageDrawable(null)
                }
            }
        }
    }

    override fun onBind(value: Item, position: Int, payloads: List<Any>) {
        if (payloads.contains(SelectionTracker.SELECTION_CHANGED_MARKER)) mode(value.check, 100)
        else super.onBind(value, position, payloads)
    }

    class Item(@JvmField val uri: Uri) {
        @JvmField
        var check: Boolean? = null
    }
}
