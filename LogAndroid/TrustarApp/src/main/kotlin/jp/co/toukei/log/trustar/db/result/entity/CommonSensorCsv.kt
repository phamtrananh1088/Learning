package jp.co.toukei.log.trustar.db.result.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import jp.co.toukei.log.trustar.user.UserInfo

@Entity(
    tableName = "sensor_csv",
)
class CommonSensorCsv(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") @JvmField val id: Int,
    @ColumnInfo(name = "company_cd") @JvmField val companyCd: String,
    @ColumnInfo(name = "user_id") @JvmField val userId: String,
    @ColumnInfo(name = "allocation_no") @JvmField val allocationNo: String,
    @ColumnInfo(name = "csv_file", typeAffinity = ColumnInfo.BLOB) @JvmField val csv: ByteArray,
) {

    constructor(userInfo: UserInfo, allocationNo: String, csv: ByteArray) : this(
        0,
        userInfo.companyCd,
        userInfo.userId,
        allocationNo,
        csv,
    )
}
