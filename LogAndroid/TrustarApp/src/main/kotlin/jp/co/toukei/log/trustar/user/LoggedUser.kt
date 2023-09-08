package jp.co.toukei.log.trustar.user

import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import jp.co.toukei.log.lib.child
import jp.co.toukei.log.lib.ctx.Ctx
import jp.co.toukei.log.lib.inTransaction
import jp.co.toukei.log.lib.makeDirs
import jp.co.toukei.log.lib.util.ClosableContainer
import jp.co.toukei.log.lib.util.FusedLocationHelperRx
import jp.co.toukei.log.trustar.Config
import jp.co.toukei.log.trustar.common.FilesInDir
import jp.co.toukei.log.trustar.common.copyToNewDir
import jp.co.toukei.log.trustar.db.image.ImageSendingDB
import jp.co.toukei.log.trustar.db.result.db.ResultDB
import jp.co.toukei.log.trustar.db.result.entity.CommonBinResult
import jp.co.toukei.log.trustar.db.result.entity.CommonCollectionResult
import jp.co.toukei.log.trustar.db.result.entity.CommonDeliveryChart
import jp.co.toukei.log.trustar.db.result.entity.CommonIncidentalHeaderResult
import jp.co.toukei.log.trustar.db.result.entity.CommonIncidentalTimeResult
import jp.co.toukei.log.trustar.db.result.entity.CommonNotice
import jp.co.toukei.log.trustar.db.result.entity.CommonWorkResult
import jp.co.toukei.log.trustar.db.user.db.UserDB
import jp.co.toukei.log.trustar.setThenGetPending
import okhttp3.internal.closeQuietly

class LoggedUser(
    @JvmField val userInfo: UserInfo,
    @JvmField val token: String,
    @JvmField val resultDb: ResultDB,
    @JvmField val imageDB: ImageSendingDB,
) {

    private val closableContainer = ClosableContainer()

    val locationHelper = FusedLocationHelperRx()

    @JvmField
    val userDB: UserDB = Room.databaseBuilder(
        Ctx.context,
        UserDB::class.java,
        userInfo.homeDir.child("user_db").path
    ).fallbackToDestructiveMigration()
        .addCallback(object : androidx.room.RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                db.execSQL(UserDB.CREATE_INDEX_INCIDENTAL_HEADER_SHEET)
                db.execSQL(UserDB.CREATE_INDEX_INCIDENTAL_TIME_SHEET)
            }
        })
        .build()

    val imageStore: CachedPicStore = userInfo.homeDir
        .child("images")
        .makeDirs()
        .let { CachedPicStore(it, this) }

    @JvmField
    val binLocationTask = BinLocationTask(
        this,
    )

    fun commitCommonDb() {
        commitNoticeToCommon()
        commitBinToCommon()
        commitIncidentalToCommon()
        commitCollectionResultToCommon()
        commitDeliveryChartToCommon()
    }

    fun commitBinToCommon() {
        userDB.inTransaction {
            val wr = binDetailDao()
            val br = binHeaderDao()

            wr.setPending()  //sync = 1
            br.setSyncPendingByWorkResult() //sync = 1
            br.setPending()  //sync = 1

            val pw = wr.getPending()
            resultDb.commonWorkResultDao().insertOrReplace(
                pw.map {
                    it.setSyncFinished()
                    CommonWorkResult(userInfo, it)
                }
            )

            wr.updateOrIgnore(pw)  //sync = 2

            val pb = br.getPending()
            resultDb.commonBinResultDao().insertOrReplace(
                pb.map {
                    it.setSyncFinished()
                    CommonBinResult(userInfo, it)
                }
            )
            br.updateOrIgnore(pb)  //sync = 2
        }
    }

    fun commitCollectionResultToCommon() {
        userDB.inTransaction {
            val d = collectionResultDao()
            val ls = d.setThenGetPending()
            resultDb.commonCollectionResultDao().insertOrReplace(
                ls.map {
                    it.setSyncFinished()
                    CommonCollectionResult(userInfo, it)
                }
            )
            d.updateOrIgnore(ls)
        }
    }

    fun commitNoticeToCommon() {
        userDB.inTransaction {
            noticeDao().apply {
                val pending = setThenGetPending()//sync = 1
                val target = pending.map {
                    it.setSyncFinished()
                    CommonNotice(userInfo, it)
                }
                resultDb.commonNoticeDao().insertOrReplace(target)
                updateOrIgnore(pending)  //sync = 2
            }
        }
    }

    fun commitIncidentalToCommon() {
        userDB.inTransaction {
            val h = incidentalHeaderDao()
            val t = incidentalTimeDao()

            val hp = h.setThenGetPending()
            resultDb.commonIncidentalHeaderResultDao().insertOrReplace(
                hp.map {
                    it.setSyncFinished()
                    CommonIncidentalHeaderResult(userInfo, it)
                }
            )
            h.updateOrIgnore(hp)

            val tp = t.setThenGetPending()
            resultDb.commonIncidentalTimeResultDao().insertOrReplace(
                tp.map {
                    it.setSyncFinished()
                    CommonIncidentalTimeResult(userInfo, it)
                }
            )
            t.updateOrIgnore(tp)
        }
    }

    fun commitDeliveryChartToCommon() {
        userDB.inTransaction {
            deliveryChartDao().apply {
                val pending = setThenGetPending()
                val target = pending.map {
                    it.setSyncFinished()
                    CommonDeliveryChart(
                        userInfo = userInfo,
                        deliveryChart = it,
                        images = it.images.mapNotNull { img ->
                            // ignore possible IO err..
                            runCatching {
                                img.copy(
                                    dbStoredFile = img.dbStoredFile.copyToNewDir(
                                        fromDir = userChartSyncDir,
                                        toDir = Config.commonChartSyncDir
                                    )
                                )
                            }.getOrNull()
                        }
                    )
                }
                resultDb.commonDeliveryChartDao().insertOrReplace(target)
                updateOrIgnore(pending)  //sync = 2
            }
        }
    }

    fun close() {
        imageStore.closeQuietly()
        binLocationTask.dispose()
        userDB.close()
        closableContainer.closeQuietly()
        locationHelper.forceStopLocationUpdates()
    }


    @JvmField
    val userChartSyncDir = FilesInDir(UserInfo.userLocalFileDir(userInfo).child("chart"))
}
