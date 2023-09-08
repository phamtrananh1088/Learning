package jp.co.toukei.log.trustar.chat

import android.util.ArrayMap
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.internal.disposables.ListCompositeDisposable
import io.reactivex.rxjava3.internal.functions.Functions
import jp.co.toukei.log.lib.addTo
import jp.co.toukei.log.lib.delayedRetry
import jp.co.toukei.log.lib.flatMapCompletable
import jp.co.toukei.log.lib.moveOrCopy
import jp.co.toukei.log.lib.observeOnIO
import jp.co.toukei.log.lib.orElseNull
import jp.co.toukei.log.lib.subscribeOnIO
import jp.co.toukei.log.lib.subscribeOrIgnore
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.db.chat.Attachment
import jp.co.toukei.log.trustar.db.chat.ChatDB
import jp.co.toukei.log.trustar.db.chat.ChatMessagePendingDao
import jp.co.toukei.log.trustar.db.chat.entity.ChatMessagePending
import jp.co.toukei.log.trustar.repo.chat.ChatRepository
import jp.co.toukei.log.trustar.rest.model.FileKey
import jp.co.toukei.log.trustar.rest.model.FileKey2
import jp.co.toukei.log.trustar.toState
import jp.co.toukei.log.trustar.user.UserInfo
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.util.Collections
import java.util.concurrent.TimeUnit

private class FileRepository {

    fun msgUploadFile(filename: String, body: RequestBody): Single<FileKey> {
        return msgUploadFile2(filename, body).map { it.key }
    }

    fun msgUploadFile2(filename: String, body: RequestBody): Single<FileKey2> {
        val p = MultipartBody.Part.createFormData("buffer", filename, body)
        return Current.chatApi.uploadFile(p)
    }

    fun msgUploadImage(filename: String, body: RequestBody): Single<FileKey> {
        val p = MultipartBody.Part.createFormData("buffer", filename, body)
        return Current.chatApi.uploadImage(p)
    }

    fun msgUploadVideo(filename: String, body: RequestBody): Single<FileKey2> {
        return msgUploadFile2(filename, body)
    }
}

class MessageTask : Disposable {

    private val fileRepository = FileRepository()

    private val dc = ListCompositeDisposable()
    private val tasks = Collections.synchronizedMap(ArrayMap<String, Disposable>())

    fun observePendingChanges() {
        dc.clear()
        tasks.clear()
        val db = Current.chatDB.getInstance()
        db.chatMessagePendingDao().unsentOrSendingRoomIds()
            .subscribeOrIgnore { l ->
                l.forEach { roomTask(db, it.id) }
            }
            .addTo(dc)
    }

    //buggy?
    private fun roomTask(db: ChatDB, id: String): Boolean {
        val d = tasks[id]
        if (d != null && !d.isDisposed) return false
        val t = db.sendAndReceiveMessage(id)
            .doFinally {
                tasks.remove(id)?.let(dc::remove)
            }
            .subscribe(
                Functions.EMPTY_ACTION,
                Functions.emptyConsumer()
            )
        t.addTo(dc)
        tasks.put(id, t)?.let(dc::remove)
        return true
    }

    fun sendAndReceiveMessage(roomId: String) {
        roomTask(Current.chatDB.getInstance(), roomId)
    }

    private fun <T : Any> Maybe<T>.doOnUploadError(dao: ChatMessagePendingDao, id: Long): Maybe<T> {
        return doOnError {
            dao.setStatus(ChatMessagePending.STATE_ERR, id)
        }
    }

    private fun ChatDB.sendAndReceiveMessage(roomId: String): Completable {
        val repo = ChatRepository(this)
        val dao = chatMessagePendingDao()
        return Flowable
            .generate<ChatMessagePending> {
                val f = dao.firstUnsentOrSending(roomId).orElseNull()
                if (f == null)
                    it.onComplete()
                else
                    it.onNext(f)
            }
            // todo
            .subscribeOnIO()
            .flatMapCompletable(false, 1) { pending ->
                val id = pending.id
                dao.setStatus(ChatMessagePending.STATE_SENDING, id)

                when (pending.type) {
                    ChatMessagePending.TYPE_IMG_SEND -> Maybe
                        .defer {
                            val m = dao.selectById(id)
                            val f = m?.let { ChatMessagePending.attachmentExt(it) }
                            if (m == null || f == null || !f.file.canRead()) {
                                Maybe.fromAction { repo.deletePendingMessage(pending) }
                            } else fileRepository.msgUploadImage(f.name, f.file.asRequestBody())
                                .map { m to it }
                                .toMaybe()
                        }
                        .observeOnIO()
                        .delayedRetry(2, 2, TimeUnit.SECONDS)
                        .doOnSuccess { v ->
                            val n = ChatMessagePending.imageKeyMessage(
                                v.first,
                                v.second.fileKey
                            )
                            dao.updateOrIgnore(n)
                        }
                        .doOnUploadError(dao, id)
                        .ignoreElement()
                        .onErrorComplete()

                    ChatMessagePending.TYPE_AUDIO_SEND -> Maybe
                        .defer {
                            val m = dao.selectById(id)
                            val f = m?.let { ChatMessagePending.attachmentExt(it) }
                            if (m == null || f == null || !f.file.canRead()) {
                                Maybe.fromAction { repo.deletePendingMessage(pending) }
                            } else fileRepository.msgUploadFile(f.name, f.file.asRequestBody())
                                .map { m to it }
                                .toMaybe()
                        }
                        .observeOnIO()
                        .delayedRetry(1, 2, TimeUnit.SECONDS)
                        .doOnSuccess { v ->
                            val n = ChatMessagePending.audioKeyMessage(
                                v.first,
                                v.second.fileKey
                            )
                            dao.updateOrIgnore(n)
                        }
                        .doOnUploadError(dao, id)
                        .ignoreElement()
                        .onErrorComplete()

                    ChatMessagePending.TYPE_VIDEO_SEND -> Maybe
                        .defer {
                            val m = dao.selectById(id)
                            val f = m?.let { ChatMessagePending.attachmentExt(it) }
                            if (m == null || f == null || !f.file.canRead()) {
                                Maybe.fromAction { repo.deletePendingMessage(pending) }
                            } else fileRepository.msgUploadVideo(f.name, f.file.asRequestBody())
                                .map { Triple(m, it, f) }
                                .toMaybe()
                        }
                        .observeOnIO()
                        .delayedRetry(1, 2, TimeUnit.SECONDS)
                        .doOnSuccess { v ->
                            val (old, uploaded, oldAtt) = v
                            val newAtt = Attachment(
                                uploaded.realFileKey,
                                oldAtt.name,
                                oldAtt.size
                            )
                            val n = ChatMessagePending.videoKeyMessage(
                                old,
                                newAtt,
                                uploaded.key.fileKey
                            )
                            dao.updateOrIgnore(n)
                            UserInfo.cacheFileByKey(Current.userInfo, newAtt.key).let {
                                oldAtt.file.moveOrCopy(it.file)
                                oldAtt.file.delete()
                            }
                        }
                        .doOnUploadError(dao, id)
                        .ignoreElement()
                        .onErrorComplete()

                    ChatMessagePending.TYPE_FILE_SEND -> Maybe
                        .defer {
                            val m = dao.selectById(id)
                            val f = m?.let { ChatMessagePending.attachmentExt(it) }
                            if (m == null || f == null || !f.file.canRead()) {
                                Maybe.fromAction { repo.deletePendingMessage(pending) }
                            } else fileRepository.msgUploadFile2(f.name, f.file.asRequestBody())
                                .map { Triple(m, it, f) }
                                .toMaybe()
                        }
                        .observeOnIO()
                        .delayedRetry(1, 2, TimeUnit.SECONDS)
                        .doOnSuccess { v ->
                            val (old, uploaded, oldAtt) = v
                            val newAtt = Attachment(
                                uploaded.realFileKey,
                                oldAtt.name,
                                oldAtt.size
                            )
                            val n = ChatMessagePending.fileKeyMessage(
                                old,
                                newAtt,
                                uploaded.key.fileKey
                            )
                            dao.updateOrIgnore(n)
                            UserInfo.cacheFileByKey(Current.userInfo, newAtt.key).let {
                                oldAtt.file.moveOrCopy(it.file)
                                oldAtt.file.delete()
                            }
                        }
                        .doOnUploadError(dao, id)
                        .ignoreElement()
                        .onErrorComplete()

                    else -> Maybe
                        .defer {
                            val m = dao.selectById(id)
                            if (m == null) Maybe.fromAction { repo.deletePendingMessage(pending) }
                            else Current.chatApi.sendMessage(m).toState().toMaybe()
                        }
                        .observeOnIO()
                        .doOnDispose {
                            dao.setStatus(ChatMessagePending.STATE_NORMAL, id)
                        }
                        .delayedRetry(2, 2, TimeUnit.SECONDS)
                        .doOnSuccess { v ->
                            dao.setStatusAndTargetId(
                                ChatMessagePending.STATE_SENT,
                                v.orElseNull(),
                                id
                            )
                        }
                        .doOnError {
                            dao.setStatusAndTargetId(
                                ChatMessagePending.STATE_ERR,
                                null,
                                id
                            )
                        }
                        .ignoreElement()
                        .onErrorComplete()
                }
            }
            .andThen(Completable.defer {
                repo.loadBottomMessage(roomId, 80).ignoreElement()
            })
    }

    override fun isDisposed(): Boolean {
        return dc.isDisposed
    }

    override fun dispose() {
        dc.dispose()
    }
}

