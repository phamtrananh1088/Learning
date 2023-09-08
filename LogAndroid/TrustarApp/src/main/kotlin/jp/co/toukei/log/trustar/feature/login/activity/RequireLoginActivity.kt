package jp.co.toukei.log.trustar.feature.login.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import jp.co.toukei.log.lib.startActivityClearAndNewTask
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.readUserInfoFromLocal

abstract class RequireLoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Current.user != null) {
            onCreate1(savedInstanceState)
        } else {
            // block ui thread here.
            val user = readUserInfoFromLocal()
            if (user != null) {
                Current.login(user)
                onCreate1(savedInstanceState)
            } else {
                startActivityClearAndNewTask<LoginActivity>()
                finish()
            }
        }
    }

    abstract fun onCreate1(savedInstanceState: Bundle?)
}
