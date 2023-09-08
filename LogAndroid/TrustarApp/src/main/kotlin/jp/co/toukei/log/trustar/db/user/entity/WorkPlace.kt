package jp.co.toukei.log.trustar.db.user.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

/**
 * 発着地
 */
@Entity(tableName = "work_place", primaryKeys = ["place_cd"])
class WorkPlace(
        @ColumnInfo(name = "place_cd") @JvmField val placeCd: String,
        @ColumnInfo(name = "place_nm") @JvmField val placeNm: String,
        @ColumnInfo(name = "place_image_list") @JvmField val placeImageList: List<String>?
)
