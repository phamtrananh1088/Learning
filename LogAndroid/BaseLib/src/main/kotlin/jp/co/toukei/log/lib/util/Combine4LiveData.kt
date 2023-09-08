package jp.co.toukei.log.lib.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

class Combine4LiveData<A : Any, B : Any, C : Any, D : Any, R>(
    sourceA: LiveData<out A?>,
    sourceB: LiveData<out B?>,
    sourceC: LiveData<out C?>,
    sourceD: LiveData<out D?>,
    private val block: (a: A?, b: B?, c: C?, d: D?) -> R?
) : MediatorLiveData<R>() {
    private var a: A? = null
    private var b: B? = null
    private var c: C? = null
    private var d: D? = null

    init {
        addSource(sourceA) {
            a = it
            value = block(a, b, c, d)
        }
        addSource(sourceB) {
            b = it
            value = block(a, b, c, d)
        }
        addSource(sourceC) {
            c = it
            value = block(a, b, c, d)
        }
        addSource(sourceD) {
            d = it
            value = block(a, b, c, d)
        }
    }
}
