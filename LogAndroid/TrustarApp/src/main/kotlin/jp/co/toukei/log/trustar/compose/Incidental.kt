@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)

package jp.co.toukei.log.trustar.compose

import android.app.Activity
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.sebaslogen.resaca.rememberScoped
import jp.co.toukei.log.common.compose.AppPropTodo
import jp.co.toukei.log.common.compose.BigButton
import jp.co.toukei.log.common.compose.HeadButton
import jp.co.toukei.log.common.compose.LocalAppColor
import jp.co.toukei.log.lib.compose.DefaultSheet
import jp.co.toukei.log.trustar.Config
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.compose.sheet.ShipperChoose
import jp.co.toukei.log.trustar.db.user.embedded.IncidentalListItem
import jp.co.toukei.log.trustar.db.user.entity.IncidentalHeader
import jp.co.toukei.log.trustar.db.user.entity.TimeRange
import jp.co.toukei.log.trustar.defaultSizeTextDrawable
import jp.co.toukei.log.trustar.enum.EditEnum
import jp.co.toukei.log.trustar.enum.IncidentalType
import jp.co.toukei.log.trustar.feature.sign.SignActivity
import jp.co.toukei.log.trustar.viewmodel.IncidentalListVM
import third.Result
import java.util.UUID

@Composable
fun IncidentalList(
    modifier: Modifier = Modifier,
    list: List<IncidentalListItem>,
    clickButton: () -> Unit,
    deleteItem: (IncidentalListItem) -> Unit,
    clickItem: (IncidentalListItem) -> Unit,
) {
    val listBg = LocalAppColor.current.listItemBg
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        HeadButton(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(8.dp),
            horizontalPadding = 24,
            verticalPadding = 8,
            text = stringResource(id = R.string.incidental_sheet_new_create),
            style = AppPropTodo.Text.bold16,
            color = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            click = clickButton
        )
        PrimaryListHeadTitle(
            stringResource(id = R.string.incidental_created)
        )

        if (list.isEmpty()) {
            Placeholder()
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F, false)
        ) {
            itemsIndexed(list) { index, it ->
                key(index, it) {
                    SwipeToDismiss(
                        onConfirm = {
                            deleteItem(it)
                        }
                    ) {
                        IncidentalListItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(listBg),
                            it
                        ) {
                            clickItem(it)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun IncidentalListItem(
    modifier: Modifier = Modifier,
    item: IncidentalListItem,
    click: () -> Unit,
) {
    Row(
        modifier = modifier
            .clickable(onClick = click)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .weight(1F)
                .padding(8.dp),
            text = item.shipperNm.orEmpty(),
            style = AppPropTodo.Text.size16,
        )
        when (item.signStatus) {
            0 -> BinStatusLabel(
                text = stringResource(id = R.string.unsigned),
                bgColor = AppPropTodo.Color.unsignedBg,
                textColor = MaterialTheme.colorScheme.onPrimary,
                minWide = 3,
                textStyle = AppPropTodo.Text.default
            )

            1 -> BinStatusLabel(
                text = stringResource(id = R.string.signed),
                bgColor = AppPropTodo.Color.signedBg,
                textColor = MaterialTheme.colorScheme.onPrimary,
                minWide = 3,
                textStyle = AppPropTodo.Text.default
            )
        }
    }
}

@Composable
fun IncidentalView(
    modifier: Modifier = Modifier,
    header: IncidentalHeader,
    vm: IncidentalListVM,
    defaultShipper: ComposeData.Shipper?,
    defaultWorks: List<ComposeData.IncidentalWork>?,
    defaultTimeRangeList: List<TimeRange>,
    edit: () -> Unit,
    onClick: () -> Unit,
) {
    val timeRangeListGroup = rememberScoped {
        defaultTimeRangeList.distinctBy(TimeRange.key).groupBy { it.type }
    }

    val lists = IncidentalType.values().map {
        it to timeRangeListGroup[it.type]?.sortedWith(
            compareBy(
                { it.start },
                { it.end }
            )
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {
            if (it.resultCode == Activity.RESULT_OK) {
                SignActivity.resolveResultIntent(it.data)?.let { f ->
                    vm.sign(f, header)
                }
            }
        }
    )
    val context = LocalContext.current

    val signFile by remember(vm, header.uuid) {
        vm.signFile(header.uuid)
    }.subscribeAsState(null)

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = modifier
                .weight(1F, true)
                .verticalScroll(rememberScrollState())
        ) {
            Header(
                text = defaultShipper?.shipperNm.orEmpty(),
                work = remember(defaultWorks) {
                    defaultWorks?.let(ComposeData.IncidentalWork::joinWorkNm)
                },
                buttonText = stringResource(id = R.string.edit),
                buttonClick = edit,
            )
            lists.forEach { (enum, list) ->
                TimeRangesView(
                    when (enum) {
                        IncidentalType.Nimachi -> R.string.incidental_sheet_await
                        IncidentalType.Futai -> R.string.incidental_sheet_addition_work
                    },
                    list,
                )
            }
            PrimaryListHeadTitle(
                stringResource(id = R.string.incidental_sheet_signature)
            )

            GlideImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(3F)
                    .padding(6.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.onPrimary)
                    .border(
                        width = 0.4.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .clickable {
                        launcher.launch(
                            SignActivity.intentForStartActivity(
                                context,
                                vm.createTmpFile().canonicalPath
                            )
                        )
                    }
                    .padding(2.dp),
                model = when (val v = signFile) {
                    is Result.Error -> {
                        context.defaultSizeTextDrawable(R.string.server_connection_err)
                    }

                    is Result.Loading -> {
                        context.defaultSizeTextDrawable(R.string.loading_image)
                    }

                    is Result.Value -> {
                        v.value
                    }

                    else -> Uri.EMPTY
                },
                contentDescription = null
            ) {
                it.diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .error(context.defaultSizeTextDrawable(R.string.tap_to_edit))
                    .fitCenter()
            }
        }
        BigButton(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(8.dp),
            text = stringResource(id = R.string.incidental_back_sheet_list),
            click = onClick
        )
    }

}

@Composable
fun IncidentalEdit(
    modifier: Modifier = Modifier,
    vm: IncidentalListVM,
    defaultShipper: ComposeData.Shipper?,
    defaultWorks: List<ComposeData.IncidentalWork>?,
    defaultTimeRangeList: List<TimeRange>,//todo merge unedited
    buttonClick: (
        shipper: ComposeData.Shipper,
        works: List<ComposeData.IncidentalWork>,
        addedTimeRangeList: List<TimeRange>,
        deletedTimeRangeList: List<TimeRange>,
        editedTimeRangeList: List<TimeRange>,
    ) -> Unit,
) {
    var selectedShipper by rememberScoped {
        mutableStateOf(defaultShipper)
    }
    var selectedWorks by rememberScoped {
        mutableStateOf(defaultWorks)
    }
    val timeRangeListGroup = rememberScoped {
        defaultTimeRangeList.distinctBy(TimeRange.key).groupBy { it.type }
    }
    var changeShipper by rememberScoped {
        mutableStateOf(false)
    }
    var changeWorks by rememberScoped {
        mutableStateOf(false)
    }
    val mutableLists = IncidentalType.values().map {
        it to timeRangeListGroup.mutableStateList(key = it.type)
    }
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = modifier
                .weight(1F, true)
                .verticalScroll(rememberScrollState())
        ) {
            Header(
                text = selectedShipper?.shipperNm,
                work = remember(selectedWorks) {
                    selectedWorks?.let(ComposeData.IncidentalWork::joinWorkNm)
                },
                buttonText = stringResource(id = R.string.change_shipper),
                buttonClick = { changeShipper = true }
            ) {
                HeadButton(
                    modifier = Modifier.align(Alignment.End),
                    horizontalPadding = 16,
                    verticalPadding = 6,
                    text = stringResource(id = R.string.select_work),
                    style = AppPropTodo.Text.defaultBold,
                    color = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    click = { changeWorks = true },
                )
            }
            mutableLists.forEach { (enum, list) ->
                TimeRanges(
                    enum.type,
                    when (enum) {
                        IncidentalType.Nimachi -> R.string.incidental_sheet_await
                        IncidentalType.Futai -> R.string.incidental_sheet_addition_work
                    },
                    list,
                    onChange = { c ->
                        val old = list.indexOfFirst { it.id == c.id }
                        val deletedAdd = c.isDeletedAdded()
                        if (old >= 0) {
                            if (deletedAdd) list.removeAt(old) else list[old] = c
                        } else if (!deletedAdd) {
                            list += c
                        }
                    },
                )
            }
        }
        BigButton(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(8.dp),
            text = stringResource(id = R.string.update_content),
            enabled = selectedShipper != null,
            click = {
                val shipper = selectedShipper
                if (shipper != null) {
                    val added = mutableListOf<TimeRange>()
                    val edited = mutableListOf<TimeRange>()
                    val deleted = mutableListOf<TimeRange>()

                    mutableLists.forEach { (_, l) ->
                        l.forEach {
                            val o = it.origState
                            val n = it.newState
                            if (o == EditEnum.Added) {
                                if (it.newState != EditEnum.Deleted) {
                                    added += it
                                }
                            } else {
                                if (n == EditEnum.Deleted) {
                                    deleted += it
                                } else if (n == EditEnum.Edited) {
                                    edited += it
                                }
                            }
                        }
                    }

                    buttonClick(
                        shipper,
                        selectedWorks ?: emptyList(),
                        added, deleted, edited
                    )
                }
            }
        )
    }
    if (changeShipper) {
        val shipperList by vm.shipperList.subscribeAsState(emptyList())
        ShipperChoose(
            currentText = selectedShipper?.shipperNm.orEmpty(),
            list = shipperList,
            select = {
                selectedShipper = it
            },
            onDismiss = {
                changeShipper = false
            }
        )
    }
    if (changeWorks) {
        val ls by vm.workList.subscribeAsState(emptyList())
        DefaultSheet(
            swipeDismiss = false,
            onDismiss = {
                changeWorks = false
            }
        ) { dismissRequest ->
            WorkSelection(
                list = ls,
                selected = selectedWorks,
            ) {
                selectedWorks = it
                dismissRequest()
            }
        }
    }
}

@Composable
private fun Header(
    text: String?,
    work: String?,
    buttonText: String,
    buttonClick: () -> Unit,
    content: (@Composable ColumnScope.() -> Unit)? = null,
) {
    Column(
        modifier = Modifier.padding(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier
                    .weight(1F)
                    .padding(horizontal = 8.dp),
                text = text.orEmpty(),
                style = AppPropTodo.Text.size18,
                color = MaterialTheme.colorScheme.primary,
            )
            HeadButton(
                modifier = Modifier,
                horizontalPadding = 16,
                verticalPadding = 6,
                text = buttonText,
                style = AppPropTodo.Text.defaultBold,
                color = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                click = buttonClick,
            )
        }
        Text(
            modifier = Modifier.padding(vertical = 8.dp),
            text = stringResource(id = R.string.incidental_sheet_addition_work_),
            style = AppPropTodo.Text.size16,
        )
        Text(
            modifier = Modifier.padding(8.dp),
            text = work.orEmpty(),
            style = AppPropTodo.Text.size16,
        )
        content?.invoke(this)
    }
}

@Composable
private fun ColumnScope.Placeholder() {
    Text(
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(8.dp),
        text = stringResource(id = R.string.unregistered_placeholder)
    )
}

@Composable
private fun ColumnScope.TimeRangesView(
    @StringRes headStrRes: Int,
    list: List<TimeRange>?,
) {
    PrimaryListHeadTitle(
        stringResource(id = headStrRes)
    )
    if (list.isNullOrEmpty()) {
        Placeholder()
    } else {
        val listBg = LocalAppColor.current.listItemBg
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            list.forEachIndexed { index, s ->
                TimeRange(
                    modifier = Modifier
                        .background(listBg),
                    index = index,
                    t = s,
                    onClick = null,
                )
            }
        }
    }
}

@Composable
private fun ColumnScope.TimeRanges(
    type: Int,
    @StringRes headStrRes: Int,
    list: SnapshotStateList<TimeRange>,
    onChange: (TimeRange) -> Unit,
) {
    var pickDate by rememberScoped {
        mutableStateOf<TimeRange?>(null)
    }
    PrimaryListHeadTitle(
        stringResource(id = headStrRes)
    )
    val notDeletedList = list.filterNot { it.newState == EditEnum.Deleted }
        .sortedWith(
            compareBy(
                { it.start },
                { it.end }
            )
        )
    if (notDeletedList.isEmpty()) {
        Placeholder()
    } else {
        val listBg = LocalAppColor.current.listItemBg

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            notDeletedList.forEachIndexed { index, s ->
                key(index, s) {
                    SwipeToDismiss(
                        onConfirm = {
                            onChange(s.deleted())
                        }
                    ) {
                        TimeRange(
                            modifier = Modifier
                                .background(listBg)
                                .background(
                                    if (s.origState == EditEnum.Added) {
                                        Color(0x1100FF00)
                                    } else if (s.newState == EditEnum.Edited) {
                                        Color(0x11FF0000)
                                    } else {
                                        Color.Transparent
                                    }
                                ),
                            index = index,
                            t = s,
                        ) {
                            pickDate = s
                        }
                    }
                }
            }
        }
    }
    IconAdd {
        val now = System.currentTimeMillis()
        pickDate = TimeRange(
            delegate = null,
            id = UUID.randomUUID().toString(),
            type = type,
            start = now,
            end = now,
            origState = EditEnum.Added,
            newState = null,
        )
    }
    pickDate?.let { p ->
        val dismiss = {
            pickDate = null
        }
        DatePickerDialog(
            modifier = Modifier.heightIn(max = 512.dp),
            defaultDate0 = p.start,
            defaultDate1 = p.end,
            onDismissRequest = dismiss,
            onSelected = { start, end ->
                onChange(p.editDate(start, end))
                dismiss()
            }
        )
    }
}

@Composable
private fun TimeRange(
    modifier: Modifier,
    index: Int,
    t: TimeRange,
    onClick: (() -> Unit)?,
) {
    val f = Config.dateFormatterMMddHHmm

    val s = remember(t) {
        f.format(t.start)
    }
    val e = remember(t) {
        f.format(t.end)
    }
    Row(
        modifier = modifier
            .run { if (onClick == null) this else clickable(onClick = onClick) }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val m = Modifier
            .weight(1F)
        Row(
            modifier = m,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "${index + 1}",
                modifier = Modifier
                    .weight(6F)
                    .padding(4.dp),
                style = AppPropTodo.Text.defaultBold,
                textAlign = TextAlign.Start,
            )
            Text(
                text = s,
                modifier = Modifier,
                style = AppPropTodo.Text.size16,
            )
            Spacer(
                modifier = Modifier
                    .weight(1F),
            )
        }
        Text(
            text = "〜",
            modifier = Modifier,
            style = AppPropTodo.Text.size16,
        )
        Row(
            modifier = m,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(
                modifier = Modifier
                    .weight(1F),
            )
            Text(
                text = e,
                modifier = Modifier,
                style = AppPropTodo.Text.size16,
            )
            Spacer(
                modifier = Modifier
                    .weight(6F),
            )
        }
    }
}


@Composable
private fun IconAdd(
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick
    ) {
        Icon(
            imageVector = Icons.Default.AddCircle,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun WorkSelection(
    list: List<ComposeData.IncidentalWork>,
    selected: List<ComposeData.IncidentalWork>?,
    onClick: (List<ComposeData.IncidentalWork>) -> Unit,
) {
    val buttonModifier = Modifier
        .padding(12.dp)
    val buttonTextModifier = Modifier
        .padding(vertical = 8.dp)
    val c1 = MaterialTheme.colorScheme.primary
    val c2 = MaterialTheme.colorScheme.onPrimary
    val buttonColor = ButtonDefaults.buttonColors(
        containerColor = c1,
        contentColor = c2,
        disabledContainerColor = AppPropTodo.Color.buttonDisabled,
        disabledContentColor = c2,
    )
    val buttonUnselectedColor = ButtonDefaults.buttonColors(
        containerColor = c2,
        contentColor = c1,
        disabledContainerColor = c2,
        disabledContentColor = AppPropTodo.Color.buttonDisabled,
    )
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = stringResource(id = R.string.incidental_sheet_works_selection),
            style = AppPropTodo.Text.size16,
        )

        val selectedArrList = rememberScoped(selected) {
            (selected?.map { it.workCd } ?: emptyList()).toMutableStateList()
        }

        LazyVerticalGrid(
            columns = GridCells.Adaptive(172.dp),
            modifier = Modifier.weight(1F, true),
        ) {
            items(
                list
            ) { w ->
                val match = selectedArrList.any { s -> s == w.workCd }
                Button(
                    modifier = buttonModifier,
                    colors = if (match) buttonColor else buttonUnselectedColor,
                    shape = AppPropTodo.Shape.workButtonShape,
                    onClick = {
                        if (match) {
                            selectedArrList.removeAll { s -> s == w.workCd }
                        } else {
                            selectedArrList.add(w.workCd)
                        }
                    },
                    border = if (match) null else BorderStroke(1.dp, c1)
                ) {
                    Text(
                        modifier = buttonTextModifier,
                        text = w.workNm,
                        fontSize = 18.sp,
                        lineHeight = 24.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        BigButton(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(8.dp),
            text = stringResource(id = R.string.confirm_content),
            click = {
                val l = selectedArrList.run {
                    list.filter { e ->
                        contains(e.workCd)
                    }
                }
                onClick(l)
            }
        )
    }
}

@Composable
private fun <K, V> Map<K, List<V>>.mutableStateList(key: K): SnapshotStateList<V> {
    return remember(this, key) {
        (get(key) ?: emptyList()).toMutableStateList()
    }
}

@Composable
private fun SwipeToDismiss(
    modifier: Modifier = Modifier,
    onConfirm: () -> Unit,
    dismissContent: @Composable RowScope.() -> Unit,
) {
    val d = LocalDensity.current.run { 128.dp.toPx() }
    SwipeToDismiss(
        modifier = modifier,
        state = rememberDismissState(
            confirmValueChange = {
                val dismiss = it != DismissValue.Default
                if (dismiss)
                    onConfirm()
                dismiss
            },
            positionalThreshold = { minOf(d, it / 2) }
        ),
        background = {
            Spacer(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Red.copy(alpha = 0.1F))
            )
        },
        dismissContent = dismissContent
    )
}
