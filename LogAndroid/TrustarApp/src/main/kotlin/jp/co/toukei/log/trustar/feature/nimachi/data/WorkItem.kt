package jp.co.toukei.log.trustar.feature.nimachi.data

import jp.co.toukei.log.lib.util.CompareItem
import jp.co.toukei.log.trustar.db.user.entity.IncidentalWork

class WorkItem(
        @JvmField val work: IncidentalWork,
        @JvmField var selected: Boolean
) : CompareItem<WorkItem> {

    @JvmField
    val workNm = work.workNm

    override fun sameItem(other: WorkItem): Boolean {
        return selected == other.selected && work.sameItem(other.work)
    }

    fun toggle() {
        selected = !selected
    }
}
