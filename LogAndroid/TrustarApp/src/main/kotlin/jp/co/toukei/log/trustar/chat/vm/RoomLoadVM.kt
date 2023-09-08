package jp.co.toukei.log.trustar.chat.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.toLiveData
import io.reactivex.rxjava3.processors.BehaviorProcessor
import jp.co.toukei.log.lib.flatMap
import jp.co.toukei.log.lib.observeOnUI
import jp.co.toukei.log.lib.replayThenAutoConnect
import jp.co.toukei.log.lib.subscribe
import jp.co.toukei.log.lib.subscribeOnIO
import jp.co.toukei.log.lib.toResultWithLoading
import jp.co.toukei.log.trustar.common.CommonViewModel
import jp.co.toukei.log.trustar.repo.chat.ChatRoomRepository
import third.Result

class RoomLoadVM : CommonViewModel() {

    private val loadRoom = BehaviorProcessor.create<Boolean>()

    @JvmField
    val loadStateLiveData = MutableLiveData<Result<Unit>>()

    private val chatRoomRepository = ChatRoomRepository()

    init {
        loadRoom.onBackpressureDrop()
            .flatMap(false, 1) {
                chatRoomRepository.reloadRooms()
                    .toSingleDefault(Unit)
                    .toResultWithLoading(200)
                    .subscribeOnIO()
            }
            .observeOnUI()
            .subscribe(loadStateLiveData)
            .disposeOnClear()
    }

    fun load() {
        loadRoom.onNext(false)
    }

    @JvmField
    val listLiveData = chatRoomRepository.sortedRoomList()
        .replayThenAutoConnect(disposableContainer)
        .toLiveData()
}
