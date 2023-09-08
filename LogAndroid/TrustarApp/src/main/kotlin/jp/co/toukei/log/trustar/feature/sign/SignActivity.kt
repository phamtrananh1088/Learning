package jp.co.toukei.log.trustar.feature.sign

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import jp.co.toukei.log.lib.common.indefiniteSnackbar
import jp.co.toukei.log.lib.common.toast
import jp.co.toukei.log.lib.getViewModel
import jp.co.toukei.log.lib.loadingState
import jp.co.toukei.log.lib.observeNonNull
import jp.co.toukei.log.lib.runOnValue
import jp.co.toukei.log.trustar.deprecated.intentFor
import java.io.File

/**
 * 署名
 */
class SignActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val path = intent?.getStringExtra(PNG_PATH) ?: return run {
            toast("err")
            finish()
        }
        val limitSize = intent?.getIntExtra("limit_size", 2048) ?: 2048
        val file = File(path)

        val vm = getViewModel<SignVM>()
        val ui = SignActivityUI(this)
        setContentView(ui.view)

        val onClick = View.OnClickListener {
            /*署名・確定押下時に起動されるイベント*/
            val bitmap = ui.painter.run {
                Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).also {
                    draw(Canvas(it))
                }
            }
            vm.saveBitmap(bitmap, limitSize.toFloat(), file)
        }

        vm.processed.observeNonNull(this) {
            it.loadingState { l ->
                if (l) {
                    ui.view.indefiniteSnackbar("processing...")
                    ui.done.setOnClickListener(null)
                }
            }
            it.runOnValue {
                setResult(Activity.RESULT_OK, Intent().putExtra(PNG_PATH, it.absolutePath))
                finish()
            }
        }
        ui.done.setOnClickListener(onClick)
    }

    companion object {
        const val PNG_PATH = "png_path"

        fun resolveResultIntent(data: Intent?): File? {
            val f = data?.getStringExtra(PNG_PATH)?.let(::File)
            return if (f != null && f.exists()) f else null
        }

        fun intentForStartActivity(context: Context, path: String): Intent {
            return context.intentFor<SignActivity>(PNG_PATH to path)
        }
    }
}
