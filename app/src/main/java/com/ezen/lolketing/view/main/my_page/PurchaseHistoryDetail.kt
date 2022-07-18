package com.ezen.lolketing.view.main.my_page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ezen.lolketing.R
import com.ezen.lolketing.model.ShopItem
import com.ezen.lolketing.util.findCodeName
import com.ezen.lolketing.util.priceFormat
import com.ezen.lolketing.view.main.TitleBar
import com.ezen.lolketing.view.main.shop.ImageViewPager
import com.ezen.lolketing.view.main.shop.ShopViewModel
import com.ezen.lolketing.view.ui.theme.Black
import com.ezen.lolketing.view.ui.theme.LightBlack
import com.ezen.lolketing.view.ui.theme.Typography

@Composable
fun PurchaseHistoryDetail(
    routeAction: MyPageRouteAction,
    documentId: String,
    amount: Int,
    viewModel: ShopViewModel = hiltViewModel()
) {

    val shopItemState = viewModel.shopItemState.collectAsState()
    viewModel.getShopItem(documentId)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
    ) {

        Column(modifier = Modifier.padding(vertical = 56.dp)) {
            shopItemState.value?.let {
                ImageViewPager(it.images)
                ShopItemHistoryInfo(itemInfo = it, amount)
            }
        }

        /** 타이틀 바 **/
        TitleBar(
            onBackClick = { routeAction.popBackStack() },
            title = stringResource(id = R.string.purchase_history)
        )

    }
}

/** 구매 내역 UI **/
@Composable
fun ShopItemHistoryInfo(itemInfo: ShopItem, amount: Int) {
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
            Text(text = stringResource(id = R.string.price), style = Typography.labelMedium)
            Spacer(modifier = Modifier.weight(1f))
            Text(text = (itemInfo.price * amount).priceFormat(), style = Typography.titleMedium)
        }
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(text = stringResource(id = R.string.quantity), style = Typography.labelMedium)
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = amount.toString(),
                style = Typography.titleMedium,
            )
        }
    }
}