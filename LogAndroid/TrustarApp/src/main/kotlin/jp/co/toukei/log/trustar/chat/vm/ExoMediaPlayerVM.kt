package jp.co.toukei.log.trustar.chat.vm

import android.net.Uri
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SeekParameters
import jp.co.toukei.log.lib.ctx.Ctx
import jp.co.toukei.log.trustar.common.CommonViewModel

class ExoMediaPlayerVM : CommonViewModel() {

    private var uri: Uri? = null

    fun setUri(uri: Uri) {
        if (uri == this.uri) return
        this.uri = uri
        player.setMediaItem(MediaItem.fromUri(uri))
    }

    private var lastState = true

    val player: Player = ExoPlayer.Builder(Ctx.context)
        .setSeekParameters(SeekParameters.PREVIOUS_SYNC)
        .setWakeMode(C.WAKE_MODE_LOCAL)
        .build()
        .apply { playWhenReady = lastState }

    fun pause() {
        lastState = player.playWhenReady
        player.playWhenReady = false
    }

    fun resume() {
        player.playWhenReady = lastState
    }
}
