package com.ezen.lolketing.view.main.shop

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ezen.lolketing.R
import com.ezen.lolketing.view.ui.theme.Black
import com.ezen.lolketing.view.ui.theme.LightBlack
import com.ezen.lolketing.view.ui.theme.SubColor
import com.ezen.lolketing.view.ui.theme.Typography
import com.google.accompanist.flowlayout.FlowColumn
import com.google.accompanist.flowlayout.FlowRow

@Composable
fun PurchaseContainer(navHostController: NavHostController) {
    val scrollState = rememberScrollState()

    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Black)
                .padding(vertical = 56.dp)
                .verticalScroll(scrollState)
        ) {
            SelectShopItems()
            Test()
        }
        ShoppingTitleBar { navHostController.popBackStack() }
    }

}

@Composable
fun SelectShopItems() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "선택 상품",
            style = Typography.labelMedium,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        Spacer(modifier = Modifier.size(10.dp))
        FlowRow(modifier = Modifier.fillMaxWidth()) {
            repeat(3) {
                BasketItem(index = it)
            }
        }
    }
}

@Composable
fun BasketItem(index: Int) {
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
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(SubColor)
                    .size(80.dp)
            )
            Spacer(modifier = Modifier.size(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "[카테고리]", style = Typography.labelMedium)
                Text(text = "상품 이름", style = Typography.labelMedium)
                Row {
                    Text(text = "수량", style = Typography.labelMedium)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = "1개", style = Typography.labelMedium)
                }
                Row {
                    Text(text = "가격", style = Typography.labelMedium)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = "30,000원", style = Typography.labelMedium)
                }
            }
        }
    }
}

@Composable
fun ShippingInfo() {
    OutlinedTextField(
        value = "",
        onValueChange = {},
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_user),
                contentDescription = "user"
            )
        }
    )
}

@Composable
fun Test() {
    TextField(
        value = "",
        onValueChange = {},
        modifier = Modifier.fillMaxWidth()
    )
}