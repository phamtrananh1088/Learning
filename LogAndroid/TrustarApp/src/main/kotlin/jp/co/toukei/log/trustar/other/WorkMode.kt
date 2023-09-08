package jp.co.toukei.log.trustar.other

import androidx.annotation.StringRes
import jp.co.toukei.log.trustar.R

sealed class WorkMode(
        @StringRes @JvmField val string: Int
) {
    object Normal : WorkMode(R.string.work_mode_manual)
    object Automatic : WorkMode(R.string.work_mode_automatic)
}
