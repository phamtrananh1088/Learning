package jp.co.toukei.log.trustar.chat

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.distinctUntilChanged
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.internal.disposables.DisposableHelper
import io.reactivex.rxjava3.internal.functions.Functions
import jp.co.toukei.log.lib.common.materialAlertDialogBuilder
import jp.co.toukei.log.lib.observeOnUI
import jp.co.toukei.log.lib.util.AudioPlayerHelper
import jp.co.toukei.log.lib.util.DialogWrapper
import jp.co.toukei.log.lib.util.Progress
import jp.co.toukei.log.trustar.chat.ui.PlayerProgressUI
import java.io.File
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference

class RxAudioPlayerHelper {
    private val state = MutableLiveData(AudioPlayerHelper.CLOSED)

    private val mediaPlayerHelper = AudioPlayerHelper(state)

    private val playerProgress = MutableLiveData<Progress?>()

    fun isPlaying() = state.value == AudioPlayerHelper.PLAYING

    @JvmField
    val progress = playerProgress.distinctUntilChanged()

    private val mediaDisposable = AtomicReference<Disposable>()

    fun playAudio(file: Single<File>) {
        val d = file.observeOnUI()
            .flatMapPublisher {
                mediaPlayerHelper.playAudio(it)
                Flowable.interval(0, 200, TimeUnit.MILLISECONDS)
                    .onBackpressureLatest()
                    .switchMap {
                        val p = mediaPlayerHelper.progress()
                        if (p != null) Flowable.just(p) else Flowable.just(Progress())
                    }
                    .takeUntil(Progress::isCompleted)
            }
            .startWithItem(Progress(-1))
            .onErrorResumeWith(Flowable.empty())
            .subscribe(
                { playerProgress.postValue(it) },
                Functions.emptyConsumer(),
                { playerProgress.postValue(null) }
            )
        DisposableHelper.set(mediaDisposable, d)
    }

    fun stopPlayer() {
        DisposableHelper.set(mediaDisposable, null)
        mediaPlayerHelper.stopPlay()
        playerProgress.postValue(null)
    }

    fun dialogWrapper(context: Context): DialogWrapper<PlayerProgressUI> {
        return object : DialogWrapper<PlayerProgressUI>() {
            override fun createDialog(onDismiss: DialogInterface.OnDismissListener): Dialog {
                val v = PlayerProgressUI(context)
                v.pauseControl(mediaPlayerHelper)
                additionValue = v
                return context.materialAlertDialogBuilder {
                    setView(v.view)
                    setOnDismissListener {
                        stopPlayer()
                        onDismiss.onDismiss(it)
                    }
                }.create()
            }
        }
    }
}
