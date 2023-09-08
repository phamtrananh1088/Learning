@file:OptIn(ExperimentalMaterial3Api::class)

package jp.co.toukei.log.lib.compose

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

@Composable
fun DefaultConfirmDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    title: Int? = null,
    content: Int?,
    confirmButtonText: Int = android.R.string.ok,
    dismissButtonText: Int? = null,
    dismissButtonClick: () -> Unit = onDismissRequest,
    confirmButtonClick: () -> Unit,
) {
    DefaultConfirmDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        title = title?.let { stringResource(id = it) },
        content = content?.let { stringResource(id = it) },
        confirmButtonText = stringResource(id = confirmButtonText),
        dismissButtonText = dismissButtonText?.let { stringResource(id = it) },
        dismissButtonClick = dismissButtonClick,
        confirmButtonClick = confirmButtonClick
    )
}

@Composable
fun DefaultConfirmDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    title: String? = null,
    content: String?,
    confirmButtonText: String = stringResource(id = android.R.string.ok),
    dismissButtonText: String? = null,
    dismissButtonClick: () -> Unit = onDismissRequest,
    confirmButtonClick: () -> Unit,
) {
    DefaultDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = confirmButtonClick) {
                Text(text = confirmButtonText)
            }
        },
        dismissButton = dismissButtonText?.let { text ->
            {
                TextButton(onClick = dismissButtonClick) {
                    Text(text = text)
                }
            }
        },
        modifier = modifier,
        title = title,
        content = content,
    )
}

@Composable
fun DefaultDialog(
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    dismissButton: @Composable (() -> Unit)? = null,
    title: String? = null,
    content: String?,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = confirmButton,
        modifier = modifier,
        dismissButton = dismissButton,
        title = title?.let {
            {
                Text(it)
            }
        },
        text = content?.let {
            {
                Text(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    text = it,
                )
            }
        },
        shape = MaterialTheme.shapes.medium,
    )
}

@Composable
fun DefaultProgressDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = { },
    title: String? = null,
    content: String,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        properties = DialogProperties(),
    ) {
        Surface(
            modifier = modifier,
            shape = MaterialTheme.shapes.medium,
            color = AlertDialogDefaults.containerColor,
            tonalElevation = AlertDialogDefaults.TonalElevation,
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                title?.let {
                    CompositionLocalProvider(
                        LocalContentColor provides MaterialTheme.colorScheme.onSurface
                    ) {
                        val textStyle = MaterialTheme.typography.headlineSmall
                        ProvideTextStyle(textStyle) {
                            Box(
                                Modifier
                                    .padding(bottom = 16.dp)
                                    .align(Alignment.Start)
                            ) {
                                Text(it)
                            }
                        }
                    }
                }
                CompositionLocalProvider(
                    LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant
                ) {
                    val textStyle = MaterialTheme.typography.bodyMedium
                    ProvideTextStyle(textStyle) {
                        Row(
                            Modifier
                                .weight(weight = 1f, fill = false)
                                .align(Alignment.Start),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(32.dp),
                            )

                            Text(
                                modifier = Modifier.padding(start = 24.dp),
                                text = content,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DefaultCheckboxDialog(
    checkState: MutableState<Boolean>,
    checkBoxText: String,
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    content: String,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        properties = DialogProperties(),
    ) {
        Surface(
            modifier = modifier,
            shape = MaterialTheme.shapes.medium,
            color = AlertDialogDefaults.containerColor,
            tonalElevation = AlertDialogDefaults.TonalElevation,
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp)
            ) {
                title?.let {
                    CompositionLocalProvider(
                        LocalContentColor provides MaterialTheme.colorScheme.onSurface
                    ) {
                        val textStyle = MaterialTheme.typography.headlineSmall
                        ProvideTextStyle(textStyle) {
                            Box(
                                Modifier
                                    .padding(start = 8.dp, end = 8.dp, bottom = 16.dp)
                                    .align(Alignment.Start)
                            ) {
                                Text(it)
                            }
                        }
                    }
                }
                CompositionLocalProvider(
                    LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant
                ) {
                    val textStyle = MaterialTheme.typography.bodyMedium
                    ProvideTextStyle(textStyle) {
                        Column(
                            modifier = Modifier
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
                                    .verticalScroll(rememberScrollState())
                                    .weight(1F, false),
                                text = content,
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                val interactionSource = remember { MutableInteractionSource() }
                                val switch = { checkState.value = !checkState.value }
                                val m = remember { mutableStateOf(Offset.Zero) }
                                TriStateCheckbox(
                                    modifier = Modifier
                                        .positionInParent(m),
                                    state = ToggleableState(checkState.value),
                                    onClick = switch,
                                    interactionSource = interactionSource,
                                )
                                Text(
                                    text = checkBoxText,
                                    modifier = Modifier
                                        .delegatePress(interactionSource, switch, m)
                                )
                            }
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(horizontal = 8.dp)
                ) {
                    CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.primary) {
                        val textStyle =
                            MaterialTheme.typography.labelLarge
                        ProvideTextStyle(value = textStyle, content = confirmButton)
                    }
                }
            }
        }
    }
}

@Composable
fun DialogHeightFix(
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    dismissButton: @Composable (() -> Unit)? = null,
    title: String? = null,
    content: @Composable () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = confirmButton,
        modifier = modifier.padding(24.dp),
        dismissButton = dismissButton,
        title = title?.let {
            {
                Text(it)
            }
        },
        text = content,
        shape = MaterialTheme.shapes.medium,
        properties = DialogProperties(usePlatformDefaultWidth = false), //fixme
    )
}
