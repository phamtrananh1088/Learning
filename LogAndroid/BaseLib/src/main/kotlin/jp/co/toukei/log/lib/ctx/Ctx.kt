package jp.co.toukei.log.lib.ctx

import android.annotation.SuppressLint
import android.content.Context
import androidx.startup.Initializer

@SuppressLint("StaticFieldLeak")
class Ctx : Initializer<Ctx> {

    override fun create(context: Context): Ctx {
        Ctx.context = context.applicationContext
        return this
    }

    override fun dependencies() = emptyList<Nothing>()

    companion object {

        @JvmStatic
        lateinit var context: Context
            private set
    }
}
