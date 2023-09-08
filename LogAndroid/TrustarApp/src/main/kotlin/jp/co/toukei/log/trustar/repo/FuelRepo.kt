package jp.co.toukei.log.trustar.repo

import io.reactivex.rxjava3.core.Flowable
import jp.co.toukei.log.trustar.db.user.db.UserDB
import jp.co.toukei.log.trustar.db.user.entity.Fuel

class FuelRepo(private val userDB: UserDB) {

    fun fuelList(): Flowable<Array<Fuel>> {
        return userDB.fuelDao().list()
    }
}
