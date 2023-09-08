package jp.co.toukei.log.trustar.chat.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import jp.co.toukei.log.common.compose.M3ContentTheme
import jp.co.toukei.log.trustar.activity.RequireLoginActivity
import jp.co.toukei.log.trustar.chat.vm.ChatOptionVM
import jp.co.toukei.log.trustar.deprecated.intentFor
import jp.co.toukei.log.trustar.user.LoggedUser

class ChatOption : RequireLoginActivity() {
    override fun onCreate1(savedInstanceState: Bundle?, user: LoggedUser) {
        val roomId = intent?.getStringExtra(ARG_ROOM_ID) ?: return finish()

        setContent {
            val vm = viewModel {
                ChatOptionVM(roomId, user)
            }
            M3ContentTheme {
                jp.co.toukei.log.trustar.compose.ChatOption(
                    vm,
                    userAdded = {
                        setResult(RESP_RELOAD) //todo reload without finish
                        finish()
                    },
                    naviBack = {
                        finish()
                    }
                )
            }
        }
    }

    companion object {

        @JvmStatic
        val ARG_ROOM_ID = "ARG_ROOM_ID"

        @JvmStatic
        val RESP_RELOAD = 1


        fun intentForStartActivity(context: Context, roomId: String): Intent {
            return context.intentFor<ChatOption>(ARG_ROOM_ID to roomId)
        }
    }
}
