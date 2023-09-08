package jp.co.toukei.log.lib.room

import androidx.room.ColumnInfo

abstract class AbstractSync {

    /**
     *  # -1: ignored. untouched, nothing to do.
     *  # 0: ready. this record has changed.
     *  # 1: pending. this record is going to sync.
     *  # 2: synced. synced successful.
     */
    @ColumnInfo(name = "sync")
    var sync: Int = -1

    fun recordChanged() {
        sync = 0
    }

    fun setSyncFinished() {
        sync = 2
    }
}
