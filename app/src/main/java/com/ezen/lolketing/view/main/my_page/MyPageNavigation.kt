package com.ezen.lolketing.view.main.my_page

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MyPageNavigationGraph() {
    val navHostController = rememberAnimatedNavController()
    val routeAction = MyPageRouteAction(navHostController)

    AnimatedNavHost(
        navController = navHostController,
        startDestination = MyPageRouteAction.MyPage
    ) {
        composable(
            MyPageRouteAction.MyPage,
            enterTransition = { scaleIn(animationSpec = spring(stiffness = Spring.StiffnessMedium)) },
            exitTransition = { scaleOut(animationSpec = spring(stiffness = Spring.StiffnessMedium)) }
        ) {
            MyPageContainer(routeAction = routeAction)
        }

        composable(
            MyPageRouteAction.UserWithdrawal,
            enterTransition = { scaleIn(animationSpec = spring(stiffness = Spring.StiffnessMedium)) },
            exitTransition = { scaleOut(animationSpec = spring(stiffness = Spring.StiffnessMedium)) }
        ) {
            WithdrawalContainer(routeAction = routeAction)
        }

        composable(
            MyPageRouteAction.PurchaseHistory,
            enterTransition = { scaleIn(animationSpec = spring(stiffness = Spring.StiffnessMedium)) },
            exitTransition = { scaleOut(animationSpec = spring(stiffness = Spring.StiffnessMedium)) }
        ) {
            PurchaseHistoryContainer(routeAction = routeAction)
        }
    }

}

class MyPageRouteAction(private val navHostController: NavHostController) {
    val navToPurchaseHistory: () -> Unit = {
        navHostController.navigate(PurchaseHistory)
    }

    val navToModify: () -> Unit = {

    }

    val navToWithdrawal: () -> Unit = {
        navHostController.navigate(UserWithdrawal)
    }

    val navToCoupon: () -> Unit = {

    }

    fun popBackStack() {
        navHostController.popBackStack()
    }

    companion object {
        const val MyPage = "my_page"
        const val PurchaseHistory = "purchase_history"
        const val UserWithdrawal = "user_withdrawal"
        const val CouponList = "coupon_list"

    }

}