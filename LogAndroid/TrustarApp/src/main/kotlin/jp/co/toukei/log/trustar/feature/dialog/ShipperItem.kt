package jp.co.toukei.log.trustar.feature.dialog

import android.content.Context
import androidx.recyclerview.widget.DiffUtil
import jp.co.toukei.log.lib.util.CompareContent
import jp.co.toukei.log.trustar.db.user.entity.Shipper
import jp.co.toukei.log.trustar.other.CodeNameItem

class ShipperItem(context: Context) : CodeNameItem<ShipperItem.Item>(context) {

    override fun onBind(bound: Item) {
        val s = bound.shipper
        code.text = s.shipperCd
        name.text = s.shipperNm
    }

    class Item(@JvmField val shipper: Shipper) : CompareContent<Item> {
        override fun sameContent(other: Item): Boolean {
            return other.shipper.shipperNm == shipper.shipperNm
        }
    }

    class Diff : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.shipper.sameItem(newItem.shipper)
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.sameContent(newItem)
        }
    }
}
