package jp.co.toukei.log.trustar.db.user.entity

import android.location.Location
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import jp.co.toukei.log.lib.room.AbstractSync
import jp.co.toukei.log.lib.util.CompareItem
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.db.user.embedded.LocationRecord
import jp.co.toukei.log.trustar.db.user.entity.bin.BinWork
import jp.co.toukei.log.trustar.db.user.entity.bin.Place
import jp.co.toukei.log.trustar.db.user.entity.bin.PlaceExt
import jp.co.toukei.log.trustar.db.user.entity.bin.PlaceLocation
import jp.co.toukei.log.trustar.displayHHmmRange
import kotlin.math.roundToInt

/**
 *
 * BinDetail
 *
 * ${package_name}
 * ${file_name}
 * ${project_name}
 *
 * @since  : 1.0 : 2019/05/23
 * @author : M.Watanabe
 *
 * @property allocationNo 配車管理No.
 * @property allocationRowNo 配車管理行No.
 * @property allocationPlanFlag 配車計画有無フラグ
 * @property workCd 作業コード
 * @property workNm 作業名称
 * @property serviceOrder (計画)運行順
 * @property noteNoticeClass 発着地備考通知区分
 * @property status 作業ステータス
 * @property delayCheckFlag 遅配チェックフラグ
 * @property delayToleranceFrom 遅配許容時間From
 * @property delayToleranceTo 遅配許容時間To
 * @property delayStatus 遅配ステータス
 * @property misdeliveryCheckFlag 誤配チェックフラグ
 * @property misdeliveryMeterTo 誤配許容距離
 * @property misdeliveryStatus 誤配ステータス
 *
 * Copyright (c) Toukei Computer Co., Ltd.<br>
 *
 */

@Entity(tableName = "bin_detail", primaryKeys = ["allocation_no", "allocation_row_no"])
class BinDetail(
    @ColumnInfo(name = "allocation_no") @JvmField val allocationNo: String,
    @ColumnInfo(name = "allocation_row_no") @JvmField val allocationRowNo: Int,

    @ColumnInfo(name = "allocation_plan_flag") @JvmField val allocationPlanFlag: Int,
    work: BinWork?,
    @ColumnInfo(name = "service_order") @JvmField val serviceOrder: Int?,
    @ColumnInfo(name = "appointed_date_from") @JvmField val appointedDateFrom: Long?,
    @ColumnInfo(name = "appointed_date_to") @JvmField val appointedDateTo: Long?,

    @Embedded(prefix = "place_") @JvmField val place: Place,
    @Embedded(prefix = "place_") @JvmField val placeExt: PlaceExt?,
    placeLocation: PlaceLocation?,
    @ColumnInfo(name = "note_notice_class") @JvmField val noteNoticeClass: Int?,
    status: Int,
    @ColumnInfo(name = "delay_check_flag") @JvmField val delayCheckFlag: Int,
    @ColumnInfo(name = "delay_tolerance_from") @JvmField val delayToleranceFromInMin: Int,
    @ColumnInfo(name = "delay_tolerance_to") @JvmField val delayToleranceToInMin: Int,
    delayStatus: Int,
    @ColumnInfo(name = "misdelivery_check_flag") @JvmField val misdeliveryCheckFlag: Int,
    @ColumnInfo(name = "misdelivery_meter_to") @JvmField val misdeliveryMeterTo: Int,
    @ColumnInfo(name = "stay_time") @JvmField val stayTimeInMin: Int,
    misdeliveryStatus: Int,
    @ColumnInfo(name = "operation_order") @JvmField val operationOrder: Int?,
    delayReasonCd: String?,
    @ColumnInfo(name = "delay_rank") @JvmField val delayRank: String?,
    @ColumnInfo(name = "orig_allocation_row_no") @JvmField val origAllocationRowNo: Int?,
    workStart: LocationRecord?,
    workEnd: LocationRecord?,
    temperature: Double?,
    experiencePlaceNote1: String?,
    @ColumnInfo(name = "updated_date") @JvmField val updatedDate: Long?,
) : AbstractSync(), CompareItem<BinDetail> {

    companion object {

        /** 配送計画一覧からの追加作業 */
        fun newBinDetail(
            allocationNo: String,
            allocationRowNo: Int,
            operationOrder: Int,
            work: BinWork,
            place: Place
        ): BinDetail = BinDetail(
            allocationNo,
            allocationRowNo,
            0,
            work,
            null,
            null,
            null,
            place,
            null,
            null,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            operationOrder,
            null,
            null,
            0,
            null,
            null,
            null,
            null,
            System.currentTimeMillis()
        ).apply { recordChanged() }

        /** 予定外作業なら運行情報明細を登録する */
        fun unscheduledBinDetail(
            binDetail: BinDetail,
            allocationRowNo: Int,
            operationOrder: Int,
            work: BinWork
        ): BinDetail = BinDetail(
            binDetail.allocationNo,
            allocationRowNo,
            0,
            work,
            null,
            binDetail.appointedDateFrom,
            binDetail.appointedDateTo,
            binDetail.place,
            binDetail.placeExt,
            binDetail.placeLocation,
            0,
            0,
            0,
            binDetail.delayToleranceFromInMin,
            binDetail.delayToleranceToInMin,
            0,
            binDetail.misdeliveryCheckFlag,
            binDetail.misdeliveryMeterTo,
            binDetail.stayTimeInMin,
            0,
            operationOrder,
            null,
            null,
            binDetail.allocationRowNo,
            null,
            null,
            null,
            null,
            System.currentTimeMillis()
        ).apply { recordChanged() }

        fun stayTimeDelayInMin(binDetail: BinDetail): Int {
            return if (binDetail.unplanned) Current.loggedUser.userInfo.stayTimeInMin else binDetail.stayTimeInMin
        }

        /**
         * 距離と範囲内
         */
        fun checkDeliveryDeviation(binDetail: BinDetail, compare: Location): Pair<Int, Boolean>? =
            binDetail.run {
                val pl = placeLocation ?: return null
                val d = compare.distanceTo(pl.location).roundToInt()
                val m =
                    if (binDetail.unplanned) Current.loggedUser.userInfo.misdeliveryMeterTo
                    else binDetail.misdeliveryMeterTo
                d to (d < m)
            }

        /**
         * 誤配
         */
        fun checkIfMisdelivered(binDetail: BinDetail, compare: Location): Boolean = binDetail.run {
            if (origAllocationRowNo == 0 || misdeliveryCheckFlag == 0) return false
            val check = checkDeliveryDeviation(binDetail, compare) ?: return false
            !check.second
        }

        /**
         * 早配
         */
        fun checkIfEarlyDelivery(binDetail: BinDetail, byDate: Long): Boolean = binDetail.run {
            val ap = appointedDateFrom
            if (ap == null || unplanned || delayCheckFlag == 0) {
                return false
            }
            val d = byDate - ap
            return d / 60_000 < delayToleranceFromInMin
        }

        /**
         * 遅配
         */
        fun checkIfDelayedDelivery(binDetail: BinDetail, byDate: Long): Boolean = binDetail.run {
            val ap = appointedDateFrom
            if (ap == null || unplanned || delayCheckFlag == 0) {
                return false
            }
            val d = byDate - ap
            return d / 60_000 > delayToleranceToInMin
        }
    }

    @Embedded
    var work = work
        set(value) {
            field = value
            recordChanged()
        }

    @Embedded(prefix = "place_")
    var placeLocation = placeLocation
        set(value) {
            field = value
            recordChanged()
        }

    @ColumnInfo(name = "temperature")
    var temperature = temperature
        set(value) {
            field = value
            recordChanged()
        }

    @ColumnInfo(name = "experience_place_note1")
    var experiencePlaceNote1 = experiencePlaceNote1
        set(value) {
            field = value
            recordChanged()
        }


    /**
     * 0: timely delivery
     * 1: too early
     * 2: delayed
     */
    @ColumnInfo(name = "delay_status")
    var delayStatus = delayStatus
        set(value) {
            field = value
            recordChanged()
        }

    @ColumnInfo(name = "misdelivery_status")
    var misdeliveryStatus = misdeliveryStatus
        set(value) {
            field = value
            recordChanged()
        }

    /** [WorkStatus.workStatusCd] */
    @ColumnInfo(name = "status")
    var status = status
        set(value) {
            field = value
            recordChanged()
        }

    /** [DelayReason.reasonCd] */
    @ColumnInfo(name = "delay_reason_cd")
    var delayReasonCd = delayReasonCd
        set(value) {
            field = value
            recordChanged()
        }

    @Embedded(prefix = "work_start_")
    var workStart = workStart
        set(value) {
            field = value
            recordChanged()
        }

    @Embedded(prefix = "work_end_")
    var workEnd = workEnd
        set(value) {
            field = value
            recordChanged()
        }

    @JvmField
    @ColumnInfo(name = "flag_am")
    var autoModeFlag: Boolean = false

    fun operatedInAutoMode() {
        autoModeFlag = true
        recordChanged()
    }

    @Ignore
    @JvmField
    val displayPlanTime: String? = when {
        appointedDateFrom == null || appointedDateTo == null -> null
        else -> displayHHmmRange(appointedDateFrom, appointedDateTo)
    }

    fun isDelayed(): Boolean = delayStatus != 0

    fun displayWorkTime(): String? {
        val from = workStart?.date ?: return null
        return displayHHmmRange(from, workEnd?.date)
    }

    @Ignore
    @JvmField
    val hasNotice: Boolean = noteNoticeClass != 0

    @Ignore
    @JvmField
    val unplanned: Boolean = allocationPlanFlag == 0

    override fun sameItem(other: BinDetail): Boolean {
        return other.allocationNo == allocationNo && other.allocationRowNo == allocationRowNo
    }

    @Ignore
    @JvmField
    val statusType: WorkStatus.Type? = WorkStatus.Type.fromValue(status)
}
