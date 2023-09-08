package jp.co.toukei.log.trustar.other

import jp.co.toukei.log.lib.*
import java.util.*

class DateDetector(private val timeZone: TimeZone) {

    private val regex = listOf(
        Regex("(?<!\\d)(?:([12]\\d{3})/)?(0?[1-9]|1[0-2])/(3[01]|[12]\\d|0?[1-9])(?!\\d)"),
        Regex("(?<!\\d)(?:([12]\\d{3})-)?(0?[1-9]|1[0-2])-(3[01]|[12]\\d|0?[1-9])(?!\\d)"),
        Regex("(?<!\\d)(?:([12]\\d{3})年)?(0?[1-9]|1[0-2])月(3[01]|[12]\\d|0?[1-9])(?!\\d)日?")
    )

    fun findAll(string: String): List<Pair<IntRange, Calendar>> {
        return string.numberToHalfWidth()
            .regexFindAll { from ->
                regex.mapNotNull {
                    it.find(this, from)
                }.minByOrNull {
                    it.range.first
                }
            }
            .mapNotNull {
                val g3 = it.groups[3]?.value?.toIntOrNull() ?: return@mapNotNull null
                val g2 = it.groups[2]?.value?.toIntOrNull() ?: return@mapNotNull null
                val g1 = it.groups[1]?.value?.toIntOrNull()

                it.range to timeZone.currentCalendar().apply {
                    setMidnight0()
                    g1?.let { y -> setIfValid(Calendar.YEAR, y) ?: return@mapNotNull null }
                    setIfValid(Calendar.MONTH, g2 - 1) ?: return@mapNotNull null
                    setIfValid(Calendar.DAY_OF_MONTH, g3) ?: return@mapNotNull null
                }
            }
            .toList()
    }
}
