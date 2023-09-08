@file:Suppress("NOTHING_TO_INLINE")

package jp.co.toukei.log.lib

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Notification
import android.app.Service
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.StateListDrawable
import android.net.Uri
import android.os.Build
import android.os.HandlerThread
import android.provider.Settings
import android.text.Editable
import android.text.InputFilter
import android.text.Spanned
import android.text.SpannedString
import android.text.style.ForegroundColorSpan
import android.util.Base64
import android.util.Size
import android.util.StateSet
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.MainThread
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.annotation.WorkerThread
import androidx.camera.core.impl.utils.Exif
import androidx.collection.SimpleArrayMap
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.text.buildSpannedString
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.room.RoomDatabase
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.gms.tasks.Tasks
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.common.util.concurrent.ListenableFuture
import com.squareup.moshi.Moshi
import io.reactivex.rxjava3.annotations.CheckReturnValue
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.CompletableSource
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.MaybeSource
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleSource
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.disposables.DisposableContainer
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.functions.Cancellable
import io.reactivex.rxjava3.internal.functions.Functions
import io.reactivex.rxjava3.schedulers.Schedulers
import jp.co.toukei.log.lib.common.setLayoutParams
import jp.co.toukei.log.lib.util.BindHolder
import jp.co.toukei.log.lib.util.Progress
import jp.co.toukei.log.lib.util.ProgressForwardSource
import jp.co.toukei.log.lib.util.SimpleTextWatcher
import jp.co.toukei.log.lib.util.SugoiAdapter
import jp.co.toukei.log.lib.util.ValueBind
import jp.co.toukei.log.lib.util.Wrapper
import okhttp3.ResponseBody
import okhttp3.internal.closeQuietly
import okio.IOException
import okio.buffer
import okio.sink
import org.json.JSONArray
import org.json.JSONObject
import org.reactivestreams.Publisher
import splitties.dimensions.dip
import splitties.systemservices.inputMethodManager
import splitties.views.centerText
import splitties.views.dsl.core.textView
import splitties.views.padding
import splitties.views.textResource
import third.Event
import third.Result
import java.io.File
import java.io.InputStream
import java.lang.ref.WeakReference
import java.nio.ByteBuffer
import java.security.MessageDigest
import java.util.Calendar
import java.util.LinkedList
import java.util.Optional
import java.util.Queue
import java.util.TimeZone
import java.util.UUID
import java.util.concurrent.Callable
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.max

fun gradientDrawable(radius: Int = 0, color: Int): GradientDrawable {
    return GradientDrawable().apply {
        shape = if (radius < 0) GradientDrawable.OVAL else GradientDrawable.RECTANGLE
        cornerRadius = radius.toFloat()
        setColor(color)
    }
}

fun rippleDrawable(
    radius: Int,
    color: Int = Color.TRANSPARENT,
    ripple: Int = 0x20000000,
): RippleDrawable {
    val d = gradientDrawable(radius, color)
    val mask = gradientDrawable(radius, Color.BLACK)
    return RippleDrawable(ColorStateList.valueOf(ripple), d, mask)
}

fun gradientDrawableBorder(radius: Int = 0, color: Int, border: Int, width: Int): GradientDrawable {
    return GradientDrawable().apply {
        shape = if (radius < 0) GradientDrawable.OVAL else GradientDrawable.RECTANGLE
        cornerRadius = radius.toFloat()
        setColor(color)
        setStroke(width, border)
    }
}

fun rippleDrawableBorder(
    radius: Int = 0,
    color: Int,
    border: Int,
    width: Int,
    ripple: Int = 0x20000000,
): RippleDrawable {
    val d = gradientDrawableBorder(radius, color, border, width)
    val mask = gradientDrawable(radius, Color.BLACK)
    return RippleDrawable(ColorStateList.valueOf(ripple), d, mask)
}

fun View.hideIME() {
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}

fun EditText.lengthFilter(length: Int) {
    filters = arrayOf(InputFilter.LengthFilter(length))
}

fun EditText.setStringIfChanged(content: String) {
    if (text.toString() != content)
        setText(content)
}

fun Calendar.year(): Int {
    return get(Calendar.YEAR)
}

fun Calendar.month(): Int {
    return get(Calendar.MONTH)
}

fun Calendar.dayOfMonth(): Int {
    return get(Calendar.DAY_OF_MONTH)
}

fun Calendar.hourOfDay(): Int {
    return get(Calendar.HOUR_OF_DAY)
}

fun Calendar.minute(): Int {
    return get(Calendar.MINUTE)
}

inline fun <reified T : ViewModel> Fragment.getActivityModel(): T {
    return ViewModelProvider(requireActivity())[T::class.java]
}

inline fun <reified T : ViewModel> ViewModelStoreOwner.getViewModel(): T {
    return ViewModelProvider(this)[T::class.java]
}

inline fun <reified T : ViewModel> ViewModelStoreOwner.getViewModel(key: String): T {
    return ViewModelProvider(this)[key, T::class.java]
}

inline fun <reified T : ViewModel> ViewModelStoreOwner.lazyViewModel(): Lazy<T> {
    return lazy { getViewModel() }
}

inline fun File?.child(name: String): File = File(this, name)

inline fun File.makeDirs() = apply { mkdirs() }

fun File.deleteQuickly(): Boolean {
    val tmp = parentFile.child(UUID.randomUUID().toString())
    val ok = renameTo(tmp)
    if (ok) {
        runOnIo { tmp.deleteRecursively() }
    }
    return ok
}

inline fun runOnIo(crossinline block: () -> Unit) {
    Const.ioExecutor.execute { block() }
}

inline fun runOnComputation(crossinline block: () -> Unit) {
    Const.computationExecutor.execute { block() }
}

inline fun runOnUi(crossinline block: () -> Unit) {
    Const.mainHandler.post { block() }
}

fun <T : Any> Single<T>.observeOnUI(): Single<T> = observeOn(Const.mainScheduler)
fun <T : Any> Single<T>.observeOnIO(): Single<T> = observeOn(Schedulers.io())
fun <T : Any> Single<T>.subscribeOnIO(): Single<T> = subscribeOn(Schedulers.io())
fun Completable.observeOnUI(): Completable = observeOn(Const.mainScheduler)
fun Completable.observeOnIO(): Completable = observeOn(Schedulers.io())
fun Completable.subscribeOnIO(): Completable = subscribeOn(Schedulers.io())
fun <T : Any> Maybe<T>.subscribeOnIO(): Maybe<T> = subscribeOn(Schedulers.io())
fun <T : Any> Maybe<T>.observeOnIO(): Maybe<T> = observeOn(Schedulers.io())
fun <T : Any> Flowable<T>.subscribeOnIO(): Flowable<T> = subscribeOn(Schedulers.io())
fun <T : Any> Flowable<T>.subscribeOnComputation(): Flowable<T> =
    subscribeOn(Schedulers.computation())

fun <T : Any> Flowable<T>.observeOnIO(bufferSize: Int = Flowable.bufferSize()): Flowable<T> =
    observeOn(Schedulers.io(), false, bufferSize)

fun <T : Any> Flowable<T>.observeOnComputation(bufferSize: Int = Flowable.bufferSize()): Flowable<T> =
    observeOn(Schedulers.computation(), false, bufferSize)

fun <T : Any> Flowable<T>.observeOnUI(bufferSize: Int = Flowable.bufferSize()): Flowable<T> =
    observeOn(Const.mainScheduler, false, bufferSize)

fun <L : Any, R : Any> rxBiFunctionTakeLeft(): BiFunction<L, R, L> = BiFunction { t, _ -> t }

inline fun <T : Any> rxConsumer(crossinline block: (T) -> Unit): io.reactivex.rxjava3.functions.Consumer<T> {
    return io.reactivex.rxjava3.functions.Consumer { block(it) }
}

fun <T : Any> Flowable<out Result<T>>.subscribe(liveData: MutableLiveData<in Result<T>>): Disposable {
    return subscribe(
        { liveData.value = it },
        { liveData.value = Result.Error(it) }
    )
}

inline fun Disposable.addTo(disposableContainer: DisposableContainer): Boolean {
    return disposableContainer.add(this)
}

inline fun <T : Any?> nonNullObserver(crossinline block: (T) -> Unit): Observer<T?> {
    return Observer { it?.let(block) }
}

inline fun <T : Any> LiveData<out T?>.observeNonNull(
    owner: LifecycleOwner,
    crossinline block: (T) -> Unit,
) {
    observe(owner, nonNullObserver(block))
}

inline fun <T : Any> LiveData<out T?>.observeNullable(
    owner: LifecycleOwner,
    crossinline block: (T?) -> Unit,
) {
    observe(owner, Observer { block(it) })
}

fun <T : Any> Result<T>.value(): T? {
    return if (this is Result.Value) value else null
}

inline fun <T : Any> Result<T>.isLoading(): Boolean {
    return this === Result.Loading
}

inline fun <T : Any, R : Any> Result<T>.mapResult(mapper: (T) -> R): Result<R> {
    return when (this) {
        is Result.Loading -> this
        is Result.Error -> this
        is Result.Value -> Result.Value(mapper(value))
    }
}

inline fun LiveData<out Result<*>?>.isLoading(): Boolean {
    return value?.isLoading() ?: false
}

fun Context.startIntentClearAndNewTask(intent: Intent) {
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(intent)
}

inline fun <reified T : Activity> Context.startActivityClearAndNewTask() {
    startIntentClearAndNewTask(Intent(this, T::class.java))
}

fun RequestBuilder<Drawable>.fitCenterInto(imageView: ImageView) {
    fitCenter().into(imageView)
}

fun RequestBuilder<Drawable>.centerInsideInto(imageView: ImageView) {
    centerInside().into(imageView)
}

fun RequestManager.loadFileNoCache(file: File?): RequestBuilder<Drawable> {
    return load(file)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .skipMemoryCache(true)
}

fun TimeZone.calendar(millis: Long): Calendar {
    return Calendar.getInstance(this).apply { timeInMillis = millis }
}

fun TimeZone.currentCalendar(): Calendar {
    return calendar(System.currentTimeMillis())
}

fun Calendar.setMidnight0() {
    setTime(0, 0, 0)
}

fun Calendar.setIfValid(field: Int, value: Int): Calendar? {
    if (value in getActualMinimum(field)..getActualMaximum(field)) {
        set(field, value)
        return this
    }
    return null
}

fun Calendar.setDate(year: Int, month: Int, dayOfMonth: Int) {
    set(Calendar.YEAR, year)
    set(Calendar.MONTH, month)
    set(Calendar.DAY_OF_MONTH, dayOfMonth)
}

fun Calendar.setTime(hour24: Int, min: Int, sec: Int) {
    set(Calendar.HOUR_OF_DAY, hour24)
    set(Calendar.MINUTE, min)
    set(Calendar.SECOND, sec)
    set(Calendar.MILLISECOND, 0)
}

inline fun <T> List<T>.randomAccess(): List<T> {
    return if (this is RandomAccess) this else ArrayList(this)
}

inline fun <T> Iterable<T>.asList(): List<T> {
    return if (this is List) this else toList()
}


fun <T : Any> Single<T>.toResult(): Single<Result<T>> {
    return map<Result<T>> { Result.Value(it) }.onErrorWrapComplete()
}

fun <T : Any> Flowable<T>.toResult(): Flowable<Result<T>> {
    return map<Result<T>> { Result.Value(it) }.onErrorWrapComplete()
}

fun <T : Any> Single<T>.toResultWithLoading(delayMillisLoading: Long = 0): Flowable<Result<T>> {
    return toResult().withLoading(delayMillisLoading)
}

fun <T : Any> Flowable<T>.toResultWithLoading(delayMillisLoading: Long = 0): Flowable<Result<T>> {
    return toResult().withLoading(delayMillisLoading)
}

fun <T : Any> Single<Result<T>>.withLoading(delayMillisLoading: Long = 0): Flowable<Result<T>> {
    return toFlowable().withLoading(delayMillisLoading)
}

fun <T : Any> Flowable<Result<T>>.withLoading(delayMillisLoading: Long = 0): Flowable<Result<T>> {
    return startWithItem(Result.Loading)
        .switchMap {
            val f = Flowable.just(it)
            if (it.isLoading()) f.delay(delayMillisLoading, TimeUnit.MILLISECONDS)
            else f
        }
}

inline fun <T : Any> Result<T>.toFlowable(): Flowable<T> {
    return when (this) {
        is Result.Loading -> Flowable.never()
        is Result.Error -> Flowable.error(error)
        is Result.Value -> Flowable.just(value)
    }
}

fun <T : Any> Flowable<Result<T>>.onErrorWrapComplete(): Flowable<Result<T>> {
    return onErrorReturn { Result.Error(it) }
}

fun <T : Any> Single<Result<T>>.onErrorWrapComplete(): Single<Result<T>> {
    return onErrorReturn { Result.Error(it) }
}

inline fun FragmentManager.replaceToTag(
    containerId: Int,
    tag: String,
    transaction: FragmentTransaction,
    crossinline maybeCreate: () -> Fragment,
): FragmentTransaction = transaction.apply {
    val exist = findFragmentByTag(tag)
    fragments.forEach {
        if (it.id == containerId && it != exist) hide(it)
    }
    if (exist != null) {
        show(exist)
    } else {
        maybeCreate().let {
            add(containerId, it, tag).show(it)
        }
    }
}

fun <T, V : Any> T.toBindHolder(): BindHolder<V> where T : UI, T : ValueBind<V> {
    return BindHolder(view, this)
}

fun assertFailed(): Nothing {
    throw AssertionError()
}

inline fun <T : Any> sugoiAdapterCreator(
    crossinline dsl: Context.() -> BindHolder<T>,
): SugoiAdapter.Creator<T> {
    return object : SugoiAdapter.Creator<T> {
        override fun onCreateViewHolder(context: Context, parent: ViewGroup): BindHolder<T> {
            return dsl(context)
        }
    }
}

inline fun <T : Any> sugoiCreator(
    bind: ValueBind<T>,
    crossinline dsl: Context.() -> View,
): SugoiAdapter.Creator<T> {
    return sugoiAdapterCreator { BindHolder(dsl(), bind) }
}

inline fun <T : Any, E> sugoiCreator(
    crossinline dsl: Context.() -> E,
): SugoiAdapter.Creator<T> where E : ValueBind<T>, E : UI {
    return sugoiAdapterCreator { dsl().toBindHolder() }
}

inline fun singleViewBlock(crossinline dsl: Context.() -> View): SugoiAdapter.ValueBlock<Unit> {
    return SugoiAdapter.ValueBlock(sugoiCreator(SugoiAdapter.NoBind(), dsl)).applyValue(Unit)
}

fun textPlaceholderBlock(
    @StringRes id: Int,
    isOffline: Boolean,
    textView: (TextView.() -> Unit)? = null,
): SugoiAdapter.ValueBlock<Unit> {
    return singleViewBlock {
        textView {
            textResource = id
            setLayoutParams()
            centerText()
            padding = dip(8)
            textView?.invoke(this)
        }
    }.apply { offline = isOffline }
}

inline fun <T : UI> T.applyView(block: View.(T) -> Unit): T {
    block(view, this)
    return this
}

inline fun BottomSheetDialog.setContent(block: Context. () -> View) = apply {
    setContentView(block(context))
}

fun Context.spanCountBy(px: Int): Int = max(1, resources.displayMetrics.widthPixels / px)

inline fun BottomSheetBehavior<*>.expand() {
    state = BottomSheetBehavior.STATE_EXPANDED
}

inline fun BottomSheetBehavior<*>.collapse() {
    state = BottomSheetBehavior.STATE_COLLAPSED
}

inline fun <reified T> Moshi.fromJSON(json: String): T? = adapter(T::class.java).fromJson(json)

inline fun <reified T> Moshi.tryFromJSON(json: String): T? {
    return runCatching { fromJSON<T>(json) }.getOrNull()
}

@MainThread
fun <X> LiveData<out X>.distinctBy(compare: (old: X?, new: X, firstTime: Boolean) -> Boolean): LiveData<X> {
    return MediatorLiveData<X>().also { m ->
        m.addSource(this, object : Observer<X> {
            private var firstTime = true
            override fun onChanged(it: X) {
                if (!compare(m.value, it, firstTime)) {
                    firstTime = false
                    m.value = it
                }
            }
        })
    }
}

@MainThread
fun <X, Y> LiveData<out X>.mapMutable(mapper: MutableLiveData<Y>.(X) -> Y): MutableLiveData<Y> {
    return createMediatorLiveData {
        value = mapper(it)
    }
}

inline fun <X, Y> LiveData<out X>.switchMapNullable(
    crossinline transform: (X) -> LiveData<out Y>?,
): LiveData<out Y> = Transformations.switchMap(this) { transform(it) }

fun <X : Any> LiveData<out Optional<X>?>.mapOrElseNull(): MutableLiveData<X?> {
    return mapMutable { it?.orElseNull() }
}

@MainThread
inline fun <X> LiveData<out X>.filterOrNull(crossinline condition: (X) -> Boolean): MutableLiveData<X?> {
    return mapMutable { if (condition(it)) it else null }
}

@MainThread
inline fun <X, Y> LiveData<out X>.createMediatorLiveData(noinline block: MutableLiveData<Y>.(X) -> Unit): MediatorLiveData<Y> {
    return MediatorLiveData<Y>().also { m ->
        m.addSource(this) { m.block(it) }
    }
}

inline fun <X : Any> LiveData<out X?>.withValue(block: (X) -> Unit) {
    value?.let(block)
}

inline fun <X : Any> Optional<out X>.withValue(block: (X) -> Unit) {
    orElseNull()?.let(block)
}

inline fun <X : Any> Optional<X>.orElseNull(): X? = orElse(null)

@MainThread
inline fun <X : Any> LiveData<out X?>.ignoreNullValue(): MutableLiveData<X> {
    return MediatorLiveData<X>().also { m ->
        m.addSource(this) {
            if (it != null) m.value = it
        }
    }
}

inline fun <X : Any, Y : Any, R> LiveData<out X?>.combineWith(
    other: LiveData<out Y?>,
    crossinline block: (X?, Y?) -> R,
): LiveData<R> {
    return MediatorLiveData<R>().also { m ->
        val ob = Observer<Any> { m.value = block(value, other.value) }
        m.addSource(this, ob)
        m.addSource(other, ob)
    }
}

inline fun <T> Iterable<T>.jsonArr(mapper: (element: T) -> Any): JSONArray {
    return map(mapper).jsonArr()
}

inline fun <T> Iterable<T>.jsonArrIndexed(mapper: (index: Int, element: T) -> Any): JSONArray {
    return mapIndexed(mapper).jsonArr()
}

inline fun <T> Iterable<T>.jsonArrIndexedNotNull(mapper: (index: Int, element: T) -> Any?): JSONArray {
    return mapIndexedNotNull(mapper).jsonArr()
}

inline fun <T> Iterable<T>.jsonArrNotNull(mapper: (element: T) -> Any?): JSONArray {
    return mapNotNull(mapper).jsonArr()
}

inline fun <T> Collection<T>.jsonArr(): JSONArray {
    return JSONArray(this)
}

fun <T> computationDifferConfig(diffCallback: DiffUtil.ItemCallback<T>): AsyncDifferConfig<T> {
    return AsyncDifferConfig.Builder(diffCallback)
        .setBackgroundThreadExecutor(Const.computationExecutor)
        .build()
}

@Throws(Exception::class)
inline fun <R : RoomDatabase, V> R.inTransaction(crossinline call: R.() -> V): V {
    return runInTransaction(Callable { call() })
}

inline fun <reified T : Service> Context.startForegroundServiceCompat() {
    ContextCompat.startForegroundService(
        this,
        Intent(this, T::class.java)
    )
}

@CheckReturnValue
inline fun <T : Any> Single<T>.subscribeOrIgnore(crossinline onSuccess: (T) -> Unit): Disposable {
    return subscribe(rxConsumer<T>(onSuccess), Functions.emptyConsumer())
}

@CheckReturnValue
inline fun <T : Any> Observable<T>.subscribeOrIgnore(crossinline onNext: (T) -> Unit): Disposable {
    return subscribe(rxConsumer<T>(onNext), Functions.emptyConsumer())
}

@CheckReturnValue
inline fun <T : Any> Flowable<T>.subscribeOrIgnore(crossinline onNext: (T) -> Unit): Disposable {
    return subscribe(rxConsumer<T>(onNext), Functions.emptyConsumer())
}


fun String.tryJsonObj(): JSONObject? {
    return try {
        JSONObject(this)
    } catch (e: Throwable) {
        null
    }
}

fun String.tryJsonArr(): JSONArray? {
    return try {
        JSONArray(this)
    } catch (e: Throwable) {
        null
    }
}

@SuppressLint("HardwareIds")
fun Context.getAndroidId(): String? {
    return Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
}

fun <T : Any> Result<T>?.maybe(): Maybe<T> {
    return when (this) {
        is Result.Value -> Maybe.just(value)
        is Result.Error -> Maybe.error(error)
        else -> Maybe.empty()
    }
}

fun <T : Any> Single<Result<T>>.flatResultMaybe(): Maybe<T> {
    return flatMapMaybe { it.maybe() }
}

fun TextView.textChangedFlowable(emitFirst: Boolean): Flowable<out CharSequence> {
    return Observable.create { emitter ->
        val l = object : SimpleTextWatcher(), Cancellable {
            override fun afterTextChanged(s: Editable?) {
                emitter.onNext(s ?: "")
            }

            override fun cancel() {
                removeTextChangedListener(this)
            }
        }
        emitter.setCancellable(l)
        addTextChangedListener(l)
        if (emitFirst) emitter.onNext(text)
    }.toFlowable(BackpressureStrategy.LATEST)
}

fun Context.makeDial(number: String): Exception? {
    return try {
        val intent = Intent(Intent.ACTION_DIAL, "tel:${Uri.encode(number)}".toUri())
        startActivity(intent)
        null
    } catch (e: Exception) {
        e
    }
}

fun Context.email(email: String, subject: String = "", text: String = ""): Exception? {
    return try {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        if (subject.isNotEmpty())
            intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        if (text.isNotEmpty())
            intent.putExtra(Intent.EXTRA_TEXT, text)
        startActivity(intent)
        null
    } catch (e: Exception) {
        e
    }
}

fun textViewMakeDialOnClick(): View.OnClickListener {
    return View.OnClickListener {
        val t = it as? TextView
        val content = t?.text?.toString()?.trim()
        if (!content.isNullOrEmpty()) {
            t.context.makeDial(content)
        }
    }
}

fun textViewSendMailOnClick(): View.OnClickListener {
    return View.OnClickListener {
        val t = it as? TextView
        val content = t?.text?.toString()?.trim()
        if (!content.isNullOrEmpty()) {
            t.context.email(content)
        }
    }
}


fun TimeZone.millisAfterOffset(millis: Long): Long {
    return getOffset(millis) + millis
}

fun disabledStateDrawable(disabled: Drawable, default: Drawable): StateListDrawable {
    return StateListDrawable().apply {
        addState(intArrayOf(-android.R.attr.state_enabled), disabled)
        addState(StateSet.WILD_CARD, default)
    }
}

fun activatedStateDrawable(activated: Drawable, default: Drawable): StateListDrawable {
    return StateListDrawable().apply {
        addState(intArrayOf(android.R.attr.state_activated), activated)
        addState(StateSet.WILD_CARD, default)
    }
}

inline fun <T> Queue<out T>.pollAll(block: (T) -> Unit) {
    @Suppress("ControlFlowWithEmptyBody")
    while (poll()?.apply(block) != null);
}

val Context.localBroadcastManager
    get() = LocalBroadcastManager.getInstance(this)

fun startNewHandlerThread(name: String): HandlerThread {
    return HandlerThread(name).apply { start() }
}

fun <T : Any> T?.weakRef(): WeakReference<T> {
    return WeakReference<T>(this)
}

inline fun <T> WeakReference<T>.get(block: T.() -> Unit) {
    get()?.let(block)
}

fun ByteArray.md5(): String {
    return MessageDigest.getInstance("MD5")
        .digest(this)
        .fold(StringBuilder(32)) { s, b ->
            s.append((b.toInt() and 0xff).toString(16).padStart(2, '0'))
        }
        .toString()
}

/**
 * @see [Base64.encodeToString]
 */
fun ByteArray.encodeToBase64String(flags: Int): String {
    return Base64.encodeToString(this, flags)
}

/**
 * @see [Base64.encodeToString]
 */
fun ByteArray.encodeToBase64StringForFilename(): String {
    return encodeToBase64String(Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP)
}

inline fun Context.buildNotification(
    channelId: String,
    builder: NotificationCompat.Builder.() -> Unit,
): Notification {
    return NotificationCompat.Builder(this, channelId).apply(builder).build()
}

fun <A : Any, B : Any> bothNotNull(): (a: A?, b: B?) -> Pair<A, B>? {
    return { a, b -> if (a == null || b == null) null else a to b }
}

/**
 * fast calculate.
 * @see String.format
 */
fun fastHHMMString(minutes: Int, showPositive: Boolean = false): CharSequence {
    val isN = minutes < 0
    val am = if (isN) -minutes else minutes
    val h = am / 60
    val m = am - h * 60
    return buildString(8) {
        if (isN) append('-') else if (showPositive) append('+')
        if (h in 0..9) append('0')
        append(h)
        append(':')
        if (m in 0..9) append('0')
        append(m)
    }
}

fun <T, E> Iterable<T>.filterAll(words: Collection<E>, filter: (T, each: E) -> Boolean): List<T> {
    return if (words.isEmpty()) asList()
    else {
        val fastLoop = ArrayList(words.toSet())
        val size = if (this is Collection) size else 100
        filterTo(ArrayList(size)) { t -> fastLoop.all { w -> filter(t, w) } }
    }
}

fun <T : Any> T.asEvent(): Event<T> = Event(this)

inline fun <T : Any> ValueBind<T>.withBoundValue(block: T.() -> Unit) {
    boundValue?.let(block)
}

fun <T> TextView.multipleQueryByTextChanged(
    sampleInMillis: Long,
    source: Observable<out Iterable<T>>,
    filter: (T, word: String) -> Boolean,
): Flowable<List<T>> = source
    .toFlowable(BackpressureStrategy.LATEST)
    .filterListByQuery(
        textChangedFlowable(true),
        sampleInMillis,
        filter = filter
    )

fun Flowable<out CharSequence>.splitWords(
    sampleInMillis: Long,
    splitRegex: Regex = Regex("\\s+"),
): Flowable<List<String>> {
    return observeOnComputation()
        .throttleLatest(sampleInMillis, TimeUnit.MILLISECONDS, true)
        .map { it.toString().trim().split(splitRegex) }
        .distinctUntilChanged()
        .onErrorReturnItem(emptyList())
}

fun <T> Flowable<out Iterable<T>>.filterListByQuery(
    textChangeSource: Flowable<out CharSequence>,
    sampleInMillis: Long,
    splitRegex: Regex = Regex("\\s+"),
    filter: (item: T, word: String) -> Boolean,
): Flowable<List<T>> = Flowable.combineLatest<Iterable<T>, Collection<String>, List<T>>(
    observeOnComputation(),
    textChangeSource.splitWords(sampleInMillis, splitRegex),
) { list, q -> list.filterAll(q, filter) }

@WorkerThread
fun imageValidation(file: File): Boolean {
    val w = file.decodeImageSize()
    return w.width > 0
}

fun UUID.byteArray(): ByteArray {
    val target = ByteArray(16)
    ByteBuffer.wrap(target).apply {
        putLong(mostSignificantBits)
        putLong(leastSignificantBits)
    }
    return target
}

fun Activity.requirePermission(permission: String, requestCode: Int): Boolean {
    if (ActivityCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    ) return true
    ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
    return false
}

@RequiresApi(Build.VERSION_CODES.M)
fun Activity.requirePermission(permission: Array<String>, requestCode: Int): Boolean {
    if (permission.all { checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED }) return true
    requestPermissions(permission, requestCode)
    return false
}

@RequiresApi(Build.VERSION_CODES.M)
fun Fragment.requirePermission(context: Context, permission: String, requestCode: Int): Boolean {
    if (context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) return true
    requestPermissions(arrayOf(permission), requestCode)
    return false
}

fun Activity.startAppSettings() {
    startActivity(
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
    )
}

fun Activity.permissionResultCheck(
    permissions: Array<out String>,
    grantResults: IntArray,
    deniedCallback: (shouldShowRequestPermissionRationale: Boolean, which: String) -> Unit,
): Boolean {
    grantResults.forEachIndexed { index, s ->
        if (s != PackageManager.PERMISSION_GRANTED) {
            val p = permissions[index]
            deniedCallback(ActivityCompat.shouldShowRequestPermissionRationale(this, p), p)
            return false
        }
    }
    return true
}

fun Context.mainExecutor(): Executor {
    return ContextCompat.getMainExecutor(this)
}

fun <T> Iterable<T>.chains(iterable: Iterable<T>?): Iterable<T> {
    if (iterable == null) return this
    return Iterable {
        val t1 = iterator()
        val t2 = iterable.iterator()

        object : Iterator<T> {
            private var current = t1

            override fun hasNext(): Boolean {
                return when {
                    current.hasNext() -> true
                    current === t2 -> false
                    else -> {
                        current = t2
                        current.hasNext()
                    }
                }
            }

            override fun next(): T = current.next()
        }
    }
}

fun <T : Any> Flowable<T>.replayThenAutoConnect(
    container: DisposableContainer,
    numberOfSubscribers: Int = 1,
): Flowable<T> {
    return replay(1).autoConnect(numberOfSubscribers, rxConsumer { it.addTo(container) })
}

inline fun StringBuilder.appendIfNotNull(any: Any?): StringBuilder {
    if (any != null) append(any)
    return this
}

inline fun <T> T.wrap(): Wrapper<T> = Wrapper(this)

inline fun <T : Any> T?.optional(): Optional<T> = Optional.ofNullable(this)

fun TimeZone.daysBetween(from: Long, to: Long): Int {
    val offset = rawOffset + dstSavings
    return ((to + offset) / 86400000).toInt() - ((from + offset) / 86400000).toInt()
}

@Throws(IOException::class)
fun InputStream.copyTo(file: File) {
    file.outputStream().use { o -> copyTo(o, 81920) }
}

inline fun <T : Any, R : Any> Flowable<T>.flatMapMaybe(
    delayErrors: Boolean,
    maxConcurrency: Int,
    crossinline mapper: (T) -> MaybeSource<out R>,
): Flowable<R> {
    return flatMapMaybe({ mapper(it) }, delayErrors, maxConcurrency)
}

inline fun <T : Any> Flowable<T>.flatMapCompletable(
    delayErrors: Boolean,
    maxConcurrency: Int,
    crossinline mapper: (T) -> CompletableSource,
): Completable {
    return flatMapCompletable({ mapper(it) }, delayErrors, maxConcurrency)
}

inline fun <T : Any, R : Any> Flowable<T>.flatMap(
    delayErrors: Boolean,
    maxConcurrency: Int,
    crossinline mapper: (T) -> Publisher<R>,
): Flowable<R> {
    return flatMap({ mapper(it) }, delayErrors, maxConcurrency)
}

inline fun <T : Any, R : Any> Flowable<T>.flatMapSingle(
    delayErrors: Boolean,
    maxConcurrency: Int,
    crossinline mapper: (T) -> SingleSource<R>,
): Flowable<R> {
    return flatMapSingle({ mapper(it) }, delayErrors, maxConcurrency)
}

@SuppressLint("RestrictedApi")
fun File.streamDecodeBitmap(options: BitmapFactory.Options): Bitmap? {
    return runCatching {
        inputStream().use {
            val r = Exif.createFromFile(this).rotation
            val bitmap = BitmapFactory.decodeStream(it, null, options)
            if (r == 90 || r == 270) {
                val w = options.outWidth
                val h = options.outHeight
                options.outHeight = w
                options.outWidth = h
            }
            bitmap?.let { b ->
                Bitmap.createBitmap(
                    b,
                    0,
                    0,
                    b.width,
                    b.height,
                    Matrix().apply { postRotate(r.toFloat()) },
                    true
                )
            }
        }
    }.getOrNull()
}

fun File.decodeImageSize(): Size {
    val op = BitmapFactory.Options()
    op.inJustDecodeBounds = true
    streamDecodeBitmap(op)
    val w = op.outWidth
    val h = op.outHeight
    return Size(w, h)
}

fun File.streamDecodeBitmap(max: Int): Bitmap? {
    return streamDecodeBitmap(max, BitmapFactory.Options())
}

private fun File.streamDecodeBitmap(max: Int, op: BitmapFactory.Options): Bitmap? {
    if (max < 1) return null
    op.inJustDecodeBounds = true
    streamDecodeBitmap(op)
    val ow = op.outWidth
    val oh = op.outHeight
    if (ow <= 0 || oh <= 0) return null
    val m = minOf(ow / max, oh / max)
    if (m > 1) {
        op.inSampleSize = Integer.highestOneBit(m)
    }
    op.inJustDecodeBounds = false
    return streamDecodeBitmap(op)
}

@SuppressLint("RestrictedApi")
fun compressDefaultJpeg(
    file: File,
    target: File,
    quality: Int,
    max: Int,
    format: Bitmap.CompressFormat? = null,
): Bitmap.CompressFormat? {
    val op = BitmapFactory.Options()
    val it = file.streamDecodeBitmap(max, op) ?: return null
    val s = max.toFloat() / maxOf(it.width, it.height)
    val bitmap = if (s >= 1) it else Bitmap.createBitmap(
        it, 0, 0, it.width, it.height,
        Matrix().apply { setScale(s, s) }, true
    )
    val mime = op.outMimeType
    return target.outputStream().use { o ->
        if (format != null) {
            if (bitmap.compress(format, quality, o))
                format
            else null
        } else if (mime == "image/png") {
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 0, o))
                Bitmap.CompressFormat.PNG
            else null
        } else {
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, quality, o))
                Bitmap.CompressFormat.JPEG
            else null
        }
    }
}

@SuppressLint("NewApi")
fun Bitmap.CompressFormat.extension(): String {
    return when {
        this === Bitmap.CompressFormat.JPEG -> ".jpeg"
        this === Bitmap.CompressFormat.PNG -> ".png"
        this === Bitmap.CompressFormat.WEBP -> ".webp"
        // api 30
        this === Bitmap.CompressFormat.WEBP_LOSSY -> ".webp"
        this === Bitmap.CompressFormat.WEBP_LOSSLESS -> ".webp"
        else -> throw AssertionError()
    }
}

inline fun <T> Sequence<T>.loopIndexed(action: (index: Int, T, hasNext: Boolean) -> Unit) {
    var index = 0
    val it = iterator()
    var h = it.hasNext()
    while (h) {
        val n = it.next()
        h = it.hasNext()
        action(index++, n, h)
    }
}

inline fun <T, reified R> List<T>.mapToArray(transform: (T) -> R): Array<R> {
    val r = randomAccess()
    return Array(size) { transform(r[it]) }
}

inline fun <T> List<T>.reversedForEach(block: (T) -> Unit) {
    val iterator = listIterator(size)
    while (iterator.hasPrevious()) {
        val element = iterator.previous()
        block(element)
    }
}

inline fun <T, R> loopTree(n: T?, sub: (T?) -> Iterable<T>?, c: (R?, T) -> R) {
    val s = LinkedList<Pair<R?, T>>()
    s.push(n?.let { null to it })
    while (s.size > 0) {
        val p = s.pop()
        sub(p?.second)?.map { c(p?.first, it) to it }?.reversedForEach(s::push)
    }
}

fun <T : Any, R : Any, K : Any> Collection<T>.treeNodeList(
    n: T?,
    id: (T) -> K,
    pid: (T) -> K?,
    c: (R?, T) -> R,
): List<R> {
    val g = groupBy(pid)
    val arr = ArrayList<R>(size)
    loopTree<T, R>(n, { g[it?.let(id)] }, { p, t -> c(p, t).also { arr += it } })
    return arr
}

fun <T : Any> Flowable<T>.delayedRetry(times: Int, delay: Long, timeUnit: TimeUnit): Flowable<T> {
    return Flowable.defer {
        val counter = AtomicInteger(times)
        doOnNext { counter.set(times) }.retryWhen { e ->
            e.flatMap {
                if (counter.decrementAndGet() < 0) {
                    Flowable.error(it)
                } else {
                    Flowable.just(Unit).delay(delay, timeUnit)
                }
            }
        }
    }
}

fun <T : Any> Single<T>.delayedRetry(times: Int, delay: Long, timeUnit: TimeUnit): Single<T> {
    return toFlowable()
        .delayedRetry(times, delay, timeUnit)
        .singleOrError()
}

fun Completable.delayedRetry(times: Int, delay: Long, timeUnit: TimeUnit): Completable {
    return toFlowable<Nothing>()
        .delayedRetry(times, delay, timeUnit)
        .ignoreElements()
}

fun <T : Any> Maybe<T>.delayedRetry(times: Int, delay: Long, timeUnit: TimeUnit): Maybe<T> {
    return toFlowable()
        .delayedRetry(times, delay, timeUnit)
        .singleElement()
}

fun Any.sameClass(any: Any): Boolean {
    return this::class.java === any::class.java
}

fun File.moveOrCopy(target: File): Boolean {
    target.canonicalFile.parentFile?.makeDirs()
    return renameTo(target) || runCatching { copyTo(target, true) }.isSuccess
}

fun <T> Iterable<T>.forEachPrevNext(block: (prev: T?, e: T, next: T?) -> Unit) {
    val it = iterator()
    val f = if (it.hasNext()) it.next() else return
    val s = if (it.hasNext()) it.next() else null
    block(null, f, s)
    if (s == null) return
    var p = f
    var e: T = s
    while (it.hasNext()) {
        val n = it.next()
        block(p, e, n)
        p = e
        e = n
    }
    block(p, e, null)
}

fun String.toColoredSpan(@ColorInt color: Int): SpannedString {
    val str = this
    return buildSpannedString {
        append(str)
        setSpan(
            ForegroundColorSpan(color),
            0,
            length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
}

fun ContentResolver.readToTmpFile(uri: Uri, dir: File): File? {
    return readToStream(uri) {
        dir.createTempFileInDir().also { f -> it.copyTo(f) }
    }
}

inline fun <R> ContentResolver.readToStream(uri: Uri, stream: (InputStream) -> R): R? {
    return runCatching {
        openInputStream(uri)?.use(stream)
    }.getOrNull()
}

fun Single<ResponseBody>.downloadToFile(
    file: File,
    tmpDir: File,
    intervalMillis: Long = 1000,
): Flowable<Progress> {
    return observeOnIO()
        .map { body -> ProgressForwardSource(body.contentLength(), body.source()) }
        .flatMapPublisher { b ->
            Flowable.create({ e ->
                val p = Flowable.interval(0, intervalMillis, TimeUnit.MILLISECONDS)
                    .onBackpressureLatest()
                    .map { Progress(b.percentage) }
                    .takeWhile { !it.isCompleted() }
                    .subscribe(
                        { e.onNext(it) },
                        Functions.emptyConsumer()
                    )
                val t = tmpDir.createTempFileInDir()
                e.setCancellable {
                    p.dispose()
                    b.closeQuietly()
                    t.delete()
                }
                val ok = try {
                    t.sink().use { o -> b.buffer().use { i -> i.readAll(o) } }
                    t.moveOrCopy(file)
                } catch (ex: Throwable) {
                    false
                }
                p.dispose()
                if (!e.isCancelled)
                    if (ok) {
                        e.onNext(Progress())
                        e.onComplete()
                    } else {
                        e.tryOnError(Exception("saving failed."))
                    }
            }, BackpressureStrategy.LATEST)
        }
        .unsubscribeOn(Schedulers.io())
        .startWith(Flowable.just(Progress(-1)))
}

fun String.detectMime(): String? {
    return let(Uri::encode)
        .let(MimeTypeMap::getFileExtensionFromUrl)
        .let(MimeTypeMap.getSingleton()::getMimeTypeFromExtension)
}

fun File.createTempFileInDir(prefix: String = "tmp", suffix: String? = null): File {
    return File.createTempFile(prefix, suffix, this)
}

fun File.createTempDirInDir(prefix: String = "tmp", suffix: String? = null): File {
    val f = createTempFileInDir(prefix, suffix)
    f.delete()
    if (f.mkdir()) {
        return f
    } else {
        throw IOException("dir")
    }
}

fun View.setOnLostFocusHideIME() {
    setOnFocusChangeListener { v, hasFocus -> if (!hasFocus) v.hideIME() }
}

fun <R> castArrayToList(size: Int): io.reactivex.rxjava3.functions.Function<in Array<*>, out List<R>> {
    return io.reactivex.rxjava3.functions.Function<Array<*>, List<R>> { arr ->
        @Suppress("UNCHECKED_CAST")
        arr.map { it as R }.also { require(it.size == size) }
    }
}

fun <T : Any> Array<Flowable<T>>.combineLatest(): Flowable<List<T>> {
    return Flowable.combineLatestArray(this, castArrayToList(size))
}

fun <T : Any> Array<Single<T>>.zip(): Single<List<T>> {
    return Single.zipArray(castArrayToList(size), *this)
}

inline fun Result<*>.loadingState(block: (isLoading: Boolean) -> Unit) {
    block(isLoading())
}

inline fun Result<*>.runOnError(block: (err: Throwable) -> Unit) {
    if (this is Result.Error) {
        block(error)
    }
}

inline fun <T : Any> Result<T>.runOnValue(block: (value: T) -> Unit) {
    if (this is Result.Value) {
        block(value)
    }
}

inline fun <T : Any> Event<T>.handle(block: (value: T) -> Unit) {
    getContentIfNotHandled()?.let(block)
}

inline fun <T : Any> Event<T>.peek(block: (T) -> Unit) {
    peekContent().let(block)
}

inline fun <T : Any> Event<Result<T>>.handleAndRunOnValue(block: (value: T) -> Unit) {
    handle { it.runOnValue(block) }
}

inline fun String.numberToHalfWidth(): String {
    val chars = toCharArray()
    chars.forEachIndexed { index, c ->
        if (c.code in 65296 until 65306) {
            chars[index] = c - 65248
        }
    }
    return String(chars)
}

fun CharSequence.regexFindAll(find: CharSequence.(startIndex: Int) -> MatchResult?): Sequence<MatchResult> {
    return generateSequence(find(0)) {
        val r = it.range
        find(r.last + 1 + if (r.isEmpty()) 1 else 0)
    }
}

fun <T, V> SimpleArrayMap<T, V>.entries(): Array<Pair<T, V>> {
    return Array(size()) {
        Pair(keyAt(it), valueAt(it))
    }
}

fun Bitmap.pure(@ColorInt c: Int): Boolean {
    val wc = width
    val ps = IntArray(wc)
    repeat(height) { h ->
        getPixels(ps, 0, wc, 0, h, wc, 1)
        ps.forEach { p ->
            if (p != c) {
                return false
            }
        }
    }
    return true
}

fun <T : Any> com.google.android.gms.tasks.Task<T>.toMaybe(awaitMillis: Long = -1): Maybe<T> {
    return Maybe.create { e ->
        runCatching {
            when {
                awaitMillis > 0L -> Tasks.await(this, awaitMillis, TimeUnit.MILLISECONDS)
                // todo almost no wait.
                awaitMillis == 0L -> Tasks.await(this, 1, TimeUnit.MILLISECONDS)
                else -> Tasks.await(this)
            }
        }.onSuccess {
            if (it == null) e.onComplete() else e.onSuccess(it)
        }.onFailure {
            val err = when (it) {
                is java.util.concurrent.ExecutionException -> it.cause ?: it
                else -> it
            }
            e.tryOnError(err)
        }
    }
}

fun <V> ListenableFuture<V>.getSuccessResult(executor: Executor, action: (V) -> Unit) {
    addListener({
        runCatching { get() }.onSuccess(action)
    }, executor)
}
