package jp.co.toukei.log.trustar.common

import android.view.MenuItem
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.feature.login.activity.RequireLoginActivity

abstract class ToolbarActivity : RequireLoginActivity() {

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    protected fun sabCloseIconHomeAsUp() {
        supportActionBar?.apply {
            setHomeAsUpIndicator(R.drawable.round_close_24)
            setDisplayHomeAsUpEnabled(true)
        }
    }
}
