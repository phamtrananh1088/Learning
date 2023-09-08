package jp.co.toukei.log.trustar.compose

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import jp.co.toukei.log.lib.ctx.Ctx
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.db.user.entity.BinDetail
import jp.co.toukei.log.trustar.db.user.entity.WorkStatus
import jp.co.toukei.log.trustar.enum.BinStatusEnum
import jp.co.toukei.log.trustar.enum.NoticeUseCase
import jp.co.toukei.log.trustar.enum.WorkStatusEnum
import jp.co.toukei.log.trustar.other.Weather

object ComposeData {

    @Immutable
    data class Bin(
        @JvmField val allocationNo: String,
        @JvmField val allocationNm: String,
        @JvmField val truckNm: String,
        val binStatus: BinStatusEnum,
        @JvmField val incomingMeter: Int?,
    )

    @Immutable
    data class EndBin(
        @JvmField val bin: Bin,
        @JvmField val weather: Weather,
    )

    @Immutable
    data class BinRow(
        @JvmField val allocationNo: String,
        @JvmField val allocationRowNo: Int,
//       val status: WorkStatusEnum,
    )

    @Immutable
    data class BinHeaderRow(
        @JvmField val allocationNo: String,
        @JvmField val allocationNm: String,
        val binStatus: BinStatusEnum,
        @JvmField val statusNm: String,
        @JvmField val statusBgColor: Int,
        @JvmField val statusTextColor: Int,
        @JvmField val truckCd: String,
        @JvmField val truckNm: String,
    ) {
        companion object {
            @JvmField
            val key: (BinHeaderRow) -> String = { it.allocationNo }
        }
    }

    @Immutable
    data class BinDetailRow(
        @JvmField val row: BinRow,
        val workStatus: WorkStatusEnum,
        @JvmField val locationLabel: Label?,
        @JvmField val lastDetectLocation: Location?,
        @JvmField val work: Work?,

        @JvmField val statusNm: String,
        @JvmField val statusBgColor: Int,
        @JvmField val statusTextColor: Int,

        @JvmField val rowBgColor: Int,

        @JvmField val hasNotice: Boolean,
        @JvmField val hasPlaceLocation: Boolean,

        @JvmField val workTitle: String,
        @JvmField val appointedTime: String?,
        @JvmField val target: String,
        @JvmField val address: String?,
        @JvmField val warning: String?,
    ) {
        companion object {

            fun fromBinDetail(
                detail: BinDetail,
                status: WorkStatus,
                location: android.location.Location?,
            ): BinDetailRow {
                val hasPlaceLocation = detail.placeLocation != null
                return BinDetailRow(
                    row = BinRow(detail.allocationNo, detail.allocationRowNo),
                    workStatus = detail.workStatus,
                    locationLabel = run {
                        if (hasPlaceLocation) {
                            val p = location?.let { BinDetail.checkDeliveryDeviation(detail, it) }
                            p?.let { (distance, inRange) ->
                                Label(
                                    if (distance <= 1000) "${distance}m" else "${(distance / 100) / 10F}km",
                                    if (inRange) Color.Red else Color.Gray
                                )
                            }
                        } else {
                            Label(
                                Ctx.context.getString(R.string.location_0),
                                Color.Red
                            )
                        }
                    },
                    lastDetectLocation = location?.run {
                        Location(latitude, longitude, accuracy)
                    },
                    work = detail.work?.run {
                        Work(
                            workCd = workCd,
                            workNm = workNm.orEmpty(),
                            displayOrder = 0
                        )
                    },
                    statusNm = status.workStatusNm,
                    statusBgColor = status.bgColor,
                    statusTextColor = status.textColor,
                    rowBgColor = status.itemColor,
                    hasNotice = detail.hasNotice,
                    hasPlaceLocation = hasPlaceLocation,
                    workTitle = "[${detail.work?.workNm.orEmpty()}]",
                    appointedTime = detail.displayPlanTime,
                    target = detail.place.placeNameFull,
                    address = detail.place.addr,
                    warning = detail.run {
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
                )
            }
        }
    }

    @Immutable
    data class Work(
        @JvmField val workCd: String,
        @JvmField val workNm: String,
        @JvmField val displayOrder: Int,
    ) {
        companion object {
            @JvmField
            val key: (Work) -> String = { it.workCd }
        }
    }

    @Immutable
    data class Place(
        @JvmField val nm1: String?,
        @JvmField val nm2: String?,
        @JvmField val address: String?,
    ) {
        @JvmField
        val placeNameFull: String = "${nm1.orEmpty()} ${nm2.orEmpty()}"
    }

    @Immutable
    data class Location(
        @JvmField val latitude: Double,
        @JvmField val longitude: Double,
        @JvmField val accuracy: Float,
    )

    @Immutable
    data class Label(
        @JvmField val string: String,
        val color: Color,
    )

    @Immutable
    data class Notice(
        @JvmField val id: String,
        @JvmField val title: String,
        @JvmField val content: String,
        @JvmField val isNew: Boolean,
        val useCase: NoticeUseCase,
    )

    @Immutable
    data class TruckKun(
        @JvmField val truckCd: String,
        @JvmField val truckNm: String,
    ) {
        companion object {
            @JvmField
            val key: (TruckKun) -> String = { it.truckCd }
        }
    }

    @Immutable
    data class Shipper(
        @JvmField val shipperCd: String,
        @JvmField val shipperNm: String,
    ) {
        companion object {
            @JvmField
            val key: (Shipper) -> String = { it.shipperCd }
        }
    }

    @Immutable
    data class IncidentalWork(
        @JvmField val workCd: String,
        @JvmField val workNm: String,
    ) {
        companion object {
            @JvmField
            val key: (IncidentalWork) -> String = { it.workCd }

            fun joinWorkNm(iterable: Iterable<IncidentalWork>): String {
                return iterable.joinToString("／") { it.workNm }
            }
        }
    }

    @Immutable
    data class SelectChatUser(
        @JvmField val id: String,
        @JvmField val companyCd: String,
        @JvmField val name: String?,
        @JvmField val avatarUrl: String?,
    ) {

        companion object {
            @JvmField
            val key: (SelectChatUser) -> String = { it.id }
        }
    }

}
