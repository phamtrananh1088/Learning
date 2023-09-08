package jp.co.toukei.log.trustar.common

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GenericVM<T> : ViewModel() {

    @JvmField
    val mutableLiveData = MutableLiveData<T>()
}
