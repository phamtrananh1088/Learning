package jp.co.toukei.log.trustar.feature.dialog

import android.content.Context
import androidx.recyclerview.widget.DiffUtil
import jp.co.toukei.log.lib.util.CompareContent
import jp.co.toukei.log.trustar.db.user.entity.Truck
import jp.co.toukei.log.trustar.other.CodeNameItem

class TruckItem(context: Context) : CodeNameItem<TruckItem.Item>(context) {

    override fun onBind(bound: Item) {
        val t = bound.truck
        code.text = t.truckCd
        name.text = t.truckNm
    }

    class Item(@JvmField val truck: Truck) : CompareContent<Item> {
        override fun sameContent(other: Item): Boolean {
            return other.truck.truckNm == truck.truckNm
        }
    }

    class Diff : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.truck.sameItem(newItem.truck)
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.sameContent(newItem)
        }
    }
}
