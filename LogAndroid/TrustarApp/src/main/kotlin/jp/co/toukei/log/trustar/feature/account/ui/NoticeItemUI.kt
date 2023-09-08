package jp.co.toukei.log.trustar.feature.account.ui

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.text.style.TextAppearanceSpan
import android.widget.TextView
import androidx.core.text.buildSpannedString
import androidx.core.text.inSpans
import jp.co.toukei.log.lib.UI
import jp.co.toukei.log.lib.common.below
import jp.co.toukei.log.lib.common.boldTypeface
import jp.co.toukei.log.lib.common.bottomToBottomParent
import jp.co.toukei.log.lib.common.constraintLayout
import jp.co.toukei.log.lib.common.defaultLParams
import jp.co.toukei.log.lib.common.endToEndParent
import jp.co.toukei.log.lib.common.gravityCenterHorizontal
import jp.co.toukei.log.lib.common.startToStartParent
import jp.co.toukei.log.lib.common.topToTopParent
import jp.co.toukei.log.lib.rippleDrawable
import jp.co.toukei.log.lib.util.Differ
import jp.co.toukei.log.lib.util.ValueBind
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.db.user.entity.Notice
import splitties.dimensions.dip
import splitties.views.dsl.core.add
import splitties.views.dsl.core.horizontalLayout
import splitties.views.dsl.core.lParams
import splitties.views.dsl.core.matchParent
import splitties.views.dsl.core.textView
import splitties.views.dsl.core.wrapContent
import splitties.views.padding
import splitties.views.textColorResource

class NoticeItemUI(context: Context) : ValueBind<NoticeItemUI.Item>(), UI {

    private val title: TextView
    private val content: TextView

    override val view = context.horizontalLayout {
        gravityCenterHorizontal()
        add(constraintLayout {
            background = rippleDrawable(dip(8), Color.WHITE)
            padding = dip(4)
            maxWidth = dip(512)

            title = add(textView {
                id = R.id.id1
                padding = dip(4)
                textColorResource = R.color.accentOrange
                boldTypeface()
                textSize = 16F
            }, defaultLParams(wrapContent, wrapContent) {
                topToTopParent()
                startToStartParent()
                endToEndParent()
                horizontalBias = 0F
                constrainedWidth = true
            })

            content = add(textView {
                id = R.id.id2
                padding = dip(4)
                textColorResource = R.color.textDark
                textSize = 16F
            }, defaultLParams(wrapContent, wrapContent) {
                below(R.id.id1)
                bottomToBottomParent()
                startToStartParent()
                endToEndParent()
                horizontalBias = 0F
                constrainedWidth = true
            })
        }, lParams(matchParent, wrapContent))
    }

    private val spanReuse = TextAppearanceSpan(
        null,
        0,
        title.textSize.toInt(),
        ColorStateList.valueOf(Color.RED),
        null
    )

    override fun onBind(bound: Item) {
        content.text = bound.noticeText
        val t = bound.noticeTitle
        if (bound.isNew) {
            title.setTextWithNew(t)
        } else {
            title.text = t
        }
    }

    private fun TextView.setTextWithNew(str: String) {
        text = buildSpannedString {
            append(str)
            inSpans(spanReuse) { append("　NEW") }
        }
    }

    class Item(@JvmField val notice: Notice) {
        @JvmField
        val noticeText: CharSequence = notice.noticeText.orEmpty()

        @JvmField
        val noticeTitle: String = notice.noticeTitle.orEmpty()

        @JvmField
        val isNew: Boolean = notice.unreadFlag && noticeTitle.isNotEmpty()
    }

    class Diff : Differ.SimpleDiffCallback<Item>() {
        override fun areItemsTheSame(
            oldItem: Item,
            newItem: Item,
            oldItemPosition: Int,
            newItemPosition: Int,
        ): Boolean {
            return oldItem.notice.recordId == newItem.notice.recordId
        }

        override fun areContentsTheSame(
            oldItem: Item,
            newItem: Item,
            oldItemPosition: Int,
            newItemPosition: Int,
        ): Boolean {
            return oldItem.isNew == newItem.isNew &&
                    oldItem.noticeText == newItem.noticeText &&
                    oldItem.noticeTitle == newItem.noticeTitle
        }
    }
}
