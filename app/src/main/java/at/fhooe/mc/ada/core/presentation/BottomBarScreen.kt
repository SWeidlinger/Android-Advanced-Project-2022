package at.fhooe.mc.ada.core.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomBarScreen(
        route = "home",
        title = "Cards",
        icon = Icons.Default.Home
    )

    object Receipts : BottomBarScreen(
        route = "receipts",
        title = "Receipts",
        icon = Icons.Default.List
    )

    object CurrencyConverter : BottomBarScreen(
        route = "currencyConverter",
        title = "Currency",
        icon = Icons.Default.Info
    )
}

sealed class Screen(val route: String) {
    object AddEditCardScreen : Screen("add_edit_card_screen")
}
