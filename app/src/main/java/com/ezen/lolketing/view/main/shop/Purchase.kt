@file:OptIn(ExperimentalMaterial3Api::class)

package com.ezen.lolketing.view.main.shop

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ezen.lolketing.R
import com.ezen.lolketing.database.entity.ShopEntity
import com.ezen.lolketing.util.findCodeName
import com.ezen.lolketing.util.priceFormat
import com.ezen.lolketing.util.startActivity
import com.ezen.lolketing.util.toast
import com.ezen.lolketing.view.main.BasicContentsDialog
import com.ezen.lolketing.view.main.BasicTitleDialog
import com.ezen.lolketing.view.main.my_page.cache.CacheChargingActivity
import com.ezen.lolketing.view.ui.theme.*
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun PurchaseContainer(
    navHostController: NavHostController,
    routeAction: RouteAction,
    indexList: List<Long>? = null,
    viewModel: PurchaseViewModel = hiltViewModel()
) {
    val purchaseState = viewModel.purchaseState.collectAsState()
    val userInfoState = viewModel.userInfoState.collectAsState()
    var nicknameState by remember { mutableStateOf(TextFieldValue("")) }
    var phoneState by remember { mutableStateOf(TextFieldValue("")) }
    var addressState by remember { mutableStateOf(TextFieldValue("")) }
    var messageState by remember { mutableStateOf(TextFieldValue("")) }
    var totalPrice by remember { mutableStateOf(0L) }
    val context = LocalContext.current

    viewModel.getUserInfo()
    userInfoState.value?.let {
        nicknameState = TextFieldValue(it.nickname)
        phoneState = TextFieldValue(it.phone)
        addressState = TextFieldValue(it.address)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
    ) {
        /** 타이틀바 **/
        ShoppingTitleBar(
            routeAction = routeAction,
        ) { navHostController.popBackStack() }
        LazyColumn(
            modifier = Modifier.padding(top = 56.dp, bottom = 169.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            /** 선택 상품 **/
            item {
                Text(
                    text = "선택 상품",
                    style = Typography.labelMedium,
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                )
            }
            item { Spacer(modifier = Modifier.size(10.dp)) }
            when (val value = purchaseState.value) {
                is PurchaseViewModel.Event.Init -> {
                    viewModel.selectShoppingBasket(indexList?.toList() ?: listOf())
                }
                is PurchaseViewModel.Event.PurchaseLoading -> {
                    if (value.isLoading) {
                        item { CircularProgressIndicator() }
                    }
                }
                is PurchaseViewModel.Event.PurchaseItems -> {
                    if (value.list.isEmpty()) {
                        item {
                            Text(
                                text = "선택한 상품이 없습니다.",
                                style = Typography.labelMedium,
                                textAlign = TextAlign.Center,
                                color = Gray,
                                modifier = Modifier
                                    .padding(horizontal = 20.dp)
                                    .fillMaxWidth()
                            )
                        }
                    }

                    totalPrice = value.list.sumOf { it.price }

                    itemsIndexed(value.list) { index, item ->
                        BasketItem(index = index, item = item)
                    }
                }
            }

            /** 배송지 정보 **/
            item { Spacer(modifier = Modifier.height(28.dp)) }
            item {
                Text(
                    text = "배송지 정보",
                    style = Typography.labelMedium,
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                )
            }
            item { Spacer(modifier = Modifier.height(10.dp)) }
            // 닉네임
            item {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    value = nicknameState,
                    onValueChange = {
                        nicknameState = it
                        userInfoState.value?.nickname = it.text
                    },
                    textStyle = Typography.labelMedium,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    placeholder = { Text(text = "닉네임") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_user),
                            contentDescription = null,
                            tint = White
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = SubColor,
                        unfocusedBorderColor = White,
                        placeholderColor = Gray,
                        disabledPlaceholderColor = Gray
                    )
                )
            }

            item { Spacer(modifier = Modifier.height(12.dp)) }
            // 전화번호
            item {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    value = phoneState,
                    onValueChange = {
                        phoneState = it
                        userInfoState.value?.phone = it.text
                    },
                    textStyle = Typography.labelMedium,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Next
                    ),
                    placeholder = { Text(text = "전화번호") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_phone),
                            contentDescription = null,
                            tint = White
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = SubColor,
                        unfocusedBorderColor = White,
                        placeholderColor = Gray,
                        disabledPlaceholderColor = Gray
                    )
                )
            }

            item { Spacer(modifier = Modifier.height(12.dp)) }
            // 주소
            item {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    value = addressState,
                    onValueChange = {
                        addressState = it
                        userInfoState.value?.address = it.text
                    },
                    textStyle = Typography.labelMedium,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    placeholder = { Text(text = "주소") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_address),
                            contentDescription = null,
                            tint = White
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = SubColor,
                        unfocusedBorderColor = White,
                        placeholderColor = Gray,
                        disabledPlaceholderColor = Gray
                    )
                )
            }

            item { Spacer(modifier = Modifier.height(12.dp)) }
            // 배송 메시지
            item {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    value = messageState,
                    onValueChange = {
                        messageState = it
                    },
                    textStyle = Typography.labelMedium,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    placeholder = { Text(text = "배송 메시지") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_address),
                            contentDescription = null,
                            tint = White
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = SubColor,
                        unfocusedBorderColor = White,
                        placeholderColor = Gray,
                        disabledPlaceholderColor = Gray
                    )
                )
            }

            item { Spacer(modifier = Modifier.height(30.dp)) }
        }
        /** 결제 정보 **/
        PurchaseInfo(
            totalPrice = totalPrice,
            userCache = userInfoState.value?.cache ?: 0,
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        var isEnabled by remember { mutableStateOf(true) }

        /** 결제하기 버튼 **/
        Button(
            onClick = {
                if (totalPrice > (userInfoState.value?.cache ?: 0)) {
                    viewModel.isOutOfCacheDialogState.value = true
                } else {
                    viewModel.isPurchaseDialogState.value = true
                }
            },
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = MainColor
            ),
            enabled = isEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .align(Alignment.BottomCenter)
        ) {
            Text(text = "결제하기", style = Typography.labelLarge)
        }

        /** 잔액 부족 다이얼로그 **/
        BasicTitleDialog(
            title = "잔액 부족",
            contents = context.getString(R.string.out_of_cache),
            confirmText = context.getString(R.string.ok),
            isShow = viewModel.isOutOfCacheDialogState
        ) {
            context.startActivity(CacheChargingActivity::class.java)
        }

        /** 결제하기 다이얼로그 **/
        BasicContentsDialog(
            isShow = viewModel.isPurchaseDialogState,
            contents = "결제를 진행하시겠습니까?",
            confirmText = context.getString(R.string.ok),
            cancelText = context.getString(R.string.cancel),
            onConfirmClick = {
                val list = (purchaseState.value as? PurchaseViewModel.Event.PurchaseItems)?.list
                val userInfo = userInfoState.value
                if (list == null || userInfo == null) {
                    context.toast(R.string.error_unexpected)
                    viewModel.isPurchaseDialogState.value = false
                    return@BasicContentsDialog
                }
                isEnabled = false

                viewModel.setPurchaseItems(
                    list,
                    userInfo,
                    messageState.text,
                    successListener = {
                        isEnabled = true
                        context.toast("구매가 완료되었습니다.")
                        navHostController.popBackStack()
                    },
                    failureListener = {
                        isEnabled = true
                    }
                )
            }
        )

    }

}

@Composable
fun BasketItem(index: Int, item: ShopEntity) {
    val color = if (index % 2 == 0) LightBlack else Black
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color)
            .padding(vertical = 9.dp, horizontal = 20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
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
    }
}

@Composable
fun PurchaseInfo(
    totalPrice: Long,
    userCache: Long,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 56.dp)
            .height(113.dp)
            .clip(RoundedCornerShape(topStart = 13.dp, topEnd = 13.dp))
            .background(White)
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row {
            Text(text = "결제 정보", style = Typography.labelMedium, color = Black)
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "환불 안내 >", style = Typography.displaySmall, color = Gray)
        }
        Row {
            Text(text = "전체 상품 가격", style = Typography.labelMedium, color = Black)
            Spacer(modifier = Modifier.weight(1f))
            Text(text = totalPrice.priceFormat(), style = Typography.titleMedium)
        }
        Row {
            Text(text = "My 캐시", style = Typography.labelMedium, color = Black)
            Spacer(modifier = Modifier.weight(1f))
            Text(text = userCache.priceFormat(), style = Typography.titleMedium)
        }
    }
}
