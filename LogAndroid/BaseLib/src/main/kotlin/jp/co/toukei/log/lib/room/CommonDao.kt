package jp.co.toukei.log.lib.room

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

interface CommonDao<T> {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entity: T): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entity: Array<T>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entity: Iterable<T>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplace(entity: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplace(entity: Iterable<T>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplace(entity: Array<T>)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun updateOrIgnore(entity: T)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun updateOrIgnore(entity: Iterable<T>)

    @Delete
    fun delete(entity: T)

    @Delete
    fun delete(entity: Iterable<T>)
}
