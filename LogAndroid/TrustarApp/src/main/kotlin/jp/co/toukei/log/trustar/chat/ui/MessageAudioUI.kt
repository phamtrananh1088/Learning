package jp.co.toukei.log.trustar.chat.ui

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
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
import jp.co.toukei.log.lib.common.gravityCenterVertical
import jp.co.toukei.log.lib.common.horizontalCenterWith
import jp.co.toukei.log.lib.common.matchConstraint
import jp.co.toukei.log.lib.common.startToStartParent
import jp.co.toukei.log.lib.common.topToTopParent
import jp.co.toukei.log.lib.common.verticalCenterInParent
import jp.co.toukei.log.lib.util.Progress
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.chat.MessageItem
import jp.co.toukei.log.trustar.db.chat.AttachmentExt
import splitties.dimensions.dip
import splitties.views.bottomPadding
import splitties.views.dsl.core.add
import splitties.views.dsl.core.horizontalLayout
import splitties.views.dsl.core.imageView
import splitties.views.dsl.core.lParams
import splitties.views.dsl.core.textView
import splitties.views.dsl.core.view
import splitties.views.dsl.core.wrapContent
import splitties.views.horizontalPadding
import splitties.views.padding
import splitties.views.textColorResource
import splitties.views.textResource

class MessageAudioUI(
    context: Context,
    private val rtl: Boolean = false,
) : AbstractMessageUI(context), Dl {

    override val username: TextView
    override val sentTimeTextView: TextView
    override val statusTextView: TextView
    override val avatar: ImageView
    override val avatarAlignment: View
    val content: View

    private val icon: ImageView
    private val text: TextView

    private val rb = context.dip(8).toFloat()
    private val rs = context.dip(4).toFloat()

    private val corner = FloatArray(8) { rb }

    private val gd = GradientDrawable().apply {
        shape = GradientDrawable.RECTANGLE
        setColor(context.getColor(if (rtl) R.color.primaryLight else R.color.grayBackground))
        cornerRadii = corner
    }
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

        content = add(horizontalLayout {
            id = iContent
            gravityCenterVertical()
            background = gd
            val c = if (rtl) Color.WHITE else context.getColor(R.color.textColor)
            icon = add(imageView {
                padding = dip(8)
                setColorFilter(c)
            }, lParams(dip(48), dip(48)))
            text = add(textView {
                padding = dip(8)
                textSize = 16F
                setTextColor(c)
                minEms = 6
            }, lParams(wrapContent, wrapContent))
        }, defaultLParams(matchConstraint, wrapContent) {
            below(iName)
            bottomToBottomParent()
            verticalBias = 0F
            matchConstraintMaxWidth = wrapContent
            constrainedWidth = true
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

    private var boundItem: MessageItem.Audio? = null
    override fun onBind(value: MessageItem, position: Int, payloads: List<Any>) {
        boundItem = value as? MessageItem.Audio
        super.onBind(value, position, payloads)
    }

    override fun onBind(bound: MessageItem) {
        super.onBind(bound)

        val hp = bound.hasPrev
        val hn = bound.hasNext

        (if (hp) rs else rb).let {
            corner[0] = it
            corner[1] = it
            corner[2] = it
            corner[3] = it
        }
        (if (hn) rs else rb).let {
            corner[4] = it
            corner[5] = it
            corner[6] = it
            corner[7] = it
        }
        gd.cornerRadii = corner
        content.requestLayout()
        bindContentOnly(bound)
    }

    override fun bindContentOnly(bound: MessageItem) {
        setDownloadProgress(null)
    }

    override fun boundAttachmentExt(): AttachmentExt? {
        return boundItem?.attachmentExt()
    }

    override fun setDownloadProgress(progress: Progress?) {
        if (progress == null) {
            text.textResource = R.string.audio_file
            val f = boundAttachmentExt()

            when {
                f == null -> {
                    icon.setImageDrawable(null)
                }

                f.file.exists() -> {
                    icon.setImageResource(R.drawable.round_play_circle_outline_24)
                }

                else -> {
                    icon.setImageResource(R.drawable.round_download_24)
                }
            }
        } else {
            text.text = progress.progressString()
        }
    }
}
