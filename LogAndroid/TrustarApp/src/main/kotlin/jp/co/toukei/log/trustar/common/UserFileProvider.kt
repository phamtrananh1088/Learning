package jp.co.toukei.log.trustar.common

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.pm.ProviderInfo
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import jp.co.toukei.log.lib.child
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.user.UserInfo
import java.io.File

class UserFileProvider : ContentProvider() {

    override fun onCreate(): Boolean = true

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?,
    ): Cursor? {
        val file = getFileFromUri(uri)
        if (!file.canRead()) return null
        val p = projection ?: arrayOf(OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE)
        val ls = p.mapNotNull<String, Pair<String, Any>> {
            when (it) {
                OpenableColumns.DISPLAY_NAME -> {
                    OpenableColumns.DISPLAY_NAME to uri.lastPathSegment.orEmpty()
                }

                OpenableColumns.SIZE -> {
                    OpenableColumns.SIZE to file.length()
                }

                else -> null
            }
        }
        val cols = arrayOfNulls<String>(ls.size)
        val values = arrayOfNulls<Any>(ls.size)
        ls.forEachIndexed { index, (k, v) ->
            cols[index] = k
            values[index] = v
        }
        return MatrixCursor(cols, 1).apply { addRow(values) }
    }

    override fun openFile(uri: Uri, mode: String): ParcelFileDescriptor? {
        val file = getFileFromUri(uri)
        val m = ParcelFileDescriptor.parseMode(mode)
        return ParcelFileDescriptor.open(file, m)
    }

    override fun getType(uri: Uri): String {
        val mime = uri.lastPathSegment
            ?.substringAfterLast('.', "")
            ?.let { MimeTypeMap.getSingleton().getMimeTypeFromExtension(it) }
        return mime ?: "application/octet-stream"
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        throw UnsupportedOperationException()
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return if (getFileFromUri(uri).delete()) 1 else 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?,
    ): Int {
        throw UnsupportedOperationException()
    }

    override fun attachInfo(context: Context?, info: ProviderInfo) {
        super.attachInfo(context, info)
        if (info.exported) throw SecurityException("Provider must not be exported")
        if (!info.grantUriPermissions) throw SecurityException("Provider must grant uri permissions")
    }

    private fun getFileFromUri(uri: Uri): File {
        val user = Current.userInfo
        val s = uri.pathSegments
        require(s.size == 3) { "Failed to resolve: $uri" }
        val route = s[0]
        val key = s[1]
        return when (route) {
            "1" -> UserInfo.cacheFileByKey(user, key).file
            "2" -> UserInfo.userLocalFileDir(user).child(key)
            else -> throw IllegalArgumentException("Failed to resolve: $uri")
        }
    }

    companion object {

        @JvmStatic
        fun getUriForCacheFile(fileKey: String, name: String, authority: String): Uri {
            return Uri.Builder().scheme("content")
                .authority(authority)
                .appendPath("1")
                .appendPath(fileKey)
                .appendPath(name)
                .build()
        }

        @JvmStatic
        fun getUriForLocalFile(id: String, name: String, authority: String): Uri {
            return Uri.Builder().scheme("content")
                .authority(authority)
                .appendPath("2")
                .appendPath(id)
                .appendPath(name)
                .build()
        }
    }
}
