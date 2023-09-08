package jp.co.toukei.log.trustar.chat.activity

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.FragmentActivity
import com.google.android.exoplayer2.ui.StyledPlayerView
import jp.co.toukei.log.lib.common.showOrGone
import jp.co.toukei.log.lib.lazyViewModel
import jp.co.toukei.log.lib.rippleDrawable
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.chat.vm.ExoMediaPlayerVM
import jp.co.toukei.log.trustar.deprecated.startActivity
import splitties.dimensions.dip
import splitties.views.dsl.core.add
import splitties.views.dsl.core.frameLayout
import splitties.views.dsl.core.imageView
import splitties.views.dsl.core.lParams
import splitties.views.dsl.core.matchParent
import splitties.views.dsl.core.view
import splitties.views.padding

class ExoMediaPlayer : FragmentActivity() {

    private val vm by lazyViewModel<ExoMediaPlayerVM>()

    private class UI(context: Context) {
        val close: View
        val playerView: StyledPlayerView

        @JvmField
        val view = context.frameLayout {
            playerView = add(view(::StyledPlayerView) {
                controllerShowTimeoutMs = 2000
                controllerAutoShow = true
                setShowPreviousButton(false)
                setShowNextButton(false)
            }, lParams(matchParent, matchParent))
            close = add(imageView {
                setImageResource(R.drawable.round_close_24)
                padding = dip(12)
                setColorFilter(Color.WHITE)
                background = rippleDrawable(Int.MAX_VALUE, Color.TRANSPARENT, Color.WHITE)
            }, lParams(dip(48), dip(48)))
        }
    }

    private val ui by lazy { UI(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)

        val uri = intent?.getParcelableExtra<Uri>(ARG_URI) ?: return finish()
        setContentView(ui.view)

        window.setBackgroundDrawable(ColorDrawable(Color.BLACK))
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        vm.setUri(uri)

        vm.player.prepare()

        ui.close.setOnClickListener {
            finish()
        }
        ui.playerView.apply {
            setControllerVisibilityListener(
                StyledPlayerView.ControllerVisibilityListener {
                    ui.close.showOrGone(it == View.VISIBLE)
                }
            )
            player = vm.player
            ui.close.showOrGone(isControllerFullyVisible)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ui.playerView.player = null
    }

    override fun onPause() {
        super.onPause()
        vm.pause()
    }

    override fun onResume() {
        super.onResume()
        vm.resume()
    }

    companion object {
        const val ARG_URI = "ARG_URI"

        fun play(context: Context, uri: Uri) {
            context.startActivity<ExoMediaPlayer>(ARG_URI to uri)
        }
    }
}
