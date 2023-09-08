package jp.co.toukei.log.trustar.viewmodel

import android.graphics.Bitmap
import android.net.Uri
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import jp.co.toukei.log.lib.compressDefaultJpeg
import jp.co.toukei.log.lib.ctx.Ctx
import jp.co.toukei.log.lib.downloadToFile
import jp.co.toukei.log.lib.extension
import jp.co.toukei.log.lib.optional
import jp.co.toukei.log.lib.orElseNull
import jp.co.toukei.log.lib.readToTmpFile
import jp.co.toukei.log.lib.switchMapOptionalOrEmpty
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.common.FileRef
import jp.co.toukei.log.trustar.common.FilesInDir
import jp.co.toukei.log.trustar.common.TmpFileViewModel
import jp.co.toukei.log.trustar.common.asDbStoredFile
import jp.co.toukei.log.trustar.common.fileRef
import jp.co.toukei.log.trustar.common.storeFileByMove
import jp.co.toukei.log.trustar.compose.ComposeData
import jp.co.toukei.log.trustar.db.AllocationRow
import jp.co.toukei.log.trustar.db.user.entity.DeliveryChart
import jp.co.toukei.log.trustar.repo.BinDetailRepo
import jp.co.toukei.log.trustar.repo.DeliveryChartRepo
import jp.co.toukei.log.trustar.user.LoggedUser
import jp.co.toukei.log.trustar.user.UserInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.io.File
import java.util.UUID

sealed class EditImage(val fileRef: FileRef) {

    class Exists(
        val imageFile: DeliveryChart.ChartImageFile,
        dir: FilesInDir
    ) : EditImage(imageFile.dbStoredFile.fileRef(dir))

    class Add(fileRef: FileRef, val saveTo: FilesInDir) : EditImage(fileRef)
}

class DeliveryChartVM(
    private val bin: ComposeData.BinRow,
    val user: LoggedUser,
) : TmpFileViewModel() {

    fun readImageToTmpJPEG(uri: List<Uri>): List<FileRef.ByAnyFile> {
        return runBlocking(Dispatchers.IO) {
            val r = Ctx.context.contentResolver
            uri.mapNotNull {
                r.readToTmpFile(it, tmpDir)?.let { f ->
                    val t = createTmpFile()
                    compressDefaultJpeg(
                        file = f,
                        target = t,
                        quality = 90,
                        max = 1920,
                        format = Bitmap.CompressFormat.JPEG
                    )?.run {
                        f.delete()
                        FileRef.ByAnyFile(file = t, dotExt = extension())
                    }
                }
            }
        }
    }

    private val deliveryChartRepo = DeliveryChartRepo(user.userDB)
    private val binDetailRepo = BinDetailRepo(user.userDB)


    fun download(fileRef: FileRef.ByKey): Single<File> {
        val file = UserInfo.cacheFileByKey(user.userInfo, fileRef.key).file
        return if (file.canRead()) {
            Single.just(file)
        } else {
            Current.onlineApi
                .getChartImage(userId = user.userInfo.userId, fileId = fileRef.key)
                .downloadToFile(file, tmpDir)
                .ignoreElements()
                .toSingleDefault(file)
        }
    }

    @JvmField
    val matchedChart = Flowable
        .defer {
            binDetailRepo.findBinDetail(bin.allocationNo, bin.allocationRowNo)
                .switchMapOptionalOrEmpty { bin ->
                    deliveryChartRepo.findChart(bin).map { o ->
                        val matched = o.orElseNull()
                        val chart = matched ?: DeliveryChart(
                            chartCd = UUID.randomUUID().toString(),
                            placeCd = bin.place.cd,
                            info = DeliveryChart.Info(
                                dest = bin.place.placeNameFull,
                                addr1 = bin.place.addr.orEmpty(),
                                addr2 = "",
                                tel = bin.placeExt?.tel1 ?: "",
                                carrier = "",
                                carrierTel = bin.placeExt?.tel2 ?: "",
                            ),
                            memos = emptyList(),
                            images = emptyList(),
                            lastAllocationRow = null,
                            extra = null,
                        )
                        Pair(chart, matched == null).optional()
                    }
                }
        }

    fun save(
        old: DeliveryChart,
        info: DeliveryChart.Info,
        memos: List<DeliveryChart.ChartMemo>,
        images: List<EditImage>,
    ): Boolean {
        return runBlocking(Dispatchers.IO) {
            runCatching {
                val imageFiles = images.map {
                    when (it) {
                        is EditImage.Exists -> it.imageFile
                        is EditImage.Add -> DeliveryChart.ChartImageFile(
                            dbStoredFile = it.fileRef.storeFileByMove(it.saveTo).asDbStoredFile(),
                            extra = null
                        )
                    }
                }
                val c = old.copy(
                    info = info,
                    memos = memos.filterNot {
                        it.label.isEmpty() && it.note.isEmpty()
                    },
                    images = imageFiles,
                    lastAllocationRow = AllocationRow(bin.allocationNo, bin.allocationRowNo),
                )
                c.recordChanged()
                deliveryChartRepo.save(c)

                Current.syncChart()
            }.isSuccess
        }
    }
}
