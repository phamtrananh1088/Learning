package jp.co.toukei.log.trustar.feature.home.model

import androidx.lifecycle.MutableLiveData
import jp.co.toukei.log.lib.observeOnIO
import jp.co.toukei.log.lib.orElseNull
import jp.co.toukei.log.lib.subscribeOnIO
import jp.co.toukei.log.lib.util.GetValue
import jp.co.toukei.log.trustar.Config
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.common.CommonViewModel
import jp.co.toukei.log.trustar.db.user.embedded.LocationRecord
import jp.co.toukei.log.trustar.db.user.entity.Truck
import jp.co.toukei.log.trustar.rest.post.UnscheduledBin
import jp.co.toukei.log.trustar.user.ClientInfo
import third.Result

class UnscheduledBinStartVM : CommonViewModel() {

    @JvmField
    val startState = MutableLiveData<Result<String>?>()

    //todo shitty code
    fun startUnscheduledBin(truck: Truck, outgoingMeter: Int? = null) {
        val user = Current.user ?: return

        if (startState.value is Result.Loading) return
        startState.value = Result.Loading

        Config.fetchApi
            .startUnscheduledBin(
                user.token,
                UnscheduledBin(
                    truck,
                    LocationRecord(Current.lastLocation, System.currentTimeMillis()),
                    ClientInfo(),
                    user.userInfo,
                    outgoingMeter
                )
            )
            .observeOnIO()
            .flatMap { it.toSingle() }
            .subscribeOnIO()
            .subscribe(
                { b ->
                    Current.syncBin(true) {
                        if (it.orElseNull() is GetValue.Value) startState.postValue(Result.Value(b))
                        else startState.postValue(null)
                    }
                },
                { startState.postValue(Result.Error(it)) }
            )
            .disposeOnClear()
    }

    fun clearError() {
        if (startState.value is Result.Error) startState.value = null
    }
}
