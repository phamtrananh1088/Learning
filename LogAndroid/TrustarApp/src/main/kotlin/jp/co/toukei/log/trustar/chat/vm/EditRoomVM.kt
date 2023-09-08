package jp.co.toukei.log.trustar.chat.vm

import androidx.lifecycle.toLiveData
import io.reactivex.rxjava3.processors.BehaviorProcessor
import jp.co.toukei.log.lib.asEvent
import jp.co.toukei.log.lib.subscribeOrIgnore
import jp.co.toukei.log.lib.toResultWithLoading
import jp.co.toukei.log.trustar.common.CommonViewModel
import jp.co.toukei.log.trustar.repo.chat.ChatRoomRepository
import jp.co.toukei.log.trustar.rest.model.UserLite
import third.Event
import third.Result

class EditRoomVM : CommonViewModel() {

    private val saveState = BehaviorProcessor.create<Event<Result<Unit>>>()

    @JvmField
    val stateLiveData = saveState.toLiveData()

    private val chatRoomRepository = ChatRoomRepository()

    fun editRoom(roomId: String, users: List<UserLite>) {
        chatRoomRepository.editRoom(roomId, users)
            .toSingleDefault(Unit)
            .toResultWithLoading()
            .subscribeOrIgnore { saveState.onNext(it.asEvent()) }
            .disposeOnClear()
    }
}
