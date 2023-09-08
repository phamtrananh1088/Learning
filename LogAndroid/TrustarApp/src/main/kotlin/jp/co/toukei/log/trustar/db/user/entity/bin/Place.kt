package jp.co.toukei.log.trustar.db.user.entity.bin

import androidx.room.ColumnInfo
import androidx.room.Ignore

/**
 *
 * @property nm1 作業地名称1
 * @property nm2 作業地名称2
 * @property addr 作業地住所
 */
class Place(
        @ColumnInfo(name = "cd") @JvmField val cd: String?,
        @ColumnInfo(name = "nm1") @JvmField val nm1: String?,
        @ColumnInfo(name = "nm2") @JvmField val nm2: String?,
        @ColumnInfo(name = "addr") @JvmField val addr: String?
) {
    constructor(name1: String?) : this(
            null,
            name1,
            null,
            null
    )

    @Ignore
    @JvmField
    val placeNameFull: String = "${nm1.orEmpty()} ${nm2.orEmpty()}"
}
