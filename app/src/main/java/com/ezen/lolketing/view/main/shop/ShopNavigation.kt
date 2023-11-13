package com.ezen.lolketing.view.main.shop

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.ezen.lolketing.database.entity.ShopEntity
import com.google.gson.Gson

@Composable
fun ShopNavigationGraph() {
//    val navHostController = rememberAnimatedNavController()
//    val routeAction = remember(navHostController) {
//        RouteAction(navHostController)
//    }
//
//    AnimatedNavHost(
//        navController = navHostController,
//        startDestination = RouteAction.Shop,
//        ) {
//        /** 쇼핑 메인화면 **/
//        composable(
//            route = RouteAction.Shop,
//            enterTransition = { scaleIn(animationSpec = spring(stiffness = Spring.StiffnessMedium)) },
//            exitTransition = { scaleOut(animationSpec = spring(stiffness = Spring.StiffnessMedium)) }
//        ) {
//            ShoppingContainer(routeAction)
//        }
//        /** 쇼핑 아이템 상세화면 **/
//        composable(
//            route = "${RouteAction.Detail}/{documentId}",
//            arguments = listOf(
//                navArgument("documentId") { type = NavType.StringType }
//            ),
//            enterTransition = { scaleIn(animationSpec = spring(stiffness = Spring.StiffnessMedium)) },
//            exitTransition = { scaleOut(animationSpec = spring(stiffness = Spring.StiffnessMedium)) }
//        ) { entry ->
//            val documentId = entry.arguments?.getString("documentId")
//            val context = LocalContext.current
//
//            if (documentId == null) {
//                context.toast(stringResource(id = R.string.error_unexpected))
//                return@composable
//            }
//
//            ShopDetailContainer(
//                navHostController = navHostController,
//                documentId = documentId,
//                routeAction = routeAction
//            )
//        }
//        /** 구매하기 화면 (바로 구매) **/
//        composable(
//            route = "${RouteAction.Purchase}/{item}",
//            arguments = listOf(
//                navArgument("item") { type = ShopEntity.NavigationType }
//            ),
//            enterTransition = { scaleIn(animationSpec = spring(stiffness = Spring.StiffnessMedium)) },
//            exitTransition = { scaleOut(animationSpec = spring(stiffness = Spring.StiffnessMedium)) }
//        ) { entry ->
//            val item = entry.arguments?.getParcelable<ShopEntity>("item")
//            PurchaseContainer(navHostController = navHostController, routeAction = routeAction, item = item)
//        }
//        /** 구매하기 화면 (장바구니 선택 후) **/
//        composable(
//            route = "${RouteAction.Purchase}?indexList={indexList}",
//            arguments = listOf(
//                navArgument("indexList") { type = NavType.StringType },
//            ),
//            enterTransition = { scaleIn(animationSpec = spring(stiffness = Spring.StiffnessMedium)) },
//            exitTransition = { scaleOut(animationSpec = spring(stiffness = Spring.StiffnessMedium)) }
//        ) { entry ->
//            val result = entry.arguments?.getString("indexList")
//            val after = Uri.decode(Gson().toJson(result))
//            val indexList = try {
//                after.substring(2, after.length - 2).split(",").map { it.toLong() }
//            } catch (e: Exception) {
//                e.printStackTrace()
//                emptyList()
//            }
//
//            PurchaseContainer(
//                navHostController = navHostController,
//                routeAction = routeAction,
//                indexList = indexList
//            )
//        }
//        /** 장바구니 화면 **/
//        composable(
//            route = RouteAction.Basket,
//            enterTransition = { scaleIn(animationSpec = spring(stiffness = Spring.StiffnessMedium)) },
//            exitTransition = { scaleOut(animationSpec = spring(stiffness = Spring.StiffnessMedium)) }
//        ) {
//            ShoppingBasketContainer(
//                navHostController = navHostController,
//                routeAction = routeAction
//            )
//        }
//    }
}


class RouteAction(navHostController: NavHostController) {
    val navToDetail: (String) -> Unit = {
        navHostController.navigate("$Detail/$it")
    }

    val navToPurchase: (LongArray) -> Unit = {
        val url = Uri.encode(Gson().toJson(it))
        navHostController.navigate("$Purchase?indexList=$url")
    }

    val navToRightAwayPurchase: (ShopEntity) -> Unit = {
        val item = Uri.encode(Gson().toJson(it))
        navHostController.navigate("$Purchase/$item")
    }

    val navToShopBasket: () -> Unit = {
        navHostController.navigate(Basket)
    }

    val popBackStack: () -> Unit = {
        navHostController.popBackStack()
    }

    companion object {
        const val Shop = "shop"
        const val Detail = "detail"
        const val Purchase = "purchase"
        const val Basket = "basket"
    }

}