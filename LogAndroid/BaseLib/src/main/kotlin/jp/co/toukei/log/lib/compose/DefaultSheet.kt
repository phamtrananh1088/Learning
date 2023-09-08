package jp.co.toukei.log.lib.compose

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.dokar.sheets.BottomSheet
import com.dokar.sheets.BottomSheetDefaults
import com.dokar.sheets.BottomSheetValue
import com.dokar.sheets.rememberBottomSheetState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DefaultSheet(
    swipeDismiss: Boolean = true,
    onDismiss: () -> Unit,
    closing: (Boolean) -> Unit = { },
    content: @Composable (dismissRequest: () -> Unit) -> Unit,
) {
    val state = rememberBottomSheetState(BottomSheetValue.Collapsed)
    val scope = rememberCoroutineScope()
    var e by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        closing(false)
        state.expand()
        e = true
    }
    if (e) {
        LaunchedEffect(state.visible) {
            if (!state.visible) {
                onDismiss()
            }
        }
    }
    val dismiss: () -> Unit = {
        scope.launch {
            delay(20)
            closing(true)
            state.collapse()
        }
    }

    BottomSheet(
        modifier = Modifier,
        state = state,
        dragHandle = {},
        skipPeeked = true,
        behaviors = BottomSheetDefaults.dialogSheetBehaviors(
            lightStatusBar = false,
            lightNavigationBar = false,
        ),
        backgroundColor = MaterialTheme.colorScheme.surface,
    ) {
        Surface {
            content(dismiss)
        }
    }
}
