package com.ezen.lolketing.view.main.shop

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ezen.lolketing.model.ShopItem
import com.ezen.lolketing.util.findCodeName
import com.ezen.lolketing.view.ui.theme.Black
import com.ezen.lolketing.view.ui.theme.SubColor
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.flow.asStateFlow

@Composable
fun ShopDetailContainer(navHostController: NavHostController, documentId: String, viewModel: ShopViewModel = hiltViewModel()) {

    val scrollState = rememberScrollState()
    val shopItem = viewModel.shopItemState.asStateFlow()
    viewModel.getShopItem(documentId)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
            .verticalScroll(scrollState)
    ) {
        ShoppingTitleBar { navHostController.popBackStack() }
        shopItem.value?.let {
            ImageViewPager(it.images)
            ShopItemInfo(itemInfo = it)
        }
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
fun ShopItemInfo(itemInfo: ShopItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Text(text = "[${findCodeName(itemInfo.group)}]",)
    }
}
