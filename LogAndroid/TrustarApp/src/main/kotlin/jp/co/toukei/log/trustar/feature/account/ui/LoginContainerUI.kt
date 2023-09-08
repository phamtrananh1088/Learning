package jp.co.toukei.log.trustar.feature.account.ui

import android.content.Context
import android.view.View
import jp.co.toukei.log.trustar.R
import splitties.views.dsl.core.Ui
import splitties.views.dsl.core.frameLayout

class LoginContainerUI(override val ctx: Context) : Ui {

    override val root: View = ctx.frameLayout {
        id = R.id.main_root
        fitsSystemWindows = true
    }
}
