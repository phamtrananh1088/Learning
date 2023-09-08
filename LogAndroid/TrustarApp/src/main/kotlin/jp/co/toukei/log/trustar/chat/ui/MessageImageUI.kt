package jp.co.toukei.log.trustar.chat.ui

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import jp.co.toukei.log.lib.common.Ids
import jp.co.toukei.log.lib.common.above
import jp.co.toukei.log.lib.common.alignTop
import jp.co.toukei.log.lib.common.below
import jp.co.toukei.log.lib.common.boldTypeface
import jp.co.toukei.log.lib.common.bottomToBottomParent
import jp.co.toukei.log.lib.common.circleImageView
import jp.co.toukei.log.lib.common.constraintLayout
import jp.co.toukei.log.lib.common.defaultLParams
import jp.co.toukei.log.lib.common.endToEndParent
import jp.co.toukei.log.lib.common.horizontalCenterWith
import jp.co.toukei.log.lib.common.matchConstraint
import jp.co.toukei.log.lib.common.roundedImageView
import jp.co.toukei.log.lib.common.startToStartParent
import jp.co.toukei.log.lib.common.topToTopParent
import jp.co.toukei.log.lib.common.verticalCenterInParent
import jp.co.toukei.log.lib.fitCenterInto
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.chat.MessageItem
import splitties.dimensions.dip
import splitties.views.bottomPadding
import splitties.views.dsl.core.add
import splitties.views.dsl.core.textView
import splitties.views.dsl.core.view
import splitties.views.dsl.core.wrapContent
import splitties.views.horizontalPadding
import splitties.views.padding
import splitties.views.textColorResource

class MessageImageUI(context: Context, rtl: Boolean = false) : AbstractMessageUI(context) {

    override val username: TextView
    override val sentTimeTextView: TextView
    override val statusTextView: TextView
    override val avatar: ImageView
    override val avatarAlignment: View
    val imageView: ImageView

    override val view: View = context.constraintLayout {
        val (iAvatar, iContent, iTime, iStatus, iAlign, iName) = Ids
        val dp54 = dip(54)
        avatarAlignment = add(view(::View) {
            id = iAlign
        }, defaultLParams(dp54, 1) {
            if (rtl) {
                endToEndParent()
            } else {
                startToStartParent()
            }
        })
        avatar = add(circleImageView {
            id = iAvatar
            setImageResource(R.drawable.ic_avatar)
        }, defaultLParams(dp54, dp54) {
            verticalCenterInParent()
            verticalBias = 0F
            horizontalCenterWith(iAlign)
            below(iAlign)
        })

        username = add(textView {
            id = iName
            textSize = 14F
            horizontalPadding = dip(8)
            bottomPadding = dip(4)
            boldTypeface()
            textColorResource = R.color.textColor
        }, defaultLParams(wrapContent, wrapContent) {
            constrainedWidth = true
            alignTop(iAvatar)
            if (rtl) {
                endToStart = iAvatar
                startToStartParent()
                horizontalBias = 1F
            } else {
                startToEnd = iAvatar
                endToEndParent()
                horizontalBias = 0F
            }
        })
        imageView = add(roundedImageView(dip(8).toFloat()) {
            id = iContent
            scaleType = ImageView.ScaleType.CENTER_INSIDE
        }, defaultLParams(matchConstraint, matchConstraint) {
            below(iName)
            bottomToBottomParent()
            verticalBias = 0F
            bottomMargin = dip(4)
            if (rtl) {
                endToStart = iAlign
                marginEnd = dip(4)
                startToStartParent()
                horizontalBias = 1F
            } else {
                startToEnd = iAlign
                marginStart = dip(4)
                endToEndParent()
                horizontalBias = 0F
            }
            constrainedWidth = true
            constrainedHeight = true
        })
        sentTimeTextView = add(textView {
            id = iTime
            padding = dip(2)
            textSize = 12F
        }, defaultLParams(wrapContent, wrapContent) {
            bottomToBottomParent()
            verticalBias = 1F
            bottomToBottom = iContent
            constrainedWidth = true
            matchConstraintMinWidth = wrapContent
            if (rtl) {
                endToStart = iContent
                horizontalBias = 1F
                startToStartParent()
                marginEnd = dip(8)
            } else {
                startToEnd = iContent
                horizontalBias = 0F
                endToEndParent()
                marginStart = dip(8)
            }
        })
        statusTextView = add(textView {
            id = iStatus
            padding = dip(2)
            textSize = 12F
            gravity = if (rtl) Gravity.END else Gravity.START
        }, defaultLParams(wrapContent, wrapContent) {
            above(iTime)
            topToTopParent()
            verticalBias = 1F
            constrainedWidth = true
            matchConstraintMinWidth = wrapContent
            if (rtl) {
                endToEnd = iTime
                horizontalBias = 1F
                startToStartParent()
            } else {
                startToStart = iTime
                horizontalBias = 0F
                endToEndParent()
            }
        })
    }

    override fun onBind(bound: MessageItem) {
        super.onBind(bound)
        bindContentOnly(bound)
    }

    override fun bindContentOnly(bound: MessageItem) {
        if (bound !is MessageItem.Image) return
        imageView.apply {
            val max = dip(192)
            (layoutParams as ConstraintLayout.LayoutParams).apply {
                val r = bound.ratio
                dimensionRatio = if (r > 0) "$r" else "1F"

                if (r >= 1) {
                    width = max
                    height = 0
                } else {
                    width = 0
                    height = max
                }
            }
            val img = bound.imageUri
            if (img == null) {
                glide.clear(this)
            } else if (tag != img.uri) {
                glide.load(img)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .error(R.drawable.round_broken_image_24)
                    .placeholder(R.drawable.round_photo_24)
                    .dontAnimate()
                    .override(max, max)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean,
                        ): Boolean {
                            tag = null
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean,
                        ): Boolean {
                            tag = img.uri
                            return false
                        }

                    })
                    .fitCenterInto(this)
            }
        }
    }
}
