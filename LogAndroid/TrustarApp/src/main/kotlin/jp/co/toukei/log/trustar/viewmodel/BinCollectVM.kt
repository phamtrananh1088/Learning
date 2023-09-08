package jp.co.toukei.log.trustar.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.toLiveData
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.processors.BehaviorProcessor
import jp.co.toukei.log.lib.stripTrailingZerosString
import jp.co.toukei.log.lib.subscribeOnIO
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.common.CommonViewModel
import jp.co.toukei.log.trustar.db.user.entity.CollectionGroup
import jp.co.toukei.log.trustar.db.user.entity.CollectionResult
import jp.co.toukei.log.trustar.repo.CollectionRepo
import jp.co.toukei.log.trustar.user.LoggedUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

class BinCollectVM(
    val user: LoggedUser,
    private val allocationNo: String,
    private val allocationRowNo: Int,
) : CommonViewModel() {

    private val idCounter = AtomicInteger(0)

    private val repo = CollectionRepo(user.userDB)

    class RowEdit(
        @JvmField val genId: Int,
        @JvmField val row: CollectionResult.Row,
        @JvmField val expectedQuantity: Double = row.expectedQuantity,
        @JvmField val actualQuantity: Double = row.actualQuantity,
        @JvmField val name: String = row.collectionNm.orEmpty(),
    ) {
        @JvmField
        val expectedQuantityStr = expectedQuantity.stripTrailingZerosString()

        @JvmField
        val actualQuantityStr = actualQuantity.stripTrailingZerosString()

        fun copy(quantity: Double, name: String): RowEdit {
            return RowEdit(genId, row, expectedQuantity, quantity, name)
        }

        fun toResultRow(): CollectionResult.Row {
            return row.newRow(actualQuantity, name)
        }
    }

    class Group(
        @JvmField val genId: Int,
        @JvmField val group: CollectionGroup,
        private val map: MutableMap<Int, RowEdit>,
    ) {
        @JvmField
        val sortedList: List<RowEdit> = map.values.sortedBy { it.row.displayOrder }

        fun tryAddRow(row: RowEdit): Group? {
            if (row.row.collectionClass != group.collectionClass)
                return null
            map[row.genId] = row
            return Group(genId, group, map)
        }
    }


    private val editedList = BehaviorProcessor.createDefault<List<Group>>(emptyList())

    private val displayList = Flowable.defer {
        editedList.switchMap {
            // don't refresh if edited.
            if (it.isEmpty()) {
                repo.collectionResultsWithGroup(allocationNo, allocationRowNo)
                    // network updates too fast?
                    .throttleWithTimeout(50, TimeUnit.MILLISECONDS)
                    .map { m ->
                        m.map { (g, rs) ->
                            Group(
                                idCounter.incrementAndGet(),
                                g,
                                rs.map { r ->
                                    RowEdit(idCounter.incrementAndGet(), r)
                                }.associateByTo(LinkedHashMap(), RowEdit::genId)
                            )
                        }
                    }
                    .subscribeOnIO()
            } else {
                Flowable.just(it)
            }
        }
    }

    val displayListLiveData: LiveData<List<Group>> = displayList.toLiveData()

    fun rowEdit(row: RowEdit, quantity: Double, name: String) {
        if (row.actualQuantity == quantity && row.name == name) return
        addRow(row.copy(quantity, name))
    }

    fun addRow(name: String, g: CollectionGroup) {
        val id = idCounter.incrementAndGet()
        addRow(
            RowEdit(
                id,
                CollectionResult.Row(
                    null,
                    null,
                    name,
                    0.0,
                    0.0,
                    g.collectionClass,
                    -id
                ),
            )
        )
    }

    private fun addRow(re: RowEdit) {
        val l = displayListLiveData.value?.map { it.tryAddRow(re) ?: it } ?: emptyList()
        editedList.onNext(l)
    }

    fun save() {
        val list = editedList.value ?: return
        runBlocking(Dispatchers.IO) {
            val l = list.flatMap { it.sortedList.map(RowEdit::toResultRow) }
            val r = CollectionResult(allocationNo, allocationRowNo, l)
            r.recordChanged()
            repo.saveResult(r)
            Current.syncCollection()
        }
    }
}
