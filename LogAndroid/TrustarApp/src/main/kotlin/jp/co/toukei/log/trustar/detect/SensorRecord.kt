package jp.co.toukei.log.trustar.detect

import jp.co.toukei.log.trustar.db.user.entity.SensorDetectEvent

sealed class SensorRecord {

    class DropRecent(
        @JvmField val allocationNo: String,
        @JvmField val from: Long,
        @JvmField val to: Long,
        @JvmField val isFinished: Boolean,
    ) : SensorRecord()

    class Record(
        @JvmField val event: SensorDetectEvent,
    ) : SensorRecord()
}
