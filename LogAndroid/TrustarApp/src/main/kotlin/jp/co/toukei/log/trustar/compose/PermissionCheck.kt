@file:OptIn(ExperimentalPermissionsApi::class)

package jp.co.toukei.log.trustar.compose

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import jp.co.toukei.log.lib.Const
import jp.co.toukei.log.lib.compose.DefaultConfirmDialog
import jp.co.toukei.log.lib.ctx.Ctx
import jp.co.toukei.log.lib.startAppSettings
import jp.co.toukei.log.trustar.R
import splitties.systemservices.powerManager


@Composable
fun CheckAppPermissionOnce(
    checked: () -> Unit,
) {
    val context = LocalContext.current

    val startAppSettings = {
        context.startAppSettings()
    }

    var location by remember {
        mutableStateOf(true)
    }
    var notification by remember {
        mutableStateOf(true)
    }
    var battery by remember {
        mutableStateOf(true)
    }

    when {
        location -> {
            BackgroundLocationPermissionCheck(startAppSettings = startAppSettings) {
                location = false
            }
        }

        notification -> {
            NotificationPermissionCheck(startAppSettings = startAppSettings) {
                notification = false
            }
        }

        battery -> {
            BatteryOptimizationsPermissionCheck {
                battery = false
            }
        }

        else -> SideEffect(checked)
    }
}

@Composable
fun BackgroundLocationPermissionCheck(
    startAppSettings: () -> Unit,
    onGranted: () -> Unit,
) {
    var g by remember {
        mutableStateOf(true)
    }
    if (g) {
        P2(
            permissions = listOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ),
            denyShowRationale = R.string.permission_request_msg_location,
            denyDoNotShow = if (Build.VERSION.SDK_INT < 29) {
                R.string.app_settings_location_permission_msg
            } else {
                R.string.app_settings_location_background_permission_msg
            },
            startAppSettings = startAppSettings,
            onGranted = { g = false },
        )
    } else {
        if (Build.VERSION.SDK_INT < 29) {
            SideEffect(onGranted)
        } else {
            P(
                permission = Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                denyShowRationale = if (Build.VERSION.SDK_INT == 29) {
                    R.string.permission_request_msg_location_full
                } else {
                    R.string.app_settings_location_background_permission_msg
                },
                startAppSettings = startAppSettings,
                onGranted = onGranted,
            )
        }
    }
}

@Composable
fun FineLocationPermissionCheck(
    startAppSettings: () -> Unit,
    onGranted: () -> Unit,
) {
    P2(
        permissions = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        ),
        denyShowRationale = R.string.permission_request_msg_location,
        denyDoNotShow = R.string.app_settings_location_permission_msg,
        startAppSettings = startAppSettings,
        onGranted = onGranted,
    )
}

@Composable
fun NotificationPermissionCheck(
    startAppSettings: () -> Unit,
    onGranted: () -> Unit,
) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
        onGranted()
    } else {
        P(
            permission = Manifest.permission.POST_NOTIFICATIONS,
            denyShowRationale = R.string.permission_request_msg_notification,
            startAppSettings = startAppSettings,
            onGranted = onGranted,
        )
    }
}


@SuppressLint("BatteryLife")
@Composable
fun BatteryOptimizationsPermissionCheck(
    onGranted: () -> Unit,
) {
    val pkg = Ctx.context.packageName

    var any by remember {
        mutableIntStateOf(0)
    }

    if (any < 0 || powerManager.isIgnoringBatteryOptimizations(pkg)) {
        SideEffect(onGranted)
    } else {
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) { _ -> any++ }

        SideEffect {
            try {
                launcher.launch(
                    Intent(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
                        .apply { data = "package:$pkg".toUri() }
                )
            } catch (e: ActivityNotFoundException) {
                any = Int.MIN_VALUE
            }
        }
    }
}

@Composable
fun PendingIntentCheck(
    pendingIntent: PendingIntent,
    onResult: (ActivityResult?) -> Unit,
) {
    val r by rememberUpdatedState(newValue = onResult)
    var l by remember {
        mutableStateOf(true)
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = {
            r(it)
            l = true
        },
    )
    LaunchedEffect(pendingIntent) {
        if (l) {
            try {
                l = false
                launcher.launch(
                    IntentSenderRequest.Builder(pendingIntent)
                        .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP, Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        .build()
                )
            } catch (e: ActivityNotFoundException) {
                r(null)
            }
        }
    }
}

@Composable
fun ReadImageMediaPermissionCheck(
    startAppSettings: () -> Unit,
    cancel: (() -> Unit)? = null,
    onGranted: () -> Unit,
) {
    P(
        permission = if (Const.API_PRE_33) {
            Manifest.permission.READ_EXTERNAL_STORAGE
        } else {
            Manifest.permission.READ_MEDIA_IMAGES
        },
        denyShowRationale = if (Const.API_PRE_33) {
            R.string.app_permission_settings_storage
        } else {
            R.string.app_permission_settings_image
        },
        startAppSettings = startAppSettings,
        cancel = cancel,
        onGranted = onGranted
    )
}


@Composable
private fun D(
    @StringRes id: Int,
    onClick: () -> Unit,
    cancel: (() -> Unit)? = null,
) {
    if (cancel == null) {
        DefaultConfirmDialog(
            content = id,
            confirmButtonClick = onClick
        )
    } else {
        DefaultConfirmDialog(
            content = id,
            dismissButtonText = android.R.string.cancel,
            dismissButtonClick = cancel,
            confirmButtonClick = onClick
        )
    }
}

@Composable
private fun P(
    permission: String,
    denyShowRationale: Int,
    denyDoNotShow: Int = denyShowRationale,
    startAppSettings: () -> Unit,
    cancel: (() -> Unit)? = null,
    onGranted: () -> Unit,
) {
    var d by remember { mutableStateOf(false) }
    val p = rememberPermissionState(permission = permission) { d = true }
    val r: () -> Unit = p::launchPermissionRequest
    val s = p.status
    when {
        s.isGranted -> SideEffect(onGranted)
        s.shouldShowRationale -> D(denyShowRationale, r, cancel)
        d -> D(denyDoNotShow, startAppSettings, cancel)
        else -> LaunchedEffect(Unit) { r() }
    }
}

@Composable
private fun P2(
    permissions: List<String>,
    denyShowRationale: Int,
    denyDoNotShow: Int = denyShowRationale,
    startAppSettings: () -> Unit,
    cancel: (() -> Unit)? = null,
    onGranted: () -> Unit,
) {
    var d by remember { mutableStateOf(false) }
    val p = rememberMultiplePermissionsState(permissions = permissions) { d = true }
    val r: () -> Unit = p::launchMultiplePermissionRequest
    when {
        p.allPermissionsGranted -> SideEffect(onGranted)
        p.shouldShowRationale -> D(denyShowRationale, r, cancel)
        d -> D(denyDoNotShow, startAppSettings, cancel)
        else -> LaunchedEffect(Unit) { r() }
    }
}
