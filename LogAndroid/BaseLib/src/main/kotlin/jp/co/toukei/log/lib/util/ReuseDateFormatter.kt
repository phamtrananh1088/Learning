package jp.co.toukei.log.lib.util

import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.*

class ReuseDateFormatter(
        private val pattern: String,
        private val locale: Locale,
        private val timeZone: TimeZone
) : ReuseObject<SimpleDateFormat>() {

    override fun make(): SimpleDateFormat {
        return SimpleDateFormat(pattern, locale).also { it.timeZone = timeZone }
    }

    fun format(date: Date): String = format(date.time)
    fun format(date: Long): String = use { it.format(date) }
    fun parse(text: String): Date? = use { it.parse(text, ParsePosition(0)) ?: null }
}
