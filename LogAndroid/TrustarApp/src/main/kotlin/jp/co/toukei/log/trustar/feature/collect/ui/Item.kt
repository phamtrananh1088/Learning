package jp.co.toukei.log.trustar.feature.collect.ui

import jp.co.toukei.log.lib.sameClass
import jp.co.toukei.log.lib.util.CompareContent
import jp.co.toukei.log.lib.util.CompareItem
import jp.co.toukei.log.trustar.db.user.entity.CollectionGroup
import jp.co.toukei.log.trustar.feature.collect.vm.CollectFragmentVM

sealed class Item : CompareItem<Item>, CompareContent<Item> {
    class Group(@JvmField val group: CollectionGroup) : Item() {
        override fun sameItem(other: Item): Boolean {
            return other is Group && sameClass(other) && group === other.group
        }

        override fun sameContent(other: Item): Boolean {
            return true
        }
    }

    class Row(@JvmField val row: CollectFragmentVM.Row) : Item() {
        override fun sameItem(other: Item): Boolean {
            return other is Row && sameClass(other) && row.result === other.row.result
        }

        override fun sameContent(other: Item): Boolean {
            return other is Row && sameClass(other) &&
                    row.actualQuantity == other.row.actualQuantity &&
                    row.name == other.row.name
        }
    }
}
