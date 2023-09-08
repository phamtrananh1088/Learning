package jp.co.toukei.log.trustar.db.user.entity.bin

import androidx.compose.runtime.Immutable
import androidx.room.ColumnInfo

/**
 *
 * @property zip 郵便番号
 * @property tel1  Tel
 * @property mail1 Mail
 * @property tel2 担当者Tel
 * @property mail2 担当者Mail
 * @property note1 発着地備考1
 * @property note2 発着地備考2
 * @property note3 発着地備考3
 */
@Immutable
data class PlaceExt(
    @ColumnInfo(name = "zip") @JvmField val zip: String?,
    @ColumnInfo(name = "tel1") @JvmField val tel1: String?,
    @ColumnInfo(name = "mail1") @JvmField val mail1: String?,
    @ColumnInfo(name = "tel2") @JvmField val tel2: String?,
    @ColumnInfo(name = "mail2") @JvmField val mail2: String?,
    @ColumnInfo(name = "note1") @JvmField val note1: String?,
    @ColumnInfo(name = "note2") @JvmField val note2: String?,
    @ColumnInfo(name = "note3") @JvmField val note3: String?
)
