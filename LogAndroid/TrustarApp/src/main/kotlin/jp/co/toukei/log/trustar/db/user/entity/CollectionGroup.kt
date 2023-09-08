package jp.co.toukei.log.trustar.db.user.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import annotation.Keep
import annotation.Para

@Entity(tableName = "collection_group", primaryKeys = ["collection_class"])
class CollectionGroup @Keep constructor(
        @Para("collectionClass") @ColumnInfo(name = "collection_class") @JvmField val collectionClass: Int,
        @Para("collectionClassName") @ColumnInfo(name = "collection_class_name") @JvmField val collectionClassName: String?,
        @Para("displayOrder") @ColumnInfo(name = "display_order") @JvmField val displayOrder: Int,
//        @Para("collectionNo") @ColumnInfo(name = "collection_no") @JvmField val collectionNo: String?,
//        @Para("collectionCd") @ColumnInfo(name = "collection_cd") @JvmField val collectionCd: String,
//        @Para("collectionNm") @ColumnInfo(name = "collection_nm") @JvmField val collectionNm: String,
)
