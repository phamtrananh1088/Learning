package jp.co.toukei.log.trustar.repo

import android.net.Uri
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import jp.co.toukei.log.lib.imageValidation
import jp.co.toukei.log.lib.inTransaction
import jp.co.toukei.log.trustar.db.image.ImageSending
import jp.co.toukei.log.trustar.db.user.embedded.IncidentalListItem
import jp.co.toukei.log.trustar.db.user.entity.IncidentalHeader
import jp.co.toukei.log.trustar.db.user.entity.IncidentalTime
import jp.co.toukei.log.trustar.db.user.entity.IncidentalWork
import jp.co.toukei.log.trustar.other.IncidentalHeaderDetail
import jp.co.toukei.log.trustar.rest.post.PicPost
import jp.co.toukei.log.trustar.user.LoggedUser
import java.io.File
import java.util.Optional
import java.util.UUID

class IncidentalRepo(private val user: LoggedUser) {
    private val userDb = user.userDB
    private val imageDB = user.imageDB

    private val headerDao = userDb.incidentalHeaderDao()
    private val workDao = userDb.incidentalWorkDao()
    private val timeDao = userDb.incidentalTimeDao()

    private val cachedPicStore = user.imageStore

    fun sortedSheetList(
        allocationNo: String,
        allocationRowNo: Int,
    ): Flowable<List<IncidentalListItem>> {
        return headerDao.selectSheetList(allocationNo, allocationRowNo)
            .map { l ->
                l.sortedWith(compareBy(
                    { it.sheet.sheetNo },
                    { -it.sheet.localCreatedDate }
                ))
            }
    }

    fun workList(): Flowable<List<IncidentalWork>> {
        return workDao.selectAll()
    }

    fun incidentalHeader(uuid: String): Flowable<Optional<IncidentalHeader>> {
        return headerDao.selectByUUIDRx(uuid)
    }

    fun deleteIncidentalHeader(uuid: String) {
        val h = userDb.inTransaction {
            headerDao.selectByUUID(uuid)?.also {
                it.deleted = true
                headerDao.insertOrReplace(it)
            }
        }
        h?.picId?.let {
            imageDB.imageSendingDao().deleteByPicId(it)
        }
    }

    fun cacheSign(h: IncidentalHeader): Maybe<Uri> {
        return cachedPicStore.getOrDownload(h.localSignatureId(), h.picId)
    }

    fun sheetDetailsByUUID(uuid: String): Flowable<IncidentalHeaderDetail> {
        return headerDao.findByUUID(uuid)
            .switchMap {
                val ls = it.sheet
                val works = ls.workList
                Flowable.combineLatest(
                    workList().map { a -> a.filter { w -> works.contains(w.workCd) } },
                    timeDao.findByHeaderUUID(ls.uuid)
                ) { w, t -> IncidentalHeaderDetail(it.shipper, it.sheet, w, t) }
            }
    }

    fun saveIncidentalHeaderWithPic(uuid: String, file: File?) {
        val header = headerDao.selectByUUID(uuid) ?: return
        val oldId = header.localSignatureId()
        cachedPicStore.removeFiles(listOf(oldId))
        if (file != null && imageValidation(file)) {
            runCatching {
                val bytes = file.readBytes()
                saveImage(header, bytes)
                val newId = header.localSignatureId()
                cachedPicStore.store(newId, bytes.inputStream())
            }
        } else {
            saveImage(header, null)
        }
        headerDao.insertOrReplace(header)
    }

    private fun saveImage(header: IncidentalHeader, pic: ByteArray?) {
        val img = pic?.let {
            ImageSending(user.userInfo, it, PicPost.Content.IncidentalSign(header))
        }
        imageDB.imageSendingDao().apply {
            header.picId?.let { deleteByPicId(it) }
            img?.let { insert(it) }
        }
        header.apply {
            picId = img?.picId
            setSignStatus(picId != null)
        }
    }

    fun setShipperAndWorks(
        uuid: String,
        shipperCd: String,
        workCdList: List<String>,
    ): IncidentalHeader? {
        return userDb.inTransaction {
            headerDao.selectByUUID(uuid)?.also {
                it.shipperCd = shipperCd
                it.workList = workCdList
                headerDao.insertOrReplace(it)
            }
        }
    }

    fun addIncidentalHeader(
        allocationNo: String,
        allocationRowNo: Int,
        shipperCd: String,
        workCdList: List<String>,
    ): IncidentalHeader {
        val header = IncidentalHeader(
            uuid = UUID.randomUUID().toString(),
            sheetNo = null,
            allocationNo = allocationNo,
            allocationRowNo = allocationRowNo,
            shipperCd = shipperCd,
            workList = workCdList,
            status = 0,
            picId = null,
            localCreatedDate = System.currentTimeMillis(),
        ).apply { recordChanged() }
        headerDao.insertOrReplace(header)
        return header
    }

    fun saveTimeList(list: Iterable<IncidentalTime>) {
        timeDao.insertOrReplace(list)
    }
}
