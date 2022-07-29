package at.fhooe.mc.ada.features

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import at.fhooe.mc.ada.features.feature_currencyConversion.domain.MainViewModel
import at.fhooe.mc.ada.core.presentation.BottomBarScreen
import at.fhooe.mc.ada.core.presentation.BottomNavGraph

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(viewModel: MainViewModel) {
    val navController = rememberNavController()
    Scaffold(bottomBar = {
        NavigationBar(
            containerColor = Color.Transparent,
            modifier = Modifier
                .offset(0.dp, (200).dp)
        ) {

        }
    }) {
        BottomNavGraph(navHostController = navController, paddingValues = it, viewModel = viewModel)
    }

}

@Composable
fun BottomBar(navHostController: NavHostController) {
    val screens = listOf(
        BottomBarScreen.BudgetTracker,
        BottomBarScreen.Home,
        BottomBarScreen.CurrencyConverter
    )
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar() {
        screens.forEach {
            AddItem(
                screen = it,
                currentDestination = currentDestination,
                navHostController = navHostController
            )
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    navHostController: NavHostController
) {
    NavigationBarItem(
        label = {
            Text(text = screen.title)
        }, icon = {
            Icon(
                imageVector = screen.icon,
                contentDescription = screen.title,
                modifier = Modifier.scale(1.3f),
                tint = if (isSystemInDarkTheme()) Color.White else Color.Black
            )
        }, selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true, alwaysShowLabel = false,
        onClick = {
            navHostController.navigate(screen.route) {
                popUpTo(navHostController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        })
}