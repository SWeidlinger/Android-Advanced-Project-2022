package at.fhooe.mc.ada.ui.bottomNavigation.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import at.fhooe.mc.ada.ui.BottomBar

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ReceiptScreen(title: String, navHostController: NavHostController, paddingValues: PaddingValues) {
    Scaffold(
        topBar = {
            SmallTopAppBar(title = { Text(text = title) })
        }, bottomBar = {
            BottomBar(navHostController = navHostController)
        }, content = {
            Box(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
                    .background(Color.Blue)
            )
        }
    )
}