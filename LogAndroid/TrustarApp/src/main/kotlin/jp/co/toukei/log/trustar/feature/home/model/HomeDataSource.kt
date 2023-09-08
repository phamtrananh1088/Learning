package jp.co.toukei.log.trustar.feature.home.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.toLiveData
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.DisposableContainer
import io.reactivex.rxjava3.processors.BehaviorProcessor
import jp.co.toukei.log.lib.bothNotNull
import jp.co.toukei.log.lib.mapOrElseNull
import jp.co.toukei.log.lib.observeOnIO
import jp.co.toukei.log.lib.orElseNull
import jp.co.toukei.log.lib.replayThenAutoConnect
import jp.co.toukei.log.lib.util.Combine2LiveData
import jp.co.toukei.log.trustar.db.user.embedded.BinDetailAndStatus
import jp.co.toukei.log.trustar.db.user.embedded.BinHeaderAndStatus
import jp.co.toukei.log.trustar.db.user.entity.BinDetail
import jp.co.toukei.log.trustar.db.user.entity.BinHeader
import jp.co.toukei.log.trustar.db.user.entity.Work
import jp.co.toukei.log.trustar.isWorking
import jp.co.toukei.log.trustar.isWorkingOrMoving
import jp.co.toukei.log.trustar.other.WorkBin
import jp.co.toukei.log.trustar.repo.AioRepository
import java.util.Optional

class HomeDataSource(
    disposableContainer: DisposableContainer,
    aioRepository: AioRepository,
) {

    private val binHeaderRepo = aioRepository.binHeaderRepo
    private val binDetailRepo = aioRepository.binDetailRepo

    private val binHeaders = binHeaderRepo.list()
        .observeOnIO()
        .replayThenAutoConnect(disposableContainer)

    private val works = aioRepository.workRepo.workList()
        .observeOnIO()
        .replayThenAutoConnect(disposableContainer)

    private val selectBinHeader = BehaviorProcessor.create<String>()
    private val selectWorkBin = BehaviorProcessor.create<Optional<WorkBin>>()

    private val binHeaderSelected: Flowable<Optional<BinHeaderAndStatus>> = selectBinHeader
        .onBackpressureLatest()
        .observeOnIO(1)
        .distinctUntilChanged()
        .switchMap(binHeaderRepo::selectBinHeaderWithStatus)
        .replayThenAutoConnect(disposableContainer)

    private val binDetailListOfSelected = binHeaderSelected
        .onBackpressureLatest()
        .observeOnIO(1)
        .map { Optional.ofNullable(it.orElseNull()?.header?.allocationNo) }
        .distinctUntilChanged()
        .switchMap {
            it.orElseNull()
                ?.let(binDetailRepo::binDetailWithStatusList)
                ?: Flowable.just(emptyList())
        }
        .replayThenAutoConnect(disposableContainer)

    private val startedBinDetailListOfSelected = binDetailListOfSelected
        .onBackpressureLatest()
        .observeOnIO(1)
        .map { it.filter { l -> l.detail.statusType.isWorkingOrMoving() } }
        .replayThenAutoConnect(disposableContainer)

    private val currentWorkBin = selectWorkBin
        .onBackpressureLatest()
        .observeOnIO(1)
        .distinctUntilChanged { t1, t2 ->
            val b1 = t1.orElseNull()
            val b2 = t2.orElseNull()
            b1 != null && b2 != null && b1.sameItem(b2)
        }
        .switchMap<Optional<WorkBin>> {
            when (val bin = it.orElseNull()) {
                is WorkBin.Bin -> {
                    val b = bin.binDetail
                    binDetailRepo.findBinDetail(b.allocationNo, b.allocationRowNo)
                        .map { n -> Optional.ofNullable(WorkBin.Bin(n)) }
                }

                else -> Flowable.just(it)
            }
        }
        .replayThenAutoConnect(disposableContainer)


    @JvmField
    val workList: LiveData<List<Work>> = works.toLiveData()


    /**
     * ダッシュボードBinHeaderリスト、すべて
     */
    @JvmField
    val binHeaderList: LiveData<List<BinHeaderAndStatus>> = binHeaders.toLiveData()

    /**
     * BinHeaderを選択
     */
    fun selectBinHeader(binHeader: BinHeader) {
        selectBinHeader.onNext(binHeader.allocationNo)
    }

    /**
     * 配送計画一覧
     */
    @JvmField
    val selectedBinHeader: LiveData<BinHeaderAndStatus?> =
        binHeaderSelected.toLiveData().mapOrElseNull()

    /**
     * 配送計画一覧BinDetailリスト
     */
    val binDetailListOfSelectedBinHeader: LiveData<List<BinDetailAndStatus>> =
        binDetailListOfSelected.toLiveData()

    /**
     * 作業中BinDetail
     */
    val startedBinDetailListOfSelectedBinHeader: LiveData<List<BinDetailAndStatus>> =
        startedBinDetailListOfSelected.toLiveData()

    /**
     * 追加できる？
     */
    @JvmField
    val enableToAddWorkOfSelectedBinHeader: LiveData<BinHeaderAndStatus?> =
        binHeaderSelected.switchMap {
            val h = it.orElseNull()
            if (h != null && h.status.isWorking()) {
                binDetailListOfSelected.switchMap { l ->
                    if (l.none { f -> f.status.isWorking() }) Flowable.just(Optional.of(h))
                    else Flowable.just(Optional.empty())
                }
            } else {
                Flowable.just(Optional.empty())
            }
        }.toLiveData().mapOrElseNull()

    /**
     * BinDetailを選択
     */
    fun selectBinDetail(binDetail: BinDetail?) {
        if (binDetail == null) unsetAdd()
        else selectWorkBin.onNext(Optional.of(WorkBin.Bin(binDetail)))
    }

    /**
     * 追加作業
     */
    fun workAdd(bin: BinHeader) {
        selectWorkBin.onNext(Optional.of(WorkBin.Add.New(bin)))
    }

    /**
     * 追加作業、作業済から
     */
    fun workAdd(finished: BinDetail) {
        selectWorkBin.onNext(Optional.of(WorkBin.Add.Finished(finished)))
    }

    /**
     * 追加しなかった
     */
    fun unsetAdd() {
        selectWorkBin.onNext(Optional.empty())
    }

    /**
     * 今の作業
     * 追加作業 or 作業中BinDetail
     */
    @JvmField
    val currentWork: LiveData<WorkBin?> = currentWorkBin.toLiveData().mapOrElseNull()

    @JvmField
    val currentUnfinishedWork: LiveData<Pair<WorkBin, List<Work>>?> = Combine2LiveData(
        currentWork,
        workList,
        bothNotNull()
    )
}
