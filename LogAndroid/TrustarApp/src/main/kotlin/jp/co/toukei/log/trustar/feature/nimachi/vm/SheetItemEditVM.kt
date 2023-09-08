package jp.co.toukei.log.trustar.feature.nimachi.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.internal.functions.Functions
import jp.co.toukei.log.lib.rxConsumer
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.common.CommonViewModel
import jp.co.toukei.log.trustar.db.user.entity.*
import jp.co.toukei.log.trustar.feature.nimachi.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SheetItemEditVM : CommonViewModel() {

    private val repo = Current.userRepository.incidentalRepo

    private var edit: Editor? = null

    fun editTarget(target: EditTarget): Editor {
        return edit ?: run {
            val e = when (target) {
                is EditTarget.Sheet -> {
                    EditorDB().apply {
                        repo.sheetDetailsByUUID(target.uuid)
                            .subscribe(rxConsumer { setData(it) }, Functions.emptyConsumer())
                            .disposeOnClear()
                    }
                }
                is EditTarget.Add -> {
                    EditorLocal(target.binDetail).apply { update() }
                }
            }
            e.also { edit = it }
        }
    }

    suspend fun save(): IncidentalHeader? {
        return withContext(Dispatchers.IO) {
            edit?.save().also { Current.syncIncidental() }
        }
    }

    abstract class Editor {

        fun deleteTime(time: EditedTime) {
            time.markDeleted = true
            update()
        }

        fun addTime(time: TimeItem) {
            addedTimes.add(EditedTime(time, 0x1100FF00).apply { justChanged = true })
            update()
        }

        fun editTime(time: EditedTime, begin: Long?, end: Long?) {
            time.overrideBeginDate = begin?.let { DateItem(it) }
            time.overrideEndDate = end?.let { DateItem(it) }
        }

        private fun display(value: ItemData) {
            _displayData.postValue(value)
            _displayShipper.postValue(value.shipper)
        }

        protected var overrideShipper: Shipper? = null
        protected var overrideWorks: List<IncidentalWork?>? = null

        private val addedTimes: MutableList<EditedTime> = mutableListOf()
        private var editedTimes: Iterable<EditedTime> = emptyList()
        private var editedTimesToDisplay: Iterable<EditedTime> = timeToDisplay()

        private fun timeToDisplay(): Iterable<EditedTime> {
            return editedTimes.asSequence()
                .sortedWith(compareBy({ it.begin?.date }, { it.end?.date }))
                .plus(addedTimes)
                .filterNot { it.markDeleted }
                .asIterable()
        }

        fun setShipper(shipper: Shipper?) {
            overrideShipper = shipper
            update()
        }

        fun setWorks(works: List<IncidentalWork>?) {
            overrideWorks = works
            update()
        }

        fun save(): IncidentalHeader? {
            val changed = editedTimes.filter(EditedTime::hasChanged)
            val added = addedTimes.filterNot { it.markDeleted }
            return onSave(changed, added)
        }

        abstract fun onSave(
            edited: Iterable<EditedTime>,
            added: Iterable<EditedTime>
        ): IncidentalHeader?

        fun timeReload(times: Iterable<TimeItem>?) {
            val edited = editedTimes.filter { it.hasChanged() }
            editedTimes = times?.filter { o -> edited.find { o.sameItem(it.t) } == null }
                ?.map { EditedTime(it) }
                ?.plus(edited)
                ?: edited
            editedTimesToDisplay = timeToDisplay()
            update()
        }

        protected abstract val source: IncidentalItemData?

        fun update() {
            val s = source
            display(
                ItemData(
                    overrideShipper ?: s?.shipper,
                    overrideWorks ?: s?.works,
                    editedTimesToDisplay,
                    null
                )
            )
        }

        private val _displayData = MutableLiveData<ItemData>()
        private val _displayShipper = MutableLiveData<Shipper?>()
        val displayData: LiveData<ItemData> = _displayData
        val displayShipper: LiveData<Shipper?> = _displayShipper
    }

    private inner class EditorLocal(private val binDetail: BinDetail) : Editor() {

        override fun onSave(
            edited: Iterable<EditedTime>,
            added: Iterable<EditedTime>
        ): IncidentalHeader? {
            val shipper = overrideShipper ?: return null
            val works = overrideWorks ?: emptyList()
            val header = repo.addIncidentalHeader(binDetail, shipper, works)
            repo.addTimeList(header, added)
            return header
        }

        override val source: IncidentalItemData? = ItemData(null, null, null, null)
    }

    private inner class EditorDB : Editor() {

        override var source: IncidentalItemDataDB? = null

        fun setData(orig: IncidentalItemDataDB) {
            source = orig
            timeReload(orig.times)
        }

        override fun onSave(
            edited: Iterable<EditedTime>,
            added: Iterable<EditedTime>
        ): IncidentalHeader? {
            val header = source?.item?.sheet ?: return null

            val saveList = mutableListOf<IncidentalTime>()
            edited.forEach {
                when (val t = it.t) {
                    is TimeItemDB -> {
                        saveList += t.time.apply {
                            if (it.markDeleted) {
                                deleted = true
                            } else {
                                it.overrideBeginDate?.run { beginDatetime = date }
                                it.overrideEndDate?.run { endDatetime = date }
                            }
                        }
                    }
                }
            }
            repo.saveTimeList(saveList)
            repo.addTimeList(header, added)

            overrideShipper?.apply {
                header.shipperCd = shipperCd
            }
            overrideWorks?.apply {
                header.workList = mapNotNull { it?.workCd }
            }
            if (header.sync == 0) {
                repo.saveIncidentalHeader(header)
            }
            return header
        }
    }

    class ItemData(
        shipper: Shipper?,
        works: List<IncidentalWork?>?,
        override val times: Iterable<EditedTime>?,
        editTarget: EditTarget?
    ) : IncidentalItemData(shipper, works, times, editTarget)
}
