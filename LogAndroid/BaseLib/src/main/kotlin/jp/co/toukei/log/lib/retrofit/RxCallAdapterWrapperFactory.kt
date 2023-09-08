package jp.co.toukei.log.lib.retrofit

import com.squareup.moshi.Moshi
import io.reactivex.rxjava3.core.Single
import retrofit2.CallAdapter
import retrofit2.HttpException
import retrofit2.Retrofit
import java.lang.reflect.Type
import kotlin.reflect.KClass

class RxCallAdapterWrapperFactory(
    private val moshi: Moshi,
    private val delegate: CallAdapter.Factory,
    private vararg val parseDefault: KClass<out Exception>,
) : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit,
    ): CallAdapter<*, *>? {
        @Suppress("UNCHECKED_CAST")
        val r = delegate.get(returnType, annotations, retrofit) as? CallAdapter<Any, Any>
        r ?: return null
        return object : CallAdapter<Any, Any> {
            override fun responseType(): Type = r.responseType()

            override fun adapt(call: retrofit2.Call<Any>): Any {
                val rx = r.adapt(call)
                if (rx is Single<*>) {
                    val t = annotations.filterIsInstance<ParseErrorBody>().firstOrNull()?.type
                    val ls = arrayListOf<KClass<out Exception>>().apply {
                        t?.let(::addAll)
                        addAll(parseDefault)
                    }
                    if (ls.isNotEmpty()) return rx.onErrorResumeNext {
                        if (it is HttpException && it.code() in 400..599) {
                            val err = it.response()?.errorBody()?.string()
                            if (err != null) {
                                val d = ls.firstNotNullOfOrNull { e ->
                                    runCatching {
                                        moshi.adapter(e.java).fromJson(err)
                                    }.getOrNull()
                                }
                                if (d != null) {
                                    return@onErrorResumeNext Single.error(d)
                                }
                            }
                        }
                        return@onErrorResumeNext Single.error(it)
                    }
                }
                return rx
            }
        }
    }
}
