package com.ezen.lolketing.view.main.guide

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ezen.lolketing.R
import com.ezen.lolketing.view.main.TitleBar
import com.ezen.lolketing.view.ui.theme.Black
import com.ezen.lolketing.view.ui.theme.SubColor
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState

@Composable
fun TermsGuideDetailContainer(
    routeAction: GuideRouteAction
) {

    val list = listOf(
        R.drawable.img_terms01,
        R.drawable.img_terms02,
        R.drawable.img_terms03,
        R.drawable.img_terms04,
        R.drawable.img_terms05,
        R.drawable.img_terms06,
        R.drawable.img_terms07,
        R.drawable.img_terms08,
        R.drawable.img_terms09,
        R.drawable.img_terms10,
        R.drawable.img_terms11,
        R.drawable.img_terms12,
        R.drawable.img_terms13,
        R.drawable.img_terms14,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
    ) {

        /** 타이틀 바 **/
        TitleBar(
            onBackClick = { routeAction.popBackStack() },
            title = stringResource(id = R.string.game_terms)
        )

        LazyColumn(modifier = Modifier.fillMaxWidth()) {

            item {
                Image(
                    painter = painterResource(id = R.drawable.lol_guide_term),
                    contentDescription = "titleImage",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                )
            }

            item { TermsViewPager(list) }

        }

    }

}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TermsViewPager(list: List<Int>) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        val pagerState = rememberPagerState()

        HorizontalPagerIndicator(
            pagerState = pagerState,
            activeColor = SubColor,
            modifier = Modifier.padding(vertical = 15.dp)
        )

        HorizontalPager(
            count = list.size,
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 0.dp),
            modifier = Modifier
                .fillMaxWidth(),
        ) { index ->
            Image(
                painter = painterResource(id = list[index]),
                contentDescription = "",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth()
            )
        }

    }
}
