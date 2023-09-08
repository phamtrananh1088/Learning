package jp.co.toukei.log.trustar.enum

import androidx.compose.runtime.Immutable

@Immutable
sealed class IncidentalEnum {

    class List(
        val allocationNo: String,
        val allocationRowNo: Int,
    ) : IncidentalEnum()

    class Add(
        val allocationNo: String,
        val allocationRowNo: Int,
    ) : IncidentalEnum()

    class View(
        val headerUUID: String,
    ) : IncidentalEnum()

    class Edit(
        val headerUUID: String,
    ) : IncidentalEnum()

}

@Suppress("SpellCheckingInspection")
enum class IncidentalType(
    val type: Int,
) {
    Nimachi(0),
    Futai(1)
}
