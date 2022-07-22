package at.fhooe.mc.ada.core.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import at.fhooe.mc.ada.features.feature_currencyConversion.domain.MainViewModel
import at.fhooe.mc.ada.ui.bottomNavigation.screens.CurrencyConverterScreen
import at.fhooe.mc.ada.features.feature_card.presentation.HomeScreen
import at.fhooe.mc.ada.features.feature_card.presentation.add_edit_card.AddEditCardScreen
import at.fhooe.mc.ada.ui.bottomNavigation.screens.ReceiptScreen

@Composable
fun BottomNavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
    viewModel: MainViewModel
) {
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Home.route
    ) {
        composable(route = BottomBarScreen.Receipts.route) {
            ReceiptScreen(BottomBarScreen.Receipts.title, navController, paddingValues)
        }
        composable(route = BottomBarScreen.Home.route) {
            HomeScreen(navController, paddingValues)
        }
        composable(route = BottomBarScreen.CurrencyConverter.route) {
            CurrencyConverterScreen(
                BottomBarScreen.CurrencyConverter.title,
                navController,
                paddingValues,
                viewModel
            )
        }
        composable(
            route = Screen.AddEditCardScreen.route + "?cardId={cardId}",
            arguments = listOf(navArgument(name = "cardId") {
                type = NavType.IntType
                defaultValue = -1
            })
        ) {
            AddEditCardScreen(navHostController = navController)
        }
    }
}