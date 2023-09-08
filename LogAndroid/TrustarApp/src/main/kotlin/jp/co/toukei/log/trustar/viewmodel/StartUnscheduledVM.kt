package jp.co.toukei.log.trustar.viewmodel

import android.location.Location
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import jp.co.toukei.log.lib.addTo
import jp.co.toukei.log.lib.observeOnIO
import jp.co.toukei.log.lib.subscribeOnIO
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.common.CommonViewModel
import jp.co.toukei.log.trustar.compose.ComposeData
import jp.co.toukei.log.trustar.db.user.embedded.LocationRecord
import jp.co.toukei.log.trustar.enum.BinStatusEnum
import jp.co.toukei.log.trustar.enum.StartUnscheduledBinState
import jp.co.toukei.log.trustar.repo.BinHeaderRepo
import jp.co.toukei.log.trustar.rest.post.UnscheduledBin
import jp.co.toukei.log.trustar.rest.response.UnscheduledBinAllocation
import jp.co.toukei.log.trustar.user.ClientInfo
import jp.co.toukei.log.trustar.user.LoggedUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StartUnscheduledVM(
    val user: LoggedUser,
) : CommonViewModel() {

    val meterInputEnabled = user.userInfo.meterInputEnabled

    private val repo: BinHeaderRepo = BinHeaderRepo(user.userDB)

    @JvmField
    val truckList: Flowable<List<ComposeData.TruckKun>> = user.userDB.truckDao()
        .selectAll()
        .map { l ->
            l.map { t ->
                ComposeData.TruckKun(t.truckCd, t.truckNm)
            }
        }

    private fun tokenWhenNotWorking(): Single<StartUnscheduledBinState> {
        return Current.onlineApi
            .getToken()
            .flatMap { token ->
                Current.rxSyncBin()
                    .observeOnIO()
                    .flatMap {
                        if (repo.anyBinHeader(BinStatusEnum.Working) == null) {
                            Single.just(StartUnscheduledBinState.Ready(token))
                        } else {
                            Single.just(StartUnscheduledBinState.WorkingBinExists())
                        }
                    }
                    .onErrorReturnItem(StartUnscheduledBinState.Ready(token)) //fallback
            }
            .subscribeOnIO()
    }

    val sus = mutableStateOf<StartUnscheduledBinState?>(null)

    fun fetchStartUnscheduledToken() {
        viewModelScope.launch(Dispatchers.IO) {
            tokenWhenNotWorking()
                .toFlowable()
                .startWithItem(StartUnscheduledBinState.TokenLoading())
                .subscribe(
                    { sus.value = it },
                    { sus.value = StartUnscheduledBinState.GetTokenError(it) }
                )
                .addTo(disposableContainer)
        }
    }

    fun startUnscheduledBin(
        token: String,
        truck: ComposeData.TruckKun,
        kilometer: Int?,
        location: Location?,
    ) {
        Current.onlineApi
            .startUnscheduledBin(
                UnscheduledBin(
                    token = token,
                    truck = truck,
                    locationRecord = LocationRecord(location, System.currentTimeMillis()),
                    clientInfo = ClientInfo(),
                    userInfo = user.userInfo,
                    outgoingMeter = kilometer
                )
            )
            .flatMap(UnscheduledBinAllocation::toSingle)
            .flatMap { a ->
                Current.rxSyncBin()
                    .map<StartUnscheduledBinState> { StartUnscheduledBinState.Added(a) }
                    .onErrorReturnItem(StartUnscheduledBinState.AddedButReloadFailed(a))
            }
            .toFlowable()
            .startWithItem(StartUnscheduledBinState.Sending())
            .subscribeOnIO()
            .subscribe(
                { sus.value = it },
                { sus.value = StartUnscheduledBinState.AddFailed() }
            )
            .addTo(disposableContainer)
    }
}
