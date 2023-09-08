package jp.co.toukei.log.trustar.feature.home.fragment

import android.app.Activity
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import jp.co.toukei.log.lib.common.CommonFragment
import jp.co.toukei.log.lib.common.setLayoutParams
import jp.co.toukei.log.lib.common.snackbar
import jp.co.toukei.log.lib.getActivityModel
import jp.co.toukei.log.lib.isLoading
import jp.co.toukei.log.lib.lazyViewModel
import jp.co.toukei.log.lib.runOnError
import jp.co.toukei.log.lib.sugoiAdapterCreator
import jp.co.toukei.log.lib.toBindHolder
import jp.co.toukei.log.lib.util.Differ
import jp.co.toukei.log.lib.util.SugoiAdapter
import jp.co.toukei.log.lib.withBoundValue
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.chat.PushMessage
import jp.co.toukei.log.trustar.chat.activity.AddRoom
import jp.co.toukei.log.trustar.chat.activity.Chat
import jp.co.toukei.log.trustar.chat.activity.RoomUserSelectionActivity
import jp.co.toukei.log.trustar.chat.ui.RoomItemUI
import jp.co.toukei.log.trustar.chat.vm.RoomLoadVM
import jp.co.toukei.log.trustar.db.chat.entity.ChatRoomExt
import jp.co.toukei.log.trustar.deprecated.startActivityForResult
import jp.co.toukei.log.trustar.feature.home.activity.HomeActivity
import jp.co.toukei.log.trustar.feature.home.model.HomeModel
import jp.co.toukei.log.trustar.feature.home.ui.NavMessageUI
import jp.co.toukei.log.trustar.setTextObserver

class NavMessage : CommonFragment<HomeActivity>() {

    private val mainVM by lazy { getActivityModel<HomeModel>() }
    private val vm by lazyViewModel<RoomLoadVM>()

    private val listener = object : SwipeRefreshLayout.OnRefreshListener, Observer<Any?> {
        override fun onRefresh() {
            vm.load()
        }

        override fun onChanged(t: Any?) {
            if (t != null) onRefresh()
        }
    }

    override fun createView(owner: HomeActivity): View {
        val u = NavMessageUI(owner)
        mainVM.lastFormattedDataLiveData.observe(u.dateTextView.setTextObserver())
        u.apply {
            val adapter = SugoiAdapter(1)
            val differ = object : Differ.DefaultDiffCallback<ChatRoomExt>() {
                override fun areItemsTheSame(
                    oldItem: ChatRoomExt,
                    newItem: ChatRoomExt,
                    oldItemPosition: Int,
                    newItemPosition: Int,
                ): Boolean {
                    return oldItem.room.id == newItem.room.id
                }

                override fun areContentsTheSame(
                    oldItem: ChatRoomExt,
                    newItem: ChatRoomExt,
                    oldItemPosition: Int,
                    newItemPosition: Int,
                ): Boolean {
                    return false
                }

                override fun getChangePayload(
                    oldItem: ChatRoomExt,
                    newItem: ChatRoomExt,
                    oldItemPosition: Int,
                    newItemPosition: Int,
                ): Any = Unit
            }
            val blk = SugoiAdapter.DiffListBlock(differ)

            val creator = sugoiAdapterCreator {
                RoomItemUI(this).apply {
                    view.setLayoutParams()
                    view.setOnClickListener {
                        withBoundValue {
                            startActivityForResult<Chat>(2, Chat.ARG_ROOM_ID to room.id)
                        }
                    }
                }.toBindHolder()
            }
            blk.attachToAdapter(adapter)
            list.adapter = adapter
            mainVM.lastTimeMillisLiveData.observeNonNull {
                vm.updateDate(System.currentTimeMillis())
            }
            vm.listLiveData.observeNullable {
                blk.submitList(it, creator)
            }
            vm.loadStateLiveData.observeNonNull {
                swipeRefreshLayout.isRefreshing = it.isLoading()
                it.runOnError {
                    u.coordinatorLayout.snackbar(R.string.load_failed)
                }
            }
        }
        u.toolbar.apply {
            setTitle(R.string.talk)
            menu.add(Menu.NONE, R.string.create_talk_room, 1, R.string.create_talk_room)
                .apply {
                    setIcon(R.drawable.round_add_24)
                    setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
                }
            setOnMenuItemClickListener {
                if (it.itemId == R.string.create_talk_room) {
                    startActivityForResult<AddRoom>(
                        1,
                        RoomUserSelectionActivity.ARG_TITLE to getString(R.string.create_talk_room)
                    )
                }
                true
            }
        }

        listener.onRefresh()
        u.swipeRefreshLayout.setOnRefreshListener(listener)
        PushMessage.pushMessageLiveData.observe(viewLifecycleOwner, listener)

        return u.coordinatorLayout
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) listener.onRefresh()
    }

    override fun onResume() {
        super.onResume()
        listener.onRefresh()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            1 -> if (resultCode == Activity.RESULT_OK) vm.load()
            2 -> if (resultCode == Chat.RESP_RELOAD) vm.load()
        }
    }
}
