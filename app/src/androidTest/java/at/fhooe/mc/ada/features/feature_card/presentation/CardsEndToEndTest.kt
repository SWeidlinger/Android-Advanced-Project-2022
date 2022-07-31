package at.fhooe.mc.ada.features.feature_card.presentation

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import at.fhooe.mc.ada.core.util.TestTags
import at.fhooe.mc.ada.di.AppModuleBudgetRecord
import at.fhooe.mc.ada.di.AppModuleCard
import at.fhooe.mc.ada.di.AppModuleCurrencyConversion
import at.fhooe.mc.ada.features.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(
    AppModuleCard::class, AppModuleBudgetRecord::class, AppModuleCurrencyConversion::class
)
class CardsEndToEndTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule: ComposeContentTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun saveNewCard() {
        composeRule.onNodeWithTag(TestTags.FAB_CARD_SCREEN_ADD).performClick()

        composeRule.onNodeWithTag(TestTags.TEXTFIELD_CARD_ADD_TITLE).performTextInput("test card")
        composeRule.onNodeWithTag(TestTags.BUTTON_ADD_NEW_CARD).performClick()

        composeRule.onNodeWithText("test card").assertIsDisplayed()
    }
}