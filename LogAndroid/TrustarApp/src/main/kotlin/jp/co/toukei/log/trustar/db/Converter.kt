package jp.co.toukei.log.trustar.db

import androidx.room.TypeConverter
import jp.co.toukei.log.lib.jsonArr
import jp.co.toukei.log.lib.tryJsonArr
import jp.co.toukei.log.lib.tryJsonObj
import jp.co.toukei.log.trustar.common.DbFileRecord
import jp.co.toukei.log.trustar.db.user.entity.DeliveryChart
import jp.co.toukei.log.trustar.db.user.entity.bin.Place
import jp.co.toukei.log.trustar.db.user.entity.bin.PlaceExt
import jp.co.toukei.log.trustar.forEachOptObject
import org.json.JSONArray
import org.json.JSONObject
import third.jsonObj

object Converter {

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

    private fun <T : Any> JSONArray.jsonArrayMapObj(map: (JSONObject) -> T): List<T> {
        val ls = ArrayList<T>()
        forEachOptObject { _, j ->
            j?.let(map)?.let(ls::add)
        }
        return ls
    }

    @TypeConverter
    fun jsonArrayToStringList(jsonArr: JSONArray): List<String> {
        return jsonArr.run { List<String>(length()) { optString(it) } }
    }

    @TypeConverter
    fun listToJsonArray(stringList: List<String>?): JSONArray? {
        stringList ?: return null
        return JSONArray().apply { stringList.forEach { put(it) } }
    }

    @TypeConverter
    fun jsonToLocationDb(json: JSONObject?): LocationDb? {
        return json?.run {
            val lon = optString("lon").toFloatOrNull()
            val lat = optString("lat").toFloatOrNull()
            val acc = optString("accuracy").toFloatOrNull()
            if (lon != null && lat != null && acc != null) LocationDb(lon, lat, acc) else null
        }
    }

    @TypeConverter
    fun locationDbToJson(location: LocationDb?): JSONObject? {
        val l = location ?: return null
        return jsonObj {
            "lon" v l.lon.toString()
            "lat" v l.lat.toString()
            "accuracy" v l.accuracy.toString()
        }
    }

    @TypeConverter
    fun jsonToBinPlace(json: JSONObject?): Place? {
        return json?.run {
            val cd = json.optString("cd")
            val nm1 = json.optString("nm1")
            val nm2 = json.optString("nm2")
            val addr = json.optString("addr")
            Place(cd = cd, nm1 = nm1, nm2 = nm2, addr = addr)
        }
    }

    @TypeConverter
    fun binPlaceToJson(place: Place?): JSONObject? {
        val p = place ?: return null
        return jsonObj {
            "cd" v p.cd
            "nm1" v p.nm1
            "nm2" v p.nm2
            "addr" v p.addr
        }
    }

    @TypeConverter
    fun jsonToBinPlaceExt(json: JSONObject?): PlaceExt? {
        return json?.run {
            val zip = json.optString("zip")
            val tel1 = json.optString("tel1")
            val mail1 = json.optString("mail1")
            val tel2 = json.optString("tel2")
            val mail2 = json.optString("mail2")
            val note1 = json.optString("note1")
            val note2 = json.optString("note2")
            val note3 = json.optString("note3")
            PlaceExt(zip, tel1, mail1, tel2, mail2, note1, note2, note3)
        }
    }

    @TypeConverter
    fun binPlaceExtToJson(place: PlaceExt?): JSONObject? {
        val p = place ?: return null
        return jsonObj {
            "zip" v p.zip
            "tel1" v p.tel1
            "mail1" v p.mail1
            "tel2" v p.tel2
            "mail2" v p.mail2
            "note1" v p.note1
            "note2" v p.note2
            "note3" v p.note3
        }
    }

    @TypeConverter
    fun allocationRow1(row: IntAndString): String {
        return IntAndString.asString(row)
    }

    @TypeConverter
    fun allocationRow2(string: String): IntAndString {
        return IntAndString.fromString(string)
    }


    //todo serialization tools?

    @TypeConverter
    fun memoToJson(memo: List<DeliveryChart.ChartMemo>): JSONArray {
        return memo.jsonArr { p ->
            jsonObj {
                "label" v p.label
                "note" v p.note
                "highlight" v p.highlight
                "extra" v p.extra
            }
        }
    }

    @TypeConverter
    fun jsonToMemo(json: JSONArray): List<DeliveryChart.ChartMemo> {
        return json.jsonArrayMapObj { j ->
            val l = j.optString("label")
            val n = j.optString("note")
            val h = j.optBoolean("highlight")
            val e = j.optJSONObject("extra")
            DeliveryChart.ChartMemo(
                label = l,
                note = n,
                highlight = h,
                extra = e,
            )
        }
    }

    @TypeConverter
    fun convert4(memo: List<DeliveryChart.ChartImageFile>): JSONArray {
        return memo.jsonArr { p ->
            jsonObj {
                "type" v p.dbStoredFile.type
                "value" v p.dbStoredFile.value
                "extra" v p.extra
            }
        }
    }

    @TypeConverter
    fun convert5(json: JSONArray): List<DeliveryChart.ChartImageFile> {
        return json.jsonArrayMapObj { j ->
            val v = j.optString("value")
            val t = j.optInt("type")
            val e = j.optJSONObject("extra")
            DeliveryChart.ChartImageFile(
                DbFileRecord(t, v),
                extra = e,
            )
        }
    }

    @TypeConverter
    fun convert1(p: DeliveryChart.Info): JSONObject {
        return jsonObj {
            "dest" v p.dest
            "addr1" v p.addr1
            "addr2" v p.addr2
            "tel" v p.tel
            "carrier" v p.carrier
            "carrierTel" v p.carrierTel
        }
    }

    @TypeConverter
    fun convert2(j: JSONObject): DeliveryChart.Info {
        return DeliveryChart.Info(
            dest = j.optString("dest"),
            addr1 = j.optString("addr1"),
            addr2 = j.optString("addr2"),
            tel = j.optString("tel"),
            carrier = j.optString("carrier"),
            carrierTel = j.optString("carrierTel"),
        )
    }
}
