package jp.co.toukei.log.trustar.repo

import io.reactivex.rxjava3.core.Flowable
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.db.result.entity.CommonRefuel
import jp.co.toukei.log.trustar.db.user.entity.Fuel
import jp.co.toukei.log.trustar.user.LoggedUser
import java.util.Optional

class FuelRepo(private val user: LoggedUser) {

    fun fuelList(): Flowable<List<Fuel>> {
        return user.userDB.fuelDao().list()
    }

    private val resultDb = user.resultDb

    fun refuel(
        allocationNo: String,
        truckCd: String,
        fuelCd: String,
        quantity: Float,
        cost: Float,
    ) {
        val k = CommonRefuel(
            userInfo = user.userInfo,
            allocationNo = allocationNo,
            truckCd = truckCd,
            fuelCd = fuelCd,
            quantity = quantity,
            cost = cost,
            location = Current.lastLocation
        )
        resultDb.commonRefuelDao().insert(k)
    }

    fun refuelAmountOfBin(allocationNo: String, fuelClass: String): Flowable<Optional<Float>> {
        val userInfo = user.userInfo
        return resultDb.commonRefuelDao()
            .getTotalAmount(userInfo.companyCd, allocationNo, fuelClass)
    }

}
