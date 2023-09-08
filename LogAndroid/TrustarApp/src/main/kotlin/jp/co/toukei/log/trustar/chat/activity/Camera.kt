package jp.co.toukei.log.trustar.chat.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.ScaleGestureDetector
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.RequiresPermission
import androidx.camera.core.AspectRatio
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.core.VideoCapture
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.net.toUri
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.distinctUntilChanged
import jp.co.toukei.log.lib.common.disable
import jp.co.toukei.log.lib.common.enable
import jp.co.toukei.log.lib.common.gone
import jp.co.toukei.log.lib.common.show
import jp.co.toukei.log.lib.common.toast
import jp.co.toukei.log.lib.createTempDirInDir
import jp.co.toukei.log.lib.createTempFileInDir
import jp.co.toukei.log.lib.deleteQuickly
import jp.co.toukei.log.lib.getSuccessResult
import jp.co.toukei.log.lib.getViewModel
import jp.co.toukei.log.lib.mainExecutor
import jp.co.toukei.log.lib.makeDirs
import jp.co.toukei.log.lib.moveOrCopy
import jp.co.toukei.log.lib.observeNonNull
import jp.co.toukei.log.lib.requirePermission
import jp.co.toukei.log.lib.util.OrientationLiveData
import jp.co.toukei.log.lib.util.ReuseDateFormatter
import jp.co.toukei.log.trustar.Config
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.chat.ui.CameraUI
import jp.co.toukei.log.trustar.common.GenericVM
import jp.co.toukei.log.trustar.defaultPermissionResultCheck
import java.io.File
import java.util.TimeZone
import kotlin.math.floor

class Camera : FragmentActivity() {

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (permissions.isEmpty()) return
        when (requestCode) {
            1 -> {
                if (defaultPermissionResultCheck(
                        permissions,
                        grantResults,
                        R.string.app_permission_settings_camera
                    )
                ) {
                    checkPermission()
                }
            }

            2 -> {
                if (defaultPermissionResultCheck(
                        permissions,
                        grantResults,
                        R.string.app_permission_settings_audio_record
                    )
                ) {
                    checkPermission()
                }
            }
        }
    }

    private interface Op {
        val tmpFile: File
        fun onSaved(isVideo: Boolean, ext: String)
    }

    private fun op(): Op? {
        val i = intent ?: return null
        val tmpFilePath = i.getStringExtra(ARG_TMP_FILE_PATH)
        return when {
            tmpFilePath != null -> {
                object : Op {
                    override val tmpFile: File = File(tmpFilePath)
                    override fun onSaved(isVideo: Boolean, ext: String) {
                        setResult(
                            Activity.RESULT_OK,
                            Intent().putExtra(INTENT_FILE_IS_VIDEO, isVideo)
                                .putExtra(INTENT_FILE_EXTENSION, ext)
                                .putExtra(INTENT_FILE_URI, tmpFile.toUri())
                        )
                        finish()
                    }
                }
            }

            i.getBooleanExtra(ARG_SAVE_TO_LOCAL, false) -> {
                object : Op {
                    override val tmpFile = tmp.makeDirs().createTempFileInDir()
                    override fun onSaved(isVideo: Boolean, ext: String) {
                        val d = if (isVideo) Config.localVideoDir else Config.localGalleryDir
                        val target = d.makeDirs().createTempFileInDir(
                            prefix = ReuseDateFormatter(
                                "yyyyMMddHHmm_",
                                Config.locale,
                                TimeZone.getDefault()
                            ).format(System.currentTimeMillis()),
                            suffix = ext
                        )
                        val ok = tmpFile.moveOrCopy(target)
                        setResult(
                            Activity.RESULT_OK,
                            if (ok)
                                Intent().putExtra(INTENT_FILE_IS_VIDEO, isVideo)
                                    .putExtra(INTENT_FILE_EXTENSION, ext)
                                    .putExtra(INTENT_FILE_URI, target.toUri())
                            else null
                        )
                        finish()
                    }
                }
            }

            else -> null
        }
    }

    private val chooseVM by lazy { getViewModel<GenericVM<Boolean>>("1") }
    private val ui by lazy { CameraUI(this) }

    private fun rotateByRotation(rotation: Int) {
        val r = when (rotation) {
            1 -> 90F
            2 -> 180F
            3 -> 270F
            else -> 360F
        }
        val old = ui.cameraImg.rotation
        val o = floor(old / 360) * 360 + r - old
        val f = old + if (o >= 180) o - 360 else o

        ui.cameraImg.animate().rotation(f).duration = 500
        ui.recordImg.animate().rotation(f).duration = 500
    }

    @SuppressLint("RestrictedApi")
    private fun checkPermission() {
        val isCamera = chooseVM.mutableLiveData.value ?: return
        if (!requirePermission(Manifest.permission.CAMERA, 1)) return
        if ((!isCamera) && !requirePermission(Manifest.permission.RECORD_AUDIO, 2)) return

        bindUseCase(isCamera)
    }

    private val processCameraProvider by lazy { ProcessCameraProvider.getInstance(this) }

    @SuppressLint("RestrictedApi")
    private fun bindUseCase(isCamera: Boolean) {
        val ooo: LifecycleOwner = this
        orientationLiveData.removeObservers(ooo)
        processCameraProvider.getSuccessResult(mainExecutor()) { provider ->
            provider.unbindAll()
            ui.flash.apply {
                setOnClickListener(null)
                gone()
            }
            ui.takePicture.apply {
                setOnClickListener(null)
                setImageDrawable(null)
                enable()
            }
            val op = op()
            val preview = Preview.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .build()
            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build()
            if (isCamera) {
                val imageCapture = ImageCapture.Builder()
                    .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                    .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                    .build()
                orientationLiveData.observeNonNull(ooo) {
                    imageCapture.targetRotation = it
                    rotateByRotation(it)
                }

                val c = try {
                    provider.bindToLifecycle(
                        this,
                        cameraSelector,
                        preview,
                        imageCapture
                    )
                } catch (e: Throwable) {
                    e.message?.let { toast(it) }
                    finish()
                    return@getSuccessResult
                }
                if (c.cameraInfo.hasFlashUnit()) {
                    ui.flash.show()
                    ui.flash.setOnClickListener { v ->
                        val t = !v.isActivated
                        imageCapture.flashMode = if (t) {
                            ImageCapture.FLASH_MODE_ON
                        } else {
                            ImageCapture.FLASH_MODE_OFF
                        }
                        v.isActivated = imageCapture.flashMode != ImageCapture.FLASH_MODE_OFF
                    }
                }
                setupZoom(c)
                if (op != null) {
                    ui.takePicture.apply {
                        setOnClickListener {
                            val output = ImageCapture.OutputFileOptions.Builder(op.tmpFile).build()
                            disable()
                            val s = object : ImageCapture.OnImageSavedCallback {
                                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                                    op.onSaved(false, ".jpg")
                                }

                                override fun onError(exception: ImageCaptureException) {
                                    exception.message?.let(::toast)
                                    enable()
                                    setColorFilter(Color.WHITE)
                                    setImageResource(R.drawable.round_fiber_manual_record_24)
                                }
                            }
                            imageCapture.takePicture(output, mainExecutor(), s)
                        }
                        setColorFilter(Color.WHITE)
                        setImageResource(R.drawable.round_fiber_manual_record_24)
                    }
                }
            } else {
                val videoCapture = VideoCapture.Builder()
                    .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                    .build()

                orientationLiveData.observeNonNull(ooo) {
                    videoCapture.setTargetRotation(it)
                    rotateByRotation(it)
                }

                val c = try {
                    provider.bindToLifecycle(
                        this,
                        cameraSelector,
                        preview,
                        videoCapture
                    )
                } catch (e: Throwable) {
                    e.message?.let { toast(it) }
                    return@getSuccessResult finish()
                }
                setupZoom(c)
                if (op != null) {
                    val l = object : View.OnClickListener {
                        @RequiresPermission(Manifest.permission.RECORD_AUDIO)
                        override fun onClick(v: View?) {
                            val that = this
                            val output = VideoCapture.OutputFileOptions.Builder(op.tmpFile).build()
                            val s = object : VideoCapture.OnVideoSavedCallback,
                                View.OnClickListener {
                                private var saveClicked = false
                                override fun onVideoSaved(outputFileResults: VideoCapture.OutputFileResults) {
                                    if (saveClicked) op.onSaved(true, ".mp4")
                                }

                                override fun onError(
                                    videoCaptureError: Int,
                                    message: String,
                                    cause: Throwable?,
                                ) {
                                    cause?.message?.let(::toast)
                                    ui.takePicture.apply {
                                        setColorFilter(Color.WHITE)
                                        setImageResource(R.drawable.round_fiber_manual_record_24)
                                        setOnClickListener(that)
                                        enable()
                                    }
                                }

                                override fun onClick(v: View?) {
                                    saveClicked = true
                                    videoCapture.stopRecording()
                                }
                            }
                            orientationLiveData.removeObservers(ooo)
                            videoCapture.startRecording(output, mainExecutor(), s)
                            ui.takePicture.apply {
                                setColorFilter(Color.RED)
                                setImageResource(R.drawable.round_stop_24)
                                setOnClickListener(s)
                                enable()
                            }
                        }
                    }
                    ui.takePicture.apply {
                        setColorFilter(Color.WHITE)
                        setImageResource(R.drawable.round_fiber_manual_record_24)
                        setOnClickListener(l)
                    }
                }
            }
            preview.setSurfaceProvider(ui.previewView.surfaceProvider)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupZoom(c: Camera) {
        val ci = c.cameraInfo
        val listener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                ci.zoomState.value?.let {
                    c.cameraControl.setZoomRatio(it.zoomRatio * detector.scaleFactor)
                }
                return true
            }
        }
        val gestureDetector = ScaleGestureDetector(this, listener)
        ui.previewView.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setBackgroundDrawable(ColorDrawable(Color.BLACK))
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val chooseLiveData = chooseVM.mutableLiveData

        chooseLiveData.distinctUntilChanged().observeNonNull(this) {
            ui.chooseCameraOrRecorder(it)
            checkPermission()
        }
        if (chooseLiveData.value == null) chooseLiveData.value = true

        val title = intent?.getStringExtra(ARG_TITLE)
        val videoEnabled = intent.getBooleanExtra(ARG_VIDEO_ENABLED, false)
        if (videoEnabled) {
            ui.toggle.show()
            ui.camera.setOnClickListener {
                chooseLiveData.value = true
            }
            ui.record.setOnClickListener {
                chooseLiveData.value = false
            }
        } else {
            ui.toggle.gone()
            chooseLiveData.value = true
        }
        ui.apply {
            setContentView(view)
            titleTextView.text = title
            close.setOnClickListener {
                finish()
            }
        }
    }

    private val orientationLiveData by lazy { OrientationLiveData(this) }

    private val tmp = Config.tmpDir.makeDirs().createTempDirInDir()

    override fun onDestroy() {
        super.onDestroy()
        tmp.deleteQuickly()
    }

    companion object {

        @JvmStatic
        val INTENT_FILE_URI = "INTENT_FILE_URI"

        @JvmStatic
        val INTENT_FILE_IS_VIDEO = "INTENT_FILE_IS_VIDEO"

        @JvmStatic
        val INTENT_FILE_EXTENSION = "INTENT_FILE_EXTENSION"

        @JvmStatic
        val ARG_SAVE_TO_LOCAL = "ARG_SAVE_TO_LOCAL"

        @JvmStatic
        val ARG_TMP_FILE_PATH = "ARG_TMP_FILE_PATH"

        @JvmStatic
        val ARG_VIDEO_ENABLED = "ARG_VIDEO_ENABLED"

        @JvmStatic
        val ARG_TITLE = "ARG_TITLE"

        fun resolveResultIntent(data: Intent?): CameraResult? {
            data ?: return null
            val v = data.getBooleanExtra(INTENT_FILE_IS_VIDEO, false)
            val ext = data.getStringExtra(INTENT_FILE_EXTENSION) ?: return null
            val uri = data.getParcelableExtra<Uri>(INTENT_FILE_URI) ?: return null
            return CameraResult(v, ext, uri)
        }

        class CameraResult(
            @JvmField val isVideo: Boolean,
            @JvmField val fileExt: String,
            @JvmField val fileUri: Uri,
        )
    }
}
