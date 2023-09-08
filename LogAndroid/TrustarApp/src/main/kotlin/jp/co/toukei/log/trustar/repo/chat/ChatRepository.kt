package jp.co.toukei.log.trustar.repo.chat

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.functions.Consumer
import jp.co.toukei.log.lib.inTransaction
import jp.co.toukei.log.lib.observeOnIO
import jp.co.toukei.log.lib.rxConsumer
import jp.co.toukei.log.lib.subscribeOnIO
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.db.chat.ChatDB
import jp.co.toukei.log.trustar.db.chat.entity.ChatMessage
import jp.co.toukei.log.trustar.db.chat.entity.ChatMessagePending
import jp.co.toukei.log.trustar.rest.model.MessageItem
import jp.co.toukei.log.trustar.rest.post.DefaultPostContent
import jp.co.toukei.log.trustar.rest.post.MessageItemRows
import jp.co.toukei.log.trustar.toCompletable
import java.util.LinkedList

class ChatRepository(@JvmField val chatDB: ChatDB) {

    private val api = Current.chatApi

    fun updateRead(messageRow: String): Completable {
        return api.updateRead(MessageItemRows(listOf(messageRow)))
            .subscribeOnIO()
            .toCompletable()
    }

    fun loadBottomMessage(roomId: String, pageSize: Int): Single<List<MessageItem>> {
        return Single
            .defer {
                val oldest = localLatestSentMessage(roomId, 1).firstOrNull()?.id
                if (oldest != null) api.getMessageNext(roomId, oldest, pageSize)
                else api.getMessageItems(roomId, pageSize)
            }
            .subscribeOnIO()
            .observeOnIO()
            .doOnSuccess(insertMessagesConsumer())
    }

    fun loadTopMessage(roomId: String, pageSize: Int): Single<List<MessageItem>> {
        return Single
            .defer {
                val oldest = chatDB.chatMessageDao().messageOldest(roomId, 1).firstOrNull()?.id
                if (oldest != null) api.getMessagePrevious(roomId, oldest, pageSize)
                else api.getMessageItems(roomId, pageSize)
            }
            .subscribeOnIO()
            .observeOnIO()
            .doOnSuccess(insertMessagesConsumer())
    }

    private fun insertMessagesConsumer(): Consumer<List<MessageItem>> {
        return rxConsumer { insertMessages(it.map(MessageItem::message)) }
    }

    private fun insertMessages(iterable: Iterable<ChatMessage>) {
        val gs = iterable.groupBy { it.roomId }
        val deleted = LinkedList<ChatMessagePending>()
        chatDB.inTransaction {
            gs.forEach { (t, u) ->
                chatMessageDao().insertOrReplace(u)
                deleted += chatMessagePendingDao().selectSent(t)
                chatMessagePendingDao().deleteSent(t)
            }
        }
        deleted.forEach {
            runCatching {
                val att = ChatMessagePending.attachmentExt(it)
                if (att?.isCacheFile == false) {
                    att.file.delete()
                }
            }
        }
    }

    fun deletePendingMessage(pending: ChatMessagePending) {
        deletePendingMessage(listOf(pending))
    }

    fun deleteMessage(msg: ChatMessage): Completable {
        return api.deleteMessage(DefaultPostContent.messageId(msg.id))
            .subscribeOnIO()
            .toCompletable()
            .doOnComplete {
                ChatMessage.attachmentFile(msg)?.file?.delete()
            }
    }

    private fun deletePendingMessage(pending: List<ChatMessagePending>) {
        chatDB.inTransaction {
            chatMessagePendingDao().delete(pending)
        }
        pending.forEach {
            runCatching { ChatMessagePending.attachmentExt(it)?.file?.delete() }
        }
    }

    fun localPendingMessages(roomId: String): List<ChatMessagePending> {
        return chatDB.chatMessagePendingDao().messageByRoom(roomId)
    }

    fun localPendingMessagesPrev(
        roomId: String,
        createdDate: Long,
        includeSelf: Boolean,
    ): List<ChatMessagePending> {
        return if (includeSelf) {
            chatDB.chatMessagePendingDao().messagePrevIncluded(roomId, createdDate)
        } else {
            chatDB.chatMessagePendingDao().messagePrev(roomId, createdDate)
        }
    }

    fun localPendingMessagesNext(roomId: String, createdDate: Long): List<ChatMessagePending> {
        return chatDB.chatMessagePendingDao().messageNext(roomId, createdDate)
    }

    fun localLatestSentMessage(roomId: String, pageSize: Int): List<ChatMessage> {
        return chatDB.chatMessageDao().messageLatest(roomId, pageSize)
    }

    fun localMessagesPrev(roomId: String, pageSize: Int, startR: String): List<ChatMessage> {
        return chatDB.chatMessageDao().messagePrev(roomId, pageSize, startR)
    }

    fun localMessagesNext(roomId: String, pageSize: Int, startR: String): List<ChatMessage> {
        return chatDB.chatMessageDao().messageNext(roomId, pageSize, startR)
    }

    fun localMessagesBetween(roomId: String, startR: String, endR: String): List<ChatMessage> {
        return chatDB.chatMessageDao().messageBetween(roomId, startR, endR)
    }

    fun localMessagesBetweenAround(
        roomId: String,
        startR: String,
        endR: String,
        prepend: Int,
        append: Int,
    ): List<ChatMessage> {
        val p = localMessagesPrev(roomId, prepend, startR)
        val b = localMessagesBetween(roomId, startR, endR)
        val n = localMessagesNext(roomId, append, endR)
        return ArrayList<ChatMessage>(p.size + b.size + n.size).apply {
            addAll(p)
            addAll(b)
            addAll(n)
        }
    }
}
