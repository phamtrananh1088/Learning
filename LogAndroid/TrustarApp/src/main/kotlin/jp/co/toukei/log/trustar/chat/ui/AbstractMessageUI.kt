package jp.co.toukei.log.trustar.chat.ui

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import jp.co.toukei.log.lib.UI
import jp.co.toukei.log.lib.common.gone
import jp.co.toukei.log.lib.common.show
import jp.co.toukei.log.lib.common.showOrGone
import jp.co.toukei.log.lib.util.ValueBind
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.chat.MessageItem
import jp.co.toukei.log.trustar.withGlide

abstract class AbstractMessageUI(context: Context) : ValueBind<MessageItem>(), UI {

    abstract val username: TextView
    abstract val sentTimeTextView: TextView
    abstract val statusTextView: TextView
    abstract val avatarAlignment: View
    abstract val avatar: ImageView

    protected val glide = context.withGlide()

    override fun onBind(bound: MessageItem) {
        val self = bound.self
        sentTimeTextView.text = bound.dateStr
        statusTextView.text = bound.status

        val gone = self || bound.hasPrev
        if (bound is MessageItem.Sent && !gone) {
            username.show()
            username.text = bound.username
            glide.load(bound.avatar ?: R.drawable.ic_avatar)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.ic_avatar)
                .centerCrop()
                .into(avatar)
        } else {
            username.gone()
            username.text = null
            glide.clear(avatar)
            avatar.setImageDrawable(null)
        }
        avatar.showOrGone(!gone)
        avatarAlignment.showOrGone(!self)
    }

    protected abstract fun bindContentOnly(bound: MessageItem)

    override fun onBind(value: MessageItem, position: Int, payloads: List<Any>) {
        val p = payloads.mapNotNull { it as? Int }
        if (p.isEmpty()) {
            super.onBind(value, position, payloads)
        } else {
            p.forEach {
                if (MessageItem.hasPayload(MessageItem.PAYLOAD_CONTENT, it)) {
                    bindContentOnly(value)
                }
                if (MessageItem.hasPayload(MessageItem.PAYLOAD_STATUS, it)) {
                    statusTextView.text = value.status
                }
                if (MessageItem.hasPayload(MessageItem.PAYLOAD_DATE, it)) {
                    sentTimeTextView.text = value.dateStr
                }
            }
        }
        return
    }
}
