package jp.co.toukei.log.trustar

import android.app.Application
import jp.co.toukei.log.lib.buildNotification
import jp.co.toukei.log.lib.child
import jp.co.toukei.log.lib.common.toast
import jp.co.toukei.log.lib.ctx.Ctx
import jp.co.toukei.log.lib.util.ReuseDateFormatter
import jp.co.toukei.log.trustar.detect.DetectRestEvent
import jp.co.toukei.log.trustar.detect.SensorRecord
import splitties.systemservices.notificationManager

object SplitBuildVariant {

    private val formatter = ReuseDateFormatter("HH:mm:ss", Config.locale, Config.timeZone)
    private val channelId = Config.NotificationChannelIdWarning

    fun debugSensorEvent(
        record: SensorRecord,
        skip: Boolean,
    ) {
        val allocationNo = when (record) {
            is SensorRecord.DropRecent -> record.allocationNo
            is SensorRecord.Record -> record.event.allocationNo
        }
        val s = when (record) {
            is SensorRecord.DropRecent -> {
                val f = formatter.format(record.from)
                val t = formatter.format(record.to)
                "delete $f ~ $t"
            }

            is SensorRecord.Record -> {
                val d = formatter.format(record.event.eventRecord.date)
                "$d, skip=$skip"
            }
        }
        notification("検出 $allocationNo", s)
    }

    fun debugDbExport() {
        val target = Ctx.context.getExternalFilesDir(null)!!.child("dump")
        Current.restartLaunch(Ctx.context)
        Config.userDir.copyRecursively(target.child("user"), true)
        Ctx.context.getDatabasePath("result_db2").parentFile!!.copyRecursively(
            target.child("db"),
            true
        )
        Ctx.context.toast("ok!")
    }

    fun debugRestEvent(allocationNo: String, e: DetectRestEvent) {
        val s = when (e) {
            is DetectRestEvent.MoveDetected -> {
                val t = e.elapsedRestMillis() / 1000
                "rest! t=${t}s, ${formatter.format(e.restEndTimeBasedOsDate())}"
            }

            is DetectRestEvent.Reset -> {
                val t = e.elapsedMillis?.let { it / 1000 }
                if (t != null) "rest cancel, t=${t}s" else null
            }

            is DetectRestEvent.RestDetected -> {
                "rest start, s=${(e.speed * 1000).toInt()}m/s"
            }
        }
        if (s != null) {
            notification("休憩検知 $allocationNo", s)
        }
    }

    fun initAppCenter(app: Application?) = Unit

    private fun notification(
        title: String,
        content: String,
    ) {
        notificationManager.notify(
            Config.notificationIdCounter.incrementAndGet(),
            Ctx.context.buildNotification(channelId) {
                setContentTitle(title)
                setOngoing(false)
                setGroup(channelId)
                setSmallIcon(R.drawable.baseline_info_24)
                setContentText(content)
            })
    }
}
