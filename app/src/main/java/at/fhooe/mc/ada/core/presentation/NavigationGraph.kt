package at.fhooe.mc.ada.core.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import at.fhooe.mc.ada.features.feature_currencyConversion.domain.MainViewModel
import at.fhooe.mc.ada.features.feature_currencyConversion.presentation.CurrencyConverterScreen
import at.fhooe.mc.ada.features.feature_card.presentation.HomeScreen
import at.fhooe.mc.ada.features.feature_card.presentation.add_edit_card.AddEditCardScreen
import at.fhooe.mc.ada.features.feature_budget_tracker.presentation.BudgetTrackerScreen
import at.fhooe.mc.ada.features.feature_budget_tracker.presentation.add_edit_budget_record.AddEditBudgetRecordScreen

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
        composable(route = BottomBarScreen.BudgetTracker.route) {
            BudgetTrackerScreen(BottomBarScreen.BudgetTracker.title, navController, paddingValues)
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
        composable(
            route = Screen.AddEditCardScreen.route + "?cardId={cardId}",
            arguments = listOf(navArgument(name = "cardId") {
                type = NavType.IntType
                defaultValue = -1
            })
        ) {
            AddEditCardScreen(navHostController = navController)
        }

        composable(
            route = Screen.AddEditBudgetRecordScreen.route + "?budgetRecordId={budgetRecordId}",
            arguments = listOf(navArgument(name = "budgetRecordId") {
                type = NavType.IntType
                defaultValue = -1
            })
        ) {
            AddEditBudgetRecordScreen(navHostController = navController)
        }
    }
}