package jp.co.toukei.log.trustar.repo

import io.reactivex.rxjava3.core.Flowable
import jp.co.toukei.log.lib.optionalSwitchMap
import jp.co.toukei.log.lib.orElseNull
import jp.co.toukei.log.trustar.db.user.db.UserDB
import jp.co.toukei.log.trustar.db.user.embedded.BinDetailAndStatus
import jp.co.toukei.log.trustar.db.user.entity.BinDetail
import jp.co.toukei.log.trustar.other.BinDetailExtraData
import java.util.Optional

class BinDetailRepo(userDB: UserDB) {

    private val incidentalHeaderDao = userDB.incidentalHeaderDao()
    private val binDetailDao = userDB.binDetailDao()
    private val delayReasonDao = userDB.delayReasonDao()
    private val workDao = userDB.workDao()
    private val collectionRepo = userDB.collectionResultDao()

    fun binDetailWithStatusList(allocationNo: String): Flowable<List<BinDetailAndStatus>> {
        return binDetailDao.binDetailAndStatusListByAllocationNo(allocationNo)
    }

    fun findBinDetail(allocationNo: String, allocationRowNo: Int): Flowable<Optional<BinDetail>> {
        return binDetailDao.selectBinDetail(allocationNo, allocationRowNo)
    }

    fun findBinDetailWithExtraData(
        allocationNo: String,
        allocationRowNo: Int,
    ): Flowable<BinDetailExtraData> {
        return findBinDetail(allocationNo, allocationRowNo).optionalSwitchMap {
            Flowable.combineLatest(
                delayReasonDao.selectAll(),
                incidentalHeaderDao.countSheetList(it.allocationNo, it.allocationRowNo),
                incidentalHeaderDao.countSignedSheetList(it.allocationNo, it.allocationRowNo),
                it.work?.workCd?.let(workDao::workByCd) ?: Flowable.just(Optional.empty()),
                collectionRepo.getResult(it.allocationNo, it.allocationRowNo),
            ) { a, c, sc, w, r -> BinDetailExtraData(it, a, c, sc, w.orElseNull(), r.orElseNull()) }
        }
    }

    fun countByStatus(allocationNo: String, status: Int): Flowable<Int> {
        return binDetailDao.countByStatus(allocationNo, status)
    }

    fun countWorkingStatus(allocationNo: String): Flowable<Int> {
        return countByStatus(allocationNo, 2)
    }
}
