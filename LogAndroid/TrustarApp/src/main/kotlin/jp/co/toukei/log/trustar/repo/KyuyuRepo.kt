package jp.co.toukei.log.trustar.repo

import io.reactivex.rxjava3.core.Flowable
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.db.result.entity.CommonKyuyu
import jp.co.toukei.log.trustar.other.Refueled
import jp.co.toukei.log.trustar.user.LoggedUser
import java.util.*

class KyuyuRepo(private val user: LoggedUser) {
    private val resultDb = user.resultDb
    fun refuel(refueled: Refueled) {
        val k = CommonKyuyu(
            user.userInfo,
            refueled,
            Current.lastLocation
        )
        resultDb.commonKyuyuDao().insert(k)
    }

    fun totalAmountOfCompany(allocationNo: String, fuelClass: String): Flowable<Optional<Float>> {
        val userInfo = user.userInfo
        return resultDb.commonKyuyuDao().getTotalAmount(userInfo.companyCd, allocationNo, fuelClass)
    }
}
