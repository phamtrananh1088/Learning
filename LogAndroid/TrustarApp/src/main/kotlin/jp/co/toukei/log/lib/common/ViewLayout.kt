@file:Suppress("NOTHING_TO_INLINE")

package jp.co.toukei.log.lib.common

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import android.widget.ViewFlipper
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID
import androidx.constraintlayout.widget.Group
import androidx.constraintlayout.widget.Guideline
import androidx.core.widget.NestedScrollView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.RelativeCornerSize
import com.google.android.material.snackbar.Snackbar
import jp.co.toukei.log.lib.R
import splitties.resources.color
import splitties.views.dsl.core.NO_THEME
import splitties.views.dsl.core.matchParent
import splitties.views.dsl.core.view
import splitties.views.dsl.core.wrapContent
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

inline fun ConstraintLayout.LayoutParams.topToTopParent() {
    topToTop = PARENT_ID
}

inline fun ConstraintLayout.LayoutParams.bottomToBottomParent() {
    bottomToBottom = PARENT_ID
}

inline fun ConstraintLayout.LayoutParams.startToStartParent() {
    startToStart = PARENT_ID
}

inline fun ConstraintLayout.LayoutParams.endToEndParent() {
    endToEnd = PARENT_ID
}

fun ConstraintLayout.LayoutParams.horizontalCenterInParent() {
    startToStartParent()
    endToEndParent()
    horizontalBias = 0.5F
}

fun ConstraintLayout.LayoutParams.verticalCenterInParent() {
    topToTopParent()
    bottomToBottomParent()
    verticalBias = 0.5F
}

fun ConstraintLayout.LayoutParams.horizontalCenterWith(id: Int) {
    startToStart = id
    endToEnd = id
    horizontalBias = 0.5F
}

fun ConstraintLayout.LayoutParams.verticalTo(id: Int, bias: Float) {
    topToTop = id
    bottomToBottom = id
    verticalBias = bias
}

inline fun ConstraintLayout.LayoutParams.above(@IdRes id: Int) {
    bottomToTop = id
}

inline fun ConstraintLayout.LayoutParams.below(@IdRes id: Int) {
    topToBottom = id
}

inline fun ConstraintLayout.LayoutParams.alignTop(@IdRes id: Int) {
    topToTop = id
}

inline fun TextView.whiteText() {
    setTextColor(Color.WHITE)
}

inline fun TextView.empty() {
    text = null
}

inline fun TextView.boldTypeface() {
    typeface = Typeface.DEFAULT_BOLD
}

inline fun <T : View> T.gone() = apply {
    visibility = View.GONE
}

inline fun <T : View> T.show() = apply {
    visibility = View.VISIBLE
}

inline fun <T : View> T.showOrGone(predicate: Boolean) = apply {
    if (predicate) show() else gone()
}

inline fun <T : View> T.enable() = apply {
    isEnabled = true
}

inline fun <T : View> T.disable() = apply {
    isEnabled = false
}

inline fun LinearLayout.gravityCenterHorizontal() {
    gravity = Gravity.CENTER_HORIZONTAL
}

inline fun LinearLayout.gravityCenterVertical() {
    gravity = Gravity.CENTER_VERTICAL
}

inline fun View.setLayoutParams(
    width: Int = matchParent,
    height: Int = wrapContent,
) {
    layoutParams = ViewGroup.LayoutParams(width, height)
}

inline fun View.setMarginLayoutParams(
    width: Int = matchParent,
    height: Int = wrapContent,
    block: ViewGroup.MarginLayoutParams.() -> Unit,
) {
    val lp = ViewGroup.MarginLayoutParams(width, height)
    block(lp)
    layoutParams = lp
}

inline fun View.snackbar(@StringRes message: Int) = Snackbar
    .make(this, message, Snackbar.LENGTH_SHORT)
    .apply { show() }

inline fun View.snackbar(message: CharSequence) = Snackbar
    .make(this, message, Snackbar.LENGTH_SHORT)
    .apply { show() }

inline fun View.indefiniteSnackbar(@StringRes message: Int) = Snackbar
    .make(this, message, Snackbar.LENGTH_INDEFINITE)
    .apply { show() }

inline fun View.indefiniteSnackbar(message: CharSequence) = Snackbar
    .make(this, message, Snackbar.LENGTH_INDEFINITE)
    .apply { show() }

@kotlin.OptIn(ExperimentalContracts::class)
inline fun View.roundedImageView(
    cornerSize: Float,
    initView: ShapeableImageView.() -> Unit = {},
): ShapeableImageView {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return context.view(::ShapeableImageView, View.NO_ID, NO_THEME) {
        shapeAppearanceModel =
            shapeAppearanceModel.toBuilder().setAllCorners(CornerFamily.ROUNDED, cornerSize).build()
        initView()
    }
}

@kotlin.OptIn(ExperimentalContracts::class)
inline fun View.circleImageView(
    initView: ShapeableImageView.() -> Unit = {},
): ShapeableImageView {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return context.view(::ShapeableImageView, View.NO_ID, NO_THEME) {
        shapeAppearanceModel =
            shapeAppearanceModel.toBuilder().setAllCornerSizes(RelativeCornerSize(0.5F)).build()
        initView()
    }
}

@kotlin.OptIn(ExperimentalContracts::class)
inline fun Context.nestedScrollView(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: NestedScrollView.() -> Unit = {},
): NestedScrollView {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return view(::NestedScrollView, id, theme, initView)
}

@kotlin.OptIn(ExperimentalContracts::class)
inline fun View.nestedScrollView(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: NestedScrollView.() -> Unit = {},
): NestedScrollView {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return context.nestedScrollView(id, theme, initView)
}


@kotlin.OptIn(ExperimentalContracts::class)
inline fun View.constraintLayout(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: ConstraintLayout.() -> Unit = {},
): ConstraintLayout {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return context.constraintLayout(id, theme, initView)
}

@kotlin.OptIn(ExperimentalContracts::class)
inline fun Context.constraintLayout(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: ConstraintLayout.() -> Unit = {},
): ConstraintLayout {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return view(::ConstraintLayout, id, theme, initView)
}

@kotlin.OptIn(ExperimentalContracts::class)
inline fun ConstraintLayout.defaultLParams(
    width: Int = wrapContent,
    height: Int = wrapContent,
    initParams: ConstraintLayout.LayoutParams.() -> Unit = {},
): ConstraintLayout.LayoutParams {
    contract { callsInPlace(initParams, InvocationKind.EXACTLY_ONCE) }
    return ConstraintLayout.LayoutParams(width, height).apply(initParams)
}

@kotlin.OptIn(ExperimentalContracts::class)
inline fun ConstraintLayout.guideline(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: Guideline.() -> Unit = {},
): Guideline {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return view(::Guideline, id, theme, initView)
}

@kotlin.OptIn(ExperimentalContracts::class)
inline fun ConstraintLayout.group(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: Group.() -> Unit = {},
): Group {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return view(::Group, id, theme, initView)
}

@Suppress("unused")
@kotlin.OptIn(ExperimentalContracts::class)
inline fun GridLayout.defaultLParams(
    initParams: GridLayout.LayoutParams.() -> Unit = {},
): GridLayout.LayoutParams {
    contract { callsInPlace(initParams, InvocationKind.EXACTLY_ONCE) }
    return GridLayout.LayoutParams().apply(initParams)
}

@kotlin.OptIn(ExperimentalContracts::class)
inline fun TableLayout.defaultLParams(
    width: Int = wrapContent,
    height: Int = wrapContent,
    weight: Float = 0f,
    initParams: TableLayout.LayoutParams.() -> Unit = {},
): TableLayout.LayoutParams {
    contract { callsInPlace(initParams, InvocationKind.EXACTLY_ONCE) }
    return TableLayout.LayoutParams(width, height, weight).apply(initParams)
}

@kotlin.OptIn(ExperimentalContracts::class)
inline fun TableRow.defaultLParams(
    width: Int = wrapContent,
    height: Int = wrapContent,
    weight: Float = 0f,
    initParams: TableRow.LayoutParams.() -> Unit = {},
): TableRow.LayoutParams {
    contract { callsInPlace(initParams, InvocationKind.EXACTLY_ONCE) }
    return TableRow.LayoutParams(width, height, weight).apply(initParams)
}

@Suppress("unused")
val ConstraintLayout.matchConstraint
    get() = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT

var View.backgroundColorResId: Int
    @Deprecated("", level = DeprecationLevel.ERROR) get() = throw Error()
    set(@ColorRes colorId) = setBackgroundColor(context.color(colorId))

var TextView.textColor: Int
    @Deprecated("", level = DeprecationLevel.ERROR) get() = throw Error()
    set(@ColorInt color) = setTextColor(color)

@kotlin.OptIn(ExperimentalContracts::class)
inline fun View.progressBar(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: ProgressBar.() -> Unit = {},
): ProgressBar {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return context.view(::ProgressBar, id, theme, initView)
}

fun Int.withAlpha(alpha: Int): Int {
    require(alpha in 0..0xFF)
    return this and 0x00FFFFFF or (alpha shl 24)
}

fun Context.sp(value: Int): Int = (value * resources.displayMetrics.scaledDensity).toInt()
fun View.sp(value: Int): Int = context.sp(value)


inline fun Context.toast(message: Int): Toast = Toast
    .makeText(this, message, Toast.LENGTH_SHORT)
    .apply {
        show()
    }

inline fun Context.toast(message: CharSequence): Toast = Toast
    .makeText(this, message, Toast.LENGTH_SHORT)
    .apply {
        show()
    }


@kotlin.OptIn(ExperimentalContracts::class)
inline fun View.viewFlipper(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: ViewFlipper.() -> Unit = {},
): ViewFlipper {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return context.view(::ViewFlipper, id, theme, initView)
}

@kotlin.OptIn(ExperimentalContracts::class)
inline fun View.horizontalProgressBar(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: ProgressBar.() -> Unit = {},
): ProgressBar {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return context.view(
        { ProgressBar(it, null, android.R.attr.progressBarStyleHorizontal) },
        id,
        theme,
        initView
    )
}

object Ids {
    inline operator fun component1() = R.id.id1
    inline operator fun component2() = R.id.id2
    inline operator fun component3() = R.id.id3
    inline operator fun component4() = R.id.id4
    inline operator fun component5() = R.id.id5
    inline operator fun component6() = R.id.id6
    inline operator fun component7() = R.id.id7
    inline operator fun component8() = R.id.id8
    inline operator fun component9() = R.id.id9
    inline operator fun component10() = R.id.id10
    inline operator fun component11() = R.id.id11
    inline operator fun component12() = R.id.id12
    inline operator fun component13() = R.id.id13
    inline operator fun component14() = R.id.id14
    inline operator fun component15() = R.id.id15
    inline operator fun component16() = R.id.id16
    inline operator fun component17() = R.id.id17
    inline operator fun component18() = R.id.id18
    inline operator fun component19() = R.id.id19
    inline operator fun component20() = R.id.id20
    inline operator fun component21() = R.id.id21
    inline operator fun component22() = R.id.id22
    inline operator fun component23() = R.id.id23
    inline operator fun component24() = R.id.id24
    inline operator fun component25() = R.id.id25
    inline operator fun component26() = R.id.id26
    inline operator fun component27() = R.id.id27
    inline operator fun component28() = R.id.id28
    inline operator fun component29() = R.id.id29
    inline operator fun component30() = R.id.id30
    inline operator fun component31() = R.id.id31
    inline operator fun component32() = R.id.id32
    inline operator fun component33() = R.id.id33
    inline operator fun component34() = R.id.id34
    inline operator fun component35() = R.id.id35
    inline operator fun component36() = R.id.id36
    inline operator fun component37() = R.id.id37
    inline operator fun component38() = R.id.id38
    inline operator fun component39() = R.id.id39
    inline operator fun component40() = R.id.id40
    inline operator fun component41() = R.id.id41
    inline operator fun component42() = R.id.id42
}

@kotlin.OptIn(ExperimentalContracts::class)
fun Context.materialAlertDialogBuilder(init: MaterialAlertDialogBuilder.() -> Unit): MaterialAlertDialogBuilder {
    contract { callsInPlace(init, InvocationKind.EXACTLY_ONCE) }
    return MaterialAlertDialogBuilder(this).apply(init)
}
