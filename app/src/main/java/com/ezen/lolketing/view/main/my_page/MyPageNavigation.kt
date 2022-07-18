package com.ezen.lolketing.view.main.my_page

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.ezen.lolketing.R
import com.ezen.lolketing.util.toast
import com.ezen.lolketing.view.main.my_page.cache.CouponListContainer
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
        // 내 정보
        composable(
            route = MyPageRouteAction.MyPage,
            enterTransition = { scaleIn(animationSpec = spring(stiffness = Spring.StiffnessMedium)) },
            exitTransition = { scaleOut(animationSpec = spring(stiffness = Spring.StiffnessMedium)) }
        ) {
            MyPageContainer(routeAction = routeAction)
        }
        // 회원 탈퇴
        composable(
            route = MyPageRouteAction.UserWithdrawal,
            enterTransition = { scaleIn(animationSpec = spring(stiffness = Spring.StiffnessMedium)) },
            exitTransition = { scaleOut(animationSpec = spring(stiffness = Spring.StiffnessMedium)) }
        ) {
            WithdrawalContainer(routeAction = routeAction)
        }
        // 구매내역
        composable(
            route = MyPageRouteAction.PurchaseHistory,
            enterTransition = { scaleIn(animationSpec = spring(stiffness = Spring.StiffnessMedium)) },
            exitTransition = { scaleOut(animationSpec = spring(stiffness = Spring.StiffnessMedium)) }
        ) {
            PurchaseHistoryContainer(routeAction = routeAction)
        }
        // 구매내역 상세
        composable(
            route = "${MyPageRouteAction.PurchaseHistoryDetail}/{documentId}/{amount}",
            arguments = listOf(
                navArgument("documentId") { type = NavType.StringType },
                navArgument("amount") { type = NavType.IntType }
            ),
            enterTransition = { scaleIn(animationSpec = spring(stiffness = Spring.StiffnessMedium)) },
            exitTransition = { scaleOut(animationSpec = spring(stiffness = Spring.StiffnessMedium)) }
        ) { entry ->
            val documentId = entry.arguments?.getString("documentId")
            val amount = entry.arguments?.getInt("amount")
            val context = LocalContext.current

            if (documentId == null || amount == null) {
                context.toast(stringResource(id = R.string.error_unexpected))
                return@composable
            }

            PurchaseHistoryDetail(routeAction = routeAction, documentId = documentId, amount = amount)
        }
        // 쿠폰함
        composable(
            route = MyPageRouteAction.CouponList,
            enterTransition = { scaleIn(animationSpec = spring(stiffness = Spring.StiffnessMedium)) },
            exitTransition = { scaleOut(animationSpec = spring(stiffness = Spring.StiffnessMedium)) }
        ) {
            CouponListContainer(routeAction)
        }

    }

}

class MyPageRouteAction(private val navHostController: NavHostController) {
    val navToPurchaseHistory: () -> Unit = {
        navHostController.navigate(PurchaseHistory)
    }

    val navToHistoryDetail: (String, Int) -> Unit = { documentId, amount ->
        navHostController.navigate("$PurchaseHistoryDetail/$documentId/$amount")
    }

    val navToWithdrawal: () -> Unit = {
        navHostController.navigate(UserWithdrawal)
    }

    val navToCoupon: () -> Unit = {
        navHostController.navigate(CouponList)
    }

    fun popBackStack() {
        navHostController.popBackStack()
    }

    companion object {
        const val MyPage = "my_page"
        const val PurchaseHistory = "purchase_history"
        const val PurchaseHistoryDetail = "purchase_history_detail"
        const val UserWithdrawal = "user_withdrawal"
        const val CouponList = "coupon_list"

    }

}