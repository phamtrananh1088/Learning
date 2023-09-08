package jp.co.toukei.log.trustar.chat.activity

import android.app.Activity
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.net.toUri
import androidx.core.text.buildSpannedString
import androidx.core.view.ViewCompat
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.processors.BehaviorProcessor
import jp.co.toukei.log.common.ui.MaterialChattingList
import jp.co.toukei.log.lib.addTo
import jp.co.toukei.log.lib.common.gone
import jp.co.toukei.log.lib.common.nestedScrollView
import jp.co.toukei.log.lib.common.setLayoutParams
import jp.co.toukei.log.lib.common.setMarginLayoutParams
import jp.co.toukei.log.lib.common.showOrGone
import jp.co.toukei.log.lib.common.toast
import jp.co.toukei.log.lib.gradientDrawable
import jp.co.toukei.log.lib.handle
import jp.co.toukei.log.lib.lazyViewModel
import jp.co.toukei.log.lib.observeNonNull
import jp.co.toukei.log.lib.observeNullable
import jp.co.toukei.log.lib.observeOnComputation
import jp.co.toukei.log.lib.observeOnUI
import jp.co.toukei.log.lib.orElseNull
import jp.co.toukei.log.lib.subscribeOrIgnore
import jp.co.toukei.log.lib.toBindHolder
import jp.co.toukei.log.lib.util.BindHolder
import jp.co.toukei.log.lib.util.ProgressDialogWrapper
import jp.co.toukei.log.lib.util.ValueBind
import jp.co.toukei.log.lib.withBoundValue
import jp.co.toukei.log.lib.withValue
import jp.co.toukei.log.trustar.Config
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.alertMsg
import jp.co.toukei.log.trustar.chat.MessageItem
import jp.co.toukei.log.trustar.chat.PushMessage
import jp.co.toukei.log.trustar.chat.ui.ChatUI
import jp.co.toukei.log.trustar.chat.ui.Dl
import jp.co.toukei.log.trustar.chat.ui.LoadHeaderUI
import jp.co.toukei.log.trustar.chat.ui.MessageAudioUI
import jp.co.toukei.log.trustar.chat.ui.MessageFileUI
import jp.co.toukei.log.trustar.chat.ui.MessageImageUI
import jp.co.toukei.log.trustar.chat.ui.MessageTextUI
import jp.co.toukei.log.trustar.chat.ui.MessageVideoUI
import jp.co.toukei.log.trustar.chat.vm.ChatVM
import jp.co.toukei.log.trustar.chat.vm.RoomLoadVM
import jp.co.toukei.log.trustar.common.ToolbarActivity
import jp.co.toukei.log.trustar.db.chat.entity.ChatMessagePending
import jp.co.toukei.log.trustar.deprecated.startActivityForResult
import jp.co.toukei.log.trustar.service.DownloadService
import jp.co.toukei.log.trustar.showDefaultImageViewer
import jp.co.toukei.log.trustar.snackbarMessage
import jp.co.toukei.log.trustar.startOpenFile
import jp.co.toukei.log.trustar.startShareFile
import kotlinx.coroutines.launch
import splitties.dimensions.dip
import splitties.systemservices.clipboardManager
import splitties.systemservices.notificationManager
import splitties.views.dsl.core.add
import splitties.views.dsl.core.horizontalMargin
import splitties.views.dsl.core.lParams
import splitties.views.dsl.core.matchParent
import splitties.views.dsl.core.textView
import splitties.views.dsl.core.verticalLayout
import splitties.views.dsl.core.verticalMargin
import splitties.views.dsl.core.wrapContent
import splitties.views.padding
import java.io.File
import java.util.Optional
import java.util.concurrent.TimeUnit

class Chat : ToolbarActivity() {

    private val vm by lazyViewModel<ChatVM>()
    private val roomLoadVM by lazyViewModel<RoomLoadVM>()

    private fun setStatusClickListener(textView: TextView, binder: ValueBind<MessageItem>) {
        textView.setOnClickListener {
            binder.withBoundValue {
                val ctx = textView.context
                if (this is MessageItem.Sent && readers.isNotEmpty()) {
                    val bg = ctx.getColor(R.color.grayBackground)
                    SimpleTooltip.Builder(ctx)
                        .anchorView(it)
                            //fixme compiler bug?
//                        .contentView(ctx.view(::ScrollViewWithMaxHeight) {
                        .contentView(ctx.nestedScrollView {
                            background = gradientDrawable(dip(8), bg)
//                            setMaxHeight(dip(480))
                            add(verticalLayout {
                                readers.forEach {
                                    add(textView {
                                        text = it.name
                                        padding = dip(8)
                                        setTextColor(context.getColor(R.color.textColor))
                                    }, lParams(wrapContent, wrapContent))
                                }
                            }, lParams(matchParent, wrapContent))
                        }, 0)
                        .dismissOnOutsideTouch(true)
                        .dismissOnInsideTouch(false)
                        .arrowColor(bg)
                        .showArrow(true)
                        .gravity(Gravity.START)
                        .build()
                        .show()
                }
            }
        }
    }

    private fun setLongClickMenu(view: View, binder: ValueBind<MessageItem>) {
        view.setOnCreateContextMenuListener { menu, _, _ ->
            val m = binder.boundValue
            currentMsgItemForContextMenu = m
            if (m is MessageItem.Text) {
                menu.add(0, R.string.msg_options_copy, 0, R.string.msg_options_copy)
            }
            if (
                (m is MessageItem.Sent && m.self) ||
                (m is MessageItem.Pending && m.pending.status.let {
                    it == ChatMessagePending.STATE_ERR || it == ChatMessagePending.STATE_NORMAL
                })
            ) {
                menu.add(0, R.string.delete, 1, R.string.delete)
            }
            menu.add(0, R.string.msg_options_details, 2, R.string.msg_options_details)
            if (m is MessageItem.Dl && m.attachmentExt()?.file?.canRead() == true) {
                menu.add(0, R.string.share, 3, R.string.share)
            }
        }
    }

    private var currentMsgItemForContextMenu: MessageItem? = null
    override fun onContextItemSelected(item: MenuItem): Boolean {
        val msg = currentMsgItemForContextMenu
        if (msg != null) {
            when (item.itemId) {
                R.string.msg_options_copy -> {
                    if (msg is MessageItem.Text) {
                        clipboardManager.setPrimaryClip(ClipData.newPlainText("", msg.text))
                        toast(R.string.msg_options_copied)
                    }
                }

                R.string.delete ->
                    alertMsg(R.string.alert_delete_message_msg) {
                        setPositiveButton(R.string.delete) { _, _ ->
                            vm.deleteMessage(msg)
                        }
                        setNegativeButton(R.string.cancel, null)
                    }

                R.string.msg_options_details -> {
                    MaterialAlertDialogBuilder(this)
                        .setTitle(R.string.msg_details)
                        .setMessage(
                            buildSpannedString {
                                append(
                                    getString(
                                        R.string.msg_options_sent_time,
                                        Config.dateFormatterForChat.format(msg.sentDate)
                                    )
                                )
                                //todo more details.
                            }
                        )
                        .show()
                }

                R.string.share -> {
                    val m = msg as? MessageItem.Dl
                    m?.shareFile(this)
                }
            }
        }
        return super.onContextItemSelected(item)
    }

    private fun textMessageHolder(self: Boolean): BindHolder<MessageItem> {
        return MessageTextUI(this, self)
            .apply {
                view.setMarginLayoutParams {
                    horizontalMargin = dip(16)
                    verticalMargin = dip(4)
                }
                if (self) {
                    setStatusClickListener(statusTextView, this)
                } else {
                    statusTextView.gone()
                }

                textTextView.setOnClickListener {
                    withBoundValue {
                        if (this is MessageItem.Pending) vm.resetErrStatus(pending)
                    }
                }
                setLongClickMenu(textTextView, this)
            }
            .toBindHolder()
    }

    private fun imgMessageHolder(self: Boolean): BindHolder<MessageItem> {
        return MessageImageUI(this, self)
            .apply {
                view.setMarginLayoutParams {
                    horizontalMargin = dip(16)
                    verticalMargin = dip(4)
                }
                if (self) {
                    setStatusClickListener(statusTextView, this)
                } else {
                    statusTextView.gone()
                }
                imageView.setOnClickListener {
                    withBoundValue {
                        if (this is MessageItem.Image) {
                            if (this is MessageItem.Pending) {
                                if (isSending) imageUri?.let(imageView::showDefaultImageViewer)
                                else vm.resetErrStatus(pending)
                            } else {
                                imageUri?.let(imageView::showDefaultImageViewer)
                            }
                        }
                    }
                }
                setLongClickMenu(imageView, this)
            }
            .toBindHolder()
    }

    private fun videoMessageHolder(self: Boolean): BindHolder<MessageItem> {
        return MessageVideoUI(this, self)
            .apply {
                view.setMarginLayoutParams {
                    horizontalMargin = dip(16)
                    verticalMargin = dip(4)
                }
                if (self) {
                    setStatusClickListener(statusTextView, this)
                } else {
                    statusTextView.gone()
                }
                imageView.setOnClickListener { v ->
                    withBoundValue {
                        if (this is MessageItem.Sent.VideoSent) {
                            playVideo(v.context, true)
                        } else if (this is MessageItem.Pending.VideoPending) {
                            if (isSending) playVideo(v.context, false)
                            else vm.resetErrStatus(pending)
                        }
                    }
                }
                setLongClickMenu(imageView, this)
            }
            .toBindHolder()
    }

    private fun audioMessageHolder(self: Boolean): BindHolder<MessageItem> {
        return MessageAudioUI(this, self)
            .apply {
                view.setMarginLayoutParams {
                    horizontalMargin = dip(16)
                    verticalMargin = dip(4)
                }
                if (self) {
                    setStatusClickListener(statusTextView, this)
                } else {
                    statusTextView.gone()
                }
                content.setOnClickListener {
                    withBoundValue {
                        if (this is MessageItem.Audio) {
                            if (this is MessageItem.Pending) vm.resetErrStatus(pending)
                            else {
                                playAudio(true)
                            }
                        }
                    }
                }
                setLongClickMenu(content, this)
            }
            .toBindHolder()
    }

    private fun fileMessageHolder(self: Boolean): BindHolder<MessageItem> {
        return MessageFileUI(this, self)
            .apply {
                view.setMarginLayoutParams {
                    horizontalMargin = dip(16)
                    verticalMargin = dip(4)
                }
                if (self) {
                    setStatusClickListener(statusTextView, this)
                } else {
                    statusTextView.gone()
                }
                content.setOnClickListener { v ->
                    withBoundValue {
                        if (this is MessageItem.Sent.FileSent) {
                            openFile(v.context, true)
                        } else if (this is MessageItem.Pending.FilePending) {
                            if (isSending) openFile(v.context, false)
                            else vm.resetErrStatus(pending)
                        }
                    }
                }
                setLongClickMenu(content, this)
            }
            .toBindHolder()
    }

    private fun download(fileKey: String, fileName: String) {
        DownloadService.startDownload(
            this,
            fileKey,
            null,
            fileName,
            DownloadService.NOTIFICATION_CLEAR
        )
        psc.tryBind()
    }

    private fun MessageItem.Dl.playVideo(ctx: Context, download: Boolean) {
        val att = attachmentExt() ?: return
        if (att.file.exists()) {
            ExoMediaPlayer.play(ctx, att.file.toUri())
        } else if (download) {
            download(att.key, att.name)
        }
    }

    private fun MessageItem.Dl.playAudio(download: Boolean) {
        val att = attachmentExt() ?: return
        if (att.file.exists()) {
            vm.player.playAudio(Single.just(att.file))
        } else if (download) {
            download(att.key, att.name)
        }
    }

    private fun MessageItem.Dl.openFile(ctx: Context, download: Boolean) {
        val att = attachmentExt() ?: return
        if (att.file.exists()) {
            ctx.startOpenFile(att.contentUri(), null)
        } else if (download) {
            download(att.key, att.name)
        }
    }

    private fun MessageItem.Dl.shareFile(ctx: Context) {
        val att = attachmentExt() ?: return
        if (att.file.exists()) {
            ctx.startShareFile(att.contentUri())
        }
    }

    private val differConfig = object : DiffUtil.ItemCallback<MessageItem>() {

        override fun areItemsTheSame(oldItem: MessageItem, newItem: MessageItem): Boolean {
            return when {
                oldItem is MessageItem.Pending && newItem is MessageItem.Pending -> oldItem.pending.id == newItem.pending.id
                else -> oldItem.listId == newItem.listId
            }
        }

        override fun areContentsTheSame(oldItem: MessageItem, newItem: MessageItem): Boolean {
            return oldItem.sameContent(newItem)
        }

        override fun getChangePayload(oldItem: MessageItem, newItem: MessageItem): Any? {
            return oldItem.getPayload(newItem)
        }
    }

    private lateinit var roomId: String

    private val scrollLastLiveData = MutableLiveData(false)

    private val progressDialogWrapper = ProgressDialogWrapper()

    private val progressLiveData = MediatorLiveData<List<DownloadService.Expose>>()

    private val psc = DownloadService.ProgressServiceConnection(progressLiveData, this, true)

    override fun onStart() {
        super.onStart()
        psc.tryBind()
    }

    override fun onStop() {
        super.onStop()
        psc.tryUnbind()
    }

    override fun onCreate1(savedInstanceState: Bundle?) {
        roomId = intent?.getStringExtra(ARG_ROOM_ID) ?: return finish()

        vm.loadRoom(roomId)

        val u = ChatUI(this).apply {
            setContentView(coordinatorLayout)
            setSupportActionBar(toolbar)
            sabCloseIconHomeAsUp()
            val textChange = object : TextWatcher, Runnable {
                override fun afterTextChanged(s: Editable?) {
                    val empty = s.isNullOrEmpty()
                    mic.showOrGone(empty)
                    addPic.showOrGone(empty)
                    takePhoto.showOrGone(empty)
                    upload.showOrGone(empty)

                    if (empty) {
                        if (send.isLaidOut) run() else send.post(this)
                    } else {
                        send.show(null)
                    }
                }

                override fun run() {
                    send.gone()
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int,
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            }
            editText.addTextChangedListener(textChange)
            textChange.afterTextChanged(editText.text)
            ViewCompat.setOnReceiveContentListener(
                editText,
                arrayOf("image/png", "image/jpeg")
            ) { v, payload ->
                scrollLastLiveData.value = false
                val partition = payload.partition { it.uri != null }
                partition.first?.clip
                    ?.run { List(itemCount) { getItemAt(it)?.uri ?: Uri.EMPTY } }
                    ?.takeUnless { it.isEmpty() }
                    ?.let { uris ->
                        vm.sendImage(roomId, uris)
                        v.clearFocus()
                    }
                partition.second
            }
            mic.setOnClickListener {
                startActivityForResult<Recorder>(
                    4,
                    Recorder.TARGET_FILE to vm.pending.getPending(2).absolutePath.toString()
                )
            }
            addPic.setOnClickListener {
                startActivityForResult<Gallery>(2)
            }
            takePhoto.setOnClickListener {
                startActivityForResult<Camera>(
                    1,
                    Camera.ARG_TMP_FILE_PATH to vm.pending.getPending(1).absolutePath.toString(),
                    Camera.ARG_VIDEO_ENABLED to true
                )
            }
            upload.setOnClickListener {
                runCatching {
                    startActivityForResult(
                        Intent(Intent.ACTION_OPEN_DOCUMENT)
                            .addCategory(Intent.CATEGORY_OPENABLE)
                            .setType("*/*"),
                        5
                    )
                }
            }
            send.setOnClickListener {
                val t = editText.text?.toString()?.trim()
                if (!t.isNullOrBlank()) {
                    scrollLastLiveData.postValue(false)
                    vm.insertMessage(roomId, t)
                }
                editText.text = null
            }
        }
        val lm = LinearLayoutManager(this).apply {
            stackFromEnd = true
        }

        val adapter0 =
            object : PagingDataAdapter<MessageItem, BindHolder<MessageItem>>(differConfig) {
                override fun onBindViewHolder(holder: BindHolder<MessageItem>, position: Int) {}

                override fun onBindViewHolder(
                    holder: BindHolder<MessageItem>,
                    position: Int,
                    payloads: MutableList<Any>,
                ) {
                    val item = getItem(position)
                    if (item != null) holder.valueBind.bind(item, position, payloads)
                }

                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int,
                ): BindHolder<MessageItem> {
                    return when (viewType) {
                        1 -> textMessageHolder(false)
                        2 -> textMessageHolder(true)
                        3 -> imgMessageHolder(false)
                        4 -> imgMessageHolder(true)
                        5 -> audioMessageHolder(false)
                        6 -> audioMessageHolder(true)
                        7 -> videoMessageHolder(false)
                        8 -> videoMessageHolder(true)
                        9 -> fileMessageHolder(false)
                        10 -> fileMessageHolder(true)
                        else -> throw IllegalStateException()
                    }
                }

                override fun getItemViewType(position: Int): Int {
                    val messageItem = getItem(position) ?: throw IllegalStateException()
                    val self = messageItem.self
                    return when (messageItem) {
                        is MessageItem.Pending.TextPending, is MessageItem.Sent.TextSent -> {
                            if (self) 2 else 1
                        }

                        is MessageItem.Pending.ImgPending, is MessageItem.Sent.ImgSent -> {
                            if (self) 4 else 3
                        }

                        is MessageItem.Pending.AudioPending, is MessageItem.Sent.AudioSent -> {
                            if (self) 6 else 5
                        }

                        is MessageItem.Pending.VideoPending, is MessageItem.Sent.VideoSent -> {
                            if (self) 8 else 7
                        }

                        is MessageItem.Pending.FilePending, is MessageItem.Sent.FileSent -> {
                            if (self) 10 else 9
                        }
                    }
                }

                private val attachedDl = mutableSetOf<Dl>()

                override fun onViewDetachedFromWindow(holder: BindHolder<MessageItem>) {
                    super.onViewDetachedFromWindow(holder)
                    val b = holder.valueBind
                    if (b is Dl) {
                        attachedDl.remove(b)
                    }
                }

                override fun onViewAttachedToWindow(holder: BindHolder<MessageItem>) {
                    super.onViewAttachedToWindow(holder)
                    val b = holder.valueBind as? Dl
                    val att = b?.boundAttachmentExt()
                    if (att != null && !att.file.exists()) {
                        attachedDl.add(b)
                        val e = updateProgress?.firstOrNull { it.fileKey == att.key }
                        b.setDownloadProgress(e?.progress)
                    }
                }

                var updateProgress: List<DownloadService.Expose>? = null
                    set(value) {
                        field = value
                        value?.let { v ->
                            attachedDl.forEach { dl ->
                                dl.boundAttachmentExt()?.key?.let { k ->
                                    val e = v.firstOrNull { it.fileKey == k }
                                    dl.setDownloadProgress(e?.progress)
                                }
                            }
                        }
                    }
            }

        progressLiveData.observeNonNull(this) {
            adapter0.updateProgress = it
        }

        val adapterUpdated = BehaviorProcessor.create<Unit>()

        adapterUpdated
            .observeOnComputation()
            .throttleWithTimeout(1, TimeUnit.SECONDS)
            .map { adapter0.snapshot() }
            .map {
                Optional.ofNullable(it.asReversed().firstNotNullOfOrNull { m ->
                    if (m is MessageItem.Sent && !m.self) m.msg else null
                })
            }
            .distinctUntilChanged { t1, t2 ->
                val v1 = t1.orElseNull()
                val v2 = t2.orElseNull()
                v1 != null && v2 != null && v2.messageRow <= v1.messageRow
            }
            .observeOnUI()
            .subscribeOrIgnore { o ->
                o.withValue {
                    vm.markRead(it.messageRow)
                }
            }
            .addTo(d)

        val ad = adapter0.withLoadStateHeader(Header {
            adapter0.retry()
        })

        adapter0.addLoadStateListener {
            adapterUpdated.onNext(Unit)
        }
        vm.roomData.observeNullable(this) {
            adapter0.refresh()

            val r = it?.orElseNull()?.room
            if (r == null) {
                setResult(RESP_RELOAD)
                finish()
            } else u.toolbar.title = r.name
        }


        vm.chatLiveData.observeNonNull(this) {
            lifecycleScope.launch {
                adapter0.submitData(it)
            }
        }
        vm.errLiveData.observeNonNull(this) { e ->
            e.handle {
                it.snackbarMessage(u.coordinatorLayout)
            }
        }

        u.list.setContent {
            MaterialChattingList(Current.userId, page = vm.chatLiveData.asFlow())
        }

        vm.deletingMessageLiveData.observeNonNull(this) {
            if (it) {
                progressDialogWrapper.showProgressDialog(this, R.string.processing)
            } else {
                progressDialogWrapper.cancel()
            }
        }
        vm.player.progress.observeNullable(this) {
            if (it == null) {
                progressDialog.dismiss()
            } else {
                progressDialog.showDialog()
                progressDialog.additionValue?.apply {
                    setProgress(it, vm.player.isPlaying())
                }
            }
        }

        vm.resetAllErrStatus(roomId)

        PushMessage.observeMessage(roomId).observeNonNull(this) {
            roomLoadVM.load()
            cancelNotification()
        }

    }

    private val progressDialog by lazy { vm.player.dialogWrapper(this) }

    private val d = CompositeDisposable()

    private fun cancelNotification() {
        val id = PushMessage.notificationId(roomId)
        notificationManager.cancel(id)
        PushMessage.clearNotification(roomId)
    }

    override fun onDestroy() {
        super.onDestroy()
        d.dispose()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add(Menu.NONE, R.string.talk_option, 1, R.string.talk_option)?.apply {
            setIcon(R.drawable.round_more_vert_24)
            setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.string.talk_option) {
            startActivityForResult<ChatOption>(3, ChatOption.ARG_ROOM_ID to roomId)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> if (resultCode == Activity.RESULT_OK) {
                vm.pending.commit(1)?.let { f ->
                    Camera.resolveResultIntent(data)?.let {
                        val u = f.toUri()
                        if (it.isVideo) {
                            vm.sendVideo(roomId, u, it.fileExt)
                        } else {
                            vm.sendImage(roomId, listOf(u))
                        }
                        scrollLastLiveData.value = false
                    }
                }
            }

            2 -> if (resultCode == Activity.RESULT_OK) {
                data?.getStringArrayListExtra(Gallery.INTENT_DATA_KEY)?.let { list ->
                    scrollLastLiveData.value = false
                    vm.sendImage(roomId, list.map { it.toUri() })
                }
            }

            3 -> if (resultCode == ChatOption.RESP_RELOAD) {
                roomLoadVM.load()
            }

            4 -> if (resultCode == Activity.RESULT_OK) {
                data?.getStringExtra(Recorder.TARGET_FILE)?.let {
                    scrollLastLiveData.value = false
                    val f = File(it)
                    val ext = data.getStringExtra(Recorder.TARGET_FILE_EXT)
                    vm.sendAudio(roomId, f.toUri(), ext)
                }
            }

            5 -> if (resultCode == Activity.RESULT_OK) {
                data?.data?.let {
                    scrollLastLiveData.value = false
                    vm.sendFile(roomId, it)
                }
            }
        }
    }

    private class Header(
        private val onClickListener: View.OnClickListener,
    ) : LoadStateAdapter<BindHolder<LoadState>>() {
        override fun onBindViewHolder(holder: BindHolder<LoadState>, loadState: LoadState) {
            holder.valueBind.bind(loadState, 0, emptyList())
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            loadState: LoadState,
        ): BindHolder<LoadState> {
            return LoadHeaderUI(parent.context)
                .apply {
                    view.setLayoutParams()
                    text.setOnClickListener(onClickListener)
                }
                .toBindHolder()
        }
    }


    companion object {

        const val ARG_ROOM_ID = "ARG_ROOM_ID"
        const val RESP_RELOAD = 1

    }
}
