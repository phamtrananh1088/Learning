package jp.co.toukei.log.trustar.db.user.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import annotation.Keep
import annotation.Para

/**
 *
 * Fuel
 *
 * ${package_name}
 * ${file_name}
 * ${project_name}
 *
 * @since  : 1.0 : 2019/05/23
 * @author : M.Watanabe
 *
 * @property fuelCd 燃料種類コード
 * @property fuelNm 燃料種類名称
 * @property displayOrder 表示順
 *
 * Copyright (c) Toukei Computer Co., Ltd.<br>
 *
 */

@Entity(tableName = "fuel", primaryKeys = ["fuel_cd"])
class Fuel @Keep constructor(
        @Para("fuelCd") @ColumnInfo(name = "fuel_cd") @JvmField val fuelCd: String,
        @Para("fuelNm") @ColumnInfo(name = "fuel_nm") @JvmField val fuelNm: String,
        @Para("displayOrder") @ColumnInfo(name = "display_order") @JvmField val displayOrder: Int
)
