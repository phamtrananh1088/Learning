package jp.co.toukei.log.lib.util

import okhttp3.Request
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.asResponseBody
import okhttp3.internal.stripBody
import okio.Buffer
import okio.Timeout
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException
import java.lang.reflect.Type
import java.util.function.Function

class CallAdapterFactory(
    private val rewriteRequest: (annotations: Array<Annotation>) -> Function<Request, Request>?,
) : CallAdapter.Factory() {

    private var flag = false

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit,
    ): CallAdapter<Any, Any>? {
        if (flag) return null
        flag = true
        @Suppress("UNCHECKED_CAST")
        val raw = retrofit.callAdapter(returnType, annotations) as CallAdapter<Any, Any>
        flag = false
        val responseType: Type = raw.responseType()
        val callFactory = retrofit.callFactory()
        val converter = retrofit.responseBodyConverter<Any>(responseType, annotations)
        return rewriteRequest(annotations)?.let { Adapter(it, raw, converter, callFactory) } ?: raw
    }

    private class Adapter(
        private val rewrite: Function<Request, Request>,
        private val raw: CallAdapter<Any, Any>,
        private val converter: Converter<ResponseBody, Any>,
        private val callFactory: okhttp3.Call.Factory,
    ) : CallAdapter<Any, Any> by raw {

        override fun adapt(call: Call<Any>): Any {
            return raw.adapt(C(call))
        }

        private inner class C(
            private val call: Call<Any>,
        ) : Call<Any> {

            private val r = callFactory.newCall(call.request().let(rewrite::apply))

            override fun enqueue(callback: Callback<Any>) {
                r.enqueue(object : okhttp3.Callback {
                    override fun onFailure(call: okhttp3.Call, e: IOException) = f(e)

                    private fun f(e: Throwable) {
                        callback.onFailure(this@C, e)
                    }

                    override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                        runCatching { convert(response) }
                            .onFailure { f(it) }
                            .onSuccess {
                                callback.onResponse(this@C, it)
                            }
                    }
                })
            }

            override fun execute(): Response<Any> = convert(r.execute())

            override fun isExecuted(): Boolean = r.isExecuted()
            override fun timeout(): Timeout = r.timeout()
            override fun clone(): Call<Any> = C(call)
            override fun isCanceled(): Boolean = r.isCanceled()
            override fun cancel() = r.cancel()
            override fun request(): Request = r.request()

            private fun convert(r: okhttp3.Response): Response<Any> {
                val body = r.body
                val code = r.code
                val response = r.stripBody()
                return when {
                    code < 200 || code >= 300 -> body.use {
                        val s = Buffer()
                        it.source().readAll(s)
                        val b = s.asResponseBody(it.contentType(), it.contentLength())
                        Response.error(b, response)
                    }

                    code == 204 || code == 205 -> {
                        body.close()
                        Response.success(null, response)
                    }

                    else -> Response.success(converter.convert(body), response)
                }
            }
        }
    }
}
