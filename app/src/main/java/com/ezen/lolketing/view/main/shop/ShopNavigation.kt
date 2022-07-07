package com.ezen.lolketing.view.main.shop

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ezen.lolketing.R
import com.ezen.lolketing.util.toast
import com.google.gson.Gson

@Composable
fun ShopNavigationGraph() {
    val navHostController = rememberNavController()
    val routeAction = remember(navHostController) {
        RouteAction(navHostController)
    }

    NavHost(navController = navHostController, startDestination = RouteAction.Shop) {
        composable(RouteAction.Shop) {
            ShoppingContainer(routeAction)
        }

        composable(
            route = "${RouteAction.Detail}/{documentId}",
            arguments = listOf(
                navArgument("documentId") { type = NavType.StringType }
            )
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

        composable(RouteAction.Purchase) {
            PurchaseContainer(navHostController = navHostController, routeAction = routeAction)
        }

        composable(
            route = "${RouteAction.Purchase}?indexList={indexList}",
            arguments = listOf(
                navArgument("indexList") { type = NavType.StringType },
            )
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

        composable(RouteAction.Basket) {
            ShoppingBasketContainer(navHostController = navHostController, routeAction = routeAction)
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