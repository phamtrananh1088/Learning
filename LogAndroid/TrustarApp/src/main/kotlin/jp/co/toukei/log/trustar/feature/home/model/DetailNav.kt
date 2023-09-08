package jp.co.toukei.log.trustar.feature.home.model

import android.location.Location
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.DisposableContainer
import io.reactivex.rxjava3.processors.BehaviorProcessor
import jp.co.toukei.log.lib.addTo
import jp.co.toukei.log.lib.distinctUntilOptionalChanged
import jp.co.toukei.log.lib.mapOptionalOrEmpty
import jp.co.toukei.log.lib.optional
import jp.co.toukei.log.lib.orElseNull
import jp.co.toukei.log.lib.replayThenAutoConnect
import jp.co.toukei.log.lib.switchMapOptionalOrEmpty
import jp.co.toukei.log.trustar.compose.ComposeData
import jp.co.toukei.log.trustar.db.user.embedded.BinDetailAndStatus
import jp.co.toukei.log.trustar.db.user.embedded.BinHeaderAndStatus
import jp.co.toukei.log.trustar.repo.BinDetailRepo
import jp.co.toukei.log.trustar.repo.BinHeaderRepo
import jp.co.toukei.log.trustar.user.LoggedUser
import java.util.Optional

class DetailNav(
    private val user: LoggedUser,
    private val binHeaderRepo: BinHeaderRepo,
    private val binDetailRepo: BinDetailRepo,
    dc: DisposableContainer
) {
    private val selectBinHeader = BehaviorProcessor.create<Optional<String>>()

    fun select(allocationNo: String?) {
        selectBinHeader.onNext(allocationNo.optional())
    }

    private val selected: Flowable<Optional<BinHeaderAndStatus>> =
        selectBinHeader
            .distinctUntilOptionalChanged()
            .switchMapOptionalOrEmpty(binHeaderRepo::selectBinHeaderWithStatus)
            .replayThenAutoConnect(dc)

    private val binDetailListOfSelected = selected
        .mapOptionalOrEmpty { it.header.allocationNo }
        .distinctUntilOptionalChanged()
        .switchMap {
            it.orElseNull()
                ?.let(binDetailRepo::binDetailWithStatusList)
                ?: Flowable.just(emptyList())
        }
        .replayThenAutoConnect(dc)

    fun binDetailListFlowable(): Flowable<List<ComposeData.BinDetailRow>> {
        return Flowable.combineLatest(
            binDetailListOfSelected,
            user.binLocationTask.throttleLatestRecord(2)
                .switchMapSingle {
                    if (it.running) {
                        val l = it.location
                        if (l == null) Single.never()
                        else Single.just(l.optional())
                    } else {
                        Single.just(Optional.empty<Location>())
                    }
                }
                .concatWith(Flowable.just(Optional.empty()))
                .startWithItem(Optional.empty()),
        ) { t1, t2 ->
            val l = t2.orElseNull()
            t1.map {
                ComposeData.BinDetailRow.fromBinDetail(it.detail, it.status, l)
            }
        }
    }

    private val mSelectedBinHeader = mutableStateOf<BinHeaderAndStatus?>(null)

    @JvmField
    val selectedBinHeader: State<BinHeaderAndStatus?> = mSelectedBinHeader

    private val mStartedBinDetailList = mutableStateOf<List<BinDetailAndStatus>?>(null)

    @JvmField
    val startedBinDetailList: State<List<BinDetailAndStatus>?> = mStartedBinDetailList

    private val mInAutoMode = mutableStateOf(false)

    @JvmField
    val inAutoMode: State<Boolean> = mInAutoMode


    private val mEnableAddWorkBin = mutableStateOf(false)

    @JvmField
    val enableAddWorkBin: State<Boolean> = mEnableAddWorkBin


    init {
        selected.subscribe {
            mSelectedBinHeader.value = it.orElseNull()
        }.addTo(dc)

        binDetailListOfSelected
            .map { it.filter { l -> l.detail.workStatus.isWorkingOrMoving() } }
            .subscribe {
                mStartedBinDetailList.value = it
            }
            .addTo(dc)

        val e = selected
            .switchMap { b ->
                val n = b.orElseNull()?.header?.allocationNo
                if (n == null) {
                    Flowable.just(false)
                } else {
                    user.binLocationTask.inAutoModeBin
                        .startWithItem(Optional.empty())
                        .switchMap { a ->
                            Flowable.just(a.orElseNull() == n)
                        }
                }
            }
            .distinctUntilChanged()
            .replay(1)
            .refCount()

        e.subscribe { mInAutoMode.value = it }.addTo(dc)

        selected
            .switchMap { b ->
                val h = b.orElseNull()
                if (h != null && h.header.binStatus.isWorking()) {
                    binDetailListOfSelected.switchMap { l ->
                        if (l.none { f -> f.detail.workStatus.isWorking() }) {
                            e
                        } else {
                            Flowable.just(true)
                        }
                    }
                } else {
                    Flowable.just(true)
                }
            }
            .subscribe {
                mEnableAddWorkBin.value = !it
            }
            .addTo(dc)
    }
}
