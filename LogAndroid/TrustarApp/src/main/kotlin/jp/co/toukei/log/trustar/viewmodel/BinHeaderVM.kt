package jp.co.toukei.log.trustar.viewmodel

import io.reactivex.rxjava3.core.Flowable
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.common.CommonViewModel
import jp.co.toukei.log.trustar.repo.BinHeaderRepo
import jp.co.toukei.log.trustar.user.LoggedUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BinHeaderVM(
    val user: LoggedUser,
    val allocationNo: String,
) : CommonViewModel() {

    private val repo: BinHeaderRepo = BinHeaderRepo(user.userDB)

    val detail = Flowable.defer {
        repo.selectBinHeaderWithStatus(allocationNo)
    }


    fun setOutgoing(kilometer: Int?) {
        vmScope.launch(Dispatchers.IO) {
            repo.setOutgoingMeter(allocationNo, kilometer)
            Current.syncBin()
        }
    }

    fun setIncoming(kilometer: Int?) {
        vmScope.launch(Dispatchers.IO) {
            repo.setIncomingMeter(allocationNo, kilometer)
            Current.syncBin()
        }
    }

}
