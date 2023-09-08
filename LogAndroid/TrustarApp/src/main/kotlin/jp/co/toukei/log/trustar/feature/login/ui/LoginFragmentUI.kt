package jp.co.toukei.log.trustar.feature.login.ui

import android.content.Context
import android.text.InputType
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout
import jp.co.toukei.log.lib.common.below
import jp.co.toukei.log.lib.common.boldTypeface
import jp.co.toukei.log.lib.common.bottomToBottomParent
import jp.co.toukei.log.lib.common.constraintLayout
import jp.co.toukei.log.lib.common.defaultLParams
import jp.co.toukei.log.lib.common.endToEndParent
import jp.co.toukei.log.lib.common.hintResource
import jp.co.toukei.log.lib.common.horizontalCenterInParent
import jp.co.toukei.log.lib.common.invisible
import jp.co.toukei.log.lib.common.matchConstraint
import jp.co.toukei.log.lib.common.nestedScrollView
import jp.co.toukei.log.lib.common.progressBar
import jp.co.toukei.log.lib.common.show
import jp.co.toukei.log.lib.common.singleLine
import jp.co.toukei.log.lib.common.startToStartParent
import jp.co.toukei.log.lib.common.textInputEditText
import jp.co.toukei.log.lib.common.textInputLayoutOutlined
import jp.co.toukei.log.lib.common.topToTopParent
import jp.co.toukei.log.lib.common.whiteText
import jp.co.toukei.log.lib.lengthFilter
import jp.co.toukei.log.trustar.Config
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.common.ui.CoordinatorUI
import jp.co.toukei.log.trustar.whiteButtonStyle
import splitties.dimensions.dip
import splitties.views.centerText
import splitties.views.dsl.coordinatorlayout.defaultLParams
import splitties.views.dsl.core.add
import splitties.views.dsl.core.frameLayout
import splitties.views.dsl.core.lParams
import splitties.views.dsl.core.matchParent
import splitties.views.dsl.core.textView
import splitties.views.dsl.core.wrapContent
import splitties.views.gravityCenter
import splitties.views.gravityCenterHorizontal
import splitties.views.horizontalPadding
import splitties.views.padding
import splitties.views.verticalPadding

class LoginFragmentUI(val context: Context) : CoordinatorUI(context) {
    val login: View
    val companyId: EditText
    val companyIdParent: TextInputLayout
    val userId: EditText
    val userIdParent: TextInputLayout
    val password: EditText
    val passwordParent: TextInputLayout
    private val progressBar: View

    @JvmField
    val constraintLayout = coordinatorLayout.run {
        setBackgroundResource(R.drawable.window_placeholder)
        add(nestedScrollView {
            isFillViewport = true
            add(constraintLayout {
                maxWidth = dip(512)
                padding = dip(48)

                add(textView {
                    setText(R.string.trustar)
                    id = R.id.id1
                    whiteText()
                    textSize = 26F
                    boldTypeface()
                    verticalPadding = dip(32)
                    horizontalPadding = dip(8)
                    setShadowLayer(4F, 1F, 1F, 0x66000000)
                }, defaultLParams(wrapContent, wrapContent) {
                    topToTopParent()
                    horizontalCenterInParent()
                })

                /* 企業ID */
                companyIdParent =
                    add(textInputLayoutOutlined(theme = R.style.LoginTheme_TextInputLayoutStyle) {
                        id = R.id.id2
                        companyId = add(textInputEditText {
                            singleLine()
                            inputType = InputType.TYPE_CLASS_TEXT
                            hintResource = R.string.company_id
                            imeOptions =
                                EditorInfo.IME_ACTION_NEXT or EditorInfo.IME_FLAG_NO_EXTRACT_UI
                            lengthFilter(10)
                        }, lParams(matchParent, wrapContent))
                    }, defaultLParams(matchConstraint, wrapContent) {
                        horizontalCenterInParent()
                        below(R.id.id1)
                        topMargin = dip(16)
                    })

                /* ユーザーID */
                userIdParent =
                    add(textInputLayoutOutlined(theme = R.style.LoginTheme_TextInputLayoutStyle) {
                        id = R.id.id3
                        userId = add(textInputEditText {
                            singleLine()
                            inputType = InputType.TYPE_CLASS_TEXT
                            hintResource = R.string.user_id
                            imeOptions =
                                EditorInfo.IME_ACTION_NEXT or EditorInfo.IME_FLAG_NO_EXTRACT_UI
                            lengthFilter(10)
                        }, lParams(matchParent, wrapContent))
                    }, defaultLParams(matchConstraint, wrapContent) {
                        horizontalCenterInParent()
                        below(R.id.id2)
                        topMargin = dip(16)
                    })

                /* パスワード */
                passwordParent =
                    add(textInputLayoutOutlined(theme = R.style.LoginTheme_TextInputLayoutStyle) {
                        endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
                        setEndIconDrawable(R.drawable.design_password_eye_reversed)
                        id = R.id.id4
                        password = add(textInputEditText {
                            singleLine()
                            inputType =
                                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                            hintResource = R.string.password
                            imeOptions =
                                EditorInfo.IME_ACTION_DONE or EditorInfo.IME_FLAG_NO_EXTRACT_UI
                            lengthFilter(256)
                        }, lParams(matchParent, wrapContent))
                    }, defaultLParams(matchConstraint, wrapContent) {
                        horizontalCenterInParent()
                        below(R.id.id3)
                        topMargin = dip(16)
                    })

                /* ログイン */
                add(frameLayout {
                    id = R.id.id5
                    login = add(textView {
                        setText(R.string.login)
                        whiteButtonStyle()
                    }, lParams(matchParent, wrapContent, gravity = gravityCenter))
                    progressBar = add(progressBar {
                        invisible()
                        isIndeterminate = true
                    }, lParams(matchParent, wrapContent, gravity = gravityCenter))
                }, defaultLParams(wrapContent, wrapContent) {
                    topMargin = dip(48)
                    horizontalCenterInParent()
                    below(R.id.id4)
                    constrainedWidth = true
                })

                add(textView {
                    setText(R.string.login_footer_info)
                    id = R.id.id6
                    textSize = 16F
                    padding = dip(8)
                    centerText()
                }, defaultLParams(matchConstraint, wrapContent) {
                    horizontalCenterInParent()
                    below(R.id.id5)
                    topMargin = dip(48)
                })

                add(textView {
                    textSize = 14F
                    text = context.getString(R.string.terminal_id_s1, Config.androidId)
                }, defaultLParams(matchConstraint, wrapContent) {
                    topMargin = dip(32)
                    below(R.id.id6)
                    bottomToBottomParent()
                    startToStartParent()
                    endToEndParent()
                    horizontalBias = 0F
                    verticalBias = 1F
                })
            }, lParams(matchParent, matchParent, gravity = gravityCenterHorizontal))
        }, defaultLParams(matchParent, matchParent))
    }

    fun setIsLoading(loading: Boolean) {
        if (loading) {
            login.invisible()
            progressBar.show()
        } else {
            login.show()
            progressBar.invisible()
        }
    }
}
