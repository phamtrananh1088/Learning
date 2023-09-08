package jp.co.toukei.log.common.compose

import android.text.style.URLSpan
import android.text.util.Linkify
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.input.pointer.PointerEventTimeoutCancellationException
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.core.text.toSpannable
import jp.co.toukei.log.trustar.Config
import kotlinx.coroutines.coroutineScope

sealed class TextClk {
    class Tel(val text: String) : TextClk() {
        override fun toString(): String {
            return text
        }

        companion object {
            const val TAG = "TEL"
            fun hasFlag(v: Int) = v and TEL == TEL
        }
    }

    class Url(val text: String) : TextClk() {
        override fun toString(): String {
            return text
        }

        companion object {
            const val TAG = "URL"
            fun hasFlag(v: Int) = v and URL == URL
        }
    }

    class Date(val timeInMillis: Long) : TextClk() {
        override fun toString(): String {
            return timeInMillis.toString()
        }

        companion object {
            const val TAG = "DATE"
            fun hasFlag(v: Int) = v and DATE == DATE
        }
    }

    companion object {

        const val TEL = 0b1
        const val URL = 0b10
        const val DATE = 0b100
    }
}

private fun String.annotatedString(
    mask: Int = -1,
): AnnotatedString {
    return AnnotatedString.Builder(this).also { builder ->
        if (TextClk.Tel.hasFlag(mask)) {
            val tag = TextClk.Tel.TAG
            Config.japanTelRegex.findAll(this).forEach {
                val s = it.range.first
                val e = it.range.last + 1
                builder.addStringAnnotation(tag, it.value, s, e)
                builder.addStyle(
                    SpanStyle(
                        color = Color.Blue,
                        fontWeight = FontWeight.Bold,
                    ), s, e
                )
            }
        }
        if (TextClk.Url.hasFlag(mask)) {
            val tag = TextClk.Url.TAG
            val sp = toSpannable()
            Linkify.addLinks(sp, Linkify.WEB_URLS)
            sp.getSpans(0, sp.length, URLSpan::class.java).forEach {
                val s = sp.getSpanStart(it)
                val e = sp.getSpanEnd(it)
                builder.addStringAnnotation(tag, it.url, s, e)
                builder.addStyle(
                    SpanStyle(
                        color = Color.Blue,
                        fontWeight = FontWeight.Bold,
                    ), s, e
                )
            }
        }
        if (TextClk.Date.hasFlag(mask)) {
            val tag = TextClk.Date.TAG
            Config.dateRegex.findAll(this).forEach { (range, date) ->
                val s = range.first
                val e = range.last + 1
                builder.addStringAnnotation(tag, date.timeInMillis.toString(), s, e)
                builder.addStyle(
                    SpanStyle(
                        color = Color.Green,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline
                    ), s, e
                )
            }
        }
    }.toAnnotatedString()
}

private fun AnnotatedString.detectFirst(
    mask: Int,
    offset: Int
): Pair<AnnotatedString.Range<*>, TextClk>? {
    if (TextClk.Tel.hasFlag(mask)) {
        val r = getStringAnnotations(TextClk.Tel.TAG, offset, offset).firstOrNull()
        if (r != null) return r to TextClk.Tel(r.item)
    }
    if (TextClk.Url.hasFlag(mask)) {
        val r = getStringAnnotations(TextClk.Url.TAG, offset, offset).firstOrNull()
        if (r != null) return r to TextClk.Url(r.item)
    }
    if (TextClk.Date.hasFlag(mask)) {
        val r = getStringAnnotations(TextClk.Date.TAG, offset, offset).firstOrNull()
        if (r != null) return r to TextClk.Date(r.item.toLong())
    }
    return null
}


@Composable
fun MyClickableText(
    mask: Int,
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    softWrap: Boolean = true,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
    onLongPressed: (() -> Unit)? = null,
    onClick: (TextClk) -> Unit,
) {
    val h = rememberUpdatedState(newValue = onClick)
    val layoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }

    val annotatedText = remember(text) {
        text.annotatedString(mask)
    }
    var pressed by remember(text) {
        mutableStateOf<AnnotatedString?>(null)
    }
    BasicText(
        text = pressed ?: annotatedText,
        modifier = modifier.pointerInput(h, text) {
            coroutineScope {
                awaitEachGesture {
                    val down = awaitFirstDown()
                    layoutResult.value?.run {
                        val offset = getOffsetForPosition(down.position)
                        annotatedText.detectFirst(mask, offset)?.let { (range, clk) ->
                            down.consume()
                            pressed = AnnotatedString.Builder(annotatedText).apply {
                                addStyle(
                                    SpanStyle(
                                        background = Color.Blue.copy(alpha = 0.2F)
                                    ), range.start, range.end
                                )
                            }.toAnnotatedString()

                            val uoc = if (onLongPressed == null) {
                                waitForUpOrCancellation()
                            } else {
                                try {
                                    val t = viewConfiguration.longPressTimeoutMillis
                                    withTimeout(t) {
                                        waitForUpOrCancellation()
                                    }
                                } catch (e: PointerEventTimeoutCancellationException) {
                                    onLongPressed()
                                    null
                                }
                            }
                            if (uoc != null && !uoc.isConsumed) {
                                uoc.consume()
                                h.value(clk)

                            }
                            pressed = null
                        }
                    }
                }
            }
        },
        style = style.merge(
            color = style.color.takeOrElse {
                LocalContentColor.current
            }
        ),
        softWrap = softWrap,
        overflow = overflow,
        maxLines = maxLines,
        onTextLayout = { layoutResult.value = it }
    )
}
