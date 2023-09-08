package jp.co.toukei.log.trustar.repo

import android.location.Location
import io.reactivex.rxjava3.core.Flowable
import jp.co.toukei.log.lib.inTransaction
import jp.co.toukei.log.lib.orElseNull
import jp.co.toukei.log.trustar.db.user.db.UserDB
import jp.co.toukei.log.trustar.db.user.embedded.BinHeaderAndStatus
import jp.co.toukei.log.trustar.db.user.entity.BinHeader
import jp.co.toukei.log.trustar.db.user.entity.BinStatus
import jp.co.toukei.log.trustar.db.user.entity.Truck
import jp.co.toukei.log.trustar.other.Weather
import java.util.*

class BinHeaderRepo(private val userDb: UserDB) {

    /**
     * ダッシュボードのデータ
     */
    fun list(): Flowable<List<BinHeaderAndStatus>> {
        return userDb.binHeaderDao().selectAllWithStatus()
    }

    fun anyBinHeader(status: BinStatus.Type): BinHeader? {
        return userDb.binHeaderDao().selectOneByStatus(status.value).orElseNull()
    }

    /**
     * 運行開始
     */
    fun startBin(
        binHeader: BinHeader,
        startLocation: Location?
    ) {
        userDb.inTransaction {
            binHeaderDao().apply {
                find(binHeader.allocationNo)?.apply {
                    startBin(startLocation)
                    updateOrIgnore(this)
                }
            }
        }
    }

    /**
     * 運行終了
     */
    fun endBin(
        binHeader: BinHeader,
        weather: Weather,
        endLocation: Location?
    ) {
        userDb.inTransaction {
            binDetailDao().unsetMoving(binHeader.allocationNo)
            binHeaderDao().apply {
                find(binHeader.allocationNo)?.apply {
                    endBin(weather, endLocation)
                    updateOrIgnore(this)
                }
            }
        }
    }

    /**
     * 車両設定
     */
    fun setTruck(
        binHeader: BinHeader,
        truck: Truck
    ) {
        userDb.inTransaction {
            binHeaderDao().apply {
                find(binHeader.allocationNo)?.apply {
                    truckCd = truck.truckCd
                    updateOrIgnore(this)
                }
            }
        }
    }

    fun selectBinHeaderWithStatus(allocationNo: String): Flowable<Optional<BinHeaderAndStatus>> {
        return userDb.binHeaderDao().selectBinHeaderWithStatus(allocationNo)
    }

    fun countByStatus(status: BinStatus.Type): Int {
        return userDb.binHeaderDao().countByStatus(status.value)
    }

    /**
     * 出庫メーター
     */
    fun setOutgoingMeter(
        binHeader: BinHeader,
        kilometer: Int?
    ) {
        userDb.inTransaction {
            binHeaderDao().apply {
                find(binHeader.allocationNo)?.apply {
                    outgoingMeter = kilometer
                    updateOrIgnore(this)
                }
            }
        }
    }

    /**
     * 入庫メーター
     */
    fun setIncomingMeter(
        binHeader: BinHeader,
        kilometer: Int?
    ) {
        userDb.inTransaction {
            binHeaderDao().apply {
                find(binHeader.allocationNo)?.apply {
                    incomingMeter = kilometer
                    updateOrIgnore(this)
                }
            }
        }
    }
}
