package cz.mendelu.pef.xdostal8.saywhatsnap.uiTests

import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.test.espresso.Espresso
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.navigate
import cz.mendelu.pef.xdostal8.saywhatsnap.R
import cz.mendelu.pef.xdostal8.saywhatsnap.mock.ServerMock
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.activities.MainActivity
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.elements.TestContent
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.elements.TestErrorMsgTag
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.elements.TestTextFieldWithinAutocompleteTag
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.NavGraphs
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.destinations.SettingsScreenDestination
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.destinations.TranslationListScreenDestination
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.pin_edit_screen.TestCategoryTextField
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.pin_edit_screen.TestDescriptionTextField
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.pin_edit_screen.TestNameTextField
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.settings_screen.TestAppLangAutocomplete
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.settings_screen.TestTransLangAutocomplete
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.translate_screen.TestOrigLangAutocomplete
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.translate_screen.TestOrigStringTextField
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.translation_edit_screen.TestSaveButton
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.theme.SayWhatSnapTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
@HiltAndroidTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class UITestSettings {

    private lateinit var navController: NavHostController

    private var errorMsg: String = "Cannot be empty"


    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    private fun launchSettingsDirectly() {
        composeRule.activity.setContent {
            MaterialTheme {
                SayWhatSnapTheme {
                    // Setup the NavHost with the Destinations library
                    navController = rememberNavController()
                    DestinationsNavHost(
                        navController = navController,
                        navGraph = NavGraphs.root
                    )
                    // Navigate to the TranslationEditScreen with the desired translationID
                    LaunchedEffect(Unit) {
                        navController.navigate(SettingsScreenDestination())
                    }
                }
            }
        }
    }

    @Test
    fun testSaveableWithGibberish() {
        // save able when no valid app language selected... - but does not change the params
        // translation language is optional
        launchSettingsDirectly()
        with(composeRule) {
            waitForIdle()

            // assert, that the app lang and translation lang are displayed
            onNodeWithTag(TestAppLangAutocomplete + TestTextFieldWithinAutocompleteTag)
                .assertIsDisplayed()
            onNodeWithTag(TestTransLangAutocomplete + TestTextFieldWithinAutocompleteTag)
                .assertIsDisplayed()

            // assert, that the settings data are loaded from API and Datastore
            onNodeWithTag(TestAppLangAutocomplete + TestTextFieldWithinAutocompleteTag)
                .assert(hasText(ServerMock.initAppLanguage))
            onNodeWithTag(TestTransLangAutocomplete + TestTextFieldWithinAutocompleteTag)
                .assert(hasText(ServerMock.initTransLanguage))

            // clear app lang and change it to gibberish
            onNodeWithTag(TestAppLangAutocomplete + TestTextFieldWithinAutocompleteTag)
                .performTextClearance()
            onNodeWithTag(TestTransLangAutocomplete + TestTextFieldWithinAutocompleteTag)
                .performTextInput("12345")

            Espresso.closeSoftKeyboard()
            waitForIdle()
            Thread.sleep(1000)

            // try saving
            Espresso.closeSoftKeyboard()
            onNodeWithTag(TestSaveButton).performClick()

            waitForIdle()
            Thread.sleep(1000)

            // navigation does occur, but the settings do not change because they were not valid
            val currentRoute = navController.currentBackStackEntry?.destination?.route
            Assert.assertEquals(TranslationListScreenDestination.route, currentRoute)
        }
    }

    @Test
    fun testSaveable() {
        // save able when language selected...
        // translation language is optional
        launchSettingsDirectly()
        with(composeRule) {
            waitForIdle()


            // assert, that the app lang and translation lang are displayed
            onNodeWithTag(TestAppLangAutocomplete + TestTextFieldWithinAutocompleteTag)
                .assertIsDisplayed()
            onNodeWithTag(TestTransLangAutocomplete + TestTextFieldWithinAutocompleteTag)
                .assertIsDisplayed()

            // with proper values got from mocked repositories
            onNodeWithTag(TestAppLangAutocomplete + TestTextFieldWithinAutocompleteTag)
                .assert(hasText(ServerMock.initAppLanguage))
            onNodeWithTag(TestTransLangAutocomplete + TestTextFieldWithinAutocompleteTag)
                .assert(hasText(ServerMock.initTransLanguage))

            // clear values
            onNodeWithTag(TestAppLangAutocomplete + TestTextFieldWithinAutocompleteTag)
                .performTextClearance()
            onNodeWithTag(TestTransLangAutocomplete + TestTextFieldWithinAutocompleteTag)
                .performTextClearance()
            waitForIdle()
            Thread.sleep(1000)

            // edit values
            onNodeWithTag(TestTransLangAutocomplete + TestTextFieldWithinAutocompleteTag)
                .performTextInput(ServerMock.changedTransLanguage)
            onNodeWithTag(TestAppLangAutocomplete + TestTextFieldWithinAutocompleteTag)
                .performTextInput(ServerMock.changedAppLanguage)
            Thread.sleep(1000)

            Espresso.closeSoftKeyboard()
            onNodeWithTag(TestContent).performScrollToNode(hasTestTag(TestSaveButton))

            // save without errors and check validity by mocked module - module would Error()
            // if not updated correctly
            onNodeWithTag(TestSaveButton).performClick()
            waitForIdle()
            Thread.sleep(1000)

            // navigation occurs - occurs only when pin successfully saved, test ends
            val currentRoute = navController.currentBackStackEntry?.destination?.route
            Assert.assertEquals(TranslationListScreenDestination.route, currentRoute)
        }
    }
}