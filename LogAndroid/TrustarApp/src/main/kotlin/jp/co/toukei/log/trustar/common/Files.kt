package jp.co.toukei.log.trustar.common

import androidx.compose.runtime.Immutable
import jp.co.toukei.log.lib.child
import jp.co.toukei.log.lib.deleteQuickly
import jp.co.toukei.log.lib.extensionIncludeDot
import jp.co.toukei.log.lib.makeDirs
import jp.co.toukei.log.lib.moveOrCopy
import jp.co.toukei.log.trustar.user.UserInfo
import java.io.File
import java.util.UUID

/** it's hard to track files, so use multiple dirs.. */
class FilesInDir(
    @JvmField
    val dir: File,
) {

    fun fileByName(filename: String): File {
        return dir.makeDirs().child(filename)
    }

    fun deleteAll() {
        dir.deleteQuickly()
        dir.mkdirs()
    }
}


/** local stored file or key of remote file. */
sealed class StoredFile {

    abstract fun fileRef(): FileRef

    @Immutable
    data class ByKey(
        @JvmField val key: String,
    ) : StoredFile() {
        override fun fileRef(): FileRef.ByKey {
            return FileRef.ByKey(key)
        }
    }

    @Immutable
    data class SyncDirFile(
        @JvmField val filesInDir: FilesInDir,
        @JvmField val filename: String,
    ) : StoredFile() {


        @JvmField
        val storedFile = filesInDir.fileByName(filename)

        val fileExtension: String
            get() = storedFile.extensionIncludeDot

        fun isSameFile(file: SyncDirFile): Boolean {
            return storedFile.canonicalPath == file.storedFile.canonicalPath
        }

        fun delete() {
            storedFile.delete()
        }

        override fun fileRef(): FileRef.ByStored {
            return FileRef.ByStored(this)
        }
    }
}

/** For local or remote file access. */
@Immutable
sealed class FileRef {

    @Immutable
    data class ByKey(
        @JvmField val key: String,
    ) : FileRef()

    @Immutable
    data class ByAnyFile(
        @JvmField val file: File,
        @JvmField val dotExt: String,
    ) : FileRef()

    @Immutable
    data class ByStored(
        @JvmField val stored: StoredFile.SyncDirFile,
    ) : FileRef()
}

fun copySyncDirFile(
    dir: FilesInDir,
    source: File,
    dotExt: String,
    retry: Int = 3
): StoredFile.SyncDirFile {
    val s = StoredFile.SyncDirFile(
        filesInDir = dir,
        filename = UUID.randomUUID().toString() + dotExt
    )
    if (!s.storedFile.exists()) {
        source.copyTo(s.storedFile)
        return s
    }
    if (retry <= 0) {
        throw Exception()
    } else {
        //try another filename...
        return copySyncDirFile(dir, source, dotExt, retry - 1)
    }
}

private fun createSyncDirFile(
    dir: FilesInDir,
    source: File,
    extension: String? = null,
    retry: Int = 3
): StoredFile.SyncDirFile {
    val s = StoredFile.SyncDirFile(
        filesInDir = dir,
        filename = UUID.randomUUID().toString() + extension.orEmpty()
    )
    if (source.moveOrCopy(s.storedFile)) return s
    if (retry <= 0) {
        throw Exception()
    } else {
        //try another filename...
        return createSyncDirFile(dir, source, extension, retry - 1)
    }
}

fun FileRef.storeFileByMove(dir: FilesInDir): StoredFile {
    return when (this) {
        is FileRef.ByAnyFile -> createSyncDirFile(dir, file, dotExt)
        is FileRef.ByKey -> StoredFile.ByKey(key)
        is FileRef.ByStored -> stored
    }
}

/** local file or cache of remote file */
fun FileRef.localFile(userInfo: UserInfo): File {
    return when (this) {
        is FileRef.ByAnyFile -> file
        is FileRef.ByKey -> UserInfo.cacheFileByKey(userInfo, key).file
        is FileRef.ByStored -> stored.storedFile
    }
}

private const val typeKey = 0
private const val typeFileName = 1

// file record in db. fu.....
@Immutable
data class DbFileRecord(
    val type: Int,
    val value: String,
)

fun DbFileRecord.asStoredFile(dir: FilesInDir): StoredFile {
    return when (type) {
        typeKey -> StoredFile.ByKey(value)
        typeFileName -> StoredFile.SyncDirFile(dir, value)
        else -> throw IllegalArgumentException()
    }
}

fun StoredFile.asDbStoredFile(): DbFileRecord {
    return when (this) {
        is StoredFile.ByKey -> DbFileRecord(typeKey, key)
        is StoredFile.SyncDirFile -> DbFileRecord(typeFileName, filename)
    }
}

fun DbFileRecord.fileRef(dir: FilesInDir): FileRef {
    return asStoredFile(dir).fileRef()
}

fun DbFileRecord.copyToNewDir(
    fromDir: FilesInDir,
    toDir: FilesInDir
): DbFileRecord {
    return when (val stored = asStoredFile(fromDir)) {
        is StoredFile.ByKey -> this
        is StoredFile.SyncDirFile -> {
            copySyncDirFile(
                dir = toDir,
                source = stored.storedFile,
                dotExt = stored.fileExtension,
            ).asDbStoredFile()
        }
    }
}
