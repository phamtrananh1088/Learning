package jp.co.toukei.log.trustar.compose

import androidx.annotation.StringRes

sealed class VmEvent {
    class Msg(@StringRes val id: Int) : VmEvent()
}
