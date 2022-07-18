package com.ezen.lolketing.view.main.my_page.cache

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ezen.lolketing.R
import com.ezen.lolketing.model.CouponInfo
import com.ezen.lolketing.util.Code
import com.ezen.lolketing.util.findCodeName
import com.ezen.lolketing.util.getSimpleDateFormatMillSec
import com.ezen.lolketing.util.toast
import com.ezen.lolketing.view.main.TitleBar
import com.ezen.lolketing.view.main.my_page.MyPageRouteAction
import com.ezen.lolketing.view.main.my_page.MyPageViewModel
import com.ezen.lolketing.view.ui.theme.*

@Composable
fun CouponListContainer(
    routeAction: MyPageRouteAction,
    viewModel: MyPageViewModel = hiltViewModel()
) {

    val listState = viewModel.couponListState.collectAsState()
    val updateState = viewModel.updateState.collectAsState()
    val context = LocalContext.current
    viewModel.getCouponList()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 56.dp)
        ) {
            if (listState.value.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillParentMaxSize()) {
                        Text(text = stringResource(id = R.string.coupon_empty), style = Typography.labelMedium)
                    }
                }
            } else {
                listState.value.forEachIndexed { index, couponInfo ->
                    item {
                        CouponItem(index = index, info = couponInfo, viewModel = viewModel)
                    }
                }
            }
        }

        when (updateState.value) {
            MyPageViewModel.Event.Init -> {}
            MyPageViewModel.Event.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            MyPageViewModel.Event.Failure -> {
                context.toast(R.string.error_unexpected)
            }
            MyPageViewModel.Event.Success -> {
                context.toast(stringResource(id = R.string.coupon_used))
            }
        }

        TitleBar(
            onBackClick = { routeAction.popBackStack() },
            title = stringResource(id = R.string.coupon_box)
        )

    }
}

/** 쿠폰 아이템 UI **/
@Composable
fun CouponItem(index: Int, info: CouponInfo, viewModel: MyPageViewModel) {
    val color = if (index % 2 == 0) LightBlack else Black
    val limit = getSimpleDateFormatMillSec(info.limit, "yyyy-MM-dd mm:dd") ?: 0
    val isExpiry = limit < System.currentTimeMillis()
    val isEnable = (info.use == Code.USED.code || isExpiry).not()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color)
            .padding(vertical = 11.dp, horizontal = 20.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            Row {
                Text(
                    text = findCodeName(info.title),
                    style = Typography.titleMedium,
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "${info.limit} 까지",
                    style = Typography.labelMedium,
                    color = Gray,
                )
            }


            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = buildAnnotatedString {
                    append("${info.point}")
                    withStyle(
                        style = SpanStyle(
                            color = White
                        )
                    ) {
                        append(" 포인트 적립")
                    }
                },
                style = Typography.titleMedium,
            )

            Spacer(modifier = Modifier.height(5.dp))

            Button(
                onClick = {
                    viewModel.updateCoupon(info.documentId, info.point)
                },
                shape = RoundedCornerShape(10.dp),
                enabled = isEnable,
                colors = ButtonDefaults.buttonColors(
                    containerColor = SubColor,
                    disabledContainerColor = Gray
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (info.use == Code.USED.code) {
                        Code.USED.codeName
                    } else if (isExpiry) {
                        Code.EXPIRY.codeName
                    } else {
                        Code.NOT_USE.codeName
                    },
                    style = Typography.labelMedium,
                )
            }
        }
    }

}
