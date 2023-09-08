package jp.co.toukei.log.trustar.user

import android.location.Location
import androidx.annotation.MainThread
import androidx.collection.ArrayMap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.distinctUntilChanged
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.internal.disposables.ListCompositeDisposable
import io.reactivex.rxjava3.internal.functions.Functions
import io.reactivex.rxjava3.processors.BehaviorProcessor
import io.reactivex.rxjava3.processors.PublishProcessor
import jp.co.toukei.log.lib.addTo
import jp.co.toukei.log.lib.ctx.Ctx
import jp.co.toukei.log.lib.observeOnIO
import jp.co.toukei.log.lib.observeOnUI
import jp.co.toukei.log.lib.orElseNull
import jp.co.toukei.log.lib.replayThenAutoConnect
import jp.co.toukei.log.lib.subscribeOnIO
import jp.co.toukei.log.lib.util.FusedLocationHelper
import jp.co.toukei.log.trustar.Config
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.db.result.entity.CommonCoordinate
import jp.co.toukei.log.trustar.db.user.embedded.BinDetailAndStatus
import jp.co.toukei.log.trustar.db.user.entity.BinDetail
import jp.co.toukei.log.trustar.db.user.entity.BinHeader
import jp.co.toukei.log.trustar.db.user.entity.WorkStatus
import jp.co.toukei.log.trustar.isWorking
import jp.co.toukei.log.trustar.repo.BinDetailRepo
import jp.co.toukei.log.trustar.repo.BinHeaderRepo
import jp.co.toukei.log.trustar.repo.WorkRepo
import jp.co.toukei.log.trustar.shouldSkipLocation
import java.lang.ref.WeakReference
import java.util.Optional
import java.util.concurrent.TimeUnit
import kotlin.collections.set

/**
 * GPSデータ取得　
 */
class BinLocationTask(
    private val loggedUser: LoggedUser,
    simpleLog: SimpleLog? = null,
) : Disposable {

    class Record(
        @JvmField val location: Location,
        @JvmField val time: Long,
    ) {
        override fun toString(): String {
            return buildString(30) {
                append(location.latitude).append(',').append(location.longitude)
            }
        }
    }

    @JvmField
    val log: SimpleLog? = if (Config.isProductRelease) null else simpleLog

    private inline fun loggable(flush: Boolean = false, block: () -> CharSequence) {
        if (Config.isProductRelease) return
        log?.apply {
            logLine(block())
            if (flush) flush()
        }
    }

    private val callback = object : LocationCallback(), Consumer<List<BinHeader>> {

        private var userInfo: UserInfo? = loggedUser.userInfo

        private var bin: List<BinHeader>? = null
        private var previous: Location? = null
        private var lastRecorded: ArrayMap<String, Location?> = ArrayMap()
        private val decimationRange = loggedUser.userInfo.decimationRange ?: 50

        @Synchronized
        override fun accept(t: List<BinHeader>) {
            userInfo ?: return
            val old = lastRecorded
            lastRecorded = ArrayMap()

            bin = t
            bin?.forEach {
                val k = it.allocationNo
                old[k]?.let { o -> lastRecorded[k] = o } // copy old location if exists.
            }
            tryConnect()
            loggable(true) { if (bin.isNullOrEmpty()) "disconnect" else "connect" }
        }

        fun tryConnect() {
            if (bin.isNullOrEmpty()) {
                helper.disconnect()
                recordProcessor.onNext(Optional.empty())
            } else {
                helper.connect()
            }
        }

        @Synchronized
        override fun onLocationResult(result: LocationResult) {
            /*GPSデータ取得・GPS値の取得箇所*/
            val u = userInfo ?: return
            val c = result.lastLocation ?: return
            val p = previous
            previous = c
            Current.lastLocation = c
            val b = bin ?: return

            val r = Record(c, System.currentTimeMillis())
            recordProcessor.onNext(Optional.of(r))

            val toSave = mutableListOf<CommonCoordinate>()
            b.forEach { header ->
                val k = header.allocationNo
                val autoMode = k == inAutoMode.value

                if (autoMode) {
                    autoModeProcess(k, r)
                }

                val last = lastRecorded[k]
                val skip = shouldSkipLocation(decimationRange, c, p, last)
                if (!skip) {
                    toSave.add(CommonCoordinate(c, header, u, autoMode))
                    lastRecorded[k] = c
                }
            }
            if (toSave.isNotEmpty()) {
                loggable(true) {
                    buildString {
                        append(r).append('\t').append(toSave.joinToString { it.allocationNo })
                        p?.distanceTo(c)?.let { d -> append('\t').append("p=").append(d) }
                    }
                }
                Config.resultDb.commonCoordinateDao().insertOrReplace(toSave)
            }
        }

        @Synchronized
        fun cancel() {
            userInfo = null
        }
    }

    private class M(
        @JvmField val binDetailAndStatus: BinDetailAndStatus,
        check: Pair<Int, Boolean>?,
    ) {
        @JvmField
        val detail = binDetailAndStatus.detail

        @JvmField
        val status = binDetailAndStatus.status

        @JvmField
        val inRange = check?.second
    }

    private val autoStart = PublishProcessor.create<List<M>>()

    private fun autoModeProcess(allocationNo: String, record: Record) {
        val location = record.location
        val detailList = detailRepo.binDetailWithStatusList(allocationNo)
            .subscribeOnIO()
            .blockingFirst(emptyList())
            .map { M(it, BinDetail.checkDeliveryDeviation(it.detail, location)) }

        val group = detailList.groupBy { it.status.type }
        val working = group[WorkStatus.Type.Working]?.firstOrNull()
        if (working != null) {
            val inRange = working.inRange
            if (inRange != false) return
            else {
                workRepo.endWork(working.detail, location, true, isAutoMode = true)
            }
        } else {
            val moving = group[WorkStatus.Type.Moving]?.firstOrNull { it.inRange ?: false }
            if (moving == null) {
                workRepo.autoMoveBinDetail(allocationNo)
            }
        }
        autoStart.onNext(detailList)
    }

    private val helper: FusedLocationHelper = FusedLocationHelper(Ctx.context, callback)

    private val recordProcessor: BehaviorProcessor<Optional<Record>> = BehaviorProcessor.create()

    fun throttleLatestRecord(timeoutSec: Long): Flowable<Optional<Record>> {
        return recordProcessor.throttleLatest(timeoutSec, TimeUnit.SECONDS, true)
            .onBackpressureLatest()
            .observeOnIO(1)
    }

    private val userLocationRequest = LocationRequest.create().apply {
        priority = Priority.PRIORITY_HIGH_ACCURACY
        val t = loggedUser.userInfo.getIntervalInSec * 1000L
        interval = t
        fastestInterval = 1000L
    }
    private val autoModeLocationRequest = LocationRequest.create().apply {
        priority = Priority.PRIORITY_HIGH_ACCURACY
        interval = 1000L
        fastestInterval = 1000L
    }
    private val onFailure = Consumer<Throwable> { t ->
        weakError?.get()?.accept(t)
        loggable { t.toString() }
    }

    private val disposables = ListCompositeDisposable()

    private val headerRepo = BinHeaderRepo(loggedUser.userDB)
    private val detailRepo = BinDetailRepo(loggedUser.userDB)
    private val workRepo = WorkRepo(loggedUser.userDB)

    private val workingBinHeaders = headerRepo.list()
        .map { all ->
            all.mapNotNull { if (it.status.isWorking()) it.header else null }
        }
        .observeOnIO()
        .subscribeOnIO()
        .replayThenAutoConnect(disposables)

    init {
        workingBinHeaders.subscribe(callback, Functions.emptyConsumer()).addTo(disposables)
        workingBinHeaders
            .observeOnUI()
            .subscribe(
                { working ->
                    val v = inAutoMode.value
                    if (v != null && working.none { it.allocationNo == v }) {
                        cancelAutoMode(working.isNotEmpty())
                    }
                },
                {
                    onFailure.accept(it)
                    dispose()
                }
            )
            .addTo(disposables)

        autoStart.onBackpressureLatest()
            .observeOnIO(1)
            .scan(Optional.empty<BinDetailAndStatus>()) { acc, e ->
                loggable {
                    "${Thread.currentThread()} scan ${
                        e.joinToString { "s:" + it.detail.serviceOrder + " m:" + it.detail.misdeliveryMeterTo + it.inRange }
                    }"
                }
                val inRange: List<M> = e.filterNot {
                    it.status.isWorking() || (it.inRange != true)
                }
                val b = acc.orElseNull()
                    ?.let { b -> inRange.firstOrNull { it.binDetailAndStatus.sameItem(b) } }
                    ?: inRange.minWithOrNull { o1, o2 ->
                        compareValuesBy(o1, o2,
                            {
                                when (it.status.type) {
                                    WorkStatus.Type.Moving -> 1
                                    WorkStatus.Type.Ready -> 2
                                    WorkStatus.Type.Finished -> 3
                                    else -> 4
                                }
                            },
                            {
                                val s = it.detail.serviceOrder
                                if (s == null) Int.MAX_VALUE
                                else when (it.status.type) {
                                    WorkStatus.Type.Finished -> s.inv()
                                    else -> s
                                }
                            }
                        )
                    }
                loggable {
                    "${Thread.currentThread()}  a:${acc.orElseNull()?.detail?.serviceOrder} e:${b?.detail?.serviceOrder}"
                }
                Optional.ofNullable(b?.binDetailAndStatus)
            }
            .distinctUntilChanged { t1, t2 ->
                val b1 = t1.orElseNull()
                val b2 = t2.orElseNull()
                val r = b1 != null && b2 != null && b1.sameItem(b2)
                loggable {
                    "${Thread.currentThread()}  l:${b1?.detail?.serviceOrder} r:${b2?.detail?.serviceOrder} $r"
                }
                r
            }
            .switchMap {
                val detail = it.orElseNull()?.detail
                if (detail == null) {
                    loggable {
                        "${Thread.currentThread()} switch null"
                    }
                    Flowable.empty()
                } else {
                    val delayInMin = BinDetail.stayTimeDelayInMin(detail).toLong()
                    loggable {
                        "${Thread.currentThread()} switch s:${detail.serviceOrder} delay:$delayInMin"
                    }
                    Flowable.just(detail).delay(delayInMin, TimeUnit.MINUTES)
                }
            }
            .onBackpressureDrop()
            .observeOnIO(1)
            .retry()
            .subscribe {
                val w = it.work
                if (w != null)
                    workRepo.startWork(it, w, Current.lastLocation, true, isAutoMode = true)
                loggable { "${Thread.currentThread()} start ${it.serviceOrder}" }
            }
            .addTo(disposables)
    }

    private var weakError: WeakReference<Consumer<Throwable>>? = null

    @MainThread
    fun ensureRequest(onError: WeakReference<Consumer<Throwable>>) {
        if (isDisposed) return
        weakError = onError
        ensureLocationRequestMode()
    }

    private fun ensureLocationRequestMode() {
        if (isDisposed) return
        helper.setLocationRequest(
            if (inAutoMode.value == null) userLocationRequest else autoModeLocationRequest,
            onFailure
        )
    }

    fun checkSettings() {
        val disposed = isDisposed
        if (disposed) return
        helper.checkSettings()
        callback.tryConnect()
    }

    override fun dispose() {
        disposables.dispose()
        callback.cancel()
        helper.destroy()
        recordProcessor.onComplete()

        loggable { "dispose" }
    }

    override fun isDisposed(): Boolean {
        return disposables.isDisposed
    }

    init {
        loggable { "open" }
    }

    private val inAutoMode = MutableLiveData<String?>()

    @JvmField
    val inAutoModeBin: LiveData<String?> = inAutoMode.distinctUntilChanged()

    @MainThread
    fun setInAutoMode(binHeader: BinHeader) {
        loggable { "${Thread.currentThread()} setInAutoMode ${binHeader.allocationNo}" }
        inAutoMode.value = binHeader.allocationNo
        ensureLocationRequestMode()
    }

    @MainThread
    fun cancelAutoMode(ensureRequest: Boolean = true) {
        loggable { "${Thread.currentThread()} cancelAutoMode ${inAutoMode.value}" }
        inAutoMode.value = null
        autoStart.onNext(emptyList())
        if (ensureRequest)
            ensureLocationRequestMode()
    }
}
