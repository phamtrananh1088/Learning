/**
 * copied from internet.
 */

package third

import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.disposables.DisposableContainer
import io.reactivex.rxjava3.exceptions.CompositeException
import java.util.WeakHashMap

class WeakDisposableContainer : DisposableContainer, Disposable {

    private val container = WeakHashMap<Disposable, Any>()

    @Volatile
    private var disposed = false

    override fun add(d: Disposable): Boolean {
        if (!disposed) {
            synchronized(this) {
                if (!disposed) {
                    container[d] = Unit
                    return true
                }
            }
        }
        d.dispose()
        return false
    }

    override fun remove(d: Disposable): Boolean {
        if (delete(d)) {
            d.dispose()
            return true
        }
        return false
    }

    override fun delete(d: Disposable): Boolean {
        if (disposed) return false
        synchronized(this) {
            if (disposed) return false
            return container.remove(d) != null
        }
    }

    override fun isDisposed(): Boolean = disposed

    override fun dispose() {
        clear()
        disposed = true
    }

    fun clear() {
        if (disposed) return
        val d: Iterable<Disposable>
        synchronized(this) {
            if (disposed) return
            d = ArrayList(container.keys)
            container.clear()
        }
        val err = d.mapNotNull { runCatching { it.dispose() }.exceptionOrNull() }
        if (err.isNotEmpty()) throw CompositeException(err)
    }
}
