package jp.co.toukei.log.trustar.rest.model

import annotation.Keep
import annotation.Para

class FileKey @Keep constructor(
    @Para("fileKey") val fileKey: String,
)
