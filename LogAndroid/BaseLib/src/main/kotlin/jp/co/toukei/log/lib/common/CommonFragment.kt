package jp.co.toukei.log.lib.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import jp.co.toukei.log.lib.hideIME
import jp.co.toukei.log.lib.observeNonNull
import jp.co.toukei.log.lib.observeNullable
import third.WeakDisposableContainer

abstract class CommonFragment<T : FragmentActivity> : Fragment() {

    abstract fun createView(owner: T): View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        @Suppress("UNCHECKED_CAST")
        val owner = requireActivity() as T
        return createView(owner)
    }

    inline fun <T : Any> LiveData<out T?>.observeNonNull(crossinline block: (T) -> Unit) {
        observeNonNull(viewLifecycleOwner, block)
    }

    fun <T> LiveData<out T>.observe(observer: Observer<T>) {
        observe(viewLifecycleOwner, observer)
    }

    inline fun <T : Any> LiveData<out T?>.observeNullable(crossinline block: (T?) -> Unit) {
        observeNullable(viewLifecycleOwner, block)
    }

    protected val viewDisposableContainer: WeakDisposableContainer = WeakDisposableContainer()

    override fun onDestroyView() {
        super.onDestroyView()
        viewDisposableContainer.clear()
    }

    protected fun hideCurrentIME() {
        activity?.currentFocus?.hideIME()
    }

    override fun onDetach() {
        super.onDetach()
        hideCurrentIME()
    }

    open fun handleBackPressed(): Boolean = false

    protected fun setupToolbarNavigation(toolbar: Toolbar) {
        toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.isClickable = true
        view.isFocusable = true
    }
}
