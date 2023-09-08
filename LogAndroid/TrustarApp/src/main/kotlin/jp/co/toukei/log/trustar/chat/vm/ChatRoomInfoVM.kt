package jp.co.toukei.log.trustar.chat.vm

import androidx.lifecycle.toLiveData
import io.reactivex.rxjava3.processors.BehaviorProcessor
import jp.co.toukei.log.lib.replayThenAutoConnect
import jp.co.toukei.log.trustar.chat.ChatRoomWithUsers
import jp.co.toukei.log.trustar.common.CommonViewModel
import jp.co.toukei.log.trustar.repo.chat.ChatRoomRepository
import org.reactivestreams.Publisher
import java.util.Optional

open class ChatRoomInfoVM : CommonViewModel() {

    private val roomSource = BehaviorProcessor.create<Publisher<Optional<ChatRoomWithUsers>>>()

    protected val chatRoomWithUsers =
        roomSource.switchMap { it }.replayThenAutoConnect(disposableContainer)

    @JvmField
    val roomData = chatRoomWithUsers.toLiveData()

    private val chatRoomRepository = ChatRoomRepository()

    fun loadRoom(roomId: String) {
        val o = chatRoomRepository.chatRoomWithUsers(roomId)
        roomSource.onNext(o)
    }
}
