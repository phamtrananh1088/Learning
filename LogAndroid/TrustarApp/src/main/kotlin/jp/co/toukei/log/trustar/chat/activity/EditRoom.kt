package jp.co.toukei.log.trustar.chat.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.ComposeView
import jp.co.toukei.log.common.compose.M3ContentTheme
import jp.co.toukei.log.trustar.compose.EditChatRoomUser
import jp.co.toukei.log.trustar.deprecated.intentFor
import jp.co.toukei.log.trustar.activity.RequireLoginActivity
import jp.co.toukei.log.trustar.user.LoggedUser

class EditRoom : RequireLoginActivity() {
    override fun onCreate1(savedInstanceState: Bundle?, user: LoggedUser) {
        val roomId = intent?.getStringExtra(ARG_ROOM_ID) ?: return finish()
        setContent {
            ComposeView(this).apply {
                M3ContentTheme {
                    EditChatRoomUser(
                        roomId = roomId,
                        user = user,
                        onDone = {
                            setResult(RESULT_OK)
                            finish()
                        }
                    ) {
                        finish()
                    }
                }
            }
        }
    }

    companion object {
        private const val ARG_ROOM_ID = "1"
        fun intentForStartActivity(context: Context, roomId: String): Intent {
            return context.intentFor<EditRoom>(
                ARG_ROOM_ID to roomId
            )
        }
    }
}
