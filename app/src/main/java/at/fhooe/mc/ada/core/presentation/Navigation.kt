package at.fhooe.mc.ada.core.presentation

import android.content.res.Resources
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import at.fhooe.mc.ada.R

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomBarScreen(
        route = "cards",
        title = "Cards",
        icon = Icons.Default.CreditCard
    )

    object BudgetTracker : BottomBarScreen(
        route = "budgetTracker",
        title = "Budget",
        icon = Icons.Default.Payments
    )

    object CurrencyConverter : BottomBarScreen(
        route = "currencyConverter",
        title = "Currency",
        icon = Icons.Default.CurrencyExchange
    )
}

sealed class Screen(val route: String) {
    object AddEditCardScreen : Screen("add_edit_card_screen")
    object AddEditBudgetRecordScreen : Screen("add_edit_budget_record_screen")
}
