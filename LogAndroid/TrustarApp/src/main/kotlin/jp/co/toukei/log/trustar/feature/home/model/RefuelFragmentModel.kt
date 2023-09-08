package jp.co.toukei.log.trustar.feature.home.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.toLiveData
import androidx.lifecycle.viewModelScope
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.processors.PublishProcessor
import jp.co.toukei.log.lib.*
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.common.CommonViewModel
import jp.co.toukei.log.trustar.db.user.entity.BinHeader
import jp.co.toukei.log.trustar.db.user.entity.Fuel
import jp.co.toukei.log.trustar.other.Refueled
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class RefuelFragmentModel : CommonViewModel() {

    private val binHeaderList = Flowable
        .defer { Current.userRepository.binHeaderRepo.list() }
        .subscribeOnIO()
        .replayThenAutoConnect(disposableContainer)

    @JvmField
    val binHeaderListLiveData = binHeaderList.toLiveData()

    private val selectBinHeader = PublishProcessor.create<BinHeader>()

    private val selectedBinHeader = Flowable
        .combineLatest(
            binHeaderList,
            selectBinHeader.observeOnIO().map { it.optional() }.startWithItem(Optional.empty())
        ) { l, ob ->
            val b = ob.orElseNull()
            val s = if (b == null) null else l.firstOrNull { it.header.sameItem(b) }
            s.optional()
        }
        .onBackpressureLatest()
        .distinctUntilChanged()
        .replayThenAutoConnect(disposableContainer)

    @JvmField
    val selectedBinHeaderLiveData = selectedBinHeader.toLiveData()

    private val fuelList = Flowable
        .defer { Current.userRepository.fuelRepo.fuelList() }
        .subscribeOnIO()
        .replayThenAutoConnect(disposableContainer)

    @JvmField
    val fuelListLiveData = fuelList.toLiveData()

    private val selectFuel = PublishProcessor.create<Fuel>()

    private val selectedFuelCd = Flowable
        .combineLatest(
            fuelList,
            selectFuel.observeOnIO().map { it.optional() }.startWithItem(Optional.empty())
        ) { l, of ->
            val f = of.orElseNull()
            val s = if (f == null) null else l.firstOrNull { it.fuelCd == f.fuelCd }
            s.optional()
        }
        .onBackpressureLatest()
        .distinctUntilChanged()
        .replayThenAutoConnect(disposableContainer)

    @JvmField
    val selectedFuelCdLiveData = selectedFuelCd.toLiveData()

    @JvmField
    val totalAmountOfSelected = Flowable
        .combineLatest(
            selectedBinHeader,
            selectedFuelCd
        ) { b, f ->
            val al = b.orElseNull()?.header?.allocationNo
            val fu = f.orElseNull()?.fuelCd
            if (al == null || fu == null) Optional.empty()
            else Pair(al, fu).optional()
        }
        .switchMap {
            val b = it.orElseNull()
            if (b == null) Flowable.just(Optional.empty())
            else Current.userRepository.kyuyuRepo
                .totalAmountOfCompany(b.first, b.second)
        }
        .subscribeOnIO()
        .replayThenAutoConnect(disposableContainer)
        .toLiveData()

    fun selectBinHeader(binHeader: BinHeader) {
        selectBinHeader.offer(binHeader)
    }

    fun selectFuel(fuel: Fuel) {
        selectFuel.offer(fuel)
    }

    fun refuel(refueled: Refueled) {
        viewModelScope.launch(Dispatchers.IO) {
            Current.userRepository.kyuyuRepo.refuel(refueled)
            Current.syncFuel()
        }
    }

    @JvmField
    val currentQuantityMutableLivaData = MutableLiveData<Float>()

    @JvmField
    val currentPaidMutableLivaData = MutableLiveData<Float>()
}
