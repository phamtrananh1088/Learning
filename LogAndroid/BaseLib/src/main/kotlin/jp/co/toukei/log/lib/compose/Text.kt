package jp.co.toukei.log.lib.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.sebaslogen.resaca.rememberScoped
import jp.co.toukei.log.lib.stripTrailingZerosString
import java.math.BigDecimal


@Composable
fun ToTextFieldValue(
    value: String,
    onValueChange: (String) -> Unit,
    content: @Composable (
        TextFieldValue,
        (TextFieldValue) -> Unit,
        MutableState<TextFieldValue>,
    ) -> Unit,
) {
    val tfState = remember {
        mutableStateOf(TextFieldValue(value))
    }
    val tf0 = tfState.value
    val tf = tf0.copy(text = value)
    SideEffect {
        if (
            tf.selection != tf0.selection ||
            tf.composition != tf0.composition
        ) {
            tfState.value = tf
        }
    }
    var lastTextValue by remember(value) {
        mutableStateOf(value)
    }

    content(tf, { v ->
        tfState.value = v
        val n = v.text
        val changed = lastTextValue != n
        lastTextValue = n
        if (changed) {
            onValueChange(n)
        }
    }, tfState)
}

@Composable
fun ToTextFieldDefaultZero(
    value: String,
    onValueChange: (String) -> Unit,
    defaultWithZero: Boolean = false,
    content: @Composable (
        TextFieldValue,
        (TextFieldValue) -> Unit,
        MutableState<TextFieldValue>,
    ) -> Unit,
) {
    ToTextFieldValue(
        value = value,
        onValueChange = onValueChange,
    ) { value1, onValueChange1, tfv ->
        val callback: (TextFieldValue) -> Unit = {
            val v2 = if (it.text.isEmpty()) {
                TextFieldValue(text = "0", selection = TextRange(1, 0))
            } else {
                it
            }
            onValueChange1(v2)
        }
        SideEffect {
            if (defaultWithZero && value1.text.isEmpty()) {
                callback(value1)
            }
        }
        content(value1, callback, tfv)
    }
}

@Composable
fun SearchInputBox(
    value: String,
    onValueChange: (String) -> Unit,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    placeholder: String? = null,
    icon: ImageVector = Icons.Default.Search,
    iconClick: () -> Unit = { onValueChange("") },
) {
    BasicTextField(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(vertical = 8.dp, horizontal = 12.dp)
            .background(MaterialTheme.colorScheme.onPrimary, RoundedCornerShape(4.dp)),
        value = value,
        onValueChange = onValueChange,
        textStyle = textStyle,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Text,
        ),
        singleLine = true,
        keyboardActions = LocalFocusManager.current.clearFocusOnActionDone(),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier.heightIn(min = 42.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                val empty = value.isEmpty()
                Box(
                    modifier = Modifier
                        .weight(1F, true)
                        .padding(8.dp),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    if (placeholder != null && empty) {
                        Row(
                            modifier = Modifier.alpha(0.4F),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null
                            )
                            Text(
                                text = placeholder,
                                style = textStyle,
                            )
                        }
                    }
                    innerTextField()
                }
                if (!empty) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = null,
                        modifier = Modifier
                            .alpha(0.8F)
                            .padding(horizontal = 4.dp)
                            .clip(CircleShape)
                            .clickable(onClick = iconClick)
                            .padding(4.dp),
                    )
                }
            }
        }
    )
}


@Composable
fun BasicSearchBox(
    modifier: Modifier = Modifier,
    value: String,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    placeholder: String? = null,
    onValueChange: (String?) -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val contentColor = LocalContentColor.current
        val alphaColor = contentColor.copy(alpha = 0.8F)
        CompositionLocalProvider(
            LocalTextSelectionColors provides TextSelectionColors(
                handleColor = MaterialTheme.colorScheme.primaryContainer,
                backgroundColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            val focusRequester = remember { FocusRequester() }
            BasicTextField(
                modifier = Modifier
                    .weight(1F, true)
                    .drawWithContent {
                        val h = 1.dp.toPx()
                        drawRect(
                            color = alphaColor,
                            topLeft = Offset(0F, size.height - h),
                            size = Size(size.width, h)
                        )
                        drawContent()
                    }
                    .padding(4.dp)
                    .focusRequester(focusRequester),
                value = value,
                onValueChange = onValueChange,
                textStyle = textStyle.copy(
                    color = contentColor
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Text,
                ),
                keyboardActions = LocalFocusManager.current.clearFocusOnActionDone(),
                singleLine = true,
                cursorBrush = SolidColor(contentColor),
                decorationBox = { innerTextField ->
                    Box {
                        if (placeholder != null && value.isEmpty()) {
                            Text(
                                text = placeholder,
                                style = textStyle.copy(
                                    color = alphaColor
                                ),
                                modifier = Modifier
                                    .alpha(0.4F)
                                    .align(Alignment.CenterStart)
                            )
                        }
                        innerTextField()
                    }
                }
            )
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
        }
        IconButton(
            onClick = {
                onValueChange(if (value.isEmpty()) null else "")
            }
        ) {
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = null,
                tint = if (value.isEmpty()) contentColor else alphaColor
            )
        }
    }
}


@Composable
fun InputTextField(
    modifier: Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    isFocused: (MutableState<Boolean>)? = null,
    imeAction: ImeAction = ImeAction.Done,
    keyboardType: KeyboardType = KeyboardType.Text,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    keyboardActions: KeyboardActions = LocalFocusManager.current.clearFocusOnActionDone(),
    placeholder: String? = null,
    decorationBoxModifier: Modifier = Modifier
        .background(MaterialTheme.colorScheme.onPrimary)
        .border(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline,
            shape = RoundedCornerShape(1.dp)
        )
        .padding(12.dp)
) {
    ToTextFieldValue(value = value, onValueChange = onValueChange) { p3, p4, p5 ->
        BasicTextField(
            value = p3,
            onValueChange = p4,
            modifier = modifier
                .onFocusSelectAll(p5)
                .run { isFocused?.let(::onFocusChanged) ?: this },
            textStyle = textStyle,
            keyboardOptions = KeyboardOptions(
                imeAction = imeAction,
                keyboardType = keyboardType,
            ),
            keyboardActions = keyboardActions,
            decorationBox = { innerTextField ->
                Box(
                    modifier = decorationBoxModifier
                ) {
                    if (placeholder != null && p3.text.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = textStyle,
                            modifier = Modifier
                                .alpha(0.4F)
                                .align(Alignment.CenterStart)
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}

@Composable
fun BigDecimalContent(
    number: BigDecimal?,
    trimToRealValue: Boolean = false,
    onValueChange: (String, BigDecimal?) -> Unit,
    inputValidation: (String, BigDecimal) -> Boolean,
    content: @Composable (
        String,
        (String) -> Unit,
    ) -> Unit,
) {
    val trimmedRealValue = number?.stripTrailingZerosString()

    var input by remember {
        mutableStateOf(trimmedRealValue.orEmpty())
    }

    SideEffect {
        if (trimToRealValue ||
            trimmedRealValue != input.toBigDecimalOrNull()?.stripTrailingZerosString()
        ) {
            input = trimmedRealValue.orEmpty()
        }
    }
    content(input) { value ->
        if (value == "-") {
            //negative supported ?
            input = value
            onValueChange(value, null)
        } else if (value.isEmpty()) {
            input = value
            onValueChange(value, null)
        } else {
            val n = value.toBigDecimalOrNull()
            if (n != null && inputValidation(value, n)) {
                input = value
                onValueChange(value, n)
            }
        }
    }
}

@Composable
fun NumberInputBox(
    modifier: Modifier,
    value: String,
    imeAction: ImeAction = ImeAction.Done,
    keyboardType: KeyboardType = KeyboardType.Decimal,
    inputValidation: (String, BigDecimal) -> Boolean,
    onValueChange: (String, BigDecimal?) -> Unit,
) {
    val isFocused = remember(value) {
        mutableStateOf(true)
    }
    BigDecimalContent(
        number = value.toBigDecimalOrNull(),
        trimToRealValue = !isFocused.value,
        inputValidation = inputValidation,
        onValueChange = onValueChange
    ) { p1, p2 ->
        InputTextField(
            modifier = modifier,
            value = p1,
            onValueChange = p2,
            isFocused = isFocused,
            imeAction = imeAction,
            keyboardType = keyboardType
        )
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
    confirmButtonText: String,
    dismissButtonText: String,
    onConfirm: (String) -> Unit,
) {
    var t by rememberScoped {
        mutableStateOf(defaultText)
    }
    DialogHeightFix(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                enabled = enabled,
                onClick = {
                    onConfirm(t)
                }
            ) {
                Text(text = confirmButtonText)
            }
        },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = dismissButtonText)
            }
        },
        title = title,
        content = {
            val focusRequester = remember { FocusRequester() }
            NumberInputBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                value = t,
                imeAction = imeAction,
                keyboardType = keyboardType,
                inputValidation = inputValidation,
                onValueChange = { it, _ -> t = it }
            )
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
        },
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
    confirmButtonText: String,
    dismissButtonText: String,
    onConfirm: (String) -> Unit,
) {
    var t by rememberScoped {
        mutableStateOf(defaultText)
    }
    DialogHeightFix(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = {
                onConfirm(t)
            }) {
                Text(text = confirmButtonText)
            }
        },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = dismissButtonText)
            }
        },
        title = title,
        content = {
            val focusRequester = remember { FocusRequester() }
            InputTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                value = t,
                imeAction = imeAction,
                keyboardType = keyboardType,
                onValueChange = {
                    if (inputValidation(it)) {
                        t = it
                    }
                },
            )
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
        },
    )
}

@Composable
fun NumberInput(
    modifier: Modifier,
    value: String,
    textStyle: TextStyle,
    onValueChange: (String) -> Unit,
    delayMillis: Long,
    stepMillis: Long,
    onPlus: (Int) -> Unit,
) {
    Row(
        modifier = modifier
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val focusManager = LocalFocusManager.current
        Icon(
            imageVector = Icons.Default.Remove,
            contentDescription = null,
            modifier = Modifier
                .longPressRepeat(delayMillis, stepMillis) {
                    if (it == 0) focusManager.clearFocus()
                    onPlus(-1)
                }
                .padding(8.dp)
        )
        ToTextFieldDefaultZero(value, onValueChange) { v1, vc1, tfv ->
            BasicTextField(
                value = v1,
                onValueChange = vc1,
                modifier = Modifier
                    .weight(1F)
                    .fillMaxHeight()
                    .wrapContentHeight()
                    .onFocusSelectAll(tfv),
                textStyle = textStyle,
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
            )
        }
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            modifier = Modifier
                .longPressRepeat(delayMillis, stepMillis) {
                    if (it == 0) focusManager.clearFocus()
                    onPlus(1)
                }
                .padding(8.dp)
        )
    }
}

@Composable
fun NumberInput(
    modifier: Modifier,
    numberValue: String,
    inputValidation: (String, BigDecimal) -> Boolean,
    minValue: String?,
    maxValue: String?,
    textStyle: TextStyle,
    onValueChange: (BigDecimal) -> Unit,
) {
    val max = remember(maxValue) { maxValue?.toBigDecimalOrNull() }
    val min = remember(minValue) { minValue?.toBigDecimalOrNull() }

    val number by remember(numberValue) {
        mutableStateOf(numberValue.toBigDecimalOrNull())
    }
    val isFocused = remember(numberValue) {
        mutableStateOf(true)
    }
    val onChange: (String, BigDecimal?) -> Unit = { _, b ->
        onValueChange(
            (b ?: BigDecimal.ZERO)
                .run {
                    min?.let { coerceAtLeast(it) } ?: this
                }
                .run {
                    max?.let { coerceAtMost(it) } ?: this
                }
        )
    }
    BigDecimalContent(
        number = number,
        trimToRealValue = !isFocused.value,
        onValueChange = onChange,
        inputValidation = inputValidation
    ) { value1, onValueChange1 ->
        NumberInput(
            modifier = modifier.onFocusChanged(isFocused),
            value = value1,
            textStyle = textStyle,
            onValueChange = onValueChange1,
            delayMillis = 400,
            stepMillis = 50,
            onPlus = {
                val b = number?.plus(BigDecimal(it))
                onChange(b?.toPlainString().orEmpty(), b)
            }
        )
    }
}

