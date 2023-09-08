package jp.co.toukei.log.trustar.feature.nimachi.data

import jp.co.toukei.log.trustar.Config

class DateItem(
        @JvmField val date: Long
) {

    @JvmField
    val localDateString: String = Config.dateFormatterMMddHHmm.format(date)
}
