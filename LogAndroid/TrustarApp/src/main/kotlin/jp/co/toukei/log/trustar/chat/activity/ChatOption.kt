package jp.co.toukei.log.trustar.chat.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import androidx.annotation.MainThread
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.switchmaterial.SwitchMaterial
import jp.co.toukei.log.lib.applyView
import jp.co.toukei.log.lib.common.gravityCenterVertical
import jp.co.toukei.log.lib.common.setLayoutParams
import jp.co.toukei.log.lib.common.switchMaterial
import jp.co.toukei.log.lib.fitCenterInto
import jp.co.toukei.log.lib.getViewModel
import jp.co.toukei.log.lib.handle
import jp.co.toukei.log.lib.loadingState
import jp.co.toukei.log.lib.observeNonNull
import jp.co.toukei.log.lib.observeNullable
import jp.co.toukei.log.lib.orElseNull
import jp.co.toukei.log.lib.peek
import jp.co.toukei.log.lib.runOnError
import jp.co.toukei.log.lib.runOnValue
import jp.co.toukei.log.lib.singleViewBlock
import jp.co.toukei.log.lib.sugoiCreator
import jp.co.toukei.log.lib.util.ProgressDialogWrapper
import jp.co.toukei.log.lib.util.SugoiAdapter
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.alertMsg
import jp.co.toukei.log.trustar.chat.ui.ListItemHeaderUI
import jp.co.toukei.log.trustar.chat.ui.UserItemUI
import jp.co.toukei.log.trustar.chat.vm.ChatOptionVM
import jp.co.toukei.log.trustar.common.ToolbarActivity
import jp.co.toukei.log.trustar.common.ui.DefaultRecyclerViewUI
import jp.co.toukei.log.trustar.deprecated.startActivityForResult
import jp.co.toukei.log.trustar.listItemDrawable
import jp.co.toukei.log.trustar.withGlide
import splitties.dimensions.dip
import splitties.views.dsl.core.add
import splitties.views.dsl.core.horizontalLayout
import splitties.views.dsl.core.lParams
import splitties.views.dsl.core.textView
import splitties.views.dsl.core.wrapContent
import splitties.views.horizontalPadding
import splitties.views.padding
import splitties.views.textColorResource
import splitties.views.textResource

class ChatOption : ToolbarActivity() {

    override fun onCreate1(savedInstanceState: Bundle?) {
        val roomId = intent?.getStringExtra(ARG_ROOM_ID) ?: return finish()

        val vm = getViewModel<ChatOptionVM>()

        vm.loadRoom(roomId)

        val adapter = SugoiAdapter(6)

        val u = DefaultRecyclerViewUI(this).apply {
            setContentView(coordinatorLayout)
            setSupportActionBar(toolbar)
            sabCloseIconHomeAsUp()
            list.adapter = adapter
        }

        val titleCreator = sugoiCreator {
            ListItemHeaderUI<Any?>(this)
                .apply {
                    view.setLayoutParams()
                }
        }
        val creator = sugoiCreator {
            UserItemUI(this)
                .applyView {
                    setLayoutParams()
                }
        }

        val members = SugoiAdapter.ValueBlock(titleCreator)
        members.applyValue(ListItemHeaderUI.Header("", null, null))
            .attachToAdapter(adapter)

        singleViewBlock {
            UserItemUI(this)
                .apply {
                    nameTextView.apply {
                        textResource = R.string.add_user
                    }
                    avatarImageView.apply {
                        withGlide().load(R.drawable.ic_avatar)
                            .fitCenterInto(this)
                    }
                    view.apply {
                        setLayoutParams()
                        setOnClickListener {
                            startActivityForResult<EditRoom>(
                                1,
                                RoomUserSelectionActivity.ARG_ROOM_ID to roomId
                            )
                        }
                    }
                }
                .view
        }.attachToAdapter(adapter)

        val block = SugoiAdapter.DiffListBlock(UserItemUI.Diff())
        block.attachToAdapter(adapter)

        val switcher = object : View.OnClickListener, CompoundButton.OnCheckedChangeListener {

            private var byUser = false

            var switch: SwitchMaterial? = null

            @MainThread
            override fun onClick(v: View?) {
                byUser = true
                switch?.toggle()
            }

            @MainThread
            fun checkProgrammatically(isChecked: Boolean) {
                byUser = false
                switch?.isChecked = isChecked
                byUser = true
            }

            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if (byUser) {
                    vm.notificationOnOff(roomId, isChecked)
                }
            }
        }

        vm.roomData.observeNullable(this) {
            val r = it?.orElseNull()
            if (r == null) finish() else {
                val users = r.users
                val room = r.room
                val userCount = room.userCount
                val t = users.run {
                    resources.getQuantityString(R.plurals.members_of_room, userCount, userCount)
                }
                members.applyValue(ListItemHeaderUI.Header(t, null, null))
                block.submitList(users, creator)
                u.toolbar.title = room.name
                switcher.checkProgrammatically(r.room.notification)
            }
        }

        SugoiAdapter.ValueBlock(titleCreator)
            .applyValue(ListItemHeaderUI.Header(getString(R.string.talk_room_settings), null, null))
            .attachToAdapter(adapter)

        singleViewBlock {
            horizontalLayout {
                background = context.listItemDrawable()
                weightSum = 1F
                setLayoutParams()
                gravityCenterVertical()
                add(textView {
                    textResource = R.string.allow_notification
                    textColorResource = R.color.textColor
                    textSize = 18F
                    padding = dip(16)
                }, lParams(0, wrapContent, weight = 1F))
                val switch = add(switchMaterial {
                    horizontalPadding = dip(16)
                }, lParams(wrapContent, wrapContent))
                switcher.switch = switch
                switch.setOnCheckedChangeListener(switcher)
                setOnClickListener(switcher)
            }
        }.attachToAdapter(adapter)

        singleViewBlock {
            textView {
                background = context.listItemDrawable()
                setLayoutParams()
                padding = dip(16)
                textResource = R.string.quit_talk_room
                textColorResource = R.color.textColor
                textSize = 18F
                setOnClickListener {
                    vm.roomData.value?.orElseNull()?.room?.let { r ->
                        it.context.apply {
                            MaterialAlertDialogBuilder(it.context)
                                .setTitle(R.string.confirm)
                                .setMessage(getString(R.string.room_quit_s1_alert_msg, r.name))
                                .setPositiveButton(R.string.quit) { _, _ ->
                                    vm.leaveRoom(roomId)
                                }
                                .setNegativeButton(R.string.cancel, null)
                                .show()
                        }
                    }
                }
            }
        }.attachToAdapter(adapter)

        vm.leaveLiveData.observeNonNull(this) { e ->
            setResult(RESP_RELOAD)
            e.peek {
                it.loadingState { s ->
                    if (s) {
                        userLeaveProgressDialog.showProgressDialog(this, R.string.processing)
                    } else {
                        userLeaveProgressDialog.cancel()
                    }
                }
            }
            e.handle {
                it.runOnValue {
                    finish()
                }
                it.runOnError {
                    alertMsg(R.string.room_leave_err_alert_msg)
                }
            }
        }
        vm.notificationLiveData.observeNonNull(this) { e ->
            setResult(RESP_RELOAD)
            e.peek {
                it.loadingState { s ->
                    if (s) {
                        notificationProgressDialog.showProgressDialog(this, R.string.processing)
                    } else {
                        notificationProgressDialog.cancel()
                    }
                }
            }
            e.handle {
                it.runOnValue {
                    finish()
                }
                it.runOnError {
                    alertMsg(R.string.room_update_settings_err_alert_msg)
                }
            }
        }
    }

    private val notificationProgressDialog = ProgressDialogWrapper()
    private val userLeaveProgressDialog = ProgressDialogWrapper()


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            setResult(RESP_RELOAD)
            finish()
        }
    }

    companion object {

        @JvmStatic
        val ARG_ROOM_ID = "ARG_ROOM_ID"

        @JvmStatic
        val RESP_RELOAD = 1
    }
}
