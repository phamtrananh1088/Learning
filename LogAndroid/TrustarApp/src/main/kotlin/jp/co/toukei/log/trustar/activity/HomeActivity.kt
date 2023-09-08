package jp.co.toukei.log.trustar.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.Priority
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import jp.co.toukei.log.common.compose.M3ContentTheme
import jp.co.toukei.log.lib.buildNotification
import jp.co.toukei.log.lib.compose.DefaultConfirmDialog
import jp.co.toukei.log.lib.compose.recollectState
import jp.co.toukei.log.lib.ctx.Ctx
import jp.co.toukei.log.lib.startAppSettings
import jp.co.toukei.log.lib.startForegroundServiceCompat
import jp.co.toukei.log.lib.util.LocationRequestHelper
import jp.co.toukei.log.trustar.App
import jp.co.toukei.log.trustar.BroadcastEvent
import jp.co.toukei.log.trustar.Config
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.compose.BackgroundLocationPermissionCheck
import jp.co.toukei.log.trustar.compose.CheckAppPermissionOnce
import jp.co.toukei.log.trustar.compose.NavGraphs
import jp.co.toukei.log.trustar.compose.PendingIntentCheck
import jp.co.toukei.log.trustar.compose.destinations.AddChatRoomDestination
import jp.co.toukei.log.trustar.compose.destinations.ChatOptionDestination
import jp.co.toukei.log.trustar.compose.destinations.HomeDestination
import jp.co.toukei.log.trustar.compose.destinations.HomeWeatherSettingDestination
import jp.co.toukei.log.trustar.compose.destinations.NoticeListDestination
import jp.co.toukei.log.trustar.compose.destinations.SettingsDestination
import jp.co.toukei.log.trustar.service.BinLocationService
import jp.co.toukei.log.trustar.user.LoggedUser
import jp.co.toukei.log.trustar.viewmodel.HomeVM
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import splitties.systemservices.notificationManager

class HomeActivity : RequireLoginActivity() {


    private val resumedState = mutableStateOf(false)

    override fun onResume() {
        super.onResume()
        resumedState.value = true
    }

    override fun onPause() {
        super.onPause()
        resumedState.value = false
    }

    override fun onCreate1(savedInstanceState: Bundle?, user: LoggedUser) {
        val alwaysOnLocationRequest = LocationRequestHelper(
            user.locationHelper,
            {
                Current.lastLocation = it
            },
            {
                App.gmsLocationErrFlow.tryEmit(it)
            }
        ).apply {
            setRequest(
                LocationRequest.Builder((user.userInfo.backgroundInterval ?: 60) * 1000L)
                    .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                    .setMinUpdateIntervalMillis(3000)
                    .build()
            )
        }
        val checkGMSLocation: () -> Unit = {
            alwaysOnLocationRequest.reconnected(true)
        }
        val displayGmsErr: (Boolean) -> Unit = {
            notificationManager.apply {
                val id = Config.nidLocationUnavailable
                if (it) {
                    notify(id, buildNotification(Config.NotificationChannelIdErr) {
                        setContentText(Ctx.context.getString(R.string.location_service_unavailable))
                        setSmallIcon(R.drawable.baseline_info_24)
                        setOngoing(true)
                    })
                } else {
                    cancel(id)
                }
            }
        }

        setContent {
            M3ContentTheme(
                darkTheme = false
            ) {
                val resumed = resumedState.value
                LaunchedEffect(resumed) {
                    if (resumed) {
                        checkGMSLocation()
                    } else {
                        alwaysOnLocationRequest.disconnect(false)
                    }
                }

                var logout by remember {
                    mutableStateOf(false)
                }
                val vm = viewModel {
                    HomeVM(user)
                }
                LaunchedEffect(Unit) {
                    App.broadcastFlow().collectLatest {
                        when (it) {
                            is BroadcastEvent.Logout -> {
                                logout = true
                            }

                            is BroadcastEvent.LocationSettingsChanged -> {
                                checkGMSLocation()
                            }

                            else -> {}
                        }
                    }
                }

                if (logout) {
                    //todo: top dialog.
                    DefaultConfirmDialog(
                        content = R.string.invalid_account_alert_msg,
                    ) {
                        Current.logout(Ctx.context)
                    }
                }

                PermissionCheck(
                    displayGmsErr = displayGmsErr,
                    checkGMSLocation = checkGMSLocation
                )

                StartLocationService(vm)

                DestinationsNavHost(
                    navGraph = NavGraphs.root,
                    dependenciesContainerBuilder = {
                        dependency(HomeDestination) { vm }
                        dependency(SettingsDestination) { vm }
                        dependency(HomeWeatherSettingDestination) { vm }
                        dependency(NoticeListDestination) { vm }
                        dependency(AddChatRoomDestination) { vm }
                        dependency(ChatOptionDestination) { vm }
                    }
                )
            }
        }
    }
}

@Composable
private fun StartLocationService(
    vm: HomeVM
) {
    var permissionOk by remember {
        mutableStateOf(false)
    }
    if (permissionOk) {
        val t by remember(vm) {
            vm.user.binLocationTask.throttleLatestRecord(5)
        }.subscribeAsState(null)

        if (t?.running == true) {
            LaunchedEffect(t) {
                Ctx.context.startForegroundServiceCompat<BinLocationService>()
            }
        }
    } else {
        BackgroundLocationPermissionCheck({
            Ctx.context.startAppSettings()
        }) {
            permissionOk = true
        }
    }
}

//todo
@Composable
private fun PermissionCheck(
    displayGmsErr: (Boolean) -> Unit,
    checkGMSLocation: () -> Unit,
) {
    var permissionOk by remember {
        mutableStateOf(false)
    }
    if (permissionOk) {
        var recheck by remember {
            mutableIntStateOf(1)
        }
        val gmsErr by App.gmsLocationErrFlow.recollectState(null, recheck)

        gmsErr?.let { g ->
            if (g is ResolvableApiException) {
                PendingIntentCheck(g.resolution) {
                    recheck++
                }
            } else {
                DisposableEffect(Unit) {
                    displayGmsErr(true)
                    onDispose {
                        displayGmsErr(false)
                    }
                }
            }
        }
        LaunchedEffect(recheck) {
            delay(1000)
            checkGMSLocation()
        }
    } else {
        CheckAppPermissionOnce {
            permissionOk = true
        }
    }
}
