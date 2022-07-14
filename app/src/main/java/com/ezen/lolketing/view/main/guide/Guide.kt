package com.ezen.lolketing.view.main.guide

import android.app.Activity
import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ezen.lolketing.R
import com.ezen.lolketing.util.createIntent
import com.ezen.lolketing.util.startActivity
import com.ezen.lolketing.view.main.TitleBar
import com.ezen.lolketing.view.ui.theme.Black
import com.ezen.lolketing.view.ui.theme.Typography
import com.ezen.lolketing.view.ui.theme.White

@Composable
fun GuideContainer(routeAction: GuideRouteAction) {

    val activity = LocalContext.current as? Activity

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
    ) {

        Column() {

            /** 타이틀 이미지 **/
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_guide),
                    contentDescription = "title",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .background(Color(0x4D000000))
                )
            }

            /** item 1행 **/
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(top = 20.dp, start = 20.dp, end = 20.dp)
            ) {
                GuideItem(
                    imgRes = R.drawable.lol_guide_aos,
                    title = stringResource(id = R.string.aos),
                    modifier = Modifier.weight(1f)
                ) {
                    activity?.let {
                        it.startActivity(
                            it.createIntent(LoLGuideDetailActivity::class.java).also { intent ->
                                intent.putExtra(
                                    LoLGuideDetailActivity.STATUS,
                                    LoLGuideDetailActivity.AOS
                                )
                            })
                    }
                }
                Spacer(modifier = Modifier.width(20.dp))
                GuideItem(
                    imgRes = R.drawable.lol_guide_rule,
                    title = stringResource(id = R.string.rule),
                    modifier = Modifier.weight(1f)
                ) {
                    activity?.let {
                        it.startActivity(
                            it.createIntent(LoLGuideDetailActivity::class.java).also { intent ->
                                intent.putExtra(
                                    LoLGuideDetailActivity.STATUS,
                                    LoLGuideDetailActivity.RULE
                                )
                            })
                    }
                }
            }

            /** item 2행 **/
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(top = 20.dp, start = 20.dp, end = 20.dp)
            ) {
                GuideItem(
                    imgRes = R.drawable.lol_guide_position,
                    title = stringResource(id = R.string.position),
                    modifier = Modifier.weight(1f)
                ) {
                    activity?.let {
                        it.startActivity(
                            it.createIntent(LoLGuideDetailActivity::class.java).also { intent ->
                                intent.putExtra(
                                    LoLGuideDetailActivity.STATUS,
                                    LoLGuideDetailActivity.POSITION
                                )
                            })
                    }
                }
                Spacer(modifier = Modifier.width(20.dp))
                GuideItem(
                    imgRes = R.drawable.lol_guide_nature,
                    title = stringResource(id = R.string.nature),
                    modifier = Modifier.weight(1f)
                ) {
                    activity?.let {
                        it.startActivity(
                            it.createIntent(LoLGuideDetailActivity::class.java).also { intent ->
                                intent.putExtra(
                                    LoLGuideDetailActivity.STATUS,
                                    LoLGuideDetailActivity.NATURE
                                )
                            })
                    }
                }
            }

            /** item 3행 **/
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(top = 20.dp, start = 20.dp, end = 20.dp)
            ) {
                GuideItem(
                    imgRes = R.drawable.lol_guide_score,
                    title = stringResource(id = R.string.score),
                    modifier = Modifier.weight(1f)
                ) {
                    activity?.let {
                        it.startActivity(
                            it.createIntent(LoLGuideDetailActivity::class.java).also { intent ->
                                intent.putExtra(
                                    LoLGuideDetailActivity.STATUS,
                                    LoLGuideDetailActivity.SCORE
                                )
                            })
                    }
                }
                Spacer(modifier = Modifier.width(20.dp))
                GuideItem(
                    imgRes = R.drawable.lol_guide_term,
                    title = stringResource(id = R.string.game_terms),
                    modifier = Modifier.weight(1f)
                ) {
                    routeAction.navToDetail()
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

        }

        /** 타이틀 영역 **/
        TitleBar(
            onBackClick = { activity?.finish() },
            title = "롤알못 가이드",
            titleColor = White
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuideItem(
    @DrawableRes imgRes: Int,
    title: String,
    modifier: Modifier,
    onClick: () -> Unit
) {

    OutlinedCard(
        shape = RoundedCornerShape(15.dp),
        border = BorderStroke(1.dp, White),
        modifier = modifier.clickable {
            onClick()
        }
    ) {
        Box {
            Image(
                painter = painterResource(id = imgRes),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x80000000))
            )
            Text(
                text = title,
                style = Typography.labelLarge,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }

}