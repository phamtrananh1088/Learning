package jp.co.toukei.log.lib.util

import android.app.ProgressDialog
import android.content.Context

class ProgressDialogWrapper {

    @Suppress("DEPRECATION")
    private var processingModalDialog: ProgressDialog? = null

    fun cancel() {
        processingModalDialog?.cancel()
    }

    fun showProgressDialog(ctx: Context, title: Int): ProgressDialog {
        return showProgressDialog(ctx, ctx.getString(title))
    }

    fun showProgressDialog(ctx: Context, title: CharSequence): ProgressDialog {
        return processingModalDialog ?: run {
            ProgressDialog.show(ctx, title, null, true, false) {
                processingModalDialog = null
            }.also { processingModalDialog = it }
        }
    }
}
