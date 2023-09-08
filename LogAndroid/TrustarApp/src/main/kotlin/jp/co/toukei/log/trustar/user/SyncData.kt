package jp.co.toukei.log.trustar.user

import android.util.Log
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.CompletableObserver
import io.reactivex.rxjava3.core.CompletableOperator
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.processors.PublishProcessor
import io.reactivex.rxjava3.schedulers.Schedulers
import jp.co.toukei.log.lib.Const
import jp.co.toukei.log.lib.addTo
import jp.co.toukei.log.lib.flatMapCompletable
import jp.co.toukei.log.lib.flatMapSingle
import jp.co.toukei.log.lib.inTransaction
import jp.co.toukei.log.lib.moshi.CustomMoshiAdapterFactory
import jp.co.toukei.log.lib.observeOnIO
import jp.co.toukei.log.lib.rxConsumer
import jp.co.toukei.log.lib.util.CallAdapterFactory
import jp.co.toukei.log.lib.util.GetValue
import jp.co.toukei.log.trustar.BuildConfig
import jp.co.toukei.log.trustar.Config
import jp.co.toukei.log.trustar.common.StoredFile
import jp.co.toukei.log.trustar.common.asStoredFile
import jp.co.toukei.log.trustar.db.result.entity.CommonRefuel
import jp.co.toukei.log.trustar.db.result.entity.FailedData
import jp.co.toukei.log.trustar.rest.OfflineSyncApi
import jp.co.toukei.log.trustar.rest.post.BinPost
import jp.co.toukei.log.trustar.rest.post.ChartResultPost
import jp.co.toukei.log.trustar.rest.post.CollectionPost
import jp.co.toukei.log.trustar.rest.post.FuelInfo
import jp.co.toukei.log.trustar.rest.post.Geo
import jp.co.toukei.log.trustar.rest.post.IncidentalPost
import jp.co.toukei.log.trustar.rest.post.LocalData
import jp.co.toukei.log.trustar.rest.post.NoticePost
import jp.co.toukei.log.trustar.rest.post.PicPost
import jp.co.toukei.log.trustar.rest.post.RestPost
import jp.co.toukei.log.trustar.rest.response.Bin
import jp.co.toukei.log.trustar.rest.response.Incidental
import jp.co.toukei.log.trustar.rest.response.Master
import jp.co.toukei.log.trustar.rest.response.RawChart
import jp.co.toukei.log.trustar.rest.response.ResponseException
import jp.co.toukei.log.trustar.setThenGetPending
import jp.co.toukei.log.trustar.toCompletable
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.create
import third.WeakDisposableContainer
import java.util.Optional
import java.util.UUID
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.Function
import kotlin.math.max

/**
 * データ送信、受信
 */
class SyncData(
    private val apiKey: String,
    postIntervalInMin: Int,
    private val clientInfo: ClientInfo,
    loggedUser: LoggedUser?,
) : Disposable {

    private val syncApi: OfflineSyncApi = Config.baseRetrofit.newBuilder()
        .apply {
            callAdapterFactories().add(0, CallAdapterFactory { _ ->
                Function {
                    it.newBuilder()
                        .url(
                            it.url.newBuilder()
                                .addQueryParameter("api_key", apiKey)
                                .build()
                        )
                        .build()
                }
            })
        }
        .build()
        .create()

    private val resultDb = Config.resultDb
    private val imageDB = Config.imageDB
    private val disposableContainer = WeakDisposableContainer()

    /**
     * nullのとき、受信しません。
     */
    private var fetchDataUser: LoggedUser? = loggedUser

    override fun dispose() {
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
        if (!BuildConfig.isDebug) {
            when (it) {
                is retrofit2.HttpException -> {
                    when (it.code()) {
                        in 500..599 -> {
                            Config.logException(it, fetchDataUser?.userInfo)
                        }
                    }
                }

                is CustomMoshiAdapterFactory.InvalidJsonException -> {
                    Config.logException(
                        it.cause ?: it,
                        fetchDataUser?.userInfo
                    )
                }
            }
        }
        Log.w("tr", it.message, it)
    }

    private abstract inner class Task<T : Any> {

        /**
         * タスクCompletable
         */
        protected abstract fun send(): Completable

        /**
         * タスク成功したら、（受信）
         * @see [Completable.andThen]
         */
        protected open fun fetch(lo: LoggedUser): Single<out T>? = null

        protected open fun afterSuccess() {}

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
                    send()
                        .andThen(Single.defer {
                            val f = if (fetch) fetchDataUser else null
                            f?.let(::fetch)
                                ?.map { Optional.of<GetValue<T>>(GetValue.Value(it)) }
                                ?: Single.just(Optional.empty<GetValue<T>>())
                        })
                        .subscribeOn(Schedulers.io())
                        .doOnSuccess { v ->
                            callbacks.forEach { it(v) }
                            afterSuccess()
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

    /**
     * @property retryTimes 成功ときReset
     */
    private abstract inner class TaskRetry<T : Any>(private val retryTimes: Int) : Task<T>() {

        private val retryRemain = AtomicInteger(retryTimes)

        override fun afterSuccess() {
            retryRemain.set(retryTimes)
        }

        fun Completable.ignoreAPIFailed(
            failedData: (Throwable) -> FailedData.Content,
        ): Completable {
            val op = CompletableOperator { downstream ->
                object : CompletableObserver {
                    override fun onSubscribe(d: Disposable) {
                        downstream.onSubscribe(d)
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
                        downstream.onError(e)
                    }

                    private fun r(e: Exception) {
                        if (retryRemain.decrementAndGet() > 0) {
                            downstream.onError(e)
                        } else {
                            try {
                                saveData(FailedData(e, failedData(e)))
                            } catch (e: Throwable) {
                                downstream.onError(e)
                                return
                            }
                            downstream.onComplete()
                        }
                    }
                }
            }
            return lift(op)
        }
    }

    init {
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
                        syncSensorCsvUpload()
                        syncSendRest()
                        syncChart()
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
     * Binデータ、運行データ
     */
    private val binTask = object : TaskRetry<Bin>(5) {

        override fun send() = Single
            .fromCallable {
                loggedUser?.commitBinToCommon()
                resultDb.commonWorkResultDao().setPending()
                resultDb.commonBinResultDao().setPending()
                resultDb.commonBinResultDao().getPendingWithWork()
            }
            .flatMapMaybe { map ->
                val br = map.keys.distinctBy {
                    it.run { "$companyCd $userId $allocationNo" }
                }
                val wr = map.values.flatten().distinctBy {
                    it.run { "$companyCd $userId $allocationNo $allocationRowNo" }
                }
                if (br.isEmpty() && wr.isEmpty()) Maybe.empty()
                else {
                    val b = BinPost(br, wr, clientInfo)
                    syncApi.postBin(b)
                        .toCompletable()
                        .ignoreAPIFailed {
                            FailedData.Content.Bin(b)
                        }
                        .toSingleDefault(b)
                        .toMaybe()
                }
            }
            .doOnSuccess {
                //todo fixme bug.
                resultDb.commonWorkResultDao().delete(it.workResults)
                resultDb.commonBinResultDao().delete(it.binResults)
            }
            .ignoreElement()

        override fun fetch(lo: LoggedUser) = syncApi.binData(lo.userInfo)
            .doOnSuccess { b ->
                lo.userDB.inTransaction {
                    binDetailDao().apply {
                        delete(safeToDeleteList())
                        b.binDetails?.let { insert(it) }
                    }
                    binHeaderDao().apply {
                        delete(safeToDeleteList())
                        insert(b.binHeaders)
                    }
                    //todo ng
                    collectionResultDao().apply {
                        delete(safeToDeleteList())
                        b.collectionResults?.let { insert(it) }
                    }
                }
            }
    }

    /**
     * 荷待ち、附帯データ
     */
    private val incidentalTask = object : TaskRetry<Incidental>(5) {

        override fun send() = Maybe
            .defer {
                loggedUser?.commitIncidentalToCommon()
                val hp = resultDb.commonIncidentalHeaderResultDao().setThenGetPending()
                val tp = resultDb.commonIncidentalTimeResultDao().setThenGetPending()
                if (hp.isEmpty() && tp.isEmpty()) Maybe.empty()
                else Maybe.just(IncidentalPost(clientInfo, hp, tp))
            }
            .flatMap { data ->
                syncApi.postIncidental(data)
                    .toCompletable()
                    .ignoreAPIFailed {
                        FailedData.Content.Incidental(data)
                    }
                    .toSingleDefault(data)
                    .toMaybe()
            }
            .doOnSuccess {
                //todo fixme bug.
                resultDb.commonIncidentalHeaderResultDao().delete(it.headerList)
                resultDb.commonIncidentalTimeResultDao().delete(it.timeList)
            }
            .ignoreElement()

        override fun fetch(lo: LoggedUser) = syncApi.incidentalData(lo.userInfo)
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
     */
    private val collectionTask = object : TaskRetry<Bin>(5) {

        override fun send() = Maybe
            .defer {
                loggedUser?.commitCollectionResultToCommon()
                val l = resultDb.commonCollectionResultDao().setThenGetPending()
                val u = loggedUser?.userInfo
                if (l.isEmpty()) Maybe.empty()
                else Maybe.just(CollectionPost(u, l))
            }
            .flatMap { data ->
                syncApi.postCollection(data)
                    .toCompletable()
                    .ignoreAPIFailed {
                        FailedData.Content.Collection(data)
                    }
                    .toSingleDefault(data)
                    .toMaybe()
            }
            .doOnSuccess {
                //todo fixme bug.
                resultDb.commonCollectionResultDao().delete(it.collections)
            }
            .ignoreElement()
    }

    /**
     * 位置のデータ（運行）
     */
    private val geoTask = object : TaskRetry<Unit>(5) {

        override fun send() = Single
            .fromCallable {
                resultDb.commonCoordinateDao().setThenGetPending()
            }
            .flatMapMaybe { pending ->
                if (pending.isEmpty()) Maybe.empty()
                else {
                    val g = Geo(pending, clientInfo)
                    syncApi.postGeo(g)
                        .toCompletable()
                        .ignoreAPIFailed {
                            FailedData.Content.Geo(g)
                        }
                        .toSingleDefault(g)
                        .toMaybe()
                }
            }
            .doOnSuccess {
                resultDb.commonCoordinateDao().delete(it.coordinates)
            }
            .ignoreElement()
    }

    /**
     * お知らせデータ
     */
    private val noticeTask = object : TaskRetry<Any>(5) {

        override fun send() = Single
            .fromCallable {
                loggedUser?.commitNoticeToCommon()
                resultDb.commonNoticeDao().setThenGetPending()
            }
            .flatMapMaybe { pending ->
                if (pending.isEmpty()) Maybe.empty()
                else {
                    val b = NoticePost(pending, clientInfo)
                    syncApi.postNotice(b)
                        .toCompletable()
                        .ignoreAPIFailed {
                            FailedData.Content.Notice(b)
                        }
                        .toSingleDefault(b)
                        .toMaybe()
                }
            }
            .doOnSuccess {
                //todo fixme bug.
                resultDb.commonNoticeDao().delete(it.notices)
            }
            .ignoreElement()

        override fun fetch(lo: LoggedUser) = syncApi.noticeData(lo.userInfo)
            .doOnSuccess { n ->
                n.also {
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
     */
    private val fuelTask = object : TaskRetry<Unit>(5) {

        override fun send() = Single
            .fromCallable { resultDb.commonRefuelDao().setThenGetPending() }
            .flatMapMaybe { pending ->
                if (pending.isEmpty()) Maybe.empty()
                else {
                    val f = FuelInfo(pending, clientInfo)
                    syncApi.postFuel(f)
                        .toCompletable()
                        .ignoreAPIFailed {
                            FailedData.Content.Fuel(f)
                        }
                        .toSingleDefault(f)
                        .toMaybe()
                }
            }
            .doOnSuccess {
                //todo fixme bug.
                it.kyuyu.forEach(CommonRefuel::setSyncFinished)
                resultDb.commonRefuelDao().apply {
                    updateOrIgnore(it.kyuyu)
                    deleteSyncedBeforeDate(System.currentTimeMillis() - 8 * 3600_000)
                }
            }
            .ignoreElement()
    }

    /**
     * Masterデータ
     */
    private val masterTask = object : Task<Master>() {

        override fun send(): Completable = Completable.complete()

        override fun fetch(lo: LoggedUser) = syncApi.masterData(lo.userInfo)
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
     */
    private val sendImageTask = object : Task<Unit>() {

        override fun send() = Flowable
            .generate {
                val o = imageDB.imageSendingDao().selectAny()
                if (o == null) it.onComplete() else it.onNext(PicPost(clientInfo, o))
            }
            .flatMapSingle(false, 1) {
                syncApi.uploadImgData(it)
                    .toCompletable()
                    .toSingleDefault(it)
            }
            .doOnNext { imageDB.imageSendingDao().deleteByPicId(it.picId) }
            .ignoreElements()
    }

    /**
     * Logファイル送信
     */
    private val sendLogTask = object : Task<Unit>() {
        override fun send(): Completable = Flowable
            .generate {
                val f = Config.errLogList?.firstOrNull()
                if (f == null) it.onComplete() else it.onNext(f)
            }
            .flatMapSingle(false, 1) {
                syncApi.postLogFile(it.name, it.asRequestBody())
                    .toCompletable()
                    .toSingleDefault(it)
            }
            .doOnNext { it.delete() }
            .ignoreElements()
    }

    /**
     * 数回送信失敗したのデータ、送信
     */
    private val failedDataTask = object : Task<Unit>() {

        override fun send() = Maybe
            .defer {
                val list = resultDb.failedDataDao().selectAll()
                if (list.isEmpty()) Maybe.empty()
                else {
                    val data = LocalData(list, clientInfo)
                    syncApi.postFailedData(data)
                        .toCompletable()
                        .toSingleDefault(data)
                        .toMaybe()
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

    fun syncNotice(callback: ((Optional<GetValue<Any>>) -> Unit)? = null) {
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

    private val sendSensorCsvTask = object : Task<Unit>() {

        override fun send() = Flowable
            .generate {
                val o = resultDb.commonSensorCsvDao().selectAny()
                if (o == null) it.onComplete() else it.onNext(o)
            }
            .flatMapSingle(false, 1) {
                val f = "${it.companyCd}-${it.userId}-${it.allocationNo}-${UUID.randomUUID()}.csv"

                syncApi
                    .uploadSensorCSV(
                        MultipartBody.Part.createFormData("sensor", f, it.csv.toRequestBody())
                    )
                    .toSingleDefault(it)
            }
            .doOnNext {
                resultDb.commonSensorCsvDao().delete(it)
            }
            .ignoreElements()
    }

    fun syncSensorCsvUpload() {
        sendSensorCsvTask.execute()
    }


    private val sendRestTask = object : TaskRetry<Unit>(5) {

        override fun send() = Single
            .fromCallable {
                resultDb.commonRestDao().setThenGetPending()
            }
            .flatMapMaybe { pending ->
                if (pending.isEmpty()) Maybe.empty()
                else {
                    val g = RestPost(pending, clientInfo)
                    syncApi.postRest(g)
                        .toCompletable()
                        .ignoreAPIFailed {
                            FailedData.Content.Rest(g)
                        }
                        .toSingleDefault(g)
                        .toMaybe()
                }
            }
            .doOnSuccess {
                resultDb.commonRestDao().delete(it.list)
            }
            .ignoreElement()
    }

    fun syncSendRest() {
        sendRestTask.execute()
    }

    private val chartTask = object : TaskRetry<RawChart>(5) {
        override fun send(): Completable {
            val user = fetchDataUser ?: return Completable.complete()
            val userInfo = user.userInfo
            val filesInDir = Config.commonChartSyncDir

            return Flowable
                .generate {
                    user.commitDeliveryChartToCommon()
                    val o = resultDb.commonDeliveryChartDao().run {
                        setPending()
                        getAnyPending()
                    }
                    if (o == null) it.onComplete() else it.onNext(o)
                }
                .flatMapSingle(false, 1) { chart ->
                    val existsImagesByKey = arrayListOf<Pair<String, JSONObject?>>()
                    val imagesByFile = arrayListOf<StoredFile.SyncDirFile>()

                    chart.images.forEach { image ->
                        when (val s = image.dbStoredFile.asStoredFile(filesInDir)) {
                            is StoredFile.ByKey -> existsImagesByKey += (s.key to image.extra)
                            is StoredFile.SyncDirFile -> imagesByFile += s
                        }
                    }
                    val p = ChartResultPost(
                        chart = chart,
                        userInfo = userInfo,
                        clientInfo = clientInfo,
                        imagesOverride = existsImagesByKey,
                    )
                    syncApi
                        .postChart(
                            chartJson = MultipartBody.Part.createFormData(
                                name = "",
                                filename = "",
                                body = p.jsonBody().toString()
                                    .toByteArray(Charsets.UTF_8)
                                    .toRequestBody(Const.mediaTypeJson)
                            ),
                            images = imagesByFile.map { img ->
                                val file = img.storedFile
                                MultipartBody.Part.createFormData(
                                    name = "",
                                    filename = file.name,
                                    body = file.asRequestBody()
                                )
                            }
                        )
                        .ignoreAPIFailed {
                            FailedData.Content.Chart(p)
                        }
                        .toSingleDefault(p)
                }
                .doOnNext {
                    //todo fixme bug.
                    resultDb.commonDeliveryChartDao().delete(it.chart)
                }
                .doOnComplete {
                    user.userDB.inTransaction {
                        if (resultDb.commonDeliveryChartDao().setThenGetPending().isEmpty()) {
                            filesInDir.deleteAll()
                        }
                    }
                }
                .ignoreElements()
        }

        override fun fetch(lo: LoggedUser) = syncApi.getChartData(lo.userInfo)
            .doOnSuccess { b ->
                lo.userDB.inTransaction {
                    deliveryChartDao().apply {
                        deleteSynced()
                        val count = count()
                        if (count == 0) {
                            //clear undeleted files.
                            lo.userChartSyncDir.deleteAll()
                        }
                        insert(b.charts)
                    }
                }
            }
    }

    fun syncChart() {
        chartTask.execute()
    }
}
