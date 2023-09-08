package jp.co.toukei.log.common.compose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController

val AppColorSchemeLight = lightColorScheme(
    primary = Color(0xff009af3), //cursor
    onPrimary = Color(0xffffffff),//
    primaryContainer = Color(0xff68bded),//fab
    onPrimaryContainer = Color(0xFFFFFFFF),
    inversePrimary = Color(0xffff0000),
    secondary = Color(0xffE6E6E6),
    onSecondary = Color(0xff333333),
    secondaryContainer = Color(0xffff0000),
    onSecondaryContainer = Color(0xffff0000),
    tertiary = Color(0xffff0000),
    onTertiary = Color(0xffff0000),
    tertiaryContainer = Color(0xffff0000),
    onTertiaryContainer = Color(0xffff0000),
    background = Color(0xffF5F5F5),//bg
    onBackground = Color(0xff333333),//ripple, text
    surface = Color(0xffffffff), //appbar,context menu bg
    onSurface = Color(0xff333333), //appbar text,
    surfaceVariant = Color(0xFF035483),
    onSurfaceVariant = Color(0xFF444444),//menu icon, nav tint
    surfaceTint = Color(0xFF838383),
    inverseSurface = Color(0xff222222),//snack
    inverseOnSurface = Color(0xffFFFFFF),//snack text
    error = Color(0xffff0000),
    onError = Color(0xffff0000),
    errorContainer = Color(0xffff0000),
    onErrorContainer = Color(0xffff0000),
    outline = Color(0xcccccccc),
    outlineVariant = Color(0xffff0000),
    scrim = Color(0xFF585858), //bs scrim
)


val AppColorSchemeDark = AppColorSchemeLight

private val textStyleLight = TextStyle.Default

val AppTypographyLight = Typography(
    bodyLarge = textStyleLight.copy(
        color = Color(0xff333333)
    )
)

val AppTypographyDark = AppTypographyLight


class AppColor(
    val chatInputText: Color = Color(0xff6d6d6d),
    val chatInputBg: Color = Color(0xffE6E6E6),
    val chatInputBarBg: Color = Color(0xffF3F3F3),
    val chatCardBg: Color = Color(0xffE6E6E6),
    val chatCardBgSelf: Color = Color(0xff68bded),
    val chatCardColorSelf: Color = Color(0xffFFFFFF),
    val chatReaderPopupBg: Color = Color(0xffE6E6E6),
    val chatTipBg: Color = Color(0xFF4E5F61),
    val chatTipText: Color = Color(0xffE6E6E6),

    val divider: Color = Color(0xCCE6E6E6),
    val textColor: Color = Color(0xff333333),
    val redHighlightColor: Color = Color(0xFFDB0000),
    val dialogTitle: Color = AppColorSchemeLight.primary,
    val numInputBg: Color = Color(0xFFE6E6E6),
    val listItemBg: Color = Color(0xffffffff),
)

val AppColorLight = AppColor()
val AppColorDark = AppColor()

val LocalAppColor: ProvidableCompositionLocal<AppColor> = staticCompositionLocalOf {
    AppColorLight
}

object AppPropTodo {

    object Font {
        val default = 16.sp
        val collectDefault = 16.sp
        val collectDefaultHeader = 18.sp
        val dialogTitle = 18.sp
    }

    object Shape {
        val dialog = ShapeDefaults.Small
        val workButtonShape = RoundedCornerShape(8.dp)
    }

    object Color {
        val bottomNaviBg = Color(0xffe7f4fe)
        val bottomNaviTint = Color(0xff777777)
        val bottomNaviSelected = Color(0xff111111)
        val bottomNaviSelectedIndicator = Color(0xFFDBECFA)
        val bottomNaviBadge = Color(0xFFAF1E1E)
        val bottomNaviBadgeText = Color(0xFFFFFFFF)
        val working = Color(0xFFf79646)
        val binOpNotice = Color(0xFFFA2222)
        val buttonDisabled = Color(0xcccccccc)
        val workButton = Color(0xffffffff)
        val bellNormal = Color(0xff4B4B4B)
        val orange = Color(0xffEE8833)
        val bellImportant = Color(0xffEE8833)
        val noticeTitle = Color(0xffEE8833)
        val binFinishedColor = Color(0xffeeddbb)
        val deepPrimary = Color(0xff2a24a7)
        val unsignedBg = Color(0xff58d489)
        val signedBg = Color(0xffdfcdbc)
        val iconTint = Color(0xff666666)
    }

    object Text {
        val default = TextStyle(
            fontSize = 14.sp,
            letterSpacing = 0.4.sp,
        )
        val size16 = TextStyle(
            fontSize = 16.sp,
            letterSpacing = 0.4.sp,
        )
        val size18 = TextStyle(
            fontSize = 18.sp,
            letterSpacing = 0.4.sp,
        )
        val small = TextStyle(
            fontSize = 12.sp,
            letterSpacing = 0.2.sp,
        )
        val mini = TextStyle(
            fontSize = 8.sp,
            lineHeight = 12.sp,
            letterSpacing = 0.2.sp,
        )
        val defaultBold = default.copy(
            fontWeight = FontWeight.Bold,
        )
        val bold16 = size16.copy(
            fontWeight = FontWeight.Bold,
        )
        val smallBold = small.copy(
            fontWeight = FontWeight.Bold,
        )

    }
}

@Composable
fun M3ContentTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val scheme = if (darkTheme) {
        AppColorSchemeDark
    } else {
        AppColorSchemeLight
    }

    val sysUiController = rememberSystemUiController()
    SideEffect {
        sysUiController.setSystemBarsColor(
            color = scheme.primary
        )
        sysUiController.setNavigationBarColor(Color.Black)
    }

    MaterialTheme(
        colorScheme = scheme,
//        typography = if (darkTheme) AppTypographyDark else AppTypographyLight,
//        shapes = shapes,
        content = {
            CompositionLocalProvider(
                LocalAppColor provides if (darkTheme) AppColorDark else AppColorLight
            ) {
                content()
            }
        }
    )
}
