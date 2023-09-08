package jp.co.toukei.log.lib.util

import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.annotation.MainThread
import androidx.lifecycle.MutableLiveData
import java.io.File

class AudioPlayerHelper(private val playState: MutableLiveData<Int>) {

    private var player: MediaPlayer? = null

    @MainThread
    fun playAudio(file: File): Boolean {
        release()
        val p = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            setDataSource(file.absolutePath)
            setOnCompletionListener {
                stopPlay()
            }
        }
        player = p
        val ok = runCatching {
            p.prepare()
            p.start()
        }.isSuccess
        if (!ok) release()
        playState.value = if (ok) PLAYING else CLOSED
        return ok
    }

    private fun release() {
        runCatching {
            player?.apply {
                stop()
                release()
            }
        }
        player = null
    }

    fun progress(): Progress? {
        return player?.let { Progress(it.currentPosition, it.duration) }
    }

    @MainThread
    fun stopPlay() {
        release()
        playState.value = CLOSED
    }

    @MainThread
    fun pause() {
        runCatching {
            player?.pause()
            playState.value = PAUSED
        }
    }

    @MainThread
    fun unpause() {
        runCatching {
            player?.start()
            playState.value = PLAYING
        }
    }

    fun playerStarted(): Boolean = player != null

    companion object {
        const val CLOSED = 0
        const val PLAYING = 1
        const val PAUSED = 2
    }
}
