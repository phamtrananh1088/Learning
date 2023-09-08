package jp.co.toukei.log.trustar.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sebaslogen.resaca.rememberScoped
import jp.co.toukei.log.common.compose.M3ContentTheme
import jp.co.toukei.log.lib.ctx.Ctx
import jp.co.toukei.log.lib.startActivityClearAndNewTask
import jp.co.toukei.log.trustar.Config
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.compose.Login
import jp.co.toukei.log.trustar.compose.NoticeListWithConfirm2
import jp.co.toukei.log.trustar.compose.WeatherSetting
import jp.co.toukei.log.trustar.repo.Important_Notice
import jp.co.toukei.log.trustar.viewmodel.LoginOkVM
import jp.co.toukei.log.trustar.viewmodel.LoginVM

/**
 * ログインActivity
 */
class LoginActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginCompose()
        }
    }
}


@Composable
private fun LoginCompose() {

    M3ContentTheme {

        var loggedIn by rememberScoped {
            mutableStateOf(false)
        }

        //todo fade animation, common bg

        if (loggedIn) {
            val ctx = LocalContext.current
            val home: () -> Unit = {
                ctx.startActivityClearAndNewTask<HomeActivity>()
            }
            BackHandler(onBack = home)
            val vm = viewModel {
                LoginOkVM(Current.loggedUser)
            }
            val noticeList by remember(vm) {
                vm.noticeList(
                    Important_Notice,
                    true
                )
            }.subscribeAsState(emptyList())
            if (noticeList.isEmpty() || vm.showWeather.value) {
                WeatherSetting(
                    buttonText = stringResource(id = R.string.goto_home_dashboard),
                    buttonClick = home
                )
            } else {
                NoticeListWithConfirm2(
                    vm = vm,
                    list = noticeList,
                    buttonClick = {
                        vm.noticeMarkRead(Important_Notice)
                        vm.showWeather.value = true
                    },
                )
            }
        } else {
            NavLogin {
                loggedIn = true
            }
        }
    }

}


@Composable
private fun NavLogin(
    loggedIn: () -> Unit,
) {

    val snbHost = remember {
        SnackbarHostState()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snbHost) },
    ) {
        val vm = viewModel {
            LoginVM(
                defaultCompanyCd = Config.pref.lastUserCompany.orEmpty(),
            )
        }

        Login(
            vm = vm,
            modifier = Modifier,
            paddingValues = it,
            snbHost = snbHost,
            deviceId = Config.androidId,
        ) { u ->
            Current.login(Ctx.context, u.user, u.json)
            loggedIn()
        }
    }
}
