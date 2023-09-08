package jp.co.toukei.log.trustar.chat.ui

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatCheckBox
import com.bumptech.glide.load.engine.DiskCacheStrategy
import jp.co.toukei.log.lib.UI
import jp.co.toukei.log.lib.common.appCompatCheckBox
import jp.co.toukei.log.lib.common.circleImageView
import jp.co.toukei.log.lib.common.enableIf
import jp.co.toukei.log.lib.common.gravityCenterVertical
import jp.co.toukei.log.lib.common.textColor
import jp.co.toukei.log.lib.util.Differ
import jp.co.toukei.log.lib.util.ValueBind
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.chat.vm.UserSelectionVM
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

class UserCheckItemUI(context: Context) : ValueBind<UserSelectionVM.UserCheck<*>>(), UI {

    private val avatarImageView: ImageView
    private val nameTextView: TextView
    val checkBox: AppCompatCheckBox

    override val view: View = context.horizontalLayout {
        background = context.listItemDrawable()
        gravityCenterVertical()
        val tColor = context.getColor(R.color.textColor)

        horizontalPadding = dip(16)

        checkBox = add(appCompatCheckBox {

        }, lParams(wrapContent, wrapContent))

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

    override fun onBind(bound: UserSelectionVM.UserCheck<*>) {
        val u = bound.user
        nameTextView.text = u.userName
        avatarImageView.withGlide()
            .load(bound.user.avatarUri ?: R.drawable.ic_avatar)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .placeholder(R.drawable.ic_avatar)
            .centerCrop()
            .into(avatarImageView)
        checkBox.enableIf(!bound.keepState)
    }

    override fun onBind(value: UserSelectionVM.UserCheck<*>, position: Int, payloads: List<Any>) {
        checkBox.isChecked = value.checked
        if (payloads.isEmpty()) {
            super.onBind(value, position, payloads)
        }
    }

    class Diff : Differ.DiffCallback<UserSelectionVM.UserCheck<*>> {
        override fun areItemsTheSame(
            oldItem: UserSelectionVM.UserCheck<*>,
            newItem: UserSelectionVM.UserCheck<*>,
            oldItemPosition: Int,
            newItemPosition: Int,
        ): Boolean {
            return oldItem.user.userId == newItem.user.userId
        }

        override fun areContentsTheSame(
            oldItem: UserSelectionVM.UserCheck<*>,
            newItem: UserSelectionVM.UserCheck<*>,
            oldItemPosition: Int,
            newItemPosition: Int,
        ): Boolean {
            val o = oldItem.user
            val n = newItem.user
            return o.userName == n.userName && oldItem.checked == newItem.checked && oldItem.keepState == newItem.keepState
        }

        override fun getChangePayload(
            oldItem: UserSelectionVM.UserCheck<*>,
            newItem: UserSelectionVM.UserCheck<*>,
            oldItemPosition: Int,
            newItemPosition: Int,
        ): Any? {
            val o = oldItem.user
            val n = newItem.user
            return if (o.userName == n.userName) "checked" else null
        }
    }
}
