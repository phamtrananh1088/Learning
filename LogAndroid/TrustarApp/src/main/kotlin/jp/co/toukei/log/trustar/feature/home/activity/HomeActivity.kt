package jp.co.toukei.log.trustar.feature.home.activity

import android.Manifest
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.annotation.UiThread
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.switchMap
import androidx.lifecycle.toLiveData
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView.OnItemSelectedListener
import io.reactivex.rxjava3.functions.Consumer
import jp.co.toukei.log.lib.Const
import jp.co.toukei.log.lib.common.materialAlertDialogBuilder
import jp.co.toukei.log.lib.common.snackbar
import jp.co.toukei.log.lib.common.toast
import jp.co.toukei.log.lib.ctx.Ctx
import jp.co.toukei.log.lib.lazyViewModel
import jp.co.toukei.log.lib.lifecycle.SystemTimeObserver
import jp.co.toukei.log.lib.localBroadcastManager
import jp.co.toukei.log.lib.observeNonNull
import jp.co.toukei.log.lib.observeNullable
import jp.co.toukei.log.lib.replaceToTag
import jp.co.toukei.log.lib.startAppSettings
import jp.co.toukei.log.lib.startForegroundServiceCompat
import jp.co.toukei.log.lib.switchMapNullable
import jp.co.toukei.log.lib.util.DialogWrapper
import jp.co.toukei.log.lib.util.FusedLocationHelper
import jp.co.toukei.log.lib.weakRef
import jp.co.toukei.log.trustar.Config
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.errMessage
import jp.co.toukei.log.trustar.feature.home.fragment.DashboardFragment
import jp.co.toukei.log.trustar.feature.home.fragment.DetailFragment
import jp.co.toukei.log.trustar.feature.home.fragment.NavMessage
import jp.co.toukei.log.trustar.feature.home.fragment.OperationFragment
import jp.co.toukei.log.trustar.feature.home.fragment.RefuelFragment
import jp.co.toukei.log.trustar.feature.home.fragment.SettingsFragment
import jp.co.toukei.log.trustar.feature.home.model.HomeModel
import jp.co.toukei.log.trustar.feature.home.ui.AutoModeDialogUI
import jp.co.toukei.log.trustar.feature.home.ui.HomeActivityUI
import jp.co.toukei.log.trustar.feature.login.activity.RequireLoginActivity
import jp.co.toukei.log.trustar.preventAutoModeDialog
import jp.co.toukei.log.trustar.requireIgnoringBatteryOptimizations
import jp.co.toukei.log.trustar.service.BinLocationService
import jp.co.toukei.log.trustar.setPreventAutoModeDialog
import jp.co.toukei.log.trustar.user.BinLocationTask
import java.util.Optional
import java.util.concurrent.atomic.AtomicReference

/**
 * Fragments:
 * - ダッシュボード [DashboardFragment]
 * - 配送計画 [DetailFragment]
 * - 作業入力 [OperationFragment]
 * - 給油 [RefuelFragment]
 * - 設定 [SettingsFragment]
 * - msg [NavMessage]
 */
class HomeActivity : RequireLoginActivity() {

    private var ui: HomeActivityUI? = null

    private val homeModel by lazyViewModel<HomeModel>()

    override fun onCreate1(savedInstanceState: Bundle?) {
        val ui = HomeActivityUI(this)
        setContentView(ui.root)
        this.ui = ui

        ui.bottomNavigation.setOnItemSelectedListener(onSelected)

        var menu = homeModel.lastSelectedItemId
        if (menu == -1) menu = R.string.home_navigation_dashboard
        homeModel.postMenuId(menu)

        homeModel.selectedNavMenu.observeNonNull(this) {
            it.getContentIfNotHandled()?.run {
                onSelected.selectIdFromUiThread(ui.bottomNavigation, this)
            }
        }

        homeModel.dataSource.selectedBinHeader
            .observeNullable(this) {
                if (it == null) {
                    homeModel.postMenuId(R.string.home_navigation_dashboard)
                }
            }
        homeModel.dataSource.startedBinDetailListOfSelectedBinHeader.observeNullable(this) {
            val id = R.string.home_navigation_operation
            ui.bottomNavigation.apply {
                if (it.isNullOrEmpty()) {
                    removeBadge(id)
                    postInvalidate()
                } else {
                    getOrCreateBadge(id)
                }
            }
        }
        Current.userChangeObservable.observeNullable(this) {
            alwaysOnLocationHelper.setLocationRequest(
                LocationRequest.create().apply {
                    priority = Priority.PRIORITY_HIGH_ACCURACY
                    interval = (it?.userInfo?.backgroundInterval ?: 60) * 1000L
                    fastestInterval = 3_000
                },
                errConsumer
            )
        }
        binLocationTask = Current.user?.binLocationTask
        binLocationTask?.also {
            it.ensureRequest(errConsumer.weakRef())
            //todo should try to start service while activity paused?
            it.throttleLatestRecord(5).toLiveData().observe(this, binRecorderObserver)
        }
        Config.networkState.networkLiveData.observeNonNull(this) {
            if (it) requestPermissionAll()
        }
        homeModel.showAutoModeDialog
            .switchMapNullable {
                if (it && !Current.userInfo.preventAutoModeDialog()) homeModel.inAutoMode else null
            }
            .switchMap { MutableLiveData(it) }
            .distinctUntilChanged()
            .observeNonNull(this) {
                autoModeDialogWrapper.displayDialog(it)
            }
        lifecycle.addObserver(SystemTimeObserver(this) {
            homeModel.updateSystemDate(System.currentTimeMillis())
        })
        homeModel.unreadMessageCount.observeNonNull(this) {
            if (it == 0) {
                ui.bottomNavigation.removeBadge(R.string.navigation_message)
            } else {
                ui.bottomNavigation.getOrCreateBadge(R.string.navigation_message).number = it
            }
        }
    }

    private val autoModeDialogWrapper = object : DialogWrapper<Nothing>() {
        override fun createDialog(onDismiss: DialogInterface.OnDismissListener): Dialog {
            return materialAlertDialogBuilder {
                val ui = AutoModeDialogUI(context)
                ui.setMsg(if (inAuto) R.string.auto_mode_a_dialog_msg else R.string.auto_mode_m_dialog_msg)
                setTitle(if (inAuto) R.string.auto_mode_a_dialog_title else R.string.auto_mode_m_dialog_title)
                setView(ui.view)
                setPositiveButton(android.R.string.ok) { _, _ ->
                    if (ui.checked) {
                        Current.userInfo.setPreventAutoModeDialog()
                    }
                }
                setOnDismissListener {
                    homeModel.showAutoModeDialog.value = false
                    onDismiss.onDismiss(it)
                }
            }.show()
        }

        private var inAuto = false
        fun displayDialog(isInAutoMode: Boolean) {
            if (inAuto != isInAutoMode) {
                inAuto = isInAutoMode
                dismiss()
            }
            showDialog()
        }
    }

    private var binLocationTask: BinLocationTask? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerReceiver(receiver, receiver.intentFilter)
    }

    override fun onPause() {
        super.onPause()
        localBroadcastManager.unregisterReceiver(receiver)
    }

    override fun onStop() {
        super.onStop()
        alwaysOnLocationHelper.disconnect()
    }

    override fun onStart() {
        super.onStart()
        requestPermissionAll()
    }

    override fun onResume() {
        super.onResume()
        localBroadcastManager.registerReceiver(receiver, IntentFilter(Config.ACTION_HTTP_401))
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
        alwaysOnLocationHelper.destroy()
    }

    private val receiver = object : BroadcastReceiver() {
        @JvmField
        val intentFilter = IntentFilter().apply {
            addAction(LocationManager.PROVIDERS_CHANGED_ACTION)
        }

        private var isAlertShowing = false

        override fun onReceive(context: Context, intent: Intent?) {
            when (intent?.action) {
                LocationManager.PROVIDERS_CHANGED_ACTION -> requestPermissionAll()
                Config.ACTION_HTTP_401 -> {
                    if (!isAlertShowing)
                        materialAlertDialogBuilder {
                            setCancelable(false)
                            setMessage(R.string.invalid_account_alert_msg)
                            setPositiveButton(android.R.string.ok) { d, _ ->
                                Current.logout(context)
                                isAlertShowing = false
                                d.dismiss()
                            }
                        }.show()
                    isAlertShowing = true
                }
            }
        }
    }

    private val onSelected = object : OnItemSelectedListener {

        private var byUser = true

        /**
         * No need to use ThreadLocal here.
         * I just can't hack into BottomNavigationMenuView.onClickListener
         * @see [BottomNavigationView.setSelectedItemId]
         */
        @UiThread
        fun selectIdFromUiThread(btn: BottomNavigationView, id: Int) {
            byUser = false
            btn.selectedItemId = id
            byUser = true
        }

        override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
            val id = menuItem.itemId
            if (byUser) {
                homeModel.postMenuId(id)
                return false
            }

            val fm = supportFragmentManager
            val transaction = fm.beginTransaction()

            fm.replaceToTag(R.id.id_container, id.toString(), transaction) {
                createFragmentInstance(id)
            }
            transaction.commitAllowingStateLoss()

            homeModel.lastSelectedItemId = id

            when (id) {
                R.string.home_navigation_dashboard,
                R.string.home_navigation_deliver,
                R.string.home_navigation_operation,
                -> {
                    Current.syncBin(false)
                }
            }
            return true
        }

        private fun createFragmentInstance(id: Int): Fragment {
            return when (id) {
                R.string.home_navigation_dashboard -> DashboardFragment()
                R.string.home_navigation_deliver -> DetailFragment()
                R.string.home_navigation_operation -> OperationFragment()
                R.string.home_navigation_refuel -> RefuelFragment()
                R.string.navigation_message -> NavMessage()
                else -> throw IllegalArgumentException()
            }
        }
    }

    override fun onBackPressed() {
        if (onBackPressedDispatcher.hasEnabledCallbacks()) super.onBackPressed()
        else {
            val id = when (homeModel.lastSelectedItemId) {
                R.string.home_navigation_deliver -> R.string.home_navigation_dashboard
                R.string.home_navigation_operation -> R.string.home_navigation_deliver
                R.string.home_navigation_refuel -> R.string.home_navigation_dashboard
                R.string.navigation_message -> R.string.home_navigation_dashboard
                else -> return
            }
            homeModel.postMenuId(id)
        }
    }

    private val onLocationResult = object : LocationCallback() {

        override fun onLocationResult(result: LocationResult) {
            result.lastLocation.let {
                Log.d("tr", "location: $it")
                Current.lastLocation = it
            }
        }
    }
    private val alwaysOnLocationHelper = FusedLocationHelper(Ctx.context, onLocationResult)

    private val errConsumer = object : Consumer<Throwable> {

        override fun accept(it: Throwable) {
            if (it is ResolvableApiException) {
                try {
                    if (homeModel.askedForGPS) return
                    it.startResolutionForResult(this@HomeActivity, REQ_GMS_GPS)
                    homeModel.askedForGPS = true
                } catch (e: Exception) {
                    toast(e.errMessage())
                }
            } else {
                ui?.bottomNavigation?.snackbar(it.errMessage())
            }
        }
    }

    private val binRecorderObserver = Observer<Optional<out Any>?> {
        if (it?.isPresent == true) startForegroundServiceCompat<BinLocationService>()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (permissions.isEmpty()) return
        when (requestCode) {
            REQ_LOCATION -> {
                homeModel.notAskedForLocation = false
                requestPermissionAll()
            }

            REQ_LOCATION_BG -> {
                homeModel.notAskedForLocationBg = false
                requestPermissionAll()
            }

            REQ_NOTIFICATION -> {
                requestNotificationPermission()
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQ_BATTERY -> {
                homeModel.askedForBattery = false
                requestPermissionAll()
            }

            REQ_GMS_GPS -> {
                homeModel.askedForGPS = false
                requestPermissionAll()
            }

        }
    }

    private fun requestPermissionAll() {
        if (requestLocationPermission() && requestIgnoringBatteryOptimizations() && requestNotificationPermission()) ensureLocationEnv()
    }

    private fun ensureLocationEnv() {
        binLocationTask?.checkSettings()
        alwaysOnLocationHelper.connect()
    }

    private fun requestIgnoringBatteryOptimizations(): Boolean {
        if (homeModel.askedForBattery) return false
        val r = requireIgnoringBatteryOptimizations(REQ_BATTERY)
        if (!r) {
            homeModel.askedForBattery = true
        }
        return r
    }

    private val lastLocationDialog = AtomicReference<DialogInterface>()
    private val lastNotificationDialog = AtomicReference<DialogInterface>()

    private fun requestLocationPermission(): Boolean {
        if (
            checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            val msg = if (
                homeModel.notAskedForLocation ||
                ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                R.string.permission_request_msg_location
            } else {
                when {
                    Build.VERSION.SDK_INT < 29 -> {
                        R.string.app_settings_location_permission_msg
                    }

                    else -> {
                        R.string.app_settings_location_background_permission_msg
                    }
                }
            }
            val d = materialAlertDialogBuilder {
                setMessage(msg)
                setCancelable(false)
                setPositiveButton(android.R.string.ok, null)
            }.create()
            d.setOnDismissListener {
                if (lastLocationDialog.compareAndSet(d, null)) {
                    val s = homeModel.notAskedForLocation ||
                            ActivityCompat.shouldShowRequestPermissionRationale(
                                this, Manifest.permission.ACCESS_COARSE_LOCATION
                            ) ||
                            ActivityCompat.shouldShowRequestPermissionRationale(
                                this,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            )
                    if (s) {
                        requestPermissions(
                            arrayOf(
                                Manifest.permission.ACCESS_COARSE_LOCATION, // >= sdk 31
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ),
                            REQ_LOCATION
                        )
                    } else {
                        startAppSettings()
                    }
                }
            }
            d.show()
            lastLocationDialog.getAndSet(d)?.dismiss()
        } else {
            if (Build.VERSION.SDK_INT < 29 || checkSelfPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                return true
            }

            val msg =
                if (Build.VERSION.SDK_INT == 29 && (homeModel.notAskedForLocationBg || ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    ))
                ) {
                    R.string.permission_request_msg_location_full
                } else {
                    R.string.app_settings_location_background_permission_msg
                }
            val d = materialAlertDialogBuilder {
                setMessage(msg)
                setCancelable(false)
                setPositiveButton(android.R.string.ok, null)
            }.create()
            d.setOnDismissListener {
                if (lastLocationDialog.compareAndSet(d, null)) {
                    val s =
                        homeModel.notAskedForLocationBg || ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION
                        )
                    if (s) requestPermissions(
                        arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                        REQ_LOCATION_BG
                    )
                    else startAppSettings()
                }
            }
            d.show()
            lastLocationDialog.getAndSet(d)?.dismiss()
        }
        return false
    }

    private fun requestNotificationPermission(): Boolean {
        if (!Const.API_PRE_33 && checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(
                    this@HomeActivity,
                    Manifest.permission.POST_NOTIFICATIONS
                )
            ) {
                val d = materialAlertDialogBuilder {
                    setMessage(R.string.permission_request_msg_notification)
                    setCancelable(false)
                    setPositiveButton(android.R.string.ok) { _, _ ->
                        val s2 = ActivityCompat.shouldShowRequestPermissionRationale(
                            this@HomeActivity,
                            Manifest.permission.POST_NOTIFICATIONS
                        )
                        if (s2) {
                            startAppSettings()
                        } else {
                            requestPermissions(
                                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                                REQ_NOTIFICATION
                            )
                        }
                    }
                }.create()
                d.show()
                lastNotificationDialog.getAndSet(d)?.dismiss()
                return false
            }
        }
        return true
    }
}

private const val REQ_LOCATION = 1
private const val REQ_LOCATION_BG = 2
private const val REQ_BATTERY = 3
private const val REQ_GMS_GPS = 4
private const val REQ_NOTIFICATION = 5
