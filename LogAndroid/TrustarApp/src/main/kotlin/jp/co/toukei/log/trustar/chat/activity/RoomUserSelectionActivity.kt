package jp.co.toukei.log.trustar.chat.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.LiveData
import jp.co.toukei.log.lib.common.backgroundColorResId
import jp.co.toukei.log.lib.replaceToTag
import jp.co.toukei.log.lib.util.IntPair
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.chat.ui.RoomUserSelection
import jp.co.toukei.log.trustar.common.ToolbarActivity
import jp.co.toukei.log.trustar.rest.model.Member
import splitties.views.dsl.core.frameLayout

abstract class RoomUserSelectionActivity : ToolbarActivity() {

    override fun onCreate1(savedInstanceState: Bundle?) {
        val l = frameLayout {
            backgroundColorResId = R.color.defaultBackground
            id = R.id.id_container
        }
        setContentView(l)

        if (savedInstanceState == null) {
            supportFragmentManager.apply {
                val t = beginTransaction()
                replaceToTag(
                    R.id.id_container,
                    RoomUserSelection::class.java.simpleName,
                    t,
                    ::RoomUserSelection
                ).commit()
            }
        }
    }

    private var optionsMenu: IntPair<Menu>? = null
    private var selectedUser: List<Member>? = null

    fun setSelection(list: List<Member>?) {
        selectedUser = list
        val defaultTitle = intent?.getStringExtra(ARG_TITLE)
        val size = list?.size ?: 0
        supportActionBar?.title = if (size == 0 && defaultTitle != null) defaultTitle
        else resources.getQuantityString(R.plurals.user_selected, size, size)
        optionsMenu?.let { it.value.findItem(it.int)?.isVisible = size > 0 }
    }

    abstract val excludedUser: LiveData<List<String>?>

    protected abstract fun createMenuItem(menu: Menu): MenuItem?

    protected abstract fun onMenuItem(selectedUser: List<Member>?)

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.let {
            createMenuItem(it)?.apply {
                isVisible = false
                optionsMenu = IntPair(itemId, it)
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == optionsMenu?.int) {
            onMenuItem(selectedUser)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        @JvmStatic
        val ARG_TITLE = "ARG_TITLE"

        @JvmStatic
        val ARG_ROOM_ID = "ARG_ROOM_ID"
    }
}
