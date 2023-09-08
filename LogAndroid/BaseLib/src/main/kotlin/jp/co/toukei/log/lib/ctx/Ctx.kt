package jp.co.toukei.log.lib.ctx

import android.annotation.SuppressLint
import android.content.Context

@SuppressLint("StaticFieldLeak")
object Ctx {

    lateinit var context: Context internal set
}
