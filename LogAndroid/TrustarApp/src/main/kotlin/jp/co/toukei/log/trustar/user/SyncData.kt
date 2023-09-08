package jp.co.toukei.log.trustar.user

import android.util.Log
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.MaybeObserver
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.observers.DisposableMaybeObserver
import io.reactivex.rxjava3.processors.PublishProcessor
import io.reactivex.rxjava3.schedulers.Schedulers
import jp.co.toukei.log.lib.addTo
import jp.co.toukei.log.lib.flatMapCompletable
import jp.co.toukei.log.lib.flatMapMaybe
import jp.co.toukei.log.lib.flatResultMaybe
import jp.co.toukei.log.lib.inTransaction
import jp.co.toukei.log.lib.observeOnIO
import jp.co.toukei.log.lib.rxConsumer
import jp.co.toukei.log.lib.util.GetValue
import jp.co.toukei.log.trustar.Config
import jp.co.toukei.log.trustar.db.result.entity.CommonKyuyu
import jp.co.toukei.log.trustar.db.result.entity.FailedData
import jp.co.toukei.log.trustar.mapByState
import jp.co.toukei.log.trustar.rest.FetchApi
import jp.co.toukei.log.trustar.rest.SyncApi
import jp.co.toukei.log.trustar.rest.post.BinPost
import jp.co.toukei.log.trustar.rest.post.CollectionPost
import jp.co.toukei.log.trustar.rest.post.FuelInfo
import jp.co.toukei.log.trustar.rest.post.Geo
import jp.co.toukei.log.trustar.rest.post.IncidentalPost
import jp.co.toukei.log.trustar.rest.post.LocalData
import jp.co.toukei.log.trustar.rest.post.NoticePost
import jp.co.toukei.log.trustar.rest.post.PicPost
import jp.co.toukei.log.trustar.rest.response.Bin
import jp.co.toukei.log.trustar.rest.response.Incidental
import jp.co.toukei.log.trustar.rest.response.Master
import jp.co.toukei.log.trustar.rest.response.NoticeInfo
import jp.co.toukei.log.trustar.rest.response.ResponseException
import jp.co.toukei.log.trustar.setThenGetPending
import okhttp3.RequestBody.Companion.asRequestBody
import third.WeakDisposableContainer
import java.io.File
import java.util.Optional
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.max

/**
 * データ送信、受信
 */
class SyncData(
    private val apiKey: String,
    private val postIntervalInMin: Int,
    private val clientInfo: ClientInfo,
    loggedUser: LoggedUser?,
) : Thread(), Disposable {

    private val resultDb = Config.resultDb
    private val imageDB = Config.imageDB
    private val disposableContainer = WeakDisposableContainer()

    /**
     * nullのとき、受信しません。
     */
    private var fetchDataUser: LoggedUser? = loggedUser

    override fun dispose() {
        interrupt()
        disposableContainer.dispose()
    }

    override fun isDisposed(): Boolean {
        return disposableContainer.isDisposed
    }

    private val deadErrConsumer = rxConsumer<Throwable> {
        disposableContainer.dispose()
        Config.logException(it, fetchDataUser?.userInfo)
        Log.e("tr", it.message, it)
    }
    private val logErrConsumer = rxConsumer<Throwable> {
        if (it is retrofit2.HttpException) {
            when (it.code()) {
                in 500..599 -> {
                    Config.logException(it, fetchDataUser?.userInfo)
                }
            }
        }
        Log.w("tr", it.message, it)
    }

    private abstract inner class Task3<T : Any> {

        /**
         * タスクCompletable
         */
        protected abstract fun source(): Completable

        /**
         * タスク成功したら、（受信）
         * @see [Completable.andThen]
         */
        protected open fun fetch(lo: LoggedUser): Single<out T>? = null

        protected open fun onSuccess() {}

        private val next =
            PublishProcessor.create<Pair<Boolean, List<((Optional<GetValue<T>>) -> Unit)>>>()

        init {
            next
                .onBackpressureReduce { t1, t2 ->
                    val f = t1.first || t2.first
                    f to t1.second.plus(t2.second)
                }
                .observeOnIO(1)
                .flatMapCompletable(false, 1) { (fetch, callbacks) ->
                    source()
                        .andThen(Single.defer {
                            val f = if (fetch) fetchDataUser else null
                            f?.let(::fetch)
                                ?.map { Optional.of<GetValue<T>>(GetValue.Value(it)) }
                                ?: Single.just(Optional.empty<GetValue<T>>())
                        })
                        .subscribeOn(Schedulers.io())
                        .doOnSuccess { v ->
                            callbacks.forEach { it(v) }
                            onSuccess()
                        }
                        .doOnError { e ->
                            callbacks.forEach { it(Optional.of<GetValue<T>>(GetValue.Error(e))) }
                            logErrConsumer.accept(e)
                        }
                        .doOnDispose {
                            callbacks.forEach {
                                it(Optional.of<GetValue<T>>(GetValue.Error(Exception("disposed"))))
                            }
                        }
                        .ignoreElement()
                        .onErrorComplete()
                }
                .retry()
                .subscribe()
                .addTo(disposableContainer)
        }

        /* callback will called exactly once */
        fun execute(
            preferFetch: Boolean = true,
            callback: ((Optional<GetValue<T>>) -> Unit)? = null,
        ) {
            val c = callback?.let(::listOf) ?: emptyList()
            next.onNext(preferFetch to c)
        }
    }

    private abstract inner class Task : Task3<Unit>()

    /**
     * @property retryTimes 成功ときReset
     *
     * @see [SyncApi.postFailedData]
     */
    private abstract inner class Task2<T : Any>(private val retryTimes: Int) : Task3<T>() {

        private val retryRemain = AtomicInteger(retryTimes)

        @JvmField
        val ableToRetry: () -> Boolean = { retryRemain.decrementAndGet() > 0 }

        override fun onSuccess() {
            retryRemain.set(retryTimes)
        }
    }

    override fun run() {
        val period = max(1, postIntervalInMin) * 60L
        Observable.interval(3, period, TimeUnit.SECONDS, Schedulers.io())
            .subscribe(
                rxConsumer {
                    /*定時処理の起動箇所*/
                    if (Config.networkState.hasNetwork) {
                        Log.d("tr", "sync: ${fetchDataUser?.userInfo?.userId}")
                        syncBin()
                        syncGeo()
                        syncNotice()
                        syncFuelInfo()
                        syncIncidental()
                        syncFailedData()
                        sendImage()
                        syncCollection()
                        sendLogTask.execute()
                    }
                },
                deadErrConsumer
            )
            .addTo(disposableContainer)
    }

    private fun saveData(failedData: FailedData) {
        resultDb.failedDataDao().insert(failedData)
    }

    /**
     * ## requires advanced knowledge.
     * @see Maybe.lift
     */
    private inner class DropServerErrObserver<T : Any>(
        private val retry: () -> Boolean,
        private val onErrorReturn: T,
        private val data: FailedData.Content,
        private val downstream: MaybeObserver<in T>,
    ) : DisposableMaybeObserver<T>() {

        override fun onSuccess(t: T) {
            downstream.onSuccess(t)
        }

        override fun onComplete() {
            downstream.onComplete()
        }

        override fun onError(e: Throwable) {
            when (e) {
                is retrofit2.HttpException -> r(e)
                is ResponseException -> r(e)
                else -> downstream.onError(e)
            }
        }

        override fun onStart() {
            downstream.onSubscribe(this)
        }

        private fun r(e: Exception) {
            if (retry()) {
                downstream.onError(e)
            } else {
                try {
                    saveData(FailedData(e, data))
                } catch (e: Throwable) {
                    downstream.onError(e)
                    return
                }
                downstream.onSuccess(onErrorReturn)
            }
        }
    }

    /**
     * Binデータ、運行データ
     * @see [SyncApi.postBin]
     * @see [FetchApi.binData]
     */
    private val binTask = object : Task2<Bin>(5) {

        override fun source() = Single
            .fromCallable {
                loggedUser?.commitBinToCommon()
                resultDb.commonWorkResultDao().setPending()
                resultDb.commonBinResultDao().setPending()
                resultDb.commonBinResultDao().getPendingWithWorkResult()
            }
            .flatMapMaybe<BinPost> { em ->
                val br = em.map { it.binResults }
                    .distinctBy {
                        it.run { "$companyCd $userId $allocationNo" }
                    }
                val wr = em.mapNotNull { it.workResults }
                if (br.isEmpty() && wr.isEmpty()) Maybe.empty()
                else {
                    val b = BinPost(br, wr, clientInfo)
                    Config.syncApi.postBin(apiKey, b)
                        .mapByState(b)
                        .flatResultMaybe()
                        .lift {
                            DropServerErrObserver(
                                ableToRetry,
                                b,
                                FailedData.Content.Bin(b),
                                it
                            )
                        }
                }
            }
            .doOnSuccess {
                resultDb.commonWorkResultDao().delete(it.workResults)
                resultDb.commonBinResultDao().delete(it.binResults)
            }
            .ignoreElement()

        override fun fetch(lo: LoggedUser) = Config.fetchApi.binData(apiKey, lo.userInfo)
            .doOnSuccess { b ->
                lo.userDB.inTransaction {
                    binDetailDao().apply {
                        val oldBinIdList = safeToDeleteList()
                        delete(oldBinIdList)
                        b.binDetails?.let { insert(it) }
                    }
                    binHeaderDao().apply {
                        delete(safeToDeleteList())
                        insert(b.binHeaders)
                    }
                    collectionResultDao().apply {
                        delete(safeToDeleteList())
                        b.collectionResults?.let { insert(it) }
                    }
                }
            }
    }

    /**
     * 荷待ち、附帯データ
     * @see [SyncApi.postIncidental]
     * @see [FetchApi.incidentalData]
     */
    private val incidentalTask = object : Task2<Incidental>(5) {

        override fun source() = Maybe
            .defer {
                loggedUser?.commitIncidentalToCommon()
                val hp = resultDb.commonIncidentalHeaderResultDao().setThenGetPending()
                val tp = resultDb.commonIncidentalTimeResultDao().setThenGetPending()
                if (hp.isEmpty() && tp.isEmpty()) Maybe.empty()
                else Maybe.just(IncidentalPost(clientInfo, hp, tp))
            }
            .flatMap<IncidentalPost> { data ->
                Config.syncApi.postIncidental(apiKey, data)
                    .mapByState(data)
                    .flatResultMaybe()
                    .lift {
                        DropServerErrObserver(
                            ableToRetry,
                            data,
                            FailedData.Content.Incidental(data),
                            it
                        )
                    }
            }
            .doOnSuccess {
                resultDb.commonIncidentalHeaderResultDao().delete(it.headerList)
                resultDb.commonIncidentalTimeResultDao().delete(it.timeList)
            }
            .ignoreElement()

        override fun fetch(lo: LoggedUser) = Config.fetchApi.incidentalData(apiKey, lo.userInfo)
            .doOnSuccess { d ->
                lo.userDB.inTransaction {
                    incidentalHeaderDao().apply {
                        deleteSynced()
                        d.incidentalHeaders?.let { insert(it) }
                    }
                    incidentalWorkDao().apply {
                        deleteAll()
                        d.incidentalWorks?.let { insert(it) }
                    }
                    incidentalTimeDao().apply {
                        deleteSynced()
                        d.incidentalTimes?.let { insert(it) }
                    }
                }
            }
    }

    /**
     * 集配物、回収物
     * @see [SyncApi.postCollection]
     */
    private val collectionTask = object : Task2<Bin>(5) {

        override fun source() = Maybe
            .defer {
                loggedUser?.commitCollectionResultToCommon()
                val l = resultDb.commonCollectionResultDao().setThenGetPending()
                val u = loggedUser?.userInfo
                if (l.isEmpty()) Maybe.empty()
                else Maybe.just(CollectionPost(u, l))
            }
            .flatMap<CollectionPost> { data ->
                Config.syncApi.postCollection(apiKey, data)
                    .mapByState(data)
                    .flatResultMaybe()
                    .lift {
                        DropServerErrObserver(
                            ableToRetry,
                            data,
                            FailedData.Content.Collection(data),
                            it
                        )
                    }
            }
            .doOnSuccess {
                resultDb.commonCollectionResultDao().delete(it.collections)
            }
            .ignoreElement()
    }

    /**
     * 位置のデータ（運行）
     * @see [SyncApi.postGeo]
     */
    private val geoTask = object : Task2<Unit>(5) {

        override fun source() = Single
            .fromCallable {
                resultDb.commonCoordinateDao().setThenGetPending()
            }
            .flatMapMaybe<Geo> { pending ->
                if (pending.isEmpty()) Maybe.empty()
                else {
                    val g = Geo(pending, clientInfo)
                    Config.syncApi.postGeo(apiKey, g)
                        .mapByState(g)
                        .flatResultMaybe()
                        .lift {
                            DropServerErrObserver(
                                ableToRetry,
                                g,
                                FailedData.Content.Geo(g),
                                it
                            )
                        }
                }
            }
            .doOnSuccess {
                resultDb.commonCoordinateDao().delete(it.coordinates)
            }
            .ignoreElement()
    }

    /**
     * お知らせデータ
     * @see [SyncApi.postNotice]
     * @see [FetchApi.noticeData]
     */
    private val noticeTask = object : Task2<NoticeInfo>(5) {

        override fun source() = Single
            .fromCallable {
                loggedUser?.commitNoticeToCommon()
                resultDb.commonNoticeDao().setThenGetPending()
            }
            .flatMapMaybe<NoticePost> { pending ->
                if (pending.isEmpty()) Maybe.empty()
                else {
                    val b = NoticePost(pending, clientInfo)
                    Config.syncApi.postNotice(apiKey, b)
                        .mapByState(b)
                        .flatResultMaybe()
                        .lift {
                            DropServerErrObserver(
                                ableToRetry,
                                b,
                                FailedData.Content.Notice(b),
                                it
                            )
                        }
                }
            }
            .doOnSuccess {
                resultDb.commonNoticeDao().delete(it.notices)
            }
            .ignoreElement()

        override fun fetch(lo: LoggedUser) = Config.fetchApi.noticeData(apiKey, lo.userInfo)
            .doOnSuccess { n ->
                n.notices?.also {
                    lo.userDB.inTransaction {
                        lo.commitNoticeToCommon()
                        noticeDao().apply {
                            deleteAll()
                            insertOrReplace(it)
                        }
                    }
                }
            }
    }

    /**
     * 給油データ
     * @see [SyncApi.postFuel]
     */
    private val fuelTask = object : Task2<Unit>(5) {

        override fun source() = Single
            .fromCallable { resultDb.commonKyuyuDao().setThenGetPending() }
            .flatMapMaybe<FuelInfo> { pending ->
                if (pending.isEmpty()) Maybe.empty() else {
                    val f = FuelInfo(pending, clientInfo)
                    Config.syncApi.postFuel(apiKey, f)
                        .mapByState(f)
                        .flatResultMaybe()
                        .lift {
                            DropServerErrObserver(
                                ableToRetry,
                                f,
                                FailedData.Content.Fuel(f),
                                it
                            )
                        }
                }
            }
            .doOnSuccess {
                it.kyuyu.forEach(CommonKyuyu::setSyncFinished)
                resultDb.commonKyuyuDao().apply {
                    updateOrIgnore(it.kyuyu)
                    deleteSyncedBeforeDate(System.currentTimeMillis() - 8 * 3600_000)
                }
            }
            .ignoreElement()
    }

    /**
     * Masterデータ
     * @see [FetchApi.masterData]
     */
    private val masterTask = object : Task3<Master>() {

        override fun source(): Completable = Completable.complete()

        override fun fetch(lo: LoggedUser) = Config.fetchApi.masterData(apiKey, lo.userInfo)
            .doOnSuccess { m ->
                val workPlace = m.rawWorkPlace?.map { it.workPlace }
                val u = lo.userDB

                u.inTransaction {
                    workPlaceDao().apply {
                        deleteAll()
                        workPlace?.let { insertOrReplace(it) }
                    }
                }
                u.inTransaction {
                    binStatusDao().apply {
                        deleteAll()
                        m.binStatuses?.let { insertOrReplace(it) }
                    }
                }
                u.inTransaction {
                    workStatusDao().apply {
                        deleteAll()
                        m.workStatuses?.let { insertOrReplace(it) }
                    }
                }
                u.inTransaction {
                    workDao().apply {
                        deleteAll()
                        m.works?.let { insertOrReplace(it) }
                    }
                }
                u.inTransaction {
                    fuelDao().apply {
                        deleteAll()
                        m.fuels?.let { insertOrReplace(it) }
                    }
                }
                u.inTransaction {
                    truckDao().apply {
                        deleteAll()
                        m.trucks?.let { insertOrReplace(it) }
                    }
                }
                u.inTransaction {
                    shipperDao().apply {
                        deleteAll()
                        m.shippers?.let { insertOrReplace(it) }
                    }
                }
                u.inTransaction {
                    delayReasonDao().apply {
                        deleteAll()
                        m.delayReason?.let { insertOrReplace(it) }
                    }
                }
                u.inTransaction {
                    collectionItemDao().apply {
                        deleteAll()
                        m.collections?.let { insertOrReplace(it) }
                    }
                }
            }
    }

    /**
     * イメージ画像（署名画像） 送信
     * @see [SyncApi.uploadImgData]
     */
    private val sendImageTask = object : Task() {

        override fun source() = Flowable
            .generate<PicPost> {
                val o = imageDB.imageSendingDao().selectAny()
                if (o == null) it.onComplete() else it.onNext(PicPost(clientInfo, o))
            }
            .flatMapMaybe(false, 1) {
                Config.syncApi.uploadImgData(apiKey, it)
                    .mapByState(it)
                    .flatResultMaybe()
            }
            .doOnNext { imageDB.imageSendingDao().deleteByPicId(it.picId) }
            .ignoreElements()
    }

    /**
     * Logファイル送信
     * @see [SyncApi.postLogFile]
     */
    private val sendLogTask = object : Task() {
        override fun source(): Completable = Flowable
            .generate<File> {
                val f = Config.errLogList?.firstOrNull()
                if (f == null) it.onComplete() else it.onNext(f)
            }
            .flatMapMaybe(false, 1) {
                Config.syncApi.postLogFile(apiKey, it.name, it.asRequestBody())
                    .mapByState(it)
                    .flatResultMaybe()
            }
            .doOnNext { it.delete() }
            .ignoreElements()
    }

    /**
     * 数回送信失敗したのデータ、送信
     * @see [SyncApi.postFailedData]
     */
    private val failedDataTask = object : Task() {

        override fun source() = Maybe
            .defer {
                val list = resultDb.failedDataDao().selectAll()
                if (list.isEmpty()) Maybe.empty()
                else {
                    val data = LocalData(list, clientInfo)
                    Config.syncApi.postFailedData(apiKey, data)
                        .mapByState(data)
                        .flatResultMaybe()
                }
            }
            .doOnSuccess { resultDb.failedDataDao().delete(it.failedData) }
            .ignoreElement()
    }

    fun syncMaster(callback: ((Optional<GetValue<Master>>) -> Unit)? = null) {
        masterTask.execute(true, callback)
    }

    fun syncBin(
        preferFetch: Boolean = true,
        callback: ((Optional<GetValue<Bin>>) -> Unit)? = null,
    ) {
        binTask.execute(preferFetch, callback)
    }

    fun syncNotice(callback: ((Optional<GetValue<NoticeInfo>>) -> Unit)? = null) {
        noticeTask.execute(true, callback)
    }

    fun syncIncidental() {
        incidentalTask.execute()
    }

    fun syncCollection(preferFetch: Boolean = false) {
        collectionTask.execute {
            syncBin(preferFetch)
        }
    }

    fun sendImage() {
        sendImageTask.execute()
    }

    fun syncFuelInfo() {
        fuelTask.execute()
    }

    private fun syncGeo() {
        geoTask.execute()
    }

    private fun syncFailedData() {
        failedDataTask.execute()
    }
}
