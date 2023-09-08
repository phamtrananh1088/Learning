package jp.co.toukei.log.lib.util

class IntPair<out T>(@JvmField val int: Int, @JvmField val value: T) {
    operator fun component1(): Int = int
    operator fun component2(): T = value
}
