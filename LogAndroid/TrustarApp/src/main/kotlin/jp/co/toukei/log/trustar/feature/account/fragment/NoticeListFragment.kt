package jp.co.toukei.log.trustar.feature.account.fragment

import android.content.Context
import android.view.View
import androidx.core.view.postDelayed
import androidx.fragment.app.FragmentActivity
import jp.co.toukei.log.lib.common.CommonFragment
import jp.co.toukei.log.lib.common.VmArg
import jp.co.toukei.log.lib.common.disable
import jp.co.toukei.log.lib.common.enable
import jp.co.toukei.log.lib.common.gravityCenterHorizontal
import jp.co.toukei.log.lib.common.materialAlertDialogBuilder
import jp.co.toukei.log.lib.common.setLayoutParams
import jp.co.toukei.log.lib.common.setMarginLayoutParams
import jp.co.toukei.log.lib.common.whiteText
import jp.co.toukei.log.lib.getViewModel
import jp.co.toukei.log.lib.runOnIo
import jp.co.toukei.log.lib.singleViewBlock
import jp.co.toukei.log.lib.sugoiCreator
import jp.co.toukei.log.lib.textPlaceholderBlock
import jp.co.toukei.log.lib.util.BindHolder
import jp.co.toukei.log.lib.util.SugoiAdapter
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.feature.account.ui.NoticeItemUI
import jp.co.toukei.log.trustar.feature.account.ui.NoticeListFragmentUI
import jp.co.toukei.log.trustar.feature.home.model.NoticeFragmentModel
import jp.co.toukei.log.trustar.feature.login.activity.LoginActivity
import jp.co.toukei.log.trustar.whiteButtonStyle
import splitties.dimensions.dip
import splitties.views.dsl.core.add
import splitties.views.dsl.core.horizontalLayout
import splitties.views.dsl.core.lParams
import splitties.views.dsl.core.margin
import splitties.views.dsl.core.textView
import splitties.views.verticalPadding

/**
 * お知らせ
 */
class NoticeListFragment : CommonFragment<FragmentActivity>() {

    @JvmField
    val vmArg = VmArg<Int>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        vmArg.attach(this, "1")
    }

    override fun createView(owner: FragmentActivity): View {
        val noticeModel = owner.getViewModel<NoticeFragmentModel>()
        val ui = NoticeListFragmentUI(owner)

        val rank = vmArg.arg ?: 2

        val noticeCreator = sugoiCreator {
            NoticeItemUI(this).apply {
                view.setMarginLayoutParams { margin = dip(4) }
            }
        }

        val placeHolder = textPlaceholderBlock(R.string.empty_placeholder, true) {
            whiteText()
            textSize = 20F
        }
        val block = object : SugoiAdapter.DiffListBlock<NoticeItemUI.Item>(NoticeItemUI.Diff(), {
            placeHolder.offline = it.isNotEmpty()
        }) {

            private val sendDataClick = View.OnClickListener {
                val tag = it.tag
                val ctx = it.context
                ctx.materialAlertDialogBuilder {
                    setTitle(R.string.notice_send_data_alert_title)
                    setMessage(R.string.notice_send_data_alert_msg)
                    setNegativeButton(R.string.cancel, null)
                    setPositiveButton(R.string.yes) { _, _ ->
                        runOnIo {
                            Current.user?.commitCommonDb()
                            when (tag) {
                                "logout" -> Current.logout(ctx, true)
                                "restart" -> Current.restartLaunch(ctx)
                            }
                        }
                    }
                }.show()
            }

            override fun onBind(
                holder: BindHolder<NoticeItemUI.Item>,
                value: NoticeItemUI.Item,
                position: Int,
                payloads: List<Any>,
            ) {
                super.onBind(holder, value, position, payloads)
                holder.itemView.apply {
                    if (value.notice.showDialogFlag) {
                        tag = if (value.notice.dataDelFlag) "logout" else "restart"
                        setOnClickListener(sendDataClick)
                        enable()
                    } else {
                        tag = null
                        setOnClickListener(null)
                        disable()
                    }
                }
            }
        }

        val sugoi = SugoiAdapter(2)

        val rv = ui.recyclerView
        rv.adapter = sugoi
        placeHolder.attachToAdapter(sugoi)
        block.attachToAdapter(sugoi)

        val foot = singleViewBlock {
            horizontalLayout {
                verticalPadding = dip(56)
                gravityCenterHorizontal()
                setLayoutParams()
                add(textView {
                    setText(R.string.notice_mark_read)
                    whiteButtonStyle()
                    setOnClickListener {
                        /* お知らせ・お知らせを確認した押下時のイベント */
                        if (owner is LoginActivity) owner.weatherSetup() else owner.onBackPressed()
                        noticeModel.markRead(rank)
                    }
                }, lParams())
            }
        }

        noticeModel.selectByRank(rank, false).observeNonNull {
            block.submitList(it, noticeCreator)

            rv.postDelayed(100) {
                foot.attachToAdapter(sugoi)
            }
        }
        return ui.coordinatorLayout
    }
}
