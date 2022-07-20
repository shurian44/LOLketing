package com.ezen.lolketing.view.main.guide

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun GuideNavigationGraph() {

    val navController = rememberAnimatedNavController()
    val routeAction = remember(navController) {
        GuideRouteAction(navController)
    }

    AnimatedNavHost(
        navController = navController,
        startDestination = GuideRouteAction.Guide,
    ) {

        /** 가이드 메인 화면 **/
        composable(
            route = GuideRouteAction.Guide,
            enterTransition = { scaleIn(animationSpec = spring(stiffness = Spring.StiffnessMedium)) },
            exitTransition = { scaleOut(animationSpec = spring(stiffness = Spring.StiffnessMedium)) }
        ) {
            GuideContainer(routeAction)
        }
        /** 게임 용어 화면 **/
        composable(
            route = GuideRouteAction.TermsDetail,
            enterTransition = { scaleIn(animationSpec = spring(stiffness = Spring.StiffnessMedium)) },
            exitTransition = { scaleOut(animationSpec = spring(stiffness = Spring.StiffnessMedium)) }
        ) {
            TermsGuideDetailContainer(routeAction = routeAction)
        }

    }

}

class GuideRouteAction(private val navController: NavController) {

    fun popBackStack() {
        navController.popBackStack()
    }

    val navToDetail: () -> Unit = {
        navController.navigate(TermsDetail)
    }

    companion object {
        const val Guide = "guide"
        const val TermsDetail = "detail"
    }

}