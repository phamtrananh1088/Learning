package jp.co.toukei.log.trustar.chat.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
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
import jp.co.toukei.log.lib.common.gone
import jp.co.toukei.log.lib.common.horizontalCenterWith
import jp.co.toukei.log.lib.common.matchConstraint
import jp.co.toukei.log.lib.common.roundedImageView
import jp.co.toukei.log.lib.common.show
import jp.co.toukei.log.lib.common.startToStartParent
import jp.co.toukei.log.lib.common.topToTopParent
import jp.co.toukei.log.lib.common.verticalCenterInParent
import jp.co.toukei.log.lib.common.verticalTo
import jp.co.toukei.log.lib.util.Progress
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.chat.MessageItem
import jp.co.toukei.log.trustar.db.chat.AttachmentExt
import jp.co.toukei.log.trustar.getResColor
import splitties.dimensions.dip
import splitties.views.bottomPadding
import splitties.views.dsl.core.add
import splitties.views.dsl.core.imageView
import splitties.views.dsl.core.textView
import splitties.views.dsl.core.view
import splitties.views.dsl.core.wrapContent
import splitties.views.horizontalPadding
import splitties.views.padding
import splitties.views.textColorResource
import splitties.views.textResource

class MessageVideoUI(context: Context, rtl: Boolean = false) : AbstractMessageUI(context), Dl {

    override val username: TextView
    override val sentTimeTextView: TextView
    override val statusTextView: TextView
    override val avatar: ImageView
    override val avatarAlignment: View
    val imageView: ImageView

    private val playIcon: View
    private val topText: TextView

    override val view: View = context.constraintLayout {
        val (iAvatar, iImg, iTime, iStatus, iAlign, iName) = Ids
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
            id = iImg
//            val r = dip(4).toFloat()
//            setCornerRadiiDP(r, r, r, r)
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
            matchConstraintMinHeight = dip(54)
        })

        playIcon = add(imageView {
            setImageResource(R.drawable.round_play_circle_outline_24)
            setColorFilter(context.getResColor(R.color.lightText))
        }, defaultLParams(dip(48), dip(48)) {
            horizontalCenterWith(iImg)
            verticalTo(iImg, 0.5F)
        })
        topText = add(textView {
            textResource = R.string.tap_to_download
            padding = dip(16)
            textColorResource = R.color.lightText
        }, defaultLParams(matchConstraint, wrapContent) {
            startToStart = iImg
            endToEnd = iImg
            horizontalBias = 0F
            verticalTo(iImg, 0F)
        })
        sentTimeTextView = add(textView {
            id = iTime
            padding = dip(2)
            textSize = 12F
        }, defaultLParams(wrapContent, wrapContent) {
            bottomToBottomParent()
            verticalBias = 1F
            bottomToBottom = iImg
            constrainedWidth = true
            matchConstraintMinWidth = wrapContent
            if (rtl) {
                endToStart = iImg
                horizontalBias = 1F
                startToStartParent()
                marginEnd = dip(8)
            } else {
                startToEnd = iImg
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
        setDownloadProgress(null)
    }

    private var boundVideoItem: MessageItem.Video? = null

    override fun onBind(value: MessageItem, position: Int, payloads: List<Any>) {
        boundVideoItem = value as? MessageItem.Video
        super.onBind(value, position, payloads)
    }

    override fun boundAttachmentExt(): AttachmentExt? {
        return boundVideoItem?.attachmentExt()
    }

    override fun setDownloadProgress(progress: Progress?) {
        if (progress == null) {
            topText.textResource = R.string.tap_to_download
        } else {
            topText.text = progress.progressString()
        }
        topText.gone()
        playIcon.gone()
        imageView.apply {
            setImageDrawable(null)
            glide.clear(this)

            val max = dip(192)
            val f = boundAttachmentExt()
            val lp = layoutParams as ConstraintLayout.LayoutParams
            when {
                f == null -> {
                    lp.apply {
                        height = wrapContent
                        width = max
                    }
                }

                f.file.exists() -> {
                    playIcon.show()
                    glide.asBitmap()
                        .load(f.file)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .dontAnimate()
                        .override(max, max)
                        .listener(object : RequestListener<Bitmap> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Bitmap>?,
                                isFirstResource: Boolean,
                            ): Boolean {
                                tag = null
                                return false
                            }

                            override fun onResourceReady(
                                resource: Bitmap?,
                                model: Any?,
                                target: Target<Bitmap>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean,
                            ): Boolean {
                                lp.apply {
                                    val r = resource?.let {
                                        it.width.toFloat() / it.height.toFloat()
                                    } ?: 1F
                                    dimensionRatio = if (r > 0) "$r" else "1F"

                                    if (r >= 1) {
                                        width = max
                                        height = 0
                                    } else {
                                        width = 0
                                        height = max
                                    }
                                }
                                return false
                            }

                        })
                        .fitCenter()
                        .into(this)
                }

                else -> {
                    topText.show()
                    playIcon.show()
                    lp.apply {
                        height = 0
                        dimensionRatio = "1F"
                        width = max
                    }
                    setImageDrawable(ColorDrawable(context.getResColor(R.color.textColor)))
                }
            }
        }
    }
}
