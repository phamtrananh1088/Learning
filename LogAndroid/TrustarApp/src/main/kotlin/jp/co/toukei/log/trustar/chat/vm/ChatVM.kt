package jp.co.toukei.log.trustar.chat.vm

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.toLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.paging.cachedIn
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.internal.functions.Functions
import io.reactivex.rxjava3.processors.BehaviorProcessor
import io.reactivex.rxjava3.schedulers.Schedulers
import jp.co.toukei.log.lib.addTo
import jp.co.toukei.log.lib.asEvent
import jp.co.toukei.log.lib.child
import jp.co.toukei.log.lib.compressDefaultJpeg
import jp.co.toukei.log.lib.createTempDirInDir
import jp.co.toukei.log.lib.createTempFileInDir
import jp.co.toukei.log.lib.ctx.Ctx
import jp.co.toukei.log.lib.decodeImageSize
import jp.co.toukei.log.lib.delayedRetry
import jp.co.toukei.log.lib.deleteQuickly
import jp.co.toukei.log.lib.extension
import jp.co.toukei.log.lib.flatMapCompletable
import jp.co.toukei.log.lib.isLoading
import jp.co.toukei.log.lib.makeDirs
import jp.co.toukei.log.lib.moveOrCopy
import jp.co.toukei.log.lib.observeOnIO
import jp.co.toukei.log.lib.orElseNull
import jp.co.toukei.log.lib.readToTmpFile
import jp.co.toukei.log.lib.replayThenAutoConnect
import jp.co.toukei.log.lib.runOnError
import jp.co.toukei.log.lib.runOnValue
import jp.co.toukei.log.lib.rxConsumer
import jp.co.toukei.log.lib.toResultWithLoading
import jp.co.toukei.log.lib.util.Pending
import jp.co.toukei.log.trustar.Config
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.chat.MessageItem
import jp.co.toukei.log.trustar.chat.MessageItemSource
import jp.co.toukei.log.trustar.chat.RxAudioPlayerHelper
import jp.co.toukei.log.trustar.common.CommonViewModel
import jp.co.toukei.log.trustar.db.chat.Attachment
import jp.co.toukei.log.trustar.db.chat.AttachmentExt
import jp.co.toukei.log.trustar.db.chat.Img
import jp.co.toukei.log.trustar.db.chat.entity.ChatMessagePending
import jp.co.toukei.log.trustar.displayName
import jp.co.toukei.log.trustar.repo.chat.ChatRepository
import jp.co.toukei.log.trustar.repo.chat.ChatRoomRepository
import jp.co.toukei.log.trustar.timeString
import jp.co.toukei.log.trustar.user.UserInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.withContext
import third.Event
import java.io.File
import java.util.UUID
import java.util.concurrent.TimeUnit

class ChatVM : CommonViewModel() {

    private val roomSource = BehaviorProcessor.create<String>()

    private val chatRoomWithUsers = roomSource
        .switchMap {
            chatRoomRepository.chatRoomWithUsers2(it)
        }
        .replayThenAutoConnect(disposableContainer)

    @JvmField
    val roomData = chatRoomWithUsers.toLiveData()

    fun loadRoom(roomId: String) {
        roomSource.onNext(roomId)
    }

    @JvmField
    val errLiveData = MutableLiveData<Event<Throwable>>()

    private val errConsumer = rxConsumer<Throwable> { errLiveData.postValue(it.asEvent()) }

    private val chatRepository = ChatRepository(Current.chatDB.getInstance())
    private val chatRoomRepository = ChatRoomRepository()

    @OptIn(ExperimentalCoroutinesApi::class)
    val chatLiveData: LiveData<PagingData<MessageItem>> = chatRoomWithUsers
        .distinctUntilChanged { t1, t2 -> t1.orElseNull()?.room?.id == t2.orElseNull()?.room?.id }
        .asFlow()
        .flatMapLatest { opt ->
            val r = opt.orElseNull()
            @OptIn(ExperimentalPagingApi::class)
            if (r == null) flowOf(PagingData.empty()) else {
                val roomId = r.room.id
                val remote = object : RemoteMediator<MessageItemSource.KEY, MessageItem>() {

                    override suspend fun initialize() = InitializeAction.SKIP_INITIAL_REFRESH

                    override suspend fun load(
                        loadType: LoadType,
                        state: PagingState<MessageItemSource.KEY, MessageItem>,
                    ) = withContext(Dispatchers.IO) {
                        val pageSize = state.config.pageSize
                        val rx = when (loadType) {
                            LoadType.REFRESH -> {
                                //Do a full refresh here if you like.
                                return@withContext MediatorResult.Success(false)
                            }

                            LoadType.APPEND -> {
                                chatRepository.loadBottomMessage(roomId, pageSize)
                            }

                            LoadType.PREPEND -> {
                                chatRepository.loadTopMessage(roomId, pageSize)
                            }
                        }
                        runCatching {
                            MediatorResult.Success(rx.blockingGet().isEmpty())
                        }.getOrElse { MediatorResult.Error(it) }
                    }
                }

                val pc = PagingConfig(90, 20, false, 90, 200)
                Pager(pc, remoteMediator = remote) {
                    val ur = chatRoomWithUsers.timeout(2, TimeUnit.SECONDS)
                        .onErrorComplete()
                        .blockingFirst()
                        .orElseNull()
                        ?.users ?: emptyList()
                    MessageItemSource(
                        roomId,
                        chatRepository,
                        { u -> ur.firstOrNull { it.chatUser.id == u }?.chatUser },
                        { m -> ur.map { r -> if (r.lastRow != null && r.lastRow > m.messageRow) r.chatUser else null } })
                }.flow
            }
        }
        .cachedIn(viewModelScope)
        .asLiveData()

    fun insertMessage(roomId: String, text: String) {
        vmScope.launch(Dispatchers.IO) {
            delay(50)
            Current.chatDB.getInstance().chatMessagePendingDao()
                .insert(ChatMessagePending.textMessage(roomId, text))
        }
    }

    fun resetErrStatus(chatMessagePending: ChatMessagePending) {
        if (chatMessagePending.status == ChatMessagePending.STATE_ERR)
            vmScope.launch(Dispatchers.IO) {
                Current.chatDB.getInstance().chatMessagePendingDao()
                    .setStatus(0, chatMessagePending.id)
            }
    }

    fun resetAllErrStatus(roomId: String) {
        vmScope.launch(Dispatchers.IO) {
            Current.chatDB.getInstance().chatMessagePendingDao().resetErr(roomId)
        }
    }

    private val markRead = BehaviorProcessor.create<String>()

    init {
        markRead.observeOnIO()
            .distinctUntilChanged { t1, t2 -> t2 <= t1 }
            .flatMapCompletable {
                chatRepository.updateRead(it)
                    .delayedRetry(3, 3, TimeUnit.SECONDS)
                    .onErrorComplete()
            }
            .subscribe(Functions.EMPTY_ACTION, errConsumer)
            .addTo(disposableContainer)
    }

    fun markRead(messageRow: String) {
        markRead.onNext(messageRow)
    }

    init {
        Flowable.interval(0, 1, TimeUnit.MINUTES)
            .onBackpressureLatest()
            .flatMapCompletable(false, 1) {
                chatRoomRepository.reloadRooms().onErrorComplete()
            }
            .subscribe(Functions.EMPTY_ACTION, Functions.emptyConsumer())
            .disposeOnClear()
    }

    private val tmpDir = Config.tmpDir.makeDirs().createTempDirInDir()

    override fun onCleared() {
        super.onCleared()
        player.stopPlayer()
        tmpDir.deleteQuickly()
    }

    @JvmField
    val pending = object : Pending<Int, File>() {
        override fun createPending(k: Int): File {
            return tmpDir.makeDirs().createTempFileInDir()
        }
    }

    //20 seconds limit.
    fun sendImage(roomId: String, uris: List<Uri>) {
        val tmpFiles = uris.mapNotNull {
            Ctx.context.contentResolver.readToTmpFile(it, tmpDir)
        }
        Flowable.fromIterable(tmpFiles)
            .observeOnIO()
            .flatMapCompletable(true, 1) { tmp ->
                val t2 = tmpDir.createTempFileInDir()
                val c = compressDefaultJpeg(tmp, t2, 90, 1080) ?: throw Exception()

                Completable
                    .fromCallable {
                        val name = "${timeString()}${c.extension()}"
                        val att = saveLocalAttachment(t2, name)
                        val file = att.file
                        val size = file.decodeImageSize()
                        val img = Img(
                            att,
                            att.contentUri().toString(), size.width, size.height
                        )
                        val message = ChatMessagePending.imageMessage(roomId, img)
                        Current.chatDB.getInstance().chatMessagePendingDao().insert(message)
                    }
                    .doFinally { tmp.delete() }
            }
            .upload()
    }

    private fun saveLocalAttachment(tmp: File, name: String): AttachmentExt {
        val fileName = UUID.randomUUID().toString()
        val file = UserInfo.userLocalFileDir(Current.userInfo).child(fileName)
        if (!tmp.moveOrCopy(file)) throw Exception()
        val a = Attachment(fileName, name, file.length())
        return AttachmentExt(a, file, false)
    }

    //20 seconds limit.
    fun sendAudio(roomId: String, uri: Uri, ext: String?) {
        val tmp = Ctx.context.contentResolver.readToTmpFile(uri, tmpDir) ?: return
        Completable
            .fromCallable {
                val name = "${timeString()}${ext.orEmpty()}"
                val att = saveLocalAttachment(tmp, name)
                val message = ChatMessagePending.audioMessage(roomId, att)
                Current.chatDB.getInstance().chatMessagePendingDao().insert(message)
            }
            .upload()
    }

    fun sendVideo(roomId: String, uri: Uri, extension: String) {
        val tmp = Ctx.context.contentResolver.readToTmpFile(uri, tmpDir) ?: return
        Completable
            .fromCallable {
                val name = "${timeString()}$extension"
                val att = saveLocalAttachment(tmp, name)
                val message = ChatMessagePending.videoMessage(roomId, att)
                Current.chatDB.getInstance().chatMessagePendingDao().insert(message)
            }
            .upload()
    }

    fun sendFile(roomId: String, uri: Uri) {
        val r = Ctx.context.contentResolver
        val name = uri.displayName(r)
        val tmp = r.readToTmpFile(uri, tmpDir) ?: return
        Completable
            .fromCallable {
                val att = saveLocalAttachment(tmp, name ?: "unknown")
                val message = ChatMessagePending.fileMessage(roomId, att)
                Current.chatDB.getInstance().chatMessagePendingDao().insert(message)
            }
            .upload()
    }

    private fun Completable.upload() {
        subscribeOn(Schedulers.io())
            .subscribe(Functions.EMPTY_ACTION, errConsumer)
            .disposeOnClear()
    }

    @JvmField
    val deletingMessageLiveData = MutableLiveData<Boolean>()

    fun deleteMessage(msg: MessageItem) {
        vmScope.launch(Dispatchers.IO) {
            when (msg) {
                is MessageItem.Pending -> chatRepository.deletePendingMessage(msg.pending)
                is MessageItem.Sent -> {
                    chatRepository.deleteMessage(msg.msg)
                        .toSingleDefault(Unit)
                        .toResultWithLoading(200)
                        .doFinally { deletingMessageLiveData.postValue(false) }
                        .onTerminateDetach()
                        .subscribe(
                            {
                                if (it.isLoading()) {
                                    deletingMessageLiveData.postValue(true)
                                }
                                it.runOnValue {
                                    Current.chatDB.getInstance().chatMessageDao().delete(msg.msg)
                                }
                                it.runOnError(errConsumer::accept)
                            },
                            errConsumer
                        )
                        .disposeOnClear()
                }
            }
        }
    }

    @JvmField
    val player = RxAudioPlayerHelper()
}
