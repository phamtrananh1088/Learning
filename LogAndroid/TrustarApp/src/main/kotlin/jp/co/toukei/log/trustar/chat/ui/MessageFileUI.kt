package jp.co.toukei.log.trustar.chat.ui

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import jp.co.toukei.log.lib.common.Ids
import jp.co.toukei.log.lib.common.above
import jp.co.toukei.log.lib.common.alignTop
import jp.co.toukei.log.lib.common.backgroundColorResId
import jp.co.toukei.log.lib.common.below
import jp.co.toukei.log.lib.common.boldTypeface
import jp.co.toukei.log.lib.common.bottomToBottomParent
import jp.co.toukei.log.lib.common.circleImageView
import jp.co.toukei.log.lib.common.constraintLayout
import jp.co.toukei.log.lib.common.defaultLParams
import jp.co.toukei.log.lib.common.endToEndParent
import jp.co.toukei.log.lib.common.gravityCenterHorizontal
import jp.co.toukei.log.lib.common.horizontalCenterWith
import jp.co.toukei.log.lib.common.matchConstraint
import jp.co.toukei.log.lib.common.startToStartParent
import jp.co.toukei.log.lib.common.topToTopParent
import jp.co.toukei.log.lib.common.verticalCenterInParent
import jp.co.toukei.log.lib.gradientDrawable
import jp.co.toukei.log.lib.util.Progress
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.chat.MessageItem
import jp.co.toukei.log.trustar.db.chat.AttachmentExt
import jp.co.toukei.log.trustar.getResColor
import jp.co.toukei.log.trustar.humanReadableSize
import splitties.dimensions.dip
import splitties.views.bottomPadding
import splitties.views.dsl.core.add
import splitties.views.dsl.core.imageView
import splitties.views.dsl.core.lParams
import splitties.views.dsl.core.matchParent
import splitties.views.dsl.core.textView
import splitties.views.dsl.core.verticalLayout
import splitties.views.dsl.core.verticalMargin
import splitties.views.dsl.core.view
import splitties.views.dsl.core.wrapContent
import splitties.views.horizontalPadding
import splitties.views.padding
import splitties.views.textColorResource
import splitties.views.textResource

class MessageFileUI(
    context: Context,
    private val rtl: Boolean = false,
) : AbstractMessageUI(context), Dl {

    override val username: TextView
    override val sentTimeTextView: TextView
    override val statusTextView: TextView
    override val avatar: ImageView
    override val avatarAlignment: View
    val content: View
    private val fileIcon: ImageView
    private val fileName: TextView
    private val fileSize: TextView
    private val fileState: TextView

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

        content = add(verticalLayout {
            padding = dip(8)
            id = iContent
            gravityCenterHorizontal()
            background = gradientDrawable(
                dip(12),
                context.getResColor(if (rtl) R.color.primaryLight else R.color.grayBackground)
            )
            val c = if (rtl) Color.WHITE else context.getColor(R.color.textColor)
            fileIcon = add(imageView {
                setImageResource(R.drawable.round_insert_drive_file_24)
                padding = dip(8)
                setColorFilter(c)
            }, lParams(dip(48), dip(48)))
            fileName = add(textView {
                padding = dip(4)
                horizontalPadding = dip(8)
                textSize = 20F
                boldTypeface()
                setTextColor(c)
            }, lParams(matchParent, wrapContent))
            fileSize = add(textView {
                horizontalPadding = dip(8)
                textSize = 14F
                setTextColor(c)
            }, lParams(matchParent, wrapContent))
            add(view(::View) {
                backgroundColorResId = R.color.defaultBackground
            }, lParams(matchParent, dip(1)) {
                verticalMargin = dip(8)
            })
            fileState = add(textView {
                textSize = 16F
                setTextColor(c)
                horizontalPadding = dip(8)
            }, lParams(wrapContent, wrapContent))
        }, defaultLParams(matchConstraint, wrapContent) {
            below(iName)
            bottomToBottomParent()
            verticalBias = 0F
            matchConstraintMaxWidth = wrapContent
            matchConstraintMinWidth = dip(192)
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

    private var boundFileItem: MessageItem.F? = null

    override fun onBind(bound: MessageItem) {
        super.onBind(bound)
        bindContentOnly(bound)
    }

    override fun onBind(value: MessageItem, position: Int, payloads: List<Any>) {
        boundFileItem = value as? MessageItem.F
        super.onBind(value, position, payloads)
    }

    override fun bindContentOnly(bound: MessageItem) {
        val m = boundFileItem ?: return
        val a = m.attachmentExt() ?: return
        fileName.text = a.name
        fileSize.text = a.size?.let { humanReadableSize(it) }
        setDownloadProgress(null)
    }

    override fun boundAttachmentExt(): AttachmentExt? {
        return boundFileItem?.attachmentExt()
    }

    override fun setDownloadProgress(progress: Progress?) {
        if (progress == null) {
            val f = boundAttachmentExt()

            when {
                f == null -> {
                    fileState.text = null
                }

                f.file.exists() -> {
                    fileState.textResource = R.string.preview
                }

                else -> {
                    fileState.textResource = R.string.download
                }
            }
        } else {
            fileState.text = progress.progressString()
        }
    }
}
