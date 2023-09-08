@file:OptIn(
    ExperimentalFoundationApi::class,
)

package jp.co.toukei.log.trustar.compose

import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.Checkbox
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.runtime.toMutableStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sebaslogen.resaca.rememberScoped
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import jp.co.toukei.log.common.compose.AppPropTodo
import jp.co.toukei.log.common.compose.HeadButton
import jp.co.toukei.log.common.compose.ImageSlide
import jp.co.toukei.log.common.compose.LocalAppColor
import jp.co.toukei.log.common.compose.VerticalDivider
import jp.co.toukei.log.common.enum.LaunchIntent
import jp.co.toukei.log.common.enum.open
import jp.co.toukei.log.lib.compose.DefaultConfirmDialog
import jp.co.toukei.log.lib.compose.IconButton
import jp.co.toukei.log.lib.compose.InputTextField
import jp.co.toukei.log.lib.compose.NormalTopBar
import jp.co.toukei.log.lib.ctx.Ctx
import jp.co.toukei.log.lib.orElseNull
import jp.co.toukei.log.lib.toResultWithLoading
import jp.co.toukei.log.trustar.Config
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.activity.GalleryCompose
import jp.co.toukei.log.trustar.chat.activity.Camera
import jp.co.toukei.log.trustar.common.FileRef
import jp.co.toukei.log.trustar.common.fileRef
import jp.co.toukei.log.trustar.db.user.entity.DeliveryChart
import jp.co.toukei.log.trustar.viewmodel.DeliveryChartVM
import jp.co.toukei.log.trustar.viewmodel.EditImage
import third.Result
import java.io.File


@Composable
fun DeliveryChart(
    modifier: Modifier = Modifier,
    vm: DeliveryChartVM,
    naviBack: () -> Unit,
) {
    vm.matchedChart.subscribeAsState(null).value?.orElseNull()?.let { (chart, isNewCreate) ->
        var editMode by rememberScoped {
            mutableStateOf(isNewCreate)
        }
        val editModeToggle = {
            editMode = !editMode
        }
        if (editMode) {
            DeliveryChartEdit(
                modifier = modifier,
                vm = vm,
                naviBack = {
                    if (isNewCreate) {
                        naviBack()
                    } else {
                        editModeToggle()
                    }
                },
                done = editModeToggle,
                newCreate = isNewCreate,
                chart = chart,
            )
        } else {
            DeliveryChartView(
                modifier = modifier,
                vm = vm,
                naviBack = naviBack,
                editMode = editModeToggle,
                newCreate = isNewCreate,
                chart = chart,
            )
        }
    }
}

@Composable
private fun DeliveryChartEdit(
    modifier: Modifier = Modifier,
    vm: DeliveryChartVM,
    naviBack: () -> Unit,
    done: () -> Unit,
    newCreate: Boolean,
    chart: DeliveryChart,
) {
    var eHighlight by rememberScoped {
        mutableStateOf(false)
    }
    val info = chart.info
    val eDest = rememberScoped {
        mutableStateOf(info.dest)
    }
    val eAddr1 = rememberScoped {
        mutableStateOf(info.addr1)
    }
    val eAddr2 = rememberScoped {
        mutableStateOf(info.addr2)
    }
    val eTel = rememberScoped {
        mutableStateOf(info.tel)
    }
    val eCarrier = rememberScoped {
        mutableStateOf(info.carrier)
    }
    val eCarrierTel = rememberScoped {
        mutableStateOf(info.carrierTel)
    }

    val userStoredChartDir = remember(vm) { vm.user.userChartSyncDir }

    val eMemos: SnapshotStateMap<Long, DeliveryChart.ChartMemo> = rememberScoped {
        chart.memos.map { Config.incrementedLong() to it }.toMutableStateMap()
    }
    val eImages: SnapshotStateMap<Long, EditImage> = rememberScoped {
        chart.images.map {
            Config.incrementedLong() to EditImage.Exists(it, userStoredChartDir)
        }.toMutableStateMap()
    }

    val sortedMemo = eMemos.entries.sortedBy { it.key }
    val sortedImage = eImages.entries.sortedBy { it.key }

    val textColor = LocalAppColor.current.textColor
    val redHighlightColor = LocalAppColor.current.redHighlightColor

    val fetchImage = { pic: FileRef ->
        when (pic) {
            is FileRef.ByAnyFile -> Single.just(pic.file)
            is FileRef.ByKey -> vm.download(pic)
            is FileRef.ByStored -> Single.just(pic.stored.storedFile)
        }.toResultWithLoading()
    }

    BackHandler(true, naviBack)

    var viewImage by rememberScoped {
        mutableStateOf<File?>(null)
    }
    val clickImage: (File) -> Unit = {
        viewImage = it
    }
    Scaffold(
        modifier = modifier,
        topBar = {
            NormalTopBar(
                title = stringResource(
                    id = if (newCreate) R.string.new_create else R.string.delivery_edit
                ),
                navigationIcon = {
                    IconButton(imageVector = Icons.Default.Close, onClick = naviBack)
                },
                actions = {
                    IconButton(imageVector = Icons.Default.Done) {
                        val saved = vm.save(
                            old = chart,
                            info = DeliveryChart.Info(
                                dest = eDest.value,
                                addr1 = eAddr1.value,
                                addr2 = eAddr2.value,
                                tel = eTel.value,
                                carrier = eCarrier.value,
                                carrierTel = eCarrierTel.value,
                            ),
                            memos = sortedMemo.map { it.value },
                            images = sortedImage.map { it.value },
                        )
                        if (saved) {
                            done()
                        }
                    }
                }
            )
        },
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(it)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val rowModifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)

            val weight1 = Modifier.weight(1F)

            val firstColModifier = Modifier
                .padding(8.dp)
                .widthIn(min = 72.dp)

            DestInfo(
                modifier = rowModifier,
            )

            Row(
                modifier = rowModifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = firstColModifier,
                    text = remember {
                        AnnotatedString
                            .Builder()
                            .apply {
                                withStyle(
                                    SpanStyle(
                                        color = Color.Red,
                                        fontSize = 16.sp,
                                    )
                                ) {
                                    append('*')
                                }
                                append(Ctx.context.getString(R.string.delivery_dest))
                            }
                            .toAnnotatedString()
                    },
                    style = AppPropTodo.Text.smallBold
                )
                EditText(
                    modifier = weight1,
                    text = eDest,
                    maxLength = 400,
                )
            }
            VerticalDivider()

            Row(
                modifier = rowModifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = firstColModifier,
                    text = stringResource(id = R.string.delivery_dest_addr1),
                    style = AppPropTodo.Text.smallBold
                )
                EditText(
                    modifier = weight1,
                    text = eAddr1,
                    maxLength = 100,
                )
                /*
                Column(
                    modifier = maxWidthModifier.align(Alignment.Top)
                ) {
                    var query by remember {
                        mutableStateOf<Pair<String, Long>?>(null)
                    }
                    val queryResult = query?.let { (zip, id) ->
                        remember(zip, vm, id) {
                            vm.zipCodeQuery(zip).toResult().withLoading(100)
                        }.subscribeAsState(null)
                    }
                    queryResult?.value?.value()?.let {
                        LaunchedEffect(Unit) {
                            eAddr1.value = it
                        }
                    }

                    Row(
                        modifier = maxWidthModifier,
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        var zipcode by rememberScoped {
                            mutableStateOf("")
                        }
                        val focusManager = LocalFocusManager.current
                        ZipCode(
                            modifier = Modifier.weight(1F, false),
                            zipcode = zipcode
                        ) { z ->
                            zipcode = z
                        }
                        HeadButton(
                            horizontalPadding = 16,
                            verticalPadding = 4,
                            text = stringResource(id = R.string.zipcode_query),
                            style = AppPropTodo.Text.defaultBold,
                            color = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                        ) {
                            focusManager.clearFocus()
                            query = zipcode.takeUnless(String::isEmpty)?.let { z ->
                                z to System.nanoTime()
                            }
                        }
                        if (queryResult?.value is Result.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(24.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }
                    }
                */
            }

            Row(
                modifier = rowModifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = firstColModifier,
                    text = stringResource(id = R.string.delivery_dest_addr2),
                    style = AppPropTodo.Text.smallBold,
                )
                EditText(
                    modifier = weight1,
                    text = eAddr2,
                    maxLength = 300,
                )
            }
            VerticalDivider()

            Row(
                modifier = rowModifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = firstColModifier,
                    text = stringResource(id = R.string.delivery_dest_tel),
                    style = AppPropTodo.Text.smallBold
                )
                EditText(
                    modifier = weight1,
                    keyboardType = KeyboardType.Phone,
                    text = eTel,
                    maxLength = 50,
                )
            }
            VerticalDivider()
            Row(
                modifier = rowModifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = firstColModifier,
                    text = stringResource(id = R.string.delivery_carrier),
                    style = AppPropTodo.Text.smallBold
                )
                EditText(
                    modifier = weight1,
                    text = eCarrier,
                    maxLength = 100,
                )
            }
            VerticalDivider()
            Row(
                modifier = rowModifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = firstColModifier,
                    text = stringResource(id = R.string.delivery_carrier_tel),
                    style = AppPropTodo.Text.smallBold
                )
                EditText(
                    modifier = weight1,
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Done,
                    text = eCarrierTel,
                    maxLength = 50,
                )
            }
            VerticalDivider()

            Row(
                modifier = rowModifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.memo),
                    style = AppPropTodo.Text.bold16
                )
                Spacer(modifier = Modifier.weight(1F))
                HeadButton(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    horizontalPadding = 12,
                    verticalPadding = 6,
                    text = stringResource(id = R.string.delivery_emphasis),
                    style = AppPropTodo.Text.defaultBold,
                    color = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ) {
                    eHighlight = !eHighlight
                }

                IconButton(imageVector = Icons.Default.Add, tint = AppPropTodo.Color.iconTint) {
                    eMemos[Config.incrementedLong()] = DeliveryChart.ChartMemo()
                }
            }

            val memoFirstColModifier = Modifier
                .weight(2F)
                .padding(2.dp)
            val memoSecColModifier = Modifier
                .weight(3F)
                .padding(2.dp)

            sortedMemo.forEach { (id, memo) ->
                Row(
                    modifier = rowModifier,
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val highlight = memo.highlight

                    AnimatedVisibility(visible = eHighlight) {
                        Checkbox(
                            checked = highlight,
                            onCheckedChange = { t ->
                                eMemos[id] = memo.copy(
                                    highlight = t,
                                )
                            }
                        )
                    }
                    EditText(
                        modifier = memoFirstColModifier,
                        textColor = if (highlight) redHighlightColor else textColor,
                        text = memo.label,
                        placeholder = stringResource(id = R.string.title),
                    ) { t ->
                        eMemos[id] = memo.copy(
                            label = t,
                        )
                    }
                    EditText(
                        modifier = memoSecColModifier,
                        textColor = if (highlight) redHighlightColor else textColor,
                        text = memo.note,
                        placeholder = stringResource(id = R.string.delivery_memo),
                        imeAction = ImeAction.Done,
                    ) { t ->
                        eMemos[id] = memo.copy(
                            note = t,
                        )
                    }
                    key(id) {
                        IconButton(
                            imageVector = Icons.Default.Delete,
                            tint = AppPropTodo.Color.iconTint,
                        ) {
                            eMemos.remove(id)
                        }
                    }
                }
                VerticalDivider()
            }

            ImagePart(
                modifier = rowModifier,
                vm = vm,
                size = sortedImage.size,
                addedTmpFile = { fs ->
                    fs.forEach { f ->
                        eImages[Config.incrementedLong()] = EditImage.Add(f, userStoredChartDir)
                    }
                },
                removeImage = { index ->
                    eImages.remove(sortedImage[index].key)
                },
                key = { page -> sortedImage[page].value.fileRef },
                request = fetchImage,
                clickImage = clickImage,
            )
        }
    }

    viewImage?.let { v ->
        ViewImage(v) {
            viewImage = null
        }
    }
}

@Composable
fun DeliveryChartView(
    modifier: Modifier = Modifier,
    vm: DeliveryChartVM,
    naviBack: () -> Unit,
    editMode: () -> Unit,
    newCreate: Boolean,
    chart: DeliveryChart,
) {
    val textColor = LocalAppColor.current.textColor
    val redHighlightColor = LocalAppColor.current.redHighlightColor

    val fetchImage = { pic: FileRef ->
        when (pic) {
            is FileRef.ByAnyFile -> Single.just(pic.file)
            is FileRef.ByKey -> vm.download(pic)
            is FileRef.ByStored -> Single.just(pic.stored.storedFile)
        }.toResultWithLoading()
    }
    val userStoredChartDir = remember(vm) { vm.user.userChartSyncDir }

    val pics: List<FileRef> = remember(chart.images) {
        chart.images.map { it.dbStoredFile.fileRef(userStoredChartDir) }
    }
    val context = LocalContext.current

    BackHandler(true, naviBack)

    var viewImage by rememberScoped {
        mutableStateOf<File?>(null)
    }
    val clickImage: (File) -> Unit = {
        viewImage = it
    }
    val info = chart.info
    Scaffold(
        modifier = modifier,
        topBar = {
            NormalTopBar(
                title = stringResource(
                    id = if (newCreate) R.string.new_create else R.string.delivery_chart
                ),
                navigationIcon = {
                    IconButton(imageVector = Icons.Default.ArrowBack, onClick = naviBack)
                },
                actions = {
                    IconButton(imageVector = Icons.Default.Edit, onClick = editMode)
                },
            )
        },
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(it)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val clickColor = MaterialTheme.colorScheme.primary
            val rowModifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp)

            val weight1 = Modifier
                .weight(1F)
                .padding(4.dp)

            val firstColModifier = Modifier
                .padding(8.dp)
                .widthIn(min = 72.dp)

            DestInfo(
                modifier = rowModifier,
            )

            Row(
                modifier = rowModifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = firstColModifier,
                    text = stringResource(id = R.string.delivery_dest),
                    style = AppPropTodo.Text.smallBold
                )
                Text(
                    modifier = weight1,
                    text = info.dest,
                    fontSize = 14.sp,
                )
            }
            VerticalDivider()

            Row(
                modifier = rowModifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = firstColModifier,
                    text = stringResource(id = R.string.delivery_dest_addr),
                    style = AppPropTodo.Text.smallBold,
                )
                Text(
                    modifier = Modifier.clickable {
                        LaunchIntent.Map.open(context, info.address())
                    } then weight1,
                    text = info.address(),
                    fontSize = 14.sp,
                    color = clickColor,
                )
            }
            VerticalDivider()

            Row(
                modifier = rowModifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = firstColModifier,
                    text = stringResource(id = R.string.delivery_dest_tel),
                    style = AppPropTodo.Text.smallBold
                )
                Text(
                    modifier = Modifier.clickable {
                        LaunchIntent.Dial.open(context, info.tel)
                    } then weight1,
                    text = info.tel,
                    fontSize = 14.sp,
                    color = clickColor,
                )
            }
            VerticalDivider()
            Row(
                modifier = rowModifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = firstColModifier,
                    text = stringResource(id = R.string.delivery_carrier),
                    style = AppPropTodo.Text.smallBold
                )
                Text(
                    modifier = weight1,
                    text = info.carrier,
                    fontSize = 14.sp,
                )
            }
            VerticalDivider()
            Row(
                modifier = rowModifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = firstColModifier,
                    text = stringResource(id = R.string.delivery_carrier_tel),
                    style = AppPropTodo.Text.smallBold
                )
                Text(
                    modifier = Modifier.clickable {
                        LaunchIntent.Dial.open(context, info.carrierTel)
                    } then weight1,
                    text = info.carrierTel,
                    fontSize = 14.sp,
                    color = clickColor,
                )
            }
            VerticalDivider()

            TitleV(rowModifier, R.string.memo)

            chart.memos.forEach { memo ->
                Row(
                    modifier = rowModifier,
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = firstColModifier,
                        text = memo.label,
                        style = AppPropTodo.Text.smallBold,
                        color = if (memo.highlight) redHighlightColor else textColor,
                    )
                    Text(
                        modifier = weight1,
                        text = memo.note,
                        fontSize = 14.sp,
                        color = if (memo.highlight) redHighlightColor else textColor,
                    )
                }
                VerticalDivider()
            }
            ImagePart(
                modifier = rowModifier,
                size = pics.size,
                key = { page -> pics[page] },
                request = fetchImage,
                clickImage = clickImage,
            )
        }
    }

    viewImage?.let { v ->
        ViewImage(v) {
            viewImage = null
        }
    }
}


@Composable
private fun DestInfo(
    modifier: Modifier,
) {
    Row(
        modifier = modifier.sizeIn(minHeight = 48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = R.string.delivery_dest_info),
            modifier = Modifier.weight(1F),
            style = AppPropTodo.Text.bold16
        )
    }
}
/*
@Composable
private fun ZipCode(
    modifier: Modifier,
    zipcode: String,
    zipCodeChange: (String) -> Unit,
) {
    val zipInputRegex = remember {
        Regex("^(\\d{0,7})|(\\d{3}-?\\d{0,4})$")
    }
    val textStyle = AppPropTodo.Text.default
    val size = LocalDensity.current.run { textStyle.fontSize.toDp() }
    val wide = size * 8
    InputTextField(
        modifier = modifier.widthIn(min = wide),
        value = zipcode,
        keyboardType = KeyboardType.Decimal,
        onValueChange = { s ->
            if (zipInputRegex.matches(s)) {
                zipCodeChange(s)
            }
        },
        decorationBoxModifier = Modifier
            .background(MaterialTheme.colorScheme.onPrimary)
            .border(
                width = 1.dp,
                color = AppPropTodo.Color.inputBoxBorder,
                shape = RoundedCornerShape(1.dp)
            )
            .padding(8.dp),
        textStyle = textStyle,
        placeholder = stringResource(id = R.string.zipcode)
    )
}*/

@Composable
private fun ColumnScope.ImagePartCommon(
    pagerState: PagerState,
    onDelete: ((index: Int) -> Unit)? = null,
    key: (page: Int) -> FileRef,
    request: (key: FileRef) -> Flowable<Result<File>>,
    clickImage: (File) -> Unit,
) {
    val onDeleteConfirm = onDelete?.let { r ->
        val deleteDialog = rememberScoped {
            mutableStateOf<Int?>(null)
        }
        deleteDialog.value?.let {
            DefaultConfirmDialog(
                onDismissRequest = {
                    deleteDialog.value = null
                },
                content = R.string.delivery_photo_del_msg,
                confirmButtonText = R.string.delete
            ) {
                deleteDialog.value = null
                r(it)
            }
        }
        val deleteReal: (index: Int) -> Unit = { index: Int ->
            deleteDialog.value = index
        }
        deleteReal
    }

    val pageCount = pagerState.pageCount
    if (pageCount > 0) {
        ImageSlide(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(min = 256.dp, max = 512.dp)
                .aspectRatio(4F / 3),
            pagerState = pagerState,
            key = key,
            request = request,
            onDelete = onDeleteConfirm
        ) { _, it ->
            clickImage(it)
        }
        if (pagerState.currentPage < pageCount) {
            Text(
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                text = "${pagerState.currentPage + 1} / $pageCount"
            )
        }
    } else {
        Text(
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .padding(16.dp),
            text = stringResource(id = R.string.no_data_placeholder)
        )
    }
}


@Composable
private fun ColumnScope.TitleV(
    modifier: Modifier,
    @StringRes id: Int
) {
    Spacer(modifier = Modifier.padding(12.dp))
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = id),
            modifier = Modifier.weight(1F),
            style = AppPropTodo.Text.bold16
        )
    }
}

@Composable
private fun ColumnScope.ImagePart(
    modifier: Modifier,
    size: Int,
    key: (page: Int) -> FileRef,
    request: (key: FileRef) -> Flowable<Result<File>>,
    clickImage: (File) -> Unit,
) {
    TitleV(modifier, R.string.delivery_photo)
    ImagePartCommon(
        pagerState = rememberPagerState { size },
        onDelete = null,
        key = key,
        request = request,
        clickImage = clickImage
    )
}

@Composable
private fun ColumnScope.ImagePart(
    modifier: Modifier,
    vm: DeliveryChartVM,
    size: Int,
    addedTmpFile: (List<FileRef.ByAnyFile>) -> Unit,
    removeImage: (index: Int) -> Unit,
    key: (page: Int) -> FileRef,
    request: (key: FileRef) -> Flowable<Result<File>>,
    clickImage: (File) -> Unit,
) {
    val addPicLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {
            GalleryCompose.resolveResult(it)?.let { uris ->
                addedTmpFile((vm.readImageToTmpJPEG(uris)))
            }
        }
    )
    val takePhoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {
            Camera.resolveResult(it)?.let { cr ->
                addedTmpFile(vm.readImageToTmpJPEG(listOf(cr.fileUri)))
            }
        }
    )
    val context = LocalContext.current
    val addPic = {
        addPicLauncher.launch(GalleryCompose.intentForStartActivity(context))
    }
    val takePho = {
        takePhoLauncher.launch(
            Camera.intentForStartActivity(
                context,
                vm.createTmpFile(),
                false
            )
        )
    }
    Spacer(modifier = Modifier.padding(8.dp))
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = R.string.delivery_photo),
            modifier = Modifier.weight(1F),
            style = AppPropTodo.Text.bold16
        )
        IconButton(
            imageVector = Icons.Default.Photo,
            tint = AppPropTodo.Color.iconTint,
            onClick = addPic
        )
        IconButton(
            imageVector = Icons.Default.CameraAlt,
            tint = AppPropTodo.Color.iconTint,
            onClick = takePho
        )
    }
    val pagerState = rememberPagerState {
        size
    }

    ImagePartCommon(
        pagerState = pagerState,
        onDelete = removeImage,
        key = key,
        request = request,
        clickImage = clickImage
    )
}

@Composable
private fun EditText(
    modifier: Modifier = Modifier,
    textColor: Color = Color.Unspecified,
    style: TextStyle = LocalTextStyle.current,
    imeAction: ImeAction = ImeAction.Next,
    keyboardType: KeyboardType = KeyboardType.Text,
    placeholder: String? = null,
    text: String,
    textChanged: (String) -> Unit
) {
    InputTextField(
        modifier = modifier,
        value = text,
        onValueChange = textChanged,
        imeAction = imeAction,
        keyboardType = keyboardType,
        textStyle = style.copy(color = textColor),
        placeholder = placeholder,
        decorationBoxModifier = Modifier
            .background(
                MaterialTheme.colorScheme.onPrimary
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(1.dp)
            )
            .padding(8.dp)
    )
}

@Composable
private fun EditText(
    modifier: Modifier = Modifier,
    textColor: Color = Color.Unspecified,
    style: TextStyle = LocalTextStyle.current,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    maxLength: Int = Int.MAX_VALUE,
    text: MutableState<String>,
) {
    EditText(
        modifier = modifier,
        textColor = textColor,
        style = style,
        keyboardType = keyboardType,
        imeAction = imeAction,
        text = text.value
    ) {
        if (it.length <= maxLength) {
            text.value = it
        }
    }
}
