package at.fhooe.mc.ada.util

import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import at.fhooe.mc.ada.currencyConversion.MainViewModel
import at.fhooe.mc.ada.ui.bottomNavigation.BottomBarScreen
import at.fhooe.mc.ada.ui.bottomNavigation.screens.CurrencyConverterScreen
import at.fhooe.mc.ada.ui.bottomNavigation.screens.HomeScreen
import at.fhooe.mc.ada.ui.bottomNavigation.screens.ReceiptScreen

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomNavGraph(navController: NavHostController, paddingValues: PaddingValues, viewModel: MainViewModel) {
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Home.route
    ) {
        composable(route = BottomBarScreen.Receipts.route) {
            ReceiptScreen(BottomBarScreen.Receipts.title, navController, paddingValues)
        }
        composable(route = BottomBarScreen.Home.route) {
            HomeScreen(BottomBarScreen.Home.title, navController, paddingValues)
        }
        composable(route = BottomBarScreen.CurrencyConverter.route) {
            CurrencyConverterScreen(
                BottomBarScreen.CurrencyConverter.title,
                navController,
                paddingValues,
                viewModel
            )
        }
    }
}