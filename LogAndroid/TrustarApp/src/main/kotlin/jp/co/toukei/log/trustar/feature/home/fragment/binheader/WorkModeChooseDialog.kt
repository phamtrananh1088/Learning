package jp.co.toukei.log.trustar.feature.home.fragment.binheader

import android.view.View
import androidx.fragment.app.FragmentActivity
import jp.co.toukei.log.lib.common.showOrGone
import jp.co.toukei.log.lib.observeNullable
import jp.co.toukei.log.lib.orElseNull
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.db.user.entity.BinStatus
import jp.co.toukei.log.trustar.feature.dialog.WorkModeDialog
import jp.co.toukei.log.trustar.other.WorkMode

class WorkModeChooseDialog : BinHeaderDialogFragment() {

    override fun createView(owner: FragmentActivity): View {

        val d = WorkModeDialog(
            owner, arrayOf(
                WorkMode.Normal,
                WorkMode.Automatic
            )
        )
        d.start.setOnClickListener {
            /*配送計画一覧・運行開始データ更新処理*/
            val mode = d.getSelected()
            if (mode != null) {
                binHeaderModel.start(mode, Current.lastLocation)
            }
        }
        binHeaderModel.binHeaderLiveData.observeNullable(viewLifecycleOwner) {
            if (it?.orElseNull()?.status?.type != BinStatus.Type.Ready) dismiss()
        }
        binHeaderModel.binList.observeNullable(viewLifecycleOwner) {
            d.warning.showOrGone(it?.any { it.detail.placeLocation == null } ?: false)
        }
        return d.view
    }
}
