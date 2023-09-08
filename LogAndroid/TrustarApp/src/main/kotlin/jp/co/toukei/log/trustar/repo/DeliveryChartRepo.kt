package jp.co.toukei.log.trustar.repo

import io.reactivex.rxjava3.core.Flowable
import jp.co.toukei.log.lib.switchMapIfOptionalEmpty
import jp.co.toukei.log.trustar.db.AllocationRow
import jp.co.toukei.log.trustar.db.user.db.UserDB
import jp.co.toukei.log.trustar.db.user.entity.BinDetail
import jp.co.toukei.log.trustar.db.user.entity.DeliveryChart
import java.util.Optional

class DeliveryChartRepo(private val userDB: UserDB) {

    private val dao = userDB.deliveryChartDao()


    fun save(
        deliveryChart: DeliveryChart
    ) {
        dao.insertOrReplace(deliveryChart)
    }

    fun findChart(binDetail: BinDetail): Flowable<Optional<DeliveryChart>> {
        return binDetail.run {
            run { place.cd?.let(dao::selectByPlaceCd) ?: Flowable.just(Optional.empty()) }
                .switchMapIfOptionalEmpty {
                    chartCd?.let(dao::selectByChartCd)
                }
                .switchMapIfOptionalEmpty {
                    dao.selectByAllocationRow(AllocationRow(allocationNo, allocationRowNo))
                }
        }
    }
}
