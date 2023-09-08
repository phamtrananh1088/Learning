package jp.co.toukei.log.trustar.common

import jp.co.toukei.log.lib.createTempDirInDir
import jp.co.toukei.log.lib.createTempFileInDir
import jp.co.toukei.log.lib.deleteQuickly
import jp.co.toukei.log.lib.makeDirs
import jp.co.toukei.log.trustar.Config

open class TmpFileViewModel : CommonViewModel() {

    protected val tmpDir = Config.tmpDir.makeDirs().createTempDirInDir()
    fun createTmpFile() = tmpDir.createTempFileInDir()
    fun createTmpDir() = tmpDir.createTempDirInDir()

    override fun onCleared() {
        super.onCleared()
        tmpDir.deleteQuickly()
    }
}
