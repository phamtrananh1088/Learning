package jp.co.toukei.log.trustar.chat.vm

import io.reactivex.rxjava3.core.Flowable
import jp.co.toukei.log.lib.onErrorWrapComplete
import jp.co.toukei.log.lib.replayThenAutoConnect
import jp.co.toukei.log.lib.subscribeOnIO
import jp.co.toukei.log.lib.toResultWithLoading
import jp.co.toukei.log.trustar.common.CommonViewModel
import jp.co.toukei.log.trustar.repo.chat.ChatRoomRepository
import jp.co.toukei.log.trustar.user.LoggedUser
import third.Result

class ChatOptionVM(
    @JvmField val roomId: String,
    private val user: LoggedUser, //todo
) : CommonViewModel() {
    private val chatRoomRepository = ChatRoomRepository()

    @JvmField
    val chatRoomWithUsers = chatRoomRepository.chatRoomWithUsers(roomId)
        .replayThenAutoConnect(disposableContainer)

    fun leaveRoom(): Flowable<out Result<Any>> {
        return chatRoomRepository.leaveRoom(roomId, true)
            .toSingleDefault(Unit)
            .toResultWithLoading()
            .onErrorWrapComplete()
            .subscribeOnIO()
    }

    fun notificationOnOff(on: Boolean): Flowable<out Result<Any>> {
        return chatRoomRepository.notificationOnOff(roomId, on, true)
            .toSingleDefault(Unit)
            .toResultWithLoading()
            .onErrorWrapComplete()
            .subscribeOnIO()
    }
}
