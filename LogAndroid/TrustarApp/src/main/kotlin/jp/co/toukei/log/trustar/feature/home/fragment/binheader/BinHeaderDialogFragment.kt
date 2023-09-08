package jp.co.toukei.log.trustar.feature.home.fragment.binheader

import android.os.Bundle
import android.view.View
import jp.co.toukei.log.lib.common.ArgBottomDialogFragment
import jp.co.toukei.log.lib.lazyViewModel
import jp.co.toukei.log.lib.observeNullable
import jp.co.toukei.log.trustar.feature.home.model.BinHeaderModel

abstract class BinHeaderDialogFragment : ArgBottomDialogFragment<String>() {

    protected val binHeaderModel by lazyViewModel<BinHeaderModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        argLiveData.observeNullable(viewLifecycleOwner) {
            binHeaderModel.setAllocationNo(it)
        }
    }
}
