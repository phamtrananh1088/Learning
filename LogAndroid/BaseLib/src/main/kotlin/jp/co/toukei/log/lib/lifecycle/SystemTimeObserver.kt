package jp.co.toukei.log.lib.lifecycle

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

class SystemTimeObserver(
    private val context: Context,
    private val onChanged: (intent: Intent) -> Unit
) : LifecycleEventObserver {

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_CREATE -> context.registerReceiver(broadcastReceiver, intentFilter)
            Lifecycle.Event.ON_DESTROY -> context.unregisterReceiver(broadcastReceiver)
            else -> {
            }
        }
    }

    @JvmField
    val intentFilter = IntentFilter().apply {
        addAction(Intent.ACTION_TIME_CHANGED)
        addAction(Intent.ACTION_TIME_TICK)
        addAction(Intent.ACTION_TIMEZONE_CHANGED)
    }

    private val broadcastReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                Intent.ACTION_TIME_CHANGED, Intent.ACTION_TIMEZONE_CHANGED, Intent.ACTION_TIME_TICK -> {
                    onChanged(intent)
                }
            }
        }
    }
}
