package jp.co.toukei.log.trustar.repo

import android.location.Location
import io.reactivex.rxjava3.core.Flowable
import jp.co.toukei.log.lib.inTransaction
import jp.co.toukei.log.lib.orElseNull
import jp.co.toukei.log.trustar.compose.ComposeData
import jp.co.toukei.log.trustar.db.user.db.UserDB
import jp.co.toukei.log.trustar.db.user.embedded.BinHeaderAndStatus
import jp.co.toukei.log.trustar.db.user.entity.BinHeader
import jp.co.toukei.log.trustar.enum.BinStatusEnum
import jp.co.toukei.log.trustar.other.Weather
import java.util.Optional

class BinHeaderRepo(private val userDb: UserDB) {

    /**
     * ダッシュボードのデータ
     */
    fun list(): Flowable<List<BinHeaderAndStatus>> {
        return userDb.binHeaderDao().selectAllWithStatus()
    }

    fun anyBinHeader(status: BinStatusEnum): BinHeader? {
        return userDb.binHeaderDao().selectOneByStatus(status.value).orElseNull()
    }

    /**
     * 運行開始
     */
    fun startBin(
        allocationNo: String,
        truck: ComposeData.TruckKun,
        kilometer: Optional<Int>?,
        startLocation: Location?,
    ) {
        userDb.inTransaction {
            binHeaderDao().apply {
                find(allocationNo)?.apply {
                    truckCd = truck.truckCd
                    if (kilometer != null) {
                        outgoingMeter = kilometer.orElseNull()
                    }
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
        allocationNo: String,
        weather: Weather,
        endLocation: Location?,
    ) {
        userDb.inTransaction {
            binDetailDao().unsetMoving(allocationNo)
            binHeaderDao().apply {
                find(allocationNo)?.apply {
                    endBin(weather, endLocation)
                    updateOrIgnore(this)
                }
            }
        }
    }

    fun selectBinHeaderWithStatus(allocationNo: String): Flowable<Optional<BinHeaderAndStatus>> {
        return userDb.binHeaderDao().selectBinHeaderWithStatus(allocationNo)
    }

    /**
     * 出庫メーター
     */
    fun setOutgoingMeter(
        allocationNo: String,
        kilometer: Int?,
    ) {
        userDb.inTransaction {
            binHeaderDao().apply {
                find(allocationNo)?.apply {
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
        allocationNo: String,
        kilometer: Int?,
    ) {
        userDb.inTransaction {
            binHeaderDao().apply {
                find(allocationNo)?.apply {
                    incomingMeter = kilometer
                    updateOrIgnore(this)
                }
            }
        }
    }
}
