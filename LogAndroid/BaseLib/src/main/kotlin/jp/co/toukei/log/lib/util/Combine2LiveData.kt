package jp.co.toukei.log.lib.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

class Combine2LiveData<A : Any, B : Any, R>(
    sourceA: LiveData<out A?>,
    sourceB: LiveData<out B?>,
    private val block: (a: A?, b: B?) -> R
) : MediatorLiveData<R>() {
    private var a: A? = null
    private var b: B? = null

    init {
        addSource(sourceA) {
            a = it
            value = block(a, b)
        }
        addSource(sourceB) {
            b = it
            value = block(a, b)
        }
    }
}
