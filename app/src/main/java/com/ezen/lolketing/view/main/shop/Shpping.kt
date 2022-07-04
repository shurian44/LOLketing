package com.ezen.lolketing.view.main.shop

import android.app.Activity
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ezen.lolketing.R
import com.ezen.lolketing.model.ShopListItem
import com.ezen.lolketing.util.findCodeName
import com.ezen.lolketing.util.getShoppingCategoryList
import com.ezen.lolketing.util.priceFormat
import com.ezen.lolketing.view.main.CustomScrollableTabRow
import com.ezen.lolketing.view.main.TitleBar
import com.ezen.lolketing.view.ui.theme.Black
import com.ezen.lolketing.view.ui.theme.SubColor
import com.ezen.lolketing.view.ui.theme.White
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun ShoppingContainer(routeAction: RouteAction) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
    ) {
        val activity = (LocalContext.current as? Activity)
        val tabIndex = remember { mutableStateOf(0) }

        ShoppingTitleBar { activity?.finish() }
        TabBar(tabIndex)
        ShopListItem(tabIndex, routeAction)
    }
}

@Composable
fun ShoppingTitleBar(onBackListener : () -> Unit) {
    TitleBar("굿즈 쇼핑", onBackClick = {
        onBackListener()
    }) {
        Box(modifier = Modifier.align(Alignment.CenterEnd)) {
            Image(
                painter = painterResource(id = R.drawable.ic_cart),
                contentDescription = "cart"
            )
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(0.dp, 1.dp)
                    .size(15.dp)
                    .clip(CircleShape)
                    .background(SubColor)
            )
            Text(
                text = "1",
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(0.dp, 0.dp),
                color = White
            )
        }
    }
}

@Composable
fun TabBar(tabIndex: MutableState<Int>, modifier: Modifier = Modifier) {
    val tabTitles = getShoppingCategoryList()

    Column(modifier = modifier) {
        CustomScrollableTabRow(
            tabs = tabTitles,
            selectedTabIndex = tabIndex.value,
        ) { index ->
            tabIndex.value = index
        }
    }
}

@Composable
fun ShopListItem(tabIndex: MutableState<Int>, routeAction: RouteAction, viewModel: ShopViewModel = hiltViewModel()) {
    val list by viewModel.shopListState.collectAsState()
    viewModel.getShopList(tabIndex.value)

    if (list.isEmpty()) {
        Text(
            text = "상품 준비중입니다.",
            color = White,
            fontSize = 16.sp,
            modifier = Modifier.fillMaxWidth()
        )
        return
    }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 150.dp),
        modifier = Modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(20.dp, 14.dp, 20.dp, 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        items(list) {
            ShopItem(it) {
                routeAction.navToDetail(it.documentId)
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopItem(shopListItem: ShopListItem, onClickListener : () -> Unit) {
    Card(
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(3.dp, SubColor),
        colors = CardDefaults.cardColors(
            containerColor = White
        ),
        onClick = onClickListener,
        modifier = Modifier
            .width(153.dp)
            .height(196.dp)
    ) {
        val modifier = Modifier
            .fillMaxWidth()
            .height(153.dp)
            .clip(RoundedCornerShape(10.dp, 10.dp, 0.dp, 0.dp))
            .border(3.dp, SubColor)

        GlideImage(
            imageModel = shopListItem.image,
            shimmerParams = ShimmerParams(
                baseColor = Black,
                highlightColor = SubColor,
                durationMillis = 350,
                dropOff = 0.65f,
                tilt = 20f
            ),
            failure = {
                Column(modifier = modifier, verticalArrangement = Arrangement.Center) {
                    Text(
                        text = "이미지 로드 실패",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            },
            modifier = modifier
        )

        val textStyle = TextStyle(
            fontSize = 12.sp,
            color = Black,
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp, 3.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "[${findCodeName(shopListItem.group)}]",
                    style = textStyle
                )
                Text(
                    text = shopListItem.price.priceFormat(),
                    textAlign = TextAlign.End,
                    style = textStyle,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }
            Text(
                text = shopListItem.name,
                style = textStyle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
fun ShoppingTopImage() {
    Image(
        painter = painterResource(id = R.drawable.img_shopping),
        contentDescription = "shopping"
    )
}