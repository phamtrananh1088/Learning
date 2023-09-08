package jp.co.toukei.log.trustar.db.result.entity

import androidx.room.ColumnInfo
import jp.co.toukei.log.lib.room.AbstractSync

abstract class CommonUserSync(
        @ColumnInfo(name = "company_cd") @JvmField val companyCd: String,
        @ColumnInfo(name = "user_id") @JvmField val userId: String
) : AbstractSync() {

    init {
        recordChanged()
    }
}
