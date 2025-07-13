package cz.mendelu.pef.xdostal8.saywhatsnap.uiTests

import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.test.espresso.Espresso
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.navigate
import cz.mendelu.pef.xdostal8.saywhatsnap.mock.TranslationsMock
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.activities.MainActivity
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.elements.TestContent
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.NavGraphs
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.destinations.TranslationEditScreenDestination
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.destinations.TranslationListScreenDestination
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.translation_edit_screen.TestOrigLangTextField
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.translation_edit_screen.TestOrigStringTextField
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.translation_edit_screen.TestSaveButton
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.translation_edit_screen.TestTransLangTextField
import cz.mendelu.pef.xdostal8.saywhatsnap.ui.screens.translation_edit_screen.TestTransStringTextField
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
class UITestTranslationEdit {

    private lateinit var navController: NavHostController


    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    private fun launchEditTranslationDirectly() {
        composeRule.activity.setContent {
            MaterialTheme {
                SayWhatSnapTheme {
                    navController = rememberNavController()
                    DestinationsNavHost(
                        navController = navController,
                        navGraph = NavGraphs.root
                    )
                    LaunchedEffect(Unit) {
                        navController.navigate(TranslationEditScreenDestination(TranslationsMock.todayTransId))
                    }
                }
            }
        }
    }


    @Test
    fun testEditTrans() {
        launchEditTranslationDirectly()
        with(composeRule) {
            waitForIdle()
            // are displayed
            onNodeWithTag(TestOrigStringTextField).assertIsDisplayed()
            onNodeWithTag(TestOrigLangTextField).assertIsDisplayed()
            onNodeWithTag(TestTransStringTextField).assertIsDisplayed()
            onNodeWithTag(TestTransLangTextField).assertIsDisplayed()

            // with proper values got from mocked repositories
            onNodeWithTag(TestOrigStringTextField).assert(hasText(TranslationsMock.todayTranslation.originalString))
            onNodeWithTag(TestOrigLangTextField).assert(hasText(TranslationsMock.todayTranslation.originalLanguage))
            onNodeWithTag(TestTransStringTextField).assert(hasText(TranslationsMock.todayTranslation.translatedString))
            onNodeWithTag(TestTransLangTextField).assert(hasText(TranslationsMock.todayTranslation.translatedLanguage))

            // clear values
            onNodeWithTag(TestOrigStringTextField).performTextClearance()
            onNodeWithTag(TestOrigLangTextField).performTextClearance()
            onNodeWithTag(TestTransStringTextField).performTextClearance()
            onNodeWithTag(TestTransLangTextField).performTextClearance()
            waitForIdle()
            Thread.sleep(1000)

            // edit values
            onNodeWithTag(TestOrigStringTextField).performTextInput(TranslationsMock.yesterdayTranslation.originalString)
            waitForIdle()
            onNodeWithTag(TestOrigLangTextField).performTextInput(TranslationsMock.yesterdayTranslation.originalLanguage)
            waitForIdle()
            onNodeWithTag(TestTransStringTextField).performTextInput(TranslationsMock.yesterdayTranslation.translatedString)
            waitForIdle()
            onNodeWithTag(TestTransLangTextField).performTextInput(TranslationsMock.yesterdayTranslation.translatedLanguage)
            waitForIdle()
            Thread.sleep(1000)

            Espresso.closeSoftKeyboard()
            onNodeWithTag(TestContent).performScrollToNode(hasTestTag(TestSaveButton))

            // save without errors and check validity by mocked module - module would Error()
            // if not updated correctly
            onNodeWithTag(TestSaveButton).performClick()
            waitForIdle()
            Thread.sleep(1000)

            // navigation occurs - occurs only when trans successfully saved, test ends
            val currentRoute = navController.currentBackStackEntry?.destination?.route
            Assert.assertEquals(TranslationListScreenDestination.route, currentRoute)
        }
    }

    @Test
    fun testNotSaveable() {
        launchEditTranslationDirectly()
        with(composeRule) {
            // are displayed
            onNodeWithTag(TestOrigStringTextField).assertIsDisplayed()
            onNodeWithTag(TestOrigLangTextField).assertIsDisplayed()
            onNodeWithTag(TestTransStringTextField).assertIsDisplayed()
            onNodeWithTag(TestTransLangTextField).assertIsDisplayed()

            // with proper values got from mocked repositories
            onNodeWithTag(TestOrigStringTextField).assert(hasText(TranslationsMock.todayTranslation.originalString))
            onNodeWithTag(TestOrigLangTextField).assert(hasText(TranslationsMock.todayTranslation.originalLanguage))
            onNodeWithTag(TestTransStringTextField).assert(hasText(TranslationsMock.todayTranslation.translatedString))
            onNodeWithTag(TestTransLangTextField).assert(hasText(TranslationsMock.todayTranslation.translatedLanguage))

            // clear values
            onNodeWithTag(TestOrigLangTextField).performTextClearance()
            onNodeWithTag(TestOrigStringTextField).performTextClearance()
            onNodeWithTag(TestTransStringTextField).performTextClearance()
            onNodeWithTag(TestTransLangTextField).performTextClearance()
            waitForIdle()
            Thread.sleep(1000)


            Espresso.closeSoftKeyboard()
            onNodeWithTag(TestContent).performScrollToNode(hasTestTag(TestSaveButton))

            // will not save and errors will display
            onNodeWithTag(TestSaveButton).performClick()
            waitForIdle()
            Thread.sleep(1000)

            // navigation does not occur
            val currentRoute = navController.currentBackStackEntry?.destination?.route
            Assert.assertEquals(TranslationEditScreenDestination.route, currentRoute)

        }
    }
}