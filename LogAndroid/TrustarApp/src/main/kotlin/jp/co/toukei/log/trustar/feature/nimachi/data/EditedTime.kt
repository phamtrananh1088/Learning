package jp.co.toukei.log.trustar.feature.nimachi.data

class EditedTime(
        @JvmField val t: TimeItem,
        @JvmField val color: Int = 0x11FF0000
) : TimeItem(t.begin, t.end, t.type) {

    var justChanged = false
    var markDeleted = false
    var overrideBeginDate: DateItem? = null
    var overrideEndDate: DateItem? = null

    fun targetBeginDate(): DateItem? {
        return overrideBeginDate ?: begin
    }

    fun targetEndDate(): DateItem? {
        return overrideEndDate ?: end
    }

    override val beginDateString: String?
        get() = targetBeginDate()?.localDateString

    override val endDateString: String?
        get() = targetEndDate()?.localDateString

    fun hasChanged() = justChanged || markDeleted || overrideBeginDate != null || overrideEndDate != null
}
