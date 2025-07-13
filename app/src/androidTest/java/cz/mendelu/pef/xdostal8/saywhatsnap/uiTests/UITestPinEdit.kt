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
import cz.mendelu.pef.xdostal8.saywhatsnap.mock.TranslationsMock
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.activities.MainActivity
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.elements.TestContent
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.elements.TestErrorMsgTag
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.NavGraphs
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.destinations.PinEditScreenDestination
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.destinations.TranslationListScreenDestination
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.pin_edit_screen.TestCategoryTextField
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.pin_edit_screen.TestDescriptionTextField
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.pin_edit_screen.TestNameTextField
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
class UITestPinEdit {

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

    private fun launchEditPinDirectly() {
        composeRule.activity.setContent {
            MaterialTheme {
                SayWhatSnapTheme {
                    navController = rememberNavController()
                    DestinationsNavHost(
                        navController = navController,
                        navGraph = NavGraphs.root
                    )
                    LaunchedEffect(Unit) {
                        navController.navigate(PinEditScreenDestination(TranslationsMock.todayTransId))
                    }
                }
            }
        }
    }

    @Test
    fun testEditPin() {
        launchEditPinDirectly()
        with(composeRule) {
            waitForIdle()
            // are displayed
            onNodeWithTag(TestNameTextField).assertIsDisplayed()
            onNodeWithTag(TestDescriptionTextField).assertIsDisplayed()
            onNodeWithTag(TestCategoryTextField).assertIsDisplayed()

            // with proper values got from mocked repositories
            onNodeWithTag(TestNameTextField).assert(hasText(TranslationsMock.todayTranslation.name))
            onNodeWithTag(TestDescriptionTextField).assert(hasText(TranslationsMock.todayTranslation.description))
            onNodeWithTag(TestCategoryTextField).assert(hasText(TranslationsMock.todayTranslation.category))

            // clear values
            onNodeWithTag(TestNameTextField).performTextClearance()
            onNodeWithTag(TestDescriptionTextField).performTextClearance()
            onNodeWithTag(TestCategoryTextField).performTextClearance()
            waitForIdle()
            Thread.sleep(1000)

            // edit values
            onNodeWithTag(TestNameTextField).performTextInput(TranslationsMock.yesterdayTranslation.name)
            waitForIdle()
            onNodeWithTag(TestDescriptionTextField).performTextInput(TranslationsMock.yesterdayTranslation.description)
            waitForIdle()
            onNodeWithTag(TestCategoryTextField).performTextInput(TranslationsMock.yesterdayTranslation.category)
            waitForIdle()
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


    @Test
    fun testNotSaveable() {
        launchEditPinDirectly()
        with(composeRule) {
            // are displayed
            onNodeWithTag(TestNameTextField).assertIsDisplayed()
            onNodeWithTag(TestDescriptionTextField).assertIsDisplayed()
            onNodeWithTag(TestCategoryTextField).assertIsDisplayed()

            // with proper values got from mocked repositories
            onNodeWithTag(TestNameTextField).assert(hasText(TranslationsMock.todayTranslation.name))
            onNodeWithTag(TestDescriptionTextField).assert(hasText(TranslationsMock.todayTranslation.description))
            onNodeWithTag(TestCategoryTextField).assert(hasText(TranslationsMock.todayTranslation.category))

            // clear values
            onNodeWithTag(TestNameTextField).performTextClearance()
            onNodeWithTag(TestDescriptionTextField).performTextClearance()
            onNodeWithTag(TestCategoryTextField).performTextClearance()
            waitForIdle()
            Thread.sleep(1000)


            Espresso.closeSoftKeyboard()
            onNodeWithTag(TestContent).performScrollToNode(hasTestTag(TestSaveButton))



            // will not save and errors will display
            onNodeWithTag(TestSaveButton).performClick()
            waitForIdle()
            Thread.sleep(1000)


            // error messages displayed
            onNodeWithTag(TestDescriptionTextField + TestErrorMsgTag).assert(hasText(errorMsg))
            onNodeWithTag(TestCategoryTextField + TestErrorMsgTag).assert(hasText(errorMsg))
            onNodeWithTag(TestNameTextField + TestErrorMsgTag).assert(hasText(errorMsg))


            // navigation does not occur
            val currentRoute = navController.currentBackStackEntry?.destination?.route
            Assert.assertEquals(PinEditScreenDestination.route, currentRoute)

        }
    }


}