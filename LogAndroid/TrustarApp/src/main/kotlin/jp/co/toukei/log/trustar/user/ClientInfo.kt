package jp.co.toukei.log.trustar.user

import android.os.Build
import jp.co.toukei.log.lib.util.RequestBodyJson
import jp.co.toukei.log.trustar.BuildConfig
import jp.co.toukei.log.trustar.Config
import org.json.JSONObject
import third.jsonObj

// immutable
class ClientInfo : RequestBodyJson {

    override fun jsonBody(): JSONObject {
        return jsonObj {
            "terminalId" v Config.androidId
            "terminalName" v Build.MODEL
            "osVersion" v Build.VERSION.RELEASE
            "appVersion" v BuildConfig.VERSION_CODE
        }
    }
}
