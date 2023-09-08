package jp.co.toukei.log.trustar.db.user.entity

import androidx.compose.runtime.Immutable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import jp.co.toukei.log.lib.room.AbstractSync
import jp.co.toukei.log.trustar.common.DbFileRecord
import jp.co.toukei.log.trustar.db.AllocationRow
import org.json.JSONObject

@Entity(
    tableName = "delivery_chart",
)
@Immutable
data class DeliveryChart(
    @ColumnInfo(name = "chart_cd") @PrimaryKey @JvmField val chartCd: String,
    @ColumnInfo(name = "place_cd") @JvmField val placeCd: String?,

    @ColumnInfo(name = "info") @JvmField val info: Info,

    @ColumnInfo(name = "memos") @JvmField val memos: List<ChartMemo>,
    @ColumnInfo(name = "images") @JvmField val images: List<ChartImageFile>,
    @ColumnInfo(name = "last_allocation_row") val lastAllocationRow: AllocationRow?,
    @ColumnInfo(name = "extra") @JvmField val extra: JSONObject?,
) : AbstractSync() {

    @Immutable
    data class ChartMemo(
        val label: String,
        val note: String,
        val highlight: Boolean,
        val extra: JSONObject?,
    ) {

        constructor() : this(
            label = "",
            note = "",
            highlight = false,
            extra = null,
        )
    }

    @Immutable
    data class ChartImageFile(
        val dbStoredFile: DbFileRecord,
        val extra: JSONObject?,
    )

    @Immutable
    data class Info(
        @JvmField val dest: String,
        @JvmField val addr1: String,
        @JvmField val addr2: String,
        @JvmField val tel: String,
        @JvmField val carrier: String,
        @JvmField val carrierTel: String,
    ) {

        fun address(): String = "$addr1 $addr2".trim()
    }
}
