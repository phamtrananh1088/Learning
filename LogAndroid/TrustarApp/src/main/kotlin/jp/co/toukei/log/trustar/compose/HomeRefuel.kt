@file:OptIn(ExperimentalMaterial3Api::class)

package jp.co.toukei.log.trustar.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.sebaslogen.resaca.rememberScoped
import jp.co.toukei.log.common.compose.BigButton
import jp.co.toukei.log.lib.compose.NumberInputBox
import jp.co.toukei.log.lib.compose.replaceMessage
import jp.co.toukei.log.lib.ctx.Ctx
import jp.co.toukei.log.lib.orElseNull
import jp.co.toukei.log.lib.util.DecimalInputFilter
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.compose.sheet.FuelBinListSheet
import jp.co.toukei.log.trustar.compose.sheet.FuelSheet
import jp.co.toukei.log.trustar.db.user.entity.Fuel
import jp.co.toukei.log.trustar.viewmodel.HomeVM
import java.util.Optional


@Composable
fun HomeRefuel(
    vm: HomeVM,
    paddingValues: PaddingValues,
    snbHost: SnackbarHostState,
) {
    var showBinHeaderSheet by rememberScoped {
        mutableStateOf(false)
    }
    var showFuelSheet by rememberScoped {
        mutableStateOf(false)
    }

    var binHeaderRow: ComposeData.BinHeaderRow? by rememberScoped {
        mutableStateOf(null)
    }
    var fuel: Fuel? by rememberScoped {
        mutableStateOf(null)
    }

    var refuelAmount by remember {
        mutableStateOf(Optional.empty<Float>())
    }

    val selectB = binHeaderRow?.allocationNo
    val selectF = fuel?.fuelCd
    DisposableEffect(selectB, selectF) {
        val disposable = if (selectB == null || selectF == null) null else {
            vm.refuelAmountOfBin(selectB, selectF)
                .subscribe {
                    refuelAmount = it
                }
        }
        onDispose {
            disposable?.dispose()
        }
    }
    val scope = rememberCoroutineScope()

    HomeRefuel(
        modifier = Modifier.fillMaxSize(),
        paddingValues = paddingValues,
        binHeaderRow = binHeaderRow,
        allocationClick = {
            showBinHeaderSheet = true
        },
        fuel = fuel,
        fuelClick = {
            showFuelSheet = true
        },
        refuelAmount = refuelAmount.orElseNull() ?: 0F,
        click = { a, f, t, q, c ->
            if (q > 0) {
                vm.refuel(
                    allocationNo = a,
                    fuelCd = f,
                    truckCd = t,
                    quantity = q,
                    cost = c
                )
                snbHost.replaceMessage(scope, R.string.refuel_success_msg)
            }
        }
    )

    if (showBinHeaderSheet) {
        FuelBinListSheet(
            vm = vm,
            selectBinHeader = {
                binHeaderRow = it
            }
        ) {
            showBinHeaderSheet = false
        }
    }
    if (showFuelSheet) {
        FuelSheet(
            vm = vm,
            selectFuel = {
                fuel = it
            }
        ) {
            showFuelSheet = false
        }
    }
}

@Composable
fun HomeRefuel(
    modifier: Modifier,
    paddingValues: PaddingValues, ///todo ????
    binHeaderRow: ComposeData.BinHeaderRow?,
    allocationClick: () -> Unit,
    fuel: Fuel?,//todo
    fuelClick: () -> Unit,
    refuelAmount: Float,
    click: (allocationNo: String, fuelCd: String, truckCd: String, q: Float, c: Float) -> Unit,
) {
    val regex70 = remember {
        DecimalInputFilter(7, 0).regex
    }
    val regex41 = remember {
        DecimalInputFilter(4, 1).regex
    }

    val allocationNo = binHeaderRow?.allocationNo
    val fuelCd = fuel?.fuelCd
    val truckCd = binHeaderRow?.truckCd

    Box(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = 16.dp),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .widthIn(max = 512.dp)
                .padding(16.dp),
        ) {
            val unselectedStr = remember {
                Ctx.context.getString(R.string.unselected)
            }
            val maxModifier = Modifier
                .fillMaxWidth()
            Selector(
                modifier = maxModifier,
                text = binHeaderRow?.allocationNm ?: unselectedStr,
                onClick = allocationClick,
            )
            Text(
                modifier = Modifier
                    .padding(vertical = 12.dp),
                text = binHeaderRow?.truckNm.orEmpty(),
                style = MaterialTheme.typography.bodyLarge,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier
                        .weight(1F, fill = false),
                    text = stringResource(id = R.string.refuel_amount_of_the_operation),
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    modifier = Modifier
                        .padding(12.dp),
                    text = stringResource(id = R.string.f_liter, refuelAmount),
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            Selector(
                modifier = maxModifier,
                text = fuel?.fuelNm ?: unselectedStr,
                onClick = fuelClick,
            )
            Spacer(modifier = Modifier.size(32.dp))

            var inputQuantity by rememberScoped {
                mutableStateOf("")
            }
            var inputCost by rememberScoped {
                mutableStateOf("")
            }

            val inputQuantityFloat = inputQuantity.toFloatOrNull()
            val inputCostFloat = inputCost.toFloatOrNull()

            Text(
                modifier = Modifier
                    .padding(vertical = 12.dp),
                text = stringResource(id = R.string.refuel_amount_in_liter),
                style = MaterialTheme.typography.bodyLarge,
            )
            NumberInputBox(
                modifier = maxModifier,
                value = inputQuantity,
                inputValidation = { s, _ -> regex41.matches(s) },
                imeAction = ImeAction.Next,
            ) { s, _ ->
                inputQuantity = s
            }
            Text(
                modifier = Modifier
                    .padding(vertical = 12.dp),
                text = stringResource(id = R.string.amount_include_tax),
                style = MaterialTheme.typography.bodyLarge,
            )
            NumberInputBox(
                modifier = maxModifier,
                value = inputCost,
                keyboardType = KeyboardType.Number,
                inputValidation = { s, _ -> regex70.matches(s) },
            ) { s, _ ->
                inputCost = s
            }
            val focusManager = LocalFocusManager.current

            val clk: (() -> Unit)? = if (
                inputQuantityFloat != null &&
                inputCostFloat != null &&
                allocationNo != null &&
                truckCd != null &&
                fuelCd != null
            ) {
                {
                    focusManager.clearFocus()
                    click(
                        allocationNo,
                        fuelCd,
                        truckCd,
                        inputQuantityFloat,
                        inputCostFloat
                    )
                    inputQuantity = ""
                    inputCost = ""
                }
            } else null

            BigButton(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 32.dp),
                enabled = clk != null,
                text = stringResource(id = R.string.confirm_input),
                click = clk ?: {}
            )
        }
    }
}


@Composable
private fun Selector(
    modifier: Modifier,
    text: String,
    onClick: () -> Unit,
) {
    val color = MaterialTheme.colorScheme.primary
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .clickable(onClick = onClick)
            .background(MaterialTheme.colorScheme.onPrimary)
            .border(
                border = BorderStroke(2.dp, color),
                shape = RoundedCornerShape(50),
            )
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier
                .weight(1F),
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = color,
        )
        Icon(
            imageVector = Icons.Default.ExpandMore,
            contentDescription = null,
            tint = color
        )
    }
}
