package jp.co.toukei.log.trustar.chat.vm

import androidx.lifecycle.toLiveData
import io.reactivex.rxjava3.processors.BehaviorProcessor
import jp.co.toukei.log.lib.asEvent
import jp.co.toukei.log.lib.onErrorWrapComplete
import jp.co.toukei.log.lib.subscribeOnIO
import jp.co.toukei.log.lib.subscribeOrIgnore
import jp.co.toukei.log.lib.toResultWithLoading
import jp.co.toukei.log.trustar.repo.chat.ChatRoomRepository
import third.Event
import third.Result

class ChatOptionVM : ChatRoomInfoVM() {

    private val leaveState = BehaviorProcessor.create<Event<Result<Any>>>()

    private val notificationState = BehaviorProcessor.create<Event<Result<Any>>>()

    @JvmField
    val leaveLiveData = leaveState.toLiveData()

    @JvmField
    val notificationLiveData = notificationState.toLiveData()

    private val chatRoomRepository = ChatRoomRepository()

    fun leaveRoom(roomId: String) {
        chatRoomRepository.leaveRoom(roomId)
            .toResultWithLoading()
            .onErrorWrapComplete()
            .subscribeOnIO()
            .subscribeOrIgnore { leaveState.onNext(it.asEvent()) }
            .disposeOnClear()
    }

    fun notificationOnOff(roomId: String, on: Boolean) {
        chatRoomRepository.notificationOnOff(roomId, on)
            .toResultWithLoading()
            .onErrorWrapComplete()
            .subscribeOnIO()
            .subscribeOrIgnore { notificationState.onNext(it.asEvent()) }
            .disposeOnClear()
    }

}
