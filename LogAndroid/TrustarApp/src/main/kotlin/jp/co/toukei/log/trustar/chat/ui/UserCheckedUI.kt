package jp.co.toukei.log.trustar.chat.ui

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import jp.co.toukei.log.lib.UI
import jp.co.toukei.log.lib.common.below
import jp.co.toukei.log.lib.common.bottomToBottomParent
import jp.co.toukei.log.lib.common.circleImageView
import jp.co.toukei.log.lib.common.constraintLayout
import jp.co.toukei.log.lib.common.defaultLParams
import jp.co.toukei.log.lib.common.horizontalCenterInParent
import jp.co.toukei.log.lib.common.showOrGone
import jp.co.toukei.log.lib.common.topToTopParent
import jp.co.toukei.log.lib.gradientDrawable
import jp.co.toukei.log.lib.util.ValueBind
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.chat.vm.UserSelectionVM
import jp.co.toukei.log.trustar.withGlide
import splitties.dimensions.dip
import splitties.views.dsl.core.add
import splitties.views.dsl.core.imageView
import splitties.views.dsl.core.textView
import splitties.views.dsl.core.wrapContent

class UserCheckedUI(context: Context) : ValueBind<UserSelectionVM.UserCheck<*>>(), UI {

    private val avatarImageView: ImageView
    private val nameTextView: TextView
    private val closeView: View

    override val view: View = context.constraintLayout {
        avatarImageView = add(circleImageView {
            id = R.id.id1
        }, defaultLParams(dip(48), dip(48)) {
            horizontalCenterInParent()
            topToTopParent()
        })
        closeView = add(imageView {
            setImageResource(R.drawable.round_close_24)
            id = R.id.id2
            background = gradientDrawable(Int.MAX_VALUE, context.getColor(R.color.colorPrimary))
            setColorFilter(Color.WHITE)
        }, defaultLParams(dip(16), dip(16)) {
            topToTop = R.id.id1
            endToEnd = R.id.id1
        })
        nameTextView = add(textView {
            textSize = 12F
        }, defaultLParams(wrapContent, wrapContent) {
            below(R.id.id1)
            horizontalCenterInParent()
            bottomToBottomParent()
        })
    }

    override fun onBind(bound: UserSelectionVM.UserCheck<*>) {
        val user = bound.user
        nameTextView.text = user.userName
        avatarImageView.withGlide()
            .load(user.avatarUri ?: R.drawable.ic_avatar)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .placeholder(R.drawable.ic_avatar)
            .centerCrop()
            .into(avatarImageView)
        closeView.showOrGone(!bound.keepState)
    }
}
