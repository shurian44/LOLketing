package com.ezen.lolketing.view.main

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.ezen.lolketing.R
import com.ezen.lolketing.view.ui.theme.*

@Composable
fun TitleBar(
    title: String = "",
    onBackClick: () -> Unit,
    titleColor: Color= SubColor,
    content: @Composable BoxScope.() -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp, 0.dp)
            .height(56.dp),
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_back),
            contentDescription = "back",
            modifier = Modifier
                .align(Alignment.CenterStart)
                .clickable {
                    onBackClick()
                }
        )
        Text(
            text = title,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = titleColor,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
        )
        content()
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
fun BasicTitleDialog(
    title: String,
    contents: String,
    confirmText: String,
    isShow: MutableState<Boolean>,
    onConfirmClick: (() -> Unit)? = null
) {

    if (isShow.value) {
        Dialog(onDismissRequest = { isShow.value = true }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Black)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = title,
                        style = Typography.labelLarge,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                    )
                    Text(
                        text = contents,
                        style = Typography.labelMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    Button(
                        onClick = {
                            onConfirmClick?.invoke()
                            isShow.value = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MainColor
                        ),
                        shape = RoundedCornerShape(3.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(text = confirmText, style = Typography.labelLarge)
                    }
                }
                Image(
                    painter = painterResource(id = R.drawable.ic_cancel),
                    contentDescription = "cancel",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                        .clickable {
                            isShow.value = false
                        }
                )
            }
        }

    }

}

@Composable
fun BasicContentsDialog(
    isShow: MutableState<Boolean>,
    contents: String,
    confirmText: String,
    onConfirmClick: (() -> Unit)? = null
) {

    if (isShow.value) {
        Dialog(onDismissRequest = { isShow.value = true }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(
                        minHeight = 200.dp
                    )
                    .clip(RoundedCornerShape(10.dp))
                    .background(Black)
            ) {

                Text(
                    text = contents,
                    style = Typography.labelMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 72.dp)
                        .align(Alignment.Center)
                )
                Button(
                    onClick = {
                        onConfirmClick?.invoke()
                        isShow.value = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MainColor
                    ),
                    shape = RoundedCornerShape(3.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .align(Alignment.BottomCenter)
                ) {
                    Text(text = confirmText, style = Typography.labelLarge)
                }

                Image(
                    painter = painterResource(id = R.drawable.ic_cancel),
                    contentDescription = "cancel",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                        .clickable {
                            isShow.value = false
                        }
                )
            }
        }
    }
}

@Composable
fun BasicContentsDialog(
    isShow: MutableState<Boolean>,
    contents: String,
    confirmText: String,
    cancelText: String,
    onConfirmClick: (() -> Unit)? = null,
    onCancelClick: (() -> Unit)? = null
) {

    if (isShow.value) {
        Dialog(onDismissRequest = { isShow.value = true }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(
                        minHeight = 200.dp
                    )
                    .clip(RoundedCornerShape(10.dp))
                    .background(Black)
            ) {

                Text(
                    text = contents,
                    style = Typography.labelMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 72.dp)
                        .align(Alignment.Center)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .align(Alignment.BottomCenter)
                ) {
                    OutlinedButton(
                        onClick = {
                            onCancelClick?.invoke()
                            isShow.value = false
                        },
                        shape = RoundedCornerShape(3.dp),
                        border = BorderStroke(1.dp, MainColor),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = cancelText, style = Typography.labelLarge)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = {
                            onConfirmClick?.invoke()
                            isShow.value = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MainColor
                        ),
                        shape = RoundedCornerShape(3.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = confirmText, style = Typography.labelLarge)
                    }
                }

                Image(
                    painter = painterResource(id = R.drawable.ic_cancel),
                    contentDescription = "cancel",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                        .clickable {
                            isShow.value = false
                        }
                )
            }
        }

    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LOLketingTheme {
        Column(modifier = Modifier.fillMaxSize()) {
        }
    }
}