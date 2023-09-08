package jp.co.toukei.log.trustar

import android.content.Context
import android.location.Location
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.room.Room
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.DisposableContainer
import io.reactivex.rxjava3.processors.BehaviorProcessor
import jp.co.toukei.log.lib.Const
import jp.co.toukei.log.lib.addTo
import jp.co.toukei.log.lib.child
import jp.co.toukei.log.lib.ctx.Ctx
import jp.co.toukei.log.lib.deleteQuickly
import jp.co.toukei.log.lib.makeDirs
import jp.co.toukei.log.lib.optional
import jp.co.toukei.log.lib.orElseNull
import jp.co.toukei.log.lib.startActivityClearAndNewTask
import jp.co.toukei.log.lib.startIntentClearAndNewTask
import jp.co.toukei.log.lib.util.CallAdapterFactory
import jp.co.toukei.log.lib.util.GetValue
import jp.co.toukei.log.lib.util.Singleton
import jp.co.toukei.log.trustar.chat.MessageTask
import jp.co.toukei.log.trustar.chat.PushMessage
import jp.co.toukei.log.trustar.chat.UpdateFirebaseToken
import jp.co.toukei.log.trustar.db.chat.ChatDB
import jp.co.toukei.log.trustar.activity.LoginActivity
import jp.co.toukei.log.trustar.rest.annotation.InjectHttpApi
import jp.co.toukei.log.trustar.rest.annotation.Type
import jp.co.toukei.log.trustar.rest.api.ChatApi
import jp.co.toukei.log.trustar.rest.api.OnlineApi
import jp.co.toukei.log.trustar.rest.response.Bin
import jp.co.toukei.log.trustar.user.ClientInfo
import jp.co.toukei.log.trustar.user.LoggedUser
import jp.co.toukei.log.trustar.user.SyncData
import jp.co.toukei.log.trustar.user.UserInfo
import jp.co.toukei.log.trustar.user.UserWithToken
import okhttp3.Cache
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.create
import third.WeakDisposableContainer
import java.io.IOException
import java.util.Optional
import java.util.function.Function

object Current {

    private val disposable = WeakDisposableContainer()
    private val userChangeMutable = BehaviorProcessor.create<Optional<LoggedUser>>()

    @JvmField
    val disposableContainer: DisposableContainer = disposable

    @JvmField
    val userFlowable: Flowable<Optional<LoggedUser>> = userChangeMutable

    @Deprecated("")
    var user: LoggedUser? = null
        private set(value) {
            field = value
            userChangeMutable.offer(value.optional())
        }

    private fun setUser(u: UserWithToken?) {
        sync = null
        disposable.clear()
        chatDB.clear()
        user?.close()
        user = u?.run { LoggedUser(userInfo, token, Config.resultDb, Config.imageDB) }
        PushMessage.clearNotification()
    }

    /**
     * 元のユーザーのログイン情報が削除される。
     *
     * @param json 新しいログイン情報
     */
    fun login(context: Context, login: UserWithToken, json: JSONObject) {
        val o = user?.userInfo
        login(login)
        o?.removeLoginData()
        context.setLastUser(login, json)
        UpdateFirebaseToken.updateToken()
    }

    /**
     * ログインだけ。
     */
    fun login(user: UserWithToken) {
        setUser(user)
        setTask(user.token, user.userInfo.postIntervalInMin)
        val m = MessageTask().also { it.observePendingChanges() }
        disposableContainer.add(m)
        syncMaster()
        UpdateFirebaseToken.newToken()
    }

    /**
     * ログアウト。
     * @param removeHome true:ユーザーデータを削除、false:ログイン情報を削除
     */
    fun logout(context: Context, removeHome: Boolean = false) {
        val old = user
        setUser(null)
        old?.let {
            it.userInfo.apply {
                if (removeHome) homeDir.deleteQuickly() else removeLoginData()
                setTask(it.token, postIntervalInMin)
            }
        }
        context.startActivityClearAndNewTask<LoginActivity>()
    }

    fun restartLaunch(context: Context) {
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName) ?: return
        setUser(null)
        context.startIntentClearAndNewTask(intent)
    }

    private var sync: SyncData? = null

    init {
        Config.pref.lastApiKey?.let {
            setTask(it, 1)
        }
    }

    private fun setTask(apiKey: String, interval: Int) {
        sync = SyncData(
            apiKey,
            interval,
            ClientInfo(),
            user
        ).apply {
            addTo(disposableContainer)
        }
    }

    fun syncBin(
        preferFetch: Boolean = true,
        callback: ((Optional<GetValue<Bin>>) -> Unit)? = null,
    ) {
        val s = sync
        if (s == null) callback?.invoke(Optional.empty())
        else s.syncBin(preferFetch, callback)
    }

    fun rxSyncBin(): Single<Bin> {
        return Single.create {
            syncBin(true) { c ->
                when (val v = c.orElseNull()) {
                    is GetValue.Value -> if (!it.isDisposed) it.onSuccess(v.value)
                    is GetValue.Error -> if (!it.isDisposed) it.onError(v.error)
                    else -> if (!it.isDisposed) it.onError(IOException("unexpected error"))
                }
            }
        }
    }

    fun syncIncidental() {
        sync?.sendImage()
        sync?.syncIncidental()
    }

    fun syncMaster() {
        sync?.syncMaster()
    }

    fun syncNotice() {
        sync?.syncNotice()
    }

    fun syncFuel() {
        sync?.syncFuelInfo()
    }

    fun syncCollection(preferFetch: Boolean = false) {
        sync?.syncCollection(preferFetch)
    }

    fun syncSensorCsvUpload() {
        sync?.syncSensorCsvUpload()
    }

    fun syncRest() {
        sync?.syncSendRest()
    }

    fun syncChart() {
        sync?.syncChart()
    }

    val location = mutableStateOf<Location?>(null)
    var lastLocation by location

    @Deprecated("")
    val loggedUser: LoggedUser
        get() = user ?: throw IllegalStateException("logged out")

    val userInfo: UserInfo
        get() = loggedUser.userInfo

    val userId: String
        get() = userInfo.userId


    // be careful.
    @JvmField
    val chatDB = object : Singleton<ChatDB>(ChatDB::class.java) {

        override fun create(): ChatDB {
            val u = userInfo
            return Room.databaseBuilder(
                Ctx.context,
                ChatDB::class.java,
                u.homeDir.child("chat_db").path
            ).fallbackToDestructiveMigration().build()
        }

        override fun onCleared(t: ChatDB) {
            t.close()
        }
    }

    private val v2Retrofit = Config.baseRetrofit.newBuilder()
        .apply {
            callAdapterFactories().add(0, CallAdapterFactory { a ->
                val injects = a.filterIsInstance<InjectHttpApi>().firstOrNull()?.type
                if (injects.isNullOrEmpty()) null
                else Function {
                    val builder = it.newBuilder()
                    val urlBuilder = it.url.newBuilder()
                    val u = user
                    val userInfo = u?.userInfo
                    val companyCd = userInfo?.companyCd
                    val token = u?.token
                    injects.forEach { t ->
                        when (t) {
                            Type.MsgApiKeyAndTokenHeader -> {
                                builder.addHeader("token", token.orEmpty())
                                    .addHeader("apiKey", BuildConfig.msgApiKey)
                            }

                            Type.CompanyCdHeader -> {
                                builder.addHeader("companyCd", companyCd.orEmpty())
                            }

                            Type.CompanyCdQuery -> {
                                urlBuilder.addQueryParameter("companyCd", companyCd)
                            }

                            Type.ApiKeyQuery -> {
                                urlBuilder.addQueryParameter("api_key", token.orEmpty())
                            }

                            Type.PostUserInfo -> {
                                userInfo?.let { usr ->
                                    builder.post(
                                        usr.jsonBody().toString().toRequestBody(Const.mediaTypeJson)
                                    )
                                }
                            }
                        }
                    }
                    builder.url(urlBuilder.build()).build()
                }
            })
        }
        .build()

    private val chatRetrofit = v2Retrofit.newBuilder()
        .baseUrl(BuildConfig.messageBaseUrl)
        .build()

    @JvmField
    val chatApi: ChatApi = chatRetrofit.create()

    @JvmField
    val onlineApi: OnlineApi = v2Retrofit.create()


    fun loggedIn(): Boolean = user?.token != null

    @JvmField
    val msgCachedHttpClientV2: Call.Factory = run {
        OkHttpClient.Builder()
            .cache(Cache(Config.cacheDir.child("okhttp_cache").makeDirs(), 1L shl 30))
            //todo glide + retrofit.
            .addNetworkInterceptor { chain ->
                val request = chain.request().newBuilder().apply {
                    val u = user
                    addHeader("token", u?.token.orEmpty())
                    addHeader("apiKey", BuildConfig.msgApiKey)
                    addHeader("companyCd", u?.userInfo?.companyCd.orEmpty())
                }.build()
                chain.proceed(request)
            }
            .build()
    }
}
