package jp.co.toukei.log.lib

import android.os.Build
import android.os.Handler
import android.os.Looper
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory

object Const {

    @JvmField
    val computationExecutor: Executor = Executors.newFixedThreadPool(
        Runtime.getRuntime().availableProcessors(),
        ThreadFactoryImpl("computation", Thread.MAX_PRIORITY)
    )

    @JvmField
    val mainLooper: Looper = Looper.getMainLooper()

    @JvmField
    val mainHandler = Handler(mainLooper)

    @JvmField
    val ioExecutor: Executor =
        Executors.newCachedThreadPool(ThreadFactoryImpl("io", Thread.MIN_PRIORITY))

    @JvmField
    val mainScheduler: Scheduler = AndroidSchedulers.mainThread()

    private class ThreadFactoryImpl(
        private val name: String,
        private val priority: Int,
    ) : ThreadFactory {

        override fun newThread(r: Runnable?): Thread {
            return Thread(r, name).also { it.priority = priority }
        }
    }

    @JvmField
    val API_PRE_24 = Build.VERSION.SDK_INT < Build.VERSION_CODES.N

    @JvmField
    val API_PRE_26 = Build.VERSION.SDK_INT < Build.VERSION_CODES.O

    @JvmField
    val API_PRE_29 = Build.VERSION.SDK_INT < Build.VERSION_CODES.Q

    @JvmField
    val API_PRE_33 = Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU

    @JvmField
    val mediaTypeJson: MediaType = "application/json; charset=UTF-8".toMediaType()

}
