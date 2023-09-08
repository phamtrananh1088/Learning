package jp.co.toukei.log.common.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.github.skgmn.composetooltip.AnchorEdge
import com.github.skgmn.composetooltip.Tooltip
import com.github.skgmn.composetooltip.rememberTooltipStyle
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.chat.MessageItem
import jp.co.toukei.log.trustar.db.chat.entity.ChatMessagePending
import jp.co.toukei.log.trustar.humanReadableSize
import kotlinx.coroutines.flow.Flow

@Composable
fun ChattingUI(
    myId: String,
    page: Flow<PagingData<MessageItem>>,
    micClick: () -> Unit,
    imageClick: () -> Unit,
    cameraClick: () -> Unit,
    uploadClick: () -> Unit,
) {
    Column {
        ChattingList(myId, page)
        //todo
    }
}

@Composable
fun MaterialChattingList(myId: String, page: Flow<PagingData<MessageItem>>) {
    MaterialTheme(
        //todo
    ) {
        ChattingList(myId, page)
    }
}

@Composable
fun ChattingList(myId: String, page: Flow<PagingData<MessageItem>>) {
    val collect: LazyPagingItems<MessageItem> = page.collectAsLazyPagingItems()

    val scrollState = rememberLazyListState(
        initialFirstVisibleItemIndex = collect.itemCount
    )

    var popupReaderListRow: String? by remember {
        mutableStateOf(null)
    }

    LazyColumn(
        state = scrollState,
        verticalArrangement = Arrangement.Bottom
    ) {
        itemsIndexed(
            collect,
            key = { _, msg -> msg.id_Compose }
        ) { index, msg ->
            val lastIndex = collect.itemCount - 1
            val uid = msg?.userId
            val isSelf = myId == uid
            val prevSameUid = if (index < 1) false else {
                collect.peek(index - 1)?.userId == uid
            }
            val nextSameUid = if (index >= lastIndex) false else {
                collect.peek(index + 1)?.userId == uid
            }

            CompositionLocalProvider(LocalLayoutDirection provides if (isSelf) LayoutDirection.Rtl else LayoutDirection.Ltr) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                ) {
                    Box(
                        modifier = Modifier.widthIn(0.dp, 360.dp)
                    ) {
                        if (msg == null) {
                            Placeholder()
                        } else {
                            MessageWithAvatar(
                                msg,
                                prevSameUid,
                                nextSameUid,
                                isSelf,
                                popupReaderListRow
                            ) { popupReaderListRow = it }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Placeholder() {
    Text(text = "Placeholder??")
}

@Composable
fun MessageWithAvatar(
    msg: MessageItem,
    prevSameUid: Boolean,
    nextSameUid: Boolean,
    isSelf: Boolean,
    popupReaderListRow: String?,
    popupReaderListRowUpdate: (String?) -> Unit,
) {
    val displayAvatar = !(isSelf || prevSameUid)
    val displayName = displayAvatar

    if (isSelf) {
        MessageAndDateStr(
            msg,
            prevSameUid,
            nextSameUid,
            true,
            popupReaderListRow,
            popupReaderListRowUpdate
        )
    } else {
        Row(
            verticalAlignment = Alignment.Top
        ) {
            if (msg is MessageItem.Sent && displayAvatar) {
                MsgAvatar(sent = msg, modifier = Modifier.size(54.dp))
            } else {
                Spacer(modifier = Modifier.size(54.dp, 1.dp))
            }
            val name = if (msg is MessageItem.Sent) msg.username else null
            if (name != null && displayName) {
                Column {
                    Text(
                        text = name,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(8.dp, 4.dp),
                        color = colorResource(id = R.color.textColor)
                    )
                    MessageAndDateStr(
                        msg,
                        false,
                        nextSameUid,
                        false,
                        popupReaderListRow,
                        popupReaderListRowUpdate
                    )
                }
            } else {
                MessageAndDateStr(
                    msg,
                    prevSameUid,
                    nextSameUid,
                    false,
                    popupReaderListRow,
                    popupReaderListRowUpdate
                )
            }
        }
    }
}

@Composable
fun MessageAndDateStr(
    msg: MessageItem,
    prevSameUid: Boolean,
    nextSameUid: Boolean,
    isSelf: Boolean,
    popupReaderListRow: String?,
    popupReaderListRowUpdate: (String?) -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(start = 8.dp, bottom = 8.dp)
            .widthIn(min = 32.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        Message_(
            msg, prevSameUid, nextSameUid, isSelf,
            modifier = Modifier.weight(1F, fill = false)
        )
        Column(
            modifier = Modifier
                .padding(start = 10.dp),
        ) {
            if (isSelf) {
                if (msg is MessageItem.Pending) {
                    when (msg.state) {
                        ChatMessagePending.STATE_SENDING -> {
                            Text(
                                text = stringResource(R.string.msg_sending),
                                fontSize = 12.sp,
                                modifier = Modifier
                                    .padding(2.dp),
                                color = colorResource(id = R.color.textColor)
                            )
                        }

                        ChatMessagePending.STATE_ERR -> {
                            Text(
                                text = stringResource(R.string.msg_tap_to_retry),
                                fontSize = 12.sp,
                                modifier = Modifier
                                    .padding(2.dp),
                                color = Color.Red
                            )
                        }
                    }
                } else if (msg is MessageItem.Sent) {
                    val readerSize = msg.readers.size
                    if (readerSize > 0) {
                        val everyoneRead = readerSize == msg.userCountOfRoom_Compose

                        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                            Box {
                                Text(
                                    text = stringResource(R.string.msg_been_read).let { if (everyoneRead) it else "$it$readerSize" },
                                    fontSize = 12.sp,
                                    color = colorResource(id = R.color.textColor),
                                    modifier = Modifier
                                        .clickable {
                                            popupReaderListRowUpdate(msg.msg.messageRow)
                                        }
                                        .padding(2.dp),
                                )
                                if (popupReaderListRow != null && popupReaderListRow == msg.msg.messageRow) {
                                    Tooltip(
                                        anchorEdge = AnchorEdge.Start,
                                        tooltipStyle = rememberTooltipStyle(
                                            color = colorResource(
                                                id = R.color.grayBackground
                                            )
                                        ),
                                        onDismissRequest = { popupReaderListRowUpdate(null) }
                                    ) {
                                        LazyColumn {
                                            items(items = msg.readers) {
                                                Text(
                                                    text = it.name.orEmpty(),
                                                    color = colorResource(id = R.color.textColor),
                                                    modifier = Modifier.padding(8.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            MsgDateStr(msg)
        }
    }
}

@Composable
fun MsgDateStr(msg: MessageItem) {
    Text(
        text = msg.dateStr,
        fontSize = 14.sp,
        modifier = Modifier
            .padding(2.dp),
        color = colorResource(id = R.color.textColor)
    )
}

@Composable
private fun Message_(
    msg: MessageItem,
    prevSameUid: Boolean,
    nextSameUid: Boolean,
    isSelf: Boolean,
    modifier: Modifier = Modifier,
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        when (msg) {
            is MessageItem.Pending.AudioPending -> AudioMessage(
                msg,
                isSelf = isSelf,
                prevSameUid = prevSameUid,
                nextSameUid = nextSameUid,
                modifier = modifier
            )

            is MessageItem.Pending.FilePending -> FileMessage(
                msg,
                isSelf = isSelf,
                modifier = modifier
            )

            is MessageItem.Pending.ImgPending -> ImageMessage(
                msg,
                modifier = modifier
            )

            is MessageItem.Pending.TextPending -> TextMessage(
                msg,
                isSelf = isSelf,
                prevSameUid = prevSameUid,
                nextSameUid = nextSameUid,
                modifier = modifier
            )

            is MessageItem.Pending.VideoPending -> VideoMessage(
                msg,
                modifier = modifier
            )

            is MessageItem.Sent.AudioSent -> AudioMessage(
                msg,
                isSelf = isSelf,
                prevSameUid = prevSameUid,
                nextSameUid = nextSameUid,
                modifier = modifier
            )

            is MessageItem.Sent.FileSent -> FileMessage(
                msg,
                isSelf = isSelf,
                modifier = modifier
            )

            is MessageItem.Sent.ImgSent -> ImageMessage(
                msg,
                modifier = modifier
            )

            is MessageItem.Sent.TextSent -> TextMessage(
                msg,
                isSelf = isSelf,
                prevSameUid = prevSameUid,
                nextSameUid = nextSameUid,
                modifier = modifier
            )

            is MessageItem.Sent.VideoSent -> VideoMessage(
                msg,
                modifier = modifier
            )
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MsgAvatar(sent: MessageItem.Sent, modifier: Modifier = Modifier) {
    val model = remember(sent.avatar?.uri?.toString()) {
        sent.avatar
    }
    GlideImage(model = model, contentDescription = null, modifier = modifier) {
        it.diskCacheStrategy(DiskCacheStrategy.NONE)
            .placeholder(R.drawable.ic_avatar)
            .centerCrop()
    }
}

@Composable
fun <T> TextMessage(
    msg: T,
    prevSameUid: Boolean,
    nextSameUid: Boolean,
    isSelf: Boolean,
    modifier: Modifier = Modifier,
) where T : MessageItem, T : MessageItem.Text {
    val t = if (prevSameUid) 4.dp else 8.dp
    val b = if (nextSameUid) 4.dp else 8.dp
    Text(
        text = msg.text_Compose.orEmpty(),
        fontSize = 16.sp,
        modifier = modifier
            .clip(RoundedCornerShape(topStart = t, topEnd = t, bottomStart = b, bottomEnd = b))
            .background(colorResource(id = if (isSelf) R.color.primaryLight else R.color.grayBackground))
            .padding(8.dp),
        color = if (isSelf) Color.White else colorResource(id = R.color.textColor)
    )
}


@Composable
fun <T> AudioMessage(
    msg: T,
    prevSameUid: Boolean,
    nextSameUid: Boolean,
    isSelf: Boolean,
    modifier: Modifier = Modifier,
) where T : MessageItem, T : MessageItem.Audio {
    val f = remember { msg.attachmentExt() }
    if (f != null) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .clip(RoundedCornerShape(8.dp))
                .background(colorResource(id = if (isSelf) R.color.primaryLight else R.color.grayBackground))
        ) {
            val icon = if (f.file.exists()) {
                R.drawable.round_play_circle_outline_24
            } else {
                R.drawable.round_download_24
            }
            Image(
                modifier = Modifier
                    .size(48.dp)
                    .padding(8.dp),
                painter = painterResource(id = icon),
                contentDescription = null,
                colorFilter = ColorFilter.tint(if (isSelf) Color.White else colorResource(id = R.color.textColor))
            )
            //todo progress update: text = progress.progressString()
            Text(
                text = stringResource(id = R.string.audio_file),
                fontSize = 16.sp,
                color = if (isSelf) Color.White else colorResource(id = R.color.textColor),
                modifier = Modifier
                    .padding(8.dp),
            )
        }
    } else InvalidMessage()
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun <T> VideoMessage(
    msg: T,
    modifier: Modifier = Modifier,
) where T : MessageItem, T : MessageItem.Video {
    val f = remember { msg.attachmentExt() }
    if (f != null) {
        Box(
            modifier = modifier
                .sizeIn(maxHeight = 192.dp, maxWidth = 192.dp),
        ) {
            if (f.file.exists()) {
                val model = remember { f.file }
                Box {
                    GlideImage(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .sizeIn(54.dp, 54.dp),
                        model = model,
                        contentDescription = null,
                    ) {
                        it.diskCacheStrategy(DiskCacheStrategy.NONE)
                            .error(R.drawable.round_broken_image_24)
                            .placeholder(R.drawable.round_photo_24)
                            .dontAnimate()
                            .fitCenter()
                    }
                    Image(
                        modifier = Modifier
                            .size(48.dp)
                            .align(Alignment.Center),
                        painter = painterResource(id = R.drawable.round_play_circle_outline_24),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(colorResource(id = R.color.lightText))
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(colorResource(id = R.color.textColor))
                        .defaultMinSize(minHeight = 128.dp)
                ) {
                    //todo progress update: text = progress.progressString()
                    Text(
                        text = stringResource(id = R.string.tap_to_download),
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.lightText),
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.TopCenter),
                    )
                    Image(
                        modifier = Modifier
                            .size(48.dp)
                            .align(Alignment.Center),
                        painter = painterResource(id = R.drawable.round_download_24),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(colorResource(id = R.color.lightText))
                    )
                }
            }
        }
    } else InvalidMessage()
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun <T> ImageMessage(
    msg: T,
    modifier: Modifier = Modifier,
) where T : MessageItem, T : MessageItem.Image {
    Box(
        modifier = modifier
            .sizeIn(maxHeight = 192.dp, maxWidth = 192.dp),
    ) {
        val model = remember(msg.imageUri?.uri?.toString()) {
            msg.imageUri
        }

        GlideImage(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .sizeIn(54.dp, 54.dp),
            model = model,
            contentDescription = null,
        ) {
            it.diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.round_broken_image_24)
                .placeholder(R.drawable.round_photo_24)
                .dontAnimate()
                .fitCenter()
        }
    }
}

@Composable
fun <T> FileMessage(
    msg: T,
    isSelf: Boolean,
    modifier: Modifier = Modifier,
) where T : MessageItem, T : MessageItem.F {
    val f = remember { msg.attachmentExt() }
    if (f != null) {
        Column(
            modifier = modifier
                .clip(RoundedCornerShape(12.dp))
                .background(colorResource(id = if (isSelf) R.color.primaryLight else R.color.grayBackground))
                .sizeIn(maxWidth = 192.dp, minWidth = 192.dp)
                .padding(8.dp)
        ) {
            val color = if (isSelf) Color.White else colorResource(id = R.color.textColor)

            val exists = f.file.exists()
            val fileSize = remember(f) { f.size?.let { humanReadableSize(it) }.orEmpty() }
            Image(
                modifier = Modifier
                    .size(48.dp)
                    .padding(8.dp)
                    .align(Alignment.CenterHorizontally),
                painter = painterResource(id = R.drawable.round_insert_drive_file_24),
                contentDescription = null,
                colorFilter = ColorFilter.tint(color)
            )
            Text(
                text = f.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = color,
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterHorizontally),
            )
            Text(
                text = fileSize,
                fontSize = 14.sp,
                color = color,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .align(Alignment.CenterHorizontally),
            )
            Divider(
                color = color,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth()
                    .height(1.dp)
            )
            //todo progress update: text = progress.progressString()
            Text(
                text = stringResource(if (exists) R.string.preview else R.string.download),
                fontSize = 16.sp,
                color = color,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .align(Alignment.CenterHorizontally),
            )
        }
    } else InvalidMessage()
}

@Composable
fun InvalidMessage() {
    Text(
        text = stringResource(R.string.invalid_message),
        color = colorResource(id = R.color.textColor),
        modifier = Modifier
            .border(1.dp, Color.Red)
            .padding(2.dp)
    )
}
