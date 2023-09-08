package jp.co.toukei.log.lib.common

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import third.Event

class PassDataVM<T, R> : ViewModel() {

    @JvmField
    var source: MutableLiveData<T> = MutableLiveData()

    @JvmField
    var result: MutableLiveData<Event<R>> = MutableLiveData()
}
