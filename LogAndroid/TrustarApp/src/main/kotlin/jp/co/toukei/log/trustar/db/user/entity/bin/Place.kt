package jp.co.toukei.log.trustar.db.user.entity.bin

import androidx.compose.runtime.Immutable
import androidx.room.Ignore
import jp.co.toukei.log.trustar.compose.ComposeData

/**
 *
 * @property nm1 作業地名称1
 * @property nm2 作業地名称2
 * @property addr 作業地住所
 */
@Immutable
data class Place(
    @JvmField val cd: String?,
    @JvmField val nm1: String?,
    @JvmField val nm2: String?,
    @JvmField val addr: String?,
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


    fun todoCompose(): ComposeData.Place {
        return ComposeData.Place(nm1, nm2, addr)
    }
}
