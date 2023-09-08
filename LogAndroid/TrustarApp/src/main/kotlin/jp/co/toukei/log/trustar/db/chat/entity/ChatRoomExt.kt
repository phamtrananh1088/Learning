package jp.co.toukei.log.trustar.db.chat.entity

import androidx.room.Embedded
import androidx.room.Ignore
import jp.co.toukei.log.lib.daysBetween
import jp.co.toukei.log.lib.millisAfterOffset
import jp.co.toukei.log.trustar.Config
import jp.co.toukei.log.trustar.imageURI
import third.Clock

class ChatRoomExt(
    @Embedded @JvmField val room: ChatRoom,
) {

    @Ignore
    @JvmField
    val imageUri = room.image?.imageURI()

    @Ignore
    @JvmField
    val title = "${room.name}(${room.userCount})"

    @Ignore
    @JvmField
    val lastUpdateHHmm = Clock(Config.GMT9.millisAfterOffset(room.lastUpdated)).hhmm

    @Ignore
    @JvmField
    val lastUpdateMMdd = Config.mmddFormatter.format(room.lastUpdated)

    fun sameDayToLastUpdated(time: Long): Boolean {
        return Config.GMT9.daysBetween(room.lastUpdated, time) == 0
    }
}
