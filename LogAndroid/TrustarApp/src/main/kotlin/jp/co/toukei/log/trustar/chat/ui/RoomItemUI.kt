package jp.co.toukei.log.trustar.chat.ui

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.Barrier
import com.bumptech.glide.load.engine.DiskCacheStrategy
import jp.co.toukei.log.lib.UI
import jp.co.toukei.log.lib.common.Ids
import jp.co.toukei.log.lib.common.barrier
import jp.co.toukei.log.lib.common.below
import jp.co.toukei.log.lib.common.bottomToBottomParent
import jp.co.toukei.log.lib.common.circleImageView
import jp.co.toukei.log.lib.common.constraintLayout
import jp.co.toukei.log.lib.common.defaultLParams
import jp.co.toukei.log.lib.common.endToEndParent
import jp.co.toukei.log.lib.common.showOrGone
import jp.co.toukei.log.lib.common.singleLine
import jp.co.toukei.log.lib.common.startToStartParent
import jp.co.toukei.log.lib.common.topToTopParent
import jp.co.toukei.log.lib.common.verticalCenterInParent
import jp.co.toukei.log.lib.common.whiteText
import jp.co.toukei.log.lib.gradientDrawable
import jp.co.toukei.log.lib.util.ValueBind
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.db.chat.entity.ChatRoomExt
import jp.co.toukei.log.trustar.listItemDrawable
import jp.co.toukei.log.trustar.withGlide
import splitties.dimensions.dip
import splitties.views.centerText
import splitties.views.dsl.core.add
import splitties.views.dsl.core.horizontalMargin
import splitties.views.dsl.core.textView
import splitties.views.dsl.core.wrapContent
import splitties.views.horizontalPadding
import splitties.views.padding

class RoomItemUI(context: Context) : ValueBind<ChatRoomExt>(), UI {

    private val avatarImageView: ImageView
    private val contentTextView: TextView
    private val timeTextView: TextView
    private val unreadTextView: TextView

    override val view = context.constraintLayout {
        background = context.listItemDrawable()
        padding = dip(16)

        val (iAvatar, iTime, iUnread, iBarrier, iContent) = Ids

        avatarImageView = add(circleImageView {
            id = iAvatar
        }, defaultLParams(dip(54), dip(54)) {
            verticalCenterInParent()
            startToStartParent()
        })

        timeTextView = add(textView {
            id = iTime
            textSize = 14F
        }, defaultLParams(0, wrapContent) {
            topToTopParent()
            endToEndParent()
        })

        unreadTextView = add(textView {
            id = iUnread
            includeFontPadding = false
            centerText()
            whiteText()
            singleLine()
            horizontalPadding = dip(8)
            background = gradientDrawable(Int.MAX_VALUE, context.getColor(R.color.colorPrimary))
        }, defaultLParams(wrapContent, dip(24)) {
            below(iTime)
            bottomToBottomParent()
            endToEndParent()
            verticalBias = 1F
            matchConstraintMinWidth = dip(24)
            constrainedWidth = true
        })
        add(barrier {
            id = iBarrier
            referencedIds = intArrayOf(iTime, iUnread)
            type = Barrier.START
        }, defaultLParams())
        contentTextView = add(textView {
            id = iContent
            textSize = 20F
        }, defaultLParams(0, wrapContent) {
            verticalCenterInParent()
            startToEnd = iAvatar
            endToStart = iBarrier
            horizontalMargin = dip(16)
        })
    }

    override fun onBind(bound: ChatRoomExt) {
        avatarImageView.withGlide()
            .load(bound.imageUri ?: R.drawable.ic_avatar)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .placeholder(R.drawable.ic_avatar)
            .centerCrop()
            .into(avatarImageView)

        contentTextView.text = bound.title
        timeTextView.text = bound.run {
            if (sameDayToLastUpdated(System.currentTimeMillis())) lastUpdateHHmm else lastUpdateMMdd
        }
        unreadTextView.apply {
            val count = bound.room.unread
            showOrGone(count > 0)
            text = if (count > 99) "99+" else count.toString()
        }
    }
}
