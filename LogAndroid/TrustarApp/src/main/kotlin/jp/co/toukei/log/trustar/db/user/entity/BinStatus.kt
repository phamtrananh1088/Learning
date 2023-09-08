package jp.co.toukei.log.trustar.db.user.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import annotation.Find
import annotation.Para
import jp.co.toukei.log.lib.util.CompareContent
import jp.co.toukei.log.lib.util.CompareItem
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
    @ColumnInfo(name = "bin_font_color") @JvmField val textColor: Int
) : CompareItem<BinStatus>, CompareContent<BinStatus> {
    @Find
    constructor(
        @Para("binStatusCd") binStatusCd: Int,
        @Para("binStatusNm") binStatusNm: String,
        @Para("binBackColor") binBackColor: String?,
        @Para("binFontColor") binFontColor: String?
    ) : this(
        binStatusCd,
        binStatusNm,
        binBackColor.colorFromString(),
        binFontColor.colorFromString()
    )

    @Ignore
    @JvmField
    val type = Type.fromValue(binStatusCd)

    override fun sameItem(other: BinStatus): Boolean {
        return other.binStatusCd == binStatusCd
    }

    override fun sameContent(other: BinStatus): Boolean {
        return bgColor == other.bgColor && binStatusNm == other.binStatusNm && textColor == other.textColor
    }

    sealed class Type(@JvmField val value: Int) {
        object Ready : Type(0)
        object Working : Type(1)
        object Finished : Type(2)

        companion object {

            @JvmStatic
            fun fromValue(value: Int?): Type? {
                return when (value) {
                    0 -> Ready
                    1 -> Working
                    2 -> Finished
                    else -> null
                }
            }
        }
    }
}
