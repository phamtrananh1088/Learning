package jp.co.toukei.log.lib.util

abstract class ValueBind<T : Any> {

    var boundValue: T? = null
        private set

    abstract fun onBind(bound: T)

    ////// -----    EXT   -----
    fun bind(value: T, position: Int, payloads: List<Any>) {
        boundValue = value
        onBind(value, position, payloads)
    }

    open fun onBind(value: T, position: Int, payloads: List<Any>) {
        onBind(value)
    }
}
