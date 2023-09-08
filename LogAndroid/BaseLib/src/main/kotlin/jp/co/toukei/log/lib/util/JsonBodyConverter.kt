package jp.co.toukei.log.lib.util

import jp.co.toukei.log.lib.Const
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class JsonBodyConverter : Converter.Factory() {
    override fun requestBodyConverter(
            type: Type,
            parameterAnnotations: Array<Annotation>,
            methodAnnotations: Array<Annotation>,
            retrofit: Retrofit
    ): Converter<RequestBodyJson, RequestBody>? {
        val clz = type as? Class<*> ?: return null
        val able = RequestBodyJson::class.java.isAssignableFrom(clz)
        return if (able) Converter {
            it.jsonBody().toString().toRequestBody(Const.mediaTypeJson)
        } else null
    }
}
