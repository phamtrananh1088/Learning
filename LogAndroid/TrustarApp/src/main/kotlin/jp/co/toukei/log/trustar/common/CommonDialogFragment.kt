package jp.co.toukei.log.trustar.common

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import jp.co.toukei.log.lib.common.materialAlertDialogBuilder

abstract class CommonDialogFragment : DialogFragment() {
    private var dialogView: View? = null
    override fun getView(): View? {
        return dialogView
    }

    open fun createDialogBuilder(
        context: Context,
        savedInstanceState: Bundle?,
    ): MaterialAlertDialogBuilder {
        dialogView = onCreateView(layoutInflater, null, savedInstanceState)
        return context.materialAlertDialogBuilder {
            setView(dialogView)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        return createDialogBuilder(requireContext(), savedInstanceState).create()
    }
}
