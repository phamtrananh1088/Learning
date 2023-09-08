package jp.co.toukei.log.trustar.user

import android.location.Location
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.Priority
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.internal.disposables.ListCompositeDisposable
import io.reactivex.rxjava3.processors.BehaviorProcessor
import io.reactivex.rxjava3.processors.PublishProcessor
import jp.co.toukei.log.lib.addTo
import jp.co.toukei.log.lib.distinctUntilOptionalChanged
import jp.co.toukei.log.lib.observeOnIO
import jp.co.toukei.log.lib.optional
import jp.co.toukei.log.lib.optionalSwitchMap
import jp.co.toukei.log.lib.orElseNull
import jp.co.toukei.log.lib.replayThenAutoConnect
import jp.co.toukei.log.lib.subscribeOnIO
import jp.co.toukei.log.lib.util.LocationRequestHelper
import jp.co.toukei.log.lib.util.filterWithLast
import jp.co.toukei.log.trustar.App
import jp.co.toukei.log.trustar.BroadcastEvent
import jp.co.toukei.log.trustar.BuildConfig
import jp.co.toukei.log.trustar.Config
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.SplitBuildVariant
import jp.co.toukei.log.trustar.compose.ComposeData
import jp.co.toukei.log.trustar.db.result.entity.CommonCoordinate
import jp.co.toukei.log.trustar.db.result.entity.CommonRest
import jp.co.toukei.log.trustar.db.result.entity.CommonSensorCsv
import jp.co.toukei.log.trustar.db.user.entity.BinDetail
import jp.co.toukei.log.trustar.detect.DetectRestEvent
import jp.co.toukei.log.trustar.detect.SensorRecord
import jp.co.toukei.log.trustar.detect.eachWorkingBin
import jp.co.toukei.log.trustar.detect.restEvent
import jp.co.toukei.log.trustar.detect.sensorDetect
import jp.co.toukei.log.trustar.enum.WorkStatusEnum
import jp.co.toukei.log.trustar.isWorking
import jp.co.toukei.log.trustar.repo.BinDetailRepo
import jp.co.toukei.log.trustar.repo.BinHeaderRepo
import jp.co.toukei.log.trustar.repo.WorkRepo
import jp.co.toukei.log.trustar.shouldSkipCoordinateRecord
import jp.co.toukei.log.trustar.shouldSkipLocation
import kotlinx.coroutines.rx3.asFlowable
import java.util.Optional
import java.util.concurrent.TimeUnit
import kotlin.math.abs

/**
 * GPSデータ取得　
 */
class BinLocationTask(
    loggedUser: LoggedUser,
) : Disposable {

    class BinState(
        @JvmField val running: Boolean,
        @JvmField val location: Location? = null,
    )

    private val locationPublisher = PublishProcessor.create<Location>()

    private class M(
        @JvmField val detail: BinDetail,
        @JvmField val inRange: Boolean?,
    )

    private class MM(
        val allocationNo: String,
        val location: Location,
        val ls: List<M>,
    )

    private val autoStart = PublishProcessor.create<List<M>>()

    private val helper: LocationRequestHelper = LocationRequestHelper(
        loggedUser.locationHelper,
        { c ->
            Current.lastLocation = c
            locationPublisher.onNext(c)
        },
        { e ->
            App.gmsLocationErrFlow.tryEmit(e.cause ?: e)
        }
    )

    private val binStateProcessor: BehaviorProcessor<BinState> = BehaviorProcessor.create()

    fun throttleLatestRecord(timeoutSec: Long): Flowable<BinState> {
        return binStateProcessor.throttleLatest(timeoutSec, TimeUnit.SECONDS, true)
            .onBackpressureLatest()
            .observeOnIO(1)
    }


    private val userLocationRequest =
        LocationRequest.Builder(loggedUser.userInfo.getIntervalInSec * 1000L)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .setMinUpdateIntervalMillis(1000L)
            .build()

    private val autoModeLocationRequest = LocationRequest.Builder(1000L)
        .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
        .setMinUpdateIntervalMillis(1000L)
        .build()

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
        autoStart.onBackpressureLatest()
            .observeOnIO(1)
            .scan(Optional.empty<BinDetail>()) { acc, e ->
                val inRange: List<M> = e.filter {
                    it.inRange == true && it.detail.workStatus.isNotWorking()
                }
                val b = acc.orElseNull()?.let { b -> inRange.firstOrNull { it.detail.sameKey(b) } }
                    ?: inRange.minWithOrNull { o1, o2 ->
                        compareValuesBy(o1, o2,
                            {
                                when (it.detail.workStatus) {
                                    WorkStatusEnum.Moving -> 1
                                    WorkStatusEnum.New -> 2
                                    WorkStatusEnum.Finished -> 3
                                    else -> 4
                                }
                            },
                            {
                                val s = it.detail.serviceOrder
                                if (s == null) Int.MAX_VALUE
                                else when (it.detail.workStatus) {
                                    WorkStatusEnum.Finished -> s.inv()
                                    else -> s
                                }
                            }
                        )
                    }
                Optional.ofNullable(b?.detail)
            }
            .distinctUntilChanged { t1, t2 ->
                val b1 = t1.orElseNull()
                val b2 = t2.orElseNull()
                b1 != null && b2 != null && b1.sameKey(b2)
            }
            .switchMap {
                val detail = it.orElseNull()
                if (detail == null) {
                    Flowable.empty()
                } else {
                    val delayInMin = BinDetail.stayTimeDelayInMin(detail).toLong()
                    Flowable.just(detail).delay(delayInMin, TimeUnit.MINUTES)
                }
            }
            .onBackpressureDrop()
            .observeOnIO(1)
            .retry()
            .subscribe {
                val w = it.work
                if (w != null) {
                    workRepo.startWork(
                        allocationNo = it.allocationNo,
                        allocationRowNo = it.allocationRowNo,
                        work = ComposeData.Work(w.workCd, w.workNm.orEmpty(), 0),
                        fromLocation = Current.lastLocation,
                        addIfFinished = true,
                        isAutoMode = true
                    )
                }
            }
            .addTo(disposables)
    }

    override fun dispose() {
        disposables.dispose()
        helper.disconnect(true)
    }

    override fun isDisposed(): Boolean {
        return disposables.isDisposed
    }

    private val inAutoModeAllocationNo = BehaviorProcessor.create<Optional<String>>()

    @JvmField
    val inAutoModeBin: Flowable<Optional<String>> = inAutoModeAllocationNo

    fun setInAutoMode(allocationNo: String) {
        inAutoModeAllocationNo.onNext(allocationNo.optional())
    }

    fun cancelAutoMode() {
        inAutoModeAllocationNo.onNext(Optional.empty())
    }

    init {
        val user = loggedUser.userInfo

        //休憩判定
        restEvent(workingBinHeaders, locationPublisher, user)
            .observeOnIO()
            .subscribe { (allocationNo, r) ->
                if (r is DetectRestEvent.MoveDetected) {
                    val c = CommonRest(
                        userInfo = user,
                        allocationNo = allocationNo,
                        startLocation = r.rest.startRestLocation,
                        endDate = r.restEndTimeBasedOsDate(),
                        elapsed = r.elapsedRestMillis(),
                    )
                    loggedUser.resultDb.commonRestDao().insert(c)
                    Current.syncRest()
                }
                SplitBuildVariant.debugRestEvent(allocationNo, r)
            }
            .addTo(disposables)

        val d = user.decimationRange
        val dao = loggedUser.userDB.sensorDetectEventDao()
        val dao2 = Config.resultDb.commonSensorCsvDao()
        // todo. non-working bin changes

        //センサー検知
        sensorDetect(
            workingBinHeaders,
            binHeaderDelayInMillis = user.detectStartTimingInMin * 60_000L,
            workDelayInMillis = 5000,
        ) { detailRepo.countWorkingStatus(it).map { c -> c == 0 } }
            .observeOnIO()
            .subscribe {
                when (it) {
                    is SensorRecord.DropRecent -> {
                        val no = it.allocationNo
                        dao.deleteByTimeRange(no, it.from, it.to)
                        if (it.isFinished) {
                            val ls = dao.findBy(no)
                            if (ls.isNotEmpty()) {
                                Config.dateFormatterSensorCsv.use { formatter ->
                                    val csv = CommonSensorCsv(
                                        user, no,
                                        ls.joinToString("\n") {
                                            it.csvRow(formatter)
                                        }.toByteArray()
                                    )
                                    dao2.insert(csv)
                                    dao.deleteBy(no)
                                }
                                Current.syncSensorCsvUpload()
                            }
                        }
                    }

                    is SensorRecord.Record -> {
                        val n = it.event
                        val l = dao.latestEvent(n.allocationNo)
                            ?.eventRecord
                            ?.distanceTo(n.eventRecord)
                        if (l == null || d == null || l > d) {
                            dao.insert(n)
                        } else {
                            if (BuildConfig.isDebug) {
                                SplitBuildVariant.debugSensorEvent(it, true)
                                return@subscribe
                            }
                        }
                    }
                }

                if (BuildConfig.isDebug) {
                    SplitBuildVariant.debugSensorEvent(it, false)
                }
            }
            .addTo(disposables)

        //センサー検知レコード位置更新
        locationPublisher
            .observeOnIO()
            .subscribe { location ->
                dao.latestEventEachGroup()
                    .mapNotNull { sde ->
                        val prevLocationTime = sde.locationTimestamp ?: 0L
                        val currLocationTime = location.time
                        val eventTime = sde.eventRecord.date
                        val td1 = abs(eventTime - prevLocationTime)
                        val td2 = abs(eventTime - currLocationTime)
                        if (td1 > td2) {
                            sde.copyOf(location)
                        } else null
                    }
                    .let {
                        dao.insertOrReplace(it)
                    }
            }
            .addTo(disposables)

        val skipRange = user.decimationRange ?: 50
        val skipInterval = user.getIntervalInSec

        //位置レコード
        workingBinHeaders
            .eachWorkingBin { b ->
                locationPublisher
                    .observeOnIO()
                    .filterWithLast { upStreamLast, downStreamLast, current ->
                        !(shouldSkipLocation(skipRange, current, upStreamLast, downStreamLast) ||
                                shouldSkipCoordinateRecord(skipInterval, current, downStreamLast))
                    }
                    .map { location ->
                        val a = inAutoModeAllocationNo.value?.orElseNull()
                        val autoMode = b.allocationNo == a
                        CommonCoordinate(location, b, user, autoMode)
                    }
                    .onErrorComplete()
            }
            .subscribeOnIO()
            .subscribe { coordinate ->
                Config.resultDb.commonCoordinateDao().insertOrReplace(coordinate)
            }
            .addTo(disposables)

        //運行中ステータス
        workingBinHeaders
            .map { it.isNotEmpty() }
            .distinctUntilChanged()
            .switchMap { w ->
                if (w) {
                    locationPublisher
                        .map { BinState(true, it) }
                        .startWithItem(BinState(true))
                } else {
                    Flowable.just(BinState(false))
                }
            }
            .subscribe(binStateProcessor::onNext)
            .addTo(disposables)

        //自動モードオフ
        inAutoModeAllocationNo
            .optionalSwitchMap { no ->
                workingBinHeaders.map { w ->
                    w.none { it.allocationNo == no }
                }
            }
            .subscribe {
                if (it) {
                    cancelAutoMode()
                }
            }
            .addTo(disposables)

        //自動モード作業
        Flowable
            .combineLatest(
                inAutoModeAllocationNo,
                workingBinHeaders
            ) { a, w ->
                val no = a.orElseNull()
                if (no == null || w.none { it.allocationNo == no }) Optional.empty() else a
            }
            .distinctUntilOptionalChanged()
            .switchMap {
                val allocationNo = it.orElseNull()
                if (allocationNo == null) {
                    Flowable.just(Optional.empty())
                } else {
                    Flowable.combineLatest(
                        detailRepo.binDetailWithStatusList(allocationNo),
                        locationPublisher,
                    ) { list, loc ->
                        MM(
                            allocationNo,
                            loc,
                            list.map {
                                M(
                                    it.detail,
                                    BinDetail.checkDeliveryDeviation(it.detail, loc)?.second
                                )
                            }
                        ).optional()
                    }
                }
            }
            .subscribe { ooo ->
                val oo = ooo.orElseNull()
                if (oo == null) {
                    autoStart.onNext(emptyList())
                } else {
                    val l = oo.ls
                    val working = l.firstOrNull { it.detail.workStatus.isWorking() }
                    if (working != null) {
                        if (working.inRange == false) {
                            val x = working.detail
                            //out of range
                            workRepo.endWork(
                                x.allocationNo,
                                x.allocationRowNo,
                                oo.location,
                                true,
                                isAutoMode = true
                            )
                            autoStart.onNext(l)
                        }
                    } else {
                        val moving = l.firstOrNull {
                            it.detail.workStatus.isMoving() && (it.inRange ?: false)
                        }
                        if (moving == null) {
                            workRepo.autoMoveBinDetail(oo.allocationNo)
                        }
                        autoStart.onNext(l)
                    }
                }
            }
            .addTo(disposables)

        //手動・自動モード位置取得
        Flowable
            .combineLatest(
                workingBinHeaders.map { it.isNotEmpty() },
                inAutoModeAllocationNo.startWithItem(Optional.empty())
            ) { w, a ->
                if (w) {
                    Optional.ofNullable(
                        if (a.orElseNull() == null) userLocationRequest
                        else autoModeLocationRequest
                    )
                } else {
                    Optional.empty()
                }
            }
            .throttleLatest(1, TimeUnit.SECONDS, true)
            .distinctUntilOptionalChanged()
            .subscribe {
                val r = it.orElseNull()
                if (r != null) {
                    helper.setRequest(r)
                } else {
                    helper.disconnect(true)
                }
            }
            .addTo(disposables)

        //位置取得条件エラー場合
        helper.latestErr
            .switchMap {
                val e = it.orElseNull()
                if (e == null) Flowable.empty()
                else Flowable.just(e).delay(5, TimeUnit.SECONDS).repeat()
            }
            .onBackpressureLatest()
            .observeOnIO()
            .subscribe {
                //todo ???
                helper.reconnected(true)
            }
            .addTo(disposables)
        App.broadcastFlow()
            .asFlowable()
            .subscribe {
                if (it is BroadcastEvent.LocationSettingsChanged)
                    helper.reconnected(true)
            }
            .addTo(disposables)
    }
}
