package jp.co.toukei.log.trustar.feature.nimachi.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.toLiveData
import jp.co.toukei.log.lib.Const
import jp.co.toukei.log.lib.createMediatorLiveData
import jp.co.toukei.log.lib.runOnComputation
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.common.CommonViewModel
import jp.co.toukei.log.trustar.db.user.entity.IncidentalWork
import jp.co.toukei.log.trustar.feature.nimachi.data.WorkItem
import java.util.concurrent.Callable
import java.util.concurrent.FutureTask

class WorkSelectVM : CommonViewModel() {

    private val all: LiveData<List<IncidentalWork>> = Current.userRepository.incidentalRepo
        .workList()
        .map { it.sortedBy(IncidentalWork::displayOrder) }
        .toLiveData()

    private val _listLiveData: MutableLiveData<List<WorkItem>?> = all.createMediatorLiveData {
        runOnComputation {
            postValue(it?.map { w -> WorkItem(w, selectedCd.contains(w.workCd)) })
        }
    }

    @JvmField
    val listLiveData: LiveData<List<WorkItem>?> = _listLiveData

    private val selectedCd = mutableSetOf<String>()

    fun report(workItem: WorkItem) {
        val cd = workItem.work.workCd
        if (workItem.selected) selectedCd.add(cd) else selectedCd.remove(cd)
        preCompute()
    }

    fun selectOnly(workCd: Collection<IncidentalWork?>?) {
        selectedCd.clear()
        workCd?.mapNotNullTo(selectedCd) { it?.workCd }
        preCompute()
    }

    private fun preCompute() {
        future = FutureTask(compute).apply {
            Const.computationExecutor.execute(this)
        }
    }

    fun getSelected(): List<IncidentalWork>? {
        return future?.get()
    }

    private var future: FutureTask<List<IncidentalWork>?>? = null

    private val compute: Callable<List<IncidentalWork>?> = Callable {
        _listLiveData.value?.mapNotNull { if (it.selected) it.work else null }
    }

}
