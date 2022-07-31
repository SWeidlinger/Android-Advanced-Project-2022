package at.fhooe.mc.ada.features

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import at.fhooe.mc.ada.core.presentation.BottomBarScreen
import at.fhooe.mc.ada.core.presentation.BottomNavGraph
import at.fhooe.mc.ada.features.feature_currency_converter.domain.MainViewModel
import at.fhooe.mc.ada.ui.theme.AndroidAdvancedProject2022Theme
import at.fhooe.mc.ada.ui.theme.TextBlack
import at.fhooe.mc.ada.ui.theme.TextWhite
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidAdvancedProject2022Theme {
                val navController = rememberNavController()
                Scaffold(bottomBar = {
                    NavigationBar(
                        containerColor = Color.Transparent,
                        modifier = Modifier
                            .offset(0.dp, (200).dp)
                    ) {}
                }) {
                    BottomNavGraph(
                        navHostController = navController,
                        paddingValues = it
                    )
                }
            }
        }
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

    NavigationBar(contentColor = MaterialTheme.colorScheme.secondary) {
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
        },
        icon = {
            Icon(
                imageVector = screen.icon,
                contentDescription = screen.title,
                modifier = Modifier.scale(1.3f),
                tint = if (isSystemInDarkTheme()) TextWhite else TextBlack
            )
        },
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,
        alwaysShowLabel = false,
        onClick = {
            navHostController.navigate(screen.route) {
                popUpTo(navHostController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        },
        colors = NavigationBarItemDefaults.colors(indicatorColor = MaterialTheme.colorScheme.surfaceTint)
    )
}