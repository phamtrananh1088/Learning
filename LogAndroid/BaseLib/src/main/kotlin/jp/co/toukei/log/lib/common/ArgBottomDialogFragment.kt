package jp.co.toukei.log.lib.common

import android.content.Context
import androidx.lifecycle.LiveData

@Deprecated("use VmArg directly")
abstract class ArgBottomDialogFragment<A> : CommonBottomSheetDialogFragment() {

    private val vmArg = VmArg<A>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        vmArg.attach(this, "1")
    }

    val argLiveData: LiveData<A?> = vmArg.argLiveData

    var arg: A?
        set(value) {
            vmArg.arg = value
        }
        get() = vmArg.arg
}
