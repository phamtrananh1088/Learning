package jp.co.toukei.log.trustar.db.user.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import jp.co.toukei.log.lib.jsonArrNotNull
import jp.co.toukei.log.lib.room.AbstractSync
import jp.co.toukei.log.trustar.forEachOptObject
import org.json.JSONArray
import third.jsonObj

@Entity(
        tableName = "collection_result",
        primaryKeys = ["allocation_no", "allocation_row_no"]
)
class CollectionResult(
        @ColumnInfo(name = "allocation_no") @JvmField val allocationNo: String,
        @ColumnInfo(name = "allocation_row_no") @JvmField val allocationRowNo: Int,
        @ColumnInfo(name = "json") @JvmField val json: JSONArray, // No pk for rows.
) : AbstractSync() {

    constructor(
            allocationNo: String,
            allocationRowNo: Int,
            rows: List<Row>
    ) : this(allocationNo, allocationRowNo, rows.jsonArrNotNull {
        jsonObj {
            "collectionNo" v it.collectionNo
            "collectionCd" v it.collectionCd
            "collectionNm" v it.collectionNm
            "expectedQuantity" v it.expectedQuantity
            "actualQuantity" v it.actualQuantity
            "collectionClass" v it.collectionClass
            "displayOrder" v it.displayOrder
        }
    })

    fun rows(): List<Row> {
        val list = ArrayList<Row>()
        json.forEachOptObject { _, j ->
            if (j != null)
                list += Row(
                        j.optString("collectionNo"),
                        j.optString("collectionCd"),
                        j.optString("collectionNm"),
                        j.getDouble("expectedQuantity"),
                        j.getDouble("actualQuantity"),
                        j.getInt("collectionClass"),
                        j.getInt("displayOrder"),
                )
        }
        return list
    }

    class Row(
            @JvmField val collectionNo: String?,
            @JvmField val collectionCd: String?,
            @JvmField val collectionNm: String?,
            @JvmField val expectedQuantity: Double,
            @JvmField val actualQuantity: Double,
            @JvmField val collectionClass: Int,
            @JvmField val displayOrder: Int,
    ) {

        fun emptyCd(): Boolean = collectionCd.isNullOrEmpty()

        fun newRow(quantity: Double, name: String) = Row(
                collectionNo,
                collectionCd,
                if (emptyCd()) name else collectionNm,
                expectedQuantity,
                quantity,
                collectionClass,
                displayOrder
        )
    }
}
