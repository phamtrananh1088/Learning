package jp.co.toukei.log.lib.room

import androidx.room.ColumnInfo

abstract class DeletableSync : AbstractSync() {

    @ColumnInfo(name = "deleted")
    var deleted = false
        set(value) {
            field = value
            recordChanged()
        }
}
