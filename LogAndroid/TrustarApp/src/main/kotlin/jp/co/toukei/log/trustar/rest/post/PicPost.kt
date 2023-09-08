package jp.co.toukei.log.trustar.rest.post

import android.util.Base64
import jp.co.toukei.log.lib.encodeToBase64String
import jp.co.toukei.log.lib.util.RequestBodyJson
import jp.co.toukei.log.trustar.db.user.entity.IncidentalHeader
import jp.co.toukei.log.trustar.db.image.ImageSending
import jp.co.toukei.log.trustar.user.ClientInfo
import org.json.JSONObject
import third.jsonObj

class PicPost(
        private val clientInfo: ClientInfo,
        private val imageSending: ImageSending
) : RequestBodyJson {

    @JvmField
    val picId = imageSending.picId

    override fun jsonBody(): JSONObject {
        return jsonObj {
            "companyCd" v imageSending.companyCd
            "userId" v imageSending.userId
            "clientInfo" v clientInfo.jsonBody()

            "picId" v picId
            "picByteBase64String" v imageSending.picRaw.encodeToBase64String(Base64.NO_WRAP)

            val content = imageSending.content
            //this sucks.
            "picCls" v content.optString("picCls")
            "note1" v content.optString("note1")
            "note2" v content.optString("note2")
            "note3" v content.optString("note3")
            "note4" v content.optString("note4")
            "note5" v content.optString("note5")
        }
    }

    sealed class Content(
            @JvmField
            val picCls: String,
            @JvmField
            val note1: String?,
            @JvmField
            val note2: String?,
            @JvmField
            val note3: String?,
            @JvmField
            val note4: String?,
            @JvmField
            val note5: String?
    ) {

        fun toJson(): JSONObject {
            return jsonObj {
                "picCls" v picCls
                "note1" v note1
                "note2" v note2
                "note3" v note3
                "note4" v note4
                "note5" v note5
            }
        }

        class IncidentalSign(header: IncidentalHeader) : Content(
                "SIGN",
                header.allocationNo,
                header.allocationRowNo.toString(),
                header.uuid,
                "Incidental",
                null
        )
    }
}
