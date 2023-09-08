package jp.co.toukei.log.trustar.db.user.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import annotation.Find
import annotation.Para
import jp.co.toukei.log.trustar.colorFromString

/**
 *
 * WorkStatus
 *
 * ${package_name}
 * ${file_name}
 * ${project_name}
 *
 * @since  : 1.0 : 2019/05/23
 * @author : M.Watanabe
 *
 * @property workStatusCd ステータスコード
 * @property workStatusNm ステータス名称
 * @property bgColor ステータス背景色
 * @property textColor ステータス文字色
 * @property itemColor プレート背景色
 * @property displayOrder 表示順
 *
 * Copyright (c) Toukei Computer Co., Ltd.<br>
 *
 */

@Entity(tableName = "work_status", primaryKeys = ["work_status_cd"])
class WorkStatus(
    @ColumnInfo(name = "work_status_cd") @JvmField val workStatusCd: Int,
    @ColumnInfo(name = "work_status_nm") @JvmField val workStatusNm: String,
    @ColumnInfo(name = "work_back_color") @JvmField val bgColor: Int,
    @ColumnInfo(name = "work_font_color") @JvmField val textColor: Int,
    @ColumnInfo(name = "plate_back_color") @JvmField val itemColor: Int,
    @ColumnInfo(name = "display_order") @JvmField val displayOrder: Int,
) {

    @Find
    constructor(
        @Para("workStatusCd") workStatusCd: Int,
        @Para("workStatusNm") workStatusNm: String,
        @Para("workBackColor") workBackColor: String?,
        @Para("workFontColor") workFontColor: String?,
        @Para("plateBackColor") plateBackColor: String?,
        @Para("displayOrder") displayOrder: Int,
    ) : this(
        workStatusCd,
        workStatusNm,
        workBackColor.colorFromString(),
        workFontColor.colorFromString(),
        plateBackColor.colorFromString(),
        displayOrder
    )
}
