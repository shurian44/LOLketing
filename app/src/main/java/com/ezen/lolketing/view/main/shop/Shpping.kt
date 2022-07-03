package com.ezen.lolketing.view.main.shop

import android.app.Activity
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ezen.lolketing.R
import com.ezen.lolketing.view.main.TitleBar
import com.ezen.lolketing.view.ui.theme.Black
import com.ezen.lolketing.view.ui.theme.SubColor
import com.ezen.lolketing.view.ui.theme.White
import kotlin.random.Random

@Composable
fun ShoppingContainer() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
    ) {
        val activity = (LocalContext.current as? Activity)
        ShoppingTitleBar(activity = activity)
        Column(modifier = Modifier.padding(0.dp, 56.dp, 0.dp, 0.dp)) {
            TabBar()
            GridTest()
            Text(text = "test", style = TextStyle(color = White))
        }
    }
}

@Composable
fun ShoppingTitleBar(activity: Activity?) {
    TitleBar("쇼핑", onBackClick = {
        activity?.finish()
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
fun TabBar(modifier: Modifier = Modifier) {
    var tabIndex by remember { mutableStateOf(0) }
    val tabTitles = listOf("First", "Second", "Thread", "First", "Second", "Thread", "First", "Second", "Thread")

    Column(modifier = modifier) {
        CustomScrollableTabRow(
            tabs = tabTitles,
            selectedTabIndex = tabIndex,
        ) { index ->
            tabIndex = index
        }
    }
}

@Composable
fun GridTest() {
    val list = (1..10).map { "${it}번째 아이템" }
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 150.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(list) {
            GridTestItem(it)
        }
    }
}

@Composable
fun GridTestItem(item: String) {
    val red = Random.nextInt(256)
    val green = Random.nextInt(256)
    val blue = Random.nextInt(256)
    val color = Color(red, green, blue)

    Box(modifier = Modifier
        .size(150.dp)
        .background(color)
    ) {
        Text(text = item, color = White)
    }
}

// 출처 : https://medium.com/@sukhdip_sandhu/jetpack-compose-scrollabletabrow-indicator-matches-width-of-text-e79c0e5826fe
@Composable
fun CustomScrollableTabRow(
    tabs: List<String>,
    selectedTabIndex: Int,
    onTabClick: (Int) -> Unit
) {
    val density = LocalDensity.current
    val tabWidths = remember {
        val tabWidthStateList = mutableStateListOf<Dp>()
        repeat(tabs.size) {
            tabWidthStateList.add(0.dp)
        }
        tabWidthStateList
    }

    ScrollableTabRow(
        selectedTabIndex = selectedTabIndex,
        containerColor = Color.Transparent,
        contentColor = Color.White,
        edgePadding = 0.dp,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier.customTabIndicatorOffset(
                    currentTabPosition = tabPositions[selectedTabIndex],
                    tabWidth = tabWidths[selectedTabIndex]
                ),
                color = SubColor
            )
        }
    ) {
        tabs.forEachIndexed { tabIndex, tab ->
            Tab(
                selected = selectedTabIndex == tabIndex,
                onClick = { onTabClick(tabIndex) },
                selectedContentColor = SubColor,
                unselectedContentColor = White,
                text = {
                    Text(
                        text = tab,
                        onTextLayout = { textLayoutResult ->
                            tabWidths[tabIndex] =
                                with(density) { textLayoutResult.size.width.toDp() }
                        }
                    )
                }
            )
        }
    }
}

fun Modifier.customTabIndicatorOffset(
    currentTabPosition: TabPosition,
    tabWidth: Dp
): Modifier = composed(
    inspectorInfo = debugInspectorInfo {
        name = "customTabIndicatorOffset"
        value = currentTabPosition
    }
) {
    val currentTabWidth by animateDpAsState(
        targetValue = tabWidth,
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing)
    )
    val indicatorOffset by animateDpAsState(
        targetValue = ((currentTabPosition.left + currentTabPosition.right - tabWidth) / 2),
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing)
    )
    fillMaxWidth()
        .wrapContentSize(Alignment.BottomStart)
        .offset(x = indicatorOffset)
        .width(currentTabWidth)
}

@Composable
fun ShoppingTopImage() {
    Image(
        painter = painterResource(id = R.drawable.img_shopping),
        contentDescription = "shopping"
    )
}