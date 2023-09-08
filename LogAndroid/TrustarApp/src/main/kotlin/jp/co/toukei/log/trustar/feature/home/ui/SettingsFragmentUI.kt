package jp.co.toukei.log.trustar.feature.home.ui

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.View
import android.widget.TextView
import com.google.android.material.appbar.AppBarLayout
import jp.co.toukei.log.lib.common.backgroundColorResId
import jp.co.toukei.log.lib.common.below
import jp.co.toukei.log.lib.common.boldTypeface
import jp.co.toukei.log.lib.common.bottomToBottomParent
import jp.co.toukei.log.lib.common.constraintLayout
import jp.co.toukei.log.lib.common.defaultLParams
import jp.co.toukei.log.lib.common.horizontalCenterInParent
import jp.co.toukei.log.lib.common.matchConstraint
import jp.co.toukei.log.lib.common.nestedScrollView
import jp.co.toukei.log.lib.common.topToTopParent
import jp.co.toukei.log.trustar.Config
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.common.ui.AppbarUI
import jp.co.toukei.log.trustar.primaryButtonStyle
import splitties.dimensions.dip
import splitties.resources.drawable
import splitties.views.dsl.coordinatorlayout.defaultLParams
import splitties.views.dsl.core.add
import splitties.views.dsl.core.lParams
import splitties.views.dsl.core.matchParent
import splitties.views.dsl.core.textView
import splitties.views.dsl.core.view
import splitties.views.dsl.core.wrapContent
import splitties.views.padding
import splitties.views.verticalPadding

class SettingsFragmentUI(context: Context) : AppbarUI(context) {

    val tel: TextView
    val weatherSetup: View
    val switchUser: View

    init {
        toolbar.apply {
            setTitle(R.string.settings)
        }
        coordinatorLayout.run {
            add(nestedScrollView {
                backgroundColorResId = R.color.defaultBackground
                add(constraintLayout {
                    verticalPadding = dip(64)

                    /* システムの問い合わせ */
                    add(textView {
                        setText(R.string.ask_for_support)
                        id = R.id.id1
                        textSize = 14F
                        boldTypeface()
                        padding = dip(8)
                    }, defaultLParams(wrapContent, wrapContent) {
                        topToTopParent()
                        horizontalCenterInParent()
                    })

                    /* tel */
                    tel = add(textView {
                        id = R.id.id2
                        primaryButtonStyle()
                        compoundDrawablePadding = dip(8)
                        setCompoundDrawables(
                            context.drawable(R.drawable.baseline_call_24)?.mutate()?.apply {
                                colorFilter =
                                    PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
                                setBounds(0, 0, dip(24), dip(24))
                            }, null, null, null
                        )
                        minEms = 10
                    }, defaultLParams(wrapContent, wrapContent) {
                        constrainedWidth = true
                        horizontalCenterInParent()
                        below(R.id.id1)
                        matchConstraintMaxWidth = dip(512)
                    })

                    add(view(::View) {
                        id = R.id.id3
                        backgroundColorResId = R.color.gray_out
                    }, defaultLParams(matchParent, dip(1)) {
                        topMargin = dip(64)
                        below(R.id.id2)
                    })

                    /* 天気を変更する */
                    weatherSetup = add(textView {
                        setText(R.string.weather_settings)
                        id = R.id.id4
                        primaryButtonStyle()
                    }, defaultLParams(matchConstraint, wrapContent) {
                        topMargin = dip(32)
                        below(R.id.id3)
                        startToStart = R.id.id2
                        endToEnd = R.id.id2
                    })
                    add(view(::View) {
                        id = R.id.id5
                        backgroundColorResId = R.color.gray_out
                    }, defaultLParams(matchParent, dip(1)) {
                        topMargin = dip(32)
                        below(R.id.id4)
                    })

                    /* 切替 */
                    switchUser = add(textView {
                        setText(R.string.switch_user)
                        id = R.id.id6
                        primaryButtonStyle()
                    }, defaultLParams(matchConstraint, wrapContent) {
                        topMargin = dip(32)
                        below(R.id.id5)
                        startToStart = R.id.id2
                        endToEnd = R.id.id2
                    })
                    add(view(::View) {
                        id = R.id.id7
                        backgroundColorResId = R.color.gray_out
                    }, defaultLParams(matchParent, dip(1)) {
                        topMargin = dip(32)
                        below(R.id.id6)
                    })
                    add(textView {
                        textSize = 14F
                        text = context.getString(R.string.terminal_id_s1, Config.androidId)
                    }, defaultLParams(wrapContent, wrapContent) {
                        topMargin = dip(32)
                        below(R.id.id7)
                        bottomToBottomParent()
                        horizontalCenterInParent()
                    })
                }, lParams(matchParent, matchParent))
            }, defaultLParams(matchParent, matchParent) {
                behavior = AppBarLayout.ScrollingViewBehavior()
            })
        }
    }
}
