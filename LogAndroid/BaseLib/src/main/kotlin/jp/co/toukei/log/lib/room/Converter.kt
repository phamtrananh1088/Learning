package jp.co.toukei.log.lib.room

import androidx.room.TypeConverter
import jp.co.toukei.log.lib.tryJsonArr
import jp.co.toukei.log.lib.tryJsonObj
import org.json.JSONArray
import org.json.JSONObject

class Converter {

    @TypeConverter
    fun jsonObjectToString(jsonObject: JSONObject?): String? {
        return jsonObject?.toString()
    }

    @TypeConverter
    fun stringToJsonObject(json: String?): JSONObject? {
        return json?.tryJsonObj()
    }

    @TypeConverter
    fun jsonArrayToString(jsonArr: JSONArray?): String? {
        return jsonArr?.toString()
    }

    @TypeConverter
    fun stringToJsonArray(json: String?): JSONArray? {
        return json?.tryJsonArr()
    }

    @TypeConverter
    fun jsonArrayToStringList(jsonArr: JSONArray?): List<String>? {
        return jsonArr?.run { List<String>(length()) { optString(it) } }
    }

    @TypeConverter
    fun listToJsonArray(stringList: List<String>?): JSONArray? {
        stringList ?: return null
        return JSONArray().apply { stringList.forEach { put(it) } }
    }
}
