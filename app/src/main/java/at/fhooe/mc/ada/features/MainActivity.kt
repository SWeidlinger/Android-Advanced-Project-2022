package at.fhooe.mc.ada.features

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import at.fhooe.mc.ada.features.feature_currencyConversion.domain.MainViewModel
import at.fhooe.mc.ada.ui.MainScreen
import at.fhooe.mc.ada.ui.theme.AndroidAdvancedProject2022Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidAdvancedProject2022Theme {
                MainScreen(viewModel)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AndroidAdvancedProject2022Theme {
    }
}