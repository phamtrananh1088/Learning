package jp.co.toukei.log.trustar.chat.ui

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.RippleDrawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import jp.co.toukei.log.lib.activatedStateDrawable
import jp.co.toukei.log.lib.common.above
import jp.co.toukei.log.lib.common.bottomToBottomParent
import jp.co.toukei.log.lib.common.constraintLayout
import jp.co.toukei.log.lib.common.defaultLParams
import jp.co.toukei.log.lib.common.horizontalCenterInParent
import jp.co.toukei.log.lib.common.matchConstraint
import jp.co.toukei.log.lib.common.startToStartParent
import jp.co.toukei.log.lib.common.topToTopParent
import jp.co.toukei.log.lib.common.verticalTo
import jp.co.toukei.log.lib.common.whiteText
import jp.co.toukei.log.lib.gradientDrawable
import jp.co.toukei.log.lib.rippleDrawable
import jp.co.toukei.log.lib.rippleDrawableBorder
import jp.co.toukei.log.trustar.R
import splitties.dimensions.dip
import splitties.views.centerText
import splitties.views.dsl.core.add
import splitties.views.dsl.core.frameLayout
import splitties.views.dsl.core.horizontalLayout
import splitties.views.dsl.core.horizontalMargin
import splitties.views.dsl.core.imageView
import splitties.views.dsl.core.lParams
import splitties.views.dsl.core.margin
import splitties.views.dsl.core.matchParent
import splitties.views.dsl.core.textView
import splitties.views.dsl.core.view
import splitties.views.dsl.core.wrapContent
import splitties.views.horizontalPadding
import splitties.views.padding
import splitties.views.verticalPadding

class CameraUI(context: Context) {

    val previewView: PreviewView
    val titleTextView: TextView
    val takePicture: ImageView
    val close: View
    val flash: View
    val camera: View
    val record: View
    val cameraImg: View
    val recordImg: View
    val toggle: View

    @JvmField
    val view = context.constraintLayout {
        previewView = add(view(::PreviewView) {
            id = R.id.id1
        }, defaultLParams(matchParent, matchConstraint) {
            topToTopParent()
            above(R.id.id2)
            verticalBias = 1F
            horizontalCenterInParent()
            dimensionRatio = "3:4"
            bottomMargin = dip(16)
        })
        takePicture = add(imageView {
            id = R.id.id2
            setColorFilter(Color.WHITE)
            background = rippleDrawableBorder(
                Int.MAX_VALUE,
                Color.TRANSPARENT,
                Color.WHITE,
                dip(4),
                Color.WHITE
            )
            padding = dip(28)
        }, defaultLParams(dip(84), dip(84)) {
            bottomMargin = dip(16)
            horizontalCenterInParent()
            bottomToBottomParent()
        })

        flash = add(frameLayout {
            padding = dip(16)
            background = RippleDrawable(
                ColorStateList.valueOf(0x33ffffff),
                gradientDrawable(Int.MAX_VALUE, Color.TRANSPARENT),
                gradientDrawable(Int.MAX_VALUE, Color.WHITE)
            )
            add(view(::View) {
                background = activatedStateDrawable(
                    ContextCompat.getDrawable(context, R.drawable.round_flash_on_24)!!,
                    ContextCompat.getDrawable(context, R.drawable.round_flash_off_24)!!
                ).apply {
                    colorFilter = PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
                }
            }, lParams(dip(24), dip(24)))
        }, defaultLParams(wrapContent, wrapContent) {
            startToStart = R.id.id1
            bottomToBottom = R.id.id1
            horizontalMargin = dip(8)
            bottomMargin = dip(8)
        })
        toggle = add(horizontalLayout {
            padding = dip(8)
            background = gradientDrawable(dip(4), 0x20000000)
            clipChildren = false
            clipToPadding = false
            camera = add(frameLayout {
                cameraImg = add(imageView {
                    setImageResource(R.drawable.round_photo_camera_24)
                    setColorFilter(Color.WHITE)
                }, lParams(dip(20), dip(20)))

                padding = dip(12)
                horizontalPadding = dip(16)
                background = RippleDrawable(
                    ColorStateList.valueOf(0x20000000),
                    ContextCompat.getDrawable(context, R.drawable.underline_4dp)!!,
                    gradientDrawable(0, Color.BLACK)
                )
            }, lParams(wrapContent, wrapContent))

            record = add(frameLayout {
                recordImg = add(imageView {
                    setImageResource(R.drawable.round_videocam_24)
                    setColorFilter(Color.WHITE)
                }, lParams(dip(20), dip(20)))

                padding = dip(12)
                horizontalPadding = dip(16)
                background = RippleDrawable(
                    ColorStateList.valueOf(0x20000000),
                    ContextCompat.getDrawable(context, R.drawable.underline_4dp)!!,
                    gradientDrawable(0, Color.BLACK)
                )
            }, lParams(wrapContent, wrapContent))
        }, defaultLParams(wrapContent, wrapContent) {
            bottomToBottom = R.id.id1
            horizontalCenterInParent()
        })

        close = add(imageView {
            setImageResource(R.drawable.round_close_24)
            id = R.id.id3
            padding = dip(12)
            setColorFilter(Color.WHITE)
            background = rippleDrawable(Int.MAX_VALUE, Color.TRANSPARENT)
        }, defaultLParams(dip(48), dip(48)) {
            topToTopParent()
            startToStartParent()
            margin = dip(8)
        })
        titleTextView = add(textView {
            centerText()
            whiteText()
            verticalPadding = dip(4)
            textSize = 18F
        }, defaultLParams(wrapContent, wrapContent) {
            horizontalCenterInParent()
            verticalTo(R.id.id3, 0.5F)
            constrainedWidth = true
        })
    }

    fun chooseCameraOrRecorder(selectCamera: Boolean) {
        camera.isActivated = selectCamera
        record.isActivated = !selectCamera
    }
}
