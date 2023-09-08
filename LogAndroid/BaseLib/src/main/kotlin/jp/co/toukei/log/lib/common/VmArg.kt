package jp.co.toukei.log.lib.common

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import jp.co.toukei.log.lib.getViewModel
import jp.co.toukei.log.lib.observeNullable

class VmArg<A> {

    internal class ArgVM<A> : ViewModel() {
        @JvmField
        var value: A? = null
    }

    private val argObserver = MutableLiveData<A?>()
    private var firstTime = true

    fun attach(fragment: Fragment, key: String) {
        val vm = fragment.getViewModel<ArgVM<A>>(key)
        val vv = vm.value
        if (firstTime) arg = vv
        argObserver.observeNullable(fragment) { vm.value = it }
    }

    val argLiveData: LiveData<A?> = argObserver
    var arg: A?
        set(value) {
            firstTime = false
            argObserver.value = value
        }
        get() = argObserver.value
}
