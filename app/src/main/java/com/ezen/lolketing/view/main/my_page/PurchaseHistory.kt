package com.ezen.lolketing.view.main.my_page

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ezen.lolketing.database.entity.ShopEntity
import com.ezen.lolketing.util.findCodeName
import com.ezen.lolketing.util.priceFormat
import com.ezen.lolketing.view.main.TitleBar
import com.ezen.lolketing.view.ui.theme.Black
import com.ezen.lolketing.view.ui.theme.LightBlack
import com.ezen.lolketing.view.ui.theme.SubColor
import com.ezen.lolketing.view.ui.theme.Typography
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun PurchaseHistoryContainer(
    routeAction: MyPageRouteAction
) {
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
            item { HistoryDateItem(date = "2022.01.01") }
            item {
                HistoryPurchaseItem(
                    item = ShopEntity(
                        id = 1,
                        count = 2,
                        group = "",
                        name = "test",
                        price = 10000,
                        image = "https://lwi.nexon.com/elsword/home/2020/info/rena/view/line_4/character3_1.png"
                    )
                )
            }
            item {
                HistoryPurchaseItem(
                    item = ShopEntity(
                        id = 1,
                        count = 2,
                        group = "",
                        name = "test",
                        price = 10000,
                        image = "https://lwi.nexon.com/elsword/home/2020/info/rena/view/line_4/character3_1.png"
                    )
                )
            }
            item { HistoryDateItem(date = "2022.01.02") }
            item {
                HistoryPurchaseItem(
                    item = ShopEntity(
                        id = 1,
                        count = 2,
                        group = "",
                        name = "test",
                        price = 10000,
                        image = "https://lwi.nexon.com/elsword/home/2020/info/rena/view/line_4/character3_1.png"
                    )
                )
            }
            item {
                HistoryPurchaseItem(
                    item = ShopEntity(
                        id = 1,
                        count = 2,
                        group = "",
                        name = "test",
                        price = 10000,
                        image = "https://lwi.nexon.com/elsword/home/2020/info/rena/view/line_4/character3_1.png"
                    )
                )
            }
            item { HistoryDateItem(date = "2022.01.03") }
            item {
                HistoryPurchaseItem(
                    item = ShopEntity(
                        id = 1,
                        count = 2,
                        group = "",
                        name = "test",
                        price = 10000,
                        image = "https://lwi.nexon.com/elsword/home/2020/info/rena/view/line_4/character3_1.png"
                    )
                )
            }
            item {
                HistoryPurchaseItem(
                    item = ShopEntity(
                        id = 1,
                        count = 2,
                        group = "",
                        name = "test",
                        price = 10000,
                        image = "https://lwi.nexon.com/elsword/home/2020/info/rena/view/line_4/character3_1.png"
                    )
                )
            }
            item { HistoryDateItem(date = "2022.01.04") }
            item {
                HistoryPurchaseItem(
                    item = ShopEntity(
                        id = 1,
                        count = 2,
                        group = "",
                        name = "test",
                        price = 10000,
                        image = "https://lwi.nexon.com/elsword/home/2020/info/rena/view/line_4/character3_1.png"
                    )
                )
            }
            item {
                HistoryPurchaseItem(
                    item = ShopEntity(
                        id = 1,
                        count = 2,
                        group = "",
                        name = "test",
                        price = 10000,
                        image = "https://lwi.nexon.com/elsword/home/2020/info/rena/view/line_4/character3_1.png"
                    )
                )
            }
            
            item { Spacer(modifier = Modifier.height(30.dp)) }
        }

        TitleBar(
            onBackClick = {
                routeAction.popBackStack()
            },
            title = "구매 내역"
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryDateItem(date: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(7.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {

        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(SubColor)
        )

        OutlinedCard(
            shape = RoundedCornerShape(15.dp),
            border = BorderStroke(1.dp, SubColor),
            modifier = Modifier
                .padding(horizontal = 5.dp)
        ) {
            Text(
                text = date,
                style = Typography.titleMedium,
                modifier = Modifier.padding(vertical = 2.dp, horizontal = 10.dp)
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(SubColor)
        )

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryPurchaseItem(item: ShopEntity) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Black)
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

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(LightBlack)
        )
    }
}