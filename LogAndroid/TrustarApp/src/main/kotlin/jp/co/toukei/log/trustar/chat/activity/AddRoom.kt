package jp.co.toukei.log.trustar.chat.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.MutableLiveData
import jp.co.toukei.log.lib.handle
import jp.co.toukei.log.lib.lazyViewModel
import jp.co.toukei.log.lib.loadingState
import jp.co.toukei.log.lib.observeNonNull
import jp.co.toukei.log.lib.peek
import jp.co.toukei.log.lib.runOnError
import jp.co.toukei.log.lib.runOnValue
import jp.co.toukei.log.lib.util.ProgressDialogWrapper
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.alertMsg
import jp.co.toukei.log.trustar.chat.vm.AddRoomVM
import jp.co.toukei.log.trustar.rest.model.Member

class AddRoom : RoomUserSelectionActivity() {

    private val vm by lazyViewModel<AddRoomVM>()

    override fun onCreate1(savedInstanceState: Bundle?) {
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
                    alertMsg(R.string.room_create_err_alert_msg)
                }
            }
        }
        excludedUser.value = listOf(Current.userId)
    }

    override val excludedUser: MutableLiveData<List<String>?> = MutableLiveData()

    private val progressDialogWrapper = ProgressDialogWrapper()

    override fun createMenuItem(menu: Menu): MenuItem? {
        return menu.add(Menu.NONE, R.string.create, 1, R.string.create)?.apply {
            setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
            setIcon(R.drawable.round_done_24)
        }
    }

    override fun onMenuItem(selectedUser: List<Member>?) {
        selectedUser?.let(vm::addRoom)
    }
}
