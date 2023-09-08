package jp.co.toukei.log.trustar.feature.collect.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.toLiveData
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.processors.BehaviorProcessor
import jp.co.toukei.log.lib.subscribeOnIO
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.common.CommonViewModel
import jp.co.toukei.log.trustar.db.user.entity.BinDetail
import jp.co.toukei.log.trustar.db.user.entity.CollectionGroup
import jp.co.toukei.log.trustar.db.user.entity.CollectionResult
import jp.co.toukei.log.trustar.repo.CollectionRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CollectFragmentVM : CommonViewModel() {

    private val repo = CollectionRepo(Current.userDatabase)
    private val rx = BehaviorProcessor.create<BinDetail>()

    class Row(
        @JvmField val result: CollectionResult.Row,
    ) {

        @JvmField
        var name = result.collectionNm.orEmpty()

        @JvmField
        val expectedQuantity = result.expectedQuantity

        @JvmField
        var actualQuantity = result.actualQuantity
    }

    private val added = BehaviorProcessor.createDefault<MutableList<Row>>(ArrayList())

    private val collections: Flowable<List<Pair<CollectionGroup, List<Row>>>> =
        Flowable.combineLatest(
            repo.collectionItems(),
            Flowable.combineLatest(
                added,
                rx.map { b ->
                    val results = repo.collectionResults(b.allocationNo, b.allocationRowNo)
                        ?.rows() ?: emptyList()
                    results.map(::Row)
                }
            ) { t1, t2 -> (t1 + t2).sortedBy { it.result.displayOrder } }
        ) { g, t ->
            val gs = t.groupBy { it.result.collectionClass }
            g.associateWith { gs[it.collectionClass] ?: emptyList() }
                .toList()
                .sortedBy { it.first.displayOrder }
        }.subscribeOnIO()

    @JvmField
    val collectionsLiveData: LiveData<List<Pair<CollectionGroup, List<Row>>>> =
        collections.toLiveData()

    fun setBinDetail(binDetail: BinDetail) {
        rx.onNext(binDetail)
    }

    fun addRow(name: String, group: CollectionGroup) {
        val v = added.value ?: ArrayList()
        v += Row(
            CollectionResult.Row(
                null,
                null,
                name,
                0.0,
                0.0,
                group.collectionClass,
                System.currentTimeMillis().toInt() and Int.MAX_VALUE
            )
        )
        added.onNext(v)
    }

    suspend fun save() {
        val b = rx.value ?: return
        val c = collectionsLiveData.value ?: return
        withContext(Dispatchers.IO) {
            val l = c.flatMap { it.second }.map { it.result.newRow(it.actualQuantity, it.name) }
            val r = CollectionResult(b.allocationNo, b.allocationRowNo, l)
            r.recordChanged()
            repo.saveResult(r)
        }
        Current.syncCollection()
    }
}
