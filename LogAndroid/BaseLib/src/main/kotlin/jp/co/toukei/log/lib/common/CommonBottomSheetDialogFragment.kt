package jp.co.toukei.log.lib.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import jp.co.toukei.log.lib.R
import jp.co.toukei.log.lib.expand
import third.WeakDisposableContainer

abstract class CommonBottomSheetDialogFragment : AppCompatDialogFragment() {

    abstract fun createView(owner: FragmentActivity): View

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val owner = requireActivity()
        return createView(owner)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): AppCompatDialog {
        return BottomSheetDialog(requireContext(), theme).apply {
            setOnShowListener {
                val v = (it as? BottomSheetDialog)?.findViewById<View>(R.id.design_bottom_sheet)
                if (v != null) {
                    runCatching { BottomSheetBehavior.from(v) }.onSuccess(BottomSheetBehavior<View>::expand)
                }
            }
        }
    }

    protected val viewDisposableContainer: WeakDisposableContainer = WeakDisposableContainer()

    override fun onDestroyView() {
        super.onDestroyView()
        viewDisposableContainer.clear()
    }
}
