package jp.co.toukei.log.trustar.feature.home.ui

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.Gravity
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import jp.co.toukei.log.lib.common.backgroundColorResId
import jp.co.toukei.log.trustar.R
import splitties.views.dsl.core.*

class HomeActivityUI(override val ctx: Context) : Ui {

    val bottomNavigation: BottomNavigationView

    override val root: View = ctx.frameLayout {
        id = R.id.main_root
        fitsSystemWindows = true
        add(verticalLayout {
            weightSum = 1F
            add(frameLayout {
                id = R.id.id_container
                backgroundColorResId = R.color.defaultBackground
            }, lParams(matchParent, 0, weight = 1F))
            bottomNavigation = add(view(::BottomNavigationView) {
                id = R.id.id1
                backgroundColorResId = R.color.home_bnv_bg
                val color = ColorStateList(
                    arrayOf(
                        intArrayOf(-android.R.attr.state_checked),
                        intArrayOf(android.R.attr.state_checked)
                    ),
                    intArrayOf(
                        0xff777777.toInt(),
                        Color.BLACK
                    )
                )
                itemIconTintList = color
                itemTextColor = color
                inflateMenu(R.menu.home_bottom_navigation)
                labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_LABELED
                itemTextAppearanceActive = R.style.LargeLabel
                itemTextAppearanceInactive = R.style.SmallLabel
            }, lParams(matchParent, wrapContent, gravity = Gravity.BOTTOM))
        }, lParams(matchParent, matchParent))
    }
}
