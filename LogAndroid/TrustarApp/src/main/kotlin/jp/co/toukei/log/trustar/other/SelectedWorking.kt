package jp.co.toukei.log.trustar.other

import jp.co.toukei.log.trustar.db.user.entity.BinDetail
import jp.co.toukei.log.trustar.db.user.entity.Shipper
import jp.co.toukei.log.trustar.db.user.entity.Work

class SelectedWorking(
        @JvmField val work: Work,
        @JvmField val shipper: Shipper?,
        @JvmField val binDetail: BinDetail
)
