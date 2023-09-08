package jp.co.toukei.log.trustar.chat

import android.util.ArrayMap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData

object PushMessage {

    class Msg(
        @JvmField val message: PushMsg,
        @JvmField val history: List<PushMsg>,
    )

    data class PushMsg(
        val title: String,
        val body: String,
        val roomId: String,
    )

    private val firebaseMsg = MutableLiveData<Msg?>()

    @JvmField
    val pushMessageLiveData = firebaseMsg

    private val observerSet = hashSetOf<L<List<PushMsg>>>()

    fun postMessage(msg: Msg?) {
        firebaseMsg.postValue(msg)
    }

    fun observingMessage(roomId: String): Boolean {
        return observerSet.any { it.key == roomId }
    }

    fun observeMessage(roomId: String): LiveData<List<PushMsg>> {
        return L(observerSet, roomId).also { m ->
            m.addSource(firebaseMsg) {
                if (it == null) m.value = null
                else if (roomId == it.message.roomId) m.value = it.history
            }
        }
    }

    private class L<T>(
        private val set: MutableSet<L<T>>,
        @JvmField val key: String,
    ) : MediatorLiveData<T>() {

        override fun onActive() {
            set.add(this)
            super.onActive()
        }

        override fun onInactive() {
            set.remove(this)
            super.onInactive()
        }
    }


    ///////////
    private val messageMap = ArrayMap<String, ArrayList<PushMsg>>()

    fun addPushMsg(roomId: String, msg: PushMsg): ArrayList<PushMsg> {
        return messageMap.getOrPut(roomId, ::ArrayList).apply { add(msg) }
    }

    fun clearNotification(roomId: String? = null) {
        if (roomId == null) messageMap.clear() else messageMap.remove(roomId)
    }

    fun notificationId(roomId: String): Int {
        return messageMap.indexOfKey(roomId) + 0x10000000
    }

}
