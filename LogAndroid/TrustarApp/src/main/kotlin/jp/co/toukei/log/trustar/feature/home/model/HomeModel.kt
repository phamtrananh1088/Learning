package jp.co.toukei.log.trustar.feature.home.model

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map
import androidx.lifecycle.toLiveData
import io.reactivex.rxjava3.processors.BehaviorProcessor
import jp.co.toukei.log.lib.asEvent
import jp.co.toukei.log.lib.observeOnComputation
import jp.co.toukei.log.lib.orElseNull
import jp.co.toukei.log.lib.switchMapNullable
import jp.co.toukei.log.lib.util.Combine2LiveData
import jp.co.toukei.log.trustar.Config
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.common.CommonViewModel
import jp.co.toukei.log.trustar.db.user.embedded.BinDetailAndStatus
import jp.co.toukei.log.trustar.db.user.entity.BinDetail
import jp.co.toukei.log.trustar.db.user.entity.BinHeader
import jp.co.toukei.log.trustar.db.user.entity.BinStatus
import jp.co.toukei.log.trustar.db.user.entity.Work
import jp.co.toukei.log.trustar.db.user.entity.WorkStatus
import jp.co.toukei.log.trustar.db.user.entity.bin.BinWork
import jp.co.toukei.log.trustar.db.user.entity.bin.Place
import jp.co.toukei.log.trustar.hasFinished
import jp.co.toukei.log.trustar.other.WorkBin
import jp.co.toukei.log.trustar.ready
import jp.co.toukei.log.trustar.repo.chat.ChatRoomRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import third.Event
import java.util.Optional
import java.util.concurrent.atomic.AtomicInteger

class HomeModel : CommonViewModel() {

    @JvmField
    val repo = Current.userRepository

    private val binHeaderRepo = repo.binHeaderRepo
    private val binDetailRepo = repo.binDetailRepo
    private val workRepo = repo.workRepo

    @JvmField
    val dataSource = HomeDataSource(disposableContainer, repo)

    fun startWork(binDetail: BinDetail, work: Work, location: Location?) {
        vmScope.launch(Dispatchers.IO) {
            val wr =
                workRepo.startWork(binDetail, BinWork(work.workCd, work.workNm), location, true)
            dataSource.selectBinDetail(wr)
            Current.syncBin()
        }
    }

    fun startInAutoMode(binDetailAndStatus: BinDetailAndStatus, location: Location) {
        vmScope.launch(Dispatchers.IO) {
            val bd = binDetailAndStatus.detail
            val work = bd.work
            if (work != null) {
                workRepo.setLocation(bd, location)
                workRepo.startWork(bd, work, location, true)
                Current.syncBin()
            }
        }
    }

    fun startNewAddWork(binHeader: BinHeader, work: Work, place: Place, location: Location?) {
        vmScope.launch(Dispatchers.IO) {
            val wr =
                workRepo.startWork(binHeader, place, BinWork(work.workCd, work.workNm), location)
            dataSource.selectBinDetail(wr)
            Current.syncBin()
        }
    }

    /**
     * 作業終了
     */
    fun endWork(binDetail: BinDetail) {
        vmScope.launch(Dispatchers.IO) {
            workRepo.endWork(binDetail, Current.lastLocation, false)
            val h = binHeaderRepo.selectBinHeaderWithStatus(binDetail.allocationNo)
                .blockingFirst(Optional.empty()).orElseNull()?.header
            if (h != null && h.unplannedFlag) {
                dataSource.workAdd(h)
            } else {
                dataSource.selectBinDetail(null)
            }
            Current.syncBin()
        }
    }

    @JvmField
    val working: LiveData<WorkBin?> = dataSource.currentWork

    @JvmField
    val inAutoMode: LiveData<out Boolean?> = dataSource.selectedBinHeader
        .switchMapNullable { h ->
            val an = h?.header?.allocationNo
            Current.loggedUser.binLocationTask.inAutoModeBin.map { it == an }
        }

    @JvmField
    val enableAddWorkBin =
        Combine2LiveData(dataSource.enableToAddWorkOfSelectedBinHeader, inAutoMode) { a, b ->
            a != null && b != true
        }

    /**
     * Concurrency防止
     */
    private val menuIdAtomic = AtomicInteger(0)

    /**
     * Menuを選択
     */
    fun postMenuId(menuId: Int, goDefault: Boolean = true) {
        val id = menuIdAtomic.incrementAndGet()
        when (menuId) {
            R.string.home_navigation_dashboard -> menu(menuId, id)
            R.string.home_navigation_deliver -> {
                vmScope.launch(Dispatchers.Main) {
                    val working = if (goDefault) {
                        withContext(Dispatchers.IO) {
                            binHeaderRepo.anyBinHeader(BinStatus.Type.Working)
                        }
                    } else null

                    when {
                        working != null -> {
                            if (id == menuIdAtomic.get())
                                navigationBinHeader(working)
                        }

                        dataSource.selectedBinHeader.value != null -> menu(menuId, id)
                    }
                }
            }

            R.string.home_navigation_operation -> {
                if (inAutoMode.value != true) {
                    vmScope.launch(Dispatchers.Main) {
                        val working = if (goDefault) {
                            withContext(Dispatchers.IO) {
                                dataSource.startedBinDetailListOfSelectedBinHeader.value?.firstOrNull()
                            }
                        } else null
                        when {
                            working != null -> {
                                if (id == menuIdAtomic.get())
                                    navigationBinDetail(working.detail)
                            }

                            dataSource.currentUnfinishedWork.value != null -> menu(menuId, id)
                        }
                    }
                }
            }

            R.string.home_navigation_refuel -> menu(menuId, id)
            R.string.navigation_message -> menu(menuId, id)
        }
    }

    private val _selectedNavMenu = MutableLiveData<Int>()

    /**
     * Menuを選択した
     */
    @JvmField
    val selectedNavMenu: LiveData<Event<Int>> = _selectedNavMenu.distinctUntilChanged().map {
        it.asEvent()
    }

    private fun menu(menuId: Int, id: Int) {
        if (id == menuIdAtomic.get()) {
            _selectedNavMenu.value = menuId
        }
    }

    fun lazyNavigationByAllocationNo(allocationNo: String) {
        vmScope.launch(Dispatchers.IO) {
            val o = binHeaderRepo.selectBinHeaderWithStatus(allocationNo)
                .blockingFirst(Optional.empty()).orElseNull()
            if (o != null) navigationBinHeader(o.header)
        }
    }

    fun navigationBinHeader(bin: BinHeader) {
        val id = menuIdAtomic.incrementAndGet()
        vmScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                dataSource.selectBinHeader(bin)
            }
            menu(R.string.home_navigation_deliver, id)
        }
    }

    fun navigationBinDetail(binDetail: BinDetail) {
        val id = menuIdAtomic.incrementAndGet()
        vmScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                val status = binDetail.statusType
                val finished = status.hasFinished()
                if (finished) dataSource.workAdd(binDetail)
                else {
                    if (status.ready()) {
                        workRepo.moveWork(binDetail)
                        Current.syncBin()
                    }
                    dataSource.selectBinDetail(binDetail)
                }
            }
            menu(R.string.home_navigation_operation, id)
        }
    }

    fun navigationWorkAddNew(bin: BinHeader) {
        val id = menuIdAtomic.incrementAndGet()
        vmScope.launch(Dispatchers.Main) {
            dataSource.workAdd(bin)
            menu(R.string.home_navigation_operation, id)
        }
    }

    @JvmField
    var lastSelectedItemId: Int = -1

    var askedForGPS = false
    var askedForBattery = false
    var notAskedForLocation = true
    var notAskedForLocationBg = true

    @JvmField
    val showAutoModeDialog = MutableLiveData<Boolean>()


    private val lastMillis = BehaviorProcessor.createDefault(System.currentTimeMillis())

    @JvmField
    val lastTimeMillisLiveData: LiveData<out Long> = lastMillis.observeOnComputation().toLiveData()

    @JvmField
    val lastFormattedDataLiveData: LiveData<out String> = lastTimeMillisLiveData.map {
        Config.dateFormatterMMdde.format(it)
    }

    fun updateSystemDate(timeMillis: Long) {
        lastMillis.onNext(timeMillis)
    }

    private val chatRepo = ChatRoomRepository()

    @JvmField
    val unreadMessageCount = chatRepo.unreadCount().retry().toLiveData()

    fun countBinDetail(status: WorkStatus.Type, allocationNo: String): Int {
        return runBlocking(Dispatchers.IO) {
            binDetailRepo.countByStatus(status, allocationNo)
        }
    }

    fun countBinHeader(status: BinStatus.Type): Int {
        return runBlocking(Dispatchers.IO) {
            binHeaderRepo.countByStatus(status)
        }
    }
}
