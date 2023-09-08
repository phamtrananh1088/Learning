package jp.co.toukei.log.trustar.compose.sheet

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sebaslogen.resaca.rememberScoped
import jp.co.toukei.log.common.compose.AppPropTodo
import jp.co.toukei.log.common.compose.HeadButton
import jp.co.toukei.log.lib.ctx.Ctx
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.compose.ComposeData
import jp.co.toukei.log.trustar.compose.TwoButton2
import jp.co.toukei.log.trustar.other.WorkMode


@Composable
fun StartBinTruckChoose(
    list: List<ComposeData.TruckKun>,
    defaultTruck: ComposeData.TruckKun?,
    button1Text: String,
    button2Text: String,
    button2EndIcon: ImageVector?,
    buttonClick: (ComposeData.TruckKun) -> Unit,
) {
    val modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()
    val c1 = MaterialTheme.colorScheme.primary
    val c2 = MaterialTheme.colorScheme.onPrimary
    Column(
        modifier = modifier,
    ) {
        var truckKun by rememberScoped {
            mutableStateOf(defaultTruck)
        }
        var changeTruck by rememberScoped {
            mutableStateOf(false)
        }
        Text(
            modifier = modifier,
            text = stringResource(R.string.current_vehicle)
        )
        Text(
            modifier = modifier.padding(start = 32.dp),
            text = truckKun?.truckNm.orEmpty()
        )
        Text(
            modifier = modifier.padding(vertical = 24.dp),
            text = stringResource(R.string.operation_start_by_this_vehicle)
        )
        TwoButton2(
            modifier = modifier,
            color = c1,
            contentColor = c2,
            button1Text = button1Text,
            button2Text = button2Text,
            button1Click = { changeTruck = true },
            button2Click = { truckKun?.let(buttonClick) },
            button2EndIcon = button2EndIcon,
            button2Enabled = truckKun != null,
        )
        if (changeTruck) {
            VehicleChoose(
                currentText = truckKun?.truckNm.orEmpty(),
                list = list,
                select = {
                    truckKun = it
                },
                onDismiss = { changeTruck = false }
            )
        }
    }
}

@Composable
fun WorkModeChoose(
    showWarning: Boolean = false,
    onClick: (WorkMode) -> Unit,
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(vertical = 32.dp)
            .widthIn(max = 512.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
        Text(
            modifier = modifier,
            textAlign = TextAlign.Center,
            text = stringResource(id = R.string.work_mode_select_title),
        )
        if (showWarning) {
            Text(
                modifier = modifier,
                textAlign = TextAlign.Center,
                color = Color.Red,
                text = stringResource(id = R.string.work_mode_select_msg),
            )
        }

        var workMode by rememberScoped {
            mutableStateOf<WorkMode?>(null)
        }

        SelectButtonRow(
            modifier = Modifier.padding(top = 32.dp),
            selected = workMode,
            list = listOf(
                WorkMode.Normal,
                WorkMode.Automatic,
            ),
            text = { Ctx.context.getString(it.string) },
        ) {
            workMode = it
        }

        HeadButton(
            modifier = Modifier.padding(top = 32.dp),
            enabled = workMode != null,
            horizontalPadding = 28,
            verticalPadding = 12,
            text = stringResource(id = R.string.operation_start),
            style = AppPropTodo.Text.defaultBold,
            color = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            click = {
                workMode?.let(onClick)
            },
            contentEnd = {
                Icon(
                    modifier = Modifier
                        .padding(end = 6.dp)
                        .size(24.dp),
                    imageVector = Icons.Default.ArrowDownward,
                    contentDescription = null,
                )
            }
        )
    }
}

@Composable
fun <T> SelectButtonRow(
    modifier: Modifier = Modifier,
    selected: T?,
    list: List<T>,
    text: (T) -> String,
    unselect: (() -> Unit)? = null,
    select: (T) -> Unit,
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
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        list.forEach {
            val match = selected == it
            Button(
                modifier = buttonModifier,
                colors = if (match) buttonColor else buttonUnselectedColor,
                shape = AppPropTodo.Shape.workButtonShape,
                onClick = {
                    if (match) {
                        unselect?.invoke()
                    } else {
                        select(it)
                    }
                },
                border = if (match) null else BorderStroke(1.dp, c1)
            ) {
                Text(
                    modifier = buttonTextModifier,
                    text = text(it),
                    fontSize = 18.sp,
                    lineHeight = 24.sp,
                )
            }
        }
    }
}
