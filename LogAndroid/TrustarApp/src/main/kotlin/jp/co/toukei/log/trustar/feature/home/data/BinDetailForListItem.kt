package jp.co.toukei.log.trustar.feature.home.data

import android.location.Location
import jp.co.toukei.log.lib.ctx.Ctx
import jp.co.toukei.log.lib.util.CompareItem
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.db.user.embedded.BinDetailAndStatus
import jp.co.toukei.log.trustar.db.user.entity.BinDetail

class BinDetailForListItem(
    @JvmField val binDetailAndStatus: BinDetailAndStatus,
    location: Location?,
) : CompareItem<BinDetailForListItem> {

    @JvmField
    val detail = binDetailAndStatus.detail

    @JvmField
    val status = binDetailAndStatus.status

    @JvmField
    val deviation: String?

    @JvmField
    val inRange: Boolean

    init {
        val p = location?.let { BinDetail.checkDeliveryDeviation(detail, it) }
        deviation = p?.first?.let { if (it <= 1000) "${it}m" else "${(it / 100) / 10F}km" }
        inRange = p?.second ?: false
    }

    override fun sameItem(other: BinDetailForListItem): Boolean {
        return binDetailAndStatus.sameItem(other.binDetailAndStatus)
    }

    @JvmField
    val workNameAndTimeTitle: CharSequence = "[${detail.work?.workNm.orEmpty()}]"

    @JvmField
    val appointedString = detail.displayPlanTime


    @JvmField
    val additionalRedText: String? = detail.run {
        val context = Ctx.context
        val deStatus = delayStatus
        val misdelivered = misdeliveryStatus != 0
        if (deStatus == 0 && !misdelivered) null
        else buildString(8) {
            var notEmpty = false
            val delayed = when (deStatus) {
                1 -> context.getString(R.string.work_in_advance)
                2 -> context.getString(R.string.work_delayed)
                else -> null
            }
            if (delayed != null) {
                append(delayed)
                notEmpty = true
            }
            if (misdelivered) {
                if (notEmpty) append(' ')
                append(context.getString(R.string.work_misdelivered))
            }
        }
    }
}
