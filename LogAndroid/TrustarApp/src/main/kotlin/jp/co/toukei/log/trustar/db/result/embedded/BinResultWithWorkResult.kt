package jp.co.toukei.log.trustar.db.result.embedded

import androidx.room.Embedded
import jp.co.toukei.log.trustar.db.result.entity.CommonBinResult
import jp.co.toukei.log.trustar.db.result.entity.CommonWorkResult

class BinResultWithWorkResult(
        @Embedded(prefix = "b_") @JvmField val binResults: CommonBinResult,
        @Embedded @JvmField val workResults: CommonWorkResult?
)
