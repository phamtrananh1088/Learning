package jp.co.toukei.log.lib.room

import androidx.room.ColumnInfo

abstract class AbstractSync {

    @ColumnInfo(name = "sync")
    var sync: Int = SyncDone

    fun recordChanged() {
        sync = SyncChanged
    }

    fun setSyncFinished() {
        sync = SyncDone
    }
}

const val SyncChanged = 0
const val SyncPending = 1
const val SyncDone = 2
