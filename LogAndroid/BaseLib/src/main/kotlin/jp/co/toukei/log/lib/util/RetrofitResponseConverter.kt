package jp.co.toukei.log.lib.util

import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class RetrofitResponseConverter : Converter.Factory() {

    override fun responseBodyConverter(
            type: Type,
            annotations: Array<Annotation>,
            retrofit: Retrofit
    ): Converter<ResponseBody, Any>? {
        return when (type as? Class<*> ?: return null) {
            JSONObject::class.java -> Converter {
                JSONObject(it.string())
            }
            JSONArray::class.java -> Converter {
                JSONArray(it.string())
            }
            else -> null
        }
    }
}
