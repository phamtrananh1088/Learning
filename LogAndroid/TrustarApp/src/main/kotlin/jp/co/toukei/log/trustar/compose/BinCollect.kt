package jp.co.toukei.log.trustar.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sebaslogen.resaca.rememberScoped
import jp.co.toukei.log.common.compose.AppPropTodo
import jp.co.toukei.log.common.compose.End2Button
import jp.co.toukei.log.common.compose.InputBoxDialog
import jp.co.toukei.log.common.compose.LocalAppColor
import jp.co.toukei.log.common.compose.VerticalDivider
import jp.co.toukei.log.lib.compose.NumberInput
import jp.co.toukei.log.lib.compose.topShadow
import jp.co.toukei.log.lib.stripTrailingZerosString
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.db.user.entity.CollectionGroup
import jp.co.toukei.log.trustar.viewmodel.BinCollectVM
import java.math.BigDecimal

@Composable
fun CollectMain(
    modifier: Modifier,
    place: String,
    vm: BinCollectVM,
    onDismiss: () -> Unit,
) {
    val c by vm.displayListLiveData.observeAsState(initial = null)
    Column(
        modifier = modifier
    ) {
        Text(
            text = place,
            modifier = Modifier
                .padding(8.dp, 12.dp),
            fontSize = AppPropTodo.Font.collectDefault,
        )
        CollectList(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F, true),
            groupList = c,
            addRow = { name, g -> vm.addRow(name, g) },
            editName = { name, r -> vm.rowEdit(r, r.actualQuantity, name) },
            editQuantity = { q, r -> vm.rowEdit(r, q, r.name) }
        )
        End2Button(
            modifier = Modifier
                .fillMaxWidth()
                .topShadow(),
            button1Text = stringResource(id = R.string.cancel),
            button2Text = stringResource(id = R.string.update),
            button1Click = onDismiss
        ) {
            vm.save()
            onDismiss()
        }
    }
}

@Composable
fun CollectList(
    modifier: Modifier,
    groupList: List<BinCollectVM.Group>?,
    addRow: (String, CollectionGroup) -> Unit,
    editName: (String, BinCollectVM.RowEdit) -> Unit,
    editQuantity: (Double, BinCollectVM.RowEdit) -> Unit,
) {
    val sortedGroup = remember(groupList) {
        groupList?.sortedBy { it.genId }
    }

    var add by rememberScoped {
        mutableStateOf<CollectionGroup?>(null)
    }
    var edit by rememberScoped {
        mutableStateOf<BinCollectVM.RowEdit?>(null)
    }


    LazyColumn(
        modifier = modifier
    ) {
        sortedGroup?.forEach { g ->
            item(g.genId) {
                val grp = g.group
                CollectGroupTitle(grp.collectionClassName.orEmpty()) {
                    add = grp
                }
            }
            g.sortedList.forEach { r ->
                item(r.genId) {
                    CollectRow(
                        collectNm = r.name,
                        expectedQuantity = r.expectedQuantityStr,
                        actualQuantity = r.actualQuantityStr,
                        rowEdit = if (r.row.emptyCd()) { -> edit = r } else null,
                        onActualQuantityChange = {
                            editQuantity(it.toDouble(), r)
                        },
                    )
                }
            }
        }
    }

    add?.let { g ->
        val dismiss = { add = null }
        CollectEdit(
            title = g.collectionClassName.orEmpty(),
            onDismiss = dismiss
        ) {
            if (it.isNotBlank()) {
                addRow(it, g)
                dismiss()
            }
        }
    }

    edit?.let { r ->
        val dismiss = { edit = null }
        CollectEdit(
            defaultText = r.name,
            onDismiss = dismiss
        ) {
            if (it.isNotBlank()) {
                editName(it, r)
                dismiss()
            }
        }
    }
}

@Composable
fun CollectRow(
    collectNm: String,
    expectedQuantity: String,
    actualQuantity: String,
    rowEdit: (() -> Unit)? = null,
    onActualQuantityChange: (BigDecimal) -> Unit,
) {

    val color = LocalAppColor.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .weight(2F)
                .fillMaxHeight()
                .run { if (rowEdit == null) this else clickable(onClick = rowEdit) },
            contentAlignment = Alignment.CenterStart,
        ) {
            Text(
                text = collectNm,
                fontSize = AppPropTodo.Font.collectDefault,
            )
        }
        Box(
            modifier = Modifier
                .weight(2F)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = remember(expectedQuantity) {
                    BigDecimal(expectedQuantity).stripTrailingZerosString()
                },
                modifier = Modifier
                    .fillMaxHeight()
                    .widthIn(min = 44.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(color.numInputBg)
                    .wrapContentHeight(),
                fontSize = AppPropTodo.Font.collectDefaultHeader,
                textAlign = TextAlign.Center,
            )
        }

        val regex = remember { "\\d{1,5}(\\.\\d?)?".toRegex() }

        NumberInput(
            modifier = Modifier
                .weight(3F)
                .clip(RoundedCornerShape(4.dp))
                .background(color.numInputBg),
            numberValue = actualQuantity,
            inputValidation = { p, _ ->
                regex.matches(p)
            },
            minValue = "0.0",
            maxValue = "99999.9",
            textStyle = LocalTextStyle.current.copy(
                color = LocalAppColor.current.textColor,
                textAlign = TextAlign.Center,
                fontSize = AppPropTodo.Font.collectDefaultHeader
            ),
            onValueChange = onActualQuantityChange
        )
    }
}

@Composable
fun CollectGroupTitle(
    text: String,
    onClick: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        VerticalDivider()
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                modifier = Modifier.weight(1F),
                fontSize = AppPropTodo.Font.collectDefault,
            )
            IconButton(onClick = onClick) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                )
            }
        }
        RowHead()
    }
}

@Composable
fun RowHead() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(modifier = Modifier.weight(2F))
        Text(
            text = stringResource(id = R.string.expected_quantity),
            modifier = Modifier.weight(2F),
            fontSize = AppPropTodo.Font.collectDefaultHeader,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Text(
            text = stringResource(id = R.string.actual_quantity),
            modifier = Modifier.weight(3F),
            fontSize = AppPropTodo.Font.collectDefaultHeader,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun CollectEdit(
    title: String = stringResource(id = R.string.collections),
    defaultText: String = "",
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
) {
    InputBoxDialog(
        title = title,
        defaultText = defaultText,
        onDismissRequest = onDismiss,
        inputValidation = { it.length < 21 },
        onConfirm = onConfirm
    )
}
