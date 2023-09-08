/**
 * copied from my personal code.
 * modified.
 */

package third

import org.json.JSONArray
import org.json.JSONObject

@JvmInline
value class JObject(val json: JSONObject) {

    inline infix operator fun String.invoke(obj: JObject.() -> Unit) {
        json.put(this, jsonObj(obj))
    }

    infix fun String.v(value: Any?) {
        json.put(this, value)
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "abuse")
    inline infix fun String.v(@Suppress("UNUSED_PARAMETER") f: () -> Any): Nothing {
        throw Exception()
    }
}

inline fun jsonObj(builder: JObject.() -> Unit): JSONObject {
    return jsonObj(JSONObject(), builder)
}

inline fun jsonObj(jsonObject: JSONObject?, builder: JObject.() -> Unit): JSONObject {
    return JObject(jsonObject ?: JSONObject()).apply(builder).json
}

inline fun jsonObjInsideArr(builder: JObject.() -> Unit): JSONArray {
    return JSONArray().put(jsonObj(builder))
}
