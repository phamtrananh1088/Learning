package jp.co.toukei.log.trustar.db.user.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

/**
 *
 * Work
 *
 * ${package_name}
 * ${file_name}
 * ${project_name}
 *
 * @since  : 1.0 : 2019/05/23
 * @author : M.Watanabe
 *
 * @property workCd 作業コード
 * @property workNm 作業名称
 * @property displayFlag 表示flag
 * @property displayOrder 表示順
 *
 * Copyright (c) Toukei Computer Co., Ltd.<br>
 *
 */

@Entity(tableName = "work", primaryKeys = ["work_cd"])
class Work(
        @ColumnInfo(name = "work_cd") @JvmField val workCd: String,
        @ColumnInfo(name = "work_nm") @JvmField val workNm: String,
        @ColumnInfo(name = "display_flag") @JvmField val displayFlag: Boolean,
        @ColumnInfo(name = "display_order") @JvmField val displayOrder: Int
)
