package jp.co.toukei.log.trustar.db.user.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import annotation.Find
import annotation.Para
import jp.co.toukei.log.trustar.colorFromString

/**
 *
 * BinStatus
 *
 * ${package_name}
 * ${file_name}
 * ${project_name}
 *
 * @since  : 1.0 : 2019/05/23
 * @author : M.Watanabe
 *
 * @property binStatusCd ステータスコード
 * @property binStatusNm ステータス名称
 * @property bgColor ステータス背景色
 * @property textColor ステータス文字色
 *
 * Copyright (c) Toukei Computer Co., Ltd.<br>
 *
 */

@Entity(tableName = "bin_status", primaryKeys = ["bin_status_cd"])
class BinStatus(
    @ColumnInfo(name = "bin_status_cd") @JvmField val binStatusCd: Int,
    @ColumnInfo(name = "bin_status_nm") @JvmField val binStatusNm: String,
    @ColumnInfo(name = "bin_back_color") @JvmField val bgColor: Int,
    @ColumnInfo(name = "bin_font_color") @JvmField val textColor: Int,
) {
    @Find
    constructor(
        @Para("binStatusCd") binStatusCd: Int,
        @Para("binStatusNm") binStatusNm: String,
        @Para("binBackColor") binBackColor: String?,
        @Para("binFontColor") binFontColor: String?,
    ) : this(
        binStatusCd,
        binStatusNm,
        binBackColor.colorFromString(),
        binFontColor.colorFromString()
    )
}
