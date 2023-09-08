package jp.co.toukei.log.lib.util

import android.app.Dialog
import android.content.DialogInterface

abstract class DialogWrapper<T> {

    private var dialog: Dialog? = null

    fun dismiss() {
        dialog?.dismiss()
        additionValue = null
        dialog = null
    }

    var additionValue: T? = null
        protected set

    private val onDismiss = DialogInterface.OnDismissListener {
        additionValue = null
        dialog = null
    }

    abstract fun createDialog(onDismiss: DialogInterface.OnDismissListener): Dialog

    fun showDialog(): Dialog {
        val d = dialog ?: createDialog(onDismiss)
        dialog = d
        d.show()
        return d
    }
}
