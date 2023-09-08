package jp.co.toukei.log.trustar.chat.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.internal.disposables.DisposableHelper
import jp.co.toukei.log.lib.createTempFileInDir
import jp.co.toukei.log.lib.makeDirs
import jp.co.toukei.log.lib.moveOrCopy
import jp.co.toukei.log.lib.observeNonNull
import jp.co.toukei.log.lib.observeOnUI
import jp.co.toukei.log.lib.requirePermission
import jp.co.toukei.log.lib.subscribeOrIgnore
import jp.co.toukei.log.lib.util.AudioPlayerHelper
import jp.co.toukei.log.lib.util.IntPair
import jp.co.toukei.log.lib.util.MediaRecorderHelper
import jp.co.toukei.log.lib.util.Pending
import jp.co.toukei.log.trustar.Config
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.chat.ui.RecorderUI
import jp.co.toukei.log.trustar.common.ToolbarActivity
import jp.co.toukei.log.trustar.defaultPermissionResultCheck
import third.Clock
import java.io.File
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference

class Recorder : ToolbarActivity() {

    private val ui by lazy { RecorderUI(this) }

    override fun onCreate1(savedInstanceState: Bundle?) {
        setContentView(ui.coordinatorLayout)
        intent?.getStringExtra(TITLE)?.let {
            ui.toolbar.title = it
        }
        setSupportActionBar(ui.toolbar)
        sabCloseIconHomeAsUp()
        recorderState.observeNonNull(this) {
            ui.showMicOrStop(!it)
            if (it) {
                val s = Flowable.interval(0, 1, TimeUnit.SECONDS)
                    .observeOnUI()
                    .subscribeOrIgnore {
                        val d = mediaRecorderHelper.duration()
                        ui.setTextOrPlayer(Clock(if (d > 0) d else 0), false)
                    }
                DisposableHelper.set(clockRef, s)
                recorded.value = false
            } else {
                val k = key.get()
                file.commit(k)
                recorded.value = file.getTarget(k) != null
            }
        }
        playerState.observeNonNull(this) {
            ui.playOrPause(it != AudioPlayerHelper.PLAYING)
        }
        ui.mic.setOnClickListener {
            startRecord()
        }
        ui.stop.setOnClickListener {
            stopAll()
        }
        ui.play.setOnClickListener {
            if (mediaPlayerHelper.playerStarted()) mediaPlayerHelper.unpause()
            else {
                stopAll()
                file.getTarget(key.get())?.let { f -> mediaPlayerHelper.playAudio(f) }
            }
        }
        ui.pause.setOnClickListener {
            mediaPlayerHelper.pause()
        }
        recorded.observeNonNull(this) {
            optionsMenu?.let { o -> o.value.findItem(o.int)?.isVisible = it }
            ui.setTextOrPlayer(null, it)
        }
    }

    private val recorded = MutableLiveData(false)

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1 && permissions.isNotEmpty()) {
            if (defaultPermissionResultCheck(
                    permissions,
                    grantResults,
                    R.string.app_permission_settings_audio_record
                )
            ) {
                startRecord()
            }
        }
    }

    private var optionsMenu: IntPair<Menu>? = null

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add(Menu.NONE, R.string.confirm, 1, R.string.confirm)?.apply {
            setIcon(R.drawable.round_done_24)
            setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
            optionsMenu = IntPair(itemId, menu)
            isVisible = false
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.string.confirm) {
            val target = intent?.getStringExtra(TARGET_FILE)?.let(::File)
            val f = file.getTarget(key.get())
            if (target != null && f != null && f.moveOrCopy(target)) {
                setResult(
                    Activity.RESULT_OK, Intent()
                        .putExtra(TARGET_FILE, target.absolutePath)
                        .putExtra(TARGET_FILE_EXT, mediaRecorderHelper.extension)
                )
            }
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStop() {
        super.onStop()
        stopAll()
    }

    override fun onDestroy() {
        super.onDestroy()
        DisposableHelper.dispose(clockRef)
    }

    private val recorderState = MutableLiveData(false)
    private val mediaRecorderHelper = MediaRecorderHelper(recorderState)
    private val playerState = MutableLiveData(AudioPlayerHelper.CLOSED)
    private val mediaPlayerHelper = AudioPlayerHelper(playerState)
    private val clockRef = AtomicReference<Disposable>()
    private val key = AtomicInteger()

    private val file = object : Pending<Int, File>() {
        private val dir = Config.tmpDir
        override fun createPending(k: Int): File {
            return dir.makeDirs().createTempFileInDir()
        }
    }

    private fun startRecord() {
        stopAll()
        if (!requirePermission(Manifest.permission.RECORD_AUDIO, 1)) return
        mediaRecorderHelper.startRecord(file.getPending(key.incrementAndGet()))
    }

    private fun stopAll() {
        mediaRecorderHelper.stopRecord()
        mediaPlayerHelper.stopPlay()
        DisposableHelper.set(clockRef, null)
    }

    companion object {
        const val TARGET_FILE = "TARGET_FILE"
        const val TARGET_FILE_EXT = "TARGET_FILE_EXT"
        const val TITLE = "TITLE"
    }
}
