package jp.co.toukei.log.trustar.feature.nimachi.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.toLiveData
import io.reactivex.rxjava3.processors.BehaviorProcessor
import jp.co.toukei.log.lib.replayThenAutoConnect
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.common.CommonViewModel
import jp.co.toukei.log.trustar.db.user.embedded.IncidentalListItem
import jp.co.toukei.log.trustar.db.user.entity.BinDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SheetListVM : CommonViewModel() {

    private val repo = Current.userRepository.incidentalRepo

    private val bin = BehaviorProcessor.create<BinDetail>()

    @JvmField
    val listLiveData: LiveData<List<IncidentalListItem>> = bin
        .distinctUntilChanged()
        .switchMap {
            repo.sortedSheetList(it.allocationNo, it.allocationRowNo)
        }
        .replayThenAutoConnect(disposableContainer)
        .toLiveData()

    fun setBinDetail(binDetail: BinDetail) {
        bin.offer(binDetail)
    }

    fun removeIncidental(item: IncidentalListItem) {
        vmScope.launch(Dispatchers.IO) {
            item.sheet.also {
                it.deleted = true
                repo.saveIncidentalHeader(it)
            }
        }
    }
}
