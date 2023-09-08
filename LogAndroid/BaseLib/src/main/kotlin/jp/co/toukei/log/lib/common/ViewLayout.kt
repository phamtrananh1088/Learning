@file:Suppress("NOTHING_TO_INLINE")

package jp.co.toukei.log.lib.common

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
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
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.helper.widget.Flow
import androidx.constraintlayout.widget.Barrier
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID
import androidx.constraintlayout.widget.Group
import androidx.constraintlayout.widget.Guideline
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.RelativeCornerSize
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import jp.co.toukei.log.lib.R
import splitties.resources.color
import splitties.views.dsl.core.NO_THEME
import splitties.views.dsl.core.inflate
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

inline fun ConstraintLayout.LayoutParams.alignBottom(@IdRes id: Int) {
    bottomToBottom = id
}

inline fun TextView.singleLine() {
    maxLines = 1
    isSingleLine = true
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

inline fun <T : View> T.invisible() = apply {
    visibility = View.INVISIBLE
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

inline fun <T : View> T.showOrInvisible(predicate: Boolean) = apply {
    if (predicate) show() else invisible()
}

inline fun <T : View> T.enable() = apply {
    isEnabled = true
}

inline fun <T : View> T.disable() = apply {
    isEnabled = false
}

inline fun <T : View> T.enableIf(predicate: Boolean) = apply {
    if (predicate) enable() else disable()
}

inline fun LinearLayout.gravityCenterHorizontal() {
    gravity = Gravity.CENTER_HORIZONTAL
}

inline fun LinearLayout.gravityCenterVertical() {
    gravity = Gravity.CENTER_VERTICAL
}

inline fun LinearLayout.gravityCenter() {
    gravity = Gravity.CENTER
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

inline fun View.viewPager2(init: (ViewPager2).() -> Unit): ViewPager2 {
    return view(::ViewPager2, initView = init)
}


fun RecyclerView.addDividerItemDecoration(orientation: Int, color: Int, sizePx: Int) {
    addItemDecoration(DividerItemDecoration(context, orientation).apply {
        setDrawable(GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            setColor(ColorStateList.valueOf(color))
            setSize(sizePx, sizePx)
        })
    })
}


inline fun View.longSnackbar(@StringRes message: Int) = Snackbar
    .make(this, message, Snackbar.LENGTH_LONG)
    .apply { show() }

inline fun View.longSnackbar(message: CharSequence) = Snackbar
    .make(this, message, Snackbar.LENGTH_LONG)
    .apply { show() }

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
inline fun Context.swipeRefreshLayout(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: SwipeRefreshLayout.() -> Unit = {},
): SwipeRefreshLayout {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return view(::SwipeRefreshLayout, id, theme, initView)
}

@kotlin.OptIn(ExperimentalContracts::class)
inline fun View.swipeRefreshLayout(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: SwipeRefreshLayout.() -> Unit = {},
): SwipeRefreshLayout {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return context.swipeRefreshLayout(id, theme, initView)
}

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
inline fun ConstraintLayout.barrier(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: Barrier.() -> Unit = {},
): Barrier {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return view(::Barrier, id, theme, initView)
}

@kotlin.OptIn(ExperimentalContracts::class)
inline fun ConstraintLayout.flow(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: Flow.() -> Unit = {},
): Flow {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return view(::Flow, id, theme, initView)
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

@kotlin.OptIn(ExperimentalContracts::class)
inline fun View.gridLayout(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: GridLayout.() -> Unit = {},
): GridLayout {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return context.view(::GridLayout, id, theme, initView)
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
inline fun View.tableLayout(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: TableLayout.() -> Unit = {},
): TableLayout {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return context.view(::TableLayout, id, theme, initView)
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
inline fun TableLayout.tableRow(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: TableRow.() -> Unit = {},
): TableRow {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return context.view(::TableRow, id, theme, initView)
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


var TextView.hintResource: Int
    @Deprecated("", level = DeprecationLevel.ERROR) get() = throw Error()
    set(@StringRes resId) = setHint(resId)

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


@kotlin.OptIn(ExperimentalContracts::class)
inline fun Context.appCompatCheckBox(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: AppCompatCheckBox.() -> Unit = {},
): AppCompatCheckBox {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return view(::AppCompatCheckBox, id, theme, initView)
}

@kotlin.OptIn(ExperimentalContracts::class)
inline fun View.appCompatCheckBox(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: AppCompatCheckBox.() -> Unit = {},
): AppCompatCheckBox {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return context.appCompatCheckBox(id, theme, initView)
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

inline fun Context.longToast(message: Int): Toast = Toast
    .makeText(this, message, Toast.LENGTH_LONG)
    .apply {
        show()
    }

inline fun Context.longToast(message: CharSequence): Toast = Toast
    .makeText(this, message, Toast.LENGTH_LONG)
    .apply {
        show()
    }

@kotlin.OptIn(ExperimentalContracts::class)
inline fun View.textInputLayoutOutlined(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: TextInputLayout.() -> Unit = {},
): TextInputLayout {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return context.inflate(R.layout.outlined_box, id, theme, initView)
}


@kotlin.OptIn(ExperimentalContracts::class)
inline fun TextInputLayout.textInputEditText(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: TextInputEditText.() -> Unit = {},
): TextInputEditText {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return context.view(::TextInputEditText, id, theme, initView)
}

@kotlin.OptIn(ExperimentalContracts::class)
inline fun View.materialButton(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: MaterialButton.() -> Unit = {},
): MaterialButton {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return context.inflate(R.layout.material_button, id, theme, initView)
}

@kotlin.OptIn(ExperimentalContracts::class)
inline fun View.materialButtonOutlined(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: MaterialButton.() -> Unit = {},
): MaterialButton {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return context.inflate(R.layout.outlined_button, id, theme, initView)
}

@kotlin.OptIn(ExperimentalContracts::class)
inline fun View.materialButtonUnelevated(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: MaterialButton.() -> Unit = {},
): MaterialButton {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return context.inflate(R.layout.unelevated_button, id, theme, initView)
}

@kotlin.OptIn(ExperimentalContracts::class)
inline fun View.materialButtonOutlinedIconOnly(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: MaterialButton.() -> Unit = {},
): MaterialButton {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return context.inflate(R.layout.outlined_button_icon_only, id, theme, initView)
}

@kotlin.OptIn(ExperimentalContracts::class)
inline fun View.materialButtonToggleGroup(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: MaterialButtonToggleGroup.() -> Unit = {},
): MaterialButtonToggleGroup {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return context.view(::MaterialButtonToggleGroup, id, theme, initView)
}

@kotlin.OptIn(ExperimentalContracts::class)
inline fun Context.switchCompact(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: SwitchCompat.() -> Unit = {},
): SwitchCompat {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return view(::SwitchCompat, id, theme, initView)
}

@kotlin.OptIn(ExperimentalContracts::class)
inline fun View.switchCompact(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: SwitchCompat.() -> Unit = {},
): SwitchCompat {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return context.switchCompact(id, theme, initView)
}

@kotlin.OptIn(ExperimentalContracts::class)
inline fun Context.switchMaterial(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: SwitchMaterial.() -> Unit = {},
): SwitchMaterial {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return view(::SwitchMaterial, id, theme, initView)
}

@kotlin.OptIn(ExperimentalContracts::class)
inline fun View.switchMaterial(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    initView: SwitchMaterial.() -> Unit = {},
): SwitchMaterial {
    contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
    return context.switchMaterial(id, theme, initView)
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
