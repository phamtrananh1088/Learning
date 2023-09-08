package jp.co.toukei.log.trustar.chat.ui

import android.content.Context
import android.graphics.Color
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.constraintlayout.widget.ConstraintLayout
import jp.co.toukei.log.lib.Const
import jp.co.toukei.log.lib.common.above
import jp.co.toukei.log.lib.common.below
import jp.co.toukei.log.lib.common.constraintLayout
import jp.co.toukei.log.lib.common.defaultLParams
import jp.co.toukei.log.lib.common.endToEndParent
import jp.co.toukei.log.lib.common.guideline
import jp.co.toukei.log.lib.common.horizontalProgressBar
import jp.co.toukei.log.lib.common.matchConstraint
import jp.co.toukei.log.lib.common.startToStartParent
import jp.co.toukei.log.lib.common.verticalCenterInParent
import jp.co.toukei.log.lib.common.viewFlipper
import jp.co.toukei.log.lib.util.AudioPlayerHelper
import jp.co.toukei.log.lib.util.Progress
import jp.co.toukei.log.trustar.R
import splitties.dimensions.dip
import splitties.views.dsl.core.add
import splitties.views.dsl.core.horizontalMargin
import splitties.views.dsl.core.imageView
import splitties.views.dsl.core.lParams
import splitties.views.dsl.core.matchParent
import splitties.views.dsl.core.textView
import splitties.views.dsl.core.wrapContent
import splitties.views.padding
import third.Clock

class PlayerProgressUI(context: Context) {
    private val viewFlipper: ViewFlipper
    private val pause: ImageView
    private val play: ImageView
    private val progressBar: ProgressBar
    private val currentTime: TextView
    private val remainingTime: TextView

    @JvmField
    val view = context.constraintLayout {
        padding = dip(8)
        viewFlipper = add(viewFlipper {
            id = R.id.id1
            play = add(imageView {
                setImageResource(R.drawable.round_play_circle_outline_24)
                setColorFilter(Color.BLACK)
            }, lParams(matchParent, matchParent))
            pause = add(imageView {
                setImageResource(R.drawable.round_pause_circle_outline_24)
                setColorFilter(Color.BLACK)
            }, lParams(matchParent, matchParent))
        }, defaultLParams(dip(48), dip(48)) {
            verticalCenterInParent()
            startToStartParent()
        })
        add(guideline {
            id = R.id.id2
        }, defaultLParams(wrapContent, wrapContent) {
            guidePercent = 0.5F
            orientation = ConstraintLayout.LayoutParams.HORIZONTAL
        })
        progressBar = add(horizontalProgressBar {
            id = R.id.id3
            isIndeterminate = true
        }, defaultLParams(matchConstraint, wrapContent) {
            above(R.id.id2)
            startToEnd = R.id.id1
            endToEndParent()
            horizontalMargin = dip(8)
        })
        currentTime = add(textView {
            id = R.id.id4
        }, defaultLParams(wrapContent, wrapContent) {
            below(R.id.id2)
            startToStart = R.id.id3
            topMargin = dip(2)
        })
        remainingTime = add(textView {
            id = R.id.id5
        }, defaultLParams(wrapContent, wrapContent) {
            below(R.id.id2)
            endToEnd = R.id.id3
            topMargin = dip(2)
        })
    }

    fun setProgress(progress: Progress, playing: Boolean) {
        val v = progress.value
        val t = progress.total
        val r = "-${Clock(t - v).mmss}"

        viewFlipper.displayedChild = if (playing) 1 else 0
        currentTime.text = Clock(v).mmss
        remainingTime.text = r
        progressBar.apply {
            isIndeterminate = v < 0
            max = t
            if (Const.API_PRE_24) setProgress(v) else setProgress(v, true)
        }
    }

    fun pauseControl(player: AudioPlayerHelper) {
        pause.setOnClickListener {
            player.pause()
        }
        play.setOnClickListener {
            player.unpause()
        }
    }
}
