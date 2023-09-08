package jp.co.toukei.log.trustar.chat.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.ArrayMap
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ActionMode
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import com.stfalcon.imageviewer.StfalconImageViewer
import jp.co.toukei.log.lib.Const
import jp.co.toukei.log.lib.chains
import jp.co.toukei.log.lib.requirePermission
import jp.co.toukei.log.lib.util.BindHolder
import jp.co.toukei.log.lib.util.FixedAdapter
import jp.co.toukei.log.lib.util.ValueBind
import jp.co.toukei.log.lib.withBoundValue
import jp.co.toukei.log.trustar.Config
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.chat.ui.GalleryUI
import jp.co.toukei.log.trustar.chat.ui.ItemSelectUI
import jp.co.toukei.log.trustar.common.ToolbarActivity
import jp.co.toukei.log.trustar.defaultPermissionResultCheck
import jp.co.toukei.log.trustar.queryMediaImages
import jp.co.toukei.log.trustar.withGlide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.FileFilter

class Gallery : ToolbarActivity() {

    private val ui by lazy {
        GalleryUI(this).apply {
            setContentView(coordinatorLayout)
            toolbar.setTitle(R.string.camera_roll)
            setSupportActionBar(toolbar)
            sabCloseIconHomeAsUp()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 11) {
            if (defaultPermissionResultCheck(
                    permissions, grantResults, R.string.app_permission_settings_storage
                )
            ) {
                setContent()
            }
        } else if (requestCode == 12) {
            if (defaultPermissionResultCheck(
                    permissions, grantResults, R.string.app_permission_settings_image
                )
            ) {
                setContent()
            }
        }
    }

    private class Adapter(
        list: List<ItemSelectUI.Item>,
    ) : FixedAdapter<ItemSelectUI.Item>(list) {

        inner class Holder(view: View, v: ValueBind<ItemSelectUI.Item>) :
            BindHolder<ItemSelectUI.Item>(view, v) {

            val itemDetails = object : ItemDetailsLookup.ItemDetails<String>() {
                override fun getSelectionKey(): String? {
                    return valueBind.boundValue?.let { getKeyByItem(it) }
                }

                override fun getPosition(): Int {
                    return bindingAdapterPosition
                }
            }
        }

        private val positionIndex = ArrayMap<String, Int>(list.size).apply {
            list.forEachIndexed { index, it -> put(it.uri.toString(), index) }
        }

        var actionMode: ActionMode? = null
            set(value) {
                val f = field
                field = value
                f?.finish()
                notifyAllItem(value != null)
            }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int,
        ): BindHolder<ItemSelectUI.Item> {
            val it = ItemSelectUI(parent.context)
            val h = Holder(it.view, it)
            it.apply {
                view.setOnClickListener {
                    if (actionMode == null) withBoundValue {
                        StfalconImageViewer
                            .Builder(it.context, list) { view, image ->
                                val load = image.uri
                                view.withGlide()
                                    .load(load)
                                    .fitCenter()
                                    .into(view)
                            }
                            .withBackgroundColor(Color.BLACK)
                            .withTransitionFrom(img)
                            .withStartPosition(h.bindingAdapterPosition)
                            .show()
                    }
                }
            }
            return h
        }

        fun notifyAllItem(checkMode: Boolean) {
            list.forEach {
                when {
                    checkMode -> if (it.check === null) it.check = false
                    else -> it.check = null
                }
            }
            notifyItemRangeChanged(0, itemCount, SelectionTracker.SELECTION_CHANGED_MARKER)
        }

        fun selectItem(key: String, selected: Boolean) {
            getItemByKey(key)?.check = selected
        }

        fun getItemByKey(key: String): ItemSelectUI.Item? {
            return list.getOrNull(getPositionByKey(key))
        }

        fun getPositionByKey(key: String): Int {
//            return list.indexOfFirst { it.uri.toString() == key }
            return positionIndex[key] ?: -1
        }

        fun getKeyByPosition(position: Int): String? {
            return list.getOrNull(position)?.let { getKeyByItem(it) }
        }

        fun getKeyByItem(item: ItemSelectUI.Item): String? {
            return item.uri.toString()
        }
    }

    private suspend fun getUriList(): List<ItemSelectUI.Item> {
        return withContext(Dispatchers.IO) {
            val local = Config.localGalleryDir
                .listFiles(FileFilter { it.isFile })
                ?.sortedByDescending { it.lastModified() }
                ?.map { it.toUri() }
                ?: emptyList()
            local.chains(contentResolver.queryMediaImages())
                .map { ItemSelectUI.Item(it) }
        }
    }

    private fun setContent() {
        lifecycleScope.launchWhenCreated {
            setContent(getUriList())
        }
    }

    private fun setContent(uriList: List<ItemSelectUI.Item>) {
        val recyclerView = ui.list

        val adapter = Adapter(uriList)

        recyclerView.adapter = adapter

        val tracker = SelectionTracker
            .Builder(
                "id",
                recyclerView,
                object : ItemKeyProvider<String>(SCOPE_MAPPED) {
                    override fun getKey(position: Int): String? {
                        return adapter.getKeyByPosition(position)
                    }

                    override fun getPosition(key: String): Int {
                        return adapter.getPositionByKey(key)
                    }
                },
                object : ItemDetailsLookup<String>() {
                    override fun getItemDetails(e: MotionEvent): ItemDetails<String>? {
                        return recyclerView.findChildViewUnder(e.x, e.y)
                            ?.let { recyclerView.getChildViewHolder(it) as? Adapter.Holder }
                            ?.itemDetails
                    }
                },
                StorageStrategy.createStringStorage()
            )
            .withSelectionPredicate(SelectionPredicates.createSelectAnything())
            .build()

        val selectionObserver =
            object : SelectionTracker.SelectionObserver<String>(), ActionMode.Callback {

                override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                    if (item?.itemId == R.string.determine) {
                        val selected = tracker.selection
                        val arrList = selected.toCollection(ArrayList(selected.size()))
                        finishWithResult(arrList)
                    }
                    return true
                }

                override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                    menu?.add(Menu.NONE, R.string.determine, Menu.NONE, R.string.determine)
                    return true
                }

                override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                    return false
                }

                override fun onDestroyActionMode(mode: ActionMode?) {
                    tracker.clearSelection()
                }


                override fun onItemStateChanged(key: String, selected: Boolean) {
                    adapter.selectItem(key, selected)
                }

                @SuppressLint("RestrictedApi")
                override fun onSelectionCleared() {
                    adapter.actionMode = null
                }

                override fun onSelectionChanged() {
                    val size = tracker.selection.size()
                    if (size == 0) {
                        onSelectionCleared()
                    } else {
                        if (adapter.actionMode == null) {
                            adapter.actionMode = startSupportActionMode(this)
                        }
                        adapter.actionMode?.title =
                            resources.getQuantityString(R.plurals.item_selected, size, size)
                    }
                }
            }
        tracker.addObserver(selectionObserver)
    }

    override fun onCreate1(savedInstanceState: Bundle?) {
        if (Const.API_PRE_33) {
            if (requirePermission(Manifest.permission.READ_EXTERNAL_STORAGE, 11)) {
                setContent()
            }
        } else {
            if (requirePermission(Manifest.permission.READ_MEDIA_IMAGES, 12)) {
                setContent()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add(Menu.NONE, R.string.add, 1, R.string.add)?.apply {
            setIcon(R.drawable.round_photo_library_24)
            setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.string.add) {
            val i = Intent(Intent.ACTION_GET_CONTENT)
                .setType("image/*")
                .putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            runCatching { startActivityForResult(i, 1) }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            val uris = data.clipData
                ?.let {
                    (0 until it.itemCount).mapNotNullTo(ArrayList()) { i ->
                        it.getItemAt(i).uri?.toString()
                    }
                }
                ?: data.data?.toString()?.let { arrayListOf(it) }
            if (uris != null) {
                finishWithResult(uris)
            }
        }
    }

    private fun finishWithResult(uris: ArrayList<String>) {
        setResult(Activity.RESULT_OK, Intent().putStringArrayListExtra(INTENT_DATA_KEY, uris))
        finish()
    }

    companion object {

        @JvmStatic
        val INTENT_DATA_KEY = "INTENT_DATA_KEY"
    }
}
