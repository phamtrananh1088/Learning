package jp.co.toukei.log.lib.util

import android.content.DialogInterface
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.internal.disposables.DisposableHelper
import java.util.concurrent.atomic.AtomicReference

class DisposeOnDismissListener(disposable: Disposable) : DialogInterface.OnDismissListener {

    private val d = AtomicReference(disposable)

    override fun onDismiss(dialog: DialogInterface?) {
        DisposableHelper.dispose(d)
    }
}
