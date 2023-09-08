package jp.co.toukei.log.trustar.other

import jp.co.toukei.log.trustar.db.user.entity.BinHeader
import jp.co.toukei.log.trustar.db.user.entity.Fuel

class Refueled(
    @JvmField val binHeader: BinHeader,
    @JvmField val fuel: Fuel,
    @JvmField val quantity: Float,
    @JvmField val paid: Float
)
