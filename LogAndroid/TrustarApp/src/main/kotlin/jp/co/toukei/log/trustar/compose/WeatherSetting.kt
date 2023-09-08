package jp.co.toukei.log.trustar.compose

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.sebaslogen.resaca.rememberScoped
import jp.co.toukei.log.common.compose.AppPropTodo
import jp.co.toukei.log.common.compose.HeadButton
import jp.co.toukei.log.lib.compose.DefaultSheet
import jp.co.toukei.log.lib.compose.delegatePress
import jp.co.toukei.log.lib.compose.navigateUpLambda
import jp.co.toukei.log.lib.compose.positionInParent
import jp.co.toukei.log.lib.ctx.Ctx
import jp.co.toukei.log.trustar.Config
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.getLastWeather
import jp.co.toukei.log.trustar.other.Weather
import jp.co.toukei.log.trustar.setWeather
import java.lang.Integer.max

@Composable
fun WeatherSetting(
    modifier: Modifier = Modifier,
    date: String,
    buttonText: String,
    buttonClick: () -> Unit,
    weather: Weather,
    selectWeather: (Weather) -> Unit,
) {
    var showMenu by rememberScoped {
        mutableStateOf(false)
    }

    var closing by remember {
        mutableStateOf(false)
    }

    @Suppress("AnimateAsStateLabel")
    val iconRotate by animateFloatAsState(targetValue = if (showMenu && !closing) 180F else 0F)

    Box(
        modifier = modifier
            .primaryBackground()
            .padding(64.dp),
        contentAlignment = Alignment.Center,
    ) {
        val contentColor = MaterialTheme.colorScheme.onPrimary
        Text(
            text = date,
            style = TextStyle(
                color = contentColor,
                fontSize = 22.sp,
                fontWeight = FontWeight.Normal,
                letterSpacing = 0.3.sp,
                shadow = Shadow(
                    color = Color(0x66000000),
                    offset = Offset(1F, 1F),
                    blurRadius = 4F,
                )
            ),
            modifier = Modifier
                .align(Alignment.TopCenter),
        )
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                modifier = Modifier
                    .padding(8.dp),
                text = stringResource(id = R.string.tf_weather_today),
                color = contentColor,
                style = AppPropTodo.Text.default
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                val interactionSource = remember { MutableInteractionSource() }
                val click = { showMenu = true }
                val m = remember { mutableStateOf(Offset.Zero) }
                Spacer(modifier = Modifier.size(32.dp))
                Image(
                    modifier = Modifier
                        .positionInParent(m)
                        .clip(CircleShape)
                        .background(contentColor)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = LocalIndication.current,
                            onClick = click,
                        )
                        .padding(12.dp)
                        .size(36.dp),
                    painter = painterResource(id = weather.icon),
                    contentDescription = null,
                )
                Icon(
                    modifier = Modifier
                        .delegatePress(interactionSource, click, m)
                        .rotate(iconRotate)
                        .size(32.dp)
                        .padding(4.dp),
                    imageVector = Icons.Default.ExpandMore,
                    tint = contentColor,
                    contentDescription = null
                )
            }
        }

        HeadButton(
            modifier = Modifier.align(Alignment.BottomCenter),
            horizontalPadding = 32,
            verticalPadding = 12,
            text = buttonText,
            style = AppPropTodo.Text.defaultBold,
            color = MaterialTheme.colorScheme.onPrimary,
            contentColor = MaterialTheme.colorScheme.primary,
            click = buttonClick,
        )
    }

    if (showMenu) {
        DefaultSheet(
            onDismiss = {
                showMenu = false
            },
            closing = {
                closing = it
            },
        ) { dismissRequest ->
            Menu {
                selectWeather(it)
                dismissRequest()
            }
        }
    }
}

@Composable
private fun Menu(
    select: (Weather) -> Unit,
) {
    val array = arrayOf(
        Weather.Hare,
        Weather.Kumori,
        Weather.Ame,
        Weather.Yuki,
        Weather.HareKumori,
        Weather.KumoriHare
    )
    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth(),
    ) {
        val span = max(1, (maxWidth / 128.dp).toInt()).let {
            if (it == 4 || it == 5) 3 else it
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(span),
        ) {
            items(array) {
                Column(
                    modifier = Modifier
                        .clickable { select(it) }
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(
                        modifier = Modifier
                            .size(72.dp)
                            .padding(12.dp),
                        painter = painterResource(id = it.icon),
                        contentDescription = null
                    )
                    Text(
                        modifier = Modifier
                            .padding(8.dp),
                        text = stringResource(id = it.name)
                    )
                }
            }
        }
    }
}


@Composable
fun WeatherSetting(
    buttonText: String,
    buttonClick: () -> Unit,
) {
    val currentDate by rememberSystemTimeChange()
    val dateStr by remember {
        derivedStateOf {
            Config.dateFormatter4.format(currentDate)
        }
    }
    var weather by remember {
        mutableStateOf(Ctx.context.getLastWeather() ?: Weather.Hare)
    }
    //always set current one
    LaunchedEffect(weather) {
        Ctx.context.setWeather(weather)
    }
    WeatherSetting(
        date = dateStr,
        buttonText = buttonText,
        buttonClick = buttonClick,
        weather = weather,
        selectWeather = {
            weather = it
        }
    )
}

@Composable
@Destination
fun HomeWeatherSetting(
    navigator: DestinationsNavigator,
) {
    WeatherSetting(
        buttonText = stringResource(id = R.string.weather_setup_close),
        buttonClick = navigator.navigateUpLambda(),
    )
}

@Composable
@Destination
fun LoginWeather(
    buttonClick: () -> Unit,
) {
    WeatherSetting(
        buttonText = stringResource(id = R.string.goto_home_dashboard),
        buttonClick = buttonClick
    )
}
