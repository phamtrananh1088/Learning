package jp.co.toukei.log.trustar.other

import jp.co.toukei.log.lib.util.CompareItem
import jp.co.toukei.log.trustar.db.user.entity.BinDetail
import jp.co.toukei.log.trustar.db.user.entity.BinHeader

//todo rename
sealed class WorkBin : CompareItem<WorkBin> {

    sealed class Add : WorkBin() {
        class New(@JvmField val binHeader: BinHeader) : Add() {
            override fun sameItem(other: WorkBin): Boolean {
                return other is New && other.binHeader.sameItem(binHeader)
            }
        }

        class Finished(@JvmField val binDetail: BinDetail) : Add() {
            override fun sameItem(other: WorkBin): Boolean {
                return other is Finished && other.binDetail.sameItem(binDetail)
            }
        }
    }

    class Bin(@JvmField val binDetail: BinDetail) : WorkBin() {

        override fun sameItem(other: WorkBin): Boolean {
            return other is Bin && other.binDetail.sameItem(binDetail)
        }
    }
}
