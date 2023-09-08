package jp.co.toukei.log.trustar.service

import android.Manifest
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import androidx.annotation.MainThread
import androidx.collection.SimpleArrayMap
import androidx.core.app.ActivityCompat
import androidx.core.app.ComponentActivity
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.toLiveData
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.internal.disposables.ListCompositeDisposable
import jp.co.toukei.log.lib.addTo
import jp.co.toukei.log.lib.buildNotification
import jp.co.toukei.log.lib.common.toast
import jp.co.toukei.log.lib.createTempDirInDir
import jp.co.toukei.log.lib.ctx.Ctx
import jp.co.toukei.log.lib.deleteQuickly
import jp.co.toukei.log.lib.downloadToFile
import jp.co.toukei.log.lib.entries
import jp.co.toukei.log.lib.flatMapSingle
import jp.co.toukei.log.lib.localBroadcastManager
import jp.co.toukei.log.lib.makeDirs
import jp.co.toukei.log.lib.observeOnUI
import jp.co.toukei.log.lib.runOnError
import jp.co.toukei.log.lib.runOnValue
import jp.co.toukei.log.lib.subscribeOnIO
import jp.co.toukei.log.lib.util.Progress
import jp.co.toukei.log.trustar.Config
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.cacheFileShareUri
import jp.co.toukei.log.trustar.errMessage
import jp.co.toukei.log.trustar.user.UserInfo
import third.Result
import java.util.concurrent.TimeUnit

class DownloadService : Service() {

    inner class ServiceBinder : Binder() {
        val service = this@DownloadService
    }

    private val binder = ServiceBinder()

    override fun onBind(intent: Intent?) = binder

    private val disposableContainer = ListCompositeDisposable()
    private val tmpDir = Config.tmpDir.makeDirs().createTempDirInDir()

    private fun download(item: DL): Flowable<Progress> {
        return when (item) {
            is DL.KeyFile -> {
                Single.defer { Current.chatFileApi.download(item.fileKey) }
                    .downloadToFile(
                        UserInfo.cacheFileByKey(Current.userInfo, item.fileKey).file,
                        tmpDir.makeDirs(),
                        200
                    )
            }
        }.subscribeOnIO()
    }

    private val notificationManager
        get() = NotificationManagerCompat.from(this)

    // main thread safe.
    private val downloading = SimpleArrayMap<String, Pair<DL, Progress>?>()

    @MainThread
    private fun removeItem(item: DL) {
        downloading.remove(item.dlKey)
        if (downloading.isEmpty) {
            stopForeground(false)
            stopSelf()
        }
    }

    fun getProgressLiveData(interval: Long): LiveData<List<Expose>> {
        return Flowable.interval(interval, TimeUnit.MILLISECONDS)
            .onBackpressureDrop()
            .flatMapSingle(false, 1) {
                val p = downloading.entries().map { (key, pair) ->
                    Expose(key, pair?.second)
                }
                Single.just(p)
            }
            .toLiveData()
    }

    @MainThread
    private fun downloadItem(id: Int, item: DL) {
        val dlKey = item.dlKey
        if (downloading.containsKey(dlKey)) return
        downloading.put(dlKey, null)

        download(item)
            .observeOnUI()
            .doOnNext { downloading.put(dlKey, item to it) }
            .throttleLatest(500, TimeUnit.MILLISECONDS, true)
            .observeOnUI()
            .onTerminateDetach()
            .doAfterTerminate { removeItem(item) }
            .subscribe(
                {
                    val completed = it.isCompleted()

                    if (completed) {
                        val i = Intent(Config.ACTION_DOWNLOADED)
                        if (item is DL.KeyFile) {
                            i.putExtra(Config.ACTION_DOWNLOADED_EXT_FILE_KEY, item.fileKey)
                        }
                        Ctx.context.localBroadcastManager.sendBroadcast(i)
                        notificationManager.cancel(id)
                    }

                    if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        if (completed) {
                            if (item.notificationStrategy != NOTIFICATION_CLEAR) {
                                notificationManager.notify(
                                    Config.notificationIdCounter.incrementAndGet(),
                                    buildNotification(item, Result.Value(it), null)
                                )
                            }
                        } else {
                            notificationManager.notify(
                                id,
                                buildNotification(item, Result.Value(it), group)
                            )
                        }
                    }
                },
                {
                    notificationManager.cancel(id)

                    if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        notificationManager.notify(
                            Config.notificationIdCounter.incrementAndGet(),
                            buildNotification(item, Result.Error(it), null)
                        )
                    } else {
                        toast(it.errMessage())
                    }
                }
            )
            .addTo(disposableContainer)
    }

    private sealed class DL(
        @JvmField val mime: String?,
        @JvmField val displayName: String,
        @JvmField val notificationStrategy: Int,
    ) {
        abstract val dlKey: String
        abstract fun cacheFileUri(): Uri

        class KeyFile(
            @JvmField val fileKey: String,
            mime: String?,
            displayName: String,
            notificationStrategy: Int,
        ) : DL(mime, displayName, notificationStrategy) {

            override val dlKey: String
                get() = "1:$fileKey"

            override fun cacheFileUri(): Uri {
                return cacheFileShareUri(fileKey, displayName)
            }
        }
    }

    class Expose(
        @JvmField val fileKey: String,
        @JvmField val progress: Progress?,
    )

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(Config.downloadNotificationId, buildNotificationGroup())

        val item = intent?.let(Companion::toItem)
        if (item != null) {
            downloadItem(Config.notificationIdCounter.incrementAndGet(), item)
        }
        if (downloading.isEmpty) stopSelf()

        return START_NOT_STICKY
    }

    private val group = this::class.java.simpleName

    private fun buildNotificationGroup(): Notification {
        return buildNotification(Config.downloadNotificationChannelId) {
            setContentTitle(getString(R.string.download))
            setOnlyAlertOnce(true)
            setOngoing(false)
            setGroup(group)
            setGroupSummary(true)
            setSmallIcon(R.drawable.round_cloud_download_24)
            setCategory(Notification.CATEGORY_PROGRESS)
            color = getColor(R.color.colorPrimary)
            setColorized(true)
        }
    }

    private fun buildNotification(
        item: DL,
        result: Result<Progress>,
        groupKey: String?,
    ): Notification {
        val ctx = this
        return buildNotification(Config.downloadNotificationChannelId) {
            setContentTitle(item.displayName)
            setOnlyAlertOnce(true)
            setOngoing(false)
            setGroup(groupKey)
            setSmallIcon(R.drawable.round_cloud_download_24)
            setProgress(0, 0, false)
            setCategory(Notification.CATEGORY_PROGRESS)
            color = getColor(R.color.colorPrimary)
            setColorized(true)

            result.runOnError {
                setSmallIcon(R.drawable.round_warning_24)
                setContentText(it.message)
            }
            result.runOnValue { progress ->
                if (progress.isCompleted()) {
                    setSmallIcon(R.drawable.round_cloud_done_24)
                    setContentText(getString(R.string.downloaded))
                    if (item.notificationStrategy == NOTIFICATION_OPEN) {
                        val uri = item.cacheFileUri()
                        setContentIntent(
                            PendingIntent.getActivity(
                                ctx,
                                1,
                                Intent.createChooser(
                                    Intent(Intent.ACTION_VIEW)
                                        .setDataAndType(uri, item.mime)
                                        .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                                        .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION),
                                    ctx.getString(R.string.open_with)
                                ),
                                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                            )
                        )
                    }
                } else {
                    val p = progress.value
                    setProgress(progress.total, p, p < 0)
                    setContentText(null)
                    setOngoing(true)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposableContainer.dispose()
        tmpDir.deleteQuickly()
    }

    companion object {

        private const val KEY_FILE_KEY = "1"
        private const val KEY_FILE_MIME = "2"
        private const val KEY_DISPLAY_NAME = "3"
        private const val KEY_NOTIFICATION = "4"
        const val NOTIFICATION_DONE = 0
        const val NOTIFICATION_OPEN = 1
        const val NOTIFICATION_CLEAR = 2

        @JvmStatic
        private fun toItem(intent: Intent): DL? {
            val ext = intent.extras ?: return null
            val k = ext.getString(KEY_FILE_KEY)
            val m = ext.getString(KEY_FILE_MIME)
            val d = ext.getString(KEY_DISPLAY_NAME).orEmpty()
            val s = ext.getInt(KEY_NOTIFICATION, NOTIFICATION_DONE)

            return when {
                k != null -> DL.KeyFile(k, m, d, s)
                else -> null
            }
        }

//        @JvmStatic
//        fun startDownload(context: Context, file: FileData.File) {
//            startDownload(context, file.fileKey, file.mime, file.fileName, NOTIFICATION_OPEN)
//        }

        @JvmStatic
        fun startDownload(
            context: Context,
            fileKey: String,
            mime: String?,
            fileName: String,
            notificationStrategy: Int,
        ) {
            ContextCompat.startForegroundService(
                context,
                Intent(context, DownloadService::class.java)
                    .putExtra(KEY_FILE_KEY, fileKey)
                    .putExtra(KEY_FILE_MIME, mime)
                    .putExtra(KEY_DISPLAY_NAME, fileName)
                    .putExtra(KEY_NOTIFICATION, notificationStrategy)
            )
        }
    }

    class ProgressServiceConnection(
        private val progressLiveData: MediatorLiveData<List<Expose>>,
        private val activity: ComponentActivity,
        private val unbindWhenEmpty: Boolean,
    ) : ServiceConnection {

        private var ob = androidx.lifecycle.Observer<List<Expose>> {
            progressLiveData.value = it
            if (unbindWhenEmpty && it.isNullOrEmpty()) tryUnbind()
        }
        private var connectedLiveData: LiveData<List<Expose>>? = null
        private var bound = false

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as? ServiceBinder
            connectedLiveData?.removeObserver(ob)
            connectedLiveData = binder?.service?.getProgressLiveData(300)
            connectedLiveData?.observe(activity, ob)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            connectedLiveData?.removeObserver(ob)
        }

        fun tryBind() {
            if (bound) return
            bound = true
            activity.bindService(
                Intent(activity, DownloadService::class.java),
                this,
                BIND_AUTO_CREATE
            )
        }

        fun tryUnbind() {
            if (bound) {
                bound = false
                activity.unbindService(this)
            }
            connectedLiveData?.removeObserver(ob)
        }
    }
}
