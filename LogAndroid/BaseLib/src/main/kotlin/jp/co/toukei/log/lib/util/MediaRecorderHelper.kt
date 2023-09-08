package jp.co.toukei.log.lib.util

import android.media.MediaRecorder
import android.os.SystemClock
import androidx.annotation.MainThread
import androidx.lifecycle.MutableLiveData
import java.io.File

class MediaRecorderHelper(private val recordingState: MutableLiveData<Boolean>) {

    private var recorder: MediaRecorder? = null

    private var durationStart: Long = Long.MIN_VALUE

    @JvmField
    val extension = ".m4a"

    @MainThread
    fun startRecord(file: File): Boolean {
        release()
        val r = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setAudioChannels(2)
            setAudioEncodingBitRate(96000)
            setAudioSamplingRate(44100)
            setOutputFile(file.absolutePath)
            setOnErrorListener { _, _, _ ->
                stopRecord()
            }
        }
        recorder = r
        val ok = runCatching {
            r.prepare()
            r.start()
        }.isSuccess
        if (ok) durationStart = SystemClock.elapsedRealtime() else release()
        recordingState.value = ok
        return ok
    }

    @MainThread
    fun stopRecord() {
        release()
        recordingState.value = false
    }

    private fun release() {
        runCatching {
            recorder?.apply {
                stop()
                release()
            }
        }
        recorder = null
        durationStart = -1L
    }

    fun duration(): Long {
        val d = durationStart
        return if (d < 0) -1 else SystemClock.elapsedRealtime() - d
    }
}
