@file:OptIn(ExperimentalMaterial3Api::class)

package com.ezen.lolketing.view.main.shop

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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ezen.lolketing.R
import com.ezen.lolketing.database.entity.ShopEntity
import com.ezen.lolketing.util.findCodeName
import com.ezen.lolketing.util.priceFormat
import com.ezen.lolketing.util.toast
import com.ezen.lolketing.view.main.BasicContentsDialog
import com.ezen.lolketing.view.main.TitleBar
import com.ezen.lolketing.view.ui.theme.*
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun ShoppingBasketContainer(
    navHostController: NavHostController,
    routeAction: RouteAction,
    viewModel: PurchaseViewModel = hiltViewModel()
) {

    val purchaseState = viewModel.purchaseState.collectAsState()
    val userInfoState = viewModel.userInfoState.collectAsState()
    val purchaseItems = purchaseState.value as? PurchaseViewModel.Event.PurchaseItems
    val context = LocalContext.current

    viewModel.getUserInfo()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
    ) {
        LazyColumn(modifier = Modifier.padding(top = 56.dp, bottom = 169.dp)) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp, start = 20.dp)
                ) {
                    Text(text = "선택 상품", style = Typography.labelMedium)
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            when (val value = purchaseState.value) {
                is PurchaseViewModel.Event.Init -> {
                    viewModel.selectAllShoppingBasket()
                }
                is PurchaseViewModel.Event.PurchaseItems -> {
                    value.list.forEachIndexed { index, shopEntity ->
                        item {
                            BasketItem(
                                index = index,
                                item = shopEntity,
                                isVisible = true,
                                viewModel = viewModel
                            )
                        }
                    }
                    if (value.list.isEmpty()) {
                        item {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .fillParentMaxSize()
                                    .align(Alignment.Center)
                            ) {
                                Text(
                                    text = "장바구니에 등록된 상품이 없습니다.",
                                    style = Typography.labelMedium,
                                    color = Gray,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }

                        }
                    }
                    item { Spacer(modifier = Modifier.height(30.dp)) }
                }
                is PurchaseViewModel.Event.PurchaseLoading -> {
                    if (value.isLoading) {
                        item { CircularProgressIndicator() }
                    }
                }
            }
        }
        /** 타이틀 바 **/
        TitleBar(
            title = "장바구니",
            onBackClick = { navHostController.popBackStack() }
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_trash),
                contentDescription = "trash",
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clickable {
                        if(purchaseItems?.list.isNullOrEmpty()) {
                            context.toast("등록된 상품이 없습니다.")
                        } else {
                            viewModel.isDeleteDialogState.value = true
                        }
                    }
            )
        }

        val totalPrice = purchaseItems?.list?.filter { it.isChecked }?.sumOf { it.price }
        /** 결제 정보 **/
        PurchaseInfo(
            totalPrice = totalPrice ?: 0,
            userCache = userInfoState.value?.cache ?: 0,
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        /** 결제하기 버튼 **/
        Button(
            onClick = {
                val idList = purchaseItems?.list?.filter { it.isChecked }?.map { it.id }
                if (idList.isNullOrEmpty()) {
                    context.toast("선택한 상품이 없습니다.")
                    return@Button
                }
                routeAction.navToRightAwayPurchase(idList.toLongArray())
            },
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = MainColor
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .align(Alignment.BottomCenter)
        ) {
            Text(text = "선택 상품 구매하기", style = Typography.labelLarge)
        }

        /** 삭제 다이얼로그 **/
        BasicContentsDialog(
            isShow = viewModel.isDeleteDialogState,
            contents = "선택한 상품들을 삭제하시겠습니까?",
            confirmText = "예",
            cancelText = "아니오",
            onConfirmClick = {
                purchaseItems?.list
                    ?.filter { it.isChecked }
                    ?.map { it.id }
                    ?.let {
                        viewModel.deleteBasketItems(idList = it)
                    }
            }
        )
    }
}

@Composable
fun BasketItem(
    index: Int,
    item: ShopEntity,
    isVisible: Boolean = false,
    viewModel: PurchaseViewModel? = null
) {
    val color = if (index % 2 == 0) LightBlack else Black
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 9.dp, horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(2.dp, SubColor),
                modifier = Modifier.size(80.dp)
            ) {
                GlideImage(
                    imageModel = item.image
                )
            }
            Spacer(modifier = Modifier.size(16.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(text = "[${findCodeName(item.group)}]", style = Typography.labelMedium)
                Text(
                    text = item.name,
                    style = Typography.labelMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row {
                    Text(text = "수량", style = Typography.labelMedium)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = "${item.count}개", style = Typography.labelMedium)
                }
                Row {
                    Text(text = "가격", style = Typography.labelMedium)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = item.price.priceFormat(), style = Typography.labelMedium)
                }
            }
        }

        if (isVisible) {
            Checkbox(
                checked = item.isChecked,
                onCheckedChange = {
                    viewModel?.updateBasketChecked(item.id, item.isChecked.not())
                },
                colors = CheckboxDefaults.colors(
                    uncheckedColor = White,
                    checkedColor = SubColor
                ),
                modifier = Modifier
                    .padding(end = 5.dp)
                    .align(Alignment.TopEnd)
            )
        }

    }
}