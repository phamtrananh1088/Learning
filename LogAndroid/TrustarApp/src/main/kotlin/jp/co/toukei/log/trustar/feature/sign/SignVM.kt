package jp.co.toukei.log.trustar.feature.sign

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import jp.co.toukei.log.lib.pure
import jp.co.toukei.log.trustar.common.CommonViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import third.Result
import java.io.File
import java.io.RandomAccessFile

class SignVM : CommonViewModel() {

    private val _processed = MutableLiveData<Result<File>>()

    @JvmField
    val processed: LiveData<Result<File>> = _processed

    fun saveBitmap(raw: Bitmap, limit: Float, file: File) {
        _processed.value = Result.Loading

        vmScope.launch(Dispatchers.IO) {
            if (raw.pure(Color.WHITE)) {
                RandomAccessFile(file, "rw").use {
                    it.setLength(0)
                }
            } else {
                val scale = if (limit > 0) limit / maxOf(raw.width, raw.height) else 1F
                val target = if (scale >= 1) raw else Bitmap.createBitmap(
                    raw,
                    0, 0,
                    raw.width, raw.height,
                    Matrix().apply { setScale(scale, scale) }, false
                )
                file.outputStream().use {
                    target.compress(Bitmap.CompressFormat.PNG, 100, it)
                }
            }
            _processed.postValue(Result.Value(file))
        }
    }
}
