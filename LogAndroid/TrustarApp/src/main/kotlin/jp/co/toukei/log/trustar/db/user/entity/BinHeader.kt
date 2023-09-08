package jp.co.toukei.log.trustar.db.user.entity

import android.location.Location
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import annotation.Find
import annotation.Para
import jp.co.toukei.log.lib.room.AbstractSync
import jp.co.toukei.log.trustar.dateFromStringT
import jp.co.toukei.log.trustar.db.user.embedded.LocationRecord
import jp.co.toukei.log.trustar.enum.BinStatusEnum
import jp.co.toukei.log.trustar.other.Weather

/**
 *
 * BinHeader
 *
 * ${package_name}
 * ${file_name}
 * ${project_name}
 *
 * @since  : 1.0 : 2019/05/23
 * @author : M.Watanabe
 *
 * @property allocationNo 配車管理No.
 * @property allocationNm 配車管理名
 * @property binStatus 運行状況ステータス
 * @property truckCd 車両コード
 * @property updatedDate 更新日時
 * @property weatherCd 天候コード
 * @property endLocation 運行終了
 *
 * Copyright (c) Toukei Computer Co., Ltd.<br>
 *
 */

@Entity(tableName = "bin_header", primaryKeys = ["allocation_no"])
class BinHeader(
    @ColumnInfo(name = "allocation_no") @JvmField val allocationNo: String,
    @ColumnInfo(name = "allocation_nm") @JvmField val allocationNm: String,
    status: Int,
    truckCd: String,
    @ColumnInfo(name = "updated_date") @JvmField val updatedDate: Long,
    @ColumnInfo(name = "unplanned_flag") @JvmField val unplannedFlag: Boolean,

    @ColumnInfo(name = "plan_start_datetime") @JvmField val planStartDatetime: Long?,
    @ColumnInfo(name = "plan_end_datetime") @JvmField val planEndDatetime: Long?,

    startLocation: LocationRecord?,
    endLocation: LocationRecord?,

    @ColumnInfo(name = "driver_nm") @JvmField val driverNm: String?,
    @ColumnInfo(name = "sub_driver_nm") @JvmField val subDriverNm: String?,
    @ColumnInfo(name = "allocation_note1") @JvmField val allocationNote1: String?,
    outgoingMeter: Int?,
    incomingMeter: Int?,
) : AbstractSync() {

    @Find
    constructor(
        @Para("allocationNo") allocationNo: String,
        @Para("allocationNm") allocationNm: String,
        @Para("binStatus") binStatus: Int,
        @Para("truckCd") truckCd: String,
        @Para("updatedDate") updatedDate: String?,
        @Para("unplannedFlag") unplannedFlag: Int?,

        @Para("startDatetime") planStartDatetime: String?,
        @Para("endDatetime") planEndDatetime: String?,
        @Para("drvStartDatetime") drvStartDatetime: String?,
        @Para("drvEndDatetime") drvEndDatetime: String?,

        @Para("driverNm") driverNm: String?,
        @Para("subDriverNm") subDriverNm: String?,
        @Para("allocationNote1") allocationNote1: String?,

        @Para("outgoingMeter") outgoingMeter: Int?,
        @Para("incomingMeter") incomingMeter: Int?,
    ) : this(
        allocationNo = allocationNo,
        allocationNm = allocationNm,
        status = binStatus,
        truckCd = truckCd,
        updatedDate = updatedDate.dateFromStringT()?.time ?: 0L,
        unplannedFlag = unplannedFlag == 1,

        planStartDatetime = planStartDatetime.dateFromStringT()?.time,
        planEndDatetime = planEndDatetime.dateFromStringT()?.time,

        startLocation = drvStartDatetime?.dateFromStringT()?.let {
            LocationRecord(null, it.time)
        },
        endLocation = drvEndDatetime?.dateFromStringT()?.let {
            LocationRecord(null, it.time)
        },

        driverNm = driverNm,
        subDriverNm = subDriverNm,
        allocationNote1 = allocationNote1,

        outgoingMeter = outgoingMeter,
        incomingMeter = incomingMeter
    )

    /** [BinStatus.binStatusCd] */
    @Suppress("PropertyName")
    @ColumnInfo(name = "bin_status")
    var _status = status
        @Deprecated("room access only")
        set(value) {
            field = value
            recordChanged()
        }

    var binStatus
        get() = BinStatusEnum(_status)
        set(value) {
            _status = value.value
        }


    @ColumnInfo(name = "destination_row_no")
    var destinationRowNo: Int = 0
        set(value) {
            field = value
            recordChanged()
        }

    @Embedded(prefix = "start_")
    var startLocation: LocationRecord? = startLocation
        set(value) {
            field = value
            recordChanged()
        }

    @Embedded(prefix = "end_")
    var endLocation: LocationRecord? = endLocation
        set(value) {
            field = value
            recordChanged()
        }

    @ColumnInfo(name = "weather_cd")
    var weatherCd: Int? = null
        set(value) {
            field = value
            recordChanged()
        }

    @ColumnInfo(name = "outgoing_meter")
    var outgoingMeter = outgoingMeter
        set(value) {
            field = value
            recordChanged()
        }

    @ColumnInfo(name = "incoming_meter")
    var incomingMeter = incomingMeter
        set(value) {
            field = value
            recordChanged()
        }

    @ColumnInfo(name = "bin_truck_cd")
    var truckCd = truckCd
        set(value) {
            field = value
            recordChanged()
        }

    fun startBin(location: Location?) {
        binStatus = BinStatusEnum.Working
        startLocation = LocationRecord(location, System.currentTimeMillis())
    }

    /**
     * 運行終了から
     */
    fun endBin(
        weather: Weather,
        location: Location?,
    ) {
        binStatus = BinStatusEnum.Finished
        weatherCd = weather.value
        endLocation = LocationRecord(location, System.currentTimeMillis())
    }
}
