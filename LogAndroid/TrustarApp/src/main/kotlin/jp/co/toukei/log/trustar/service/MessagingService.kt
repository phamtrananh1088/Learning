package jp.co.toukei.log.trustar.service

import android.Manifest
import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import jp.co.toukei.log.lib.buildNotification
import jp.co.toukei.log.trustar.Config
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.chat.PushMessage
import jp.co.toukei.log.trustar.chat.PushMessage.PushMsg
import jp.co.toukei.log.trustar.chat.UpdateFirebaseToken
import jp.co.toukei.log.trustar.chat.activity.Chat

class MessagingService : FirebaseMessagingService() {

    /** called even app killed by OS. important!*/
    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)

        val data = p0.data

        if (Config.pref.uid != data["uid"]) {
            runCatching { FirebaseMessaging.getInstance().deleteToken() }
            return
        }
//        when (data["type"]) {
//            "1" -> data["todo"]
//            "2" -> data["todo"]
//            else ->
//        }
        processChatMessage(
            PushMsg(
                data["title"].orEmpty(),
                data["body"].orEmpty(),
                data["talkRoomId"].orEmpty()
            )
        )
    }

    private val notificationManager
        get() = NotificationManagerCompat.from(this)

    private fun processChatMessage(msg: PushMsg) {
        val roomId = msg.roomId
        val list = PushMessage.addPushMsg(roomId, msg)
        PushMessage.postMessage(PushMessage.Msg(msg, list))
        if (PushMessage.observingMessage(roomId)) {
            val id = PushMessage.notificationId(roomId)
            notificationManager.cancel(id)
            PushMessage.clearNotification(roomId)
        } else if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val ctx = this
            val n = buildNotification(Config.messageNotificationChannelId) {
                setSmallIcon(R.drawable.round_chat_24)
                setOngoing(false)
                setAutoCancel(true)
                setColorized(true)
                color = getColor(R.color.colorPrimary)
                priority = NotificationCompat.PRIORITY_MAX
                setContentTitle(list.firstOrNull()?.title.orEmpty())
                setContentText(getString(R.string.new_d_messages, list.size))
                setNumber(list.size)
                setStyle(NotificationCompat.InboxStyle().also { s ->
                    list.forEach {
                        it.body.takeIf(String::isNotEmpty)?.let(s::addLine)
                    }
                })
                setCategory(Notification.CATEGORY_MESSAGE)
                setContentIntent(
                    PendingIntent.getActivity(
                        ctx,
                        1,
                        Intent(ctx, Chat::class.java).putExtra(Chat.ARG_ROOM_ID, roomId),
                        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT
                    )
                )
//                setDeleteIntent(
//                    PendingIntent.getBroadcast(
//                        ctx,
//                        1,
//                        Intent(Config.ACTION_REMOVE_ROOM_NOTIFICATION)
//                            .putExtra(Config.ACTION_REMOVE_ROOM_NOTIFICATION, roomId),
//                        PendingIntent.FLAG_IMMUTABLE
//                    )
//                )
            }
            notificationManager.notify(PushMessage.notificationId(roomId), n)
        }
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        if (Current.loggedIn()) UpdateFirebaseToken.updateToken()
    }
}
