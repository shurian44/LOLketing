package com.ezen.lolketing.view.main.shop

import android.net.Uri
import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.ezen.lolketing.R
import com.ezen.lolketing.util.toast
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.gson.Gson

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ShopNavigationGraph() {
    val navHostController = rememberAnimatedNavController()
    val routeAction = remember(navHostController) {
        RouteAction(navHostController)
    }

    AnimatedNavHost(
        navController = navHostController,
        startDestination = RouteAction.Shop,

        ) {
        composable(
            route = RouteAction.Shop,
            enterTransition = { scaleIn(animationSpec = spring(stiffness = Spring.StiffnessMedium)) },
            exitTransition = { scaleOut(animationSpec = spring(stiffness = Spring.StiffnessMedium)) }
        ) {
            ShoppingContainer(routeAction)
        }

        composable(
            route = "${RouteAction.Detail}/{documentId}",
            arguments = listOf(
                navArgument("documentId") { type = NavType.StringType }
            ),
            enterTransition = { scaleIn(animationSpec = spring(stiffness = Spring.StiffnessMedium)) },
            exitTransition = { scaleOut(animationSpec = spring(stiffness = Spring.StiffnessMedium)) }
        ) { entry ->
            val documentId = entry.arguments?.getString("documentId")
            val context = LocalContext.current

            if (documentId == null) {
                context.toast(stringResource(id = R.string.error_unexpected))
                return@composable
            }

            ShopDetailContainer(
                navHostController = navHostController,
                documentId = documentId,
                routeAction = routeAction
            )
        }

        composable(
            route = RouteAction.Purchase,
            enterTransition = { scaleIn(animationSpec = spring(stiffness = Spring.StiffnessMedium)) },
            exitTransition = { scaleOut(animationSpec = spring(stiffness = Spring.StiffnessMedium)) }
        ) {
            PurchaseContainer(navHostController = navHostController, routeAction = routeAction)
        }

        composable(
            route = "${RouteAction.Purchase}?indexList={indexList}",
            arguments = listOf(
                navArgument("indexList") { type = NavType.StringType },
            ),
            enterTransition = { scaleIn(animationSpec = spring(stiffness = Spring.StiffnessMedium)) },
            exitTransition = { scaleOut(animationSpec = spring(stiffness = Spring.StiffnessMedium)) }
        ) { entry ->
            val result = entry.arguments?.getString("indexList")
            val after = Uri.decode(Gson().toJson(result))
            val indexList = try {
                after.substring(2, after.length - 2).split(",").map { it.toLong() }
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }

            PurchaseContainer(
                navHostController = navHostController,
                routeAction = routeAction,
                indexList = indexList
            )
        }

        composable(
            route = RouteAction.Basket,
            enterTransition = { scaleIn(animationSpec = spring(stiffness = Spring.StiffnessMedium)) },
            exitTransition = { scaleOut(animationSpec = spring(stiffness = Spring.StiffnessMedium)) }
        ) {
            ShoppingBasketContainer(
                navHostController = navHostController,
                routeAction = routeAction
            )
        }
    }
}


class RouteAction(navHostController: NavHostController) {
    val navToDetail: (String) -> Unit = {
        navHostController.navigate("$Detail/$it")
    }

    val navToRightAwayPurchase: (LongArray) -> Unit = {
        val url = Uri.encode(Gson().toJson(it))
        navHostController.navigate("$Purchase?indexList=$url")
    }

    val navToShopBasket: () -> Unit = {
        navHostController.navigate(Basket)
    }

    companion object {
        const val Shop = "shop"
        const val Detail = "detail"
        const val Purchase = "purchase"
        const val Basket = "basket"
    }

}