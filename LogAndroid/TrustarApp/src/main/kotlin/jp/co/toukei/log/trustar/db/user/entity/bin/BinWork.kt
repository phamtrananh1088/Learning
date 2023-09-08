package jp.co.toukei.log.trustar.db.user.entity.bin

import androidx.room.ColumnInfo

class BinWork(
        @ColumnInfo(name = "work_cd") @JvmField val workCd: String,
        @ColumnInfo(name = "work_nm") @JvmField val workNm: String?
)
