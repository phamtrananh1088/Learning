package jp.co.toukei.log.trustar.repo

import io.reactivex.rxjava3.core.Flowable
import jp.co.toukei.log.lib.orElseNull
import jp.co.toukei.log.trustar.db.user.db.UserDB
import jp.co.toukei.log.trustar.db.user.embedded.BinDetailAndStatus
import jp.co.toukei.log.trustar.db.user.entity.BinDetail
import jp.co.toukei.log.trustar.db.user.entity.WorkStatus
import jp.co.toukei.log.trustar.other.BinDetailData
import java.util.Optional

class BinDetailRepo(userDB: UserDB) {

    private val incidentalHeaderDao = userDB.incidentalHeaderDao()
    private val binDetailDao = userDB.binDetailDao()
    private val delayReasonDao = userDB.delayReasonDao()
    private val workDao = userDB.workDao()

    fun binDetailWithStatusList(allocationNo: String): Flowable<List<BinDetailAndStatus>> {
        return binDetailDao.binDetailAndStatusListByAllocationNo(allocationNo)
    }

    fun findBinDetail(allocationNo: String, allocationRowNo: Int): Flowable<BinDetail> {
        return binDetailDao.selectBinDetail(allocationNo, allocationRowNo)
    }

    fun countByStatus(status: WorkStatus.Type, allocationNo: String): Int {
        return binDetailDao.countByStatus(status.value, allocationNo)
    }

    fun findBinDetailWithDelayReason(
        allocationNo: String,
        allocationRowNo: Int,
    ): Flowable<BinDetailData> {
        return findBinDetail(allocationNo, allocationRowNo).switchMap {
            Flowable.combineLatest(
                delayReasonDao.selectAll(),
                incidentalHeaderDao.countSheetList(it.allocationNo, it.allocationRowNo),
                incidentalHeaderDao.countSignedSheetList(it.allocationNo, it.allocationRowNo),
                it.work?.workCd?.let(workDao::workByCd) ?: Flowable.just(Optional.empty())
            ) { a, c, sc, w -> BinDetailData(it, a, c, sc, w.orElseNull()) }
        }
    }
}
