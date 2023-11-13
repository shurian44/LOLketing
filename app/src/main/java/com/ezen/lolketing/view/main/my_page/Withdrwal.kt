package com.ezen.lolketing.view.main.my_page

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ezen.lolketing.R
import com.ezen.lolketing.util.startActivity
import com.ezen.lolketing.util.toast
import com.ezen.lolketing.view.login.LoginActivity
import com.ezen.lolketing.view.main.LoadingDialog
import com.ezen.lolketing.view.main.TitleBar
import com.ezen.lolketing.view.ui.theme.*

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun WithdrawalContainer(
    routeAction: MyPageRouteAction,
    viewModel: MyPageViewModel = hiltViewModel()
) {

    val activity = LocalContext.current as? Activity
    var textState by remember {
        mutableStateOf("")
    }
    var isError by remember {
        mutableStateOf(false)
    }
    var isEnable by remember {
        mutableStateOf(true)
    }
    var isLoading by remember {
        mutableStateOf(false)
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val deleteState = viewModel.deleteUserState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
    ) {

        /** 타이틀 바 **/
        TitleBar(onBackClick = { routeAction.popBackStack() }, title = stringResource(id = R.string.withdrawal))

        /** 탈퇴하기 컨텐츠 **/
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 56.dp)
                .align(Alignment.Center)
        ) {
            Text(
                text = buildAnnotatedString {
                    append(stringResource(id = R.string.guide_withdrawal))

                    withStyle(
                        style = SpanStyle(
                            color = SubColor
                        )
                    ) {
                        append("'${stringResource(id = R.string.label_withdrawal)}'")
                    }

                    append(stringResource(id = R.string.guide_withdrawal2))
                },
                style = Typography.labelLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            OutlinedTextField(
                value = textState,
                onValueChange = {
                    textState = it
                    isError = false
                },
                maxLines = 1,
                placeholder = { Text(text = stringResource(id = R.string.label_withdrawal)) },
                isError = isError,
//                colors = TextFieldDefaults.outlinedTextFieldColors(
//                    focusedBorderColor = SubColor,
//                    unfocusedBorderColor = White,
//                    errorBorderColor = Red,
//                    cursorColor = SubColor,
//                    textColor = White
//                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            )
        }

        /** 취소 & 회원 탈퇴 **/
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            Button(
                onClick = { routeAction.popBackStack() },
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = SubColor
                ),
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.cancel),
                    style = Typography.labelLarge
                )
            }

            Button(
                onClick = {
                    if (textState != "탈퇴하기") {
                        isError = true
                    } else {
                        isEnable = false
                        viewModel.deleteUser()
                    }
                },
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MainColor,
                    disabledContainerColor = LightBlack
                ),
                enabled = isEnable,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.label_withdrawal),
                    style = Typography.labelLarge
                )
            }
        }

        when(deleteState.value) {
            MyPageViewModel.Event.Init -> {}
            /** 로딩 프로그레스 **/
            is MyPageViewModel.Event.Loading -> {
                isLoading = true
            }
            MyPageViewModel.Event.Success -> {
                isLoading = false
                viewModel.logout()
                activity?.let {
                    it.finishAffinity()
                    it.startActivity(LoginActivity::class.java, Intent.FLAG_ACTIVITY_CLEAR_TOP)
                }
            }
            MyPageViewModel.Event.Failure -> {
                isLoading = false
                activity?.toast(stringResource(id = R.string.error_withdrawal))
                isEnable = true
            }
        }

        LoadingDialog(isShow = isLoading)

    }

}