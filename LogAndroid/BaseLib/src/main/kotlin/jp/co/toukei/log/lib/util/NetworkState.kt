package jp.co.toukei.log.lib.util

import android.Manifest
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Looper
import androidx.annotation.RequiresPermission
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import jp.co.toukei.log.lib.Const
import splitties.systemservices.connectivityManager

class NetworkState {

    private val manager = connectivityManager

    private val callback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            networkLiveDataMutable.postValue(true)
        }

        override fun onLost(network: Network) {
            networkLiveDataMutable.postValue(false)
        }

        override fun onUnavailable() {
            networkLiveDataMutable.postValue(false)
        }
    }

    private var registered = false

    private val networkLiveDataMutable: MutableLiveData<Boolean> = MutableLiveData(false)

    @JvmField
    val networkLiveData: LiveData<Boolean> = networkLiveDataMutable

    val hasNetwork: Boolean
        get() {
            return networkLiveData.value ?: false
        }

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    fun register() {
        if (registered) return
        if (Looper.getMainLooper().thread !== Thread.currentThread()) throw IllegalStateException()
        registered = true
        if (Const.API_PRE_24) {
            manager.registerNetworkCallback(NetworkRequest.Builder().build(), callback)
        } else {
            manager.registerDefaultNetworkCallback(callback)
        }
    }

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    fun unregister() {
        if (registered) {
            if (Looper.getMainLooper().thread !== Thread.currentThread()) throw IllegalStateException()
            registered = false
            manager.unregisterNetworkCallback(callback)
        }
    }
}
