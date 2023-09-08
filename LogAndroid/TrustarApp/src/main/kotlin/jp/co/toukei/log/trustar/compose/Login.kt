@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)

package jp.co.toukei.log.trustar.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sebaslogen.resaca.rememberScoped
import jp.co.toukei.log.common.compose.AppPropTodo
import jp.co.toukei.log.common.compose.CircularProgress
import jp.co.toukei.log.common.compose.HeadButton
import jp.co.toukei.log.lib.compose.DefaultConfirmDialog
import jp.co.toukei.log.lib.compose.replaceMessage
import jp.co.toukei.log.lib.ctx.Ctx
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.enum.LoginState
import jp.co.toukei.log.trustar.errMessage
import jp.co.toukei.log.trustar.rest.response.LoginException
import jp.co.toukei.log.trustar.viewmodel.LoginVM

@Composable
fun Login(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    deviceId: String,
    loading: Boolean,
    companyCd: MutableState<String>,
    userId: MutableState<String>,
    preLogin: (companyId: String, userId: String, password: String) -> Unit,
) {
    val textColor = MaterialTheme.colorScheme.onPrimary
    val err = AppPropTodo.Color.orange

    val focused = textColor.copy(alpha = 0.7F)
    val unfocused = textColor.copy(alpha = 0.5F)
    val disabled = textColor.copy(alpha = 0.3F)
    val colors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = textColor,
        unfocusedTextColor = textColor,
        disabledTextColor = disabled,
        errorTextColor = textColor,
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        disabledContainerColor = Color.Transparent,
        errorContainerColor = Color.Transparent,
        cursorColor = textColor,
        errorCursorColor = err,
        selectionColors = TextSelectionColors(textColor, unfocused),
        focusedBorderColor = focused,
        unfocusedBorderColor = unfocused,
        disabledBorderColor = disabled,
        errorBorderColor = err,
        focusedLeadingIconColor = focused,
        unfocusedLeadingIconColor = unfocused,
        disabledLeadingIconColor = disabled,
        errorLeadingIconColor = err,
        focusedTrailingIconColor = focused,
        unfocusedTrailingIconColor = unfocused,
        disabledTrailingIconColor = disabled,
        errorTrailingIconColor = err,
        focusedLabelColor = focused,
        unfocusedLabelColor = unfocused,
        disabledLabelColor = disabled,
        errorLabelColor = err,
        focusedPlaceholderColor = unfocused,
        unfocusedPlaceholderColor = unfocused,
        disabledPlaceholderColor = disabled,
        errorPlaceholderColor = err,
        focusedSupportingTextColor = focused,
        unfocusedSupportingTextColor = unfocused,
        disabledSupportingTextColor = disabled,
        errorSupportingTextColor = err,
        focusedPrefixColor = focused,
        unfocusedPrefixColor = unfocused,
        disabledPrefixColor = disabled,
        errorPrefixColor = err,
        focusedSuffixColor = focused,
        unfocusedSuffixColor = unfocused,
        disabledSuffixColor = disabled,
        errorSuffixColor = err,
    )

    var company by companyCd
    var user by userId
    var password by remember {
        mutableStateOf("")
    }
    var visible by rememberScoped {
        mutableStateOf(false)
    }
    var showSupport by rememberScoped {
        mutableStateOf(false)
    }

    val tCompany = stringResource(id = R.string.company_id)
    val tUser = stringResource(id = R.string.user_id)
    val tPasswd = stringResource(id = R.string.password)

    val sc = remember(showSupport, company) {
        if (showSupport && company.isEmpty()) {
            Ctx.context.getString(
                R.string.s1_field_required,
                tCompany
            )
        } else null
    }
    val su = remember(showSupport, user) {
        if (showSupport && user.isEmpty()) {
            Ctx.context.getString(
                R.string.s1_field_required,
                tUser
            )
        } else null
    }
    val sp = remember(showSupport, password) {
        if (showSupport && password.isEmpty()) {
            Ctx.context.getString(
                R.string.s1_field_required,
                tPasswd
            )
        } else null
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .primaryBackground(),
        contentAlignment = Alignment.TopCenter,
    ) {
        val boxModifier = Modifier
            .width(460.dp)
            .padding(vertical = 8.dp)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(id = R.string.trustar),
                style = TextStyle(
                    color = textColor,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.2.sp,
                    shadow = Shadow(
                        color = Color(0x66000000),
                        offset = Offset(1F, 1F),
                        blurRadius = 4F,
                    ),
                ),
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 32.dp),
            )
            OutlinedTextField(
                modifier = boxModifier,
                value = company,
                onValueChange = {
                    if (it.length <= 10) company = it
                },
                singleLine = true,
                colors = colors,
                label = textLabelLambda(tCompany),
                keyboardOptions = KeyboardOptions(
                    autoCorrect = false,
                    keyboardType = KeyboardType.Ascii,
                    imeAction = ImeAction.Next
                ),
                supportingText = sc?.let { textLabelLambda(it) },
                isError = sc != null,
            )
            OutlinedTextField(
                modifier = boxModifier,
                value = user,
                onValueChange = {
                    if (it.length <= 10) user = it
                },
                singleLine = true,
                colors = colors,
                label = textLabelLambda(tUser),
                keyboardOptions = KeyboardOptions(
                    autoCorrect = false,
                    keyboardType = KeyboardType.Ascii,
                    imeAction = ImeAction.Next
                ),
                supportingText = su?.let { textLabelLambda(it) },
                isError = su != null,
            )
            OutlinedTextField(
                modifier = boxModifier,
                value = password,
                onValueChange = {
                    if (it.length <= 256) password = it
                },
                singleLine = true,
                colors = colors,
                label = textLabelLambda(tPasswd),
                keyboardOptions = KeyboardOptions(
                    autoCorrect = false,
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                trailingIcon = {
                    IconButton(onClick = { visible = !visible }) {
                        Icon(
                            imageVector = if (visible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = null
                        )
                    }
                },
                visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
                supportingText = sp?.let { textLabelLambda(it) },
                isError = sp != null,
            )
            val keyboardController = LocalSoftwareKeyboardController.current
            Box(
                contentAlignment = Alignment.Center,
            ) {
                CircularProgress(
                    modifier = Modifier
                        .alpha(if (loading) 1F else 0F),
                    color = textColor
                )
                HeadButton(
                    modifier = Modifier
                        .alpha(if (loading) 0F else 1F)
                        .padding(vertical = 32.dp),
                    horizontalPadding = 32,
                    verticalPadding = 12,
                    text = stringResource(id = R.string.login),
                    style = AppPropTodo.Text.defaultBold,
                    color = textColor,
                    contentColor = MaterialTheme.colorScheme.primary,
                ) {
                    val c = company
                    val u = user
                    val p = password

                    if (c.isEmpty() || u.isEmpty() || p.isEmpty()) {
                        showSupport = true
                    } else {
                        keyboardController?.hide()
                        preLogin(c, u, p)
                    }
                }
            }

            Text(
                modifier = Modifier
                    .padding(vertical = 24.dp),
                text = stringResource(id = R.string.login_footer_info),
                color = textColor,
                style = AppPropTodo.Text.size16,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.weight(1F))
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(id = R.string.terminal_id_s1, deviceId),
                color = textColor,
                textAlign = TextAlign.Start,
                style = AppPropTodo.Text.default,
            )
        }
    }
}

private fun textLabelLambda(arg: String): @Composable () -> Unit {
    return {
        Text(
            text = arg,
        )
    }
}

@Composable
fun Login(
    vm: LoginVM,
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    snbHost: SnackbarHostState,
    deviceId: String,
    login: (LoginState.Ok) -> Unit,
) {
    val s by vm.loginState
    val state = s

    Login(
        modifier = modifier,
        paddingValues = paddingValues,
        deviceId = deviceId,
        loading = when (state) {
            is LoginState.Loading,
            is LoginState.PreLogin,
            is LoginState.Ok,
            -> true

            else -> false
        },
        companyCd = vm.inputCompanyCd,
        userId = vm.inputUser,
        preLogin = vm::preLogin,
    )

    val cancel = vm::reset

    when (state) {
        is LoginState.PreLogin -> {
            if (state.response.loginResult.isLoggedIn) {
                DefaultConfirmDialog(
                    confirmButtonText = R.string.yes,
                    dismissButtonText = R.string.no,
                    content = R.string.login_sso_alert_msg,
                    dismissButtonClick = cancel
                ) {
                    vm.login(state)
                }
            } else {
                LaunchedEffect(Unit) {
                    vm.login(state)
                }
            }
        }

        is LoginState.Err -> {
            val l = state.error as? LoginException
            val e: Pair<Int?, Int>? = when (l?.messageCode) {
                "401.1", "401.2" -> R.string.login_auth_err to R.string.login_auth_wrong_msg
                "401.3" -> null to R.string.login_auth_password_expired_msg
                "401.5" -> R.string.login_auth_err to R.string.login_auth_invalid_account_msg
                else -> null
            }
            if (e != null) {
                DefaultConfirmDialog(
                    onDismissRequest = cancel,
                    content = e.second,
                    title = e.first,
                    confirmButtonClick = cancel
                )
            } else {
                LaunchedEffect(Unit) {
                    snbHost.replaceMessage(state.error.errMessage())
                }
            }
        }

        is LoginState.Disabled -> {
            DefaultConfirmDialog(
                content = R.string.login_disabled,
                confirmButtonClick = cancel
            )
        }

        is LoginState.Ok -> {
            LaunchedEffect(Unit) {
                login(state)
            }
        }

        else -> {}
    }

}
