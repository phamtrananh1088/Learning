package jp.co.toukei.log.trustar.viewmodel

import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.processors.BehaviorProcessor
import jp.co.toukei.log.lib.mapOptionalOrEmpty
import jp.co.toukei.log.lib.toResultWithLoading
import jp.co.toukei.log.lib.util.CheckUser
import jp.co.toukei.log.lib.zip
import jp.co.toukei.log.trustar.common.CommonViewModel
import jp.co.toukei.log.trustar.compose.ComposeData.SelectChatUser
import jp.co.toukei.log.trustar.db.chat.entity.ChatUser
import jp.co.toukei.log.trustar.repo.chat.ChatRoomRepository
import jp.co.toukei.log.trustar.rest.model.TalkUser
import jp.co.toukei.log.trustar.user.LoggedUser
import third.Result
import java.util.Optional

class ChatSelectUserVM(
    val user: LoggedUser,
) : CommonViewModel() {

    private val fetch: BehaviorProcessor<Flowable<out List<List<SelectChatUser>>>> =
        BehaviorProcessor.create()

    private val chatRoomRepository = ChatRoomRepository()


    @JvmField
    val checkUser = CheckUser(
        disposableContainer = disposableContainer,
        userId = { id },
        userName = { name },
        keepState = { false },
        fetch = fetch
    )

    @JvmField
    val selectedUsers = checkUser.checkedResult

    @JvmField
    val displayList0 = checkUser.displayListR.map { it.getOrNull(0) ?: emptyList() }

    @JvmField
    val displayList1 = checkUser.displayListR.map { it.getOrNull(1) ?: emptyList() }

    fun setQueryText(query: CharSequence?) {
        checkUser.setQueryText(query)
    }

    fun excludeIds(ids: Iterable<String>?) {
        checkUser.excludeIds(ids)
    }

    @JvmField
    val state: Flowable<out Result<Any>> = checkUser.state

    fun selectId(id: String, select: Boolean) = checkUser.selectId(id, select)

    fun loadUsers() {
        fun TalkUser.todo() = SelectChatUser(userId, companyCd, userName, avatarImageUrl)
        val source = arrayOf(
            chatRoomRepository.getHistoryUsers().map { it.map { it.todo() } },
            chatRoomRepository.getAllUsers().map { it.map { it.todo() } }
        )
        fetch.onNext(source.zip().toFlowable())
    }

    fun selectedUser() = checkUser.selectedUser()

    fun editRoom(roomId: String, users: List<SelectChatUser>): Flowable<Result<Unit>> {
        return chatRoomRepository.editRoom(roomId, users)
            .toSingleDefault(Unit)
            .toResultWithLoading()
    }

    fun addRoom(users: List<SelectChatUser>): Flowable<Result<Unit>> {
        return chatRoomRepository.addRoom(users)
            .toSingleDefault(Unit)
            .toResultWithLoading()
    }

    fun roomUserIds(roomId: String): Flowable<Optional<Set<String>>> {
        return chatRoomRepository.chatRoomWithUsers(roomId)
            .mapOptionalOrEmpty {
                it.users.mapTo(linkedSetOf(), ChatUser::id)
            }
    }
}