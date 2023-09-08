package jp.co.toukei.log.trustar.viewmodel

import android.net.Uri
import io.reactivex.rxjava3.core.Flowable
import jp.co.toukei.log.lib.optionalSwitchMap
import jp.co.toukei.log.trustar.common.TmpFileViewModel
import jp.co.toukei.log.trustar.compose.ComposeData
import jp.co.toukei.log.trustar.db.user.entity.IncidentalHeader
import jp.co.toukei.log.trustar.db.user.entity.IncidentalTime
import jp.co.toukei.log.trustar.db.user.entity.TimeRange
import jp.co.toukei.log.trustar.other.IncidentalHeaderDetail
import jp.co.toukei.log.trustar.repo.IncidentalRepo
import jp.co.toukei.log.trustar.user.LoggedUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import third.Result
import java.io.File
import java.util.UUID
import java.util.concurrent.TimeUnit

class IncidentalListVM(
    val user: LoggedUser,
    val allocationNo: String,
    val allocationRowNo: Int,
) : TmpFileViewModel() {

    private val incidentalRepo = IncidentalRepo(user)

    @JvmField
    val list = Flowable.defer {
        incidentalRepo.sortedSheetList(allocationNo, allocationRowNo)
    }

    @JvmField
    val shipperList = Flowable.defer {
        user.userDB.shipperDao().selectAll().map {
            it.map { s ->
                ComposeData.Shipper(
                    s.shipperCd, s.shipperNm
                )
            }
        }
    }

    @JvmField
    val workList = Flowable.defer {
        incidentalRepo.workList().map {
            it.sortedBy { it.displayOrder }.map { s ->
                ComposeData.IncidentalWork(
                    s.workCd, s.workNm.orEmpty()
                )
            }
        }
    }

    fun delete(header: IncidentalHeader) {
        runBlocking(Dispatchers.IO) {
            incidentalRepo.deleteIncidentalHeader(header.uuid)
        }
    }

    fun signFile(uuid: String): Flowable<Result<Uri>> {
        return incidentalRepo.incidentalHeader(uuid)
            .optionalSwitchMap {
                Flowable.just<Result<Uri>>(Result.Loading)
                    .concatWith(
                        incidentalRepo.cacheSign(it)
                            .onErrorComplete()
                            .defaultIfEmpty(Uri.EMPTY)
                            .map { u -> Result.Value(u) }
                    )
            }
            .throttleWithTimeout(100, TimeUnit.MILLISECONDS)
    }


    fun sheetDetailsByUUID(uuid: String): Flowable<IncidentalHeaderDetail> {
        return runBlocking(Dispatchers.IO) {
            incidentalRepo.sheetDetailsByUUID(uuid)
        }
    }

    fun sign(file: File, header: IncidentalHeader) {
        runBlocking(Dispatchers.IO) {
            incidentalRepo.saveIncidentalHeaderWithPic(header.uuid, file)
        }
    }

    fun addIncidental(
        allocationNo: String,
        allocationRowNo: Int,
        shipper: ComposeData.Shipper,
        works: List<ComposeData.IncidentalWork>,
        items: List<TimeRange>,
    ): IncidentalHeader {
        return runBlocking(Dispatchers.IO) {
            val header = incidentalRepo.addIncidentalHeader(
                allocationNo = allocationNo,
                allocationRowNo = allocationRowNo,
                shipperCd = shipper.shipperCd,
                workCdList = works.map { it.workCd },
            )
            incidentalRepo.saveTimeList(items.map { newIncidentalTime(header, it) })
            header
        }
    }

    fun editIncidental(
        uuid: String,
        shipper: ComposeData.Shipper,
        works: List<ComposeData.IncidentalWork>,
        addedTimeRangeList: List<TimeRange>,
        deletedTimeRangeList: List<TimeRange>,
        editedTimeRangeList: List<TimeRange>,
    ) {
        return runBlocking(Dispatchers.IO) {
            val header = incidentalRepo.setShipperAndWorks(
                uuid = uuid,
                shipperCd = shipper.shipperCd,
                workCdList = works.map { it.workCd }
            ) ?: return@runBlocking

            val saveList = mutableListOf<IncidentalTime>()
            deletedTimeRangeList.forEach {
                it.delegate?.let { entity ->
                    entity.deleted = true
                    saveList += entity
                }
            }
            editedTimeRangeList.forEach {
                it.delegate?.let { entity ->
                    entity.beginDatetime = it.start
                    entity.endDatetime = it.end
                    saveList += entity
                }
            }
            addedTimeRangeList.forEach {
                saveList += newIncidentalTime(header, it)
            }
            incidentalRepo.saveTimeList(saveList)
        }
    }

    private fun newIncidentalTime(header: IncidentalHeader, it: TimeRange): IncidentalTime {
        return IncidentalTime(
            uuid = UUID.randomUUID().toString(),
            headerUUID = header.uuid,
            sheetNo = header.sheetNo,
            sheetRowNo = null,
            type = it.type,
            beginDatetime = it.start,
            endDatetime = it.end
        ).also { it.recordChanged() }
    }
}
