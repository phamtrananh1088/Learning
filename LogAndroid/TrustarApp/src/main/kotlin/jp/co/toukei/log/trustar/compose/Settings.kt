package jp.co.toukei.log.trustar.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import jp.co.toukei.log.common.compose.AppPropTodo
import jp.co.toukei.log.common.compose.HeadButton
import jp.co.toukei.log.common.compose.VerticalDivider
import jp.co.toukei.log.lib.compose.NormalTopBar
import jp.co.toukei.log.lib.compose.navigateUpLambda
import jp.co.toukei.log.lib.makeDial
import jp.co.toukei.log.trustar.Config
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.compose.destinations.HomeWeatherSettingDestination
import jp.co.toukei.log.trustar.deprecated.startActivity
import jp.co.toukei.log.trustar.activity.LoginActivity
import jp.co.toukei.log.trustar.viewmodel.HomeVM

@Composable
@Destination
fun Settings(
    vm: HomeVM,
    navigator: DestinationsNavigator,
) {
    val context = LocalContext.current
    val u = vm.userInfo
    Settings(
        naviBack = navigator.navigateUpLambda(),
        tel = u.branchTel,
        telClick = {
            context.makeDial(u.branchTel)
        },
        weatherClick = {
            navigator.navigate(HomeWeatherSettingDestination)
        },
        userClick = {
            context.startActivity<LoginActivity>()
        },
        deviceId = Config.androidId
    )
}

@Composable
fun Settings(
    naviBack: () -> Unit,
    tel: String,
    telClick: () -> Unit,
    weatherClick: () -> Unit,
    userClick: () -> Unit,
    deviceId: String,
) {
    Scaffold(
        topBar = {
            NormalTopBar(
                title = stringResource(id = R.string.settings),
                navigationIcon = {
                    IconButton(onClick = naviBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        },
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(it)
                .padding(vertical = 64.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            var btnWidth by remember {
                mutableIntStateOf(0)
            }
            val btnModifier = Modifier
                .onSizeChanged { (w, _) ->
                    btnWidth = maxOf(btnWidth, w)
                }
                .widthIn(min = LocalDensity.current.run { btnWidth.toDp() - 0.1.dp })
            val dividerModifier = Modifier.padding(vertical = 32.dp)
            Text(
                modifier = Modifier.padding(8.dp),
                text = stringResource(R.string.ask_for_support),
                style = AppPropTodo.Text.defaultBold,
            )
            HeadButton(
                modifier = btnModifier,
                horizontalPadding = 28,
                verticalPadding = 12,
                text = tel,
                style = AppPropTodo.Text.bold16,
                color = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                click = telClick,
                contentStart = {
                    Icon(
                        modifier = Modifier
                            .padding(end = 6.dp)
                            .size(24.dp),
                        imageVector = Icons.Default.Call,
                        contentDescription = null,
                    )
                }
            )
            VerticalDivider(
                modifier = dividerModifier.padding(top = 32.dp)
            )
            HeadButton(
                modifier = btnModifier,
                horizontalPadding = 28,
                verticalPadding = 12,
                text = stringResource(id = R.string.weather_settings),
                style = AppPropTodo.Text.bold16,
                color = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                click = weatherClick,
            )
            VerticalDivider(
                modifier = dividerModifier
            )
            HeadButton(
                modifier = btnModifier,
                horizontalPadding = 28,
                verticalPadding = 12,
                text = stringResource(id = R.string.switch_user),
                style = AppPropTodo.Text.bold16,
                color = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                click = userClick,
            )
            VerticalDivider(
                modifier = dividerModifier
            )
            Text(text = stringResource(id = R.string.terminal_id_s1, deviceId))
        }
    }
}
