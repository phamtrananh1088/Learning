package jp.co.toukei.log.lib.util

import android.text.InputFilter
import android.text.Spanned

class DecimalInputFilter(
        numLen: Int,
        decLen: Int,
        enableNegative: Boolean = false
) : InputFilter {

    @JvmField
    val regex: Regex

    init {
        val b = (numLen - 1).let { if (it > 0) "\\d{0,$it}" else "" }
        val c = if (decLen > 0) "(\\.\\d{0,$decLen})?" else ""
        regex = Regex(if (enableNegative) "-?(\\d$b$c)?" else "\\d$b$c")
    }

    override fun filter(
            source: CharSequence,
            start: Int,
            end: Int,
            dest: Spanned,
            dstart: Int,
            dend: Int
    ): CharSequence? {
        val d = dest.replaceRange(dstart, dend, source.subSequence(start, end))
        if (regex.matches(d))
            return null
        return ""
    }
}
