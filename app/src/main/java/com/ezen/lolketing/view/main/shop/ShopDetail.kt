package com.ezen.lolketing.view.main.shop

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ezen.lolketing.R
import com.ezen.lolketing.model.ShopItem
import com.ezen.lolketing.util.findCodeName
import com.ezen.lolketing.util.priceFormat
import com.ezen.lolketing.view.ui.theme.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.flow.asStateFlow

@Composable
fun ShopDetailContainer(
    navHostController: NavHostController,
    documentId: String,
    routeAction: RouteAction,
    viewModel: ShopViewModel = hiltViewModel()
) {

    val scrollState = rememberScrollState()
    val shopItem = viewModel.shopItemState.collectAsState()
    viewModel.getShopItem(documentId)

    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Black)
                .padding(vertical = 56.dp)
                .verticalScroll(scrollState)
        ) {
            shopItem.value?.let {
                ImageViewPager(it.images)
                ShopItemInfo(itemInfo = it, viewModel)
            }
        }
        ShoppingTitleBar { navHostController.popBackStack() }
        ShopPurchaseSelection(
            routeAction = routeAction,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ImageViewPager(list: List<String>) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(310.dp)
    ) {
        val pagerState = rememberPagerState()
        HorizontalPager(
            count = list.size,
            state = pagerState,
            // Add 32.dp horizontal padding to 'center' the pages
            contentPadding = PaddingValues(horizontal = 0.dp),
            modifier = Modifier
                .fillMaxWidth(),
        ) { index ->
            GlideImage(
                imageModel = list[index],
                shimmerParams = ShimmerParams(
                    baseColor = Black,
                    highlightColor = SubColor,
                    durationMillis = 350,
                    dropOff = 0.65f,
                    tilt = 20f
                ),
                failure = {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = "이미지 로드 실패",
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.Center),
                            textAlign = TextAlign.Center
                        )
                    }
                },
            )
        }

        HorizontalPagerIndicator(
            pagerState = pagerState,
            activeColor = SubColor,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
        )
    }
}

@Composable
fun ShopItemInfo(itemInfo: ShopItem, viewModel: ShopViewModel) {
    val count by viewModel.purchaseCount.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(LightBlack)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = "[${findCodeName(itemInfo.group)}]", style = Typography.labelMedium)
        Text(text = itemInfo.name, style = Typography.titleLarge)
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = "금액", style = Typography.labelMedium)
            Spacer(modifier = Modifier.weight(1f))
            Text(text = (itemInfo.price * count).priceFormat(), style = Typography.titleMedium)
        }
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "수량", style = Typography.labelMedium)
            Spacer(modifier = Modifier.weight(1f))

            Image(
                painter = painterResource(id = R.drawable.ic_btn_minus),
                contentDescription = "minus",
                modifier = Modifier.clickable {
                    if (count > 1) viewModel.purchaseCountDecrease()
                }
            )
            Text(
                text = count.toString(),
                style = Typography.titleMedium,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.ic_btn_plus),
                contentDescription = "minus",
                modifier = Modifier.clickable {
                    if (count < 20) viewModel.purchaseCountIncrease()
                }
            )
        }
    }
}

@Composable
fun ShopPurchaseSelection(routeAction: RouteAction, modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth()) {
        Button(
            onClick = { },
            colors = ButtonDefaults.buttonColors(
                containerColor = SubColor
            ),
            shape = RectangleShape,
            modifier = Modifier
                .height(56.dp)
                .weight(1f)
        ) {
            Text(
                text = "장바구니 담기",
                style = Typography.labelLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }

        Button(
            onClick = { routeAction.navToPurchase() },
            colors = ButtonDefaults.buttonColors(
                containerColor = MainColor
            ),
            shape = RectangleShape,
            modifier = Modifier
                .height(56.dp)
                .weight(1f)
        ) {
            Text(
                text = "바로 구매",
                style = Typography.labelLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }
}