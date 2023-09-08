package jp.co.toukei.log.trustar.feature.home.model

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.toLiveData
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.processors.BehaviorProcessor
import jp.co.toukei.log.lib.observeOnIO
import jp.co.toukei.log.lib.orElseNull
import jp.co.toukei.log.lib.replayThenAutoConnect
import jp.co.toukei.log.lib.util.GetValue
import jp.co.toukei.log.lib.util.Wrapper
import jp.co.toukei.log.lib.withValue
import jp.co.toukei.log.lib.wrap
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.common.CommonViewModel
import jp.co.toukei.log.trustar.db.user.embedded.BinHeaderAndStatus
import jp.co.toukei.log.trustar.db.user.entity.Truck
import jp.co.toukei.log.trustar.other.Weather
import jp.co.toukei.log.trustar.other.WorkMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import third.Result
import java.util.Optional

class BinHeaderModel : CommonViewModel() {

    private val repo = Current.userRepository.binHeaderRepo
    private val repo2 = Current.userRepository.binDetailRepo

    private val allocationNoRx = BehaviorProcessor.create<Wrapper<String?>>()

    private val header = allocationNoRx
        .observeOnIO()
        .distinctUntilChanged { t1, t2 -> t1.value == t2.value }
        .switchMap {
            val n = it.value
            if (n == null) Flowable.empty()
            else repo.selectBinHeaderWithStatus(n)
                .observeOnIO()
        }
        .replayThenAutoConnect(disposableContainer)

    @JvmField
    val binHeaderLiveData: LiveData<Optional<BinHeaderAndStatus>> = header.toLiveData()

    @JvmField
    val binList = header.switchMap {
        val n = it.orElseNull()
        if (n == null) Flowable.empty()
        else repo2.binDetailWithStatusList(n.header.allocationNo)
    }.toLiveData()

    fun setAllocationNo(allocationNo: String?) {
        allocationNoRx.onNext(allocationNo.wrap())
    }

    fun setOutgoing(kilometer: Int?) {
        binHeaderLiveData.value?.withValue {
            vmScope.launch(Dispatchers.IO) {
                repo.setOutgoingMeter(it.header, kilometer)
                Current.syncBin()
            }
        }
    }

    fun setIncoming(kilometer: Int?) {
        binHeaderLiveData.value?.withValue {
            vmScope.launch(Dispatchers.IO) {
                repo.setIncomingMeter(it.header, kilometer)
                Current.syncBin()
            }
        }
    }

    fun setTruck(truck: Truck) {
        binHeaderLiveData.value?.withValue {
            vmScope.launch(Dispatchers.IO) {
                repo.setTruck(it.header, truck)
                Current.syncBin()
            }
        }
    }

    fun start(workMode: WorkMode, location: Location?) {
        binHeaderLiveData.value?.withValue {
            vmScope.launch(Dispatchers.Main) {
                withContext(Dispatchers.IO) {
                    repo.startBin(it.header, location)
                }
                setAutoMode(workMode == WorkMode.Automatic)
                Current.syncBin()
            }
        }
    }

    /*
    * 0: closed
    * 1: loading
    * 2: result true
    * 3: result false
    */
    @JvmField
    val endState = MutableLiveData<Int>()

    fun endBin(weather: Weather, location: Location?, setIncomingOrNot: Int?) {
        binHeaderLiveData.value?.withValue {
            vmScope.launch(Dispatchers.IO) {
                if (setIncomingOrNot != null) repo.setIncomingMeter(it.header, setIncomingOrNot)
                repo.endBin(it.header, weather, location)
                sync()
            }
        }
    }

    fun sync(cancel: Boolean = false) {
        if (cancel) {
            endState.postValue(0)
            return
        }
        endState.postValue(1)
        Current.syncBin {
            val v = it.orElseNull()
            endState.postValue(if (v == null) 0 else if (v is GetValue.Value) 2 else 3)
        }
    }

    @JvmField
    val reloadState = MutableLiveData<Result<Unit>>()
    fun reload() {
        reloadState.postValue(Result.Loading)
        Current.syncBin(true) {
            reloadState.postValue(Result.Value(Unit))
        }
    }

    fun setAutoMode(state: Boolean) {
        val task = Current.loggedUser.binLocationTask
        if (state) {
            binHeaderLiveData.value?.withValue {
                task.setInAutoMode(it.header)
            }
        } else {
            task.cancelAutoMode()
        }
    }

}
