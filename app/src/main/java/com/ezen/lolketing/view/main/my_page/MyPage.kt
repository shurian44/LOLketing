package com.ezen.lolketing.view.main.my_page

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ezen.lolketing.R
import com.ezen.lolketing.util.*
import com.ezen.lolketing.view.login.LoginActivity
import com.ezen.lolketing.view.login.join.JoinDetailActivity
import com.ezen.lolketing.view.main.TitleBar
import com.ezen.lolketing.view.main.my_page.cache.CacheChargingActivity
import com.ezen.lolketing.view.ui.theme.*

@Composable
fun MyPageContainer(
    routeAction: MyPageRouteAction,
    viewModel: MyPageViewModel = hiltViewModel()
) {

    val activity = (LocalContext.current as? Activity)
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            viewModel.getUserInfo()
        }

    val userInfo = viewModel.userInfoState.collectAsState()
    val couponInfo = viewModel.couponInfoState.collectAsState()
    viewModel.getUserInfo()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
    ) {
        TitleBar(onBackClick = { activity?.finish() }, title = stringResource(id = R.string.my_page))
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 56.dp)
        ) {

            item {
                Text(
                    text = userInfo.value?.nickname ?: "",
                    style = Typography.titleLarge,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }
            item {
                userInfo.value?.id?.let {
                    Text(
                        text = "($it)",
                        style = Typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                }
            }
            item { Spacer(modifier = Modifier.height(15.dp)) }
            /** 유저 등급 정보 **/
            item {
                UserGradeInfo(
                    gradeName = getGradeName(userInfo.value?.grade ?: Code.UNKNOWN.code),
                    point = userInfo.value?.point ?: 0L
                )
            }

            item { Spacer(modifier = Modifier.height(25.dp)) }
            /** 캐시 & 쿠폰 정보 **/
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    UserInfoCard(
                        title = stringResource(id = R.string.retained_cache),
                        content = (userInfo.value?.cache ?: 0).priceFormat(),
                        buttonText = stringResource(id = R.string.button_charging_cache),
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        launcher.launch(activity?.createIntent(CacheChargingActivity::class.java))
                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    UserInfoCard(
                        title = stringResource(id = R.string.usable_coupon),
                        content = couponInfo.value,
                        buttonText = stringResource(id = R.string.move_coupon_box),
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        routeAction.navToCoupon()
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(25.dp)) }
            /** 구매내역 **/
            item {
                UserInfoRowItem(content = stringResource(id = R.string.purchase_history), isBlack = false) {
                    routeAction.navToPurchaseHistory()
                }
            }
            /** 내 정보 수정 **/
            item {
                UserInfoRowItem(content = stringResource(id = R.string.my_info_modify), isBlack = true) {
                    launcher.launch(
                        activity?.createIntent(JoinDetailActivity::class.java).also { intent ->
                            intent?.putExtra(JoinDetailActivity.MODIFY, true)
                        }
                    )
                }
            }
            /** 로그아웃 **/
            item {
                UserInfoRowItem(content = stringResource(id = R.string.logout), isBlack = false) {
                    viewModel.logout()
                    activity?.let {
                        it.finishAffinity()
                        it.startActivity(LoginActivity::class.java, Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    }
                }
            }
            /** 회원 탈퇴 **/
            item {
                UserInfoRowItem(content = stringResource(id = R.string.withdrawal), isBlack = true) {
                    routeAction.navToWithdrawal()
                }
            }

        }
    }

}

/** 유저 등급 UI - 등급 아이콘, 등급명, 프로그래스바 **/
@Composable
fun UserGradeInfo(gradeName: String, point: Long) {

    val res = getGradeImageRes(gradeName)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = res),
            contentDescription = "icon",
            modifier = Modifier.size(width = 117.dp, height = 137.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(text = gradeName, style = Typography.titleMedium)

        Spacer(modifier = Modifier.height(16.dp))

        CustomProgressBar(grade = gradeName, point = point)

    }

}

/** 프로그래스바 커스텀 **/
@Composable
fun CustomProgressBar(grade: String, point: Long) {
    val max = getMaxProgress(grade)
    var isStart by remember { mutableStateOf(false) }
    val widthState by animateFloatAsState(
        targetValue = if (isStart) maxOf((point.toFloat() / max), 0.05f) else 0f,
        animationSpec = tween(durationMillis = 1500)
    )
    val onClickAction = remember(Unit) {
        { isStart = true }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(24.dp)
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Gray)
    ) {
        Box(
            modifier = Modifier
                .height(24.dp)
                .fillMaxWidth(fraction = widthState)
                .clip(RoundedCornerShape(24.dp))
                .background(SubColor)
        )
        Text(
            text = "$point / $max",
            style = Typography.labelMedium,
            modifier = Modifier
                .padding(start = 16.dp)
                .align(
                    Alignment.CenterStart
                )
        )
    }

    LaunchedEffect(Unit) {
        onClickAction()
    }
}

fun getMaxProgress(grade: String) = when (grade) {
    Grade.BRONZE.gradeName -> 3_000
    Grade.SILVER.gradeName -> 30_000
    Grade.GOLD.gradeName, Grade.PLATINUM.gradeName -> 300_000
    else -> 1
}

/** 보유 캐시, 사용 가능 쿠폰 카드 UI **/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInfoCard(
    title: String,
    content: String,
    buttonText: String,
    modifier: Modifier = Modifier,
    onButtonClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, SubColor),
        colors = CardDefaults.cardColors(
            containerColor = Black
        ),
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(11.dp),
            modifier = Modifier
                .padding(7.dp)
                .background(Black)
        ) {
            Text(text = title, style = Typography.titleLarge)
            Text(text = content, style = Typography.labelMedium)
            Button(
                onClick = onButtonClick,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SubColor
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = buttonText, style = Typography.labelLarge)
            }
        }
    }
}

/** 하단 텝 UI **/
@Composable
fun UserInfoRowItem(content: String, isBlack: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (isBlack) Black else LightBlack

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .background(backgroundColor)
            .clickable {
                onClick()
            }
            .padding(horizontal = 20.dp)
    ) {

        Text(text = content, style = Typography.labelMedium)
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(id = R.drawable.ic_next),
            contentDescription = "next"
        )

    }
}
