package jp.co.toukei.log.trustar.feature.nimachi.fragment

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.switchMap
import androidx.recyclerview.widget.GridLayoutManager
import jp.co.toukei.log.lib.*
import jp.co.toukei.log.lib.common.PassDataVM
import jp.co.toukei.log.lib.common.show
import jp.co.toukei.log.lib.util.BindHolder
import jp.co.toukei.log.lib.util.BindHolderListAdapter
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.common.FullScreenDialogFragment
import jp.co.toukei.log.trustar.db.user.entity.IncidentalWork
import jp.co.toukei.log.trustar.feature.nimachi.data.WorkItem
import jp.co.toukei.log.trustar.feature.nimachi.ui.RecyclerViewButton
import jp.co.toukei.log.trustar.feature.nimachi.ui.WorkItemUI
import jp.co.toukei.log.trustar.feature.nimachi.vm.WorkSelectVM
import splitties.dimensions.dip
import splitties.views.padding
import splitties.views.textResource

class SheetWorkSelection :
    FullScreenDialogFragment<PassDataVM<List<IncidentalWork?>, List<IncidentalWork>>>() {

    override fun createView(owner: FragmentActivity): View {
        val u = RecyclerViewButton(owner)
        u.title.apply {
            show()
            padding = dip(16)
            textSize = 18F
            textResource = R.string.incidental_sheet_works_selection
        }
        u.button.textResource = R.string.confirm_content

        val diff = WorkItemUI.Diff()

        val model = getViewModel<WorkSelectVM>()

        val a = object : BindHolderListAdapter<WorkItem>(computationDifferConfig(diff)) {

            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): BindHolder<WorkItem> {
                return WorkItemUI(parent.context).apply {
                    tv.setOnClickListener {
                        /*附帯作業内容設定・複数ある作業ボタンを押下時に起動されるイベント*/
                        withBoundValue {
                            toggle()
                            onBind(this)
                            model.report(this)
                        }
                    }
                }.toBindHolder()
            }
        }
        model.listLiveData.observeNullable(viewLifecycleOwner) {
            a.submitList(it)
        }
        argLiveData.switchMapNullable { it?.source }.observeNonNull(viewLifecycleOwner) {
            /*附帯作業内容設定・データの更新処理*/
            model.selectOnly(it)
        }
        u.button.setOnClickListener {
            /*附帯作業内容設定・内容を確定する押下時に起動されるイベント*/
            arg?.result?.apply {
                value = model.getSelected()?.asEvent()
                dismiss()
            }
        }

        u.list.apply {
            val sp = context.spanCountBy(dip(172))
            layoutManager = GridLayoutManager(context, sp)
            adapter = a
        }
        return u.view
    }
}
