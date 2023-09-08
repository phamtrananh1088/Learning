package jp.co.toukei.log.trustar.feature.home.model

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.DisposableContainer
import io.reactivex.rxjava3.processors.BehaviorProcessor
import jp.co.toukei.log.lib.addTo
import jp.co.toukei.log.lib.mapOptionalOrEmpty
import jp.co.toukei.log.lib.orElseNull
import jp.co.toukei.log.trustar.db.user.entity.BinDetail
import jp.co.toukei.log.trustar.other.WorkBin
import jp.co.toukei.log.trustar.repo.BinDetailRepo
import java.util.Optional

class OperateNav(
    private val binDetailRepo: BinDetailRepo,
    dc: DisposableContainer
) {

    private val select = BehaviorProcessor.create<Optional<WorkBin>>()

    private val mCurrentWork = mutableStateOf<WorkBin?>(null)

    @JvmField
    val currentWork: State<WorkBin?> = mCurrentWork

    init {
        select
            .switchMap<Optional<WorkBin>> {
                when (val bin = it.orElseNull()) {
                    is WorkBin.Bin -> {
                        val b = bin.binDetail
                        binDetailRepo.findBinDetail(b.allocationNo, b.allocationRowNo)
                            .mapOptionalOrEmpty(WorkBin::Bin)
                    }

                    else -> Flowable.just(it)
                }
            }
            .subscribe {
                mCurrentWork.value = it.orElseNull()
            }
            .addTo(dc)
    }

    fun selectBinDetail(binDetail: BinDetail?) {
        if (binDetail == null) unsetAdd()
        else select.onNext(Optional.of(WorkBin.Bin(binDetail)))
    }

    fun workAdd(allocationNo: String) {
        select.onNext(Optional.of(WorkBin.New(allocationNo, "追加作業")))
    }

    fun workAdd(finished: BinDetail) {
        select.onNext(Optional.of(WorkBin.Bin(finished)))
    }

    fun unsetAdd() {
        select.onNext(Optional.empty())
    }

}
