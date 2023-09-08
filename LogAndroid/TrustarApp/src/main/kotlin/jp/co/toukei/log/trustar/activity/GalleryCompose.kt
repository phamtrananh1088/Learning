@file:OptIn(ExperimentalGlideComposeApi::class)

package jp.co.toukei.log.trustar.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.sebaslogen.resaca.rememberScoped
import jp.co.toukei.log.common.compose.M3ContentTheme
import jp.co.toukei.log.lib.compose.NormalTopBar
import jp.co.toukei.log.lib.mapToArrayList
import jp.co.toukei.log.lib.startAppSettings
import jp.co.toukei.log.trustar.Config
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.compose.ReadImageMediaPermissionCheck
import jp.co.toukei.log.trustar.compose.ViewImage
import jp.co.toukei.log.trustar.compose.rememberScopedMutableStateOf
import jp.co.toukei.log.trustar.deprecated.intentFor
import jp.co.toukei.log.trustar.queryMediaImages
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.FileFilter


class GalleryCompose : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            M3ContentTheme {
                Gallery(
                    onResult = {
                        setResult(Activity.RESULT_OK, toResult(it))
                        finish()
                    },
                    naviBack = {
                        finish()
                    }
                )
            }
        }
    }

    companion object {

        private const val name = "data"

        fun intentForStartActivity(context: Context): Intent {
            return context.intentFor<GalleryCompose>()
        }

        fun resolveResult(result: ActivityResult): List<Uri>? {
            if (result.resultCode != Activity.RESULT_OK) return null
            return result.data?.getStringArrayListExtra(name)?.map(String::toUri)
        }

        private fun toResult(list: List<Uri>): Intent {
            return Intent().putStringArrayListExtra(name, list.mapToArrayList(Uri::toString))
        }
    }
}


private data class ImgCheck(
    val checked: Boolean,
    val uri: Uri,
)

@Composable
fun Gallery(
    modifier: Modifier = Modifier,
    onResult: (List<Uri>) -> Unit,
    naviBack: () -> Unit,
) {
    val mediaImages: SnapshotStateList<ImgCheck> = rememberScoped {
        mutableStateListOf()
    }
    val localImages = rememberScoped {
        Config.localGalleryDir.listFiles(FileFilter { it.isFile })
            ?.sortedByDescending { it.lastModified() }
            ?.map { ImgCheck(false, it.toUri()) }
            .orEmpty()
            .toMutableStateList()
    }
    var readMediaImages by rememberScoped {
        mutableStateOf(true)
    }

    if (readMediaImages) {
        val context = LocalContext.current
        ReadImageMediaPermissionCheck(
            startAppSettings = context::startAppSettings,
            cancel = {
                readMediaImages = false
            }
        ) {
            mediaImages.clear()
            mediaImages += context.contentResolver.queryMediaImages().map { u ->
                ImgCheck(false, u)
            }.toMutableStateList()
            readMediaImages = false
        }
    }

    val list = localImages.toList() + mediaImages.toList()

    val checked = produceState(initialValue = emptyList(), list) {
        value = withContext(Dispatchers.IO) {
            list.mapNotNull { if (it.checked) it.uri else null }
        }
    }.value

    Scaffold(
        modifier = modifier,
        topBar = {
            val size = checked.size
            NormalTopBar(
                title = if (size > 0) {
                    pluralStringResource(R.plurals.item_selected, size, size)
                } else {
                    stringResource(id = R.string.camera_roll)
                },
                navigationIcon = {
                    IconButton(onClick = naviBack) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    AnimatedContent(
                        targetState = size > 0,
                        label = ""
                    ) {
                        if (it) {
                            IconButton(onClick = {
                                onResult(checked)
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Done,
                                    contentDescription = null
                                )
                            }
                        } else {
                            val extLauncher = rememberLauncherForActivityResult(
                                contract = ActivityResultContracts.GetMultipleContents(),
                                onResult = onResult
                            )
                            IconButton(onClick = {
                                extLauncher.launch("image/*")
                            }) {
                                Icon(
                                    imageVector = Icons.Default.PhotoLibrary,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                }
            )
        }
    ) {
        val space = Arrangement.spacedBy(2.dp)

        val checkboxColors: CheckboxColors = CheckboxDefaults.colors(
            uncheckedColor = Color.White
        )
        val checkboxBg: Color = Color.Black.copy(alpha = 0.1F)

        var viewImage by rememberScopedMutableStateOf<Uri>()

        val view: (ImgCheck) -> Unit = { viewImage = it.uri }

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .padding(it),
        ) {
            LazyVerticalGrid(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = space,
                horizontalArrangement = space,
                columns = GridCells.Fixed(Integer.max(1, (maxWidth / 144.dp).toInt())),
            ) {
                imageList(localImages, checkboxColors, checkboxBg, view)
                imageList(mediaImages, checkboxColors, checkboxBg, view)
            }
        }

        viewImage?.let { v ->
            ViewImage(v) {
                viewImage = null
            }
        }
    }
}


private fun LazyGridScope.imageList(
    list: SnapshotStateList<ImgCheck>,
    checkboxColors: CheckboxColors,
    checkboxBg: Color,
    onClick: (ImgCheck) -> Unit,
) {
    itemsIndexed(list) { index, img ->
        val p by animateFloatAsState(
            if (img.checked) 0.9F else 1F, label = "",
        )
        Box(
            modifier = Modifier
                .aspectRatio(1F)
                .clickable {
                    onClick(img)
                },
        ) {
            GlideImage(
                modifier = Modifier
                    .scale(p),
                model = img.uri,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                loading = placeholder(R.drawable.image_placeholder),
            )
            Checkbox(
                modifier = Modifier
                    .background(checkboxBg)
                    .align(Alignment.TopEnd)
                    .padding(4.dp),
                colors = checkboxColors,
                checked = img.checked,
                onCheckedChange = {
                    list[index] = img.copy(checked = !img.checked)
                }
            )
        }
    }
}
