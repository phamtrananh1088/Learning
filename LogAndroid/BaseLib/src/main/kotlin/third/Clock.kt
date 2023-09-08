/**
 * copied from my personal code.
 * modified
 */
package third

import java.util.*

class Clock(millisOfTheDay: Int) {
    constructor(millis: Long) : this((millis % 86_400_000).toInt())
    constructor(date: Date) : this(date.time)
    constructor(
            h: Int,
            m: Int,
            s: Int,
            millis: Int
    ) : this((((h * 60) + m) * 60 + s) * 1000 + millis)

    @JvmField
    val totalMillis = millisOfTheDay % 86_400_000

    @JvmField
    val totalSeconds = totalMillis / 1000

    @JvmField
    val totalMinutes = totalSeconds / 60

    @JvmField
    val hours = totalMinutes / 60

    @JvmField
    val minutes: Int = totalMinutes - hours * 60

    @JvmField
    val seconds: Int = totalSeconds - totalMinutes * 60

    @JvmField
    val millis: Int = totalMillis - totalSeconds * 1000

    val mmss: String
        get() = String.format("%02d:%02d", totalMinutes, seconds)

    val hhmm: String
        get() = String.format("%02d:%02d", hours, minutes)

    val hhmmss: String
        get() = hhmm + String.format(":%02d", seconds)

    val fullString: String
        get() = hhmmss + String.format(".%03d", millis)

    override fun toString(): String {
        return fullString
    }
}
