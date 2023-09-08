package jp.co.toukei.log.lib.moshi

import com.squareup.moshi.Moshi
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class RetrofitConverter(private val moshi: Moshi) : Converter.Factory() {

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit,
    ): Converter<ResponseBody, Any>? {
        val adapter = moshi.adapter<Any>(type)
        return if (adapter != null) Converter {
            val json = it.string()
            val a = annotations.asSequence().filterIsInstance<UnWrapper>().firstOrNull()
            val j = a?.key?.fold(json) { j, e -> JSONObject(j).get(e).toString() } ?: json
            adapter.fromJson(j)
        } else null
    }

    annotation class UnWrapper(vararg val key: String)
}
