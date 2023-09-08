package jp.co.toukei.log.trustar.viewmodel

import io.reactivex.rxjava3.core.Flowable
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.common.CommonViewModel
import jp.co.toukei.log.trustar.compose.ComposeData
import jp.co.toukei.log.trustar.db.user.entity.DelayReason
import jp.co.toukei.log.trustar.repo.BinDetailRepo
import jp.co.toukei.log.trustar.repo.WorkRepo
import jp.co.toukei.log.trustar.user.LoggedUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BinDetailExtraVM(
    val user: LoggedUser,
    val binDetail: ComposeData.BinRow,
) : CommonViewModel() {
    private val allocationNo: String = binDetail.allocationNo
    private val allocationRowNo: Int = binDetail.allocationRowNo

    private val workRepo = WorkRepo(user.userDB)

    private val binDetailRepo = BinDetailRepo(user.userDB)

    @JvmField
    val details = Flowable.defer {
        binDetailRepo.findBinDetailWithExtraData(allocationNo, allocationRowNo)
    }

    fun setDelayReason(delayReason: DelayReason) {
        vmScope.launch(Dispatchers.IO) {
            workRepo.setDelayReason(binDetail, delayReason)
            Current.syncBin()
        }
    }

    fun setTemperature(temperature: Double?) {
        vmScope.launch(Dispatchers.IO) {
            workRepo.setTemperature(binDetail, temperature)
            Current.syncBin()
        }
    }

    fun setMemo(memo: String?) {
        vmScope.launch(Dispatchers.IO) {
            workRepo.setMemo(binDetail, memo)
            Current.syncBin()
        }
    }
}
