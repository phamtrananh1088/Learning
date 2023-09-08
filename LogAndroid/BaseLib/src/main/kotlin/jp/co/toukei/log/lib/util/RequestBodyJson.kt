package jp.co.toukei.log.lib.util

import org.json.JSONObject

interface RequestBodyJson {

    fun jsonBody(): JSONObject
}
