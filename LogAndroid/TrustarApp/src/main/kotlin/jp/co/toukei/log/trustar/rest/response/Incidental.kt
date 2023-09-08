package jp.co.toukei.log.trustar.rest.response

import annotation.Keep
import annotation.Para
import jp.co.toukei.log.trustar.db.user.entity.IncidentalHeader
import jp.co.toukei.log.trustar.db.user.entity.IncidentalTime
import jp.co.toukei.log.trustar.db.user.entity.IncidentalWork
import jp.co.toukei.log.trustar.dateFromString2
import java.util.*

class Incidental @Keep constructor(
        @Para("incidentalHeaders") rawIncidentalHeaders: Array<RawIncidentalHeader>?,
        @Para("incidentalWorks") workRelated: Array<RawIncidentalWorkRelated>?,
        @Para("incidentals") rawIncidentalWorks: Array<RawIncidentalWork>?,
        @Para("incidentalTimes") rawIncidentalTimes: Array<RawIncidentalTime>?
) {

    @JvmField
    val incidentalHeaders: List<IncidentalHeader>? = rawIncidentalHeaders?.run {
        val related = workRelated?.groupBy { it.sheetNo }
        map {
            val sheet = it.sheetNo
            IncidentalHeader(
                    it.uuid,
                    sheet,
                    it.allocationNo,
                    it.allocationRowNo,
                    it.shipperCd,
                    related?.get(sheet)?.map(RawIncidentalWorkRelated::workCd) ?: emptyList(),
                    it.status,
                    it.picId
            )
        }
    }
    @JvmField
    val incidentalWorks: List<IncidentalWork>? = rawIncidentalWorks?.map { it.item }
    @JvmField
    val incidentalTimes: List<IncidentalTime>? = rawIncidentalTimes?.run {
        rawIncidentalHeaders?.associateBy { it.sheetNo }?.let { m ->
            mapNotNull {
                m[it.sheetNo]?.let { h ->
                    IncidentalTime(
                            it.uuid,
                            h.uuid,
                            it.sheetNo,
                            it.sheetRowNo,
                            it.type,
                            it.beginDatetime,
                            it.endDatetime
                    )
                }
            }
        }
    }

    class RawIncidentalWorkRelated @Keep constructor(
            @Para("sheetNo") @JvmField val sheetNo: String,
            @Para("incidentalCd") @JvmField val workCd: String
    )

    class RawIncidentalHeader @Keep constructor(
            @Para("localSeq") uuid: String?,
            @Para("sheetNo") @JvmField val sheetNo: String,
            @Para("allocationNo") @JvmField val allocationNo: String,
            @Para("allocationRowNo") @JvmField val allocationRowNo: Int,
            @Para("shipperCd") @JvmField val shipperCd: String,
            @Para("status") status: String?,
            @Para("picId") @JvmField val picId: String?
    ) {
        @JvmField
        val uuid = uuid ?: UUID.randomUUID().toString()
        @JvmField
        val status = when (status?.lowercase(Locale.ROOT)) {
            "x" -> -1 //署名無しで完了
            "z" -> 1  //署名済
            else -> 0 //未署名
        }
    }

    class RawIncidentalWork @Keep constructor(
            @Para("incidentalCd") incidentalCd: String,
            @Para("incidentalNm") incidentalNm: String?,
            @Para("displayOrder") displayOrder: Int
    ) {
        @JvmField
        val item = IncidentalWork(
                incidentalCd,
                incidentalNm?.trim(),
                displayOrder
        )
    }

    class RawIncidentalTime @Keep constructor(
            @Para("guid") @JvmField val uuid: String,
            @Para("sheetNo") @JvmField val sheetNo: String,
            @Para("sheetRowNo") @JvmField val sheetRowNo: Int,

            @Para("incidentalCls") @JvmField val type: Int,
            @Para("beginDatetime") beginDatetime: String?,
            @Para("endDatetime") endDatetime: String?
    ) {
        @JvmField
        val endDatetime = endDatetime?.dateFromString2()?.time
        @JvmField
        val beginDatetime = beginDatetime?.dateFromString2()?.time
    }
}
