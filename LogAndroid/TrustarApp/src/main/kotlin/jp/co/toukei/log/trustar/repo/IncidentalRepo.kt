package jp.co.toukei.log.trustar.repo

import io.reactivex.rxjava3.core.Flowable
import jp.co.toukei.log.trustar.db.image.ImageSending
import jp.co.toukei.log.trustar.db.user.embedded.IncidentalListItem
import jp.co.toukei.log.trustar.db.user.entity.*
import jp.co.toukei.log.trustar.feature.nimachi.data.EditedTime
import jp.co.toukei.log.trustar.feature.nimachi.data.IncidentalItemDataDB
import jp.co.toukei.log.trustar.rest.post.PicPost
import jp.co.toukei.log.trustar.user.LoggedUser
import java.util.*

class IncidentalRepo(private val user: LoggedUser) {
    private val userDb = user.userDB
    private val imageDB = user.imageDB

    private val headerDao = userDb.incidentalHeaderDao()
    private val workDao = userDb.incidentalWorkDao()
    private val timeDao = userDb.incidentalTimeDao()

    @JvmField
    val picStore = user.imageStore

    fun sortedSheetList(
        allocationNo: String,
        allocationRowNo: Int
    ): Flowable<List<IncidentalListItem>> {
        return headerDao.selectSheetList(allocationNo, allocationRowNo)
            .map { l -> l.sortedBy { it.sheet.sheetNo } }
    }

    fun sheetDetailsByUUID(uuid: String): Flowable<IncidentalItemDataDB> {
        return headerDao.findByUUID(uuid)
            .switchMap {
                val ls = it.sheet
                val works = ls.workList
                Flowable.combineLatest(
                    workList().map { a -> a.filter { w -> works.contains(w.workCd) } },
                    timeDao.findByHeaderUUID(ls.uuid)
                ) { w, t -> IncidentalItemDataDB(it, w, t) }
            }
    }

    fun workList(): Flowable<List<IncidentalWork>> {
        return workDao.selectAll()
    }

    fun saveIncidentalHeader(header: IncidentalHeader) {
        headerDao.insertOrReplace(header)
        if (header.deleted) {
            header.picId?.let { imageDB.imageSendingDao().deleteByPicId(it) }
        }
    }

    fun saveIncidentalHeaderWithPic(header: IncidentalHeader, pic: ByteArray?): IncidentalHeader {
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
        saveIncidentalHeader(header)
        return header
    }

    fun saveTimeList(list: Iterable<IncidentalTime>) {
        timeDao.insertOrReplace(list)
    }

    fun addIncidentalHeader(
        binDetail: BinDetail,
        shipper: Shipper,
        workList: List<IncidentalWork?>
    ): IncidentalHeader {
        val header = IncidentalHeader(
            UUID.randomUUID().toString(),
            null,
            binDetail.allocationNo,
            binDetail.allocationRowNo,
            shipper.shipperCd,
            workList.mapNotNull { it?.workCd },
            0,
            null
        ).apply { recordChanged() }
        saveIncidentalHeader(header)
        return header
    }

    fun addTimeList(header: IncidentalHeader, list: Iterable<EditedTime>) {
        val added = list.map {
            IncidentalTime(
                UUID.randomUUID().toString(),
                header.uuid,
                header.sheetNo,
                null,
                it.type,
                it.targetBeginDate()?.date,
                it.targetEndDate()?.date
            ).apply { recordChanged() }
        }
        if (added.isNotEmpty())
            saveTimeList(added)
    }
}
