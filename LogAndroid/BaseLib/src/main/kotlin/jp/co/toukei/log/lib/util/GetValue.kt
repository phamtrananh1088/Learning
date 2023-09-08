package jp.co.toukei.log.lib.util

sealed class GetValue<out T : Any> {
    class Value<out T : Any>(@JvmField val value: T) : GetValue<T>()
    class Error(@JvmField val error: Throwable) : GetValue<Nothing>()
}
