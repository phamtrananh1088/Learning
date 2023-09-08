package jp.co.toukei.log.trustar.chat.ui

import android.view.View
import android.widget.CompoundButton
import androidx.core.widget.doAfterTextChanged
import jp.co.toukei.log.lib.common.CommonFragment
import jp.co.toukei.log.lib.common.setLayoutParams
import jp.co.toukei.log.lib.isLoading
import jp.co.toukei.log.lib.lazyViewModel
import jp.co.toukei.log.lib.runOnError
import jp.co.toukei.log.lib.sugoiCreator
import jp.co.toukei.log.lib.util.SugoiAdapter
import jp.co.toukei.log.lib.withBoundValue
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.chat.activity.RoomUserSelectionActivity
import jp.co.toukei.log.trustar.chat.vm.RoomUserSelectionVM
import jp.co.toukei.log.trustar.chat.vm.UserSelectionVM
import jp.co.toukei.log.trustar.groupBlock1
import jp.co.toukei.log.trustar.rest.model.TalkUser
import jp.co.toukei.log.trustar.snackbarMessage

class RoomUserSelection : CommonFragment<RoomUserSelectionActivity>() {

    private val vm by lazyViewModel<RoomUserSelectionVM>()

    override fun createView(owner: RoomUserSelectionActivity): View {
        val u = UserSelectionUI(owner).apply {
            setupToolbarNavigation(toolbar)
            toolbar.setNavigationIcon(R.drawable.round_close_24)
            owner.setSupportActionBar(toolbar)
            search.doAfterTextChanged {
                vm.setQueryText(it)
            }

            val selectedAdapter = SugoiAdapter(1)
            val diff = UserCheckItemUI.Diff()
            val block = SugoiAdapter.DiffListBlock<UserSelectionVM.UserCheck<TalkUser>>(diff) {
                owner.setSelection(it.map { it.user })
            }
            block.attachToAdapter(selectedAdapter)
            selectedList.adapter = selectedAdapter

            val selectedCreator = sugoiCreator {
                UserCheckedUI(this).apply {
                    view.setOnClickListener {
                        withBoundValue {
                            vm.selectId(user.userId, false)
                        }
                    }
                }
            }

            val listAdapter = SugoiAdapter(4)

            val blocks = listOf(R.string.talk_history, R.string.logged_in_user).map {
                val b = SugoiAdapter.DiffListBlock(diff)
                groupBlock1(it, b).attachToAdapter(listAdapter)
                b.apply { attachToAdapter(listAdapter) }
            }

            list.adapter = listAdapter

            val creator = sugoiCreator {
                UserCheckItemUI(this).apply {
                    view.setLayoutParams()

                    val l = object : View.OnClickListener, CompoundButton.OnCheckedChangeListener {
                        override fun onClick(v: View?) {
                            checkBox.toggle()
                        }

                        override fun onCheckedChanged(
                            buttonView: CompoundButton?,
                            isChecked: Boolean,
                        ) {
                            withBoundValue {
                                vm.selectId(user.userId, isChecked)
                            }
                        }
                    }
                    checkBox.setOnCheckedChangeListener(l)
                    view.setOnClickListener(l)
                }
            }
            vm.selectedLiveData.observeNullable {
                block.submitList(it, selectedCreator)
            }
            swipeRefreshLayout.setOnRefreshListener(vm::loadUsers)
            vm.loadStateLiveData.observeNullable {
                swipeRefreshLayout.isRefreshing = it?.isLoading() ?: false
                it?.runOnError {
                    it.snackbarMessage(coordinatorLayout)
                }
            }
            vm.groupListLiveData.observeNullable {
                blocks.forEachIndexed { index, diffListBlock ->
                    diffListBlock.submitList(it?.getOrNull(index), creator)
                }
            }
        }
        vm.loadUsers()
        owner.setSelection(null)
        owner.excludedUser.observeNullable {
            vm.excludeIds(it.orEmpty())
        }
        return u.coordinatorLayout
    }
}
