package jp.co.toukei.log.trustar.rest.response

import annotation.JsonObjectExtra
import annotation.Keep
import annotation.Para
import jp.co.toukei.log.trustar.common.StoredFile
import jp.co.toukei.log.trustar.common.asDbStoredFile
import jp.co.toukei.log.trustar.db.allocationRowOrNull
import jp.co.toukei.log.trustar.db.user.entity.DeliveryChart
import org.json.JSONObject

class RawChart @Keep constructor(
    @Para("chartHeaders") @JvmField val chartHeaders: Array<RawChartHeader>?,
    @Para("chartMemos") @JvmField val chartMemos: Array<RawChartMemo>?,
    @Para("chartFiles") @JvmField val chartFiles: Array<RawChartFile>?,
) {
    @JvmField
    val charts = run {
        val memeMap = chartMemos?.groupBy { it.chartCd }
        val fMap = chartFiles?.groupBy { it.chartCd }
        chartHeaders?.map { chartHeader ->
            DeliveryChart(
                chartCd = chartHeader.chartCd,
                placeCd = chartHeader.placeCd,
                info = DeliveryChart.Info(
                    dest = chartHeader.chartNm.orEmpty(),
                    addr1 = chartHeader.addr1.orEmpty(),
                    addr2 = chartHeader.addr2.orEmpty(),
                    tel = chartHeader.tel.orEmpty(),
                    carrier = chartHeader.carrier.orEmpty(),
                    carrierTel = chartHeader.carrierTel.orEmpty(),
                ),
                memos = memeMap?.get(chartHeader.chartCd)?.map { m ->
                    DeliveryChart.ChartMemo(
                        label = m.title.orEmpty(),
                        note = m.note.orEmpty(),
                        highlight = m.highLightFlag,
                        extra = m.extraJson
                    )
                } ?: emptyList(),
                images = fMap?.get(chartHeader.chartCd)?.map { f ->
                    DeliveryChart.ChartImageFile(
                        dbStoredFile = StoredFile.ByKey(f.fileKey).asDbStoredFile(),
                        extra = f.extraJson,
                    )
                } ?: emptyList(),
                lastAllocationRow = allocationRowOrNull(
                    chartHeader.allocationNo,
                    chartHeader.allocationRowNo
                ),
                extra = chartHeader.extraJson,
            )
        } ?: emptyList()
    }
}


class RawChartHeader @Keep constructor(
    @Para("chartCd") @JvmField val chartCd: String,
    @Para("placeCd") @JvmField val placeCd: String?,
    @Para("chartNm") @JvmField val chartNm: String?,
    @Para("chartAddr1") @JvmField val addr1: String?,
    @Para("chartAddr2") @JvmField val addr2: String?,
    @Para("chartTel") @JvmField val tel: String?,
    @Para("chargeNm") @JvmField val carrier: String?,
    @Para("chargeTel") @JvmField val carrierTel: String?,
    @Para("allocationNo") @JvmField val allocationNo: String?,
    @Para("allocationRowNo") @JvmField val allocationRowNo: Int?,

    // wtf???
    @JsonObjectExtra("mkTimeStamp", "mkUserId", "mkPrgNm") @JvmField val extraJson: JSONObject,
)

class RawChartMemo @Keep constructor(
    @Para("ChartCd") @JvmField val chartCd: String,
    @Para("HighLightFlag") highLightFlag: Int,
    @Para("Title") @JvmField val title: String?,
    @Para("Note") @JvmField val note: String?,

    // wtf???
    @JsonObjectExtra("MkTimeStamp", "MkUserId", "MkPrgNm") @JvmField val extraJson: JSONObject,
) {
    @JvmField
    val highLightFlag = highLightFlag == 1
}

class RawChartFile @Keep constructor(
    @Para("ChartCd") @JvmField val chartCd: String,
    @Para("FileId") @JvmField val fileKey: String,

    // wtf???
    @JsonObjectExtra("MkTimeStamp", "MkUserId", "MkPrgNm") @JvmField val extraJson: JSONObject,
)
