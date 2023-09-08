package jp.co.toukei.log.lib.util

import jp.co.toukei.log.lib.sameClass

class Progress(@JvmField val value: Int, @JvmField val total: Int) {

    constructor() : this(100)
    constructor(percentage: Int) : this(percentage, 100)

    fun isCompleted(): Boolean = value >= total

    override fun equals(other: Any?): Boolean {
        return other is Progress && sameClass(other) && total == other.total && value == other.value
    }

    override fun hashCode(): Int {
        var result = value
        result = 31 * result + total
        return result
    }

    fun progressString(): String {
        val s = maxOf(0, value * 100)
        return "${s / total}%"
    }
}
