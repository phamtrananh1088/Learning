package jp.co.toukei.log.trustar.chat.ui

import android.animation.LayoutTransition
import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.EditText
import androidx.appcompat.widget.AppCompatEditText
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import jp.co.toukei.log.lib.common.backgroundColorResId
import jp.co.toukei.log.lib.common.gone
import jp.co.toukei.log.lib.common.gravityCenterVertical
import jp.co.toukei.log.lib.common.textColor
import jp.co.toukei.log.lib.gradientDrawable
import jp.co.toukei.log.lib.lengthFilter
import jp.co.toukei.log.lib.rippleDrawable
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.common.ui.AppbarUI
import splitties.dimensions.dip
import splitties.views.dsl.coordinatorlayout.defaultLParams
import splitties.views.dsl.core.add
import splitties.views.dsl.core.horizontalLayout
import splitties.views.dsl.core.imageView
import splitties.views.dsl.core.lParams
import splitties.views.dsl.core.matchParent
import splitties.views.dsl.core.verticalLayout
import splitties.views.dsl.core.view
import splitties.views.dsl.core.wrapContent
import splitties.views.dsl.material.floatingActionButton
import splitties.views.dsl.recyclerview.recyclerView
import splitties.views.horizontalPadding
import splitties.views.padding
import splitties.views.verticalPadding

class ChatUI(context: Context) : AppbarUI(context) {

    val list: RecyclerView
    val editText: EditText
    val mic: View
    val addPic: View
    val takePhoto: View
    val upload: View
    val send: FloatingActionButton

    init {
        coordinatorLayout.apply {
            add(verticalLayout {
                weightSum = 1F
                backgroundColorResId = R.color.defaultBackground
                list = add(recyclerView {
                    id = R.id.id1
                }, lParams(matchParent, 0, weight = 1F))

                add(horizontalLayout {
                    id = R.id.id2
                    weightSum = 1F
                    gravityCenterVertical()
                    padding = dip(8)
                    clipToPadding = false
                    elevation = dip(2).toFloat()
                    backgroundColorResId = R.color.defaultBackground
                    layoutTransition = LayoutTransition().apply {
                        disableTransitionType(LayoutTransition.DISAPPEARING)
                    }

                    val contentColor = context.getColor(R.color.textColor)

                    editText = add(view(::AppCompatEditText) {
                        background =
                            gradientDrawable(dip(16), context.getColor(R.color.grayBackground))
                        minLines = 1
                        maxLines = 4
                        horizontalPadding = dip(16)
                        verticalPadding = dip(8)
                        textColor = contentColor
                        lengthFilter(100)
                    }, lParams(0, wrapContent) {
                        weight = 1F
                        marginEnd = dip(8)
                    })

                    val icSize = dip(48)

                    mic = add(imageView {
                        setImageResource(R.drawable.round_mic_24)
                        setColorFilter(contentColor)
                        background = rippleDrawable(Int.MAX_VALUE, Color.TRANSPARENT)
                        padding = dip(8)
                    }, lParams(icSize, icSize))
                    addPic = add(imageView {
                        setImageResource(R.drawable.round_add_photo_alternate_24)
                        setColorFilter(contentColor)
                        background = rippleDrawable(Int.MAX_VALUE, Color.TRANSPARENT)
                        padding = dip(8)
                    }, lParams(icSize, icSize))
                    takePhoto = add(imageView {
                        setImageResource(R.drawable.round_photo_camera_24)
                        setColorFilter(contentColor)
                        background = rippleDrawable(Int.MAX_VALUE, Color.TRANSPARENT)
                        padding = dip(8)
                    }, lParams(icSize, icSize))
                    upload = add(imageView {
                        setImageResource(R.drawable.round_upload_file_24)
                        setColorFilter(contentColor)
                        background = rippleDrawable(Int.MAX_VALUE, Color.TRANSPARENT)
                        padding = dip(8)
                    }, lParams(icSize, icSize))

                    send = add(floatingActionButton {
                        customSize = icSize
                        setImageResource(R.drawable.round_send_24)
                        setColorFilter(Color.WHITE)
                        gone()
                    }, lParams(wrapContent, wrapContent))
                }, lParams(matchParent, wrapContent))
            }, defaultLParams(matchParent, matchParent) {
                behavior = AppBarLayout.ScrollingViewBehavior()
            })
        }
    }
}
