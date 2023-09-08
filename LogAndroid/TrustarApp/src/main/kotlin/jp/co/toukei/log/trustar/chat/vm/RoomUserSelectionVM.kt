package jp.co.toukei.log.trustar.chat.vm

import androidx.lifecycle.LiveData
import jp.co.toukei.log.lib.zip
import jp.co.toukei.log.trustar.repo.chat.ChatRoomRepository
import jp.co.toukei.log.trustar.rest.model.TalkUser

class RoomUserSelectionVM : UserSelectionVM<TalkUser>() {

    private val chatRoomRepository = ChatRoomRepository()

    fun loadUsers() {
        val source = arrayOf(
            chatRoomRepository.getHistoryUsers(),
            chatRoomRepository.getAllUsers()
        )
        fetch.onNext(source.zip().toFlowable())
    }

    @JvmField
    val selectedLiveData: LiveData<List<UserCheck<TalkUser>>> = topList

    @JvmField
    val groupListLiveData: LiveData<List<List<UserCheck<TalkUser>>>> = displayList
}
