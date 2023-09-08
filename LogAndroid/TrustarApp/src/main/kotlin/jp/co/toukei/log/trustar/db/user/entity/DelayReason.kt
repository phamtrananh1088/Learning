package jp.co.toukei.log.trustar.db.user.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import annotation.Keep
import annotation.Para

/**
 *
 * DelayReason
 *
 * ${package_name}
 * ${file_name}
 * ${project_name}
 *
 * @since  : 1.0 : 2019/05/23
 * @author : M.Watanabe
 *
 * @property reasonCd 理由コード
 * @property reasonText 理由文言
 * @property displayOrder 表示順
 *
 * Copyright (c) Toukei Computer Co., Ltd.<br>
 *
 */

@Entity(tableName = "delay_reason", primaryKeys = ["reason_cd"])
class DelayReason @Keep constructor(
        @Para("reasonCd") @ColumnInfo(name = "reason_cd") @JvmField val reasonCd: String,
        @Para("displayOrder") @ColumnInfo(name = "display_order") @JvmField val displayOrder: Int,
        @Para("reasonText") @ColumnInfo(name = "reason_text") @JvmField val reasonText: String?
)
