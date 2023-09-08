package jp.co.toukei.log.trustar.feature.home.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.toLiveData
import io.reactivex.rxjava3.processors.BehaviorProcessor
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.common.CommonViewModel
import jp.co.toukei.log.trustar.db.user.entity.BinDetail
import jp.co.toukei.log.trustar.db.user.entity.DelayReason
import jp.co.toukei.log.trustar.other.BinDetailData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BinDetailModel : CommonViewModel() {

    private val binDetailRepo = Current.userRepository.binDetailRepo
    private val workRepo = Current.userRepository.workRepo

    private val binDetailRx = BehaviorProcessor.create<BinDetail>()

    @JvmField
    val binLiveData: LiveData<BinDetailData> = binDetailRx.switchMap {
        binDetailRepo.findBinDetailWithDelayReason(it.allocationNo, it.allocationRowNo)
    }.toLiveData()

    fun binData(binDetail: BinDetail) {
        binDetailRx.onNext(binDetail)
    }

    fun setDelayReason(delayReason: DelayReason) {
        val binDetail = binDetailRx.value ?: return
        vmScope.launch(Dispatchers.IO) {
            workRepo.setDelayReason(binDetail, delayReason)
            Current.syncBin()
        }
    }

    fun setTemperature(temperature: Double?) {
        val binDetail = binDetailRx.value ?: return
        vmScope.launch(Dispatchers.IO) {
            workRepo.setTemperature(binDetail, temperature)
            Current.syncBin()
        }
    }

    fun setMemo(memo: String?) {
        val binDetail = binDetailRx.value ?: return
        vmScope.launch(Dispatchers.IO) {
            workRepo.setMemo(binDetail, memo)
            Current.syncBin()
        }
    }
}
