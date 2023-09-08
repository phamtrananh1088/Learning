package jp.co.toukei.log.trustar.db

import androidx.compose.runtime.Immutable


@Immutable
data class IntAndString(
    @JvmField val int: Int,
    @JvmField val string: String
) {
    companion object {

        fun fromString(string: String): IntAndString {
            return string.run {
                val index = indexOf(',')
                if (index < 0) {
                    throw IllegalArgumentException()
                }
                val r = substring(0, index).toInt()
                val n = substring(index + 1)
                IntAndString(r, n)
            }
        }

        fun asString(r: IntAndString): String {
            return "${r.int},${r.string}"
        }
    }
}

//todo inline class?
typealias AllocationRow = IntAndString

//todo inline class?
val AllocationRow.allocationNo
    get() = string

//todo inline class?
val AllocationRow.allocationRowNo
    get() = int

fun AllocationRow(
    allocationNo: String,
    allocationRowNo: Int,
) = AllocationRow(allocationRowNo, allocationNo)

fun allocationRowOrNull(
    allocationNo: String?,
    allocationRowNo: Int?,
) = if (allocationNo == null || allocationRowNo == null) {
    null
} else {
    AllocationRow(
        allocationRowNo,
        allocationNo
    )
}
