package jp.co.toukei.log.trustar.viewmodel

import android.location.Location
import io.reactivex.rxjava3.core.Flowable
import jp.co.toukei.log.trustar.common.CommonViewModel
import jp.co.toukei.log.trustar.compose.ComposeData
import jp.co.toukei.log.trustar.db.user.embedded.BinDetailAndStatus
import jp.co.toukei.log.trustar.db.user.entity.BinDetail
import jp.co.toukei.log.trustar.repo.BinDetailRepo
import jp.co.toukei.log.trustar.repo.BinHeaderRepo
import jp.co.toukei.log.trustar.user.LoggedUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.util.Optional

class StartBinVM(
    val user: LoggedUser,
    private val allocationNo: String,
) : CommonViewModel() {

    val meterInputEnabled = user.userInfo.meterInputEnabled
    val geofenceUseFlag = user.userInfo.geofenceUseFlag

    private val repo: BinHeaderRepo = BinHeaderRepo(user.userDB)
    private val repo2: BinDetailRepo = BinDetailRepo(user.userDB)

    private val truckDao = user.userDB.truckDao()

    @JvmField
    val truckList: Flowable<List<ComposeData.TruckKun>> = truckDao.selectAll().map { l ->
        l.map { t ->
            ComposeData.TruckKun(t.truckCd, t.truckNm)
        }
    }

    @JvmField
    val binDetailList: Flowable<List<BinDetail>> = Flowable.defer {
        repo2.binDetailWithStatusList(allocationNo).map {
            it.map(BinDetailAndStatus::detail)
        }
    }

    val data = Flowable.defer {
        repo.selectBinHeaderWithStatus(allocationNo)
    }

    fun startBin(
        allocationNo: String,
        truck: ComposeData.TruckKun,
        setKilometer: Optional<Int>?,
        location: Location?,
    ) {
        runBlocking(Dispatchers.IO) {
            repo.startBin(allocationNo, truck, setKilometer, location)
        }
    }

}
