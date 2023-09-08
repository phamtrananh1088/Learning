package jp.co.toukei.log.lib.room

import androidx.room.Entity

interface CommonSyncDao<T : AbstractSync> : CommonDao<T> {

    /**
     * `set sync = $SyncPending where sync = $SyncChanged`
     * @see [AbstractSync.sync]
     */
    fun setPending()

    /**
     * get results `where sync = $SyncPending`
     * @see [AbstractSync.sync]
     */
    fun getPending(): List<T>

    /**
     * delete `where sync = $SyncDone`
     * @see [AbstractSync.sync]
     */
    fun deleteSynced()

}
