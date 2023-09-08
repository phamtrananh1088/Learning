package jp.co.toukei.log.trustar.chat.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.MutableLiveData
import jp.co.toukei.log.lib.handle
import jp.co.toukei.log.lib.lazyViewModel
import jp.co.toukei.log.lib.loadingState
import jp.co.toukei.log.lib.observeNonNull
import jp.co.toukei.log.lib.observeNullable
import jp.co.toukei.log.lib.orElseNull
import jp.co.toukei.log.lib.peek
import jp.co.toukei.log.lib.runOnError
import jp.co.toukei.log.lib.runOnValue
import jp.co.toukei.log.lib.util.ProgressDialogWrapper
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.alertMsg
import jp.co.toukei.log.trustar.chat.vm.ChatRoomInfoVM
import jp.co.toukei.log.trustar.chat.vm.EditRoomVM
import jp.co.toukei.log.trustar.db.chat.entity.ChatUser
import jp.co.toukei.log.trustar.rest.model.Member

class EditRoom : RoomUserSelectionActivity() {

    private val vm by lazyViewModel<EditRoomVM>()
    private val roomInfoVM by lazyViewModel<ChatRoomInfoVM>()

    private lateinit var roomId: String

    override fun onCreate1(savedInstanceState: Bundle?) {
        roomId = intent?.getStringExtra(ARG_ROOM_ID) ?: return finish()
        super.onCreate1(savedInstanceState)
        vm.stateLiveData.observeNonNull(this) { e ->
            e.peek {
                it.loadingState { s ->
                    if (s) {
                        progressDialogWrapper.showProgressDialog(this, R.string.updating)
                    } else {
                        progressDialogWrapper.cancel()
                    }
                }
            }
            e.handle {
                it.runOnValue {
                    setResult(RESULT_OK)
                    finish()
                }
                it.runOnError {
                    alertMsg(R.string.room_add_user_err_alert_msg)
                }
            }
        }
        roomInfoVM.loadRoom(roomId)
        roomInfoVM.roomData.observeNullable(this) {
            val r = it?.orElseNull()
            if (r == null) finish() else excludedUser.value = r.users.map(ChatUser::id)
        }
    }

    override val excludedUser: MutableLiveData<List<String>?> = MutableLiveData()

    private val progressDialogWrapper = ProgressDialogWrapper()

    override fun createMenuItem(menu: Menu): MenuItem? {
        return menu.add(Menu.NONE, R.string.add, 1, R.string.add)?.apply {
            setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
            setIcon(R.drawable.round_done_24)
        }
    }

    override fun onMenuItem(selectedUser: List<Member>?) {
        selectedUser?.let { vm.editRoom(roomId, it) }
    }
}
