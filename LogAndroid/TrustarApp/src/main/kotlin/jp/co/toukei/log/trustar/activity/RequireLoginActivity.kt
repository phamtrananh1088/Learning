package jp.co.toukei.log.trustar.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import jp.co.toukei.log.lib.startActivityClearAndNewTask
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.readUserInfoFromLocal
import jp.co.toukei.log.trustar.user.LoggedUser

abstract class RequireLoginActivity : AppCompatActivity() {

    private fun user(): LoggedUser? {
        val u = Current.user
        if (u != null) {
            return u
        }
        val user = readUserInfoFromLocal()
        if (user != null) {
            Current.login(user)
        }
        return Current.user
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val u = user()
        if (u == null) {
            startActivityClearAndNewTask<LoginActivity>()
            finish()
        } else {
            onCreate1(savedInstanceState, u)
        }
    }

    open fun onCreate1(savedInstanceState: Bundle?, user: LoggedUser) {
        onCreate1(savedInstanceState)
    }

    @Deprecated("")
    open fun onCreate1(savedInstanceState: Bundle?) {
    }
}
