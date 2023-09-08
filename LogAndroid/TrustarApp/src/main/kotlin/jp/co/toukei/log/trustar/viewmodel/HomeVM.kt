package jp.co.toukei.log.trustar.viewmodel

import android.location.Location
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.processors.BehaviorProcessor
import jp.co.toukei.log.lib.blockingFirstOrNull
import jp.co.toukei.log.lib.flatMap
import jp.co.toukei.log.lib.mapOptionalOrEmpty
import jp.co.toukei.log.lib.observeOnIO
import jp.co.toukei.log.lib.observeOnUI
import jp.co.toukei.log.lib.orElseNull
import jp.co.toukei.log.lib.replayThenAutoConnect
import jp.co.toukei.log.lib.subscribe
import jp.co.toukei.log.lib.subscribeOnIO
import jp.co.toukei.log.lib.toResultWithLoading
import jp.co.toukei.log.lib.util.GetValue
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.common.CommonViewModel
import jp.co.toukei.log.trustar.compose.ComposeData
import jp.co.toukei.log.trustar.compose.VmEvent
import jp.co.toukei.log.trustar.db.user.embedded.BinHeaderAndStatus
import jp.co.toukei.log.trustar.db.user.entity.BinDetail
import jp.co.toukei.log.trustar.db.user.entity.BinHeader
import jp.co.toukei.log.trustar.db.user.entity.Work
import jp.co.toukei.log.trustar.db.user.entity.bin.Place
import jp.co.toukei.log.trustar.enum.BinStatusEnum
import jp.co.toukei.log.trustar.enum.EndBinState
import jp.co.toukei.log.trustar.enum.HomeNavi
import jp.co.toukei.log.trustar.feature.home.model.DetailNav
import jp.co.toukei.log.trustar.feature.home.model.OperateNav
import jp.co.toukei.log.trustar.repo.BinDetailRepo
import jp.co.toukei.log.trustar.repo.BinHeaderRepo
import jp.co.toukei.log.trustar.repo.FuelRepo
import jp.co.toukei.log.trustar.repo.NoticeRepo
import jp.co.toukei.log.trustar.repo.WorkRepo
import jp.co.toukei.log.trustar.repo.chat.ChatRoomRepository
import jp.co.toukei.log.trustar.user.LoggedUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import third.Result
import java.util.Optional
import java.util.concurrent.atomic.AtomicInteger

class HomeVM(
    val user: LoggedUser,
) : CommonViewModel() {
    private val binHeaderRepo: BinHeaderRepo = BinHeaderRepo(user.userDB)
    private val binDetailRepo: BinDetailRepo = BinDetailRepo(user.userDB)
    private val workRepo: WorkRepo = WorkRepo(user.userDB)
    private val fuelRepo: FuelRepo = FuelRepo(user)
    private val noticeRepo: NoticeRepo = NoticeRepo(user.userDB)
    private val chatRoomRepo = ChatRoomRepository()

    @JvmField
    val userInfo = user.userInfo

    @JvmField
    val chatRoomList = chatRoomRepo.sortedRoomList()
        .replayThenAutoConnect(disposableContainer)

    @JvmField
    val unreadImportant: Flowable<Int> = noticeRepo.countUnreadImportant()
        .observeOnIO()
        .distinctUntilChanged()
        .replayThenAutoConnect(disposableContainer)

    @JvmField
    val unreadNormal: Flowable<Int> = noticeRepo.countUnreadNormal()
        .observeOnIO()
        .distinctUntilChanged()
        .replayThenAutoConnect(disposableContainer)

    @JvmField
    val unreadMessageCount = chatRoomRepo.unreadCount().retry()

    @JvmField
    val selectedNavigateItem = mutableStateOf(HomeNavi.Dashboard)

    @JvmField
    val dashboardShowAll = mutableStateOf(true)

    @JvmField
    val binHeaderList: Flowable<List<BinHeaderAndStatus>> = binHeaderRepo.list()
        .observeOnIO()
        .replayThenAutoConnect(disposableContainer)


    @JvmField
    val workList: Flowable<List<Work>> = workRepo.workList()
        .observeOnIO()
        .replayThenAutoConnect(disposableContainer)

    @JvmField
    val fuelList = Flowable
        .defer { fuelRepo.fuelList() }
        .subscribeOnIO()
        .replayThenAutoConnect(disposableContainer)


    fun binHeader(allocationNo: String): Flowable<Optional<BinHeader>> {
        return binHeaderRepo.selectBinHeaderWithStatus(allocationNo)
            .mapOptionalOrEmpty { it.header }
    }

    fun binDetail(allocationNo: String, allocationRow: Int): Flowable<Optional<BinDetail>> {
        return binDetailRepo.findBinDetail(allocationNo, allocationRow)
    }


    fun refuelAmountOfBin(allocationNo: String, fuelClass: String): Flowable<Optional<Float>> {
        return fuelRepo.refuelAmountOfBin(allocationNo, fuelClass)
    }

    fun refuel(
        allocationNo: String,
        fuelCd: String,
        truckCd: String,
        quantity: Float,
        cost: Float,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            fuelRepo.refuel(
                allocationNo = allocationNo,
                truckCd = truckCd,
                fuelCd = fuelCd,
                quantity = quantity,
                cost = cost,
            )
            Current.syncFuel()
        }
    }

    @JvmField
    val reloadMessageRoomLiveData = MutableLiveData<Result<Unit>>()

    private val loadRoom = BehaviorProcessor.create<Boolean>()

    init {
        loadRoom.onBackpressureDrop()
            .flatMap(false, 1) {
                chatRoomRepo.reloadRooms()
                    .toSingleDefault(Unit)
                    .toResultWithLoading(200)
                    .subscribeOnIO()
            }
            .observeOnUI()
            .subscribe(reloadMessageRoomLiveData)
            .disposeOnClear()
    }

    fun reloadMessageRoom() {
        loadRoom.onNext(false)
    }


    val msgFlow = MutableSharedFlow<VmEvent>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    @JvmField
    val fetchBinDataState = mutableStateOf(false)
    fun fetchBinData() {
        fetchBinDataState.value = true
        Current.syncBin(true) {
            fetchBinDataState.value = false
            if (it.orElseNull() is GetValue.Error) {
                msgFlow.tryEmit(VmEvent.Msg(R.string.load_failed))
            }
        }
    }

    fun endBin(endBin: ComposeData.EndBin, setIncomingOrNot: Int?) {
        vmScope.launch(Dispatchers.IO) {
            val no = endBin.bin.allocationNo
            if (setIncomingOrNot != null) {
                binHeaderRepo.setIncomingMeter(no, setIncomingOrNot)
            }
            binHeaderRepo.endBin(no, endBin.weather, Current.lastLocation)
            endBinSync()
        }
    }

    val endStateSyncState = mutableStateOf<EndBinState?>(null)

    fun dismissBinSync() {
        endStateSyncState.value = null
    }

    fun endBinSync() {
        dismissBinSync()
        val job = vmScope.launch {
            delay(300)
            endStateSyncState.value = EndBinState.Loading
        }
        Current.syncBin {
            vmScope.launch(Dispatchers.IO) {
                job.cancelAndJoin()
                endStateSyncState.value = when (it.orElseNull()) {
                    is GetValue.Error -> EndBinState.Error
                    else -> null
                }
            }
        }
    }

    @JvmField
    val operateNav = OperateNav(binDetailRepo, disposableContainer)

    @JvmField
    val detailNav = DetailNav(user, binHeaderRepo, binDetailRepo, disposableContainer)

    fun startWork(
        allocationNo: String,
        allocationRowNo: Int,
        work: ComposeData.Work,
    ) {
        vmScope.launch(Dispatchers.IO) {
            val wr = workRepo.startWork(
                allocationNo,
                allocationRowNo, work, Current.lastLocation,
                true
            )
            operateNav.selectBinDetail(wr)
            Current.syncBin()
        }
    }

    fun startInAutoMode(binDetail: ComposeData.BinDetailRow, location: ComposeData.Location) {
        vmScope.launch(Dispatchers.IO) {
            val bd = binDetail.row
            val work = binDetail.work
            if (work != null) {
                workRepo.setLocation(bd, location)
                val l = Location(null).apply {
                    longitude = location.longitude
                    latitude = location.latitude
                    accuracy = location.accuracy
                }
                workRepo.startWork(
                    bd.allocationNo,
                    bd.allocationRowNo,
                    work,
                    l, true
                )
                Current.syncBin()
            }
        }
    }

    fun startNewAddWork(
        allocationNo: String,
        work: ComposeData.Work,
        place: Place,
    ) {
        vmScope.launch(Dispatchers.IO) {
            val wr = workRepo.startWork(
                allocationNo, place, work, Current.lastLocation
            )
            operateNav.selectBinDetail(wr)
            Current.syncBin()
        }
    }

    fun endWork(
        allocationNo: String,
        allocationRowNo: Int,
    ) {
        vmScope.launch(Dispatchers.IO) {
            workRepo.endWork(
                allocationNo,
                allocationRowNo,
                Current.lastLocation,
                false
            )
            val h = binHeaderRepo.selectBinHeaderWithStatus(allocationNo)
                .blockingFirst(Optional.empty()).orElseNull()?.header
            if (h != null && h.unplannedFlag) {
                operateNav.workAdd(h.allocationNo)
            } else {
                operateNav.selectBinDetail(null)
            }
            Current.syncBin()
        }
    }

    private val menuIdAtomic = AtomicInteger(0)

    /**
     * Menuを選択
     */
    fun postMenuId(homeNavi: HomeNavi, goDefault: Boolean = true) {
        val id = menuIdAtomic.incrementAndGet()
        when (homeNavi) {
            HomeNavi.Dashboard -> menu(homeNavi, id)
            HomeNavi.Detail -> {
                vmScope.launch(Dispatchers.Main.immediate) {
                    val working = if (goDefault) {
                        withContext(Dispatchers.IO) {
                            binHeaderRepo.anyBinHeader(BinStatusEnum.Working)
                        }
                    } else null

                    when {
                        working != null -> {
                            if (id == menuIdAtomic.get())
                                navigationBinHeader(working.allocationNo)
                        }

                        detailNav.selectedBinHeader.value != null -> menu(homeNavi, id)
                    }
                }
            }

            HomeNavi.Operate -> {
                if (!detailNav.inAutoMode.value) {
                    vmScope.launch(Dispatchers.Main.immediate) {
                        val working = if (goDefault) {
                            withContext(Dispatchers.IO) {
                                detailNav.startedBinDetailList.value?.firstOrNull()
                            }
                        } else null
                        when {
                            working != null -> {
                                if (id == menuIdAtomic.get()) {
                                    val b = working.detail.run {
                                        ComposeData.BinRow(allocationNo, allocationRowNo)
                                    }
                                    navigationBinDetail(b)
                                }
                            }

                            operateNav.currentWork.value != null -> menu(homeNavi, id)
                        }
                    }
                }
            }

            HomeNavi.Fuel -> menu(homeNavi, id)
            HomeNavi.Message -> menu(homeNavi, id)
        }
    }

    private fun menu(homeNavi: HomeNavi, id: Int) {
        if (id == menuIdAtomic.get()) {
            selectedNavigateItem.value = homeNavi
            when (homeNavi) {
                HomeNavi.Dashboard,
                HomeNavi.Detail,
                HomeNavi.Operate,
                -> {
                    Current.syncBin(false)
                }

                else -> {}
            }
        }
    }

    fun navigationBinHeader(allocationNo: String) {
        val id = menuIdAtomic.incrementAndGet()
        vmScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                detailNav.select(allocationNo)
            }
            menu(HomeNavi.Detail, id)
        }
    }

    fun navigationBinDetail(b: ComposeData.BinRow) {
        val id = menuIdAtomic.incrementAndGet()
        vmScope.launch(Dispatchers.IO) {
            val binDetail = binDetailRepo
                .findBinDetail(b.allocationNo, b.allocationRowNo)
                .blockingFirstOrNull()
                ?.orElseNull()
            if (binDetail != null) {
                val status = binDetail.workStatus
                val finished = status.isFinished()
                if (finished) operateNav.workAdd(binDetail)
                else {
                    if (status.isNew()) {
                        workRepo.moveWork(binDetail)
                        Current.syncBin()
                    }
                    operateNav.selectBinDetail(binDetail)
                }
                withContext(Dispatchers.Main) {
                    menu(HomeNavi.Operate, id)
                }
            }
        }
    }

    fun navigationWorkAddNew(allocationNo: String) {
        val id = menuIdAtomic.incrementAndGet()
        vmScope.launch(Dispatchers.Main) {
            operateNav.workAdd(allocationNo)
            menu(HomeNavi.Operate, id)
        }
    }

    fun noticeMarkRead(rank: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            noticeRepo.markRead(rank)
            Current.syncNotice()
        }
    }
}
