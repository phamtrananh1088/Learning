package jp.co.toukei.log.trustar.chat.ui

import android.content.Context
import android.graphics.Color
import android.widget.ImageView
import android.widget.TextView
import android.widget.ViewFlipper
import com.google.android.material.appbar.AppBarLayout
import jp.co.toukei.log.lib.common.below
import jp.co.toukei.log.lib.common.bottomToBottomParent
import jp.co.toukei.log.lib.common.constraintLayout
import jp.co.toukei.log.lib.common.defaultLParams
import jp.co.toukei.log.lib.common.horizontalCenterInParent
import jp.co.toukei.log.lib.common.matchConstraint
import jp.co.toukei.log.lib.common.showOrGone
import jp.co.toukei.log.lib.common.textColor
import jp.co.toukei.log.lib.common.verticalCenterInParent
import jp.co.toukei.log.lib.common.viewFlipper
import jp.co.toukei.log.lib.gradientDrawableBorder
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.common.ui.AppbarUI
import splitties.dimensions.dip
import splitties.views.backgroundColor
import splitties.views.dsl.coordinatorlayout.defaultLParams
import splitties.views.dsl.core.add
import splitties.views.dsl.core.imageView
import splitties.views.dsl.core.lParams
import splitties.views.dsl.core.matchParent
import splitties.views.dsl.core.textView
import splitties.views.dsl.core.wrapContent
import splitties.views.padding
import third.Clock

class RecorderUI(context: Context) : AppbarUI(context) {
    val mic: ImageView
    val stop: ImageView
    val pause: ImageView
    val play: ImageView
    private val viewFlipper: ViewFlipper
    private val viewFlipper2: ViewFlipper
    private val time: TextView

    init {
        coordinatorLayout.run {
            add(constraintLayout {
                backgroundColor = Color.BLACK
                viewFlipper = add(viewFlipper {
                    id = R.id.id1
                    background = gradientDrawableBorder(
                        Int.MAX_VALUE,
                        Color.TRANSPARENT,
                        Color.WHITE,
                        dip(1)
                    )
                    mic = add(imageView {
                        setImageResource(R.drawable.round_mic_24)
                        setColorFilter(Color.WHITE)
                        padding = dip(48)
                    }, lParams(matchParent, matchParent))
                    stop = add(imageView {
                        setImageResource(R.drawable.round_pause_24)
                        setColorFilter(Color.WHITE)
                        padding = dip(48)
                    }, lParams(matchParent, matchParent))
                }, defaultLParams(matchConstraint, matchConstraint) {
                    horizontalCenterInParent()
                    verticalCenterInParent()
                    dimensionRatio = "1"
                    constrainedHeight = true
                    constrainedWidth = true
                    matchConstraintMaxWidth = dip(200)
                })

                time = add(textView {
                    id = R.id.id2
                    textSize = 30F
                    textColor = Color.WHITE
                }, defaultLParams(wrapContent, wrapContent) {
                    below(R.id.id1)
                    constrainedWidth = true
                    bottomToBottomParent()
                    horizontalCenterInParent()
                })
                viewFlipper2 = add(viewFlipper {
                    id = R.id.id3
                    play = add(imageView {
                        setImageResource(R.drawable.round_play_circle_filled_24)
                        setColorFilter(Color.WHITE)
                    }, lParams(matchParent, matchParent))
                    pause = add(imageView {
                        setImageResource(R.drawable.round_pause_24)
                        setColorFilter(Color.WHITE)
                    }, lParams(matchParent, matchParent))
                }, defaultLParams(dip(48), dip(48)) {
                    below(R.id.id1)
                    bottomToBottomParent()
                    horizontalCenterInParent()
                })
            }, defaultLParams(matchParent, matchParent) {
                behavior = AppBarLayout.ScrollingViewBehavior()
            })
        }
    }

    fun showMicOrStop(showMic: Boolean) {
        viewFlipper.displayedChild = if (showMic) 0 else 1
    }

    fun setTextOrPlayer(clock: Clock?, player: Boolean) {
        time.text = clock?.hhmmss
        if (clock != null) {
            time.showOrGone(true)
            viewFlipper2.showOrGone(false)
        } else {
            viewFlipper2.showOrGone(player)
        }
    }

    fun playOrPause(play: Boolean) {
        viewFlipper2.displayedChild = if (play) 0 else 1
    }
}
