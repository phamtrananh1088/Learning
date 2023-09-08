package jp.co.toukei.log.trustar.repo

import android.location.Location
import io.reactivex.rxjava3.core.Flowable
import jp.co.toukei.log.lib.inTransaction
import jp.co.toukei.log.trustar.compose.ComposeData
import jp.co.toukei.log.trustar.db.user.db.UserDB
import jp.co.toukei.log.trustar.db.user.embedded.LocationRecord
import jp.co.toukei.log.trustar.db.user.entity.BinDetail
import jp.co.toukei.log.trustar.db.user.entity.BinDetail.Companion.checkIfDelayedDelivery
import jp.co.toukei.log.trustar.db.user.entity.BinDetail.Companion.checkIfEarlyDelivery
import jp.co.toukei.log.trustar.db.user.entity.BinDetail.Companion.checkIfMisdelivered
import jp.co.toukei.log.trustar.db.user.entity.DelayReason
import jp.co.toukei.log.trustar.db.user.entity.Work
import jp.co.toukei.log.trustar.db.user.entity.bin.BinWork
import jp.co.toukei.log.trustar.db.user.entity.bin.Place
import jp.co.toukei.log.trustar.db.user.entity.bin.PlaceLocation
import jp.co.toukei.log.trustar.enum.WorkStatusEnum

class WorkRepo(private val userDb: UserDB) {

    /**
     * 移動開始
     */
    fun moveWork(binDetail: BinDetail) {
        userDb.inTransaction {
            val allocationNo = binDetail.allocationNo
            val allocationRowNo = binDetail.allocationRowNo

            binDetailDao().apply {
                unsetMoving(allocationNo)
                setWorkStatus(WorkStatusEnum.Moving.value, allocationNo, allocationRowNo)
            }
            binHeaderDao().setDestination(allocationRowNo, allocationNo)
        }
    }

    fun autoMoveBinDetail(allocationNo: String, ignoreHasWorking: Boolean = false) {
        userDb.inTransaction {
            binDetailDao().apply {
                if (
                    ignoreHasWorking ||
                    countByStatus(WorkStatusEnum.Working.value, allocationNo) == 0
                ) {
                    val order = findByMaxEndTime(allocationNo)?.serviceOrder
                    findNextBinDetailForWork(allocationNo, order)?.let { moveWork(it) }
                }
            }
        }
    }

    /**
     * 作業開始、"追加作業"から
     * @return [BinDetail]
     */
    fun startWork(
        allocationNo: String,
        place: Place,
        work: ComposeData.Work,
        fromLocation: Location?,
    ): BinDetail = userDb.inTransaction {
        binHeaderDao().setDestination(0, allocationNo)
        binDetailDao().run {
            val newBin = BinDetail.newBinDetail(
                allocationNo,
                countByAllocationNo(allocationNo) + 1,
                countFinished(allocationNo) + 1,
                BinWork(work.workCd, work.workNm),
                place
            )
            unsetMoving(allocationNo)

            newBin.also {
                it.workStart = LocationRecord(fromLocation, System.currentTimeMillis())
                it.workStatus = WorkStatusEnum.Working
                insertOrReplace(it)
            }
        }
    }

    /**
     * 作業開始
     * @return [BinDetail]
     */
    fun startWork(
        allocationNo: String,
        allocationRowNo: Int,
        work: ComposeData.Work,
        fromLocation: Location?,
        addIfFinished: Boolean,
        isAutoMode: Boolean = false,
    ): BinDetail? = userDb.inTransaction {
        binDetailDao().run {
            val bin: BinDetail = find(allocationNo, allocationRowNo) ?: return@run null

            binHeaderDao().setDestination(0, allocationNo)
            val plan = bin.work?.workCd == work.workCd
            val now = System.currentTimeMillis()
            val finished = bin.workStatus.isFinished()
            val newAdd = addIfFinished && finished

            val target = if (plan && !newAdd) bin else {
                if (!finished) {
                    bin.workStatus = WorkStatusEnum.New
                    updateOrIgnore(bin)
                }
                BinDetail.unscheduledBinDetail(
                    bin,
                    countByAllocationNo(allocationNo) + 1,
                    countFinished(allocationNo) + 1,
                    BinWork(work.workCd, work.workNm),
                ).also { insertOrReplace(it) }
            }

            unsetMoving(allocationNo)

            target.apply {
                workStart = LocationRecord(fromLocation, now)
                workStatus = WorkStatusEnum.Working

                // １.早配・遅配の判定と更新
                if (plan) {
                    if (checkIfEarlyDelivery(this, now))
                        delayStatus = 1
                    else if (checkIfDelayedDelivery(this, now))
                        delayStatus = 2
                }
                if (isAutoMode) {
                    operatedInAutoMode()
                } else {
                    // 誤配の判定と更新
                    if (fromLocation != null && checkIfMisdelivered(this, fromLocation)) {
                        misdeliveryStatus = 1
                    }
                }
                updateOrIgnore(this)
            }
        }
    }

    /**
     * 作業終了
     */
    fun endWork(
        allocationNo: String,
        allocationRowNo: Int,
        endLocation: Location?,
        moveNext: Boolean = false,
        isAutoMode: Boolean = false,
    ) {
        return userDb.inTransaction {
            binDetailDao().run {
                val b = find(allocationNo, allocationRowNo)
                b?.apply {
                    workStatus = WorkStatusEnum.Finished
                    workEnd = LocationRecord(endLocation, System.currentTimeMillis())
                    if (isAutoMode)
                        operatedInAutoMode()
                    updateOrIgnore(this)
                }
                if (moveNext) {
                    val fromOrder = b?.serviceOrder
                    if (fromOrder != null) {
                        findNextBinDetailForWork(allocationNo, fromOrder)?.let { moveWork(it) }
                    } else {
                        autoMoveBinDetail(allocationNo)
                    }
                }
            }
        }
    }

    fun setDelayReason(binDetail: ComposeData.BinRow, delayReason: DelayReason) {
        userDb.inTransaction {
            binDetailDao().apply {
                find(binDetail.allocationNo, binDetail.allocationRowNo)?.apply {
                    delayReasonCd = delayReason.reasonCd
                    updateOrIgnore(this)
                }
            }
        }
    }

    /**
     * 温度
     */
    fun setTemperature(binDetail: ComposeData.BinRow, tem: Double?) {
        userDb.inTransaction {
            binDetailDao().apply {
                find(binDetail.allocationNo, binDetail.allocationRowNo)?.apply {
                    temperature = tem
                    updateOrIgnore(this)
                }
            }
        }
    }

    /**
     * memo
     */
    fun setMemo(binDetail: ComposeData.BinRow, memo: String?) {
        userDb.inTransaction {
            binDetailDao().apply {
                find(binDetail.allocationNo, binDetail.allocationRowNo)?.apply {
                    experiencePlaceNote1 = memo
                    updateOrIgnore(this)
                }
            }
        }
    }

    fun setLocation(binDetail: ComposeData.BinRow, location: ComposeData.Location) {
        userDb.inTransaction {
            binDetailDao().apply {
                find(binDetail.allocationNo, binDetail.allocationRowNo)?.apply {
                    placeLocation = PlaceLocation(location.latitude, location.longitude)
                    updateOrIgnore(this)
                }
            }
        }
    }

    fun workList(): Flowable<List<Work>> {
        return userDb.workDao().displayList()
    }
}
