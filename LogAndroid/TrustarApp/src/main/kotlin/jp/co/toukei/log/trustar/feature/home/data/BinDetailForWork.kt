package jp.co.toukei.log.trustar.feature.home.data

import android.content.Context
import jp.co.toukei.log.lib.fastHHMMString
import jp.co.toukei.log.trustar.Config
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.db.user.entity.BinDetail
import kotlin.math.absoluteValue

class BinDetailForWork(
        context: Context,
        @JvmField val detail: BinDetail
) {

    @JvmField
    val workStartTime = detail.workStart?.date

    private val workStartOffsetInMin: Int? = run {
        val appointedFrom = detail.appointedDateFrom
        if (workStartTime != null && appointedFrom != null && detail.isDelayed()) {
            (workStartTime - appointedFrom).toInt() / 60_000
        } else null
    }

    @JvmField
    val workStateText = when {
        workStartOffsetInMin == null -> null
        workStartOffsetInMin < 0 -> {
            "${fastHHMMString(-workStartOffsetInMin)} ${context.getString(R.string.work_in_advance)}"
        }
        else -> {
            "${fastHHMMString(workStartOffsetInMin)} ${context.getString(R.string.work_delayed)}"
        }
    }

    @JvmField
    val delayRankColor: Int? = workStartOffsetInMin?.run {
        val absN = absoluteValue
        detail.delayRank
                ?.split(',')
                ?.map { it.toIntOrNull() ?: -1 }
                ?.zip(Config.delayColorRank)
                ?.filter { (offset, _) -> offset > 0 }
                ?.firstOrNull { absN >= it.first }
                ?.second
    }
}
