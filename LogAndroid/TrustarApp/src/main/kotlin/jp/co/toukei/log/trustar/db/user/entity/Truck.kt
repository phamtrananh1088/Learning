package jp.co.toukei.log.trustar.db.user.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import annotation.Find
import annotation.Para
import jp.co.toukei.log.lib.util.CompareItem
import jp.co.toukei.log.trustar.dateFromStringT

/**
 *
 * Truck
 *
 * ${package_name}
 * ${file_name}
 * ${project_name}
 *
 * @since  : 1.0 : 2019/05/23
 * @author : M.Watanabe
 *
 * @property truckCd 車両コード
 * @property truckNm 車両名称
 * @property usageStartDate 利用開始日
 * @property usageEndDate 利用終了日
 *
 * Copyright (c) Toukei Computer Co., Ltd.<br>
 *
 */

@Entity(tableName = "truck", primaryKeys = ["truck_cd"])
class Truck(
        @ColumnInfo(name = "truck_cd") val truckCd: String,
        @ColumnInfo(name = "truck_nm") val truckNm: String,
        @ColumnInfo(name = "usage_start_date") val usageStartDate: Long,
        @ColumnInfo(name = "usage_end_date") val usageEndDate: Long
) : CompareItem<Truck> {
    @Find
    constructor(
            @Para("truckCd") truckCd: String,
            @Para("truckNm") truckNm: String,
            @Para("usageStartDate") usageStartDate: String?,
            @Para("usageEndDate") usageEndDate: String?
    ) : this(
            truckCd,
            truckNm,
            usageStartDate.dateFromStringT()?.time ?: 0L,
            usageEndDate.dateFromStringT()?.time ?: 0L
    )

    override fun sameItem(other: Truck): Boolean {
        return truckCd == other.truckCd
    }
}
