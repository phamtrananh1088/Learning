package jp.co.toukei.log.trustar.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import jp.co.toukei.log.common.compose.AppPropTodo
import jp.co.toukei.log.common.compose.HeadButton

@Composable
fun TwoButton1(
    modifier: Modifier,
    color: Color,
    contentColor: Color,
    button1Text: String,
    button2Text: String,
    button1Click: () -> Unit,
    button2Click: () -> Unit,
    button2Enabled: Boolean = true,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        HeadButton(
            horizontalPadding = 16,
            verticalPadding = 8,
            text = button1Text,
            style = AppPropTodo.Text.default,
            color = color,
            contentColor = contentColor,
            click = button1Click,
        )
        HeadButton(
            enabled = button2Enabled,
            horizontalPadding = 16,
            verticalPadding = 8,
            text = button2Text,
            style = AppPropTodo.Text.default,
            color = color,
            contentColor = contentColor,
            click = button2Click
        )
    }
}

@Composable
fun TwoButton2(
    modifier: Modifier,
    color: Color,
    contentColor: Color,
    button1Text: String,
    button2Text: String,
    button1Click: () -> Unit,
    button2Click: () -> Unit,
    button2EndIcon: ImageVector?,
    button2Enabled: Boolean,
) {
    Row(
        modifier = modifier,
    ) {
        Spacer(modifier = Modifier.weight(0.1F))
        HeadButton(
            modifier = Modifier.weight(0.6F),
            horizontalPadding = 16,
            verticalPadding = 12,
            text = button1Text,
            style = AppPropTodo.Text.default,
            color = color,
            contentColor = contentColor,
            click = button1Click,
        )
        Spacer(modifier = Modifier.weight(0.1F))
        HeadButton(
            modifier = Modifier.weight(0.6F),
            enabled = button2Enabled,
            horizontalPadding = 16,
            verticalPadding = 12,
            text = button2Text,
            style = AppPropTodo.Text.default,
            color = color,
            contentColor = contentColor,
            click = button2Click,
            contentEnd = button2EndIcon?.let { icon ->
                {
                    Icon(
                        modifier = Modifier
                            .padding(horizontal = 6.dp)
                            .size(24.dp),
                        imageVector = icon,
                        contentDescription = null,
                    )
                }
            }
        )
        Spacer(modifier = Modifier.weight(0.1F))
    }
}
