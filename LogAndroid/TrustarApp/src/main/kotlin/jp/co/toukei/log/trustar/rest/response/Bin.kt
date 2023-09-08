package jp.co.toukei.log.trustar.rest.response

import annotation.Keep
import annotation.Para
import jp.co.toukei.log.lib.calendar
import jp.co.toukei.log.lib.setMidnight0
import jp.co.toukei.log.trustar.Config
import jp.co.toukei.log.trustar.dateFromString2
import jp.co.toukei.log.trustar.dateFromStringT
import jp.co.toukei.log.trustar.db.user.embedded.LocationRecord
import jp.co.toukei.log.trustar.db.user.entity.BinDetail
import jp.co.toukei.log.trustar.db.user.entity.BinHeader
import jp.co.toukei.log.trustar.db.user.entity.CollectionResult
import jp.co.toukei.log.trustar.db.user.entity.bin.BinWork
import jp.co.toukei.log.trustar.db.user.entity.bin.Place
import jp.co.toukei.log.trustar.db.user.entity.bin.PlaceExt
import jp.co.toukei.log.trustar.db.user.entity.bin.PlaceLocation
import jp.co.toukei.log.trustar.hms60From4dig
import third.Clock

class Bin @Keep constructor(
    @Para("binHeaders") @JvmField val binHeaders: Array<BinHeader>,
    @Para("binDetails") rawBinDetails: Array<RawBinDetail>?,
    @Para("binCollections") collections: Array<RawCollectionItem>?,
) {

    @JvmField
    val binDetails: List<BinDetail>? = rawBinDetails?.map { it.binDetail }

    @JvmField
    val collectionResults: List<CollectionResult>? =
        collections?.groupBy { Pair(it.allocationNo, it.allocationRowNo) }
            ?.map { (k, v) ->
                CollectionResult(k.first, k.second, v.mapIndexed { index, it ->
                    CollectionResult.Row(
                        it.collectionNo,
                        it.collectionCd,
                        it.collectionNm,
                        it.collectionCount ?: 0.0,
                        it.collectionCountRes ?: 0.0,
                        it.collectionClass,
                        index,
                    )
                })
            }

    class RawBinDetail @Keep constructor(
        @Para("allocationNo") allocationNo: String,
        @Para("allocationRowNo") allocationRowNo: Int,
        @Para("allocationPlanFlag") allocationPlanFlag: Int,
        @Para("workCd") workCd: String?,
        @Para("workNm") workNm: String?,
        @Para("serviceOrder") serviceOrder: Int?,
        @Para("workPlanDate") workPlanDate: String?,
        @Para("appointedTimeFrom") appointedTimeFrom: String?,
        @Para("appointedTimeTo") appointedTimeTo: String?,
        @Para("placeLatitude") placeLatitude: Double?,
        @Para("placeLongitude") placeLongitude: Double?,

        @Para("placeCd") placeCd: String?,
        @Para("placeNm1") placeNm1: String?,
        @Para("placeNm2") placeNm2: String?,
        @Para("placeAddr") placeAddr: String?,
        @Para("placeZip") placeZip: String?,
        @Para("placeTel1") placeTel1: String?,
        @Para("placeMail1") placeMail1: String?,
        @Para("placeTel2") placeTel2: String?,
        @Para("placeMail2") placeMail2: String?,
        @Para("noteNoticeClass") noteNoticeClass: Int?,
        @Para("placeNote1") placeNote1: String?,
        @Para("placeNote2") placeNote2: String?,
        @Para("placeNote3") placeNote3: String?,
        @Para("status") status: Int,
        @Para("delayCheckFlag") delayCheckFlag: Int,
        @Para("delayToleranceFrom") delayToleranceFrom: Int?,
        @Para("delayToleranceTo") delayToleranceTo: Int?,
        @Para("delayStatus") delayStatus: Int,
        @Para("misdeliveryCheckFlag") misdeliveryCheckFlag: Int,
        @Para("misdeliveryMeterTo") misdeliveryMeterTo: Int?,
        @Para("stayTime") stayTime: Int,
        @Para("misdeliveryStatus") misdeliveryStatus: Int,

        @Para("operationOrder") operationOrder: Int?,
        @Para("workDatetimeFrom") workDatetimeFrom: String?,
        @Para("workDatetimeTo") workDatetimeTo: String?,
        @Para("delayReasonCd") delayReasonCd: String?,
        @Para("delayRankC") delayRankC: Int?,
        @Para("delayRankB") delayRankB: Int?,
        @Para("delayRankA") delayRankA: Int?,
        @Para("temperature") temperature: Double?,
        @Para("experiencePlaceNote1") experiencePlaceNote1: String?,
        @Para("updatedDate") updatedDate: String?,

        @Para("chartCd") chartCd: String?,
    ) {

        @JvmField
        val binDetail: BinDetail = run {
            val wd = workPlanDate?.dateFromStringT()
            val atf = wd?.let { date ->
                appointedTimeFrom?.hms60From4dig()?.let { min ->
                    Config.timeZone.calendar(date.time)
                        .apply { setMidnight0() }
                        .timeInMillis + Clock(0, min, 0, 0).totalMillis
                }
            }
            val att = wd?.let { date ->
                appointedTimeTo?.hms60From4dig()?.let { min ->
                    Config.timeZone.calendar(date.time)
                        .apply { setMidnight0() }
                        .timeInMillis + Clock(0, min, 0, 0).totalMillis
                }
            }
            val workStart = workDatetimeFrom.dateFromStringT()
                ?.run { LocationRecord(null, time) }
            val workEnd = workDatetimeTo.dateFromStringT()
                ?.run { LocationRecord(null, time) }

            val placeLocation = run {
                val lat = placeLatitude.takeUnless { it == 0.0 }
                val lon = placeLongitude.takeUnless { it == 0.0 }
                if (lat != null && lon != null) PlaceLocation(lat, lon) else null
            }

            BinDetail(
                allocationNo = allocationNo,
                allocationRowNo = allocationRowNo,
                allocationPlanFlag = allocationPlanFlag,
                work = if (workCd != null) BinWork(workCd, workNm) else null,
                serviceOrder = serviceOrder,
                appointedDateFrom = atf,
                appointedDateTo = att,
                place = Place(placeCd, placeNm1, placeNm2, placeAddr),
                placeExt = PlaceExt(
                    placeZip,
                    placeTel1, placeMail1,
                    placeTel2, placeMail2,
                    placeNote1, placeNote2, placeNote3
                ),
                placeLocation = placeLocation,
                noteNoticeClass = noteNoticeClass,
                status = status,
                delayCheckFlag = delayCheckFlag,
                delayToleranceFromInMin = delayToleranceFrom ?: 0,
                delayToleranceToInMin = delayToleranceTo ?: 0,
                delayStatus = delayStatus.let {
                    when {
                        it != 1 || atf == null -> it
                        workStart == null || workStart.date > atf -> 2
                        else -> 1
                    }
                },
                misdeliveryCheckFlag = misdeliveryCheckFlag,
                misdeliveryMeterTo = misdeliveryMeterTo ?: 0,
                stayTimeInMin = stayTime,
                misdeliveryStatus = misdeliveryStatus,
                operationOrder = operationOrder,
                delayReasonCd = delayReasonCd,
                delayRank = buildString {
                    append(delayRankC ?: -1).append(',')
                    append(delayRankB ?: -1).append(',')
                    append(delayRankA ?: -1)
                },
                origAllocationRowNo = null,
                workStart = workStart,
                workEnd = workEnd,
                temperature = temperature,
                experiencePlaceNote1 = experiencePlaceNote1,
                updatedDate = updatedDate?.dateFromString2()?.time,
                chartCd = chartCd,
            )
        }
    }

    class RawCollectionItem @Keep constructor(
        @Para("allocationNo") @JvmField val allocationNo: String,
        @Para("allocationRowNo") @JvmField val allocationRowNo: Int,
        @Para("collectionNo") @JvmField val collectionNo: String?,
        @Para("collectionClass") @JvmField val collectionClass: Int,
        @Para("collectionCd") @JvmField val collectionCd: String?,
        @Para("collectionNm") @JvmField val collectionNm: String,
        @Para("collectionCount") @JvmField val collectionCount: Double?,
        @Para("collectionCountRes") @JvmField val collectionCountRes: Double?,
    )

}
