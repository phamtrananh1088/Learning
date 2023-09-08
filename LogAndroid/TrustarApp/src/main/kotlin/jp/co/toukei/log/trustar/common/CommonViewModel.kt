package jp.co.toukei.log.trustar.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.disposables.DisposableContainer
import jp.co.toukei.log.lib.addTo
import jp.co.toukei.log.trustar.Current
import third.WeakDisposableContainer

abstract class CommonViewModel : ViewModel() {

    private val disposeOnClear = WeakDisposableContainer().apply {
        Current.disposableContainer.add(this)
    }

    protected val vmScope = viewModelScope

    override fun onCleared() {
        disposeOnClear.dispose()
    }

    fun Disposable.disposeOnClear() {
        addTo(disposeOnClear)
    }

    val disposableContainer: DisposableContainer = disposeOnClear

    fun clearDisposable() {
        disposeOnClear.clear()
    }
}
