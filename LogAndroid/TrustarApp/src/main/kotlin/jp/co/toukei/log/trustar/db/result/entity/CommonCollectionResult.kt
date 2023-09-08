package jp.co.toukei.log.trustar.db.result.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import jp.co.toukei.log.lib.jsonArrNotNull
import jp.co.toukei.log.trustar.db.user.entity.CollectionResult
import jp.co.toukei.log.trustar.user.UserInfo
import org.json.JSONArray
import third.jsonObj

@Entity(
        tableName = "common_collection_result",
        primaryKeys = ["company_cd", "user_id", "allocation_no", "allocation_row_no"]
)
class CommonCollectionResult(
        companyCd: String,
        userId: String,
        @ColumnInfo(name = "allocation_no") @JvmField val allocationNo: String,
        @ColumnInfo(name = "allocation_row_no") @JvmField val allocationRowNo: Int,
        @ColumnInfo(name = "post_json") @JvmField val postJson: JSONArray, // No pk for rows.
) : CommonUserSync(companyCd, userId) {

    constructor(userInfo: UserInfo, result: CollectionResult) : this(
            userInfo.companyCd,
            userInfo.userId,
            result.allocationNo,
            result.allocationRowNo,
            result.rows().jsonArrNotNull {
                jsonObj {
                    "allocationNo" v result.allocationNo
                    "allocationRowNo" v result.allocationRowNo
                    "collectionNo" v it.collectionNo
                    "collectionCd" v it.collectionCd
                    "collectionNm" v it.collectionNm
                    "collectionCountRes" v it.actualQuantity
                    "collectionClass" v it.collectionClass
                }
            }
    )
}
