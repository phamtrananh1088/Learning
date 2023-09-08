@file:OptIn(
    ExperimentalLayoutApi::class, ExperimentalLayoutApi::class
)

package jp.co.toukei.log.common.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import jp.co.toukei.log.lib.compose.DefaultProgressDialog
import jp.co.toukei.log.lib.compose.InputBoxDialog
import jp.co.toukei.log.lib.compose.NumberInputBoxDialog
import jp.co.toukei.log.trustar.R
import java.math.BigDecimal


@Composable
fun VerticalDivider(
    modifier: Modifier = Modifier,
    color: Color = LocalAppColor.current.divider,
) {
    Divider(
        modifier = modifier.fillMaxWidth(),
        thickness = 1.dp,
        color = color,
    )
}

@Composable
fun ProcessingDialog() {
    DefaultProgressDialog(
        content = stringResource(id = R.string.processing)
    )
}

@Composable
fun CircularProgress(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    padding: Dp = 32.dp,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(padding),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(32.dp),
            color = color,
        )
    }
}

@Composable
fun EmptyPlaceHolder() {
    TextPlaceHolder(stringResource(id = R.string.no_data_placeholder))
}

@Composable
fun TextPlaceHolder(
    text: String,
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        textAlign = TextAlign.Center,
        text = text
    )
}

@Composable
fun PrimarySurface(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.primary,
        content = content,
    )
}

@Composable
fun BigButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: String,
    click: () -> Unit,
) {
    val c = MaterialTheme.colorScheme
    Button(
        enabled = enabled,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = c.primary,
            contentColor = c.onPrimary,
            disabledContainerColor = c.primary.copy(alpha = 0.4F),
            disabledContentColor = c.onPrimary,
        ),
        onClick = click
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun HeadButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    horizontalPadding: Int,
    verticalPadding: Int,
    text: String,
    style: TextStyle,
    color: Color,
    contentColor: Color,
    contentStart: (@Composable RowScope.() -> Unit)? = null,
    contentEnd: (@Composable RowScope.() -> Unit)? = null,
    click: () -> Unit,
) {
    Button(
        enabled = enabled,
        modifier = modifier
            .widthIn(min = 1.dp)
            .heightIn(min = 1.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = contentColor,
            disabledContainerColor = color.copy(alpha = 0.4F),
            disabledContentColor = contentColor,
        ),
        contentPadding = PaddingValues(
            horizontal = horizontalPadding.dp,
            vertical = verticalPadding.dp,
        ),
        onClick = click
    ) {
        contentStart?.invoke(this)
        Text(
            text = text,
            style = style,
        )
        contentEnd?.invoke(this)
    }
}

@Composable
fun NumberInputBoxDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    defaultText: String = "",
    imeAction: ImeAction = ImeAction.Done,
    keyboardType: KeyboardType = KeyboardType.Decimal,
    inputValidation: (String, BigDecimal) -> Boolean,
    title: String? = null,
    enabled: Boolean = true,
    onConfirm: (String) -> Unit,
) {
    NumberInputBoxDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        defaultText = defaultText,
        imeAction = imeAction,
        keyboardType = keyboardType,
        inputValidation = inputValidation,
        title = title,
        enabled = enabled,
        confirmButtonText = stringResource(id = R.string.confirm),
        dismissButtonText = stringResource(id = R.string.cancel),
        onConfirm = onConfirm,
    )
}

@Composable
fun InputBoxDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    defaultText: String = "",
    imeAction: ImeAction = ImeAction.Done,
    keyboardType: KeyboardType = KeyboardType.Text,
    inputValidation: (String) -> Boolean,
    title: String? = null,
    onConfirm: (String) -> Unit,
) {
    InputBoxDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        defaultText = defaultText,
        imeAction = imeAction,
        keyboardType = keyboardType,
        inputValidation = inputValidation,
        title = title,
        confirmButtonText = stringResource(id = R.string.confirm),
        dismissButtonText = stringResource(id = R.string.cancel),
        onConfirm = onConfirm
    )
}

@Composable
fun End2Button(
    modifier: Modifier,
    button1Text: String,
    button2Text: String,
    button1Click: () -> Unit,
    button2Click: () -> Unit,
) {
    FlowRow(
        modifier = modifier.padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(
            8.dp,
            alignment = Alignment.End,
        ),
        verticalArrangement = Arrangement.Center,
    ) {
        Button(
            onClick = button1Click,
            shape = RoundedCornerShape(8.dp),
        ) {
            Text(text = button1Text)
        }
        Button(
            onClick = button2Click,
            shape = RoundedCornerShape(8.dp),
        ) {
            Text(text = button2Text)
        }
    }
}

@Deprecated("")
@Composable
fun <T> NonLazyGrid(
    modifier: Modifier,
    list: List<T>,
    columns: Int,
    content: @Composable (T) -> Unit,
) {
    val itemCount = list.size
    val rm = Modifier.fillMaxWidth()
    Column(modifier = modifier) {
        repeat(itemCount + columns - 1 / columns) { r ->
            val f = r * columns
            Row(modifier = rm) {
                val m = Modifier.weight(1F)
                repeat(columns) { c ->
                    Box(modifier = m) {
                        list.getOrNull(f + c)?.let { content(it) }
                    }
                }
            }
        }
    }
}

fun LazyGridScope.fullItem(
    key: Any? = null,
    contentType: Any? = null,
    content: @Composable LazyGridItemScope.() -> Unit,
) {
    item(
        key = key,
        span = { GridItemSpan(maxLineSpan) },
        contentType = contentType,
        content = content
    )
}
