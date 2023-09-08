package jp.co.toukei.log.trustar.user

import annotation.Keep
import annotation.Para
import jp.co.toukei.log.lib.child
import jp.co.toukei.log.lib.encodeToBase64StringForFilename
import jp.co.toukei.log.lib.makeDirs
import jp.co.toukei.log.lib.util.RequestBodyJson
import jp.co.toukei.log.trustar.Config
import jp.co.toukei.log.trustar.rest.model.UserLite
import org.json.JSONObject
import java.io.File

class UserInfo @Keep constructor(
    @Para("companyCd") companyCd: String,
    @Para("userId") userId: String,
    @Para("userNm") @JvmField val userNm: String,
    @Para("appAuthority") @JvmField val appAuthority: Int,
    @Para("branchTel") @JvmField val branchTel: String,
    @Para("positionSendInterval") @JvmField val postIntervalInMin: Int,
    @Para("positionGetInterval") @JvmField val getIntervalInSec: Int,
    @Para("backgroundInterval") @JvmField val backgroundInterval: Int?,
    @Para("decimationRange") @JvmField val decimationRange: Int?,
    @Para("incidentalUseFlag") incidentalUseFlag: Int?,
    @Para("geofenceUseFlag") geofenceUseFlag: Int?,
    @Para("meterInputFlag") meterInputFlag: Int?,
    @Para("stayTime") @JvmField val stayTimeInMin: Int,
    @Para("misdeliveryMeterTo") @JvmField val misdeliveryMeterTo: Int,
    @Para("detectStartTiming") @JvmField val detectStartTimingInMin: Int, //スマホ使用検知の開始タイミング,
    @Para("restDistance") @JvmField val restDistanceMeter: Int,
    @Para("restTime") @JvmField val restTimeInMin: Int,
) : RequestBodyJson, UserLite(userId, companyCd) {

    @JvmField
    val meterInputEnabled = meterInputFlag == 1

    @JvmField
    val incidentalEnabled = incidentalUseFlag == 1

    @JvmField
    val geofenceUseFlag = geofenceUseFlag == 1

    val homeDir: File = homeDirFile(companyCd, userId).makeDirs()

    override fun jsonBody(): JSONObject {
        return jsonObject()
    }


    class FileByKey(@JvmField val fileKey: String, @JvmField val file: File)

    companion object {

        @JvmStatic
        fun homeDirFile(companyCd: String, userId: String): File {
            return Config.userDir.child(companyCd).child(userId)
        }

        @JvmStatic
        fun homeCacheDirFile(user: UserInfo): File {
            return Config.userDirInCache
                .child(user.companyCd.toByteArray().encodeToBase64StringForFilename())
                .child(user.userId.toByteArray().encodeToBase64StringForFilename())
        }

        @JvmStatic
        fun userJsonFile(companyCd: String, userId: String): File {
            return homeDirFile(companyCd, userId).makeDirs().child("login.json")
        }

        @JvmStatic
        fun userJsonFile(user: UserInfo): File {
            return userJsonFile(user.companyCd, user.userId)
        }

        @JvmStatic
        fun userLocalFileDir(user: UserInfo): File {
            return user.homeDir.child("files").makeDirs()
        }

        @JvmStatic
        fun cacheFileByKey(user: UserInfo, fileKey: String): FileByKey {
            val f = homeCacheDirFile(user).child("file_by_key")
                .child(fileKey.toByteArray().encodeToBase64StringForFilename())
            return FileByKey(fileKey, f)
        }
    }
}
