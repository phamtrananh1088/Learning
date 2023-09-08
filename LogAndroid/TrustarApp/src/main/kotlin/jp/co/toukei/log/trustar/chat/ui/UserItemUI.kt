package jp.co.toukei.log.trustar.chat.ui

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import jp.co.toukei.log.lib.UI
import jp.co.toukei.log.lib.common.circleImageView
import jp.co.toukei.log.lib.common.gravityCenterVertical
import jp.co.toukei.log.lib.common.textColor
import jp.co.toukei.log.lib.util.Differ
import jp.co.toukei.log.lib.util.ValueBind
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.db.chat.entity.ChatUser
import jp.co.toukei.log.trustar.listItemDrawable
import jp.co.toukei.log.trustar.withGlide
import splitties.dimensions.dip
import splitties.views.dsl.core.add
import splitties.views.dsl.core.horizontalLayout
import splitties.views.dsl.core.lParams
import splitties.views.dsl.core.margin
import splitties.views.dsl.core.matchParent
import splitties.views.dsl.core.textView
import splitties.views.dsl.core.wrapContent
import splitties.views.horizontalPadding

class UserItemUI(context: Context) : ValueBind<ChatUser>(), UI {

    val avatarImageView: ImageView
    val nameTextView: TextView

    override val view: View = context.horizontalLayout {
        background = context.listItemDrawable()
        gravityCenterVertical()
        val tColor = context.getColor(R.color.textColor)

        horizontalPadding = dip(16)

        avatarImageView = add(circleImageView {

        }, lParams(dip(48), dip(48)) {
            margin = dip(8)
        })

        nameTextView = add(textView {
            textSize = 16F
            textColor = tColor
            horizontalPadding = dip(4)
        }, lParams(matchParent, wrapContent))
    }

    override fun onBind(bound: ChatUser) {
        nameTextView.text = bound.name
        avatarImageView.withGlide()
            .load(bound.avatarUri ?: R.drawable.ic_avatar)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .placeholder(R.drawable.ic_avatar)
            .centerCrop()
            .into(avatarImageView)
    }

    class Diff : Differ.DiffCallback<ChatUser> {
        override fun areItemsTheSame(
            oldItem: ChatUser,
            newItem: ChatUser,
            oldItemPosition: Int,
            newItemPosition: Int,
        ): Boolean {
            return oldItem.sameItem(newItem)
        }

        override fun areContentsTheSame(
            oldItem: ChatUser,
            newItem: ChatUser,
            oldItemPosition: Int,
            newItemPosition: Int,
        ): Boolean {
            return oldItem.name == newItem.name && oldItem.avatar == newItem.avatar
        }

        override fun getChangePayload(
            oldItem: ChatUser,
            newItem: ChatUser,
            oldItemPosition: Int,
            newItemPosition: Int,
        ): Any? = null
    }
}
