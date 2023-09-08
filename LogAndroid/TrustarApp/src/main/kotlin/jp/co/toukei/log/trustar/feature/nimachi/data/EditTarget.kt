package jp.co.toukei.log.trustar.feature.nimachi.data

import jp.co.toukei.log.trustar.db.user.entity.BinDetail

sealed class EditTarget {
    class Sheet(@JvmField val uuid: String) : EditTarget()
    class Add(@JvmField val binDetail: BinDetail) : EditTarget()
}
