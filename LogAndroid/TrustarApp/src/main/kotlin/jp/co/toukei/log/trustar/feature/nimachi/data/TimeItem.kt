package jp.co.toukei.log.trustar.feature.nimachi.data

import jp.co.toukei.log.lib.util.CompareItem
import jp.co.toukei.log.trustar.db.user.entity.IncidentalTime

/**
 * @property type 0: 荷待待機　1: 付帯業務
 */
open class TimeItem(
        @JvmField val begin: DateItem?,
        @JvmField val end: DateItem?,
        @JvmField val type: Int
) : CompareItem<TimeItem> {
    constructor(
            begin: Long?,
            end: Long?,
            type: Int
    ) : this(
            begin?.let { DateItem(it) },
            end?.let { DateItem(it) },
            type
    )

    open val beginDateString: String? = begin?.localDateString
    open val endDateString: String? = end?.localDateString

    override fun sameItem(other: TimeItem): Boolean {
        return this === other
    }
}


fun <T : TimeItem> Iterable<T>.nimachiTime(): Sequence<T> {
    return asSequence().filter { it.type == 0 }
}

fun <T : TimeItem> Iterable<T>.additionalTime(): Sequence<T> {
    return asSequence().filter { it.type == 1 }
}

class TimeItemDB(@JvmField val time: IncidentalTime) : TimeItem(
        time.beginDatetime,
        time.endDatetime,
        time.type
) {

    override fun sameItem(other: TimeItem): Boolean {
        return other is TimeItemDB && time.sameItem(other.time)
    }
}
