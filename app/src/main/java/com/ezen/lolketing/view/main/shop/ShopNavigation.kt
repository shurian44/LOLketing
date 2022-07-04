package com.ezen.lolketing.view.main.shop

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

            ShopDetailContainer(navHostController = navHostController, documentId)
        }
    }
}


class RouteAction(navHostController: NavHostController) {
    val navToDetail: (String) -> Unit = {
        navHostController.navigate("$Detail/$it")
    }

    companion object {
        const val Shop = "shop"
        const val Detail = "detail"
    }

}