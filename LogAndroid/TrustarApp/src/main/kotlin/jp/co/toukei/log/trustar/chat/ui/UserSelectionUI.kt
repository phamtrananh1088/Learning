package jp.co.toukei.log.trustar.chat.ui

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.text.Editable
import android.text.InputType
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.ImageSpan
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.text.buildSpannedString
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import jp.co.toukei.log.lib.common.backgroundColorResId
import jp.co.toukei.log.lib.common.gravityCenterVertical
import jp.co.toukei.log.lib.common.invisible
import jp.co.toukei.log.lib.common.show
import jp.co.toukei.log.lib.common.singleLine
import jp.co.toukei.log.lib.common.textColor
import jp.co.toukei.log.lib.gradientDrawable
import jp.co.toukei.log.lib.hideIME
import jp.co.toukei.log.lib.rippleDrawable
import jp.co.toukei.log.lib.util.MarginItemDecoration
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.common.ui.DefaultSwipeRecyclerViewUI
import splitties.dimensions.dip
import splitties.views.dsl.core.add
import splitties.views.dsl.core.editText
import splitties.views.dsl.core.horizontalLayout
import splitties.views.dsl.core.horizontalMargin
import splitties.views.dsl.core.imageView
import splitties.views.dsl.core.lParams
import splitties.views.dsl.core.matchParent
import splitties.views.dsl.core.verticalLayout
import splitties.views.dsl.core.verticalMargin
import splitties.views.dsl.core.wrapContent
import splitties.views.dsl.material.defaultLParams
import splitties.views.dsl.recyclerview.recyclerView
import splitties.views.horizontalPadding
import splitties.views.padding

class UserSelectionUI(context: Context) : DefaultSwipeRecyclerViewUI(context) {

    val search: EditText
    val selectedList: RecyclerView

    init {
        appBarLayout.apply {
            add(verticalLayout {
                backgroundColorResId = R.color.defaultBackground

                add(horizontalLayout {
                    weightSum = 1F
                    val color = context.getColor(R.color.textColor)

                    gravityCenterVertical()
                    background = gradientDrawable(dip(2), context.getColor(R.color.listItem))

                    search = add(editText {
                        singleLine()
                        background = null
                        textColor = color
                        textSize = 14F
                        horizontalPadding = dip(8)
                        inputType = InputType.TYPE_CLASS_TEXT
                        imeOptions = EditorInfo.IME_ACTION_DONE or EditorInfo.IME_FLAG_NO_EXTRACT_UI

                        hint = buildSpannedString {
                            AppCompatResources.getDrawable(context, R.drawable.round_search_24)
                                ?.apply {
                                    val tSize = (textSize * 1.4F).toInt()
                                    setBounds(0, 0, tSize, tSize)
                                    colorFilter =
                                        PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP)
                                    append("   ")
                                    setSpan(ImageSpan(this), 1, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                                }
                            append(context.getString(R.string.search_by_name))
                        }
                    }, lParams(0, wrapContent, weight = 1F))
                    val clear = add(imageView {
                        setImageResource(R.drawable.round_close_24)
                        setColorFilter(color)
                        padding = dip(8)
                        background = rippleDrawable(Int.MAX_VALUE)
                    }, lParams(dip(40), dip(40)))

                    val l = object : TextWatcher, View.OnClickListener {
                        override fun onClick(v: View?) {
                            search.apply {
                                hideIME()
                                text = null
                            }
                        }

                        override fun afterTextChanged(s: Editable?) {
                            val e = s.isNullOrEmpty()
                            if (e) clear.invisible() else clear.show()
                        }

                        override fun beforeTextChanged(
                            s: CharSequence?,
                            start: Int,
                            count: Int,
                            after: Int,
                        ) = Unit

                        override fun onTextChanged(
                            s: CharSequence?,
                            start: Int,
                            before: Int,
                            count: Int,
                        ) = Unit
                    }
                    search.addTextChangedListener(l)
                    clear.setOnClickListener(l)
                    clear.performClick()

                }, lParams(matchParent, wrapContent) {
                    verticalMargin = dip(8)
                    horizontalMargin = dip(16)
                })
                selectedList = add(recyclerView {
                    backgroundColorResId = R.color.listItem
                    id = R.id.id1
                    clipToPadding = false
                    horizontalPadding = dip(4)
                    addItemDecoration(MarginItemDecoration(dip(8)))
                    layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                }, lParams(matchParent, wrapContent))

            }, defaultLParams(matchParent, wrapContent) {
                scrollFlags = 0
            })
        }
    }
}
