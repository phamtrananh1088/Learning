/**
 * Copied from com.squareup.moshi.kotlin.reflect.KotlinJsonAdapter with custom modify.
 */


package jp.co.toukei.log.lib.moshi

import annotation.Find
import annotation.JsonObjectExtra
import annotation.Para
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.internal.Util
import com.squareup.moshi.internal.Util.generatedAdapter
import com.squareup.moshi.internal.Util.resolve
import jp.co.toukei.log.lib.R
import jp.co.toukei.log.lib.ctx.Ctx
import jp.co.toukei.log.lib.mapToArray
import jp.co.toukei.log.lib.randomAccess
import org.json.JSONObject
import org.json.JSONTokener
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
                a.filterIsInstance<Para>().firstOrNull()
                    ?.let { para ->
                        val name = para.name
                        val adapter = moshi.adapter<Any>(
                            resolve(type, rawType, t),
                            Util.jsonAnnotations(a),
                            name
                        )
                        KotlinJsonAdapter.Binding.Key(name, adapter)
                    }
                    ?: a.filterIsInstance<JsonObjectExtra>().firstOrNull()
                        ?.let { extra ->
                            if (t == JSONObject::class.java) {
                                KotlinJsonAdapter.Binding.JsonObjectExtra(extra.names)
                            } else {
                                throw IllegalArgumentException("must be JSONObject type: ${t.canonicalName}")
                            }
                        }
                    ?: throw IllegalArgumentException(this::class.java.simpleName)

            }
        if (bindings.isEmpty()) {
            throw IllegalArgumentException(this::class.java.simpleName)
        }
        val ja = KotlinJsonAdapter(constructor, bindings)
        return ErrorWrap(ja).nullSafe()
    }

    class InvalidJsonException(m: String, e: Exception) : Exception(m, e)

    private class ErrorWrap<T>(private val delegate: JsonAdapter<T>) : JsonAdapter<T>() {

        private val errorMsg = Ctx.context.getString(R.string.unsatisfied_json_data)

        override fun fromJson(reader: JsonReader): T? {
            try {
                return delegate.fromJson(reader)
            } catch (e: Exception) {
                val em = e.message
                val m = if (em == null) errorMsg else "${errorMsg}\n$em"
                throw InvalidJsonException(m, e)
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
        bindings: List<Binding>
    ) : JsonAdapter<T>() {
        private val argBindings = bindings.randomAccess()

        private val flatByName = argBindings.flatMapIndexedTo(ArrayList()) { index, b ->
            when (b) {
                is Binding.Key -> listOf(Triple(index, b.name, b))
                is Binding.JsonObjectExtra -> b.names.map { name -> Triple(index, name, b) }
            }
        }
        private val flatNameOp = JsonReader.Options.of(*flatByName.mapToArray { it.second })


        // todo: need optional return null when failure.

        override fun fromJson(reader: JsonReader): T {
            val args = Array<Any?>(argBindings.size) { Unit }

            reader.beginObject()
            while (reader.hasNext()) {
                val fi = reader.selectName(flatNameOp)

                val (argIndex, name, binding) = if (fi != -1) flatByName[fi] else {
                    reader.skipName()
                    reader.skipValue()
                    continue
                }

                when (binding) {
                    is Binding.Key -> {
                        if (args[argIndex] !== Unit) {
                            throw JsonDataException("Multiple values for '$name' at ${reader.path}")
                        }
                        args[argIndex] = try {
                            binding.adapter.fromJson(reader)
                        } catch (e: Exception) {
                            null
                        }
                    }

                    is Binding.JsonObjectExtra -> {
                        val extra = args[argIndex].let {
                            if (it !== Unit) {
                                it as JSONObject
                            } else {
                                JSONObject().apply { args[argIndex] = this }
                            }
                        }
                        val raw = reader.readJsonString()
                        extra.put(name, JSONTokener(raw).nextValue())
                    }
                }
            }
            reader.endObject()

            args.forEachIndexed { index, any ->
                if (any === Unit) {
                    args[index] = null
                }
            }
            return constructor.newInstance(*args)
        }

        override fun toJson(writer: JsonWriter, value: T?) {
            throw UnsupportedOperationException("todo")
        }

        sealed class Binding {
            class Key(
                val name: String,
                val adapter: JsonAdapter<Any>,
            ) : Binding()

            class JsonObjectExtra(
                val names: Array<out String>,
            ) : Binding()
        }
    }
}

private fun JsonReader.readJsonString(): String {
    return nextSource().use { it.readUtf8() }
}
