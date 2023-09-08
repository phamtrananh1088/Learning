package jp.co.toukei.log.trustar.db.result.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import jp.co.toukei.log.trustar.db.AllocationRow
import jp.co.toukei.log.trustar.db.user.entity.DeliveryChart
import jp.co.toukei.log.trustar.user.UserInfo
import org.json.JSONObject

@Entity(
    tableName = "delivery_chart",
    primaryKeys = ["company_cd", "user_id", "chart_cd"]
)
class CommonDeliveryChart(
    companyCd: String,
    userId: String,
    @ColumnInfo(name = "chart_cd") @JvmField val chartCd: String,

    @ColumnInfo(name = "place_cd") @JvmField val placeCd: String?,

    @ColumnInfo(name = "info") @JvmField val info: DeliveryChart.Info,

    @ColumnInfo(name = "memos") @JvmField val memos: List<DeliveryChart.ChartMemo>,
    @ColumnInfo(name = "images") @JvmField val images: List<DeliveryChart.ChartImageFile>,
    @ColumnInfo(name = "last_allocation_row") val lastAllocationRow: AllocationRow?,
    @ColumnInfo(name = "extra") @JvmField val extra: JSONObject?,
) : CommonUserSync(companyCd, userId) {

    constructor(
        userInfo: UserInfo,
        deliveryChart: DeliveryChart,
        images: List<DeliveryChart.ChartImageFile>,
    ) : this(
        companyCd = userInfo.companyCd,
        userId = userInfo.userId,

        chartCd = deliveryChart.chartCd,
        placeCd = deliveryChart.placeCd,

        info = deliveryChart.info,
        memos = deliveryChart.memos,
        images = images,
        lastAllocationRow = deliveryChart.lastAllocationRow,
        extra = deliveryChart.extra,
    )
}
