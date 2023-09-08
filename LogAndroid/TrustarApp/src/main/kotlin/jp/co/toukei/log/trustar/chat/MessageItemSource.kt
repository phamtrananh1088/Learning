package jp.co.toukei.log.trustar.chat

import android.annotation.SuppressLint
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.room.InvalidationTracker
import jp.co.toukei.log.lib.chains
import jp.co.toukei.log.lib.forEachPrevNext
import jp.co.toukei.log.trustar.db.chat.entity.ChatMessage
import jp.co.toukei.log.trustar.db.chat.entity.ChatMessagePending
import jp.co.toukei.log.trustar.db.chat.entity.ChatUser
import jp.co.toukei.log.trustar.repo.chat.ChatRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MessageItemSource(
    private val roomId: String,
    private val repo: ChatRepository,
    private val userGetter: (userId: String) -> ChatUser?,
    private val readStatus: (ChatMessage) -> List<ChatUser?>?,
) : PagingSource<MessageItemSource.KEY, MessageItem>() {

    sealed class KEY {

        class State(@JvmField val first: MessageItem, @JvmField val last: MessageItem) : KEY()

        class Sent(@JvmField val m: MessageItem.Sent) : KEY()

        class Pending(@JvmField val m: MessageItem.Pending) : KEY()
    }

    private val dbObserver = object :
        InvalidationTracker.Observer(ChatMessage.TABLE_NAME, ChatMessagePending.TABLE_NAME) {
        override fun onInvalidated(tables: Set<String>) {
            invalidate()
        }
    }

    init {
        @Suppress("UNUSED_VARIABLE")
        @SuppressLint("RestrictedApi")
        val o = repo.chatDB.invalidationTracker.addWeakObserver(dbObserver)
    }

    override suspend fun load(params: LoadParams<KEY>) = withContext(Dispatchers.IO) {
        val loadSize = params.loadSize
        val list = when (params) {
            is LoadParams.Refresh -> when (val k = params.key) {
                is KEY.State -> when (val f = k.first) {
                    is MessageItem.Pending -> {
                        latestSent(loadSize).chains(allPending()).toList()
                    }

                    is MessageItem.Sent -> when (val l = k.last) {
                        is MessageItem.Pending -> {
                            val s = repo.localMessagesBetweenAround(
                                roomId,
                                f.msg.messageRow,
                                f.msg.messageRow,
                                loadSize,
                                Int.MAX_VALUE
                            )
                            s.sortedByRow()
                                .chains(allPending())
                                .toList()
                        }

                        is MessageItem.Sent -> {
                            val s = repo.localMessagesBetweenAround(
                                roomId,
                                f.msg.messageRow,
                                l.msg.messageRow,
                                loadSize,
                                loadSize
                            )
                            if (s.isEmpty()) allPending().toList()
                            else s.sortedByRow().toList()
                        }
                    }
                }

                else -> latestSent(loadSize).chains(allPending()).toList()
            }

            is LoadParams.Append -> when (val k = params.key) {
                is KEY.Sent -> {
                    val key = k.m.msg.messageRow
                    repo.localMessagesNext(roomId, loadSize, key)
                        .sortedByRow()
                        .toList()
                        .takeUnless(List<*>::isEmpty) ?: allPending().toList()
                }

                is KEY.Pending -> nextPending(k.m.pending.createdDate).toList()
                else -> emptyList()
            }

            is LoadParams.Prepend -> when (val k = params.key) {
                is KEY.Sent -> {
                    val key = k.m.msg.messageRow
                    repo.localMessagesPrev(roomId, loadSize, key).sortedByRow().toList()
                }

                is KEY.Pending -> {
                    val key = k.m.pending.createdDate
                    prevPending(key)
                        .toList()
                        .takeUnless(List<*>::isEmpty)
                        ?: latestSent(loadSize).toList()
                }

                else -> emptyList()
            }
        }
        val prev = when (val it = list.firstOrNull()) {
            is MessageItem.Sent -> KEY.Sent(it)
            is MessageItem.Pending -> KEY.Pending(it)
            else -> null
        }
        val next = when (val it = list.lastOrNull()) {
            is MessageItem.Sent -> KEY.Sent(it)
            is MessageItem.Pending -> KEY.Pending(it)
            else -> null
        }
        LoadResult.Page(list, prev, next)
    }

    private fun allPending(): Iterable<MessageItem.Pending> {
        return repo.localPendingMessages(roomId).sortedByTime()
    }

    private fun nextPending(createdDate: Long): Iterable<MessageItem.Pending> {
        return repo.localPendingMessagesNext(roomId, createdDate).sortedByTime()
    }

    private fun prevPending(
        createdDate: Long,
        includeSelf: Boolean = false,
    ): Iterable<MessageItem.Pending> {
        return repo.localPendingMessagesPrev(roomId, createdDate, includeSelf).sortedByTime()
    }

    private fun latestSent(size: Int): Iterable<MessageItem.Sent> {
        return repo.localLatestSentMessage(roomId, size).sortedByRow()
    }

    private fun Iterable<ChatMessage>.sortedByRow(): Iterable<MessageItem.Sent> {
        val list = sortedBy { it.messageRow }
        val target = ArrayList<MessageItem.Sent>(list.size)
        list.forEachPrevNext { prev, it, next ->
            val uid = it.userId
            val p = prev?.userId == uid
            val n = next?.userId == uid

            target += when (it.type) {
                ChatMessage.TYPE_VIDEO -> {
                    MessageItem.Sent.VideoSent(it, userGetter(it.userId), readStatus(it), p, n)
                }

                ChatMessage.TYPE_AUDIO -> {
                    MessageItem.Sent.AudioSent(it, userGetter(it.userId), readStatus(it), p, n)
                }

                ChatMessage.TYPE_IMG -> {
                    MessageItem.Sent.ImgSent(it, userGetter(it.userId), readStatus(it), p, n)
                }

                ChatMessage.TYPE_FILE -> {
                    MessageItem.Sent.FileSent(it, userGetter(it.userId), readStatus(it), p, n)
                }

                else -> MessageItem.Sent.TextSent(it, userGetter(it.userId), readStatus(it), p, n)
            }
        }
        return target
    }

    private fun Iterable<ChatMessagePending>.sortedByTime(): Iterable<MessageItem.Pending> {
        val list = sortedBy { it.createdDate }
        val target = ArrayList<MessageItem.Pending>(list.size)
        list.forEachPrevNext { prev, it, next ->
            val uid = it.userId
            val p = prev?.userId == uid
            val n = next?.userId == uid

            target += when (it.type) {
                ChatMessagePending.TYPE_IMG_SEND, ChatMessagePending.TYPE_IMG -> {
                    MessageItem.Pending.ImgPending(it, p, n)
                }

                ChatMessagePending.TYPE_AUDIO_SEND, ChatMessagePending.TYPE_AUDIO -> {
                    MessageItem.Pending.AudioPending(it, p, n)
                }

                ChatMessagePending.TYPE_VIDEO_SEND, ChatMessagePending.TYPE_VIDEO -> {
                    MessageItem.Pending.VideoPending(it, p, n)
                }

                ChatMessagePending.TYPE_FILE_SEND, ChatMessagePending.TYPE_FILE -> {
                    MessageItem.Pending.FilePending(it, p, n)
                }

                else -> MessageItem.Pending.TextPending(it, p, n)
            }
        }
        return target
    }

    override fun getRefreshKey(state: PagingState<KEY, MessageItem>): KEY? {
        val f = state.firstItemOrNull()
        val l = state.lastItemOrNull()
        return if (f != null && l != null) KEY.State(f, l) else null
    }
}
