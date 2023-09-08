package jp.co.toukei.log.trustar.common

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatDialog
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.LiveData
import jp.co.toukei.log.lib.common.CommonBottomSheetDialogFragment
import jp.co.toukei.log.lib.common.VmArg
import jp.co.toukei.log.lib.common.backgroundColorResId
import jp.co.toukei.log.trustar.R
import splitties.views.backgroundColor
import splitties.views.dsl.coordinatorlayout.coordinatorLayout
import splitties.views.dsl.core.add
import splitties.views.dsl.core.frameLayout
import splitties.views.dsl.core.lParams
import splitties.views.dsl.core.matchParent

abstract class FullScreenDialogFragment<A> : CommonBottomSheetDialogFragment() {

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val owner = requireActivity()

        return inflater.context.frameLayout {
            fitsSystemWindows = true
            backgroundColorResId = R.color.colorPrimaryDark
            add(coordinatorLayout {
                fitsSystemWindows = true
                backgroundColor = Color.WHITE

                val view = createView(owner)

                this.addView(view, CoordinatorLayout.LayoutParams(matchParent, matchParent))
            }, lParams(matchParent, matchParent))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): AppCompatDialog {
        return AppCompatDialog(
            requireContext(),
            R.style.Theme_MaterialComponents_BottomSheetDialog
        ).apply {
            supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        }
    }

    override fun onStart() {
        dialog?.window?.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
        super.onStart()
    }
}
