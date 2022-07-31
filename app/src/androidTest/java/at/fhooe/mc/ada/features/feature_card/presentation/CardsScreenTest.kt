package at.fhooe.mc.ada.features.feature_card.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import at.fhooe.mc.ada.core.presentation.BottomBarScreen
import at.fhooe.mc.ada.core.presentation.BottomNavGraph
import at.fhooe.mc.ada.core.util.TestTags
import at.fhooe.mc.ada.di.AppModuleBudgetRecord
import at.fhooe.mc.ada.di.AppModuleCard
import at.fhooe.mc.ada.di.AppModuleCurrencyConversion
import at.fhooe.mc.ada.features.MainActivity
import at.fhooe.mc.ada.ui.theme.AndroidAdvancedProject2022Theme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@UninstallModules(
    AppModuleCard::class, AppModuleBudgetRecord::class, AppModuleCurrencyConversion::class
)
@RunWith(AndroidJUnit4::class)
class CardsScreenTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule: ComposeContentTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun testDefaultFilters() {
        composeRule.onNodeWithTag(TestTags.DATE_ADDED_FILTER_CHIP).assertIsSelected()
        composeRule.onNodeWithTag(TestTags.ASCENDING_FILTER_CHIP).assertIsSelected()

        composeRule.onNodeWithTag(TestTags.CARD_HOLDER_NAME_FILTER_CHIP).assertIsNotSelected()
        composeRule.onNodeWithTag(TestTags.CARD_NAME_FILTER_CHIP).assertIsNotSelected()
        composeRule.onNodeWithTag(TestTags.DESCENDING_FILTER_CHIP).assertIsNotSelected()
    }

}