package jp.co.toukei.log.trustar.feature.home.fragment

import android.content.Context
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.postDelayed
import androidx.lifecycle.Observer
import androidx.lifecycle.toPublisher
import io.reactivex.rxjava3.core.Flowable
import jp.co.toukei.log.lib.addTo
import jp.co.toukei.log.lib.common.CommonFragment
import jp.co.toukei.log.lib.common.gravityCenterVertical
import jp.co.toukei.log.lib.common.setLayoutParams
import jp.co.toukei.log.lib.common.snackbar
import jp.co.toukei.log.lib.common.sp
import jp.co.toukei.log.lib.ext.ImageViewWithBadgeText
import jp.co.toukei.log.lib.fitCenterInto
import jp.co.toukei.log.lib.getViewModel
import jp.co.toukei.log.lib.observeOnComputation
import jp.co.toukei.log.lib.observeOnUI
import jp.co.toukei.log.lib.rippleDrawable
import jp.co.toukei.log.lib.singleViewBlock
import jp.co.toukei.log.lib.subscribeOrIgnore
import jp.co.toukei.log.lib.sugoiCreator
import jp.co.toukei.log.lib.util.Combine2LiveData
import jp.co.toukei.log.lib.util.SugoiAdapter
import jp.co.toukei.log.lib.withBoundValue
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.db.user.embedded.BinHeaderAndStatus
import jp.co.toukei.log.trustar.db.user.entity.BinStatus
import jp.co.toukei.log.trustar.db.user.entity.Notice
import jp.co.toukei.log.trustar.feature.account.fragment.NoticeListFragment
import jp.co.toukei.log.trustar.feature.home.activity.HomeActivity
import jp.co.toukei.log.trustar.feature.home.fragment.binheader.BinHeaderInfoDialog
import jp.co.toukei.log.trustar.feature.home.model.DashboardFragmentModel
import jp.co.toukei.log.trustar.feature.home.model.HomeModel
import jp.co.toukei.log.trustar.feature.home.model.NoticeFragmentModel
import jp.co.toukei.log.trustar.feature.home.ui.BinHeadUI
import jp.co.toukei.log.trustar.feature.home.ui.DashboardFragmentUI
import jp.co.toukei.log.trustar.hasFinished
import jp.co.toukei.log.trustar.openFragmentNoAnimate
import jp.co.toukei.log.trustar.setTextObserver
import jp.co.toukei.log.trustar.showAt
import jp.co.toukei.log.trustar.showFullScreen
import jp.co.toukei.log.trustar.withGlide
import splitties.dimensions.dip
import splitties.views.dsl.core.add
import splitties.views.dsl.core.horizontalLayout
import splitties.views.dsl.core.imageView
import splitties.views.dsl.core.lParams
import splitties.views.dsl.core.matchParent
import splitties.views.dsl.core.textView
import splitties.views.dsl.core.wrapContent
import splitties.views.padding

/**
 * ダッシュボード
 * - お知らせ [NoticeListFragment]
 * - 配送計画 [DetailFragment]
 * - 計画なし運行追加 ダイアログ [UnscheduledBinStart]
 */
class DashboardFragment : CommonFragment<HomeActivity>() {

    private inner class Ob(
        private val view: ImageViewWithBadgeText,
        private val rank: Int,
        private val badgeSize: Float,
    ) : Observer<List<Notice>> {
        /**
         * お知らせ・未読の通知（重要）が存在する場合の判定処理
         * @see NoticeFragmentModel.unreadNormal
         * @see NoticeFragmentModel.unreadImportant
         */
        override fun onChanged(it: List<Notice>?) {
            val size = it?.size ?: 0
            val badge = if (size > 0) size.toString() else null
            view.apply {
                setBadge(badge, 0.6F, badgeSize)
                setOnClickListener { _ ->
                    /**ダッシュボード・通常ボタン押下時のイベント, [rank] is 2*/
                    /**ダッシュボード・重要ボタン押下時のイベント, [rank] is 1*/
                    NoticeListFragment()
                        .also { it.vmArg.arg = rank }
                        .openFragmentNoAnimate(requireActivity(), R.id.main_root)
                }
            }
        }
    }

    override fun createView(owner: HomeActivity): View {
        val context: Context = owner
        val u = DashboardFragmentUI(context)

        u.toolbar.apply {
            menu.add(Menu.NONE, R.string.settings, 1, R.string.settings)
                .apply {
                    setIcon(R.drawable.round_settings_24)
                    setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)
                }
            setOnMenuItemClickListener {
                if (it.itemId == R.string.settings) {
                    SettingsFragment().showFullScreen(owner, R.id.main_root)
                }
                true
            }
        }
        val homeModel = owner.getViewModel<HomeModel>()
        val dashboardFragmentModel = getViewModel<DashboardFragmentModel>()
        val noticeModel = owner.getViewModel<NoticeFragmentModel>()

        homeModel.lastFormattedDataLiveData.observe(u.dateTextView.setTextObserver())

        val glide = context.withGlide()
        glide.load(R.drawable.round_notifications_24).fitCenterInto(u.bellImportant)
        glide.load(R.drawable.round_notifications_24).fitCenterInto(u.bellNormal)

        val badgeSize = context.sp(8).toFloat()
        noticeModel.unreadImportant().observe(
            viewLifecycleOwner, Ob(u.bellImportant, 1, badgeSize)
        )
        noticeModel.unreadNormal().observe(
            viewLifecycleOwner, Ob(u.bellNormal, 2, badgeSize)
        )

        Current.userChangeObservable.observeNullable {
            u.fullName.text = it?.userInfo?.userNm
        }

        val adapter = SugoiAdapter(2)
        u.list.adapter = adapter

        val binBlock = SugoiAdapter.DiffListBlock(BinHeadUI.Diff(), null)
        binBlock.attachToAdapter(adapter)

        run {
            val footer = singleViewBlock {
                horizontalLayout {
                    setLayoutParams(matchParent, wrapContent)
                    background = rippleDrawable(0)
                    gravityCenterVertical()
                    add(imageView {
                        padding = dip(12)
                        setImageResource(R.drawable.baseline_add_circle_24)
                        setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary))
                    }, lParams(dip(48), dip(48)))
                    add(
                        textView {
                            setText(R.string.operation_add_unscheduled)
                        },
                        lParams(wrapContent, wrapContent)
                    )

                    setOnClickListener {
                        /*ダッシュボード・予定なし運行追加押下イベント処理*/
                        val count = homeModel.countBinHeader(BinStatus.Type.Working)
                        if (count > 0) {
                            u.coordinatorLayout.snackbar(R.string.start_when_started_operation_exists_msg)
                        } else {
                            UnscheduledBinStart().showAt(owner)
                        }
                    }
                }
            }

            u.list.postDelayed(200) { footer.attachToAdapter(adapter) }
        }

        val creator = sugoiCreator {
            BinHeadUI(context).apply {
                view.setLayoutParams()
                view.setOnClickListener {
                    /*ダッシュボード・行を押下した時のイベント*/
                    withBoundValue {
                        homeModel.navigationBinHeader(header)
                    }
                }
                icon.setOnClickListener {
                    withBoundValue {
                        BinHeaderInfoDialog().also {
                            it.arg = header.allocationNo
                        }.showAt(owner)
                    }
                }
            }
        }

        Combine2LiveData(
            homeModel.dataSource.binHeaderList,
            dashboardFragmentModel.showAllCheckState
        ) { a, b -> a to b }
            .toPublisher(viewLifecycleOwner)
            .let { Flowable.fromPublisher(it) }
            .observeOnComputation()
            .map { (list, checked) ->
                DataList(
                    checked ?: true,
                    list,
                    list?.filterNot { it.status.hasFinished() }
                )
            }
            .observeOnUI()
            .subscribeOrIgnore {
                binBlock.submitList(
                    (if (it.displayAll) it.fullList else it.unfinishedList),
                    creator
                )
                u.operationAmount.text = context.getString(
                    R.string.today_operation_amount_d,
                    it.fullList?.size ?: 0
                )
                u.operationRemain.text = context.getString(
                    R.string.today_operation_remain_d,
                    it.unfinishedList?.size ?: 0
                )
            }
            .addTo(viewDisposableContainer)

        u.showAll.setOnCheckedChangeListener { _, isChecked ->
            /*ダッシュボード・全て表示押下時のイベント*/
            dashboardFragmentModel.showAllCheckState.value = isChecked
        }
        dashboardFragmentModel.showAllCheckState.observeNonNull {
            u.showAll.isChecked = it
        }
        return u.coordinatorLayout
    }

    private class DataList(
        @JvmField val displayAll: Boolean,
        @JvmField val fullList: List<BinHeaderAndStatus>?,
        @JvmField val unfinishedList: List<BinHeaderAndStatus>?,
    )
}
