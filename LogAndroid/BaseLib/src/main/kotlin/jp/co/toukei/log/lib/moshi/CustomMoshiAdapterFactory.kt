/**
 * Copied from com.squareup.moshi.kotlin.reflect.KotlinJsonAdapter with custom modify.
 */


package jp.co.toukei.log.lib.moshi

import annotation.Find
import annotation.Para
import com.squareup.moshi.*
import com.squareup.moshi.internal.Util
import com.squareup.moshi.internal.Util.generatedAdapter
import com.squareup.moshi.internal.Util.resolve
import jp.co.toukei.log.lib.R
import jp.co.toukei.log.lib.ctx.Ctx
import jp.co.toukei.log.lib.randomAccess
import java.lang.reflect.Constructor
import java.lang.reflect.Type

class CustomMoshiAdapterFactory : JsonAdapter.Factory {
    override fun create(type: Type, annotations: MutableSet<out Annotation>, moshi: Moshi)
            : JsonAdapter<*>? {
        if (annotations.isNotEmpty()) return null

        val rawType = Types.getRawType(type)
        if (rawType.isInterface) return null
        if (rawType.isEnum) return null
        if (Util.isPlatformType(rawType)) return null
        try {
            val generatedAdapter = generatedAdapter(moshi, type, rawType)
            if (generatedAdapter != null) {
                return generatedAdapter
            }
        } catch (e: RuntimeException) {
            if (e.cause !is ClassNotFoundException) {
                throw e
            }
            // Fall back to a reflective adapter when the generated adapter is not found.
        }

        if (rawType.isLocalClass) {
            throw IllegalArgumentException("Cannot serialize local class or object expression ${rawType.name}")
        }

        /* many if ...... */

        val constructor = rawType.constructors
            .run {
                when (size) {
                    1 -> firstOrNull()
                    else -> firstOrNull { it.getAnnotation(Find::class.java) != null }
                }
            }
            ?: return null

        val bindings = constructor.run { parameterAnnotations.zip(parameterTypes) }
            .map { (a, t) ->
                val p = a.filterIsInstance<Para>()
                    .firstOrNull()
                    ?: throw IllegalArgumentException(this::class.java.simpleName)
                val name = p.name
                val adapter = moshi.adapter<Any>(
                    resolve(type, rawType, t),
                    Util.jsonAnnotations(a),
                    name
                )
                KotlinJsonAdapter.Binding(name, adapter, p)
            }
        if (bindings.isEmpty()) {
            throw IllegalArgumentException(this::class.java.simpleName)
        }
        val ja = KotlinJsonAdapter(constructor, bindings)
        return ErrorWrap(ja).nullSafe()
    }

    private class ErrorWrap<T>(private val delegate: JsonAdapter<T>) : JsonAdapter<T>() {

        private val errorMsg = Ctx.context.getString(R.string.unsatisfied_json_data)

        override fun fromJson(reader: JsonReader): T? {
            try {
                return delegate.fromJson(reader)
            } catch (e: Exception) {
                val em = e.message
                val m = if (em == null) errorMsg else "${errorMsg}\n$em"
                throw Exception(m, e)
            }
        }

        override fun toJson(writer: JsonWriter, value: T?) {
            return delegate.toJson(writer, value)
        }
    }

    /**
     * This class encodes Kotlin classes using their properties. It decodes them by first invoking the
     * constructor, and then by setting any additional properties that exist, if any.
     */
    private class KotlinJsonAdapter<T>(
        private val constructor: Constructor<T>,
        bindings: List<Binding<Any?>>
    ) : JsonAdapter<T>() {
        private val bindings = bindings.randomAccess()
        private val options = JsonReader.Options.of(*bindings.map { it.name }.toTypedArray())

        // todo: need optional return null when failure.

        override fun fromJson(reader: JsonReader): T {
            val values = Array<Any?>(bindings.size) { Unit }
            reader.beginObject()
            while (reader.hasNext()) {
                val index = reader.selectName(options)

                val b = if (index != -1) bindings[index] else {
                    reader.skipName()
                    reader.skipValue()
                    continue
                }

                if (values[index] !== Unit) {
                    throw JsonDataException("Multiple values for '${b.name}' at ${reader.path}")
                }

                values[index] = try {
                    b.adapter.fromJson(reader)
                } catch (e: Exception) {
                    null
                }
            }
            reader.endObject()

            values.forEachIndexed { index, any ->
                if (any === Unit) {
                    values[index] = null
                }
            }
            return constructor.newInstance(*values)
        }

        override fun toJson(writer: JsonWriter, value: T?) {
            throw UnsupportedOperationException("todo")
        }

        data class Binding<P>(
            val name: String,
            val adapter: JsonAdapter<P>,
            val para: Para
        )
    }

}
