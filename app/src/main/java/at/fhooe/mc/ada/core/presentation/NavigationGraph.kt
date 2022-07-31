package at.fhooe.mc.ada.core.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import at.fhooe.mc.ada.features.feature_budget_tracker.presentation.BudgetTrackerScreen
import at.fhooe.mc.ada.features.feature_budget_tracker.presentation.add_edit_budget_record.AddEditBudgetRecordScreen
import at.fhooe.mc.ada.features.feature_card.presentation.CardsScreen
import at.fhooe.mc.ada.features.feature_card.presentation.add_edit_card.AddEditCardScreen
import at.fhooe.mc.ada.features.feature_currency_converter.domain.MainViewModel
import at.fhooe.mc.ada.features.feature_currency_converter.presentation.CurrencyConverterScreen

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomNavGraph(
    navHostController: NavHostController,
    paddingValues: PaddingValues
) {
    NavHost(
        navController = navHostController,
        startDestination = BottomBarScreen.Home.route
    ) {
        composable(route = BottomBarScreen.BudgetTracker.route) {
            BudgetTrackerScreen(
                BottomBarScreen.BudgetTracker.title,
                navHostController,
                paddingValues
            )
        }
        composable(route = BottomBarScreen.Home.route) {
            CardsScreen(BottomBarScreen.Home.title, navHostController)
        }
        composable(route = BottomBarScreen.CurrencyConverter.route) {
            CurrencyConverterScreen(
                BottomBarScreen.CurrencyConverter.title,
                navHostController,
                paddingValues
            )
        }

        composable(
            route = Screen.AddEditCardScreen.route + "?cardId={cardId}",
            arguments = listOf(navArgument(name = "cardId") {
                type = NavType.IntType
                defaultValue = -1
            })
        ) {
            AddEditCardScreen(navHostController = navHostController)
        }

        composable(
            route = Screen.AddEditBudgetRecordScreen.route +
                    "?budgetRecordId={budgetRecordId}&isBudgetRecordExpense={isBudgetRecordExpense}",
            arguments = listOf(navArgument(name = "budgetRecordId") {
                type = NavType.IntType
                defaultValue = -1
            },
                navArgument(name = "isBudgetRecordExpense") {
                    type = NavType.BoolType
                    defaultValue = false
                })
        ) {
            AddEditBudgetRecordScreen(navHostController = navHostController)
        }
    }
}