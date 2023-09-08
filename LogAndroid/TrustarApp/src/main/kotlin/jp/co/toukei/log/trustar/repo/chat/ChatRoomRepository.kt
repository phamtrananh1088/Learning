package jp.co.toukei.log.trustar.repo.chat

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import jp.co.toukei.log.lib.inTransaction
import jp.co.toukei.log.lib.observeOnIO
import jp.co.toukei.log.lib.orElseNull
import jp.co.toukei.log.lib.subscribeOnIO
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.chat.ChatRoomWithUsers
import jp.co.toukei.log.trustar.chat.ChatRoomWithUsers2
import jp.co.toukei.log.trustar.compose.ComposeData
import jp.co.toukei.log.trustar.db.chat.entity.ChatRoom
import jp.co.toukei.log.trustar.db.chat.entity.ChatRoomExt
import jp.co.toukei.log.trustar.db.chat.entity.ChatRoomUser
import jp.co.toukei.log.trustar.db.chat.entity.ChatUser
import jp.co.toukei.log.trustar.rest.model.TalkUser
import jp.co.toukei.log.trustar.rest.post.ChatUserSettings
import jp.co.toukei.log.trustar.rest.post.MessageItemRows
import jp.co.toukei.log.trustar.rest.post.RoomAddMember
import jp.co.toukei.log.trustar.rest.post.RoomCreate
import jp.co.toukei.log.trustar.toCompletable
import java.util.Optional
import java.util.UUID
import java.util.concurrent.TimeUnit

class ChatRoomRepository {

    private val api = Current.chatApi

    fun chatRoomWithUsers(roomId: String): Flowable<Optional<ChatRoomWithUsers>> {
        return Current.chatDB.getInstance().chatRoomDao().selectRoom(roomId)
            .switchMap {
                val r = it.orElseNull()
                if (r == null) Flowable.just(Optional.ofNullable<ChatRoomWithUsers>(null))
                else {
                    Current.chatDB.getInstance().chatRoomUserDao().userListByRoom(r.id)
                        .map { u -> Optional.ofNullable(ChatRoomWithUsers(r, u)) }
                }
            }
            .throttleWithTimeout(100, TimeUnit.MILLISECONDS)
            .subscribeOnIO()
    }

    fun chatRoomWithUsers2(roomId: String): Flowable<Optional<ChatRoomWithUsers2>> {
        return Current.chatDB.getInstance().chatRoomDao().selectRoom(roomId)
            .switchMap {
                val r = it.orElseNull()
                if (r == null) Flowable.just(Optional.ofNullable<ChatRoomWithUsers2>(null))
                else {
                    Current.chatDB.getInstance().chatRoomUserDao().userListByRoomWithReadRow(r.id)
                        .map { u -> Optional.ofNullable(ChatRoomWithUsers2(r, u)) }
                }
            }
            .throttleWithTimeout(100, TimeUnit.MILLISECONDS)
            .subscribeOnIO()
    }

    fun reloadRooms(): Completable {
        return Single.defer { api.getData() }
            .observeOnIO()
            .subscribeOnIO()
            .flatMapCompletable { rooms ->
                val delRows = ArrayList<String>()
                Current.chatDB.getInstance().inTransaction {
                    val rs = ArrayList<ChatRoom>(rooms.size)
                    val s = rooms.sumOf { it.users?.size ?: 0 }
                    val us = ArrayList<ChatUser>(s)
                    val cs = ArrayList<ChatRoomUser>(s)

                    val version = UUID.randomUUID().toString()
                    rooms.forEach { r ->
                        rs += ChatRoom(
                            r.talkRoomId,
                            r.talkRoomName,
                            r.talkRoomImageUrl,
                            r.unreadCount ?: 0,
                            r.userCount ?: 0,
                            r.lastUpdateDatetime ?: 0L,
                            r.notificationFlag ?: false,
                            version
                        )
                        r.users?.forEach { u ->
                            cs += ChatRoomUser(r.talkRoomId, u.userId, r.readRowsByUser[u.userId])
                            us += ChatUser(u.userId, u.userName, u.mailAddress, u.avatarImageUrl)
                        }
                    }
                    chatRoomDao().apply {
                        updateOrIgnore(rs)
                        insert(rs)
                        deleteNot(version)
                    }
                    chatUserDao().apply {
                        deleteAll()
                        insertOrReplace(us.distinctBy(ChatUser::id))
                    }
                    chatRoomUserDao().apply {
                        deleteAll()
                        insertOrReplace(cs)
                    }
                    val ids = ArrayList<String>()
                    rooms.forEach { r ->
                        r.deletedMessageItems?.forEach { m ->
                            ids.add(m.messageId)
                            delRows.add(m.messageRowId)
                        }
                    }
                    if (delRows.isNotEmpty()) {
                        Current.chatDB.getInstance().chatMessageDao().deleteById(ids)
                    }
                }
                if (delRows.isEmpty()) {
                    Completable.complete()
                } else {
                    api.updateDelete(MessageItemRows(delRows)).toCompletable()
                }
            }
    }

    fun sortedRoomList(): Flowable<List<ChatRoomExt>> {
        return Flowable.defer {
            Current.chatDB.getInstance().chatRoomDao()
                .selectRoomList()
                .map { it.sortedByDescending { it.room.lastUpdated } }
        }
    }

    fun leaveRoom(roomId: String, reload: Boolean): Completable {
        return Single
            .defer {
                val it = ChatUserSettings(roomId, Current.userId, null, true)
                api.updateUserSetting(it)
            }
            .toCompletable()
            .observeOnIO()
            .doOnComplete { Current.chatDB.getInstance().chatRoomDao().deleteByRoomId(roomId) }
            .run {
                if (reload) andThen(reloadRooms())
                else this
            }
    }

    fun notificationOnOff(roomId: String, on: Boolean, reload: Boolean): Completable {
        return Single
            .defer {
                val it = ChatUserSettings(roomId, Current.userId, on, null)
                api.updateUserSetting(it)
            }
            .toCompletable()
            .run {
                if (reload) andThen(reloadRooms())
                else this
            }
    }

    fun getHistoryUsers(): Single<List<TalkUser>> {
        return api.getHistories().subscribeOnIO()
    }

    fun getAllUsers(): Single<List<TalkUser>> {
        return api.getAllUsers(false).subscribeOnIO()
    }

    fun unreadCount(): Flowable<Int> {
        return Current.chatDB.getInstance().chatRoomDao().selectRoomList().map { l ->
            l.sumOf { it.room.unread }
        }
    }

    fun addRoom(users: List<ComposeData.SelectChatUser>): Completable {
        return api.addRoom(RoomCreate(users))
            .observeOnIO()
            .toCompletable()
    }

    fun editRoom(roomId: String, users: List<ComposeData.SelectChatUser>): Completable {
        return api.addMember(RoomAddMember(roomId, users))
            .observeOnIO()
            .toCompletable()
    }
}
