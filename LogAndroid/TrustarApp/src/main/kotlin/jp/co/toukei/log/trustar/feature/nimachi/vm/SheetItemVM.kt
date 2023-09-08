package jp.co.toukei.log.trustar.feature.nimachi.vm

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.toLiveData
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.processors.BehaviorProcessor
import jp.co.toukei.log.lib.*
import jp.co.toukei.log.lib.util.Pending
import jp.co.toukei.log.trustar.Config
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.common.CommonViewModel
import jp.co.toukei.log.trustar.db.user.entity.IncidentalHeader
import jp.co.toukei.log.trustar.feature.nimachi.data.IncidentalItemDataDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import third.Result
import java.io.File
import java.util.concurrent.TimeUnit

class SheetItemVM : CommonViewModel() {

    private val header = BehaviorProcessor.create<IncidentalHeader>()
    private val repo = Current.userRepository.incidentalRepo
    private val picStore = repo.picStore

    private val itemData: Flowable<IncidentalItemDataDB> = header
        .distinctUntilChanged()
        .switchMap {
            repo.sheetDetailsByUUID(it.uuid)
        }
        .replayThenAutoConnect(disposableContainer)

    @JvmField
    val itemDataLiveData = itemData.toLiveData()

    fun setHeader(item: IncidentalHeader) {
        header.offer(item)
    }

    fun getPendingSign(k: Int): File {
        return tmpFilePending.getPending(k)
    }

    fun commitPendingSign(k: Int) {
        val store = picStore
        val f = tmpFilePending.commit(k) ?: return
        val header = header.value ?: return
        vmScope.launch(Dispatchers.IO) {
            processing.postValue(true)
            val oldId = header.localSignatureId()
            store.removeFiles(listOf(oldId))
            val isImg = imageValidation(f)
            if (isImg) {
                runCatching {
                    val b = f.readBytes()
                    val h = repo.saveIncidentalHeaderWithPic(header, b)

                    val nId = h.localSignatureId()
                    store.store(nId, b.inputStream())
                    setHeader(h)
                }
            } else {
                repo.saveIncidentalHeaderWithPic(header, null)
            }
            processing.postValue(false)
        }
    }

    @JvmField
    val signFile: LiveData<Result<Uri>> = Flowable
        .combineLatest(itemData, header, rxBiFunctionTakeLeft())
        .observeOnIO()
        .switchMap {
            val h = it.item.sheet
            Flowable.just<Result<Uri>>(Result.Loading)
                .concatWith(
                    picStore.getOrDownload(h.localSignatureId(), h.picId)
                        .onErrorComplete()
                        .defaultIfEmpty(Uri.EMPTY)
                        .map { u -> Result.Value(u) }
                )
        }
        .throttleWithTimeout(100, TimeUnit.MILLISECONDS)
        .replayThenAutoConnect(disposableContainer)
        .toLiveData()
        .distinctBy { old, new, _ ->
            old?.javaClass === new.javaClass && old.value() == new.value()
        }

    private val tmpDir by lazy { Config.tmpDir.createTempDirInDir() }

    private val tmpFilePending = object : Pending<Int, File>() {
        override fun createPending(k: Int): File {
            return tmpDir.createTempFileInDir()
        }
    }

    //handling sign file.
    @JvmField
    val processing: MutableLiveData<Boolean> = MutableLiveData()

    override fun onCleared() {
        tmpDir.deleteQuickly()
    }
}
