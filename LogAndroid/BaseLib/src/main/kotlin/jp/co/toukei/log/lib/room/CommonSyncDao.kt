package jp.co.toukei.log.lib.room

interface CommonSyncDao<T : AbstractSync> : CommonDao<T> {
    /**
     * `set sync = 1 where sync = 0`
     * @see [getPending]
     * @see [AbstractSync.sync]
     */
    fun setPending()

    /**
     * get results `where sync = 1`
     * @see [setPending]
     * @see [AbstractSync.sync]
     */
    fun getPending(): List<T>
}
