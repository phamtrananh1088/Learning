package jp.co.toukei.log.trustar.rest.model

import annotation.Keep
import annotation.Para

class FileKey2 @Keep constructor(
    @Para("fileKey") fileKey2: String,
) {
    @JvmField
    val key = FileKey(fileKey2)

    @JvmField
    val realFileKey = fileKey2.split(',').getOrNull(1) ?: fileKey2
}
